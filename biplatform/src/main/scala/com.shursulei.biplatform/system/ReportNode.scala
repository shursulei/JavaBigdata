package com.shursulei.biplatform.system

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props, Stash}
import akka.persistence._
import com.shursulei.biplatform.model.ReportQuery
import com.shursulei.biplatform.system.ReportManager.WatchMe
import com.shursulei.biplatform.common.api.Response.error

class ReportNode(var saved: Boolean = false, var published: Boolean = false, queryRouteNode: ActorRef)
    extends SleepAbleNode with Stash {

  import ReportNode._

  implicit val system: ActorSystem = context.system

  private val id = UUID.fromString(persistenceId)

  var reportQuery: Option[ReportQuery] = None

  override val sleepAfter: Int = durationSettings("designer.report.sleep-after")

  override def preStart(): Unit = {
    super.preStart()
    context.parent ! WatchMe(id, self)
  }

  override def receiveRecover: Receive = {
    case SnapshotOffer(metadata, snapshot: ReportQuery) ⇒
      log.info("Recover from snapshot")
      lastSnapshot = Some(metadata)
      reportQuery = Some(snapshot)
  }

  def save(): Unit = {
    log.info("Saving snapshot")
    saveSnapshot(reportQuery.get)
  }

  def delete(): Unit = {
    log.info("delete snapshot ")
    deleteSnapshots(SnapshotSelectionCriteria.Latest)
  }

  def markSaved(): Unit = {
    if (!saved) {
      saved = true
      context.parent ! ReportManager.Saved(id)
    }
  }

  def markPublished(): Unit = {
    if (!published) {
      published = true
      context.parent ! ReportManager.Published(id)
    }
  }

  override def receiveCommand: Receive = {
    case Execute(_, _) ⇒
      if (reportQuery.isEmpty) {
        sender() ! error("未定义维度和指标")
      }
      else {
        log.info("start execute")
        queryRouteNode forward reportQuery.get
        sleep()
      }

    case Publish(_, _, query) ⇒
      reportQuery = Some(query)
      save()
      sleep()
      context.become(waitToSaveReply(sender(), publishing = true))

    case Save(_, _, query) ⇒
      reportQuery = Some(query)
      save()
      sleep()
      context.become(waitToSaveReply(sender()))

    case Show(_, _) ⇒
      sender() ! reportQuery
      sleep()

    case Clear(_, _) ⇒
      delete()
      sleep()
      sender() ! DeleteSucceed

    case Sleep ⇒
      stop()

    case Delete ⇒
      deleteOldSnapshots(stopping = true)
      stop()
  }

  def waitToSaveReply(replyTo: ActorRef, publishing: Boolean = false): Receive = {
    case SaveSnapshotSuccess(metadata) ⇒
      log.info("Snapshot save successfully")
      lastSnapshot = Some(metadata)
      deleteOldSnapshots()
      context.unbecome()
      unstashAll()
      if (publishing) {
        markPublished()
      }
      else {
        markSaved()
      }
      replyTo ! SaveSucceed

    case SaveSnapshotFailure(metadata, cause) ⇒
      val msg = s"Snapshot not saved: ${cause.getMessage}"
      log.error(cause, msg)
      context.unbecome()
      unstashAll()
      replyTo ! SaveFailure(msg)

    case msg ⇒
      stash()
  }

  def waitToDeleteReply(replyTo: ActorRef): Receive = {
    case DeleteSnapshotsSuccess(criteria) ⇒
      log.info("Snapshot delete  successfully")
      reportQuery = None
      lastSnapshot = None
      context.unbecome()
      unstashAll()
      replyTo ! DeleteSucceed

    case DeleteSnapshotFailure(metadata, t) ⇒
      log.info("Snapshot delete failure")
      context.unbecome()
      unstashAll()
      replyTo ! DeleteFailure(t.getMessage)
    case msg ⇒
      stash()
  }

}

object ReportNode {

  def props(saved: Boolean, published: Boolean, queryRouteNode: ActorRef) = {
    Props(classOf[ReportNode], saved, published, queryRouteNode)
  }

  def name(reportId: UUID): String = reportId.toString

  trait Command extends ReportManager.Command {
    val reportId: UUID
  }

  case class Execute(login: String, reportId: UUID) extends Command

  case class Save(login: String, reportId: UUID, query: ReportQuery) extends Command

  case class Publish(login: String, reportId: UUID, query: ReportQuery) extends Command

  case class Show(login: String, reportId: UUID) extends Command

  case class Clear(login: String, reportId: UUID) extends Command

  case object SaveSucceed

  case class SaveFailure(msg: String)

  case object DeleteSucceed

  case class DeleteFailure(msg: String)
}
