import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Main {
	
    public static void main(String[] args) throws Exception
    {	
		// Read config file
        String algorithm;
        String heuristic;
		try {
			List<String> configurations = Files.readAllLines(Paths.get("settings.conf"));
			algorithm = configurations.get(0);
			heuristic = configurations.get(1);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Algorithm: " +algorithm +"\nHeuristic: " +heuristic);
        
    	// Read initial board file
        String initBoard;
		try {
			initBoard = String.join("\n", Files.readAllLines(Paths.get("map.conf")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        Board b = new Board(initBoard);
        
        // Solve the challenge
        ArtificialIntelligence artificialIntelligence = new ArtificialIntelligence(algorithm);
        artificialIntelligence.solve(b);
		
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


    }
}
