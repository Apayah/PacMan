/*     */ package pacman;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Random;
/*     */ import pacman.controllers.Controller;
/*     */ import pacman.controllers.HumanController;
/*     */ import pacman.controllers.examples.GhostsController;
/*     */ import pacman.controllers.examples.PacManController;
/*     */ import pacman.game.Constants;
/*     */ import pacman.game.Game;
/*     */ import pacman.game.GameView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Executor
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  46 */     Executor exec = new Executor();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */     boolean visual = true;
/*     */     
/*  65 */     exec.runGameTimed(new PacManController(), new GhostsController(), visual);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runExperiment(Controller<Constants.MOVE> pacManController, Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghostController, int trials)
/*     */   {
/*  98 */     double avgScore = 0.0D;
/*     */     
/* 100 */     Random rnd = new Random(0L);
/*     */     
/*     */ 
/* 103 */     for (int i = 0; i < trials; i++)
/*     */     {
/* 105 */       Game game = new Game(rnd.nextLong());
/*     */       
/* 107 */       while (!game.gameOver())
/*     */       {
/* 109 */         game.advanceGame((Constants.MOVE)pacManController.getMove(game.copy(), System.currentTimeMillis() + 40L), 
/* 110 */           (EnumMap)ghostController.getMove(game.copy(), System.currentTimeMillis() + 40L));
/*     */       }
/*     */       
/* 113 */       avgScore += game.getScore();
/* 114 */       System.out.println(i + "\t" + game.getScore());
/*     */     }
/*     */     
/* 117 */     System.out.println(avgScore / trials);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runGame(Controller<Constants.MOVE> pacManController, Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghostController, boolean visual, int delay)
/*     */   {
/* 132 */     Game game = new Game(0L);
/*     */     
/* 134 */     GameView gv = null;
/*     */     
/* 136 */     if (visual) {
/* 137 */       gv = new GameView(game).showGame();
/*     */     }
/* 139 */     while (!game.gameOver())
/*     */     {
/* 141 */       game.advanceGame((Constants.MOVE)pacManController.getMove(game.copy(), -1L), (EnumMap)ghostController.getMove(game.copy(), -1L));
/*     */       try {
/* 143 */         Thread.sleep(delay);
/*     */       } catch (Exception localException) {}
/* 145 */       if (visual) {
/* 146 */         gv.repaint();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runGameTimed(Controller<Constants.MOVE> pacManController, Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghostController, boolean visual)
/*     */   {
/* 160 */     Game game = new Game(0L);
/*     */     
/* 162 */     GameView gv = null;
/*     */     
/* 164 */     if (visual) {
/* 165 */       gv = new GameView(game).showGame();
/*     */     }
/* 167 */     if ((pacManController instanceof HumanController)) {
/* 168 */       gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());
/*     */     }
/* 170 */     new Thread(pacManController).start();
/* 171 */     new Thread(ghostController).start();
/*     */     
/* 173 */     while (!game.gameOver())
/*     */     {
/* 175 */       pacManController.update(game.copy(), System.currentTimeMillis() + 40L);
/* 176 */       ghostController.update(game.copy(), System.currentTimeMillis() + 40L);
/*     */       
/*     */       try
/*     */       {
/* 180 */         Thread.sleep(40L);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 184 */         e.printStackTrace();
/*     */       }
/*     */       
/* 187 */       game.advanceGame((Constants.MOVE)pacManController.getMove(), (EnumMap)ghostController.getMove());
/*     */       
/* 189 */       if (visual) {
/* 190 */         gv.repaint();
/*     */       }
/*     */     }
/* 193 */     pacManController.terminate();
/* 194 */     ghostController.terminate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runGameTimedSpeedOptimised(Controller<Constants.MOVE> pacManController, Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghostController, boolean fixedTime, boolean visual)
/*     */   {
/* 208 */     Game game = new Game(0L);
/*     */     
/* 210 */     GameView gv = null;
/*     */     
/* 212 */     if (visual) {
/* 213 */       gv = new GameView(game).showGame();
/*     */     }
/* 215 */     if ((pacManController instanceof HumanController)) {
/* 216 */       gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());
/*     */     }
/* 218 */     new Thread(pacManController).start();
/* 219 */     new Thread(ghostController).start();
/*     */     
/* 221 */     while (!game.gameOver())
/*     */     {
/* 223 */       pacManController.update(game.copy(), System.currentTimeMillis() + 40L);
/* 224 */       ghostController.update(game.copy(), System.currentTimeMillis() + 40L);
/*     */       
/*     */       try
/*     */       {
/* 228 */         int waited = 40;
/*     */         
/* 230 */         for (int j = 0; j < 40; j++)
/*     */         {
/* 232 */           Thread.sleep(1L);
/*     */           
/* 234 */           if ((pacManController.hasComputed()) && (ghostController.hasComputed()))
/*     */           {
/* 236 */             waited = j;
/* 237 */             break;
/*     */           }
/*     */         }
/*     */         
/* 241 */         if (fixedTime) {
/* 242 */           Thread.sleep((40 - waited) * 1);
/*     */         }
/* 244 */         game.advanceGame((Constants.MOVE)pacManController.getMove(), (EnumMap)ghostController.getMove());
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 248 */         e.printStackTrace();
/*     */       }
/*     */       
/* 251 */       if (visual) {
/* 252 */         gv.repaint();
/*     */       }
/*     */     }
/* 255 */     pacManController.terminate();
/* 256 */     ghostController.terminate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runGameTimedRecorded(Controller<Constants.MOVE> pacManController, Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghostController, boolean visual, String fileName)
/*     */   {
/* 269 */     StringBuilder replay = new StringBuilder();
/*     */     
/* 271 */     Game game = new Game(0L);
/*     */     
/* 273 */     GameView gv = null;
/*     */     
/* 275 */     if (visual)
/*     */     {
/* 277 */       gv = new GameView(game).showGame();
/*     */       
/* 279 */       if ((pacManController instanceof HumanController)) {
/* 280 */         gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());
/*     */       }
/*     */     }
/* 283 */     new Thread(pacManController).start();
/* 284 */     new Thread(ghostController).start();
/*     */     
/* 286 */     while (!game.gameOver())
/*     */     {
/* 288 */       pacManController.update(game.copy(), System.currentTimeMillis() + 40L);
/* 289 */       ghostController.update(game.copy(), System.currentTimeMillis() + 40L);
/*     */       
/*     */       try
/*     */       {
/* 293 */         Thread.sleep(40L);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 297 */         e.printStackTrace();
/*     */       }
/*     */       
/* 300 */       game.advanceGame((Constants.MOVE)pacManController.getMove(), (EnumMap)ghostController.getMove());
/*     */       
/* 302 */       if (visual) {
/* 303 */         gv.repaint();
/*     */       }
/* 305 */       replay.append(game.getGameState() + "\n");
/*     */     }
/*     */     
/* 308 */     pacManController.terminate();
/* 309 */     ghostController.terminate();
/*     */     
/* 311 */     saveToFile(replay.toString(), fileName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void replayGame(String fileName, boolean visual)
/*     */   {
/* 322 */     ArrayList<String> timeSteps = loadReplay(fileName);
/*     */     
/* 324 */     Game game = new Game(0L);
/*     */     
/* 326 */     GameView gv = null;
/*     */     
/* 328 */     if (visual) {
/* 329 */       gv = new GameView(game).showGame();
/*     */     }
/* 331 */     for (int j = 0; j < timeSteps.size(); j++)
/*     */     {
/* 333 */       game.setGameState((String)timeSteps.get(j));
/*     */       
/*     */       try
/*     */       {
/* 337 */         Thread.sleep(40L);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 341 */         e.printStackTrace();
/*     */       }
/* 343 */       if (visual) {
/* 344 */         gv.repaint();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void saveToFile(String data, String name, boolean append)
/*     */   {
/*     */     try
/*     */     {
/* 353 */       FileOutputStream outS = new FileOutputStream(name, append);
/* 354 */       PrintWriter pw = new PrintWriter(outS);
/*     */       
/* 356 */       pw.println(data);
/* 357 */       pw.flush();
/* 358 */       outS.close();
/*     */ 
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 363 */       System.out.println("Could not save data!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static ArrayList<String> loadReplay(String fileName)
/*     */   {
/* 370 */     ArrayList<String> replay = new ArrayList();
/*     */     
/*     */     try
/*     */     {
/* 374 */       BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
/* 375 */       String input = br.readLine();
/*     */       
/* 377 */       while (input != null)
/*     */       {
/* 379 */         if (!input.equals("")) {
/* 380 */           replay.add(input);
/*     */         }
/* 382 */         input = br.readLine();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 387 */       ioe.printStackTrace();
/*     */     }
/*     */     
/* 390 */     return replay;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\Executor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */