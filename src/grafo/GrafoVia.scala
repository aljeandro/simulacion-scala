
package grafo

import infraestructura.Interseccion
import infraestructura.via.{Via, Sentido}
import simulacion.Simulacion

import scalax.collection.edge.WLDiEdge
import scalax.collection.mutable.Graph

object GrafoVia {

  val grafo = Graph[Interseccion, WLDiEdge]()

  def construir(vias: Array[Via]): Unit = {
    // Se obtienen todas las vias que son de doble sentido.
    val viasDobleSentido: Array[Via] = vias.filter(_.sentido.nombre == "dobleVia")

    // Se llena el Array de las vias dirigidas que se encuentra en Simulación.
    Simulacion.viasDirigidas = vias ++ viasDobleSentido.map(via =>
      new Via(via.fin, via.origen, via.velocidadMax, via.tipoVia, Sentido.unaVia, via.numeroVia, via.nombre))

    // Se agrega todas las intersecciones al grafo.
    Simulacion.intersecciones.foreach(interseccion => grafo.add(interseccion))

    // Se agregan las vias al grafo.
    Simulacion.viasDirigidas.foreach(via =>
      grafo.add(WLDiEdge(via.origen, via.fin)(via.longitud, via.nombreIdentificador())))
  }

  // Al mandarle dos intersecciones, me retorna el camino más corto.
  def getCamino(origen: Interseccion, destino: Interseccion): Option[grafo.Path] ={
    GrafoVia.grafo.get(origen) shortestPathTo GrafoVia.grafo.get(destino)
  }
}
