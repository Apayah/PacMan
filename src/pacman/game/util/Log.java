/*    */ package pacman.game.util;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class Log
/*    */ {
/*    */   private static String fileName;
/*  8 */   private static Log log = null;
/*    */   private StringBuilder msg;
/*    */   private boolean timeStamp;
/*    */   private boolean console;
/*    */   
/*    */   private Log() {
/* 14 */     this.msg = new StringBuilder();
/*    */     
/* 16 */     fileName = "log.txt";
/* 17 */     this.timeStamp = false;
/* 18 */     this.console = false;
/*    */   }
/*    */   
/*    */   public static Log getLog()
/*    */   {
/* 23 */     if (log == null) {
/* 24 */       log = new Log();
/*    */     }
/* 26 */     return log;
/*    */   }
/*    */   
/*    */   public void enableConsolePrinting()
/*    */   {
/* 31 */     this.console = true;
/*    */   }
/*    */   
/*    */   public void disableConsolePrinting()
/*    */   {
/* 36 */     this.console = false;
/*    */   }
/*    */   
/*    */   public void setFile(String fileName)
/*    */   {
/* 41 */     fileName = fileName;
/*    */   }
/*    */   
/*    */   public void enableTimeStamp()
/*    */   {
/* 46 */     this.timeStamp = true;
/*    */   }
/*    */   
/*    */   public void disableTimeStamp()
/*    */   {
/* 51 */     this.timeStamp = false;
/*    */   }
/*    */   
/*    */   public void log(Object context, String message)
/*    */   {
/* 56 */     if (this.timeStamp)
/*    */     {
/* 58 */       String string = "[" + new Date().toString() + "; " + context.getClass().toString() + "]\t" + message;
/*    */       
/* 60 */       this.msg.append(string);
/*    */       
/* 62 */       if (this.console) {
/* 63 */         System.out.println(string);
/*    */       }
/*    */     }
/*    */     else {
/* 67 */       String string = "[" + context.getClass().toString() + "]\t" + message;
/*    */       
/* 69 */       this.msg.append(string);
/*    */       
/* 71 */       if (this.console) {
/* 72 */         System.out.println(string);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void clear() {
/* 78 */     this.msg = new StringBuilder();
/*    */   }
/*    */   
/*    */   public void saveLog(boolean append)
/*    */   {
/* 83 */     IO.saveFile(fileName, this.msg.toString(), append);
/*    */   }
/*    */ }


