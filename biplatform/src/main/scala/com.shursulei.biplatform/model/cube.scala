package com.shursulei.biplatform.model

import java.util.UUID

import org.joda.time.DateTime

/**
 * @author souo
 */
case class Dimension(
  name:        String,
  caption:     String,
  description: Option[String],
  dataType:    String
)

case class Measure(dimension: Dimension, aggregator: String = "SUM")

case class CubeSchema(
  tableId:      String,
  dimensions:   List[Dimension],
  measures:     List[Measure],
  dataSourceId: UUID
)

case class CubeMeta(cubeId: UUID, cubeName: String, createBy: String,
                    modifyBy: Option[String] = None, latModifyTime: DateTime, visible: Boolean = true)

case class Cube(meta: CubeMeta, schema: CubeSchema)

case class Cubes(list: List[Cube]) {

  def add(newCube: Cube) = Cubes(newCube :: list)

  def get(id: UUID): Option[Cube] = {
    list.find(_.meta.cubeId == id)
  }

  def removeCube(cubeId: UUID) = {
    Cubes(list.filterNot(_.meta.cubeId == cubeId))
  }

  def updateCube(cubeId: UUID, cube: Cube) = {
    val newList = cube :: list.filterNot(_.meta.cubeId == cubeId)
    Cubes(newList)
  }

}
object Cubes {
  def apply(args: Cube*): Cubes = Cubes(args.toList)
}

