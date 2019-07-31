
package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia
import geometria.Punto
import simulacion.Simulacion

import scala.collection.mutable.{ArrayBuffer, Queue}
import scala.math.abs

class VehiculoViaje(val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion,
                    val camino: Option[GrafoVia.grafo.Path]) {

  val listaViasCamino = camino.get.edges.toList.map(_.toOuter.label).
    map(label => Simulacion.viasDirigidas.filter(via => via.nombreIdentificador() == label).head)

  val colaViasCamino = Queue(listaViasCamino: _*)

  println("Intersecciones a recorrer del vehiculo " + vehiculo.placa)  // TODO: Eliminar
  colaViasCamino.foreach(via => println(via.origen))  // TODO: Eliminar
  println(colaViasCamino.last.fin)  // TODO: Eliminar

  private var viaActual = colaViasCamino.dequeue()
  vehiculo.velocidad.angulo.grados = viaActual.angulo

  var vehiculoEnDestino: Boolean = false

  def mover(dt: Double): Unit = {
    if (!vehiculoEnDestino){
      if (estaEnInterseccion(dt)){

        println("El vehiculo " + vehiculo.placa + " entró en la intersección: " + viaActual.fin)  // TODO: Eliminar

        vehiculo.posicion.x = viaActual.fin.x
        vehiculo.posicion.y = viaActual.fin.y

        if (vehiculo.posicion == destino.asInstanceOf[Punto]) {
          vehiculoEnDestino = true
          VehiculoViaje.vehiculosEnSuDestino += vehiculo

          println("El vehiculo " + vehiculo.placa + " llegó a su destino")  // TODO: Eliminar

        }
        else{
          viaActual = colaViasCamino.dequeue()
          vehiculo.velocidad.angulo.grados = viaActual.angulo
        }
      }
      else {
        vehiculo.aumentarPosicion(dt)
      }
    }
  }

  def estaEnInterseccion(dt: Double): Boolean = {
    val extremoIzquierdoIntervaloX = viaActual.fin.x - abs(vehiculo.velocidad.velocidadDireccionX()) * dt * 4
    val extremoDerechoIntervaloX = viaActual.fin.x + abs(vehiculo.velocidad.velocidadDireccionX()) * dt * 4
    val extremoIzquierdoIntervaloY = viaActual.fin.y - abs(vehiculo.velocidad.velocidadDireccionY()) * dt * 4
    val extremoDerechoIntervaloY = viaActual.fin.y + abs(vehiculo.velocidad.velocidadDireccionY()) * dt * 4

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
