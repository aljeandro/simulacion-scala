
package resultadosSimulacion

import infraestructura.Interseccion
import infraestructura.via.Via
import vehiculo.{Bus, Camion, Carro, Moto, MotoTaxi, VehiculoViaje}

class ResultadosSimulacion(
                            val vias: Array[Via],
                            val intersecciones: Array[Interseccion],
                            val vehiculoViajes: Array[VehiculoViaje],
                            val tiempoSimulado: Double,
                            val tiempoReal: Double) {

  def totalVehiculos: Int = vehiculoViajes.length

  def totalCarros: Int = {
    vehiculoViajes.map(vehiculoViaje => vehiculoViaje.vehiculo).count(vehiculo => vehiculo.getClass == classOf[Carro])
  }

  def totalMotos: Int = {
    vehiculoViajes.map(vehiculoViaje => vehiculoViaje.vehiculo).count(vehiculo => vehiculo.getClass == classOf[Moto])
  }

  def totalBuses: Int = {
    vehiculoViajes.map(vehiculoViaje => vehiculoViaje.vehiculo).count(vehiculo => vehiculo.getClass == classOf[Bus])
  }

  def totalCamiones: Int = {
    vehiculoViajes.map(vehiculoViaje => vehiculoViaje.vehiculo).count(vehiculo => vehiculo.getClass == classOf[Camion])
  }

  def totalMotoTaxis: Int = {
    vehiculoViajes.map(vehiculoViaje => vehiculoViaje.vehiculo).count(vehiculo => vehiculo.getClass == classOf[MotoTaxi])
  }

  def totalVias: Int = vias.length

  def totalIntersecciones: Int = intersecciones.length

  def viasUnSentido: Int = vias.count(via => via.sentido.nombre == "unaVia")

  def viasDobleSentido: Int = vias.count(via => via.sentido.nombre == "dobleVia")

  def velocidadMinimaVias: Double = vias.map(via => via.velocidadMax).min

  def velocidadMaximaVias: Double = vias.map(via => via.velocidadMax).max

  def longitudPromedio: Double = vias.map(via => via.longitud).sum / vias.length

  def promedioOrigen: Double = {
    intersecciones.map(inter => vehiculoViajes.count(viaje => viaje.origen == inter)).sum /
      intersecciones.length
  }

  def promedioDestino: Double = {
    intersecciones.map(inter => vehiculoViajes.count(viaje => viaje.destino == inter)).sum /
      intersecciones.length
  }

  def sinOrigen: Int = {
    intersecciones.map(inter => vehiculoViajes.map(viaje => viaje.origen).contains(inter)).count(bool => !bool)
  }

  def sinDestino: Int = {
    intersecciones.map(inter => vehiculoViajes.map(viaje => viaje.destino).contains(inter)).count(bool => !bool)
  }

  def velocidadMinima: Double = {
    vehiculoViajes.map(viaje => viaje.vehiculo).map(vehiculo => vehiculo.velocidad.magnitud).min
  }

  def velocidadMaxima: Double = {
    vehiculoViajes.map(viaje => viaje.vehiculo).map(vehiculo => vehiculo.velocidad.magnitud).max
  }

  def velocidadPromedio: Double ={
    vehiculoViajes.map(viaje => viaje.vehiculo).map(vehiculo => vehiculo.velocidad.magnitud).sum / vehiculoViajes.length
  }

  def distanciaMinima: Double = {
    vehiculoViajes.map(_.listaViasCamino.head).map(_.longitud).min
  }

  def distanciaMaxima: Double = {
    vehiculoViajes.map(_.listaViasCamino.head).map(_.longitud).max
  }

  def distanciaPromedio: Double = {
    vehiculoViajes.map(_.listaViasCamino.head).map(_.longitud).sum / vehiculoViajes.map(_.listaViasCamino.head).length
  }
}
