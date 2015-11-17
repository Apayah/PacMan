package pacman.controllers.examples;

import java.util.ArrayList;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author Adit
 */
public class RandomPacMan extends Controller<Constants.MOVE> {

    private Random rnd = new Random();
    private Constants.MOVE[] allMoves = Constants.MOVE.values();    

    public Constants.MOVE getMove(Game game, long timeDue) {
        
        ArrayList chosenMoves = new ArrayList();
        for(int i=0 ; i <allMoves.length ;i++){
            if(allMoves[i] != game.getPacmanLastMoveMade()){
                chosenMoves.add(allMoves[i]);
            }
        }
        return (MOVE) chosenMoves.get(rnd.nextInt(chosenMoves.size()));
    }
}
