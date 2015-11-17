/*    */ package pacman.game.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IO
/*    */ {
/*    */   public static final String DIRECTORY = "myData/";
/*    */   
/*    */   public static boolean saveFile(String fileName, String data, boolean append)
/*    */   {
/*    */     try
/*    */     {
/* 39 */       FileOutputStream outS = new FileOutputStream("myData/" + fileName, append);
/* 40 */       PrintWriter pw = new PrintWriter(outS);
/*    */       
/* 42 */       pw.println(data);
/* 43 */       pw.flush();
/* 44 */       outS.close();
/*    */ 
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 49 */       e.printStackTrace();
/* 50 */       return false;
/*    */     }
/*    */     
/* 53 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String loadFile(String fileName)
/*    */   {
/* 64 */     StringBuffer data = new StringBuffer();
/*    */     
/*    */     try
/*    */     {
/* 68 */       BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("myData/" + fileName)));
/* 69 */       String input = br.readLine();
/*    */       
/* 71 */       while (input != null)
/*    */       {
/* 73 */         if (!input.equals("")) {
/* 74 */           data.append(input + "\n");
/*    */         }
/* 76 */         input = br.readLine();
/*    */       }
/*    */     }
/*    */     catch (IOException ioe)
/*    */     {
/* 81 */       ioe.printStackTrace();
/*    */     }
/*    */     
/* 84 */     return data.toString();
/*    */   }
/*    */ }


