# SIA-TP1

## Instrucciones de ejecución:

1. Una vez clonado el repositorio, dentro de la carpeta JavaFX en la raíz del mismo encontraremos tres archivos ZIP.

2. Descomprimimos el correspondiente a nuestro sistema operativo.

3. Dentro de una terminal, nos desplazamos a /src/main/java desde la raíz del repositorio

4. Ejecutamos los siguientes comandos para compilar.
Si javac no es reconocido como comando, es posible que haya que configurar correctamente las variables de entorno para que la línea de comandos lo reconozca.
Si los flags de javac son rechazados, se recomienda probar con una versión de Java superior a 8.

* Si estamos en Linux/Mac:
`export PATH_TO_FX=../../../JavaFX/javafx-sdk-11.0.2/lib`
`javac --module-path $PATH_TO_FX --add-modules javafx.controls Main.java`

* Si estamos en Windows:
`SET PATH_TO_FX="../../../JavaFX/javafx-sdk-11.0.2/lib"`
`javac --module-path %PATH_TO_FX% --add-modules javafx.controls Main.java`

5. Una vez compilado el proyecto, podemos seleccionar las configuraciones para ejecutar el solver. 

* En el archivo ``map.conf`` (ubicado en la raíz del repositorio) podemos pegar el ASCII de mapa que queremos evaluar (ejemplos disponibles en la carpeta 'Example-maps'), por ejemplo:  

```javascript
########
##. $ ##
# .    #
##$ ####
##  #
##@ #
##### 
```

* En el archivo ``settings.conf`` (ubicado en la raíz del repositorio) podemos elegir en primer instancia el algoritmo de búsqueda y luego la heurística deseada, en líneas separadas, por ejemplo:

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
* MIX_1_2: Combinación CLOSEST_GOAL con CLOSEST_BOX cada uno con coeficiente 0.5
* MIX_1_3: Combinación CLOSEST_GOAL con BOXES_REMAINING cada uno con coeficiente 0.5
* MIX_2_3: Combinación CLOSEST_BOX con BOXES_REMAINING cada uno con coeficiente 0.5
* ALL_MIXED: Combinación de las 3 heurísticas, CLOSEST_GOAL y REMAINING_BOXES con coeficiente 0.4 y CLOSEST_BOX con coeficiente 0.2

6. Una vez seleccionadas las configuraciones deseadas, podemos ejecutar el programa con el siguiente comando.

* Si estamos en Linux/Mac:

``java --module-path $PATH_TO_FX --add-modules javafx.controls Main``

* Si estamos en Windows:
``java --module-path %PATH_TO_FX% --add-modules javafx.controls Main``

En caso de realizar modificaciones sobre los archivos de configuración, solo se debe ejecutar este último comando, no se requiere compilar de nuevo.


## Configuraciones recomendadas:

* Configuración 1:
Settings (La heurística elegida da igual ya que BFS es desinformado):
```java
BFS
ALL_MIXED
```

Map:
```javascript
 #########
##       #
# $##### ##
# @  .#   #
#.  $.$   #
###########
```

* Configuración 2:
Settings:
```java
A*
BOXES_REMAINING
```

Map:
```javascript
  #####
###  .#
#.@$  ##
#####  ##
#. .# $ #
#$ $#   #
#       #
#########
```