# Simulación - Scala


### Integrantes:
* Nicolás Capdet Martínez
* Alejandro Ortiz Mejía
* Hailer Serna Hernández


### Pendiente:

* Utilice datos inmutables siempre que sea posible (Tanto para atributos como para listas). Cuando el atributo sea inmutable lo puede declarar público; cuando sea mutable debe declararlo privado y crear el getter y setter según estándar de Scala.
* Aunque al corregir el parpadeo de los vehículos se corrigió el error que más salía en la ejecución de la simulación, sigue habiendo unos leves errores que salen muy de vez en cuando, es posible que esto se deba a los hilos. Hay que corregir todos estos errores.
##### Correciones pendientes hechas por el profesor:
* Mal uso de hilos. Consultar la documentación de Thready Runnable.
* La llamada a shortestPath en Simulacion debería ir en GrafoVia.
* No funciona F5 ni F6. El color de Interseccion es algo gráfico separado del modelo lógico. Debería determinarse en la clase Grafico y no en la clase Interseccion.
* El color de Vehiculo es algo gráfico separado del modelo lógico. Debería determinarse en la clase Grafico y no en la clase Vehiculo.
* Preferir Match sobre If o Else if.
* Faltan algunos Getters y Setters.