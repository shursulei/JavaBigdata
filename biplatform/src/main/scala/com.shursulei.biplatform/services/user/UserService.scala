package com.shursulei.biplatform.services.user

import com.shursulei.biplatform.model.User

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author shursulei
 */
class UserService(userRuleDao: UserRuleDao)(implicit ec: ExecutionContext) {

  def isAdminUser(user: User): Future[Boolean] = {
    userRuleDao.isAdmin(user.login)
  }

}
