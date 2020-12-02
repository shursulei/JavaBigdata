package com.shursulei.traittest

/**
  * trait增加了行为
  */
trait Doubing extends IntQueue{
  abstract override def put(x: Int): Unit = {super.put(2*x)}
}
trait Incrementing extends IntQueue{
  abstract override def put(x: Int): Unit = {super.put(x+1)}
}
trait Filtering extends IntQueue{
  abstract override def put(x: Int): Unit ={
    if(x>0) super.put(x)
  }
}