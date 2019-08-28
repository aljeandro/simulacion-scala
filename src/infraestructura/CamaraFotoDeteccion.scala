package infraestructura

import via.Via
import geometria.Punto

class CamaraFotoDeteccion(val via: Via, private var _distanciaDesdeOrigen: Double) {

  def distanciaDesdeOrigen(): Double = _distanciaDesdeOrigen
  def distanciaDesdeOrigen_=(distanciaDesdeOrigen: Double): Unit = _distanciaDesdeOrigen = distanciaDesdeOrigen

  val angulo = via.angulo

  var posicion: Punto = _
  if (angulo == 90){
    posicion = Punto(via.origen.x,via.origen.y + distanciaDesdeOrigen)
  }
  else if (angulo == 180){
    posicion = Punto(via.origen.x - distanciaDesdeOrigen, via.origen.y)
  }
  else if (angulo == 270){
    posicion = Punto(via.origen.x,via.origen.y - distanciaDesdeOrigen)
  }
  else if (angulo == 0){
    posicion = Punto(via.origen.x + distanciaDesdeOrigen,via.origen.y)
  }
  else {
    posicion = Punto(via.origen.x + math.cos(angulo.toRadians) * distanciaDesdeOrigen, via.origen.y + math.sin(angulo.toRadians) * distanciaDesdeOrigen)
  }
}
