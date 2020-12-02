package com.shursulei.biplatform.serializer

import com.esotericsoftware.kryo.Kryo
import org.joda.time.DateTime

/**
 * @author souo
 */
class KryoSerializerInit {

  def customize(kryo: Kryo): Unit = {
    kryo.register(classOf[DateTime], new JodaDateTimeSerializer)
    kryo.register(classOf[Some[_]], new SomeSerializer)
    kryo.setReferences(false)
  }

}
