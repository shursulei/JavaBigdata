package com.shursulei.biplatform.serializer

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}

/**
 * @author souo
 */
class SomeSerializer extends Serializer[Some[_]] {
  override def write(kryo: Kryo, output: Output, t: Some[_]): Unit = {
    kryo.writeClassAndObject(output, t.get)
  }

  override def read(kryo: Kryo, input: Input, aClass: Class[Some[_]]): Some[_] = {
    Some(kryo.readClassAndObject(input))
  }
}
