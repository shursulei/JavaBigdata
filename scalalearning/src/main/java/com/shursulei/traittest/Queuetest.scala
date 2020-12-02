package com.shursulei.traittest

object Queuetest {
  def main(args: Array[String]): Unit = {
//    val queue=new BasicIntQueue
//    queue.put(10)
//    queue.put(20)
//    println(queue.get())
//    println(queue.get())
//    val queue=new MyQueue
//    queue.put(20)
//    println(queue.get())
//    val queue= new BasicIntQueue with Incrementing
//    queue.put(2)
//    println(queue.get())
      val queue=new BasicIntQueue with Filtering
      queue.put(-1)
      queue.put(2)
      queue.put(0)
      println(queue.get())
    //多个特质的使用方法是，先执行最右边的
  }

}
