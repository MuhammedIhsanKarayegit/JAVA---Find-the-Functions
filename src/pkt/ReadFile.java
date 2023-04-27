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
    	
    	String fileName = args[0]; // We have determined the file name and where the file will come from.
		String icerik = ""; // String variable to hold the file contents.
		icerik = new String(Files.readAllBytes(Paths.get(fileName)));	
		
		//In this part, regular expressions (regex) are adjusted according to the criteria we want to find one by one.
        String regexFunctionFinder = "\\b(?:public|private|protected)\\s+\\w+\\s+\\w+\\s*\\(.*?\\)\\s*|\\s+\\w+\\s*\\(.*?\\)\\s*";
		String regexOneLineComment = "//.*\\w+";
		String regexMultiLineComment = "/\\*[^*]+.*?\\*/";
		String regexJavaDocComment = "/\\*\\*.*?\\*/";
		
		// In this section, we have assigned the exact phrase we will look for.
		Pattern patternFuntction = Pattern.compile(regexFunctionFinder, Pattern.MULTILINE | Pattern.DOTALL);
		Pattern patternOneLine = Pattern.compile(regexOneLineComment);
		Pattern patternMultiLine = Pattern.compile(regexMultiLineComment,Pattern.DOTALL);
		Pattern patternJavaDocLine = Pattern.compile(regexJavaDocComment,Pattern.MULTILINE | Pattern.DOTALL );
		
		//We have created the requested files.
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
            /*If there is a single line comment in this section, it first writes the function after which it is located in the specified *.txt file.
             * */
            while (singleLineCommentsMatcher.find()) 
            {	
            	String oneComment = singleLineCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
                oneLineWriter.println("Fonksiyon: " + commentFunction);
            	oneLineWriter.println(oneComment);
            	oneLineWriter.println("-------------------------------------------------");
                singleLineComments++; //The counter increments by one for each single line comment found as we are looking for.     
            }
            singleLineCommentsMatcher.reset();// Resets the values ​​found after the operation is performed.
            oneLineWriter.flush();// It is used to immediately write the data in the stream. 
            
            Matcher multiLineCommentsMatcher = patternMultiLine.matcher(funcBody);
            int multiLineComments = 0;
            /*If there is a single line comment in this section, it first writes the function after which it is located in the specified *.txt file.
             * */
            while (multiLineCommentsMatcher.find()) 
            {	
            	String multiComment = multiLineCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
            	multiLineWriter.println("Function: " + commentFunction);
            	multiLineWriter.println(multiComment);
            	multiLineWriter.println("-------------------------------------------------");
                multiLineComments++;//The counter increments by one for each single line comment found as we are looking for.     
            }
            multiLineCommentsMatcher.reset();// Resets the values ​​found after the operation is performed.
            multiLineWriter.flush();// It is used to immediately write the data in the stream.
            
            Matcher javaDocCommentsMatcher = patternJavaDocLine.matcher(funcBody);
            int javaDocComments = 0;
            /*If there is a single line comment in this section, it first writes the function after which it is located in the specified *.txt file.
             * */
            while (javaDocCommentsMatcher.find()) 
            {
            	String javadocComment = javaDocCommentsMatcher.group();
            	String commentFunction = function.replaceAll("\\s+", "");
                javadocLineWriter.println("Function: " + commentFunction);
            	javadocLineWriter.println(javadocComment);
            	javadocLineWriter.println("-------------------------------------------------");
                javaDocComments++;//The counter increments by one for each single line comment found as we are looking for.
            }
            javaDocCommentsMatcher.reset();// Resets the values ​​found after the operation is performed.
            javadocLineWriter.flush();// It is used to immediately write the data in the stream.
            
            /*
             * 
             *This code runs from the end of the previous function definition to the beginning of the current function definition.
             *(i.e. the code between the two functions) and then finds the JavaDoc comments in that range and writes them to a file.
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
                javaDocComments++;//The counter increments by one for each single line comment found as we are looking for.
            }
            javaDocCommentsMatcher.reset();// Resets the values ​​found after the operation is performed.
            javadocLineWriter.flush();// It is used to immediately write the data in the stream.
            
            
            //In this section, screen printing methods are used to obtain the desired output from the script.
            System.out.print("     Bulunan fonksiyon-->" + function + "\n");
		    System.out.print("          Tek satirli yorum sayisi : " + singleLineComments + "\n");
		    System.out.print("          Cok satirli yorum sayisi : " + multiLineComments + "\n");
		    System.out.print("          JavaDoc yorum sayisi :     " + javaDocComments + "\n");
		    System.out.print("----------------------------------------------------\n");		    
       
        }
        // We turn off the writing functions that we open to write to the file.
        oneLineWriter.close();
        multiLineWriter.close();
        javadocLineWriter.close();
    }
}