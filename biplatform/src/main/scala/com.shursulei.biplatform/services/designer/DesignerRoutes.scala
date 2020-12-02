package com.shursulei.biplatform.services.designer

import akka.http.scaladsl.server.Directives._

/**
 * @author souo
 */
trait DesignerRoutes extends ReportsRoutes
    with ExecuteRoutes
    with DownLoadRoutes
    with DashBoardRoutes {

  val designerRoutes = pathPrefix("designer") {
    reportsRoutes ~
      dashBoardRoutes ~
      executeRoutes ~
      downLoadRoutes
  }
}
