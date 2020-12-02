package com.shursulei.xmltest

object CCThremMain {
  def main(args: Array[String]): Unit = {
    val therm=new CCThrem {
        val description: String = "hot dog #5"
        val yearMade: Int = 1952
        val dateObtained: String = "March 14,2006"
        val bookPrice: Int = 2199
        val purchasePrice: Int = 600
        val condition: Int = 9
    }
    println(therm.toXML)
  }
}
