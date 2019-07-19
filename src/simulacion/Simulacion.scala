
package simulacion

import grafo.GrafoVia
import vehiculo.{Bus, Camion, Carro, Moto, MotoTaxi, Vehiculo}
import infraestructura.via.Via

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object Simulacion extends Runnable{

  val grafo = GrafoVia
  var t = 0
  var dt = 1
  var tiempoDormir = 0.5
  val minVehiculos = 100
  val maxVehiculos = 200
  val minVelocidad = 40
  val maxVelocidad = 100
  var vehiculos = ArrayBuffer[Vehiculo]()
  val vias = ArrayBuffer[Via]()
  /* Falta identificar algunas variables más, necesarias para la simulación */

  def cargarConfiguracion(): Unit = {

    t = 0
    vehiculos.clear()
    vias.clear()
    /* función sin terminar */
  }

  def crearVehiculos(
                      proporcionCarro: Double,
                      proporcionMoto: Double,
                      proporcionBus: Double,
                      proporcionCamion: Double,
                      proporcionMotoTaxi: Double
                    ): Unit = {

    val cantVehiculos: Int = Random.nextInt(maxVehiculos - minVehiculos) + minVehiculos
    val cantCarro: Int = Math.floor(cantVehiculos * proporcionCarro).toInt
    val cantMoto: Int = Math.floor(cantVehiculos * proporcionMoto).toInt
    val cantBus: Int = Math.floor(cantVehiculos * proporcionBus).toInt
    val cantCamion: Int = Math.floor(cantVehiculos * proporcionCamion).toInt
    val cantMotoTaxi: Int = Math.floor(cantVehiculos * proporcionMotoTaxi).toInt

    var contCarro: Int = 0
    var contMoto: Int = 0
    var contBus: Int = 0
    var contCamion: Int = 0
    var contMotoTaxi: Int = 0

    while (vehiculos.size < cantVehiculos) {

      val vehiculo = Vehiculo.generarVehiculo

      if (vehiculo.getClass == classOf[Carro] && contCarro < cantCarro) {vehiculos += vehiculo; contCarro += 1}

      else if (vehiculo.getClass == classOf[Moto] && contMoto < cantMoto) {vehiculos += vehiculo; contMoto += 1}

      else if (vehiculo.getClass == classOf[Bus] && contBus < cantBus) {vehiculos += vehiculo; contBus += 1}

      else if (vehiculo.getClass == classOf[Camion] && contCamion < cantCamion) {vehiculos += vehiculo; contCamion += 1}

      else if (vehiculo.getClass == classOf[MotoTaxi] && contMotoTaxi < cantMotoTaxi) {
        vehiculos += vehiculo; contMotoTaxi += 1}
    }
  }

  def crearViajesVehiculos(): Unit ={

    /* Esta función debe crear los objetos VehiculoViaje para cada Vehiculo */
  }

  def run() {

    /*
    while (true) {
      vehiculos.foreach(_.aumentarPosicion(dt))
      t += dt
      Grafico.graficarVehiculos(vehiculos)
      Thread.sleep(tRefresh)
    }
    */
  }
}

