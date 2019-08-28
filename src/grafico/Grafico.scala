
package grafico

import java.awt.event.{KeyEvent, KeyListener}
import java.awt.{BasicStroke, Color, Font}

import infraestructura.Interseccion
import infraestructura.via.Via
import org.jfree.chart.annotations.XYTextAnnotation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.{ChartFactory, ChartFrame, JFreeChart}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.jfree.util.ShapeUtilities
import simulacion.Simulacion
import vehiculo.{Bus, Camion, Carro, Moto, MotoTaxi, Vehiculo, VehiculoViaje}
import infraestructura.CamaraFotoDeteccion
import java.awt.Rectangle

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


object Grafico{

  //---------- Aqui se almacenan todas las series del gráfico
  private var _coleccionSeries: XYSeriesCollection = new XYSeriesCollection()

  def coleccionSeries: XYSeriesCollection = _coleccionSeries
  def coleccionSeries_=(coleccionSeries: XYSeriesCollection): Unit = _coleccionSeries = coleccionSeries


  //--- Este objeto define el tipo de gráfica que se utilizará
  //--- En este caso, es una gráfica del tipo ScatterPlot
  val grafica: JFreeChart = ChartFactory.createScatterPlot(
    "Simulación Tráfico de Medellín",
    "",
    "",
    coleccionSeries)

  grafica.removeLegend() // Se remueve las leyendas del gráfico que aparecen en la parte inferior


  //--- Representa la ventana del gráfico
  val marcoGrafica: ChartFrame = new ChartFrame("Simulación Scala", grafica)


  //--- Representa el plano cartesiano de la gráfica
  val trazadoGrafica: XYPlot = grafica.getXYPlot

  trazadoGrafica.getRangeAxis().setVisible(false) // Se remueve el eje Y
  trazadoGrafica.getDomainAxis().setVisible(false)  // Se remueve el eje X
  trazadoGrafica.setBackgroundPaint(Color.white)  // Fondo blanco para el gráfico


  //--- Encargado de las configuraciones relativas a la apariencia del grafico
  val renderizador: XYLineAndShapeRenderer = new XYLineAndShapeRenderer(true, false)

  trazadoGrafica.setRenderer(renderizador)


  //--- Almacena las series de las vías
  //--- Cada vía necesita de una serie
  private var _seriesVias: Array[XYSeries] = Array[XYSeries]()

  def seriesVias: Array[XYSeries] = _seriesVias
  def seriesVias_=(seriesVias: Array[XYSeries]): Unit = _seriesVias = seriesVias


  //--- Almacena las series de los vehiculos
  private var _seriesVehiculos: ArrayBuffer[XYSeries] = ArrayBuffer[XYSeries]()

  def seriesVehiculos: ArrayBuffer[XYSeries] = _seriesVehiculos
  def seriesVehiculos_=(seriesVehiculos: ArrayBuffer[XYSeries]): Unit = _seriesVehiculos = seriesVehiculos


  //---------- Creación de los colores de las intersecciones
  private var _coloresIntersecciones: Map[Interseccion, Color] = Map.empty[Interseccion, Color]

  def coloresIntersecciones: Map[Interseccion, Color] = _coloresIntersecciones
  def coloresIntersecciones_=(coloresIntersecciones: Map[Interseccion, Color]): Unit =
    _coloresIntersecciones = coloresIntersecciones

  def crearColoresIntersecciones(intersecciones: Array[Interseccion]): Unit = {
    intersecciones.foreach(interseccion => coloresIntersecciones +=
      (interseccion -> new Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))))
  }


  //---------- Eventos del teclado ----------
  marcoGrafica.addKeyListener(new KeyListener {

    override def keyTyped(keyEvent: KeyEvent): Unit = {}
    override def keyReleased(keyEvent: KeyEvent): Unit = {}

    override def keyPressed(keyEvent: KeyEvent): Unit = {

      val key : Int = keyEvent.getKeyCode

      if (key == KeyEvent.VK_F5){
        if(seriesVehiculos.nonEmpty) eliminarSeriesVehiculos()
        Simulacion.nuevaAnimacion()
      }

      else if (key == KeyEvent.VK_F1) Simulacion.continuarAnimacion()

      else if (key == KeyEvent.VK_F2) Simulacion.pausarAnimacion()
    }
  })


  //---------- Se crean las vías y los nombres de las intersecciones
  //---------- Solo se ejecuta una vez cuando se corre el programa
  def dibujarMapa(vias: Array[Via], intersecciones: Array[Interseccion]): Unit = {

    val lineaParametro = new BasicStroke(2.5f)

    seriesVias = vias.map(via => crearSerieVia(via))

    seriesVias.foreach(serieVia => coleccionSeries.addSeries(serieVia))

    vias.foreach(via => cambiarAparienciaVia(vias.indexOf(via)))

    crearColoresIntersecciones(intersecciones)

    intersecciones.foreach(interseccion => crearEtiquetaInterseccion(interseccion))

    marcoGrafica.setSize(1200, 700) // Tamaño de la ventana del gráfico
    marcoGrafica.setVisible(true) // Activa la visualización del gráfico

    /**  Recibe una via y retorna una serie de la respectiva vía.
      *  Para la creación del val SerieVia, se requiere un objeto tipo Comparable como argumento del constructor,
      *  dicho objeto está dado por el nombreIdentificador de la vía.
      */
    def crearSerieVia(via: Via): XYSeries = {
      val serieVia: XYSeries = new XYSeries(via.nombreIdentificador().asInstanceOf[Comparable[String]])

      serieVia.add(via.origen.x, via.origen.y)
      serieVia.add(via.fin.x, via.fin.y)
      serieVia
    }

    def cambiarAparienciaVia(indexVia: Int): Unit = {
      renderizador.setSeriesPaint(indexVia, Color.gray)
      renderizador.setSeriesStroke(indexVia, lineaParametro)  // Configura el grosor de la vía
      renderizador.setSeriesShapesVisible(indexVia, false)  // Desactiva las figuras al inicio y fin de la vía
      renderizador.setSeriesLinesVisible(indexVia, true)  // Activa la línea entre el inicio y final de la vía
    }

    def crearEtiquetaInterseccion(interseccion: Interseccion): Unit = {
      var aumentoPosicionY: Int = 170
      if ((interseccion.nombre.get == "Boliv con 65") ||
          (interseccion.nombre.get == "M. Laura Auto") ||
          (interseccion.nombre.get == "Exito Rob") ||
          (interseccion.nombre.get == "Col 80") ||
          (interseccion.nombre.get == "Juan 80") ||
          (interseccion.nombre.get == "St Gema") ||
          (interseccion.nombre.get == "30 con 80")){
        aumentoPosicionY = -170
      }

      val etiqueta: XYTextAnnotation = new XYTextAnnotation(
        interseccion.nombre.get,
        interseccion.x,
        interseccion.y + aumentoPosicionY
      )
      etiqueta.setPaint(coloresIntersecciones(interseccion))
      etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 9))
      trazadoGrafica.addAnnotation(etiqueta)
    }
  }


  def dibujarCamaras(camaras: ArrayBuffer[CamaraFotoDeteccion]): Unit = {

    var x: String = "1"

    camaras.foreach(camara => {
      val graficoCamara: XYSeries = new XYSeries(x)
      coleccionSeries.addSeries(graficoCamara)
      graficoCamara.add(camara.posicion.x, camara.posicion.y)
      renderizador.setSeriesShape(coleccionSeries.indexOf(graficoCamara), ShapeUtilities.createDownTriangle(6))
      renderizador.setSeriesPaint(coleccionSeries.indexOf(graficoCamara), Color.decode("#0026ff"))
      renderizador.setSeriesShapesVisible(coleccionSeries.indexOf(graficoCamara), true)
      renderizador.setSeriesLinesVisible(coleccionSeries.indexOf(graficoCamara), false)
      x += "1"
    })
  }


  def dibujarVehiculos(viajesVehiculos: Array[VehiculoViaje]): Unit = {
    viajesVehiculos.foreach(vehiculoViaje => crearRepresentacionVehiculo(vehiculoViaje))

    def crearRepresentacionVehiculo(vehiculoViaje: VehiculoViaje): Unit = {

      val vehiculo: Vehiculo = vehiculoViaje.vehiculo
      val serieVehiculo: XYSeries = new XYSeries(vehiculo.placa)
      serieVehiculo.add(vehiculo.posicion.x, vehiculo.posicion.y)

      coleccionSeries.addSeries(serieVehiculo)
      seriesVehiculos += serieVehiculo

      renderizador.setSeriesPaint(coleccionSeries.indexOf(serieVehiculo), coloresIntersecciones(vehiculoViaje.destino))
      renderizador.setSeriesLinesVisible(coleccionSeries.indexOf(serieVehiculo), false)
      renderizador.setSeriesShapesVisible(coleccionSeries.indexOf(serieVehiculo), true)

      vehiculo match {
        case _:Carro =>
          renderizador.setSeriesShape(
            coleccionSeries.indexOf(serieVehiculo),
            ShapeUtilities.createDiagonalCross(3, 1)
          )
        case _:Moto =>
          renderizador.setSeriesShape(
            coleccionSeries.indexOf(serieVehiculo),
            ShapeUtilities.createDiamond(4)
          )
        case _:Bus =>
          renderizador.setSeriesShape(
            coleccionSeries.indexOf(serieVehiculo),
            ShapeUtilities.createDownTriangle(3)
          )
        case _:MotoTaxi =>
          renderizador.setSeriesShape(
            coleccionSeries.indexOf(serieVehiculo),
            ShapeUtilities.createRegularCross(3, 1)
          )
        case _:Camion =>
          renderizador.setSeriesShape(
            coleccionSeries.indexOf(serieVehiculo),
            ShapeUtilities.createDownTriangle(4)
          )
      }
    }
  }


  //---------- Se grafican los vehiculos
  //---------- Se ejecuta por cada iteración del método run en el object Simulacion
  def graficarVehiculos(viajesVehiculos: Array[VehiculoViaje]): Unit = {
    viajesVehiculos.foreach(vehiculoViaje => {
      val vehiculo: XYSeries = coleccionSeries.getSeries(vehiculoViaje.vehiculo.placa)
      vehiculo.clear()
      vehiculo.add(vehiculoViaje.vehiculo.posicion.x, vehiculoViaje.vehiculo.posicion.y)
    })
  }


  def eliminarSeriesVehiculos(): Unit = {
    seriesVehiculos.foreach(serie => coleccionSeries.removeSeries(serie))
    seriesVehiculos.clear()
  }
}