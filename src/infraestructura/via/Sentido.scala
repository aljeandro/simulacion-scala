
package infraestructura.via

class Sentido(private val _nombre: String) {
  def nombre: String = _nombre
}

object Sentido{
  def dobleVia: Sentido = {
    new Sentido("dobleVia")
  }

  def unaVia: Sentido ={
    new Sentido("unaVia")
  }
}
