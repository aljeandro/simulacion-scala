
package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia
import geometria.Punto
import infraestructura.via.Via
import simulacion.Simulacion

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Queue}
import scala.math.abs

class VehiculoViaje(val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion,
                    val camino: Option[GrafoVia.grafo.Path]) {

  val listaViasCamino: List[Via] = camino.get.edges.toList.map(_.toOuter.label).
    map(label => Simulacion.viasDirigidas.filter(via => via.nombreIdentificador() == label).head)

  val colaViasCamino: mutable.Queue[Via] = mutable.Queue(listaViasCamino: _*)

  private var viaActual = colaViasCamino.dequeue()
  vehiculo.velocidad.direccion.grados = viaActual.angulo

  var vehiculoEnDestino: Boolean = false

  def mover(dt: Double): Unit = {
    if (!vehiculoEnDestino){
      if (estaEnInterseccion(dt)){

        vehiculo.posicion.x = viaActual.fin.x
        vehiculo.posicion.y = viaActual.fin.y

        if (vehiculo.posicion == destino.asInstanceOf[Punto]) {
          vehiculoEnDestino = true
          VehiculoViaje.vehiculosEnSuDestino += vehiculo
        }
        else{
          viaActual = colaViasCamino.dequeue()
          vehiculo.velocidad.direccion.grados = viaActual.angulo
        }
      }
      else {
        vehiculo.aumentarPosicion(dt)
      }
    }
  }

  def estaEnInterseccion(dt: Double): Boolean = {
    val extremoIzquierdoIntervaloX = viaActual.fin.x - abs(vehiculo.velocidad.velocidadDireccionX()) * dt
    val extremoDerechoIntervaloX = viaActual.fin.x + abs(vehiculo.velocidad.velocidadDireccionX()) * dt
    val extremoIzquierdoIntervaloY = viaActual.fin.y - abs(vehiculo.velocidad.velocidadDireccionY()) * dt
    val extremoDerechoIntervaloY = viaActual.fin.y + abs(vehiculo.velocidad.velocidadDireccionY()) * dt

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
  var vehiculosEnSuDestino: ArrayBuffer[Vehiculo] = ArrayBuffer[Vehiculo]()
}
