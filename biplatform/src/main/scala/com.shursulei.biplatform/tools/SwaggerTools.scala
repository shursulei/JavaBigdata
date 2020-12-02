package com.shursulei.biplatform.tools

import java.io.File
import java.nio.file.Paths

import com.shursulei.biplatform.model.Swagger.{Contact, Info, License}
import com.shursulei.biplatform.services.user.UsersRouters
import io.github.swagger2markup.{GroupBy, Language, Swagger2MarkupConfig}
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder
import io.github.swagger2markup.markup.builder.MarkupLanguage
import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.ReaderConfig
import io.swagger.models.auth.SecuritySchemeDefinition
import io.swagger.models.{ExternalDocs, Scheme, Swagger}
import io.swagger.util.Json
import org.apache.commons.lang3.StringUtils

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe._
import io.github.swagger2markup.Swagger2MarkupConverter

/**
 * gen md from swagger.json
 * @author shursulei
 */
object SwaggerTools {
  val readerConfig = new ReaderConfig {
    def getIgnoredRoutes: java.util.Collection[String] = List.empty[String].asJavaCollection

    def isScanAllResources: Boolean = false
  }

  def toJavaTypeSet(apiTypes: Seq[Type]): Set[Class[_]] = {
    apiTypes.map(t ⇒ getClassForType(t)).toSet
  }

  private lazy val mirror = scala.reflect.runtime.universe.runtimeMirror(getClass.getClassLoader)

  def getClassForType(t: Type): Class[_] = {
    mirror.runtimeClass(t.typeSymbol.asClass)
  }

  def removeInitialSlashIfNecessary(path: String): String =
    if (path.startsWith("/")) removeInitialSlashIfNecessary(path.substring(1)) else path

  def prependSlashIfNecessary(path: String): String = {
    if (path.startsWith("/")) {
      path
    }
    else {
      s"/$path"
    }
  }

  def swaggerConfig(config: SwaggerConfig): Swagger = {
    import config._
    val modifiedPath = prependSlashIfNecessary(basePath)
    val swagger = new Swagger().basePath(modifiedPath).info(info).scheme(scheme)
    if (StringUtils.isNotBlank(host)) swagger.host(host)
    swagger.setSecurityDefinitions(securitySchemeDefinitions.asJava)
    externalDocs match {
      case Some(ed) ⇒ swagger.externalDocs(ed)
      case None     ⇒ swagger
    }
  }

  def generateSwaggerJson(apiTypes: Seq[Type], path: String)(implicit config: SwaggerConfig): File = {
    val reader = new Reader(swaggerConfig(config), readerConfig)
    val swagger = reader.read(toJavaTypeSet(apiTypes).asJava)
    val file = new File(path)
    val writter = Json.pretty().writeValues(file)
    writter.write(swagger).flush()
    writter.close()
    file
  }

  implicit val config: SwaggerConfig = SwaggerConfig(
    host   = "http:localhost:8080",
    info   = Info(
      description = "biplatform",
      version     = "0.0.1",
      title       = "bitools",
      contact     = Some(
        Contact("shursulei", "", "shursulei.ven@gmail.com")
      ),
      license     = Some(License.apply("Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html"))
    ),
    scheme = Scheme.HTTP
  )

  def testGen(): File = {
    val apiTypes: Seq[Type] = Seq(typeOf[UsersRouters])
    generateSwaggerJson(apiTypes, "swagger.json")
  }

  def toMd(): Unit = {
    val config: Swagger2MarkupConfig = new Swagger2MarkupConfigBuilder()
      .withMarkupLanguage(MarkupLanguage.MARKDOWN)
      .withOutputLanguage(Language.ZH)
      .withPathsGroupedBy(GroupBy.TAGS)
      .build()

    val in = testGen()
    val converter = Swagger2MarkupConverter.from(in.toPath).withConfig(config).build
    converter.toFile(Paths.get("./swagger"))
  }

  def main(args: Array[String]): Unit = {
    toMd()
  }

}

case class SwaggerConfig(
  host:                      String,
  basePath:                  String                                = "/",
  apiDocsPath:               String                                = "",
  info:                      Info,
  scheme:                    Scheme,
  securitySchemeDefinitions: Map[String, SecuritySchemeDefinition] = Map(),
  externalDocs:              Option[ExternalDocs]                  = None
)
