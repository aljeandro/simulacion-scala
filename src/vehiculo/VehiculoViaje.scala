
package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia

import scala.collection.mutable.ArrayBuffer
import scala.math.toDegrees
import scala.math.atan
import scala.math.abs

class VehiculoViaje(val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion,
                    val camino: GrafoVia.grafo.Path) {

  val nodos = camino.get.nodes
  var nodoInicio: Interseccion = origen
  var indexNodo: Int = 1
  var nodoFin: Interseccion = nodos(indexNodo)

  var vehiculoEnDestino: Boolean = false

  def mover(dt: Double): Unit = {
    if(!vehiculoEnDestino){
      if(estaEnInterseccion(dt: Double)){
        if(nodoFin != destino){
          nodoInicio = nodoFin
          indexNodo += 1
          nodoFin = nodos(indexNodo)
          vehiculo.velocidad.angulo.grados =
            toDegrees(atan(abs(nodoInicio.y - nodoFin.y) / abs(nodoInicio.x - nodoFin.x)))
          vehiculo.posicion = nodoInicio
        }
        else{
          VehiculoViaje.vehiculosEnSuDestino += vehiculo
          vehiculoEnDestino = true
        }
      }
      else vehiculo.aumentarPosicion(dt)
    }
  }

  def estaEnInterseccion(dt: Double): Boolean = {
    val extremoIzquierdoIntervaloX = nodoFin.x - vehiculo.velocidad.velocidadDireccionX() * dt
    val extremoDerechoIntervaloX = nodoFin.x + vehiculo.velocidad.velocidadDireccionX() * dt
    val extremoIzquierdoIntervaloY = nodoFin.y - vehiculo.velocidad.velocidadDireccionY() * dt
    val extremoDerechoIntervaloY = nodoFin.y + vehiculo.velocidad.velocidadDireccionY() * dt

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
