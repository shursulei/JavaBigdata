package akka.stream.jdbc

import com.shursulei.biplatform.model.JdbcSource
import com.shursulei.biplatform.queryrouter.DataRow

/**
 * @author shursulei
 */
case class JdbcSettings(
  source:  JdbcSource,
  sql:     String,
  metaRow: DataRow
)
