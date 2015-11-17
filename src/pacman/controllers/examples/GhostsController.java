/*     */ package pacman.controllers.examples;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import pacman.controllers.Controller;
          import pacman.game.Constants;
/*     */ import pacman.game.Game;
/*     */ 
/*     */ 
/*     */ public class GhostsController extends Controller<EnumMap<Constants.GHOST, Constants.MOVE>>
/*     */ {
/*     */   public static final int CROWDED_DISTANCE = 30;
/*     */   public static final int PACMAN_DISTANCE = 10;
/*     */   public static final int PILL_PROXIMITY = 15;
/*  17 */   private final EnumMap<Constants.GHOST, Constants.MOVE> myMoves = new EnumMap(Constants.GHOST.class);
/*  18 */   private final EnumMap<Constants.GHOST, Integer> cornerAllocation = new EnumMap(Constants.GHOST.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GhostsController()
/*     */   {
/*  25 */     this.cornerAllocation.put(Constants.GHOST.BLINKY, Integer.valueOf(0));
/*  26 */     this.cornerAllocation.put(Constants.GHOST.INKY, Integer.valueOf(1));
/*  27 */     this.cornerAllocation.put(Constants.GHOST.PINKY, Integer.valueOf(2));
/*  28 */     this.cornerAllocation.put(Constants.GHOST.SUE, Integer.valueOf(3));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue)
/*     */   {
/*  36 */     int pacmanIndex = game.getPacmanCurrentNodeIndex();
/*     */     Constants.GHOST[] arrayOfGHOST;
/*  38 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghost = arrayOfGHOST[i];
/*     */       
/*  40 */       if (game.doesGhostRequireAction(ghost))
/*     */       {
/*  42 */         int currentIndex = game.getGhostCurrentNodeIndex(ghost);
/*     */         
/*     */ 
/*  45 */         if ((isCrowded(game)) && (!closeToMsPacMan(game, currentIndex))) {
/*  46 */           this.myMoves.put(ghost, getRetreatActions(game, ghost));
/*     */         }
/*  48 */         else if ((game.getGhostEdibleTime(ghost) > 0) || (closeToPower(game))) {
/*  49 */           this.myMoves.put(ghost, game.getApproximateNextMoveAwayFromTarget(currentIndex, pacmanIndex, game.getGhostLastMoveMade(ghost), Constants.DM.PATH));
/*     */         }
/*     */         else {
/*  52 */           this.myMoves.put(ghost, game.getApproximateNextMoveTowardsTarget(currentIndex, pacmanIndex, game.getGhostLastMoveMade(ghost), Constants.DM.PATH));
/*     */         }
/*     */       }
/*     */     }
/*  56 */     return this.myMoves;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean closeToPower(Game game)
/*     */   {
/*  67 */     int pacmanIndex = game.getPacmanCurrentNodeIndex();
/*  68 */     int[] powerPillIndices = game.getActivePowerPillsIndices();
/*     */     
/*  70 */     for (int i = 0; i < powerPillIndices.length; i++) {
/*  71 */       if (game.getShortestPathDistance(powerPillIndices[i], pacmanIndex) < 15)
/*  72 */         return true;
/*     */     }
/*  74 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean closeToMsPacMan(Game game, int location)
/*     */   {
/*  86 */     if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), location) < 10) {
/*  87 */       return true;
/*     */     }
/*  89 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isCrowded(Game game)
/*     */   {
/* 100 */     Constants.GHOST[] ghosts = Constants.GHOST.values();
/* 101 */     float distance = 0.0F;
/*     */     
/* 103 */     for (int i = 0; i < ghosts.length - 1; i++) {
/* 104 */       for (int j = i + 1; j < ghosts.length; j++)
/* 105 */         distance += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghosts[i]), game.getGhostCurrentNodeIndex(ghosts[j]));
/*     */     }
/* 107 */     return distance / 6.0F < 30.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Constants.MOVE getRetreatActions(Game game, Constants.GHOST ghost)
/*     */   {
/* 119 */     int currentIndex = game.getGhostCurrentNodeIndex(ghost);
/* 120 */     int pacManIndex = game.getPacmanCurrentNodeIndex();
/*     */     
/* 122 */     if ((game.getGhostEdibleTime(ghost) == 0) && (game.getShortestPathDistance(currentIndex, pacManIndex) < 10)) {
/* 123 */       return game.getApproximateNextMoveTowardsTarget(currentIndex, pacManIndex, game.getGhostLastMoveMade(ghost), Constants.DM.PATH);
/*     */     }
/* 125 */     return game.getApproximateNextMoveTowardsTarget(currentIndex, game.getPowerPillIndices()[((Integer)this.cornerAllocation.get(ghost)).intValue()], game.getGhostLastMoveMade(ghost), Constants.DM.PATH);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\controllers\examples\Legacy2TheReckoning.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */