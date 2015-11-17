/*     */ package pacman.game.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.PriorityQueue;
/*     */ import pacman.game.Constants;
/*     */ import pacman.game.Game;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AStar
/*     */ {
/*     */   private N[] graph;
/*     */   
/*     */   public void createGraph(Node[] nodes)
/*     */   {
/*  23 */     this.graph = new N[nodes.length];
/*     */     
/*     */ 
/*  26 */     for (int i = 0; i < nodes.length; i++) {
/*  27 */       this.graph[i] = new N(nodes[i].nodeIndex);
/*     */     }
/*     */     
/*  30 */     for (int i = 0; i < nodes.length; i++)
/*     */     {
/*  32 */       EnumMap<Constants.MOVE, Integer> neighbours = nodes[i].neighbourhood;
/*  33 */       Constants.MOVE[] moves = Constants.MOVE.values();
/*     */       
/*  35 */       for (int j = 0; j < moves.length; j++) {
/*  36 */         if (neighbours.containsKey(moves[j]))
/*  37 */           this.graph[i].adj.add(new E(this.graph[((Integer)neighbours.get(moves[j])).intValue()], moves[j], 1.0D));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized int[] computePathsAStar(int s, int t, Constants.MOVE lastMoveMade, Game game) {
/*  43 */     N start = this.graph[s];
/*  44 */     N target = this.graph[t];
/*     */     
/*  46 */     PriorityQueue<N> open = new PriorityQueue();
/*  47 */     ArrayList<N> closed = new ArrayList();
/*     */     
/*  49 */     start.g = 0.0D;
/*  50 */     start.h = game.getShortestPathDistance(start.index, target.index);
/*     */     
/*  52 */     start.reached = lastMoveMade;
/*     */     
/*  54 */     open.add(start);
/*     */     Iterator localIterator;
/*  56 */     for (; !open.isEmpty(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */         localIterator.hasNext())
/*     */     {
/*  58 */       N currentNode = (N)open.poll();
/*  59 */       closed.add(currentNode);
/*     */       
/*  61 */       if (currentNode.isEqual(target)) {
/*     */         break;
/*     */       }
/*  64 */       localIterator = currentNode.adj.iterator();
                E next = (E)localIterator.next();
/*     */       
/*  66 */       if (next.move != currentNode.reached.opposite())
/*     */       {
/*  68 */         double currentDistance = next.cost;
/*     */         
/*  70 */         if ((!open.contains(next.node)) && (!closed.contains(next.node)))
/*     */         {
/*  72 */           next.node.g = (currentDistance + currentNode.g);
/*  73 */           next.node.h = game.getShortestPathDistance(next.node.index, target.index);
/*  74 */           next.node.parent = currentNode;
/*     */           
/*  76 */           next.node.reached = next.move;
/*     */           
/*  78 */           open.add(next.node);
/*     */         }
/*  80 */         else if (currentDistance + currentNode.g < next.node.g)
/*     */         {
/*  82 */           next.node.g = (currentDistance + currentNode.g);
/*  83 */           next.node.parent = currentNode;
/*     */           
/*  85 */           next.node.reached = next.move;
/*     */           
/*  87 */           if (open.contains(next.node)) {
/*  88 */             open.remove(next.node);
/*     */           }
/*  90 */           if (closed.contains(next.node)) {
/*  91 */             closed.remove(next.node);
/*     */           }
/*  93 */           open.add(next.node);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  99 */     return extractPath(target);
/*     */   }
/*     */   
/*     */   public synchronized int[] computePathsAStar(int s, int t, Game game)
/*     */   {
/* 104 */     return computePathsAStar(s, t, Constants.MOVE.NEUTRAL, game);
/*     */   }
/*     */   
/*     */   private synchronized int[] extractPath(N target)
/*     */   {
/* 109 */     ArrayList<Integer> route = new ArrayList();
/* 110 */     N current = target;
/* 111 */     route.add(Integer.valueOf(current.index));
/*     */     
/* 113 */     while (current.parent != null)
/*     */     {
/* 115 */       route.add(Integer.valueOf(current.parent.index));
/* 116 */       current = current.parent;
/*     */     }
/*     */     
/* 119 */     Collections.reverse(route);
/*     */     
/* 121 */     int[] routeArray = new int[route.size()];
/*     */     
/* 123 */     for (int i = 0; i < routeArray.length; i++) {
/* 124 */       routeArray[i] = ((Integer)route.get(i)).intValue();
/*     */     }
/* 126 */     return routeArray;
/*     */   }
/*     */   
/*     */   public void resetGraph() {
/*     */     N[] arrayOfN;
/* 131 */     int j = (arrayOfN = this.graph).length; for (int i = 0; i < j; i++) { N node = arrayOfN[i];
/*     */       
/* 133 */       node.g = 0.0D;
/* 134 */       node.h = 0.0D;
/* 135 */       node.parent = null;
/* 136 */       node.reached = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\AStar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */