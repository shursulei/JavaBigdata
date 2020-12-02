package com.shursulei.biplatform.tools

import com.shursulei.biplatform.common.sql.{DatabaseConfig, SqlDatabase}
import com.typesafe.config.{Config, ConfigFactory}

object H2ShellConsole extends App {
  val config = new DatabaseConfig {
    def rootConfig: Config = ConfigFactory.load()
  }

  println("Note: when selecting from tables, enclose the table name in \" \".")
  new org.h2.tools.Shell().runTool("-url", SqlDatabase.embeddedConnectionStringFromConfig(config))
}
