/*     */ package pacman.controllers;
/*     */ 
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
/*     */ public abstract class Controller<T>
/*     */   implements Runnable
/*     */ {
/*     */   private boolean alive;
/*     */   private boolean wasSignalled;
/*     */   private boolean hasComputed;
/*     */   private volatile boolean threadStillRunning;
/*     */   private long timeDue;
/*     */   private Game game;
/*     */   protected T lastMove;
/*     */   
/*     */   public Controller()
/*     */   {
/*  26 */     this.alive = true;
/*  27 */     this.wasSignalled = false;
/*  28 */     this.hasComputed = false;
/*  29 */     this.threadStillRunning = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void terminate()
/*     */   {
/*  38 */     this.alive = false;
/*  39 */     this.wasSignalled = true;
/*     */     
/*  41 */     synchronized (this)
/*     */     {
/*  43 */       notify();
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
/*     */   public final void update(Game game, long timeDue)
/*     */   {
/*  56 */     synchronized (this)
/*     */     {
/*  58 */       this.game = game;
/*  59 */       this.timeDue = timeDue;
/*  60 */       this.wasSignalled = true;
/*  61 */       this.hasComputed = false;
/*  62 */       notify();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final T getMove()
/*     */   {
/*  73 */     return (T)this.lastMove;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void run()
/*     */   {
/*  81 */     while (this.alive)
/*     */     {
/*  83 */       synchronized (this)
/*     */       {
/*  85 */         while (!this.wasSignalled)
/*     */         {
/*     */           try
/*     */           {
/*  89 */             wait();
/*     */           }
/*     */           catch (InterruptedException e)
/*     */           {
/*  93 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         
/*  97 */         if (!this.threadStillRunning)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */           new Thread()
/*     */           {
/*     */             public void run()
/*     */             {
/* 103 */               Controller.this.threadStillRunning = true;
/* 104 */               Controller.this.lastMove = Controller.this.getMove(Controller.this.game, Controller.this.timeDue);
/* 105 */               Controller.this.hasComputed = true;
/* 106 */               Controller.this.threadStillRunning = false;
/*     */             }
/*     */           }.start();
/*     */         }
/*     */         
/* 111 */         this.wasSignalled = false;
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
/*     */   public final boolean hasComputed()
/*     */   {
/* 124 */     return this.hasComputed;
/*     */   }
/*     */   
/*     */   public abstract T getMove(Game paramGame, long paramLong);
/*     */ }


/* Location:              C:\Users\Adit\Desktop\pacman-master\PacManVsGhosts6.2.jar!\pacman\controllers\Controller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */