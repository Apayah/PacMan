/*    */ package pacman.game.internal;
/*    */ 
/*    */ import pacman.game.Constants;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PacMan
/*    */ {
/*    */   public int currentNodeIndex;
/*    */   public int numberOfLivesRemaining;
/*    */   public Constants.MOVE lastMoveMade;
/*    */   public boolean hasReceivedExtraLife;
/*    */   
/*    */   public PacMan(int currentNodeIndex, Constants.MOVE lastMoveMade, int numberOfLivesRemaining, boolean hasReceivedExtraLife)
/*    */   {
/* 16 */     this.currentNodeIndex = currentNodeIndex;
/* 17 */     this.lastMoveMade = lastMoveMade;
/* 18 */     this.numberOfLivesRemaining = numberOfLivesRemaining;
/* 19 */     this.hasReceivedExtraLife = hasReceivedExtraLife;
/*    */   }
/*    */   
/*    */   public PacMan copy()
/*    */   {
/* 24 */     return new PacMan(this.currentNodeIndex, this.lastMoveMade, this.numberOfLivesRemaining, this.hasReceivedExtraLife);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\PacMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */