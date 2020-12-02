package com.shursulei.biplatform

import akka.http.scaladsl.server.Directives._
import com.shursulei.biplatform.common.api.RoutesRequestWrapper
import com.shursulei.biplatform.services.cube.CubeRoutes
import com.shursulei.biplatform.services.designer.DesignerRoutes
import com.shursulei.biplatform.services.ds.DataSourceRoutes
import com.shursulei.biplatform.services.user.UsersRouters

/**
 * @author shursulei
 */
trait Routes extends RoutesRequestWrapper
    with UsersRouters
    with CubeRoutes
    with DataSourceRoutes
    with DesignerRoutes {

  lazy val api = requestWrapper {
    pathPrefix("api") {
      usersRoutes ~
        cubeRoutes ~
        dsRoutes ~
        designerRoutes
    }
  }
}
