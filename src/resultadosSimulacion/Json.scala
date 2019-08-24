
package resultadosSimulacion

import java.io.{File, PrintWriter}

import net.liftweb.json._
import net.liftweb.json.JObject
import net.liftweb.json.JsonDSL._

import scala.io.Source


object Json {

  val archivoResultadosJson = new PrintWriter(new File("src/resultados.json"))

  implicit val formats = DefaultFormats
  val stringJson = Source.fromFile(new File("src/parametros.json")).getLines().mkString
  val json = parse(stringJson)

  def escribirResultados(resultados: ResultadosSimulacion): Unit = {

    val objetoJson: JObject = ("resultadosSimulacion" ->
      ("vehiculos" ->
        ("total" -> resultados.totalVehiculos) ~
          ("carros" -> resultados.totalCarros) ~
          ("motos" -> resultados.totalMotos) ~
          ("buses" -> resultados.totalBuses) ~
          ("camiones" -> resultados.totalCamiones) ~
          ("motoTaxis" -> resultados.totalMotoTaxis)
        ) ~
        ("mallaVial" ->
          ("vias" -> resultados.totalVias) ~
            ("intersecciones" -> resultados.totalIntersecciones) ~
            ("viasUnSentido" -> resultados.viasUnSentido) ~
            ("viasDobleSentido" -> resultados.viasDobleSentido) ~
            ("velocidadMinima" -> resultados.velocidadMinimaVias) ~
            ("velocidadMaxima" -> resultados.velocidadMaximaVias) ~
            ("longitudPromedio" -> resultados.longitudPromedio) ~
            ("vehiculosEnInterseccion" ->
              ("promedioOrigen" -> resultados.promedioOrigen) ~
                ("promedioDestino" -> resultados.promedioDestino) ~
                ("sinOrigen" -> resultados.sinOrigen) ~
                ("sinDestino" -> resultados.sinDestino)
              )
          )) ~
      ("tiempos" ->
        ("simulacion" -> resultados.tiempoSimulado) ~
          ("realidad" -> resultados.tiempoReal)
        ) ~
      ("velocidades" ->
        ("minima" -> resultados.velocidadMinima) ~
          ("maxima" -> resultados.velocidadMaxima) ~
          ("promedio" -> resultados.velocidadPromedio)) ~
      ("distancias" ->
        ("minima" -> resultados.distanciaMinima) ~
          ("maxima" -> resultados.distanciaMaxima) ~
          ("promedio" -> resultados.distanciaPromedio))

    archivoResultadosJson.write(prettyRender(objetoJson))
    archivoResultadosJson.close()
  }

  def tiempoDt: Double = (json \ "pametrosSimulacion" \ "dt").extract[Double]

  def tiempoDormir: Long = (json \ "pametrosSimulacion" \ "tRefresh").extract[Long]

  def vehiculosMinimo: Int = (json \ "pametrosSimulacion" \ "vehiculos" \ "minimo").extract[Int]

  def vehiculosMaximo: Int = (json \ "pametrosSimulacion" \ "vehiculos" \ "maximo").extract[Int]

  def velocidadMinima: Int = (json \ "pametrosSimulacion" \ "velocidad" \ "minimo").extract[Int]

  def velocidadMaxima: Int = (json \ "pametrosSimulacion" \ "velocidad" \ "maximo").extract[Int]

  def proporcionCarros: Double = (json \ "pametrosSimulacion" \ "proporciones" \ "carros").extract[Double]

  def proporcionMotos: Double = (json \ "pametrosSimulacion" \ "proporciones" \ "motos").extract[Double]

  def proporcionBuses: Double = (json \ "pametrosSimulacion" \ "proporciones" \ "buses").extract[Double]

  def proporcionCamiones: Double = (json \ "pametrosSimulacion" \ "proporciones" \ "camiones").extract[Double]

  def proporcionMotoTaxis: Double = (json \ "pametrosSimulacion" \ "proporciones" \ "motoTaxis").extract[Double]
}
