package com.shursulei.casetest

object Exprmain {
  def main(args: Array[String]): Unit = {
    val v=Var("X")
    println(v)
    val op=BinOp("+",Number(1),v)
    println(op)
    println(v.name)
    println(op.left)
  }
  def simplifyTop(expr: Expr):Expr=expr match {
    case UnOp("-",UnOp("-",e)) =>e //双重负号
    case BinOp("+",e,Number(0))=>e //加0
    case BinOp("*",e,Number(1))=>e //乘1
    case _ => expr
  }
}
