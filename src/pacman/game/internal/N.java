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
/*     */ class N
/*     */   implements Comparable<N>
/*     */ {
/*     */   public N parent;
/*     */   public double g;
/*     */   public double h;
/* 145 */   public boolean visited = false;
/*     */   public ArrayList<E> adj;
/*     */   public int index;
/* 148 */   public Constants.MOVE reached = null;
/*     */   
/*     */   public N(int index)
/*     */   {
/* 152 */     this.adj = new ArrayList();
/* 153 */     this.index = index;
/*     */   }
/*     */   
/*     */   public N(double g, double h)
/*     */   {
/* 158 */     this.g = g;
/* 159 */     this.h = h;
/*     */   }
/*     */   
/*     */   public boolean isEqual(N another)
/*     */   {
/* 164 */     return this.index == another.index;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 169 */     return Integer.toString(this.index);
/*     */   }
/*     */   
/*     */   public int compareTo(N another)
/*     */   {
/* 174 */     if (this.g + this.h < another.g + another.h)
/* 175 */       return -1;
/* 176 */     if (this.g + this.h > another.g + another.h) {
/* 177 */       return 1;
/*     */     }
/* 179 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\N.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */