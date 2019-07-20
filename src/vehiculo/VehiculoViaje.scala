package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia
import infraestructura.via.Via
import simulacion.Simulacion

import scala.collection.mutable

class VehiculoViaje(
                     val vehiculo: Vehiculo,
                     val origen: Interseccion,
                     val destino: Interseccion,
                     val camino: GrafoVia.grafo.Path
                   ) {

  private var viaActual: Via = _ // TODO: falta definir esta variable. Es la vía por la cual el vehiculo circula actual/

  private def estaEnLaInterseccion(dt: Double): Boolean = {

    """
      |Esta función detecta si el vehículo se encuentra en la intersección final de la vía por la cual está circulando.
      |La función retorna true si la ubicación actual del vehículo se encuentra dentro de un intervalo alrededor de la
      |intersección final de la via. Este intervalo depende del dt, de la velocidad en dirección x y y del vehículo y
      | de la intersección final.
    """.stripMargin


    val extremoIzquierdoIntervaloX = viaActual.fin.x - vehiculo.velocidad.velocidadDireccionX() * dt
    val extremoDerechoIntervaloX = viaActual.fin.x + vehiculo.velocidad.velocidadDireccionX() * dt

    val extremoIzquierdoIntervaloY = viaActual.fin.y - vehiculo.velocidad.velocidadDireccionY() * dt
    val extremoDerechoIntervaloY = viaActual.fin.y + vehiculo.velocidad.velocidadDireccionY() * dt

    if (
      (vehiculo.posicion.x >= extremoIzquierdoIntervaloX && vehiculo.posicion.x <= extremoDerechoIntervaloX) &&
        (vehiculo.posicion.y >= extremoIzquierdoIntervaloY && vehiculo.posicion.y <= extremoDerechoIntervaloY)){

      true
    }
    else{

      false
    }
  }

  def mover(dt: Double): Unit = {

    """
      | Esta función está encargada de hacer el movimiento del vehículo. Debe verificar que no haya llegado a su
      | destino, si no lo a hecho, verifica si no ha llegado a la intersección final de la vía por la cual está
      | circulando; si está en la intersección final de la vía, cambia la variable viaActual a la siguiente via en
      | el camino que debe seguir el vehículo y cambia el ángulo de la velocidad del vehiculo por el ángulo de la vía
      | actual.
    """.stripMargin

  }
}
