package com.shursulei.biplatform.system

import java.util.UUID

import akka.actor.Props
import akka.persistence.RecoveryCompleted
import DataSourceNode._
import com.shursulei.biplatform.model.{DataSource, DsServer}
import cats.syntax.either._

/**
 * @author shursulei
 */
class DataSourceNode extends Node {
  var items = Items()

  def updateItems(evt: Event) = evt match {
    case Added(item) ⇒
      items = items.add(item)
    case Removed(dsId) ⇒
      items = items.remove(dsId)
  }

  override def receiveRecover: Receive = {
    case evt: Event ⇒
      updateItems(evt)
    case RecoveryCompleted ⇒
      log.info("Recover completed for {} in {}ms", persistenceId, System.currentTimeMillis() - created)
  }

  override def receiveCommand: Receive = {
    case Add(name, ds) ⇒
      val dsId = UUID.randomUUID()
      val item = Item(dsId, name, ds)
      val replyTo = sender()
      persistAsync(Added(item)){ evt ⇒
        updateItems(evt)
        replyTo ! item
      }

    case Remove(id) ⇒
      val res = Either.fromOption[Throwable, Item](
        items.items.find(_.dsId == id),
        new RuntimeException(s"no such datasource $id")
      ).map{ item ⇒
          persistAsync(Removed(id)){ evt ⇒
            updateItems(evt)
          }
        }
      sender() ! res

    case ListAllDS ⇒
      sender() ! items

    case ListAllTables(id) ⇒
      val res = Either.fromOption[Throwable, Item](
        items.items.find(_.dsId == id),
        new RuntimeException(s"no such datasource $id")
      )
        .flatMap{ item ⇒
          DsServer(item.dataSource).listAllTables()
        }
      sender() ! res

    case ListAllColumns(id, table) ⇒
      val res = Either.fromOption[Throwable, Item](
        items.items.find(_.dsId == id),
        new RuntimeException(s"no such datasource $id")
      ).flatMap{ item ⇒
          DsServer(item.dataSource).listAllColumns(table)
        }
      sender() ! res
    case Get(id) ⇒
      sender() ! items.items.find(_.dsId == id)
  }
}

object DataSourceNode {

  def props = Props(classOf[DataSourceNode])
  val name = "datasource"

  case class Item(dsId: UUID, name: String, dataSource: DataSource)
  case class Items(items: List[Item] = List.empty) {
    def add(item: Item) = {
      copy(items = items :+ item)
    }

    def remove(dsId: UUID) = {
      copy(items = items.filterNot(_.dsId == dsId))
    }
  }

  trait Command
  case class Add(name: String, dataSource: DataSource) extends Command
  case class Remove(dsId: UUID) extends Command

  trait Event
  case class Added(item: Item) extends Event
  case class Removed(dsId: UUID) extends Event

  case object ListAllDS
  case class ListAllTables(id: UUID)
  case class ListAllColumns(id: UUID, table: String)
  case class Get(dsId: UUID)

}
