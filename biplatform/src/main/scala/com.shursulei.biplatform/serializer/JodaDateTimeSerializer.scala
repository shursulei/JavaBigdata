package com.shursulei.biplatform.serializer

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import org.joda.time.{DateTime, DateTimeZone}

/**
 * @author souo
 */
class JodaDateTimeSerializer extends Serializer[DateTime] {
  override def write(kryo: Kryo, output: Output, t: DateTime): Unit = {
    output.writeLong(t.getMillis, true)
    output.writeString(t.getZone.getID)
  }

  override def read(kryo: Kryo, input: Input, aClass: Class[DateTime]): DateTime = {
    val millis = input.readLong(true)
    val zone = DateTimeZone.forID(input.readString())
    new DateTime(millis, zone)
  }
}
