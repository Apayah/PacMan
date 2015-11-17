/*     */ package pacman.game.internal;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Maze
/*     */ {
/*     */   public AStar astar;
/*     */   public int[] shortestPathDistances;
/*     */   public int[] pillIndices;
/*     */   public int[] powerPillIndices;
/*     */   public int[] junctionIndices;
/*     */   public int initialPacManNodeIndex;
/*     */   public int lairNodeIndex;
/*     */   public int initialGhostNodeIndex;
/*     */   public Node[] graph;
/*     */   public String name;
/*     */   
/*     */   public Maze(int index)
/*     */   {
/*  35 */     loadNodes(pacman.game.Constants.nodeNames[index]);
/*  36 */     loadDistances(pacman.game.Constants.distNames[index]);
/*     */     
/*     */ 
/*  39 */     this.astar = new AStar();
/*  40 */     this.astar.createGraph(this.graph);
/*     */   }
/*     */   
/*     */ 
/*     */   private void loadNodes(String fileName)
/*     */   {
/*     */     try
/*     */     {
/*  48 */       BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/mazes" + System.getProperty("file.separator") + fileName + ".txt")));
/*  49 */       String input = br.readLine();
/*     */       
/*     */ 
/*  52 */       String[] pr = input.split("\t");
/*     */       
/*  54 */       this.name = pr[0];
/*  55 */       this.initialPacManNodeIndex = Integer.parseInt(pr[1]);
/*  56 */       this.lairNodeIndex = Integer.parseInt(pr[2]);
/*  57 */       this.initialGhostNodeIndex = Integer.parseInt(pr[3]);
/*  58 */       this.graph = new Node[Integer.parseInt(pr[4])];
/*  59 */       this.pillIndices = new int[Integer.parseInt(pr[5])];
/*  60 */       this.powerPillIndices = new int[Integer.parseInt(pr[6])];
/*  61 */       this.junctionIndices = new int[Integer.parseInt(pr[7])];
/*     */       
/*  63 */       int nodeIndex = 0;
/*  64 */       int pillIndex = 0;
/*  65 */       int powerPillIndex = 0;
/*  66 */       int junctionIndex = 0;
/*     */       
/*  68 */       input = br.readLine();
/*     */       
/*  70 */       while (input != null)
/*     */       {
/*  72 */         String[] nd = input.split("\t");
/*     */         
/*  74 */         Node node = new Node(Integer.parseInt(nd[0]), Integer.parseInt(nd[1]), Integer.parseInt(nd[2]), Integer.parseInt(nd[7]), Integer.parseInt(nd[8]), 
/*  75 */           new int[] { Integer.parseInt(nd[3]), Integer.parseInt(nd[4]), Integer.parseInt(nd[5]), Integer.parseInt(nd[6]) });
/*     */         
/*  77 */         this.graph[(nodeIndex++)] = node;
/*     */         
/*  79 */         if (node.pillIndex >= 0) {
/*  80 */           this.pillIndices[(pillIndex++)] = node.nodeIndex;
/*  81 */         } else if (node.powerPillIndex >= 0) {
/*  82 */           this.powerPillIndices[(powerPillIndex++)] = node.nodeIndex;
/*     */         }
/*  84 */         if (node.numNeighbouringNodes > 2) {
/*  85 */           this.junctionIndices[(junctionIndex++)] = node.nodeIndex;
/*     */         }
/*  87 */         input = br.readLine();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/*  92 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void loadDistances(String fileName)
/*     */   {
/* 103 */     this.shortestPathDistances = new int[this.graph.length * (this.graph.length - 1) / 2 + this.graph.length];
/*     */     
/*     */     try
/*     */     {
/* 107 */       BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/distances" + System.getProperty("file.separator") + fileName)));
/* 108 */       String input = br.readLine();
/*     */       
/* 110 */       int index = 0;
/*     */       
/* 112 */       while (input != null)
/*     */       {
/* 114 */         this.shortestPathDistances[(index++)] = Integer.parseInt(input);
/* 115 */         input = br.readLine();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 120 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\internal\Maze.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */