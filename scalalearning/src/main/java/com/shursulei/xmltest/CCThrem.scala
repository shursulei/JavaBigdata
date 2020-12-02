package com.shursulei.xmltest

abstract class CCThrem {
  val description:String
  val yearMade:Int
  val dateObtained:String
  val bookPrice:Int
  val purchasePrice:Int
  val condition:Int
  override def toString: String = description
  def toXML =
  <cctherm>
  <description>{description}</description>
    <yearMade>{yearMade}</yearMade>
    <dateObtained>{dateObtained}</dateObtained>
    <bookPrice>{bookPrice}</bookPrice>
    <purchasePrice>{purchasePrice}</purchasePrice>
    <condition>{condition}</condition>
  </cctherm>
}
