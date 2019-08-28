
package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia
import geometria.Punto
import infraestructura.via.Via
import simulacion.Simulacion

import scala.collection.mutable.{ArrayBuffer, Queue}
import scala.math.abs

class VehiculoViaje(val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion,
                    val camino: Option[GrafoVia.grafo.Path]) {

  //---------- Camino a recorrer del vehiculo ----------
  val listaViasCamino: List[Via] = getListaViasCamino
  val colaViasCamino: Queue[Via] = Queue(listaViasCamino: _*)

  def getListaViasCamino: List[Via] = {
      camino.get.edges.toList.map(_.toOuter.label). // Hasta aqui, obtengo los labels de las vias a recorrer
        map(label => Simulacion.viasDirigidas.filter(via => via.nombreIdentificador() == label).head)
  }

  //---------- Parámetros del viaje ----------
  private var _viaActual: Via = _
  private var _vehiculoEnDestino: Boolean = _

  private var _desacelerarViaFinal: Boolean = false

  def viaActual: Via = _viaActual
  def viaActual_=(viaActual: Via): Unit = _viaActual = viaActual

  def vehiculoEnDestino: Boolean = _vehiculoEnDestino
  def vehiculoEnDestino_=(vehiculoEnDestino: Boolean): Unit = _vehiculoEnDestino = vehiculoEnDestino

  def desacelerarViaFinal: Boolean = _desacelerarViaFinal
  def desacelerarViaFinal_=(desacelerarViaFinal: Boolean): Unit = _desacelerarViaFinal = desacelerarViaFinal


  inicializarParametrosViaje()

  def inicializarParametrosViaje(): Unit = {
    vehiculoEnDestino = false
    viaActual = colaViasCamino.dequeue()
    vehiculo.velocidad.direccion.grados = viaActual.angulo
  }

  //---------- Para determinar la próxima posición del vehiculo ----------
  def mover(dt: Double): Unit = {
    if (!vehiculoEnDestino){   // Si el vehiculo no ha llegado a su destino
      if (estaEnInterseccion(dt)){   // verifico si está en una intersección, de ser así

        vehiculo.posicion.x = viaActual.fin.x   // actualizo la posición del vehiculo.
        vehiculo.posicion.y = viaActual.fin.y

        if (vehiculo.posicion == destino.asInstanceOf[Punto]) {   // Verifico si el vehiculo ya llego a su destino.
          vehiculoEnDestino = true
          VehiculoViaje.vehiculosEnSuDestino += vehiculo
        }
        else{
          viaActual = colaViasCamino.dequeue()
          vehiculo.velocidad.direccion.grados = viaActual.angulo
        }
      }
      else {   // Si el vehiculo no esta en una intersección, aumento su posición en la via.
        vehiculo.aumentarPosicion(dt)
        // El vehiculo está en la vía final, y ya superó 80% de la longitud de la via, entonces,
        // comience a desacelerar.
        if (desacelerarViaFinal && distanciaEntrePuntos(vehiculo.posicion, destino) <= viaActual.longitud * 0.2) {
          vehiculo.aceleracionActual = calcularDesaceleracion(viaActual.longitud * 0.2)
          desacelerarViaFinal = false
        }
      }
      // Si el Vehículo no ha llegado a su destino, verifico si viaActual es la vía final y, de ser el caso,
      // empiezo a desacelerar el vehículo.
      if (colaViasCamino.isEmpty && vehiculo.aceleracionActual >= 0) {
        desacelerarViaFinal = true
      }
    }
  }

  def calcularDesaceleracion(distancia: Double): Double = {
    -1 * Math.pow(vehiculo.velocidad.magnitud, 2) / (2 * distancia)
  }

  def distanciaEntrePuntos(punto1: Punto, punto2: Punto): Double = {
    Math.hypot(punto2.x - punto1.x, punto2.y - punto1.y)
  }

  def estaEnInterseccion(dt: Double): Boolean = {
    val extremoIzquierdoIntervaloX = viaActual.fin.x - abs(vehiculo.velocidad.velocidadDireccionX()) * dt
    val extremoDerechoIntervaloX = viaActual.fin.x + abs(vehiculo.velocidad.velocidadDireccionX()) * dt
    val extremoIzquierdoIntervaloY = viaActual.fin.y - abs(vehiculo.velocidad.velocidadDireccionY()) * dt
    val extremoDerechoIntervaloY = viaActual.fin.y + abs(vehiculo.velocidad.velocidadDireccionY()) * dt

    if ((vehiculo.posicion.x >= extremoIzquierdoIntervaloX) &&
        (vehiculo.posicion.x <= extremoDerechoIntervaloX) &&
        (vehiculo.posicion.y >= extremoIzquierdoIntervaloY) &&
        (vehiculo.posicion.y <= extremoDerechoIntervaloY)
    ) true
    else false
  }
}


object VehiculoViaje{
  private var _vehiculosEnSuDestino: ArrayBuffer[Vehiculo] = _

  def vehiculosEnSuDestino: ArrayBuffer[Vehiculo] = _vehiculosEnSuDestino
  def vehiculosEnSuDestino_=(vehiculosEnSuDestino: ArrayBuffer[Vehiculo]): Unit =
    _vehiculosEnSuDestino = vehiculosEnSuDestino
}
