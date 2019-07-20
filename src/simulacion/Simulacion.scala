
package simulacion

import grafo.GrafoVia
import vehiculo._
import infraestructura.via.Via
import infraestructura.Interseccion

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.math._

object Simulacion extends Runnable{

  val grafo = GrafoVia
  var t = 0 // Tiempo de la simulación
  var dt = 1
  var tiempoDormir = 0.5
  val minVehiculos = 100
  val maxVehiculos = 200
  val minVelocidad = 40
  val maxVelocidad = 100
  var vehiculos = ArrayBuffer[Vehiculo]()
  val vias = ArrayBuffer[Via]()


  val intersecciones = ArrayBuffer[Interseccion]()
  val listaDeVehiculos = ArrayBuffer[VehiculoViaje]()

  /* TODO: Falta identificar algunas variables más, necesarias para la simulación */

  def cargarConfiguracion(): Unit = {

    t = 0
    vehiculos.clear()
    vias.clear()
    /* TODO: función sin terminar */
  }

  def crearVehiculos(
                      proporcionCarro: Double,
                      proporcionMoto: Double,
                      proporcionBus: Double,
                      proporcionCamion: Double,
                      proporcionMotoTaxi: Double
                    ): Unit = {

    val cantVehiculos: Int = Random.nextInt(maxVehiculos - minVehiculos) + minVehiculos
    val cantCarro: Int = floor(cantVehiculos * proporcionCarro).toInt
    val cantMoto: Int = floor(cantVehiculos * proporcionMoto).toInt
    val cantBus: Int = floor(cantVehiculos * proporcionBus).toInt
    val cantCamion: Int = floor(cantVehiculos * proporcionCamion).toInt
    val cantMotoTaxi: Int = floor(cantVehiculos * proporcionMotoTaxi).toInt

    var contCarro: Int = 0
    var contMoto: Int = 0
    var contBus: Int = 0
    var contCamion: Int = 0
    var contMotoTaxi: Int = 0

    while (vehiculos.size < cantVehiculos) {

      val vehiculo = Vehiculo.generarVehiculo
      vehiculos += vehiculo

      if (vehiculo.getClass == classOf[Carro] && contCarro < cantCarro) contCarro += 1

      else if (vehiculo.getClass == classOf[Moto] && contMoto < cantMoto) contMoto += 1

      else if (vehiculo.getClass == classOf[Bus] && contBus < cantBus) contBus += 1

      else if (vehiculo.getClass == classOf[Camion] && contCamion < cantCamion) contCamion += 1

      else if (vehiculo.getClass == classOf[MotoTaxi] && contMotoTaxi < cantMotoTaxi) contMotoTaxi += 1

    }
  }

  // Esta función crea los vehículosViaje para cada vehículo
  def crearViajesVehiculos(): Unit ={

    for(i <- 0 to vehiculos.size){

      val origenAleat: Int = Random.nextInt(intersecciones.size)
      var destinoAleat: Int = Random.nextInt(intersecciones.size)

      while(origenAleat == destinoAleat) destinoAleat = Random.nextInt(intersecciones.size)

      // listaDeVehiculos += new VehiculoViaje(vehiculos(i), intersecciones(origenAleat),
      //  intersecciones(destinoAleat), )

      // Falta el cuarto argumento, el cual sería el camino
    }

  }

  def run(): Unit = {

    while (true) {
      listaDeVehiculos.foreach(_.mover(dt))
      t += dt
      Grafico.graficarVehiculos(listaDeVehiculos)
      Thread.sleep(tiempoDormir)
    }

  }

}
