package com.shursulei.biplatform.common.util

import cats.syntax.either._

/**
 * @author souo
 */
object IntParam {
  def unapply(str: String): Option[Int] = {
    Either.catchNonFatal(str.toInt).toOption
  }
}
