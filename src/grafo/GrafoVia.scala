
package grafo

import infraestructura.Interseccion
import infraestructura.via.Via
import scalax.collection.edge.WLUnDiEdge
import scalax.collection.mutable.Graph

object GrafoVia {

  val grafo = Graph[Interseccion, WLUnDiEdge]()

  def construir(vias: Array[Via]): Unit = {
    val intersecciones: Array[Interseccion] = (vias.map(_.origen) ++ vias.map(_.fin)).distinct
    intersecciones.foreach(grafo.add(_))

    vias.foreach(via => grafo.add(WLUnDiEdge(via.origen, via.fin)(via.longitud, via.nombreIdentificador())))
  }
}
