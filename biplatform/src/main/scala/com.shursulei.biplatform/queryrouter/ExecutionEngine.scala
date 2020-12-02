package com.shursulei.biplatform.queryrouter

import akka.NotUsed
import akka.stream.{Graph, SourceShape}
import com.shursulei.biplatform.model.QueryModel

/**
 * @author shursulei
 */
trait ExecutionEngine {
  def execute(query: QueryModel): Graph[SourceShape[DataRow], NotUsed]
}
