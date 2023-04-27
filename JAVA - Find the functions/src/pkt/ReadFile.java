/**
*
* @author Muhammed İhsan Karayeğit 
* @since 10.04.2023 / 23.04.2023
* <p>
* 2. Öğretim A grubu
* </p>
*/



package pkt;

import java.io.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ReadFile {

    public static void main(String[] args) throws IOException {
    	
    	String fileName = args[0]; // Dosya adı ve dosyanın nereden geleceğini belirledik.
		String icerik = ""; // Dosya içeriğini tutacak string değişkeni.
		icerik = new String(Files.readAllBytes(Paths.get(fileName)));	
		
		//Bu kısımda düzenli ifadeler (regex) tek tek bulmak istediğimiz kriterlere göre ayarlandı.
        String regexFunctionFinder = "\\b(?:public|private|protected)\\s+\\w+\\s+\\w+\\s*\\(.*?\\)\\s*|\\s+\\w+\\s*\\(.*?\\)\\s*";
		String regexOneLineComment = "//.*\\w+";
		String regexMultiLineComment = "/\\*[^*]+.*?\\*/";
		String regexJavaDocComment = "/\\*\\*.*?\\*/";
		
		// Bu kısımda arayacağımız ifadeyi tam olarak atama yaptık.
		Pattern patternFuntction = Pattern.compile(regexFunctionFinder, Pattern.MULTILINE | Pattern.DOTALL);
		Pattern patternOneLine = Pattern.compile(regexOneLineComment);
		Pattern patternMultiLine = Pattern.compile(regexMultiLineComment,Pattern.DOTALL);
		Pattern patternJavaDocLine = Pattern.compile(regexJavaDocComment,Pattern.MULTILINE | Pattern.DOTALL );
		
		//Bizden istenilen dosyaları oluşturduk.
        PrintWriter oneLineWriter = new PrintWriter("OneLineCommets.txt", "UTF-8");  
		PrintWriter multiLineWriter = new PrintWriter("MultiLineCommets.txt", "UTF-8");
		PrintWriter javadocLineWriter = new PrintWriter("JavaDocLineCommets.txt", "UTF-8");
		
		Matcher matcherFunctions = patternFuntction.matcher(icerik);
        while (matcherFunctions.find()) {
            int funcStartIndex = matcherFunctions.end();
            int funcEndIndex = icerik.indexOf("}", funcStartIndex);
            if (funcEndIndex == -1) {
                funcEndIndex = icerik.length();
            }

            String funcBody = icerik.substring(funcStartIndex, funcEndIndex);
            String function = matcherFunctions.group();

            Matcher singleLineCommentsMatcher = patternOneLine.matcher(funcBody);
            int singleLineComments = 0;
            /*Bu kısımda tek satır yorum bulunması halinde önce hangi foksiyonda 
             * bulunduğu ardında bulunan fonksiyonu belirtilen *.txt dosyası içince yazıyor.
             * */
            while (singleLineCommentsMatcher.find()) 
            {	
            	String oneComment = singleLineCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
                oneLineWriter.println("Fonksiyon: " + commentFunction);
            	oneLineWriter.println(oneComment);
            	oneLineWriter.println("-------------------------------------------------");
                singleLineComments++; //Aradığımız şekilde bulunan her bir tek satır yorumu için sayaç bir artıyor.     
            }
            singleLineCommentsMatcher.reset();// İşlem gerçekleştikten sonra bulununan değerleri sıfırlar.
            oneLineWriter.flush();// Akıştaki verileri hemen yazmak için kullanılır. 
            
            Matcher multiLineCommentsMatcher = patternMultiLine.matcher(funcBody);
            int multiLineComments = 0;
            /*Bu kısımda tek satır yorum bulunması halinde önce hangi foksiyonda 
             * bulunduğu ardında bulunan fonksiyonu belirtilen *.txt dosyası içince yazıyor.
             * */
            while (multiLineCommentsMatcher.find()) 
            {	
            	String multiComment = multiLineCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
            	multiLineWriter.println("Function: " + commentFunction);
            	multiLineWriter.println(multiComment);
            	multiLineWriter.println("-------------------------------------------------");
                multiLineComments++;//Aradığımız şekilde bulunan her bir tek satır yorumu için sayaç bir artıyor.     
            }
            multiLineCommentsMatcher.reset();// İşlem gerçekleştikten sonra bulununan değerleri sıfırlar.
            multiLineWriter.flush();// Akıştaki verileri hemen yazmak için kullanılır.
            
            Matcher javaDocCommentsMatcher = patternJavaDocLine.matcher(funcBody);
            int javaDocComments = 0;
            /*Bu kısımda tek satır yorum bulunması halinde önce hangi foksiyonda 
             * bulunduğu ardında bulunan fonksiyonu belirtilen *.txt dosyası içince yazıyor.
             * */
            while (javaDocCommentsMatcher.find()) 
            {
            	String javadocComment = javaDocCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
                javadocLineWriter.println("Function: " + commentFunction);
            	javadocLineWriter.println(javadocComment);
            	javadocLineWriter.println("-------------------------------------------------");
                javaDocComments++;//Aradığımız şekilde bulunan her bir tek satır yorumu için sayaç bir artıyor.
            }
            javaDocCommentsMatcher.reset();// İşlem gerçekleştikten sonra bulununan değerleri sıfırlar.
            javadocLineWriter.flush();// Akıştaki verileri hemen yazmak için kullanılır.
            
            /*
             * Bu kod, önceki işlev tanımının sonundan, şu anki işlev tanımının başına 
             * kadar olan metni (yani, iki işlev arasındaki kodu) alır ve ardından bu 
             * aralıktaki JavaDoc yorumlarını bulur ve bir dosyaya yazar.
             * */
            int prevFuncEndIndex = Math.max(0, icerik.lastIndexOf("}", matcherFunctions.start()));
            String prevFuncContent = icerik.substring(prevFuncEndIndex, matcherFunctions.start());
            javaDocCommentsMatcher = patternJavaDocLine.matcher(prevFuncContent);
            while (javaDocCommentsMatcher.find()) 
            {
            	String javadocComment = javaDocCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
                javadocLineWriter.println("Function: " + commentFunction);
            	javadocLineWriter.println(javadocComment);
            	javadocLineWriter.println("-------------------------------------------------");
                javaDocComments++;//Aradığımız şekilde bulunan her bir tek satır yorumu için sayaç bir artıyor.
            }
            javaDocCommentsMatcher.reset();// İşlem gerçekleştikten sonra bulununan değerleri sıfırlar.
            javadocLineWriter.flush();// Akıştaki verileri hemen yazmak için kullanılır.
            
            
            //Bu kısımda komut dosyasındaki bizden istenilen çıktıyı elde etmek için ekrana yazdırma metodları kullanılmıştır.
            System.out.print("     Bulunan fonksiyon-->" + function + "\n");
		    System.out.print("          Tek satirli yorum sayisi : " + singleLineComments + "\n");
		    System.out.print("          Cok satirli yorum sayisi : " + multiLineComments + "\n");
		    System.out.print("          JavaDoc yorum sayisi :     " + javaDocComments + "\n");
		    System.out.print("----------------------------------------------------\n");		    
       
        }
        // Dosyaya yazmak için açtığımız yazım fonksiyonlarını kapatıyoruz.
        oneLineWriter.close();
        multiLineWriter.close();
        javadocLineWriter.close();
    }
}