package pacman.controllers.examples;

import java.util.ArrayList;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.controllers.examples.GhostsController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author Adit
 */
public class PacManSimulator extends Controller<Constants.MOVE> {

    GhostsController gc;

    PacManSimulator(GhostsController gc) {
        this.gc = gc;
    }

    public Constants.MOVE getMove(Game game, long timedue) {
        Game sim;

        Constants.MOVE chosen = MOVE.NEUTRAL;
        int curr = game.getPacmanCurrentNodeIndex();

        if (game.isJunction(curr) || isAtWall(game)) {
            sim = game.copy();

            ArrayList safeMoves = new ArrayList();

            Constants.MOVE[] possibleMoves = sim.getPossibleMoves(curr);
            int live = game.getPacmanNumberOfLivesRemaining();
            for (Constants.MOVE m : possibleMoves) {
                if (m != sim.getPacmanLastMoveMade().opposite()) {
                    sim = game.copy();
                    int score = sim.getScore();
                    sim.advanceGame(m, gc.getMove(sim, 0));
                    while (!sim.isJunction(sim.getPacmanCurrentNodeIndex()) && !isAtWall(sim)) {
                        sim.advanceGame(m, gc.getMove(sim, 0));
                    }
                    if (sim.getScore() > score && sim.getPacmanNumberOfLivesRemaining() >= live) {
                        safeMoves.add(m);
                    }
                }
            }
            Random random = new Random();
            if (safeMoves.size() > 0) {
                chosen = (MOVE) safeMoves.get(random.nextInt(safeMoves.size()));
            } else {
                while (chosen == game.getPacmanLastMoveMade().opposite()) {
                    chosen = possibleMoves[random.nextInt(possibleMoves.length)];
                }
            }
        }
        return chosen;
    }

    public boolean isAtWall(Game game) {
        MOVE move = game.getPacmanLastMoveMade();
        MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());

        for (int i = 0; i < possibleMoves.length; i++) {
            if (possibleMoves[i] == move) {
                return false;
            }
        }
        return true;
    }
}