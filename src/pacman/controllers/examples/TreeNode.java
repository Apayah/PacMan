package pacman.controllers.examples;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author Adit
 */
public class TreeNode {

    public MOVE move;
    public TreeNode parent;
    public int nodeindex, score, bonus, visited, survival;
    public double mean;
    public Map<Object, TreeNode> children;

    public TreeNode() {
        this.score = 0;
        this.survival = 1;
        this.visited = 0;
        this.parent = null;
        this.move = MOVE.NEUTRAL;
        this.nodeindex = -1;
        this.mean = 0;
    }

    public TreeNode(MOVE move, TreeNode parent) {
        this();
        this.move = move;
        this.parent = parent;        
    }

    public void updateScore(int score) {
        visited++;
        if (visited == 1) {
            mean = score;
        } else {
            double lastMean = mean;
            mean += (score - lastMean) / visited;
        }
    }

    public void updateBonus(int bonus) {
        this.bonus = bonus;
    }

    public void updateSurvival(int survival) {
        this.survival = survival;
    }

    //Expand node, cek semua gerakan yang mungkin, kemudian buat nodenya
    public void expand(Game game) {
        this.nodeindex = game.getPacmanCurrentNodeIndex();
        Constants.MOVE[] m = game.getPossibleMoves(nodeindex);
        children = new HashMap<>(m.length);

        for (int i = 0; i < m.length; i++) {
            if (m[i] != game.getPacmanLastMoveMade().opposite()) {
                children.put(m[i], new TreeNode(m[i], this));
            }
        }
    }

    public boolean isLeafNode() {
        return children == null;
    }

    public Collection<TreeNode> getChildren() {
        if (isLeafNode()) {
            return null;
        }
        return children.values();
    }

    public TreeNode getChild(MOVE move) {
        if (isLeafNode()) {
            return null;
        }
        return children.get(move);
    }

    public double getTotalScore() {
        return score + bonus;
    }

    public double getUCB() {
        return (mean + bonus) * (Math.log(this.parent.visited) / this.visited);
    }
}
