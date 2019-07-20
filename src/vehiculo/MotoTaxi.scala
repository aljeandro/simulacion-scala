
package vehiculo

import scala.util.Random

class MotoTaxi extends Vehiculo() {

  val placa: String = {

    val letra1: String = Random.alphanumeric.filter(_.isLetter).head.toString
    val letra2: String = Random.alphanumeric.filter(_.isLetter).head.toString
    val letra3: String = Random.alphanumeric.filter(_.isLetter).head.toString

    val num1: Int = Random.nextInt(10)
    val num2: Int = Random.nextInt(10)
    val num3: Int = Random.nextInt(10)

    s"$num1$num2$num3$letra1$letra2$letra3"
  }

}
