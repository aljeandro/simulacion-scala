package resultadosSimulacion

import simulacion.Simulacion
import vehiculo.Vehiculo

class Comparendo (_vehiculo:Vehiculo,_velocidad:Double,_velocidadMaxima:Double){
  def vehiculo = _vehiculo
  def velocidad = _velocidad
  def velocidadMaxima = _velocidadMaxima
  Simulacion.listaComparendos.append(this)
  val exceso = (velocidad-velocidadMaxima)*100/velocidadMaxima
}
