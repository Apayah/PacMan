package pacman.controllers.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author Adit
 */
public class MCTS {

    TreeNode root;
    Game game;
    Stack<Game> gamestates;
    GhostsController ghostcontroller = new GhostsController();
    PacManSimulator sim = new PacManSimulator(ghostcontroller);
    RandomPacMan rndpm = new RandomPacMan();
    int depth = 0;

    public MCTS(Game game) {
        this.game = game;
        this.gamestates = new Stack();
        this.root = new TreeNode();
    }

    public TreeNode getBestNode() {
        double currentScore, max = Double.NEGATIVE_INFINITY;
        TreeNode bestNode = null;

        addBonusScore();
        nearestPillScore();

        if (root.getChildren() != null) {
            for (TreeNode node : root.getChildren()) {
                currentScore = (node.mean + 1 + node.bonus) * node.survival;
                System.out.print(node.move + " " + currentScore + " ");
                if (currentScore > max) {
                    max = currentScore;
                    bestNode = node;
                }
            }
        }
        System.out.println();
        if (bestNode.survival == 0) {
            return null;
        }
        return bestNode;
    }

    void updateGameState(Game game) {
        this.game = game;
    }

    void run() {
        int lives = game.getPacmanNumberOfLivesRemaining();

        try {
            //simpan gamestate sekarang
            gamestates.push(game);
            game = game.copy();

            TreeNode node = root;
            advanceGameToNextNode();

            //Selama bukan leaf telusuri pohon

            while (!node.isLeafNode() && depth <= 5) {
                depth++;

                //pilih child node 
                double max = Double.NEGATIVE_INFINITY;
                for (HashMap.Entry pair : node.children.entrySet()) {
                    TreeNode child = (TreeNode) pair.getValue();
                    if (child.getUCB() > max) {
                        max = child.getUCB();
                        node = child;
                    }
                }

                //update gamestate sesuai node yang dipilih                
                game.advanceGame(node.move, ghostcontroller.getMove(game, 0));
                advanceGameToNextNode();
                if (game.getPacmanNumberOfLivesRemaining() < lives) {
                    node.updateSurvival(0);
                    return;
                }
            }

            if (node.children == null) {
                node.expand(game);
            }
            //Pilih child node
            //**********************

            Random r = new Random();
            List<MOVE> keys = new ArrayList(node.children.keySet());
            MOVE randomKey = keys.get(r.nextInt(keys.size()));
            node = node.children.get(randomKey);

            game.advanceGame(node.move, ghostcontroller.getMove(game, 0));
            advanceGameToNextNode();

            if (game.getPacmanNumberOfLivesRemaining() < lives) {
                node.updateSurvival(0);
                return;
            }

            int[] res = playout();

            while (node != root) {
                if (node.parent.survival < node.survival) {
                    node.parent.survival = node.survival;
                }
                node.updateScore(res[0]);
                node = node.parent;
            }

        } finally {
            //kembalikan gamestate ke state sebelum simulasi
            game = gamestates.pop();
        }
    }

    private int[] playout() {
        int level = game.getCurrentLevel();
        int i = 0;
        int[] res = new int[2];
        res[0] = game.getScore();
        res[1] = 1;
        int live = game.getPacmanNumberOfLivesRemaining();

        //lakukan simulasi sampai game over, atau level berakhir, atau jumlah maximum simulasi dicapai
        long duetime = System.currentTimeMillis() + 2;
        while (System.currentTimeMillis() < duetime && !game.gameOver() && game.getCurrentLevel() == level) {
            game.advanceGame(sim.getMove(game, 0), ghostcontroller.getMove(game, 0));
            if (game.getPacmanNumberOfLivesRemaining() < live) {
                res[0] = game.getScore() - res[0];
                res[1] = 0;
                return res;
            }
            i++;
        }
        //update the score
        res[0] = game.getScore() - res[0];

        if (game.gameOver()) {
            res[0] = 0;
            res[1] = 0;
        }

        if (game.getCurrentLevel() != level) {
            res[0] = res[0] * 10;
        }
        return res;
    }

    private void advanceGameToNextNode() {
        MOVE move = game.getPacmanLastMoveMade();

        while (!isAtJunction() && !isAtWall()) {
            game.advanceGame(move, ghostcontroller.getMove(game, 0));
        }
    }

    void playMove(MOVE m) {
        game.advanceGame(m, ghostcontroller.getMove(game, 0));
    }

    //Cek apakah pac-man ada di junction
    public boolean isAtJunction() {
        int nodeIndex = game.getPacmanCurrentNodeIndex();
        return game.isJunction(nodeIndex);
    }

    //Cek apakah pac-man di depan tembok
    public boolean isAtWall() {
        MOVE move = game.getPacmanLastMoveMade();
        MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());

        for (int i = 0; i < possibleMoves.length; i++) {
            if (possibleMoves[i] == move) {
                return false;
            }
        }
        return true;
    }

    //Return children dari root, jika tidak ada children maka return List kosong
    public Collection<TreeNode> getPacManChildren() {
        Collection<TreeNode> children;

        children = root.getChildren();
        if (children != null) {
            return children;
        } else {
            return new ArrayList<TreeNode>();
        }
    }

    public void addBonusScore() {
        if (root.children == null) {
            return;
        }

        int currentIndex = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getActivePillsIndices();
        int closestIndex = game.getClosestNodeIndexFromNodeIndex(currentIndex, pills, DM.PATH);

        MOVE m = game.getNextMoveTowardsTarget(currentIndex, closestIndex, DM.PATH);

        Collection<TreeNode> children = getPacManChildren();
        for (TreeNode n : children) {
            if (n.move == m) {
                n.updateScore(400);
            }
        }
    }

    public void nearestPillScore() {

        double distance = getDistanceToNeartestPill(game);
        for (TreeNode child : getPacManChildren()) {

            gamestates.push(game);
            game = game.copy();
            playMove(child.move);

            double d = getDistanceToNeartestPill(game);

            if (d < distance) {
                child.updateScore(((game.getNumberOfPills() - game.getNumberOfActivePills()) * 2));
            }
            game = gamestates.pop();
        }
    }

    private double getDistanceToNeartestPill(Game game) {
        //get the closest pill to the current position
        int[] pills = game.getActivePillsIndices();

        if (pills.length == 0) {
            return Double.MAX_VALUE;
        }

        int currentIndex = game.getPacmanCurrentNodeIndex();
        int closestPill = game.getClosestNodeIndexFromNodeIndex(currentIndex, pills, DM.MANHATTAN);

        //return the distance to it
        return game.getDistance(currentIndex, closestPill, DM.MANHATTAN);
    }
}
