/*    */ package pacman.game.internal;
/*    */ 
/*    */ import java.util.EnumMap;
/*    */ import pacman.game.Constants;
/*    */ 
/*    */ public final class Node
/*    */ {
/*    */   public final int x;
/*    */   public final int y;
/*    */   public final int nodeIndex;
/*    */   public final int pillIndex;
/*    */   public final int powerPillIndex;
/*    */   public final int numNeighbouringNodes;
/* 14 */   public final EnumMap<Constants.MOVE, Integer> neighbourhood = new EnumMap(Constants.MOVE.class);
/* 15 */   public EnumMap<Constants.MOVE, Constants.MOVE[]> allPossibleMoves = new EnumMap(Constants.MOVE.class);
/* 16 */   public EnumMap<Constants.MOVE, int[]> allNeighbouringNodes = new EnumMap(Constants.MOVE.class);
/* 17 */   public EnumMap<Constants.MOVE, EnumMap<Constants.MOVE, Integer>> allNeighbourhoods = new EnumMap(Constants.MOVE.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Node(int nodeIndex, int x, int y, int pillIndex, int powerPillIndex, int[] _neighbourhood)
/*    */   {
/* 24 */     this.nodeIndex = nodeIndex;
/* 25 */     this.x = x;
/* 26 */     this.y = y;
/* 27 */     this.pillIndex = pillIndex;
/* 28 */     this.powerPillIndex = powerPillIndex;
/*    */     
/* 30 */     Constants.MOVE[] moves = Constants.MOVE.values();
/*    */     
/* 32 */     for (int i = 0; i < _neighbourhood.length; i++) {
/* 33 */       if (_neighbourhood[i] != -1)
/* 34 */         this.neighbourhood.put(moves[i], Integer.valueOf(_neighbourhood[i]));
/*    */     }
/* 36 */     this.numNeighbouringNodes = this.neighbourhood.size();
/*    */     
/* 38 */     for (int i = 0; i < moves.length; i++) {
/* 39 */       if (this.neighbourhood.containsKey(moves[i]))
/*    */       {
/* 41 */         EnumMap<Constants.MOVE, Integer> tmp = new EnumMap(this.neighbourhood);
/* 42 */         tmp.remove(moves[i]);
/* 43 */         this.allNeighbourhoods.put(moves[i].opposite(), tmp);
/*    */       }
/*    */     }
/* 46 */     this.allNeighbourhoods.put(Constants.MOVE.NEUTRAL, this.neighbourhood);
/*    */     
/* 48 */     int[] neighbouringNodes = new int[this.numNeighbouringNodes];
/* 49 */     Constants.MOVE[] possibleMoves = new Constants.MOVE[this.numNeighbouringNodes];
/*    */     
/* 51 */     int index = 0;
/*    */     
/* 53 */     for (int i = 0; i < moves.length; i++) {
/* 54 */       if (this.neighbourhood.containsKey(moves[i]))
/*    */       {
/* 56 */         neighbouringNodes[index] = ((Integer)this.neighbourhood.get(moves[i])).intValue();
/* 57 */         possibleMoves[index] = moves[i];
/* 58 */         index++;
/*    */       }
/*    */     }
/* 61 */     for (int i = 0; i < moves.length; i++)
/*    */     {
/* 63 */       if (this.neighbourhood.containsKey(moves[i].opposite()))
/*    */       {
/* 65 */         int[] tmpNeighbouringNodes = new int[this.numNeighbouringNodes - 1];
/* 66 */         Constants.MOVE[] tmpPossibleMoves = new Constants.MOVE[this.numNeighbouringNodes - 1];
/*    */         
/* 68 */         index = 0;
/*    */         
/* 70 */         for (int j = 0; j < moves.length; j++)
/*    */         {
/* 72 */           if ((moves[j] != moves[i].opposite()) && (this.neighbourhood.containsKey(moves[j])))
/*    */           {
/* 74 */             tmpNeighbouringNodes[index] = ((Integer)this.neighbourhood.get(moves[j])).intValue();
/* 75 */             tmpPossibleMoves[index] = moves[j];
/* 76 */             index++;
/*    */           }
/*    */         }
/*    */         
/* 80 */         this.allNeighbouringNodes.put(moves[i], tmpNeighbouringNodes);
/* 81 */         this.allPossibleMoves.put(moves[i], tmpPossibleMoves);
/*    */       }
/*    */     }
/*    */     
/* 85 */     this.allNeighbouringNodes.put(Constants.MOVE.NEUTRAL, neighbouringNodes);
/* 86 */     this.allPossibleMoves.put(Constants.MOVE.NEUTRAL, possibleMoves);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */