package grafo

import infraestructura.Interseccion
import infraestructura.via.Via
import scalax.collection.edge.WLUnDiEdge
import scalax.collection.mutable.Graph



object GrafoVia {

  val grafo = Graph[Interseccion, WLUnDiEdge]()

  def construir(vias: Array[Via]): Unit = {

    val intersecciones: Array[Interseccion] = (vias.map(via => via.origen) ++ vias.map(via => via.fin)).distinct

    intersecciones.foreach(interseccion => grafo.add(interseccion))

    vias.foreach(via => grafo.add(WLUnDiEdge(via.origen, via.fin)(via.longitud, via.nombreIdentificador())))
  }

}
