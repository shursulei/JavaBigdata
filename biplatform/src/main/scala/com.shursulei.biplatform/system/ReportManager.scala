package com.shursulei.biplatform.system

import java.util.UUID

import akka.actor.{ActorRef, Props, Terminated}
import akka.persistence.{RecoveryCompleted, SaveSnapshotFailure, SaveSnapshotSuccess, SnapshotOffer}
import org.joda.time.DateTime
import ReportManager._
import com.shursulei.biplatform.model.ReportMeta
import cats.syntax.either._

/**
 * @author shursulei
 */
class ReportManager(queryRouteNode: ActorRef) extends Node {

  var db: SnapShot = emptySnapShot

  var activeNodes: Map[UUID, ActorRef] = Map.empty[UUID, ActorRef]

  var snapshotEvery: Int = 30
  var receiveCmdCount = 0

  def save(): Unit = {
    receiveCmdCount += 1
    if (receiveCmdCount > snapshotEvery) {
      log.info("Saving snapshot")
      saveSnapshot(db)
    }
  }

  override def receiveRecover: Receive = {
    case evt: Event ⇒
      log.debug("receiveRecover evt " + evt)
      updateState(evt)

    case SnapshotOffer(metadata, snapshot: SnapShot) ⇒
      log.info("Recover from snapshot")
      lastSnapshot = Some(metadata)
      db = snapshot

    case RecoveryCompleted ⇒
      log.info("Recover completed for {} in {}ms", persistenceId, System.currentTimeMillis() - created)
  }

  private def updateState(evt: Event) = evt match {
    case ReportCreated(meta) ⇒
      db :+= meta
    case ReportUpdated(meta) ⇒
      db = db.updated(db.indexWhere(_.id == meta.id), meta)
    case ReportDropped(id) ⇒
      db = db.filterNot(_.id == id)
    case Published(id) ⇒
      val index = db.indexWhere(_.id == id)
      db = db.updated(index, db(index).copy(saved = true, published = true))
    case Saved(id) ⇒
      val index = db.indexWhere(_.id == id)
      db = db.updated(index, db(index).copy(saved = true))
  }

  private def updateAndReply(evt: Event)(replyTo: ActorRef) = {
    val res = Either.catchNonFatal{
      updateState(evt)
      db
    }
    replyTo ! res
  }

  override def receiveCommand: Receive = {
    case CreateReport(login, reportName) ⇒
      val meta = ReportMeta(
        id            = UUID.randomUUID(),
        name          = reportName,
        createBy      = login,
        createTime    = DateTime.now(),
        modifyBy      = None,
        latModifyTime = None,
        saved         = false,
        published     = false
      )
      val replyTo = sender()
      persistAsync(ReportCreated(meta)) { evt ⇒
        updateAndReply(evt)(replyTo)
        save()
      }

    case UpdateReport(login, reportId, reportName) ⇒
      db.find(_.id == reportId) match {
        case Some(meta) ⇒
          val newMeta = meta.copy(name = reportName, modifyBy = Some(login), latModifyTime = Some(DateTime.now()))
          val replyTo = sender()
          persistAsync(ReportUpdated(newMeta)) { evt ⇒
            updateAndReply(evt)(replyTo)
            save()
          }
        case None ⇒
          sender() ! Left(new RuntimeException(s"No Such report reportId:$reportId "))
      }

    case DropReport(_, id) ⇒
      val replyTo = sender()
      db.find(_.id == id) match {
        case Some(meta) ⇒
          persistAsync(ReportDropped(id)) { evt ⇒
            updateAndReply(evt)(replyTo)
            if (meta.saved) {
              reportNode(meta.id).foreach (_ ! Delete)
            }
          }
        case None ⇒
          sender() ! Left(new RuntimeException(s"No Such report reportId:$id "))
      }

    case ListAllReport(_, published) ⇒
      val res = if (published) db.filter(_.published); else db
      sender() ! res

    case WatchMe(id, ref) ⇒
      log.info("Watching actor {}", ref)
      context.watch(ref)
      activeNodes += id → ref

    case Terminated(ref) ⇒
      log.info("Actor {} terminated", ref)
      val id = UUID.fromString(ref.path.name)
      activeNodes = activeNodes.filterKeys(_ != id)
      if (activeNodes.isEmpty) {
        allSoulsReaped()
      }

    case published @ Published(id) ⇒
      log.info(s"the report $id published")
      persistAsync(published) {
        updateState
      }

    case saved @ Saved(id) ⇒
      log.info(s"the report $id saved")
      persistAsync(saved){
        updateState
      }

    case SaveSnapshotSuccess(metadata) ⇒
      log.info("Snapshot save successfully")
      deleteMessages(metadata.sequenceNr - 1)
      lastSnapshot = Some(metadata)
      deleteOldSnapshots()
      receiveCmdCount = 0

    case SaveSnapshotFailure(_, cause) ⇒
      log.error(cause, s"Snapshot not saved: ${cause.getMessage}")

    case command: ReportNode.Command ⇒
      reportNode(command.reportId) match {
        case Some(node) ⇒
          node forward command
        case None ⇒
          sender() ! Left(new RuntimeException(s"No Such report reportId:${command.reportId}"))
      }
  }

  private def reportNode(id: UUID): Option[ActorRef] = {
    activeNodes.get(id) orElse {
      db.find(_.id == id).map{ meta ⇒
        create(meta.id, meta.saved, meta.published)
      }
    }
  }

  private def create(id: UUID, saved: Boolean, published: Boolean): ActorRef = {
    context.actorOf(
      ReportNode.props(saved, published, queryRouteNode),
      ReportNode.name(id)
    )
  }

  def allSoulsReaped(): Unit = {
    log.warning(s"all report node manager by ${self.path} have terminated")
    stop()
  }

}

object ReportManager {

  def props(queryRouteNode: ActorRef) = Props(classOf[ReportManager], queryRouteNode)

  def name(login: String) = s"$login-rm"

  //cmd
  trait Command extends UserNode.Command

  case class CreateReport(login: String, reportName: String) extends Command

  case class UpdateReport(login: String, reportId: UUID, reportName: String) extends Command

  case class DropReport(login: String, reportId: UUID) extends Command

  case class ListAllReport(login: String, published: Boolean = false) extends Command

  //evt
  sealed trait Event

  case class ReportCreated(meta: ReportMeta) extends Event

  case class ReportUpdated(reportMeta: ReportMeta) extends Event

  case class ReportDropped(reportId: UUID) extends Event

  case class Published(reportId: UUID) extends Event

  case class Saved(reportId: UUID) extends Event

  case class WatchMe(id: UUID, ref: ActorRef)

  type SnapShot = Array[ReportMeta]

  val emptySnapShot: SnapShot = Array.empty[ReportMeta]
}
