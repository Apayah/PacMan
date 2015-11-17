/*      */ package pacman.game;
/*      */ 
/*      */ import java.util.BitSet;
/*      */ import java.util.EnumMap;
import java.util.Map;
/*      */ import java.util.Random;
/*      */ import pacman.game.internal.Ghost;
/*      */ import pacman.game.internal.Maze;
/*      */ import pacman.game.internal.Node;
/*      */ import pacman.game.internal.PacMan;
/*      */ import pacman.game.internal.PathsCache;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Game
/*      */ {
/*      */   private BitSet pills;
/*      */   private BitSet powerPills;
/*      */   private int mazeIndex;
/*      */   private int levelCount;
/*      */   private int currentLevelTime;
/*      */   private int totalTime;
/*      */   private int score;
/*      */   private int ghostEatMultiplier;
/*      */   private int timeOfLastGlobalReversal;
/*      */   private boolean gameOver;
/*      */   private boolean pacmanWasEaten;
/*      */   private boolean pillWasEaten;
/*      */   private boolean powerPillWasEaten;
/*      */   private EnumMap<Constants.GHOST, Boolean> ghostsEaten;
/*      */   private PacMan pacman;
/*      */   private EnumMap<Constants.GHOST, Ghost> ghosts;
/*   50 */   private static Maze[] mazes = new Maze[4];
/*      */   private Maze currentMaze;
/*      */   public static PathsCache[] caches;
/*      */   private Random rnd;
/*      */   private long seed;
/*      */   
/*   56 */   static { for (int i = 0; i < mazes.length; i++) {
/*   57 */       mazes[i] = new Maze(i);
/*      */     }
/*      */     
/*   60 */     caches = new PathsCache[4];
/*      */     
/*      */ 
/*      */ 
/*   64 */     for (int i = 0; i < mazes.length; i++)
/*      */     {
/*   66 */       caches[i] = new PathsCache(i);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Game(long seed)
/*      */   {
/*   87 */     this.seed = seed;
/*   88 */     this.rnd = new Random(seed);
/*      */     
/*   90 */     _init(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Game(long seed, int initialMaze)
/*      */   {
/*  101 */     this.seed = seed;
/*  102 */     this.rnd = new Random(seed);
/*      */     
/*  104 */     _init(initialMaze);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Game() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _init(int initialMaze)
/*      */   {
/*  119 */     this.mazeIndex = initialMaze;
/*  120 */     this.score = (this.currentLevelTime = this.levelCount = this.totalTime = 0);
/*  121 */     this.ghostEatMultiplier = 1;
/*  122 */     this.gameOver = false;
/*  123 */     this.timeOfLastGlobalReversal = -1;
/*  124 */     this.pacmanWasEaten = false;
/*  125 */     this.pillWasEaten = false;
/*  126 */     this.powerPillWasEaten = false;
/*      */     
/*  128 */     this.ghostsEaten = new EnumMap(Constants.GHOST.class);
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  130 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghost = arrayOfGHOST[i];
/*  131 */       this.ghostsEaten.put(ghost, Boolean.valueOf(false));
/*      */     }
/*  133 */     _setPills(this.currentMaze = mazes[this.mazeIndex]);
/*  134 */     _initGhosts();
/*      */     
/*  136 */     this.pacman = new PacMan(this.currentMaze.initialPacManNodeIndex, Constants.MOVE.LEFT, 3, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _newLevelReset()
/*      */   {
/*  144 */     this.mazeIndex = (++this.mazeIndex % 4);
/*  145 */     this.levelCount += 1;
/*  146 */     this.currentMaze = mazes[this.mazeIndex];
/*      */     
/*  148 */     this.currentLevelTime = 0;
/*  149 */     this.ghostEatMultiplier = 1;
/*      */     
/*  151 */     _setPills(this.currentMaze);
/*  152 */     _levelReset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _levelReset()
/*      */   {
/*  160 */     this.ghostEatMultiplier = 1;
/*      */     
/*  162 */     _initGhosts();
/*      */     
/*  164 */     this.pacman.currentNodeIndex = this.currentMaze.initialPacManNodeIndex;
/*  165 */     this.pacman.lastMoveMade = Constants.MOVE.LEFT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _setPills(Maze maze)
/*      */   {
/*  175 */     this.pills = new BitSet(this.currentMaze.pillIndices.length);
/*  176 */     this.pills.set(0, this.currentMaze.pillIndices.length);
/*  177 */     this.powerPills = new BitSet(this.currentMaze.powerPillIndices.length);
/*  178 */     this.powerPills.set(0, this.currentMaze.powerPillIndices.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _initGhosts()
/*      */   {
/*  186 */     this.ghosts = new EnumMap(Constants.GHOST.class);
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  188 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghostType = arrayOfGHOST[i];
/*  189 */       this.ghosts.put(ghostType, new Ghost(ghostType, this.currentMaze.lairNodeIndex, 0, 
/*  190 */         (int)(ghostType.initialLairTime * Math.pow(0.8999999761581421D, this.levelCount % 6)), Constants.MOVE.NEUTRAL));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getGameState()
/*      */   {
/*  203 */     StringBuilder sb = new StringBuilder();
/*      */     
/*  205 */     sb.append(this.mazeIndex + "," + this.totalTime + "," + this.score + "," + this.currentLevelTime + "," + this.levelCount + "," + 
/*  206 */       this.pacman.currentNodeIndex + "," + this.pacman.lastMoveMade + "," + this.pacman.numberOfLivesRemaining + "," + this.pacman.hasReceivedExtraLife + ",");
/*      */     
/*  208 */     for (Ghost ghost : this.ghosts.values()) {
/*  209 */       sb.append(ghost.currentNodeIndex + "," + ghost.edibleTime + "," + ghost.lairTime + "," + ghost.lastMoveMade + ",");
/*      */     }
/*  211 */     for (int i = 0; i < this.currentMaze.pillIndices.length; i++) {
/*  212 */       if (this.pills.get(i)) {
/*  213 */         sb.append("1");
/*      */       } else
/*  215 */         sb.append("0");
/*      */     }
/*  217 */     sb.append(",");
/*      */     
/*  219 */     for (int i = 0; i < this.currentMaze.powerPillIndices.length; i++) {
/*  220 */       if (this.powerPills.get(i)) {
/*  221 */         sb.append("1");
/*      */       } else
/*  223 */         sb.append("0");
/*      */     }
/*  225 */     sb.append(",");
/*  226 */     sb.append(this.timeOfLastGlobalReversal);
/*  227 */     sb.append(",");
/*  228 */     sb.append(this.pacmanWasEaten);
/*  229 */     sb.append(",");
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  231 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghost = arrayOfGHOST[i];
/*      */       
/*  233 */       sb.append(this.ghostsEaten.get(ghost));
/*  234 */       sb.append(",");
/*      */     }
/*      */     
/*  237 */     sb.append(this.pillWasEaten);
/*  238 */     sb.append(",");
/*  239 */     sb.append(this.powerPillWasEaten);
/*      */     
/*  241 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGameState(String gameState)
/*      */   {
/*  252 */     String[] values = gameState.split(",");
/*      */     
/*  254 */     int index = 0;
/*      */     
/*  256 */     this.mazeIndex = Integer.parseInt(values[(index++)]);
/*  257 */     this.totalTime = Integer.parseInt(values[(index++)]);
/*  258 */     this.score = Integer.parseInt(values[(index++)]);
/*  259 */     this.currentLevelTime = Integer.parseInt(values[(index++)]);
/*  260 */     this.levelCount = Integer.parseInt(values[(index++)]);
/*      */     
/*  262 */     this.pacman = new PacMan(Integer.parseInt(values[(index++)]), Constants.MOVE.valueOf(values[(index++)]), 
/*  263 */       Integer.parseInt(values[(index++)]), Boolean.parseBoolean(values[(index++)]));
/*      */     
/*  265 */     this.ghosts = new EnumMap(Constants.GHOST.class);
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  267 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghostType = arrayOfGHOST[i];
/*  268 */       this.ghosts.put(ghostType, new Ghost(ghostType, Integer.parseInt(values[(index++)]), Integer.parseInt(values[(index++)]), 
/*  269 */         Integer.parseInt(values[(index++)]), Constants.MOVE.valueOf(values[(index++)])));
/*      */     }
/*  271 */     _setPills(this.currentMaze = mazes[this.mazeIndex]);
/*      */     
/*  273 */     for (int i = 0; i < values[index].length(); i++) {
/*  274 */       if (values[index].charAt(i) == '1') {
/*  275 */         this.pills.set(i);
/*      */       } else
/*  277 */         this.pills.clear(i);
/*      */     }
/*  279 */     index++;
/*      */     
/*  281 */     for (int i = 0; i < values[index].length(); i++) {
/*  282 */       if (values[index].charAt(i) == '1') {
/*  283 */         this.powerPills.set(i);
/*      */       } else{
/*  285 */         this.powerPills.clear(i);                    
/*      */     }
/*  287 */     this.timeOfLastGlobalReversal = Integer.parseInt(values[(++index)]);
/*  288 */     this.pacmanWasEaten = Boolean.parseBoolean(values[(++index)]);
/*      */     
/*  290 */     this.ghostsEaten = new EnumMap(Constants.GHOST.class);
/*      */     
/*  292 */     j = (arrayOfGHOST = Constants.GHOST.values()).length; for (i = 0; i < j; i++) { Constants.GHOST ghost = arrayOfGHOST[i];
/*  293 */       this.ghostsEaten.put(ghost, Boolean.valueOf(Boolean.parseBoolean(values[(++index)])));
/*      */     }
/*  295 */     this.pillWasEaten = Boolean.parseBoolean(values[(++index)]);
/*  296 */     this.powerPillWasEaten = Boolean.parseBoolean(values[(++index)]);
/*      */   }}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Game copy()
/*      */   {
/*  307 */     Game copy = new Game();
/*      */     
/*  309 */     copy.seed = this.seed;
/*  310 */     copy.rnd = new Random(this.seed);
/*  311 */     copy.currentMaze = this.currentMaze;
/*  312 */     copy.pills = ((BitSet)this.pills.clone());
/*  313 */     copy.powerPills = ((BitSet)this.powerPills.clone());
/*  314 */     copy.mazeIndex = this.mazeIndex;
/*  315 */     copy.levelCount = this.levelCount;
/*  316 */     copy.currentLevelTime = this.currentLevelTime;
/*  317 */     copy.totalTime = this.totalTime;
/*  318 */     copy.score = this.score;
/*  319 */     copy.ghostEatMultiplier = this.ghostEatMultiplier;
/*  320 */     copy.gameOver = this.gameOver;
/*  321 */     copy.timeOfLastGlobalReversal = this.timeOfLastGlobalReversal;
/*  322 */     copy.pacmanWasEaten = this.pacmanWasEaten;
/*  323 */     copy.pillWasEaten = this.pillWasEaten;
/*  324 */     copy.powerPillWasEaten = this.powerPillWasEaten;
/*  325 */     copy.pacman = this.pacman.copy();
/*      */     
/*  327 */     copy.ghostsEaten = new EnumMap(Constants.GHOST.class);
/*  328 */     copy.ghosts = new EnumMap(Constants.GHOST.class);
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  330 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghostType = arrayOfGHOST[i];
/*      */       
/*  332 */       copy.ghosts.put(ghostType, ((Ghost)this.ghosts.get(ghostType)).copy());
/*  333 */       copy.ghostsEaten.put(ghostType, (Boolean)this.ghostsEaten.get(ghostType));
/*      */     }
/*      */     
/*  336 */     return copy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void advanceGame(Constants.MOVE pacManMove, EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves)
/*      */   {
/*  353 */     updatePacMan(pacManMove);
/*  354 */     updateGhosts(ghostMoves);
/*  355 */     updateGame();
/*      */   }
/*      */   
/*      */   public void advanceGameWithoutReverse(Constants.MOVE pacManMove, EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves)
/*      */   {
/*  360 */     updatePacMan(pacManMove);
/*  361 */     updateGhostsWithoutReverse(ghostMoves);
/*  362 */     updateGame();
/*      */   }
/*      */   
/*      */   public void advanceGameWithForcedReverse(Constants.MOVE pacManMove, EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves)
/*      */   {
/*  367 */     updatePacMan(pacManMove);
/*  368 */     updateGhostsWithForcedReverse(ghostMoves);
/*  369 */     updateGame();
/*      */   }
/*      */   
/*      */   public void advanceGameWithPowerPillReverseOnly(Constants.MOVE pacManMove, EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves)
/*      */   {
/*  374 */     updatePacMan(pacManMove);
/*      */     
/*  376 */     if (this.powerPillWasEaten) {
/*  377 */       updateGhostsWithForcedReverse(ghostMoves);
/*      */     } else {
/*  379 */       updateGhostsWithoutReverse(ghostMoves);
/*      */     }
/*  381 */     updateGame();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updatePacMan(Constants.MOVE pacManMove)
/*      */   {
/*  391 */     _updatePacMan(pacManMove);
/*  392 */     _eatPill();
/*  393 */     _eatPowerPill();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateGhosts(EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves)
/*      */   {
/*  403 */     ghostMoves = _completeGhostMoves(ghostMoves);
/*      */     
/*  405 */     if (!_reverseGhosts(ghostMoves, false)) {
/*  406 */       _updateGhosts(ghostMoves);
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateGhostsWithoutReverse(EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves) {
/*  411 */     ghostMoves = _completeGhostMoves(ghostMoves);
/*  412 */     _updateGhosts(ghostMoves);
/*      */   }
/*      */   
/*      */   public void updateGhostsWithForcedReverse(EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves)
/*      */   {
/*  417 */     ghostMoves = _completeGhostMoves(ghostMoves);
/*  418 */     _reverseGhosts(ghostMoves, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateGame()
/*      */   {
/*  428 */     _feast();
/*  429 */     _updateLairTimes();
/*  430 */     _updatePacManExtraLife();
/*      */     
/*  432 */     this.totalTime += 1;
/*  433 */     this.currentLevelTime += 1;
/*      */     
/*  435 */     _checkLevelState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateGame(boolean feast, boolean updateLairTimes, boolean updateExtraLife, boolean updateTotalTime, boolean updateLevelTime)
/*      */   {
/*  450 */     if (feast) _feast();
/*  451 */     if (updateLairTimes) _updateLairTimes();
/*  452 */     if (updateExtraLife) { _updatePacManExtraLife();
/*      */     }
/*  454 */     if (updateTotalTime) this.totalTime += 1;
/*  455 */     if (updateLevelTime) { this.currentLevelTime += 1;
/*      */     }
/*  457 */     _checkLevelState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _updateLairTimes()
/*      */   {
/*  465 */     for (Ghost ghost : this.ghosts.values()) {
/*  466 */       if (ghost.lairTime > 0) {
/*  467 */         if (--ghost.lairTime == 0) {
/*  468 */           ghost.currentNodeIndex = this.currentMaze.initialGhostNodeIndex;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void _updatePacManExtraLife()
/*      */   {
/*  476 */     if ((!this.pacman.hasReceivedExtraLife) && (this.score >= 10000))
/*      */     {
/*  478 */       this.pacman.hasReceivedExtraLife = true;
/*  479 */       this.pacman.numberOfLivesRemaining += 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _updatePacMan(Constants.MOVE move)
/*      */   {
/*  490 */     this.pacman.lastMoveMade = _correctPacManDir(move);
/*  491 */     this.pacman.currentNodeIndex = (this.pacman.lastMoveMade == Constants.MOVE.NEUTRAL ? this.pacman.currentNodeIndex : 
/*  492 */       ((Integer)this.currentMaze.graph[this.pacman.currentNodeIndex].neighbourhood.get(this.pacman.lastMoveMade)).intValue());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Constants.MOVE _correctPacManDir(Constants.MOVE direction)
/*      */   {
/*  503 */     Node node = this.currentMaze.graph[this.pacman.currentNodeIndex];
/*      */     
/*      */ 
/*  506 */     if (node.neighbourhood.containsKey(direction)) {
/*  507 */       return direction;
/*      */     }
/*      */     
/*      */ 
/*  511 */     if (node.neighbourhood.containsKey(this.pacman.lastMoveMade)) {
/*  512 */       return this.pacman.lastMoveMade;
/*      */     }
/*      */     
/*  515 */     return Constants.MOVE.NEUTRAL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _updateGhosts(EnumMap<Constants.GHOST, Constants.MOVE> moves)
/*      */   {
/*  526 */     for (Map.Entry<Constants.GHOST, Constants.MOVE> entry : moves.entrySet())
/*      */     {
/*  528 */       Ghost ghost = (Ghost)this.ghosts.get(entry.getKey());
/*      */       
/*  530 */       if (ghost.lairTime == 0)
/*      */       {
/*  532 */         if ((ghost.edibleTime == 0) || (ghost.edibleTime % 2 != 0))
/*      */         {
/*  534 */           ghost.lastMoveMade = _checkGhostDir(ghost, (Constants.MOVE)entry.getValue());
/*  535 */           moves.put((Constants.GHOST)entry.getKey(), ghost.lastMoveMade);
/*  536 */           ghost.currentNodeIndex = ((Integer)this.currentMaze.graph[ghost.currentNodeIndex].neighbourhood.get(ghost.lastMoveMade)).intValue();
/*      */         } }
/*      */     }
/*      */   }
/*      */   
/*      */   private EnumMap<Constants.GHOST, Constants.MOVE> _completeGhostMoves(EnumMap<Constants.GHOST, Constants.MOVE> moves) { Constants.GHOST[] arrayOfGHOST;
/*      */     int j;
/*      */     int i;
/*  544 */     if (moves == null)
/*      */     {
/*  546 */       moves = new EnumMap(Constants.GHOST.class);
/*      */       
/*  548 */       j = (arrayOfGHOST = Constants.GHOST.values()).length; for (i = 0; i < j; i++) { Constants.GHOST ghostType = arrayOfGHOST[i];
/*  549 */         moves.put(ghostType, ((Ghost)this.ghosts.get(ghostType)).lastMoveMade);
/*      */       }
/*      */     }
/*  552 */     if (moves.size() < 4) {
/*  553 */       j = (arrayOfGHOST = Constants.GHOST.values()).length; for (i = 0; i < j; i++) { Constants.GHOST ghostType = arrayOfGHOST[i];
/*  554 */         if (!moves.containsKey(ghostType))
/*  555 */           moves.put(ghostType, Constants.MOVE.NEUTRAL);
/*      */       } }
/*  557 */     return moves;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Constants.MOVE _checkGhostDir(Ghost ghost, Constants.MOVE direction)
/*      */   {
/*  570 */     Node node = this.currentMaze.graph[ghost.currentNodeIndex];
/*      */     
/*      */ 
/*  573 */     if ((node.neighbourhood.containsKey(direction)) && (direction != ghost.lastMoveMade.opposite())) {
/*  574 */       return direction;
/*      */     }
/*      */     
/*  577 */     if (node.neighbourhood.containsKey(ghost.lastMoveMade)) {
/*  578 */       return ghost.lastMoveMade;
/*      */     }
/*      */     
/*  581 */     Constants.MOVE[] moves = (Constants.MOVE[])node.allPossibleMoves.get(ghost.lastMoveMade);
/*  582 */     return moves[this.rnd.nextInt(moves.length)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _eatPill()
/*      */   {
/*  592 */     this.pillWasEaten = false;
/*      */     
/*  594 */     int pillIndex = this.currentMaze.graph[this.pacman.currentNodeIndex].pillIndex;
/*      */     
/*  596 */     if ((pillIndex >= 0) && (this.pills.get(pillIndex)))
/*      */     {
/*  598 */       this.score += 10;
/*  599 */       this.pills.clear(pillIndex);
/*  600 */       this.pillWasEaten = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _eatPowerPill()
/*      */   {
/*  609 */     this.powerPillWasEaten = false;
/*      */     
/*  611 */     int powerPillIndex = this.currentMaze.graph[this.pacman.currentNodeIndex].powerPillIndex;
/*      */     
/*  613 */     if ((powerPillIndex >= 0) && (this.powerPills.get(powerPillIndex)))
/*      */     {
/*  615 */       this.score += 50;
/*  616 */       this.ghostEatMultiplier = 1;
/*  617 */       this.powerPills.clear(powerPillIndex);
/*      */       
/*  619 */       int newEdibleTime = (int)(200.0D * Math.pow(0.8999999761581421D, this.levelCount % 6));
/*      */       
/*  621 */       for (Ghost ghost : this.ghosts.values()) {
/*  622 */         if (ghost.lairTime == 0) {
/*  623 */           ghost.edibleTime = newEdibleTime;
/*      */         } else
/*  625 */           ghost.edibleTime = 0;
/*      */       }
/*  627 */       this.powerPillWasEaten = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean _reverseGhosts(EnumMap<Constants.GHOST, Constants.MOVE> moves, boolean force)
/*      */   {
/*  633 */     boolean reversed = false;
/*  634 */     boolean globalReverse = false;
/*      */     
/*  636 */     if (Math.random() < 0.001500000013038516D) {
/*  637 */       globalReverse = true;
/*      */     }
/*  639 */     for (Map.Entry<Constants.GHOST, Constants.MOVE> entry : moves.entrySet())
/*      */     {
/*  641 */       Ghost ghost = (Ghost)this.ghosts.get(entry.getKey());
/*      */       
/*  643 */       if ((this.currentLevelTime > 1) && (ghost.lairTime == 0) && (ghost.lastMoveMade != Constants.MOVE.NEUTRAL))
/*      */       {
/*  645 */         if ((force) || (this.powerPillWasEaten) || (globalReverse))
/*      */         {
/*  647 */           ghost.lastMoveMade = ghost.lastMoveMade.opposite();
/*  648 */           ghost.currentNodeIndex = ((Integer)this.currentMaze.graph[ghost.currentNodeIndex].neighbourhood.get(ghost.lastMoveMade)).intValue();
/*  649 */           reversed = true;
/*  650 */           this.timeOfLastGlobalReversal = this.totalTime;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  655 */     return reversed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _feast()
/*      */   {
/*  663 */     this.pacmanWasEaten = false;
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  665 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghost = arrayOfGHOST[i];
/*  666 */       this.ghostsEaten.put(ghost, Boolean.valueOf(false));
/*      */     }
/*  668 */     for (Ghost ghost : this.ghosts.values())
/*      */     {
/*  670 */       int distance = getShortestPathDistance(this.pacman.currentNodeIndex, ghost.currentNodeIndex);
/*      */       
/*  672 */       if ((distance <= 2) && (distance != -1))
/*      */       {
/*  674 */         if (ghost.edibleTime > 0)
/*      */         {
/*  676 */           this.score += 200 * this.ghostEatMultiplier;
/*  677 */           this.ghostEatMultiplier *= 2;
/*  678 */           ghost.edibleTime = 0;
/*  679 */           ghost.lairTime = ((int)(40.0D * Math.pow(0.8999999761581421D, this.levelCount % 6)));
/*  680 */           ghost.currentNodeIndex = this.currentMaze.lairNodeIndex;
/*  681 */           ghost.lastMoveMade = Constants.MOVE.NEUTRAL;
/*      */           
/*  683 */           this.ghostsEaten.put(ghost.type, Boolean.valueOf(true));
/*      */         }
/*      */         else
/*      */         {
/*  687 */           this.pacman.numberOfLivesRemaining -= 1;
/*  688 */           this.pacmanWasEaten = true;
/*      */           
/*  690 */           if (this.pacman.numberOfLivesRemaining <= 0) {
/*  691 */             this.gameOver = true;
/*      */           } else {
/*  693 */             _levelReset();
/*      */           }
/*  695 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  700 */     for (Ghost ghost : this.ghosts.values()) {
/*  701 */       if (ghost.edibleTime > 0) {
/*  702 */         ghost.edibleTime -= 1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void _checkLevelState()
/*      */   {
/*  711 */     if (this.totalTime + 1 > 24000)
/*      */     {
/*  713 */       this.gameOver = true;
/*  714 */       this.score += this.pacman.numberOfLivesRemaining * 800;
/*      */ 
/*      */     }
/*  717 */     else if (((this.pills.isEmpty()) && (this.powerPills.isEmpty())) || (this.currentLevelTime >= 4000)) {
/*  718 */       _newLevelReset();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean wasPacManEaten()
/*      */   {
/*  732 */     return this.pacmanWasEaten;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean wasGhostEaten(Constants.GHOST ghost)
/*      */   {
/*  742 */     return ((Boolean)this.ghostsEaten.get(ghost)).booleanValue();
/*      */   }
/*      */   
/*      */   public int getNumGhostsEaten()
/*      */   {
/*  747 */     int count = 0;
/*      */     Constants.GHOST[] arrayOfGHOST;
/*  749 */     int j = (arrayOfGHOST = Constants.GHOST.values()).length; for (int i = 0; i < j; i++) { Constants.GHOST ghost = arrayOfGHOST[i];
/*  750 */       if (((Boolean)this.ghostsEaten.get(ghost)).booleanValue())
/*  751 */         count++;
/*      */     }
/*  753 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean wasPillEaten()
/*      */   {
/*  763 */     return this.pillWasEaten;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean wasPowerPillEaten()
/*      */   {
/*  773 */     return this.powerPillWasEaten;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTimeOfLastGlobalReversal()
/*      */   {
/*  783 */     return this.timeOfLastGlobalReversal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean gameOver()
/*      */   {
/*  794 */     return this.gameOver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Maze getCurrentMaze()
/*      */   {
/*  804 */     return this.currentMaze;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNodeXCood(int nodeIndex)
/*      */   {
/*  815 */     return this.currentMaze.graph[nodeIndex].x;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNodeYCood(int nodeIndex)
/*      */   {
/*  826 */     return this.currentMaze.graph[nodeIndex].y;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMazeIndex()
/*      */   {
/*  836 */     return this.mazeIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCurrentLevel()
/*      */   {
/*  846 */     return this.levelCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNumberOfNodes()
/*      */   {
/*  856 */     return this.currentMaze.graph.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGhostCurrentEdibleScore()
/*      */   {
/*  866 */     return 200 * this.ghostEatMultiplier;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGhostInitialNodeIndex()
/*      */   {
/*  877 */     return this.currentMaze.initialGhostNodeIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPillStillAvailable(int pillIndex)
/*      */   {
/*  888 */     return this.pills.get(pillIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPowerPillStillAvailable(int powerPillIndex)
/*      */   {
/*  899 */     return this.powerPills.get(powerPillIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPillIndex(int nodeIndex)
/*      */   {
/*  911 */     return this.currentMaze.graph[nodeIndex].pillIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPowerPillIndex(int nodeIndex)
/*      */   {
/*  923 */     return this.currentMaze.graph[nodeIndex].powerPillIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getJunctionIndices()
/*      */   {               
/*  933 */     return this.currentMaze.junctionIndices;            
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getPillIndices()
/*      */   {
/*  943 */     return this.currentMaze.pillIndices;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getPowerPillIndices()
/*      */   {
/*  953 */     return this.currentMaze.powerPillIndices;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPacmanCurrentNodeIndex()
/*      */   {
/*  963 */     return this.pacman.currentNodeIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getPacmanLastMoveMade()
/*      */   {
/*  973 */     return this.pacman.lastMoveMade;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPacmanNumberOfLivesRemaining()
/*      */   {
/*  983 */     return this.pacman.numberOfLivesRemaining;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGhostCurrentNodeIndex(Constants.GHOST ghostType)
/*      */   {
/*  994 */     return ((Ghost)this.ghosts.get(ghostType)).currentNodeIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getGhostLastMoveMade(Constants.GHOST ghostType)
/*      */   {
/* 1005 */     return ((Ghost)this.ghosts.get(ghostType)).lastMoveMade;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGhostEdibleTime(Constants.GHOST ghostType)
/*      */   {
/* 1016 */     return ((Ghost)this.ghosts.get(ghostType)).edibleTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isGhostEdible(Constants.GHOST ghostType)
/*      */   {
/* 1027 */     return ((Ghost)this.ghosts.get(ghostType)).edibleTime > 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getScore()
/*      */   {
/* 1037 */     return this.score;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCurrentLevelTime()
/*      */   {
/* 1047 */     return this.currentLevelTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTotalTime()
/*      */   {
/* 1057 */     return this.totalTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNumberOfPills()
/*      */   {
/* 1067 */     return this.currentMaze.pillIndices.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNumberOfPowerPills()
/*      */   {
/* 1077 */     return this.currentMaze.powerPillIndices.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNumberOfActivePills()
/*      */   {
/* 1087 */     return this.pills.cardinality();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNumberOfActivePowerPills()
/*      */   {
/* 1097 */     return this.powerPills.cardinality();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGhostLairTime(Constants.GHOST ghostType)
/*      */   {
/* 1108 */     return ((Ghost)this.ghosts.get(ghostType)).lairTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getActivePillsIndices()
/*      */   {
/* 1118 */     int[] indices = new int[this.pills.cardinality()];
/*      */     
/* 1120 */     int index = 0;
/*      */     
/* 1122 */     for (int i = 0; i < this.currentMaze.pillIndices.length; i++) {
/* 1123 */       if (this.pills.get(i))
/* 1124 */         indices[(index++)] = this.currentMaze.pillIndices[i];
/*      */     }
/* 1126 */     return indices;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getActivePowerPillsIndices()
/*      */   {
/* 1136 */     int[] indices = new int[this.powerPills.cardinality()];
/*      */     
/* 1138 */     int index = 0;
/*      */     
/* 1140 */     for (int i = 0; i < this.currentMaze.powerPillIndices.length; i++) {
/* 1141 */       if (this.powerPills.get(i))
/* 1142 */         indices[(index++)] = this.currentMaze.powerPillIndices[i];
/*      */     }
/* 1144 */     return indices;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean doesGhostRequireAction(Constants.GHOST ghostType)
/*      */   {
/* 1157 */     return ((isJunction(((Ghost)this.ghosts.get(ghostType)).currentNodeIndex)) || ((((Ghost)this.ghosts.get(ghostType)).lastMoveMade == Constants.MOVE.NEUTRAL) && (((Ghost)this.ghosts.get(ghostType)).currentNodeIndex == this.currentMaze.initialGhostNodeIndex))) && ((((Ghost)this.ghosts.get(ghostType)).edibleTime == 0) || (((Ghost)this.ghosts.get(ghostType)).edibleTime % 2 != 0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isJunction(int nodeIndex)
/*      */   {
/* 1168 */     return this.currentMaze.graph[nodeIndex].numNeighbouringNodes > 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE[] getPossibleMoves(int nodeIndex)
/*      */   {
/* 1179 */     return (Constants.MOVE[])this.currentMaze.graph[nodeIndex].allPossibleMoves.get(Constants.MOVE.NEUTRAL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE[] getPossibleMoves(int nodeIndex, Constants.MOVE lastModeMade)
/*      */   {
/* 1191 */     return (Constants.MOVE[])this.currentMaze.graph[nodeIndex].allPossibleMoves.get(lastModeMade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getNeighbouringNodes(int nodeIndex)
/*      */   {
/* 1202 */     return (int[])this.currentMaze.graph[nodeIndex].allNeighbouringNodes.get(Constants.MOVE.NEUTRAL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getNeighbouringNodes(int nodeIndex, Constants.MOVE lastModeMade)
/*      */   {
/* 1215 */     return (int[])this.currentMaze.graph[nodeIndex].allNeighbouringNodes.get(lastModeMade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNeighbour(int nodeIndex, Constants.MOVE moveToBeMade)
/*      */   {
/* 1228 */     Integer neighbour = (Integer)this.currentMaze.graph[nodeIndex].neighbourhood.get(moveToBeMade);
/*      */     
/* 1230 */     return neighbour == null ? -1 : neighbour.intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getMoveToMakeToReachDirectNeighbour(int currentNodeIndex, int neighbourNodeIndex)
/*      */   {
/*      */     Constants.MOVE[] arrayOfMOVE;
/*      */     
/*      */ 
/*      */ 
/* 1243 */     int j = (arrayOfMOVE = Constants.MOVE.values()).length; for (int i = 0; i < j; i++) { Constants.MOVE move = arrayOfMOVE[i];
/*      */       
/* 1245 */       if ((this.currentMaze.graph[currentNodeIndex].neighbourhood.containsKey(move)) && 
/* 1246 */         (((Integer)this.currentMaze.graph[currentNodeIndex].neighbourhood.get(move)).intValue() == neighbourNodeIndex))
/*      */       {
/* 1248 */         return move;
/*      */       }
/*      */     }
/*      */     
/* 1252 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getShortestPathDistance(int fromNodeIndex, int toNodeIndex)
/*      */   {
/* 1268 */     if (fromNodeIndex == toNodeIndex)
/* 1269 */       return 0;
/* 1270 */     if (fromNodeIndex < toNodeIndex) {
/* 1271 */       return this.currentMaze.shortestPathDistances[(toNodeIndex * (toNodeIndex + 1) / 2 + fromNodeIndex)];
/*      */     }
/* 1273 */     return this.currentMaze.shortestPathDistances[(fromNodeIndex * (fromNodeIndex + 1) / 2 + toNodeIndex)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getEuclideanDistance(int fromNodeIndex, int toNodeIndex)
/*      */   {
/* 1285 */     return Math.sqrt(Math.pow(this.currentMaze.graph[fromNodeIndex].x - this.currentMaze.graph[toNodeIndex].x, 2.0D) + Math.pow(this.currentMaze.graph[fromNodeIndex].y - this.currentMaze.graph[toNodeIndex].y, 2.0D));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getManhattanDistance(int fromNodeIndex, int toNodeIndex)
/*      */   {
/* 1297 */     return Math.abs(this.currentMaze.graph[fromNodeIndex].x - this.currentMaze.graph[toNodeIndex].x) + Math.abs(this.currentMaze.graph[fromNodeIndex].y - this.currentMaze.graph[toNodeIndex].y);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDistance(int fromNodeIndex, int toNodeIndex, Constants.DM distanceMeasure)
/*      */   {
/* 1310 */     switch (distanceMeasure) {
/*      */     case EUCLID: 
/* 1312 */       return getShortestPathDistance(fromNodeIndex, toNodeIndex);
/* 1313 */     case MANHATTAN:  return getEuclideanDistance(fromNodeIndex, toNodeIndex);
/* 1314 */     case PATH:  return getManhattanDistance(fromNodeIndex, toNodeIndex);
/*      */     }
/*      */     
/* 1317 */     return -1.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDistance(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade, Constants.DM distanceMeasure)
/*      */   {
/* 1331 */     switch (distanceMeasure) {
/*      */     case EUCLID: 
/* 1333 */       return getApproximateShortestPathDistance(fromNodeIndex, toNodeIndex, lastMoveMade);
/* 1334 */     case MANHATTAN:  return getEuclideanDistance(fromNodeIndex, toNodeIndex);
/* 1335 */     case PATH:  return getManhattanDistance(fromNodeIndex, toNodeIndex);
/*      */     }
/*      */     
/* 1338 */     return -1.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getClosestNodeIndexFromNodeIndex(int fromNodeIndex, int[] targetNodeIndices, Constants.DM distanceMeasure)
/*      */   {
/* 1351 */     double minDistance = 2.147483647E9D;
/* 1352 */     int target = -1;
/*      */     
/* 1354 */     for (int i = 0; i < targetNodeIndices.length; i++)
/*      */     {
/* 1356 */       double distance = 0.0D;
/*      */       
/* 1358 */       distance = getDistance(targetNodeIndices[i], fromNodeIndex, distanceMeasure);
/*      */       
/* 1360 */       if (distance < minDistance)
/*      */       {
/* 1362 */         minDistance = distance;
/* 1363 */         target = targetNodeIndices[i];
/*      */       }
/*      */     }
/*      */     
/* 1367 */     return target;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFarthestNodeIndexFromNodeIndex(int fromNodeIndex, int[] targetNodeIndices, Constants.DM distanceMeasure)
/*      */   {
/* 1380 */     double maxDistance = -2.147483648E9D;
/* 1381 */     int target = -1;
/*      */     
/* 1383 */     for (int i = 0; i < targetNodeIndices.length; i++)
/*      */     {
/* 1385 */       double distance = 0.0D;
/*      */       
/* 1387 */       distance = getDistance(targetNodeIndices[i], fromNodeIndex, distanceMeasure);
/*      */       
/* 1389 */       if (distance > maxDistance)
/*      */       {
/* 1391 */         maxDistance = distance;
/* 1392 */         target = targetNodeIndices[i];
/*      */       }
/*      */     }
/*      */     
/* 1396 */     return target;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getNextMoveTowardsTarget(int fromNodeIndex, int toNodeIndex, Constants.DM distanceMeasure)
/*      */   {
/* 1409 */     Constants.MOVE move = null;
/*      */     
/* 1411 */     double minDistance = 2.147483647E9D;
/*      */     
/* 1413 */     for (Map.Entry<Constants.MOVE, Integer> entry : this.currentMaze.graph[fromNodeIndex].neighbourhood.entrySet())
/*      */     {
/* 1415 */       double distance = getDistance(((Integer)entry.getValue()).intValue(), toNodeIndex, distanceMeasure);
/*      */       
/* 1417 */       if (distance < minDistance)
/*      */       {
/* 1419 */         minDistance = distance;
/* 1420 */         move = (Constants.MOVE)entry.getKey();
/*      */       }
/*      */     }
/*      */     
/* 1424 */     return move;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getNextMoveAwayFromTarget(int fromNodeIndex, int toNodeIndex, Constants.DM distanceMeasure)
/*      */   {
/* 1437 */     Constants.MOVE move = null;
/*      */     
/* 1439 */     double maxDistance = -2.147483648E9D;
/*      */     
/* 1441 */     for (Map.Entry<Constants.MOVE, Integer> entry : this.currentMaze.graph[fromNodeIndex].neighbourhood.entrySet())
/*      */     {
/* 1443 */       double distance = getDistance(((Integer)entry.getValue()).intValue(), toNodeIndex, distanceMeasure);
/*      */       
/* 1445 */       if (distance > maxDistance)
/*      */       {
/* 1447 */         maxDistance = distance;
/* 1448 */         move = (Constants.MOVE)entry.getKey();
/*      */       }
/*      */     }
/*      */     
/* 1452 */     return move;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getApproximateNextMoveTowardsTarget(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade, Constants.DM distanceMeasure)
/*      */   {
/* 1466 */     Constants.MOVE move = null;
/*      */     
/* 1468 */     double minDistance = 2.147483647E9D;
/*      */     
/* 1470 */     for (Map.Entry<Constants.MOVE, Integer> entry : (this.currentMaze.graph[fromNodeIndex].allNeighbourhoods.get(lastMoveMade)).entrySet())
/*      */     {
/* 1472 */       double distance = getDistance(((Integer)entry.getValue()).intValue(), toNodeIndex, distanceMeasure);
/*      */      
/* 1474 */       if (distance < minDistance)
/*      */       {
/* 1476 */         minDistance = distance;
/* 1477 */         move = (Constants.MOVE)entry.getKey();
/*      */       }
/*      */     }
/*      */     
/* 1481 */     return move;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getApproximateNextMoveAwayFromTarget(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade, Constants.DM distanceMeasure)
/*      */   {
/* 1495 */     Constants.MOVE move = null;
/*      */     
/* 1497 */     double maxDistance = -2.147483648E9D;
/*      */     
/* 1499 */     for (Map.Entry<Constants.MOVE, Integer> entry : this.currentMaze.graph[fromNodeIndex].allNeighbourhoods.get(lastMoveMade).entrySet())
/*      */     {
/* 1501 */       double distance = getDistance(((Integer)entry.getValue()).intValue(), toNodeIndex, distanceMeasure);
/*      */       
/* 1503 */       if (distance > maxDistance)
/*      */       {
/* 1505 */         maxDistance = distance;
/* 1506 */         move = (Constants.MOVE)entry.getKey();
/*      */       }
/*      */     }
/*      */     
/* 1510 */     return move;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getNextMoveTowardsTarget(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade, Constants.DM distanceMeasure)
/*      */   {
/* 1524 */     Constants.MOVE move = null;
/*      */     
/* 1526 */     double minDistance = 2.147483647E9D;
/*      */     
/* 1528 */     for (Map.Entry<Constants.MOVE, Integer> entry : (this.currentMaze.graph[fromNodeIndex].allNeighbourhoods.get(lastMoveMade)).entrySet())
/*      */     {
/* 1530 */       double distance = getDistance(((Integer)entry.getValue()).intValue(), toNodeIndex, lastMoveMade, distanceMeasure);
/*      */       
/* 1532 */       if (distance < minDistance)
/*      */       {
/* 1534 */         minDistance = distance;
/* 1535 */         move = (Constants.MOVE)entry.getKey();
/*      */       }
/*      */     }
/*      */     
/* 1539 */     return move;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Constants.MOVE getNextMoveAwayFromTarget(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade, Constants.DM distanceMeasure)
/*      */   {
/* 1553 */     Constants.MOVE move = null;
/*      */     
/* 1555 */     double maxDistance = -2.147483648E9D;
/*      */     
/* 1557 */     for (Map.Entry<Constants.MOVE, Integer> entry : (this.currentMaze.graph[fromNodeIndex].allNeighbourhoods.get(lastMoveMade)).entrySet())
/*      */     {
/* 1559 */       double distance = getDistance(((Integer)entry.getValue()).intValue(), toNodeIndex, lastMoveMade, distanceMeasure);
/*      */       
/* 1561 */       if (distance > maxDistance)
/*      */       {
/* 1563 */         maxDistance = distance;
/* 1564 */         move = (Constants.MOVE)entry.getKey();
/*      */       }
/*      */     }
/*      */     
/* 1568 */     return move;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public int[] getAStarPath(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade)
/*      */   {
/* 1584 */     return getShortestPath(fromNodeIndex, toNodeIndex, lastMoveMade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getShortestPath(int fromNodeIndex, int toNodeIndex)
/*      */   {
/* 1596 */     return caches[this.mazeIndex].getPathFromA2B(fromNodeIndex, toNodeIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public int[] getApproximateShortestPath(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade)
/*      */   {
/* 1614 */     return getShortestPath(fromNodeIndex, toNodeIndex, lastMoveMade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getShortestPath(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade)
/*      */   {
/* 1629 */     if (this.currentMaze.graph[fromNodeIndex].neighbourhood.size() == 0) {
/* 1630 */       return new int[0];
/*      */     }
/* 1632 */     return caches[this.mazeIndex].getPathFromA2B(fromNodeIndex, toNodeIndex, lastMoveMade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public int getApproximateShortestPathDistance(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade)
/*      */   {
/* 1649 */     return getShortestPathDistance(fromNodeIndex, toNodeIndex, lastMoveMade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getShortestPathDistance(int fromNodeIndex, int toNodeIndex, Constants.MOVE lastMoveMade)
/*      */   {
/* 1663 */     if (this.currentMaze.graph[fromNodeIndex].neighbourhood.size() == 0) {
/* 1664 */       return 0;
/*      */     }
/* 1666 */     return caches[this.mazeIndex].getPathDistanceFromA2B(fromNodeIndex, toNodeIndex, lastMoveMade);
/*      */   }
/*      */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\game\Game.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */