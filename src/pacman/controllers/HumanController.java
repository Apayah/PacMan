/*    */ package pacman.controllers;
/*    */ 
/*    */ import pacman.game.Constants;
/*    */ import pacman.game.Game;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HumanController
/*    */   extends Controller<Constants.MOVE>
/*    */ {
/*    */   public KeyBoardInput input;
/*    */   
/*    */   public HumanController(KeyBoardInput input)
/*    */   {
/* 16 */     this.input = input;
/*    */   }
/*    */   
/*    */   public KeyBoardInput getKeyboardInput()
/*    */   {
/* 21 */     return this.input;
/*    */   }
/*    */   
/*    */   public Constants.MOVE getMove(Game game, long dueTime)
/*    */   {
/* 26 */     switch (this.input.getKey()) {
/*    */     case 38: 
/* 28 */       return Constants.MOVE.UP;
/* 29 */     case 39:  return Constants.MOVE.RIGHT;
/* 30 */     case 40:  return Constants.MOVE.DOWN;
/* 31 */     case 37:  return Constants.MOVE.LEFT; }
/* 32 */     return Constants.MOVE.NEUTRAL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\controllers\HumanController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */