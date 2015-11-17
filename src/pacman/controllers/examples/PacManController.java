package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManController extends Controller<Constants.MOVE> /*    */ {

    private Simulator simulator;
    int live = 3;
    int level = 0;
    long lastReverseTime = 0;

    public Constants.MOVE getMove(Game game, long timeDue) /*    */ {

        Constants.MOVE m = null;

        if (simulator == null) {

            //Move pertama
            m = Constants.MOVE.NEUTRAL;
            simulator = new Simulator(game);
            simulator.playMove(m);
        } else {
            simulator.updateGameState(game);
        }
        //Kalau bukan move pertama

        if (m == null) {
            TreeNode selected = null;
            if (simulator.isAtJunction() || simulator.isAtWall()) {

                while (System.currentTimeMillis() < timeDue - 3) {
                    simulator.run();
                }
                selected = simulator.getBestNode();
                if (selected == null) {
                    simulator = new Simulator(game);
                    return game.getPacmanLastMoveMade().opposite();
                }
                m = selected.move;
            }
            //Tree Reuse
            if (live > game.getPacmanNumberOfLivesRemaining() || level != game.getCurrentLevel() || lastReverseTime != game.getTimeOfLastGlobalReversal()) {
                simulator = new Simulator(game);
            } else {
                if (selected != null) {
                    simulator.root = selected;
                }
            }
        } else {
            m = Constants.MOVE.NEUTRAL;
        }
        if (System.currentTimeMillis() > timeDue) {
            System.out.println("too late");
        }
        live = game.getPacmanNumberOfLivesRemaining();
        level = game.getCurrentLevel();
        lastReverseTime = game.getTimeOfLastGlobalReversal();
        return m;
    }

    public boolean isAtWall(Game game) {
        Constants.MOVE move = game.getPacmanLastMoveMade();
        Constants.MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());

        for (int i = 0; i < possibleMoves.length; i++) {
            if (possibleMoves[i] == move) {
                return false;
            }
        }
        return true;
    }
}






/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\controllers\examples\PacManController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */