/*    */ package pacman.game.internal;
/*    */ 
/*    */  import pacman.game.Constants;
/*    */ 
/*    */ 
/*    */ public final class Ghost
/*    */ {
/*    */   public int currentNodeIndex;
/*    */   public int edibleTime;
/*    */   public int lairTime;
/*    */   public Constants.GHOST type;
/*    */   public Constants.MOVE lastMoveMade;
/*    */   
/*    */   public Ghost(Constants.GHOST type, int currentNodeIndex, int edibleTime, int lairTime, Constants.MOVE lastMoveMade)
/*    */   {
/* 17 */     this.type = type;
/* 18 */     this.currentNodeIndex = currentNodeIndex;
/* 19 */     this.edibleTime = edibleTime;
/* 20 */     this.lairTime = lairTime;
/* 21 */     this.lastMoveMade = lastMoveMade;
/*    */   }
/*    */   
/*    */   public Ghost copy()
/*    */   {
/* 26 */     return new Ghost(this.type, this.currentNodeIndex, this.edibleTime, this.lairTime, this.lastMoveMade);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\Ghost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */