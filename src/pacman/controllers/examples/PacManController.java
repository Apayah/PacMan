package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManController extends Controller<Constants.MOVE> /*    */ {

    private MCTS mcts;
    int live = 3;
    int level = 0;
    int treeuse = 0;
    long lastReverseTime = 0;

    public Constants.MOVE getMove(Game game, long timeDue) /*    */ {

        Constants.MOVE m = null;

        if (mcts == null) {

            //Move pertama
            m = Constants.MOVE.NEUTRAL;
            mcts = new MCTS(game);
            mcts.playMove(m);
        } else {
            mcts.updateGameState(game);
        }
        //Kalau bukan move pertama

        if (m == null) {
            TreeNode selected = null;
            if (mcts.isAtJunction() || mcts.isAtWall()) {

                while (System.currentTimeMillis() < timeDue - 4) {
                    mcts.run();
                }
                selected = mcts.getBestNode();
                if (selected == null) {
                    mcts = new MCTS(game);
                    treeuse =0;
                    return game.getPacmanLastMoveMade().opposite();
                }
                m = selected.move;
            }
            //Tree Reuse
            if (live > game.getPacmanNumberOfLivesRemaining() || level != game.getCurrentLevel() || lastReverseTime != game.getTimeOfLastGlobalReversal()) {
                mcts = new MCTS(game);
                treeuse =0;
            } else {
                if (selected != null) {
                    mcts.root = selected;
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
        treeuse++;
        if(treeuse > 15){
            mcts = new MCTS(game);
            treeuse =0;
        }
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