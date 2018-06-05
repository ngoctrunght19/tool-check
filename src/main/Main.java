package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import parser.MethodVisitor;
import result.MethodDes;

public class Main {

    //use ASTParse to parse string
    public static void parse(String str) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(str.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        MethodVisitor mv = new MethodVisitor();
		cu.accept(mv);
		List<MethodDes> methodDesList = mv.getMethodList();
		String leftAlignFormat = "| %-20s | %-15s | %-10s | %-10s |%n";

		System.out.format("+----------------------+-----------------+------------+------------+%n");
		System.out.format("| class                | method          | action     | resource   |%n");
		System.out.format("+----------------------+-----------------+------------+------------+%n");
		for (MethodDes methodDes : methodDesList) {
			System.out.format(leftAlignFormat, methodDes.getClassName(), methodDes.getMethodName(), methodDes.getAction(), methodDes.getResouce());
		}
		System.out.format("+----------------------+-----------------+------------+------------+%n");
    }

    //read file content into a string
    public static String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            //System.out.println(numRead);
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return  fileData.toString();
    }

    //loop directory to get file list
    public static void ParseFilesInDir() throws IOException{
        File dirs = new File(".");
        String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;

        File root = new File(dirPath);
        //System.out.println(rootDir.listFiles());
        File[] files = root.listFiles ( );
        String filePath = null;

        for (File f : files ) {
            filePath = f.getAbsolutePath();
            if(f.isFile()){
            	parse(readFileToString(filePath));
            }
        }
    }


    public static void main(String[] args) throws IOException {
        parse(readFileToString("F:/Study/Projects/Healthcare1/src/DatabaseUtils/MRExploitations.java"));
    	
    }
}