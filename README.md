# SIA-TP1

## Instrucciones de ejecución:

1. Una vez clonado el repositorio, dentro de la carpeta JavaFX en la raíz del mismo encontraremos tres archivos ZIP.

2. Descomprimimos el correspondiente a nuestro sistema operativo.

3. Dentro de una terminal, nos desplazamos a /src/main/java desde la raíz del repositorio

4. Ejecutamos los siguientes comandos para compilar.

* Si estamos en Linux/Mac:
`export PATH_TO_FX=../../../JavaFX/javafx-sdk-11.0.2/lib`
`javac --module-path $PATH_TO_FX --add-modules javafx.controls Graphics.java`

* Si estamos en Windows:
`SET PATH_TO_FX="../../../JavaFX/javafx-sdk-11.0.2/lib"`
`javac --module-path %PATH_TO_FX% --add-modules javafx.controls Graphics.java`

5. Una vez compilado el proyecto, podemos seleccionar las configuraciones para ejecutar el solver. 

* En el archivo ``map.conf`` podemos pegar el ASCII de mapa que queremos evaluar, por ejemplo:  

```javascript
########
##. $ ##
# .    #
##$ ####
##  #
##@ #
##### 
```

* En el archivo ``settings.conf`` podemos elegir en primer instancia el algoritmo de búsqueda y luego la heurística deseada, en líneas separadas, por ejemplo:

```java
BFS
CLOSEST_GOAL
```

Los posibles valores para algoritmos son (case sensitive):
* BFS
* DFS
* IDDFS
* GGS
* A*
* IDA*

Los posibles valores para heurísticas son (case sensitive):
* CLOSEST_GOAL: sumatoria de las distancias manhattan mínimas de cada caja fuera de un goal, a alguno de los goals disponibles
* CLOSEST_BOX: distancia manhattan mínima de una caja fuera de un goal a la posición actual del jugador
* BOXES_REMAINING: cantidad de cajas fuera de goals

6. Una vez seleccionadas las configuraciones deseadas, podemos ejecutar el programa con el siguiente comando.

* Si estamos en Linux/Mac:

``java --module-path $PATH_TO_FX --add-modules javafx.controls Graphics``

* Si estamos en Windows:
``java --module-path %PATH_TO_FX% --add-modules javafx.controls Graphics``

En caso de realizar modificaciones sobre los archivos de configuración, solo se debe ejecutar este último comando, no se requiere compilar de nuevo.