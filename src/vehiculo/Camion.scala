
package vehiculo

import scala.util.Random

class Camion extends Vehiculo() {

  val placa: String = {
    val num1: Int = Random.nextInt(10)
    val num2: Int = Random.nextInt(10)
    val num3: Int = Random.nextInt(10)
    val num4: Int = Random.nextInt(10)
    val num5: Int = Random.nextInt(10)

    s"R$num1$num2$num3$num4$num5"
  }
}
