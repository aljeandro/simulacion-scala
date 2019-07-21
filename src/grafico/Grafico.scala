
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
    *   iteración del método run en el object Simulacion.
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

      if (key == KeyEvent.VK_F5) Simulacion.iniciarSimulacion()

      else if (key == KeyEvent.VK_F6) {Simulacion.continuarSimulacion = false}

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

  def graficarVehiculos(viajesVehiculos: Array[VehiculoViaje]): Unit = {

    def crearRepresentacionVehiculo(vehiculoViaje: VehiculoViaje): Unit = {

      val vehiculo: Vehiculo = vehiculoViaje.vehiculo

      val serieVehiculo: XYSeries = new XYSeries(vehiculo.placa.asInstanceOf[Comparable[String]])

      serieVehiculo.add(vehiculo.posicion.x, vehiculo.posicion.y)

      coleccionSeries.addSeries(serieVehiculo)

      renderizador.setSeriesPaint(coleccionSeries.indexOf(serieVehiculo), vehiculoViaje.destino.color)

      if (vehiculo.getClass == classOf[Carro]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(serieVehiculo), ShapeUtilities.createDiagonalCross(3, 1))
      }
      else if (vehiculo.getClass == classOf[Moto]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(serieVehiculo), ShapeUtilities.createDiamond(3))
      }
      else if (vehiculo.getClass == classOf[Bus]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(serieVehiculo), ShapeUtilities.createDownTriangle(3))
      }
      else if (vehiculo.getClass == classOf[MotoTaxi]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(serieVehiculo), ShapeUtilities.createRegularCross(3, 1))

      }
      else if (vehiculo.getClass == classOf[Camion]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(serieVehiculo), ShapeUtilities.createDownTriangle(4))
      }
    }

    /* Cada vez que se vaya a graficar los vehículos, se remueven todas las series de coleccionSeries para poder
    * actualizar las series */
    coleccionSeries.removeAllSeries()

    // Crea la representación en el gráfico de los vehiculos
    viajesVehiculos.foreach(vehiculoViaje => crearRepresentacionVehiculo(vehiculoViaje))

    /* A ColeccionSeries le agrego nuevamente las seriesVias */
    seriesVias.foreach(serie => coleccionSeries.addSeries(serie))

    marcoGrafica.setVisible(true)

  }
}
