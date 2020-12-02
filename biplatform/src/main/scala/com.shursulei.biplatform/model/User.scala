package com.shursulei.biplatform.model

import io.swagger.annotations.{ApiModel, ApiModelProperty}

import scala.annotation.meta.field

/**
 * @author souo
 */

@ApiModel
case class User(
  @(ApiModelProperty @field)(value = "登录用户名") login:String
)

