
package vehiculo

import fisica._
import geometria.{Angulo, Punto}

import scala.util.Random
import scala.collection.mutable.HashMap

abstract case class Vehiculo() extends Movil with MovimientoUniforme{

  protected var _velocidad: Velocidad = new Velocidad(0, Angulo(0))
  protected var _posicion: Punto = Punto(0, 0)
  val placa: String

  def velocidad: Velocidad = _velocidad
  def velocidad_=(velocidad: Velocidad): Unit = _velocidad = velocidad

  def posicion: Punto = _posicion
  def posicion_=(posicion: Punto): Unit = _posicion = posicion

  def aumentarPosicion(dt: Double): Unit = posicion = movimientoUniforme(dt, posicion, velocidad)
}


object Vehiculo{

  //---------- Creación de los vehiculos ----------
  private var _proporcionCarro: Double = _
  private var _proporcionMoto: Double = _
  private var _proporcionBus: Double = _
  private var _proporcionCamion: Double = _

  def proporcionCarro: Double = _proporcionCarro
  def proporcionCarro_=(proporcionCarro: Double): Unit = _proporcionCarro = proporcionCarro

  def proporcionMoto: Double = _proporcionMoto
  def proporcionMoto_=(proporcionMoto: Double): Unit = _proporcionMoto = proporcionMoto

  def proporcionBus: Double = _proporcionBus
  def proporcionBus_=(proporcionBus: Double): Unit = _proporcionBus = proporcionBus

  def proporcionCamion: Double = _proporcionCamion
  def proporcionCamion_=(proporcionCamion: Double): Unit = _proporcionCamion = proporcionCamion


  def generarVehiculo: Vehiculo = {
    val numAleatorio: Double = Random.nextDouble() // Valor aleatorio entre 0 (inclusivo) y 1 (exclusivo)

    if (0 < numAleatorio && numAleatorio <= proporcionCarro) new Carro

    else if (proporcionCarro < numAleatorio &&
      numAleatorio <= proporcionCarro + proporcionMoto) new Moto

    else if (proporcionCarro + proporcionMoto < numAleatorio &&
      numAleatorio <= proporcionCarro + proporcionMoto + proporcionBus) new Bus

    else if (proporcionCarro + proporcionMoto + proporcionBus < numAleatorio &&
      numAleatorio <= proporcionCarro + proporcionMoto + proporcionBus + proporcionCamion) new Camion

    else new MotoTaxi
  }

  //---------- Creación de las placas de los vehiculos ----------

  private var _placasVehiculos: HashMap[String, String] = HashMap.empty[String,String]

  def placasVehiculos: HashMap[String, String] = _placasVehiculos
  def placasVehiculos_=(placasVehiculos: HashMap[String, String]): Unit = _placasVehiculos = placasVehiculos


  def generarPlaca(tipoDeVehiculo: String): String = {
    var placa: String = nuevaPlaca(tipoDeVehiculo)

    // Para verificar que no se esta creando una placa ya existente.
    while(placasVehiculos.contains(placa)) placa = nuevaPlaca(tipoDeVehiculo)

    placasVehiculos += (placa -> placa)

    placa
  }

  // Se crea una nueva placa dependiendo del tipo del vehiculo.
  def nuevaPlaca(tipoDeVehiculo: String): String = tipoDeVehiculo match {

    case "Bus" | "Carro" | "MotoTaxi" =>
      val letra1: String = Random.alphanumeric.filter(_.isLetter).head.toString
      val letra2: String = Random.alphanumeric.filter(_.isLetter).head.toString
      val letra3: String = Random.alphanumeric.filter(_.isLetter).head.toString

      val num1: Int = Random.nextInt(10)
      val num2: Int = Random.nextInt(10)
      val num3: Int = Random.nextInt(10)

      s"$num1$num2$num3$letra1$letra2$letra3"

    case "Camion" =>
      val num1: Int = Random.nextInt(10)
      val num2: Int = Random.nextInt(10)
      val num3: Int = Random.nextInt(10)
      val num4: Int = Random.nextInt(10)
      val num5: Int = Random.nextInt(10)

      s"R$num1$num2$num3$num4$num5"

    case "Moto" =>
      val letra1: String = Random.alphanumeric.filter(_.isLetter).head.toString
      val letra2: String = Random.alphanumeric.filter(_.isLetter).head.toString
      val letra3: String = Random.alphanumeric.filter(_.isLetter).head.toString
      val letra4: String = Random.alphanumeric.filter(_.isLetter).head.toString

      val num1: Int = Random.nextInt(10)
      val num2: Int = Random.nextInt(10)

      s"$letra1$letra2$letra3$num1$num2$letra4"
  }
}
