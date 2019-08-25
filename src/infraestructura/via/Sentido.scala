
package infraestructura.via

case class Sentido(nombre: String) {
}

object Sentido{
  def dobleVia: Sentido = {
    new Sentido("dobleVia")
  }

  def unaVia: Sentido ={
    new Sentido("unaVia")
  }
}
