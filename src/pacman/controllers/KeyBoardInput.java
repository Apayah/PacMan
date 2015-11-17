/*    */ package pacman.controllers;
/*    */ 
/*    */ import java.awt.event.KeyAdapter;
/*    */ import java.awt.event.KeyEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyBoardInput
/*    */   extends KeyAdapter
/*    */ {
/*    */   private int key;
/*    */   
/*    */   public int getKey()
/*    */   {
/* 15 */     return this.key;
/*    */   }
/*    */   
/*    */   public void keyPressed(KeyEvent e)
/*    */   {
/* 20 */     this.key = e.getKeyCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\controllers\KeyBoardInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */