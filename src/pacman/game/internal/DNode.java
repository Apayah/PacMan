/*     */ package pacman.game.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import pacman.game.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DNode
/*     */ {
/*     */   public int nodeID;
/*     */   public ArrayList<JunctionData> closestJunctions;
/*     */   public boolean isJunction;
/*     */   
/*     */   public DNode(int nodeID, boolean isJunction)
/*     */   {
/* 334 */     this.nodeID = nodeID;
/* 335 */     this.isJunction = isJunction;
/*     */     
/* 337 */     this.closestJunctions = new ArrayList();
/*     */     
/* 339 */     if (isJunction) {
/* 340 */       this.closestJunctions.add(new JunctionData(nodeID, Constants.MOVE.NEUTRAL, nodeID, new int[0], Constants.MOVE.NEUTRAL));
/*     */     }
/*     */   }
/*     */   
/*     */   public int[] getPathToJunction(Constants.MOVE lastMoveMade) {
/* 345 */     if (this.isJunction) {
/* 346 */       return new int[0];
/*     */     }
/* 348 */     for (int i = 0; i < this.closestJunctions.size(); i++) {
/* 349 */       if (!((JunctionData)this.closestJunctions.get(i)).firstMove.equals(lastMoveMade.opposite()))
/* 350 */         return ((JunctionData)this.closestJunctions.get(i)).path;
/*     */     }
/* 352 */     return null;
/*     */   }
/*     */   
/*     */   public JunctionData getNearestJunction(Constants.MOVE lastMoveMade)
/*     */   {
/* 357 */     if (this.isJunction) {
/* 358 */       return (JunctionData)this.closestJunctions.get(0);
/*     */     }
/* 360 */     int minDist = Integer.MAX_VALUE;
/* 361 */     int bestIndex = -1;
/*     */     
/* 363 */     for (int i = 0; i < this.closestJunctions.size(); i++) {
/* 364 */       if (!((JunctionData)this.closestJunctions.get(i)).firstMove.equals(lastMoveMade.opposite()))
/*     */       {
/* 366 */         int newDist = ((JunctionData)this.closestJunctions.get(i)).path.length;
/*     */         
/* 368 */         if (newDist < minDist)
/*     */         {
/* 370 */           minDist = newDist;
/* 371 */           bestIndex = i;
/*     */         }
/*     */       }
/*     */     }
/* 375 */     if (bestIndex != -1) {
/* 376 */       return (JunctionData)this.closestJunctions.get(bestIndex);
/*     */     }
/* 378 */     return null;
/*     */   }
/*     */   
/*     */   public void addPath(int junctionID, Constants.MOVE firstMove, int nodeStartedFrom, int[] path, Constants.MOVE lastMove)
/*     */   {
/* 383 */     this.closestJunctions.add(new JunctionData(junctionID, firstMove, nodeStartedFrom, path, lastMove));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 388 */     return this.nodeID + "\t" + this.isJunction;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\DNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */