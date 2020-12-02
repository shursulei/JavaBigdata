package com.shursulei.biplatform.queryrouter

import com.shursulei.biplatform.queryrouter.DataCellType.DataCellType

/**
 * @author souo
 */
case class DataCell(
  value:      String,
  `type`:     DataCellType,
  properties: Map[String, String]
)

case class DataRow(cells: List[DataCell])

object DataCellType extends Enumeration {
  type DataCellType = Value
  val ROW_HEADER_HEADER, ROW_HEADER, COLUMN_HEADER, DATA_CELL = Value
}

