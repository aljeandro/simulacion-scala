
package grafico

import java.awt.{BasicStroke, Color, Font}

import infraestructura.Interseccion
import infraestructura.via.Via
import org.jfree.chart.annotations.XYTextAnnotation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.{ChartFactory, ChartFrame, JFreeChart}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.jfree.util.ShapeUtilities
import vehiculo.{Bus, Camion, Carro, Moto, MotoTaxi, Vehiculo}


object Grafico{

  var coleccionSeries: XYSeriesCollection = new XYSeriesCollection()
  val grafica: JFreeChart = ChartFactory.createScatterPlot("", "", "", coleccionSeries)
  val marcoGrafica: ChartFrame = new ChartFrame("Título del trabajo", grafica)
  val trazadoGrafica: XYPlot = grafica.getXYPlot
  val renderizador: XYLineAndShapeRenderer = new XYLineAndShapeRenderer(true, false)
  var seriesVias: Array[XYSeries] = Array()

  trazadoGrafica.setRenderer(renderizador)
  grafica.removeLegend()
  trazadoGrafica.getRangeAxis().setVisible(false)
  trazadoGrafica.getDomainAxis().setVisible(false)
  trazadoGrafica.setBackgroundPaint(Color.white)

  def dibujarMapa(vias: Array[Via]): Unit = {

    val lineaParametro = new BasicStroke(4.0f)

    def crearSerieVia(via: Via): XYSeries = {

      val serieVia: XYSeries = new XYSeries(via.nombre.asInstanceOf[Comparable[String]])

      serieVia.add(via.origen.x, via.origen.y)
      serieVia.add(via.fin.x, via.fin.y)
      serieVia
    }

    def cambiarAparienciaVia(indexVia: Int): Unit = {

      renderizador.setSeriesPaint(indexVia, Color.gray)
      renderizador.setSeriesStroke(indexVia, lineaParametro)
      renderizador.setSeriesShapesVisible(indexVia, false)
      renderizador.setSeriesLinesVisible(indexVia, true)
    }

    def crearEtiquetaInterseccion(interseccion: Interseccion): Unit = {

      val etiqueta: XYTextAnnotation = new XYTextAnnotation(interseccion.nombre, interseccion.x, interseccion.y)
      etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12))
      trazadoGrafica.addAnnotation(etiqueta)
    }

    seriesVias = vias.map(via => crearSerieVia(via))

    seriesVias.foreach(serieVia => coleccionSeries.addSeries(serieVia))

    vias.foreach(via => cambiarAparienciaVia(vias.indexOf(via)))

    (vias.map(via => via.origen) ++ vias.map(via => via.fin)).distinct.
      foreach(interseccion => crearEtiquetaInterseccion(interseccion))

    marcoGrafica.setSize(1500, 1000)
    marcoGrafica.setVisible(true)

  }

  def graficarVehiculos(listaVehiculos: Array[Vehiculo]): Unit = {

    def crearSeriePorTipoDeVehiculo(vehiculos: Array[Vehiculo]): XYSeries = {

      val serieVehiculos: XYSeries = new XYSeries(vehiculo.placa.asInstanceOf[Comparable[String]])
      vehiculos.foreach(vehiculo => serieVehiculos.add(vehiculo.x, vehiculo.y))
      serieVehiculos
    }

    def cambiarAparienciaVehiculos(tipoVehiculo: ClassOf[Vehiculo], seriesVehiculosAux: Array[XYSeries]): Unit = {

      if (tipoVehiculo == classOf[Carro]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(0)), ShapeUtilities.createDiagonalCross(3, 1))
        //renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(0)), Color.BLACK)
      }
      else if (tipoVehiculo == classOf[Moto]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(1)), ShapeUtilities.createDiamond(3))
        //renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(1)), Color.BLACK)
      }
      else if (tipoVehiculo == classOf[Bus]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(2)), ShapeUtilities.createDownTriangle(3))
        //renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(2)), Color.BLACK)
      }
      else if (tipoVehiculo == classOf[MotoTaxi]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(3)), ShapeUtilities.createRegularCross(3, 1))
        //renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(3)), Color.BLACK)
      }
      else if (tipoVehiculo == classOf[Camion]){

        renderizador.setSeriesShape(
          coleccionSeries.indexOf(seriesVehiculosAux(4)), ShapeUtilities.createDownTriangle(4))
        //renderizador.setSeriesPaint(coleccionSeries.indexOf(seriesVehiculosAux(4)), Color.BLACK)
      }

    }

    coleccionSeries.removeAllSeries()

    val tiposVehiculos: Array[classOf[Vehiculo]] = Array(classOf[Carro], classOf[Moto], classOf[Bus], classOf[MotoTaxi], classOf[Camion])

    val seriesVehiculos: Array[XYSeries] = tiposVehiculos.map(tipo => crearSeriePorTipoDeVehiculo(listaVehiculos.filter(tipo == _.getClass)))

    (seriesVehiculos ++ seriesVias).foreach(serie => coleccionSeries.addSeries(serie))

    tiposVehiculos.foreach(tipo => cambiarAparienciaVehiculos(tipo, seriesVehiculos))

  }
}
