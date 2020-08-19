# SIA-TP1

## Instrucciones de descarga:

Ingresar al siguiente link: https://gluonhq.com/products/javafx/  

Descargar el SDK correspondiente para su computadora (Linux, Mac o Windows) y recordar el directorio de la descarga

``/Users/user/Downloads/javafx-sdk-11.0.2`` < utilizamos este path como ejemplo

## Instrucciones de compilación del proyecto:

Una vez clonado el repositorio, en la terminal dirigirse a /src/main/java y ejecutar los siguientes comandos en orden:

`export PATH_TO_FX=/Users/user/Downloads/javafx-sdk-11.0.2/lib`  
`javac --module-path $PATH_TO_FX --add-modules javafx.controls Graphics.java`  

## Instrucciones de ejecución del programa:

Una vez compilado el proyecto, podemos seleccionar las configuraciones para ejecutar el solver. 

En el archivo ``map.conf`` podemos pegar el ASCII de mapa que queremos evaluar, por ejemplo:  

```javascript
########
##. $ ##
# .    #
##$ ####
##  #
##@ #
##### 
```

* En el archivo ``settings.conf`` podemos elegir en primer instancia el algoritmo de búsqueda y luego la heurística deseada, por ejemplo:

```java
BFS
CLOSEST_GOAL
```

Los posibles valores para algoritmos son:
(*case sensitive)
* BFS
* DFS
* IDDFS
* GGS
* A*
* IDA*

Los posibles valores para heurísticas son:
(*case sensitive)  
* CLOSEST_GOAL: sumatoria de las distancias manhattan mínimas de cada caja fuera de un goal, a alguno de los goals disponibles
* CLOSEST_BOX: distancia manhattan mínima de una caja fuera de un goal a la posición actual del jugador
* BOXES_REMAINING: cantidad de cajas fuera de goals

Una vez seleccionadas las configuraciones deseadas, podemos ejecutar el programa con el siguiente comando:

``java --module-path $PATH_TO_FX --add-modules javafx.controls Graphics``

En caso de realizar modificaciones sobre los archivos de configuración, solo se debe ejecutar este último comando, no se requiere compilar de nuevo.