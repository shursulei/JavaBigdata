package com.shursulei.biplatform.model

import io.swagger.models.{Contact ⇒ SwaggerContact, Info ⇒ SwaggerInfo, License ⇒ SwaggerLicense}

import scala.collection.JavaConverters._
import scala.language.implicitConversions

/**
 * @author souo
 */
object Swagger {
  case class Contact(name: String, url: String, email: String)

  case class License(name: String, url: String)

  case class Info(
    description:      String              = "",
    version:          String              = "",
    title:            String              = "",
    termsOfService:   String              = "",
    contact:          Option[Contact]     = None,
    license:          Option[License]     = None,
    vendorExtensions: Map[String, Object] = Map()
  )

  implicit def swagger2scala(convertMe: SwaggerContact): Option[Contact] = {
    if (convertMe == null) None else Some(Contact(convertMe.getName, convertMe.getUrl, convertMe.getEmail))
  }
  implicit def scala2swagger(convertMe: Contact): SwaggerContact = {
    if (convertMe == null) {
      null
    }
    else {
      new SwaggerContact()
        .name(convertMe.name)
        .url(convertMe.url)
        .email(convertMe.email)
    }
  }

  implicit def swagger2scala(convertMe: SwaggerLicense): Option[License] = {
    if (convertMe == null) None else Some(License(convertMe.getName, convertMe.getUrl))
  }

  implicit def scala2swagger(convertMe: License): SwaggerLicense = {
    if (convertMe == null) {
      null
    }
    else {
      new SwaggerLicense()
        .name(convertMe.name)
        .url(convertMe.url)
    }
  }

  implicit def swagger2scala(convertMe: SwaggerInfo): Info = {
    Info(
      convertMe.getDescription,
      convertMe.getVersion,
      convertMe.getTitle,
      convertMe.getTermsOfService,
      convertMe.getContact,
      convertMe.getLicense,
      convertMe.getVendorExtensions.asScala.toMap
    )
  }

  implicit def scala2swagger(convertMe: Info): SwaggerInfo = {
    val ret = new SwaggerInfo()
      .description(convertMe.description)
      .version(convertMe.version)
      .title(convertMe.title)
      .termsOfService(convertMe.termsOfService)
      .contact(convertMe.contact.getOrElse(null))
      .license(convertMe.license.getOrElse(null))

    ret.getVendorExtensions.putAll(convertMe.vendorExtensions.asJava)
    ret
  }

}
