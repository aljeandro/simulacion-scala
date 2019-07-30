
package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia
import simulacion.Simulacion
import geometria.Punto
import infraestructura.via.Via

import scala.collection.mutable.{ArrayBuffer, Queue}

class VehiculoViaje(val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion,
                    val camino: Option[GrafoVia.grafo.Path]) {

  val listaViasCamino = camino.get.edges.toList.map(_.toOuter.label).map(identificarVia(_))

  def identificarVia(label: Any): Via = {
    try{
      Simulacion.vias.filter(via => via.identificadorOrigenFin() == label).head
    } catch {
      case e: Exception => {
        val via = Simulacion.vias.filter(via => via.identificadorFinOrigen() == label).head
        new Via(via.fin, via.origen, via.velocidadMax, via.tipoVia, via.sentido, via.numeroVia, via.nombre)
      }
    }
  }

  val colaViasCamino = Queue(listaViasCamino: _*)
  private var viaActual = colaViasCamino.dequeue()
  vehiculo.velocidad.angulo.grados = viaActual.angulo

  var vehiculoEnDestino: Boolean = false

  def mover(dt: Double): Unit = {
    if (!vehiculoEnDestino){
      if (colaViasCamino.nonEmpty) {
        if (estaEnInterseccion(dt)){
          viaActual = colaViasCamino.dequeue()
          vehiculo.velocidad.angulo.grados = viaActual.angulo
          vehiculo.posicion = viaActual.origen
        }
      }
      vehiculo.aumentarPosicion(dt)
    }
    else VehiculoViaje.vehiculosEnSuDestino += vehiculo
    if (vehiculo.posicion == destino.asInstanceOf[Punto]) vehiculoEnDestino = true
  }

  def estaEnInterseccion(dt: Double): Boolean = {
    val extremoIzquierdoIntervaloX = viaActual.fin.x - vehiculo.velocidad.velocidadDireccionX() * dt
    val extremoDerechoIntervaloX = viaActual.fin.x + vehiculo.velocidad.velocidadDireccionX() * dt
    val extremoIzquierdoIntervaloY = viaActual.origen.y - vehiculo.velocidad.velocidadDireccionY() * dt
    val extremoDerechoIntervaloY = viaActual.origen.y + vehiculo.velocidad.velocidadDireccionY() * dt

    if (
        (vehiculo.posicion.x >= extremoIzquierdoIntervaloX) &&
        (vehiculo.posicion.x <= extremoDerechoIntervaloX) &&
        (vehiculo.posicion.y >= extremoIzquierdoIntervaloY) &&
        (vehiculo.posicion.y <= extremoDerechoIntervaloY)
    ) true
    else false
  }
}

object VehiculoViaje{
  var vehiculosEnSuDestino: ArrayBuffer[Vehiculo] = _
}
