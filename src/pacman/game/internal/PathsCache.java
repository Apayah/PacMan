/*     */ package pacman.game.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import pacman.game.Constants;
/*     */ import pacman.game.Game;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathsCache
/*     */ {
/*     */   public HashMap<Integer, Integer> junctionIndexConverter;
/*     */   public DNode[] nodes;
/*     */   public Junction[] junctions;
/*     */   public Game game;
/*     */   
/*     */   public PathsCache(int mazeIndex)
/*     */   {
/*  34 */     this.junctionIndexConverter = new HashMap();
/*     */     
/*  36 */     this.game = new Game(0L, mazeIndex);
/*  37 */     Maze m = this.game.getCurrentMaze();
/*     */     
/*  39 */     int[] jctIndices = m.junctionIndices;
/*     */     
/*  41 */     for (int i = 0; i < jctIndices.length; i++) {
/*  42 */       this.junctionIndexConverter.put(Integer.valueOf(jctIndices[i]), Integer.valueOf(i));
/*     */     }
/*  44 */     this.nodes = assignJunctionsToNodes(this.game);
/*  45 */     this.junctions = junctionDistances(this.game);
/*     */     
/*  47 */     for (int i = 0; i < this.junctions.length; i++) {
/*  48 */       this.junctions[i].computeShortestPaths();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] getPathFromA2B(int a, int b)
/*     */   {
/*  55 */     if (a == b) {
/*  56 */       return new int[0];
/*     */     }
/*     */     
/*  59 */     ArrayList<JunctionData> closestFromJunctions = this.nodes[a].closestJunctions;
/*     */     
/*     */ 
/*  62 */     for (int w = 0; w < closestFromJunctions.size(); w++) {
/*  63 */       for (int i = 0; i < ((JunctionData)closestFromJunctions.get(w)).path.length; i++) {
/*  64 */         if (((JunctionData)closestFromJunctions.get(w)).path[i] == b)
/*  65 */           return Arrays.copyOf(((JunctionData)closestFromJunctions.get(w)).path, i + 1);
/*     */       }
/*     */     }
/*  68 */     ArrayList<JunctionData> closestToJunctions = this.nodes[b].closestJunctions;
/*     */     
/*  70 */     int minFrom = -1;
/*  71 */     int minTo = -1;
/*  72 */     int minDistance = Integer.MAX_VALUE;
/*  73 */     int[] shortestPath = (int[])null;
/*     */     
/*  75 */     for (int i = 0; i < closestFromJunctions.size(); i++)
/*     */     {
/*  77 */       for (int j = 0; j < closestToJunctions.size(); j++)
/*     */       {
/*     */ 
/*  80 */         int distance = ((JunctionData)closestFromJunctions.get(i)).path.length;
/*     */         
/*  82 */         int[] tmpPath = 
/*  83 */           (int[])this.junctions[((Integer)this.junctionIndexConverter.get(Integer.valueOf(((JunctionData)closestFromJunctions.get(i)).nodeID))).intValue()].paths[((Integer)this.junctionIndexConverter.get(Integer.valueOf(((JunctionData)closestToJunctions.get(j)).nodeID))).intValue()].get(Constants.MOVE.NEUTRAL);
/*  84 */         distance += tmpPath.length;
/*     */         
/*  86 */         distance += ((JunctionData)closestToJunctions.get(j)).path.length;
/*     */         
/*  88 */         if (distance < minDistance)
/*     */         {
/*  90 */           minDistance = distance;
/*  91 */           minFrom = i;
/*  92 */           minTo = j;
/*  93 */           shortestPath = tmpPath;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  98 */     return concat(new int[][] { ((JunctionData)closestFromJunctions.get(minFrom)).path, shortestPath, ((JunctionData)closestToJunctions.get(minTo)).reversePath });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPathDistanceFromA2B(int a, int b, Constants.MOVE lastMoveMade)
/*     */   {
/* 106 */     return getPathFromA2B(a, b, lastMoveMade).length;
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] getPathFromA2B(int a, int b, Constants.MOVE lastMoveMade)
/*     */   {
/* 112 */     if (a == b) {
/* 113 */       return new int[0];
/*     */     }
/*     */     
/* 116 */     JunctionData fromJunction = this.nodes[a].getNearestJunction(lastMoveMade);
/*     */     
/*     */ 
/* 119 */     for (int i = 0; i < fromJunction.path.length; i++) {
/* 120 */       if (fromJunction.path[i] == b) {
/* 121 */         return Arrays.copyOf(fromJunction.path, i + 1);
/*     */       }
/*     */     }
/* 124 */     int junctionFrom = fromJunction.nodeID;
/* 125 */     int junctionFromId = ((Integer)this.junctionIndexConverter.get(Integer.valueOf(junctionFrom))).intValue();
/* 126 */     Constants.MOVE moveEnteredJunction = fromJunction.lastMove.equals(Constants.MOVE.NEUTRAL) ? lastMoveMade : fromJunction.lastMove;
/*     */     
/*     */ 
/* 129 */     ArrayList<JunctionData> junctionsTo = this.nodes[b].closestJunctions;
/*     */     
/* 131 */     int minDist = Integer.MAX_VALUE;
/* 132 */     int[] shortestPath = (int[])null;
/* 133 */     int closestJunction = -1;
/*     */     
/* 135 */     boolean onTheWay = false;
/*     */     
/* 137 */     for (int q = 0; q < junctionsTo.size(); q++)
/*     */     {
/* 139 */       int junctionToId = ((Integer)this.junctionIndexConverter.get(Integer.valueOf(((JunctionData)junctionsTo.get(q)).nodeID))).intValue();
/*     */       
/* 141 */       if (junctionFromId == junctionToId)
/*     */       {
/* 143 */         if (!this.game.getMoveToMakeToReachDirectNeighbour(junctionFrom, ((JunctionData)junctionsTo.get(q)).reversePath[0]).equals(moveEnteredJunction.opposite()))
/*     */         {
/* 145 */           int[] reversepath = ((JunctionData)junctionsTo.get(q)).reversePath;
/* 146 */           int cutoff = -1;
/*     */           
/* 148 */           for (int w = 0; w < reversepath.length; w++) {
/* 149 */             if (reversepath[w] == b)
/* 150 */               cutoff = w;
/*     */           }
/* 152 */           shortestPath = Arrays.copyOf(reversepath, cutoff + 1);
/* 153 */           minDist = shortestPath.length;
/* 154 */           closestJunction = q;
/* 155 */           onTheWay = true;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 160 */         EnumMap<Constants.MOVE, int[]> paths = this.junctions[junctionFromId].paths[junctionToId];
/* 161 */         Set<Constants.MOVE> set = paths.keySet();
/*     */         
/* 163 */         for (Constants.MOVE move : set)
/*     */         {
/* 165 */           if ((!move.opposite().equals(moveEnteredJunction)) && (!move.equals(Constants.MOVE.NEUTRAL)))
/*     */           {
/* 167 */             int[] path = (int[])paths.get(move);
/*     */             
/* 169 */             if (path.length + ((JunctionData)junctionsTo.get(q)).path.length < minDist)
/*     */             {
/* 171 */               minDist = path.length + ((JunctionData)junctionsTo.get(q)).path.length;
/* 172 */               shortestPath = path;
/* 173 */               closestJunction = q;
/* 174 */               onTheWay = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 181 */     if (!onTheWay) {
/* 182 */       return concat(new int[][] { fromJunction.path, shortestPath, ((JunctionData)junctionsTo.get(closestJunction)).reversePath });
/*     */     }
/* 184 */     return concat(new int[][] { fromJunction.path, shortestPath });
/*     */   }
/*     */   
/*     */ 
/*     */   private Junction[] junctionDistances(Game game)
/*     */   {
/* 190 */     Maze m = game.getCurrentMaze();
/* 191 */     int[] indices = m.junctionIndices;
/*     */     
/* 193 */     Junction[] junctions = new Junction[indices.length];
/*     */     
/* 195 */     for (int q = 0; q < indices.length; q++)
/*     */     {
/* 197 */       Constants.MOVE[] possibleMoves = (Constants.MOVE[])m.graph[indices[q]].allPossibleMoves.get(Constants.MOVE.NEUTRAL);
/*     */       
/* 199 */       junctions[q] = new Junction(q, indices[q], indices.length);
/*     */       
/* 201 */       for (int z = 0; z < indices.length; z++)
/*     */       {
/* 203 */         for (int i = 0; i < possibleMoves.length; i++)
/*     */         {
/* 205 */           int neighbour = game.getNeighbour(indices[q], possibleMoves[i]);
/* 206 */           int[] p = m.astar.computePathsAStar(neighbour, indices[z], possibleMoves[i], game);
/* 207 */           m.astar.resetGraph();
/*     */           
/* 209 */           junctions[q].addPath(z, possibleMoves[i], p);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 214 */     return junctions;
/*     */   }
/*     */   
/*     */   private DNode[] assignJunctionsToNodes(Game game)
/*     */   {
/* 219 */     Maze m = game.getCurrentMaze();
/* 220 */     int numNodes = m.graph.length;
/*     */     
/* 222 */     DNode[] allNodes = new DNode[numNodes];
/*     */     
/* 224 */     for (int i = 0; i < numNodes; i++)
/*     */     {
/* 226 */       boolean isJunction = game.isJunction(i);
/* 227 */       allNodes[i] = new DNode(i, isJunction);
/*     */       
/* 229 */       if (!isJunction)
/*     */       {
/* 231 */         Constants.MOVE[] possibleMoves = (Constants.MOVE[])m.graph[i].allPossibleMoves.get(Constants.MOVE.NEUTRAL);
/*     */         
/* 233 */         for (int j = 0; j < possibleMoves.length; j++)
/*     */         {
/* 235 */           ArrayList<Integer> path = new ArrayList();
/*     */           
/* 237 */           Constants.MOVE lastMove = possibleMoves[j];
/* 238 */           int currentNode = game.getNeighbour(i, lastMove);
/* 239 */           path.add(Integer.valueOf(currentNode));
/*     */           
/* 241 */           while (!game.isJunction(currentNode))
/*     */           {
/* 243 */             Constants.MOVE[] newPossibleMoves = game.getPossibleMoves(currentNode);
/*     */             
/* 245 */             for (int q = 0; q < newPossibleMoves.length; q++) {
/* 246 */               if (newPossibleMoves[q].opposite() != lastMove)
/*     */               {
/* 248 */                 lastMove = newPossibleMoves[q];
/* 249 */                 break;
/*     */               }
/*     */             }
/* 252 */             currentNode = game.getNeighbour(currentNode, lastMove);
/* 253 */             path.add(Integer.valueOf(currentNode));
/*     */           }
/*     */           
/* 256 */           int[] array = new int[path.size()];
/*     */           
/* 258 */           for (int w = 0; w < path.size(); w++) {
/* 259 */             array[w] = ((Integer)path.get(w)).intValue();
/*     */           }
/* 261 */           allNodes[i].addPath(array[(array.length - 1)], possibleMoves[j], i, array, lastMove);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 266 */     return allNodes;
/*     */   }
/*     */   
/*     */   private int[] concat(int[]... arrays)
/*     */   {
/* 271 */     int totalLength = 0;
/*     */     
/* 273 */     for (int i = 0; i < arrays.length; i++) {
/* 274 */       totalLength += arrays[i].length;
/*     */     }
/* 276 */     int[] fullArray = new int[totalLength];
/*     */     
/* 278 */     int index = 0;
/*     */     
/* 280 */     for (int i = 0; i < arrays.length; i++) {
/* 281 */       for (int j = 0; j < arrays[i].length; j++)
/* 282 */         fullArray[(index++)] = arrays[i][j];
/*     */     }
/* 284 */     return fullArray;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\PathsCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */