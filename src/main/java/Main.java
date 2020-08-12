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
        
        ArtificialIntelligence artificialIntelligence = new ArtificialIntelligence("GGS", 320, "MANHATTAN");
        artificialIntelligence.solve(b);
	}
}
/* Ejemplos de prueba:

MUY SIMPLE:

#######
# .   #
##$####
## #
##@#
####

FACIL (BFS da 11 pasos):
######
#  .##
#  # #
#  $ #
##@  #
######

FACIL:
########
##. $ ##
# .    #
##$ ####
##  #
##@ #
#####

INTERMEDIO (BFS da 34 pasos):
  #####
###  .#
#.@$  ##
#####  ##
#. .# $ #
#$ $#   #
#       #
#########

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
   
IMPOSIBLE (BFS da 83 pasos, tard� 22 minutos!)
############
##      #  #
#  #.   #  #
#      .#  #
##$$##@##$$##
 #  #.      #
 #  #   .#  #
 #  #      ##
 ###########
*/

