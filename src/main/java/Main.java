import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Main {
	
    public static void main(String[] args) throws Exception
    {	
		System.out.println("Run from Graphics, Delete Main!");
    }
}
		
/* Ejemplos de prueba:

SIN SOLUCION
############
#.         #
############
#     #    #
#$@        #
############


MUY SIMPLE:

#######
# .   #
##$####
## #
##@#
####

FACIL (BFS da 9 pasos):
########
##. $ ##
# .    #
##$ ####
##  #
##@ #
#####

FACIL (BFS da 11 pasos):
######
#  .##
#  # #
#  $ #
##@  #
######

FACIL (BFS da 20 pasos)
##########
#        #
# ##$### #
# #... # #
#  $#$$. #
#  * @## #
#        #
##########

INTERMEDIO (BFS da 32 pasos):
  #####
###  .#
#.@$  ##
#####  ##
#. .# $ #
#$ $#   #
#       #
#########

INTERMEDIO (BFS da 35 pasos)
 #########
##       #
# $##### ##
# @  .#   #
#.  $.$   #
###########

COMPLICADO (BFS da 60 pasos):
     #####
     #   #
     #   #
#### #   #
#  ####$#####
#           #
# .## # ##. #
#           #
#####$####  #
   #   # ####
   # @ #
   #   #
   #####

DIFICIL (BFS da 78 pasos)
      ###
      #.#
  #####.#####
 ##         ##
##  # # # #  ##
#  ##     ##  #
# ##  # #  ## #
#     $@$     #
####  ###  ####
   #### ####

DIFICIL (BFS da 83 pasos)
############
##      #  #
#  #.   #  #
#      .#  #
##$$##@##$$##
 #  #.      #
 #  #   .#  #
 #  #      ##
 ###########

DIFICIL (BFS da 93 pasos)
         ####
         #  #
      ####$ ####
      #        #
      # ##.### #
      # ###### #####
      #  +$ ## #   #
  ######### #. $   #
  #   ##.## ## #   #
### #       ## #####
# $   # $##### #
# ###.#  #   $.#
#   ######    ##
#   $.     ####
############


NUEVO (BFS 49)
     #####
    ##   #
    #    #
  ###    ######
  #.# # ##    #
### ###  ##   #
#   #     ## ##
#     $@      #
#   #  $  #   #
######   ### ##
 #  .## #### #
 #           #
 ##  #########
  ####

*/
