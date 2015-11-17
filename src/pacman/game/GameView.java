/*     */ package pacman.game;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Vector;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GameView
/*     */   extends JComponent
/*     */ {
/*     */   private final Game game;
/*     */   private Images images;
/*     */   private Constants.MOVE lastPacManMove;
/*     */   private int time;
/*     */   private GameFrame frame;
/*     */   private Graphics bufferGraphics;
/*     */   private BufferedImage offscreen;
/*  38 */   private static boolean isVisible = true;
/*  39 */   private static boolean saveImage = false;
/*  40 */   private static String imageFileName = "";
/*  41 */   public static Vector<DebugPointer> debugPointers = new Vector();
/*  42 */   public static Vector<DebugLine> debugLines = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameView(Game game)
/*     */   {
/*  51 */     this.game = game;
/*     */     
/*  53 */     this.images = new Images();
/*  54 */     this.lastPacManMove = game.getPacmanLastMoveMade();
/*  55 */     this.time = game.getTotalTime();
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
/*     */   public static synchronized void addPoints(Game game, Color color, int... nodeIndices)
/*     */   {
/*  71 */     if (isVisible) {
/*  72 */       for (int i = 0; i < nodeIndices.length; i++) {
/*  73 */         debugPointers.add(new DebugPointer(game.getNodeXCood(nodeIndices[i]), game.getNodeYCood(nodeIndices[i]), color));
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
/*     */   public static synchronized void addLines(Game game, Color color, int[] fromNnodeIndices, int[] toNodeIndices)
/*     */   {
/*  86 */     if (isVisible) {
/*  87 */       for (int i = 0; i < fromNnodeIndices.length; i++) {
/*  88 */         debugLines.add(new DebugLine(game.getNodeXCood(fromNnodeIndices[i]), game.getNodeYCood(fromNnodeIndices[i]), game.getNodeXCood(toNodeIndices[i]), game.getNodeYCood(toNodeIndices[i]), color));
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
/*     */   public static synchronized void addLines(Game game, Color color, int fromNnodeIndex, int toNodeIndex)
/*     */   {
/* 101 */     if (isVisible) {
/* 102 */       debugLines.add(new DebugLine(game.getNodeXCood(fromNnodeIndex), game.getNodeYCood(fromNnodeIndex), game.getNodeXCood(toNodeIndex), game.getNodeYCood(toNodeIndex), color));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawDebugInfo()
/*     */   {
/* 110 */     for (int i = 0; i < debugPointers.size(); i++)
/*     */     {
/* 112 */       DebugPointer dp = (DebugPointer)debugPointers.get(i);
/* 113 */       this.bufferGraphics.setColor(dp.color);
/* 114 */       this.bufferGraphics.fillRect(dp.x * 2 + 1, dp.y * 2 + 5, 10, 10);
/*     */     }
/*     */     
/* 117 */     for (int i = 0; i < debugLines.size(); i++)
/*     */     {
/* 119 */       DebugLine dl = (DebugLine)debugLines.get(i);
/* 120 */       this.bufferGraphics.setColor(dl.color);
/* 121 */       this.bufferGraphics.drawLine(dl.x1 * 2 + 5, dl.y1 * 2 + 10, dl.x2 * 2 + 5, dl.y2 * 2 + 10);
/*     */     }
/*     */     
/* 124 */     debugPointers.clear();
/* 125 */     debugLines.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized void saveImage(String fileName)
/*     */   {
/* 135 */     saveImage = true;
/* 136 */     imageFileName = fileName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void saveImage()
/*     */   {
/*     */     try
/*     */     {
/* 146 */       ImageIO.write(this.offscreen, "png", new File("myData/" + imageFileName + ".png"));
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 150 */       e.printStackTrace();
/*     */     }
/*     */     
/* 153 */     saveImage = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintComponent(Graphics g)
/*     */   {
/* 165 */     this.time = this.game.getTotalTime();
/*     */     
/* 167 */     if (this.offscreen == null)
/*     */     {
/* 169 */       this.offscreen = ((BufferedImage)createImage(getPreferredSize().width, getPreferredSize().height));
/* 170 */       this.bufferGraphics = this.offscreen.getGraphics();
/*     */     }
/*     */     
/* 173 */     drawMaze();
/* 174 */     drawDebugInfo();
/* 175 */     drawPills();
/* 176 */     drawPowerPills();
/* 177 */     drawPacMan();
/* 178 */     drawGhosts();
/* 179 */     drawLives();
/* 180 */     drawGameInfo();
/*     */     
/* 182 */     if (this.game.gameOver()) {
/* 183 */       drawGameOver();
/*     */     }
/* 185 */     g.drawImage(this.offscreen, 0, 0, this);
/*     */     
/* 187 */     if (saveImage) {
/* 188 */       saveImage();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawMaze()
/*     */   {
/* 196 */     this.bufferGraphics.setColor(Color.BLACK);
/* 197 */     this.bufferGraphics.fillRect(0, 0, 228, 280);
/*     */     
/* 199 */     this.bufferGraphics.drawImage(this.images.getMaze(this.game.getMazeIndex()), 2, 6, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawPills()
/*     */   {
/* 207 */     int[] pillIndices = this.game.getPillIndices();
/*     */     
/* 209 */     this.bufferGraphics.setColor(Color.white);
/*     */     
/* 211 */     for (int i = 0; i < pillIndices.length; i++) {
/* 212 */       if (this.game.isPillStillAvailable(i)) {
/* 213 */         this.bufferGraphics.fillOval(this.game.getNodeXCood(pillIndices[i]) * 2 + 4, this.game.getNodeYCood(pillIndices[i]) * 2 + 8, 3, 3);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void drawPowerPills()
/*     */   {
/* 221 */     int[] powerPillIndices = this.game.getPowerPillIndices();
/*     */     
/* 223 */     this.bufferGraphics.setColor(Color.white);
/*     */     
/* 225 */     for (int i = 0; i < powerPillIndices.length; i++) {
/* 226 */       if (this.game.isPowerPillStillAvailable(i)) {
/* 227 */         this.bufferGraphics.fillOval(this.game.getNodeXCood(powerPillIndices[i]) * 2 + 1, this.game.getNodeYCood(powerPillIndices[i]) * 2 + 5, 8, 8);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void drawPacMan()
/*     */   {
/* 235 */     int pacLoc = this.game.getPacmanCurrentNodeIndex();
/*     */     
/* 237 */     Constants.MOVE tmpLastPacManMove = this.game.getPacmanLastMoveMade();
/*     */     
/* 239 */     if (tmpLastPacManMove != Constants.MOVE.NEUTRAL) {
/* 240 */       this.lastPacManMove = tmpLastPacManMove;
/*     */     }
/* 242 */     this.bufferGraphics.drawImage(this.images.getPacMan(this.lastPacManMove, this.time), this.game.getNodeXCood(pacLoc) * 2 - 1, this.game.getNodeYCood(pacLoc) * 2 + 3, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private void drawGhosts()
/*     */   {
/*     */     Constants.GHOST[] arrayOfGHOST;
/*     */     
/* 250 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghostType = arrayOfGHOST[i];
/*     */       
/* 252 */       int currentNodeIndex = this.game.getGhostCurrentNodeIndex(ghostType);
/* 253 */       int nodeXCood = this.game.getNodeXCood(currentNodeIndex);
/* 254 */       int nodeYCood = this.game.getNodeYCood(currentNodeIndex);
/*     */       
/* 256 */       if (this.game.getGhostEdibleTime(ghostType) > 0)
/*     */       {
/*     */ 
/* 259 */         if ((this.game.getGhostEdibleTime(ghostType) < 30) && (this.time % 6 / 3 == 0)) {
/* 260 */           this.bufferGraphics.drawImage(this.images.getEdibleGhost(true, this.time), nodeXCood * 2 - 1, nodeYCood * 2 + 3, null);
/*     */         } else {
/* 262 */           this.bufferGraphics.drawImage(this.images.getEdibleGhost(false, this.time), nodeXCood * 2 - 1, nodeYCood * 2 + 3, null);
/*     */         }
/*     */       }
/*     */       else {
/* 266 */         int index = ghostType.ordinal();
/*     */         
/* 268 */         if (this.game.getGhostLairTime(ghostType) > 0) {
/* 269 */           this.bufferGraphics.drawImage(this.images.getGhost(ghostType, this.game.getGhostLastMoveMade(ghostType), this.time), nodeXCood * 2 - 1 + index * 5, nodeYCood * 2 + 3, null);
/*     */         } else {
/* 271 */           this.bufferGraphics.drawImage(this.images.getGhost(ghostType, this.game.getGhostLastMoveMade(ghostType), this.time), nodeXCood * 2 - 1, nodeYCood * 2 + 3, null);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawLives()
/*     */   {
/* 281 */     for (int i = 0; i < this.game.getPacmanNumberOfLivesRemaining() - 1; i++) {
/* 282 */       this.bufferGraphics.drawImage(this.images.getPacManForExtraLives(), 210 - 30 * i / 2, 260, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawGameInfo()
/*     */   {
/* 290 */     this.bufferGraphics.setColor(Color.WHITE);
/* 291 */     this.bufferGraphics.drawString("S: ", 4, 271);
/* 292 */     this.bufferGraphics.drawString(Integer.toString(this.game.getScore()), 16, 271);
/* 293 */     this.bufferGraphics.drawString("L: ", 78, 271);
/* 294 */     this.bufferGraphics.drawString(Integer.toString(this.game.getCurrentLevel() + 1), 90, 271);
/* 295 */     this.bufferGraphics.drawString("T: ", 116, 271);
/* 296 */     this.bufferGraphics.drawString(Integer.toString(this.game.getCurrentLevelTime()), 129, 271);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawGameOver()
/*     */   {
/* 304 */     this.bufferGraphics.setColor(Color.WHITE);
/* 305 */     this.bufferGraphics.drawString("Game Over", 80, 150);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 313 */     return new Dimension(228, 280);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameView showGame()
/*     */   {
/* 323 */     this.frame = new GameFrame(this);
/*     */     try
/*     */     {
/* 326 */       Thread.sleep(2000L);
/*     */     } catch (Exception localException) {}
/* 328 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameFrame getFrame()
/*     */   {
/* 338 */     return this.frame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class GameFrame
/*     */     extends JFrame
/*     */   {
/*     */     public GameFrame(JComponent comp)
/*     */     {
/* 353 */       getContentPane().add("Center", comp);
/* 354 */       pack();
/* 355 */       Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
/* 356 */       setLocation((int)(screen.getWidth() * 3.0D / 8.0D), (int)(screen.getHeight() * 3.0D / 8.0D));
/* 357 */       setVisible(true);
/* 358 */       setResizable(false);
/* 359 */       setDefaultCloseOperation(3);
/* 360 */       repaint();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DebugPointer
/*     */   {
/*     */     public int x;
/*     */     public int y;
/*     */     public Color color;
/*     */     
/*     */     public DebugPointer(int x, int y, Color color) {
/* 371 */       this.x = x;
/* 372 */       this.y = y;
/* 373 */       this.color = color;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DebugLine {
/*     */     public int x1;
/*     */     public int y1;
/*     */     public int x2;
/*     */     public int y2;
/*     */     public Color color;
/*     */     
/* 384 */     public DebugLine(int x1, int y1, int x2, int y2, Color color) { this.x1 = x1;
/* 385 */       this.y1 = y1;
/* 386 */       this.x2 = x2;
/* 387 */       this.y2 = y2;
/* 388 */       this.color = color;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Images {
/*     */     private EnumMap<Constants.MOVE, BufferedImage[]> pacman;
/*     */     private EnumMap<Constants.GHOST, EnumMap<Constants.MOVE, BufferedImage[]>> ghosts;
/*     */     private BufferedImage[] edibleGhosts;
/*     */     private BufferedImage[] edibleBlinkingGhosts;
/*     */     private BufferedImage[] mazes;
/*     */     
/*     */     public Images() {
/* 400 */       this.pacman = new EnumMap(Constants.MOVE.class);
/*     */       
/* 402 */       this.pacman.put(Constants.MOVE.UP, new BufferedImage[] { _loadImage("mspacman-up-normal.png"), 
/* 403 */         _loadImage("mspacman-up-open.png"), 
/* 404 */         _loadImage("mspacman-up-closed.png") });
/*     */       
/* 406 */       this.pacman.put(Constants.MOVE.RIGHT, new BufferedImage[] { _loadImage("mspacman-right-normal.png"), 
/* 407 */         _loadImage("mspacman-right-open.png"), 
/* 408 */         _loadImage("mspacman-right-closed.png") });
/*     */       
/* 410 */       this.pacman.put(Constants.MOVE.DOWN, new BufferedImage[] { _loadImage("mspacman-down-normal.png"), 
/* 411 */         _loadImage("mspacman-down-open.png"), 
/* 412 */         _loadImage("mspacman-down-closed.png") });
/*     */       
/* 414 */       this.pacman.put(Constants.MOVE.LEFT, new BufferedImage[] { _loadImage("mspacman-left-normal.png"), 
/* 415 */         _loadImage("mspacman-left-open.png"), 
/* 416 */         _loadImage("mspacman-left-closed.png") });
/*     */       
/* 418 */       this.ghosts = new EnumMap(Constants.GHOST.class);
/*     */       
/* 420 */       this.ghosts.put(Constants.GHOST.BLINKY, new EnumMap(Constants.MOVE.class));
/* 421 */       ((EnumMap)this.ghosts.get(Constants.GHOST.BLINKY)).put(Constants.MOVE.UP, new BufferedImage[] { _loadImage("blinky-up-1.png"), _loadImage("blinky-up-2.png") });
/* 422 */       ((EnumMap)this.ghosts.get(Constants.GHOST.BLINKY)).put(Constants.MOVE.RIGHT, new BufferedImage[] { _loadImage("blinky-right-1.png"), _loadImage("blinky-right-2.png") });
/* 423 */       ((EnumMap)this.ghosts.get(Constants.GHOST.BLINKY)).put(Constants.MOVE.DOWN, new BufferedImage[] { _loadImage("blinky-down-1.png"), _loadImage("blinky-down-2.png") });
/* 424 */       ((EnumMap)this.ghosts.get(Constants.GHOST.BLINKY)).put(Constants.MOVE.LEFT, new BufferedImage[] { _loadImage("blinky-left-1.png"), _loadImage("blinky-left-2.png") });
/*     */       
/* 426 */       this.ghosts.put(Constants.GHOST.PINKY, new EnumMap(Constants.MOVE.class));
/* 427 */       ((EnumMap)this.ghosts.get(Constants.GHOST.PINKY)).put(Constants.MOVE.UP, new BufferedImage[] { _loadImage("pinky-up-1.png"), _loadImage("pinky-up-2.png") });
/* 428 */       ((EnumMap)this.ghosts.get(Constants.GHOST.PINKY)).put(Constants.MOVE.RIGHT, new BufferedImage[] { _loadImage("pinky-right-1.png"), _loadImage("pinky-right-2.png") });
/* 429 */       ((EnumMap)this.ghosts.get(Constants.GHOST.PINKY)).put(Constants.MOVE.DOWN, new BufferedImage[] { _loadImage("pinky-down-1.png"), _loadImage("pinky-down-2.png") });
/* 430 */       ((EnumMap)this.ghosts.get(Constants.GHOST.PINKY)).put(Constants.MOVE.LEFT, new BufferedImage[] { _loadImage("pinky-left-1.png"), _loadImage("pinky-left-2.png") });
/*     */       
/* 432 */       this.ghosts.put(Constants.GHOST.INKY, new EnumMap(Constants.MOVE.class));
/* 433 */       ((EnumMap)this.ghosts.get(Constants.GHOST.INKY)).put(Constants.MOVE.UP, new BufferedImage[] { _loadImage("inky-up-1.png"), _loadImage("inky-up-2.png") });
/* 434 */       ((EnumMap)this.ghosts.get(Constants.GHOST.INKY)).put(Constants.MOVE.RIGHT, new BufferedImage[] { _loadImage("inky-right-1.png"), _loadImage("inky-right-2.png") });
/* 435 */       ((EnumMap)this.ghosts.get(Constants.GHOST.INKY)).put(Constants.MOVE.DOWN, new BufferedImage[] { _loadImage("inky-down-1.png"), _loadImage("inky-down-2.png") });
/* 436 */       ((EnumMap)this.ghosts.get(Constants.GHOST.INKY)).put(Constants.MOVE.LEFT, new BufferedImage[] { _loadImage("inky-left-1.png"), _loadImage("inky-left-2.png") });
/*     */       
/* 438 */       this.ghosts.put(Constants.GHOST.SUE, new EnumMap(Constants.MOVE.class));
/* 439 */       ((EnumMap)this.ghosts.get(Constants.GHOST.SUE)).put(Constants.MOVE.UP, new BufferedImage[] { _loadImage("sue-up-1.png"), _loadImage("sue-up-2.png") });
/* 440 */       ((EnumMap)this.ghosts.get(Constants.GHOST.SUE)).put(Constants.MOVE.RIGHT, new BufferedImage[] { _loadImage("sue-right-1.png"), _loadImage("sue-right-2.png") });
/* 441 */       ((EnumMap)this.ghosts.get(Constants.GHOST.SUE)).put(Constants.MOVE.DOWN, new BufferedImage[] { _loadImage("sue-down-1.png"), _loadImage("sue-down-2.png") });
/* 442 */       ((EnumMap)this.ghosts.get(Constants.GHOST.SUE)).put(Constants.MOVE.LEFT, new BufferedImage[] { _loadImage("sue-left-1.png"), _loadImage("sue-left-2.png") });
/*     */       
/* 444 */       this.edibleGhosts = new BufferedImage[2];
/* 445 */       this.edibleGhosts[0] = _loadImage("edible-ghost-1.png");
/* 446 */       this.edibleGhosts[1] = _loadImage("edible-ghost-2.png");
/*     */       
/* 448 */       this.edibleBlinkingGhosts = new BufferedImage[2];
/* 449 */       this.edibleBlinkingGhosts[0] = _loadImage("edible-ghost-blink-1.png");
/* 450 */       this.edibleBlinkingGhosts[1] = _loadImage("edible-ghost-blink-2.png");
/*     */       
/* 452 */       this.mazes = new BufferedImage[4];
/* 453 */       for (int i = 0; i < this.mazes.length; i++) {
/* 454 */         this.mazes[i] = _loadImage(Constants.mazeNames[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public BufferedImage getPacMan(Constants.MOVE move, int time) {
/* 459 */       return ((BufferedImage[])this.pacman.get(move))[(time % 6 / 2)];
/*     */     }
/*     */     
/*     */     public BufferedImage getPacManForExtraLives()
/*     */     {
/* 464 */       return ((BufferedImage[])this.pacman.get(Constants.MOVE.RIGHT))[0];
/*     */     }
/*     */     
/*     */     public BufferedImage getGhost(Constants.GHOST ghost, Constants.MOVE move, int time)
/*     */     {
/* 469 */       if (move == Constants.MOVE.NEUTRAL) {
/* 470 */         return ((BufferedImage[])((EnumMap)this.ghosts.get(ghost)).get(Constants.MOVE.UP))[(time % 6 / 3)];
/*     */       }
/* 472 */       return ((BufferedImage[])((EnumMap)this.ghosts.get(ghost)).get(move))[(time % 6 / 3)];
/*     */     }
/*     */     
/*     */     public BufferedImage getEdibleGhost(boolean blinking, int time)
/*     */     {
/* 477 */       if (!blinking) {
/* 478 */         return this.edibleGhosts[(time % 6 / 3)];
/*     */       }
/* 480 */       return this.edibleBlinkingGhosts[(time % 6 / 3)];
/*     */     }
/*     */     
/*     */     public BufferedImage getMaze(int mazeIndex)
/*     */     {
/* 485 */       return this.mazes[mazeIndex];
/*     */     }
/*     */     
/*     */     private BufferedImage _loadImage(String fileName)
/*     */     {
/* 490 */       BufferedImage image = null;
/*     */       
/*     */       try
/*     */       {
/* 494 */         image = ImageIO.read(new File(Constants.pathImages + System.getProperty("file.separator") + fileName));
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 498 */         e.printStackTrace();
/*     */       }
/*     */       
/* 501 */       return image;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\GameView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */