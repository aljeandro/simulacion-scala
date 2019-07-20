
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
import vehiculo.{Bus, Camion, Carro, Moto, MotoTaxi, Vehiculo}


object Grafico{

  /**
    * Esta clase representa el gráfico de la aplicación.
    *
    * Tiene dos métodos principales:
    *
    *   dibujarMapa: este método está encargado de crear las vías vacías con sus respectivos nombres en las
    *   intersecciones. Solo se ejecuta una vez cuando se corre el programa.
    *
    *   graficarVehiculos: este método está encargado de graficar los diferentes tipos de vehiculos. Se ejecuta por cada
    *   iteracion del método run en el object Simulacion.
    *
    * Las variables del object son:
    *
    *   coleccionSeries: en este objeto se alamacenan todas las series del gráfico.
    *
    *   grafica: es un objeto del tipo JFreeChart que define el tipo de gráfica que se utilizará, en este caso es una
    *   gráfica del tipo ScatterPlot.
    *
    *   marcoGrafica: representa la ventana del gráfico, recibe el título que irá en la parte superior izquierda del
    *   gráfico y el objeto grafica creada previamente.
    *
    *   trazadoGrafica: representa el plano cartesiano de la grafica.
    *
    *   renderizador: objeto encargado de las configuraciones relativas a la apariencia del grafico.
    *
    *   seriesVias: Es un array que almacena las series de las vías. Cada vía necesita de una serie.
    */

  var coleccionSeries: XYSeriesCollection = new XYSeriesCollection()
  val grafica: JFreeChart = ChartFactory.createScatterPlot("", "", "", coleccionSeries)
  val marcoGrafica: ChartFrame = new ChartFrame("Título del trabajo", grafica)
  val trazadoGrafica: XYPlot = grafica.getXYPlot
  val renderizador: XYLineAndShapeRenderer = new XYLineAndShapeRenderer(true, false)
  var seriesVias: Array[XYSeries] = Array()

  trazadoGrafica.setRenderer(renderizador)
  grafica.removeLegend()  // Se remueve las leyendas del gráfico que aparece en la parte inferior.
  trazadoGrafica.getRangeAxis().setVisible(false) // Se remueve el eje Y.
  trazadoGrafica.getDomainAxis().setVisible(false)  // Se remueve el eje X.
  trazadoGrafica.setBackgroundPaint(Color.white)  // Fondo blanco para el gráfico.

  marcoGrafica.addKeyListener(new KeyListener {

    override def keyTyped(keyEvent: KeyEvent): Unit = {}

    override def keyPressed(keyEvent: KeyEvent): Unit = {

      val key : Int = keyEvent.getKeyCode

      if (key == KeyEvent.VK_F5) Simulacion.IniciarSimulacion()

      else if (key == KeyEvent.VK_F6) {}// TODO: Definir forma para que se detenga la simulación cuando se presione F6

    }

    override def keyReleased(keyEvent: KeyEvent): Unit = {}
  })


  def dibujarMapa(vias: Array[Via]): Unit = {

    val lineaParametro = new BasicStroke(4.0f)

    def crearSerieVia(via: Via): XYSeries = {
      /**
        * Recibe una via y retorna una serie de la respectiva vía.
        * Para la creación del val SerieVia, se requiere un objeto tipo tipo Comparable como argumento del constructor,
        * dicho objeto está dado por el nombreIdentificador de la vía.
        */

      val serieVia: XYSeries = new XYSeries(via.nombreIdentificador().asInstanceOf[Comparable[String]])

      serieVia.add(via.origen.x, via.origen.y)
      serieVia.add(via.fin.x, via.fin.y)
      serieVia
    }

    def cambiarAparienciaVia(indexVia: Int): Unit = {

      renderizador.setSeriesPaint(indexVia, Color.gray) // Configura el color gris para la vía.
      renderizador.setSeriesStroke(indexVia, lineaParametro)  // Configura el grosor de la vía.
      renderizador.setSeriesShapesVisible(indexVia, false)  // Desactiva las figuras al inicio y fin de la vía
      renderizador.setSeriesLinesVisible(indexVia, true)  // Activa la línea entre el inicio y final de la vía.
    }

    def crearEtiquetaInterseccion(interseccion: Interseccion): Unit = {

      val etiqueta: XYTextAnnotation = new XYTextAnnotation(interseccion.nombre, interseccion.x, interseccion.y)
      etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12))
      trazadoGrafica.addAnnotation(etiqueta)
    }

    seriesVias = vias.map(via => crearSerieVia(via))

    seriesVias.foreach(serieVia => coleccionSeries.addSeries(serieVia))

    vias.foreach(via => cambiarAparienciaVia(vias.indexOf(via)))

    /* Extrae todas las intersecciones de las vías, elimina las repetidas, y por cada una crea su respectiva etiqueta
    en el gráfico.
     */
    (vias.map(via => via.origen) ++ vias.map(via => via.fin)).distinct.
      foreach(interseccion => crearEtiquetaInterseccion(interseccion))

    marcoGrafica.setSize(1500, 1000) // Tamaño de la ventana del gráfico.
    marcoGrafica.setVisible(true) // Activa la visualización del gráfico.

  }

  def graficarVehiculos(listaVehiculos: Array[Vehiculo]): Unit = {

    def crearSeriePorTipoDeVehiculo(tipoVehiculo: String, vehiculos: Array[Vehiculo]): XYSeries = {

      /**
        * Recibe un array de vehiculos. Primero detecta la clase de vehiculo que contiene dicho array, y según su tipo,
        * se hace la asignación para la variable comparableVehiculo. Luego, para cada vehiculo del array agrega un punto
        * en una serie llamada serieVehiculos. Para cada tipo de vehiculo corresponde una sola serie; por ejemplo, para
        * los vehiculos de tipo Carro corresponde una sola serie.
        * Al finalizar, se devuelve la serie de vehiculos .
        */

      var comparableVehiculo: Comparable[String] = "sin definir".asInstanceOf[Comparable[String]]

      if (tipoVehiculo == "Carro") {
        comparableVehiculo = tipoVehiculo.asInstanceOf[Comparable[String]]}

      else if (tipoVehiculo == "Moto"){
        comparableVehiculo = tipoVehiculo.asInstanceOf[Comparable[String]]}

      else if (tipoVehiculo == "MotoTaxi") {
        comparableVehiculo = tipoVehiculo.asInstanceOf[Comparable[String]]}

      else if (tipoVehiculo == "Camion") {
        comparableVehiculo = tipoVehiculo.asInstanceOf[Comparable[String]]}

      else if (tipoVehiculo == "Bus") {
        comparableVehiculo = tipoVehiculo.asInstanceOf[Comparable[String]]}

      val serieVehiculos: XYSeries = new XYSeries(comparableVehiculo)

      vehiculos.foreach(vehiculo => serieVehiculos.add(vehiculo.posicion.x, vehiculo.posicion.y))
      serieVehiculos
    }

    def cambiarAparienciaVehiculos(tipoVehiculo: String, seriesVehiculosAux: Array[XYSeries]): Unit = {

      if (tipoVehiculo == "Carro"){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(0)), ShapeUtilities.createDiagonalCross(3, 1))
        renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(0)), Color.BLACK)
      }
      else if (tipoVehiculo == "Moto"){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(1)), ShapeUtilities.createDiamond(3))
        renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(1)), Color.BLUE)
      }
      else if (tipoVehiculo == "Bus"){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(2)), ShapeUtilities.createDownTriangle(3))
        renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(2)), Color.CYAN)
      }
      else if (tipoVehiculo == "MotoTaxi"){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(3)), ShapeUtilities.createRegularCross(3, 1))
        renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(3)), Color.MAGENTA)
      }
      else if (tipoVehiculo == "Camion"){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(4)), ShapeUtilities.createDownTriangle(4))
        renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(4)), Color.ORANGE)
      }

    }
    /* Cada vez que se vaya a graficar los vehículos, se remueven todas las series de coleccionSeries para poder
    * actualizar las series */
    coleccionSeries.removeAllSeries()

    val tiposVehiculos: Array[String] = Array("Carro", "Moto", "Bus", "MotoTaxi", "Camion")

    val carros: Array[Vehiculo] = listaVehiculos.filter(_.getClass == classOf[Carro])
    val motos: Array[Vehiculo] = listaVehiculos.filter(_.getClass == classOf[Moto])
    val buses: Array[Vehiculo] = listaVehiculos.filter(_.getClass == classOf[Bus])
    val motoTaxis: Array[Vehiculo] = listaVehiculos.filter(_.getClass == classOf[MotoTaxi])
    val camiones: Array[Vehiculo] = listaVehiculos.filter(_.getClass == classOf[Camion])

    val serieCarros: XYSeries = crearSeriePorTipoDeVehiculo("Carro", carros)
    val serieMotos: XYSeries = crearSeriePorTipoDeVehiculo("Moto", motos)
    val serieBuses: XYSeries = crearSeriePorTipoDeVehiculo("Bus", buses)
    val serieMotoTaxis: XYSeries = crearSeriePorTipoDeVehiculo("MotoTaxi", motoTaxis)
    val serieCamiones: XYSeries = crearSeriePorTipoDeVehiculo("Camion", camiones)

    val seriesVehiculos: Array[XYSeries] = Array(serieCarros, serieMotos, serieBuses, serieMotoTaxis, serieCamiones)

    /* A ColeccionSeries agrego todas las series, tanto las vías como las series de los vehículos */
    (seriesVehiculos ++ seriesVias).foreach(serie => coleccionSeries.addSeries(serie))

    tiposVehiculos.foreach(tipo => cambiarAparienciaVehiculos(tipo, seriesVehiculos))
    marcoGrafica.setVisible(true)

  }
}
