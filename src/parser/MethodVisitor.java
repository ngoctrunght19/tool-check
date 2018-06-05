package parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import result.MethodDes;

public class MethodVisitor extends ASTVisitor {
	private List<MethodDes> methodDesList = new ArrayList<MethodDes>();
	
	public boolean visit(MethodDeclaration node) {
		MethodDes method = new MethodDes();
		String queryString = "";
		
		SimpleName name = node.getName();
		method.setMethodName(name.toString());
		TypeDeclaration clas = (TypeDeclaration) node.getParent();
		method.setClassName(clas.getName().toString());
		
		Block body = node.getBody();
		body.accept(new ASTVisitor() {
			private String queryString = "";
			
			public boolean visit(MethodInvocation node) {
				SimpleName methodName = node.getName();
				
				if (methodName.toString().equals("executeQuery")) {
					node.accept(new ASTVisitor() {
						public boolean visit(InfixExpression node) {
							Expression exp_left = node.getLeftOperand();
							exp_left.accept(new ASTVisitor() {
								public boolean visit(StringLiteral node) {
									queryString += node.getLiteralValue();
									return false;
								}
							});
							
							Expression exp_right = node.getRightOperand();
							exp_right.accept(new ASTVisitor() {
								public boolean visit(SimpleName node) {
									queryString += node.toString();
									return false;
								}
							});
							
							List<?> list = node.extendedOperands();
							for (Object object : list) {
								if (object instanceof StringLiteral) {
									StringLiteral sl = (StringLiteral) object;
									queryString += sl.getLiteralValue();
								} else if (object instanceof SimpleName) {
									SimpleName sn = (SimpleName) object;
									queryString += sn.toString();
								}
							}
							return false;
						}
						
						public boolean visit(StringLiteral node) {
							queryString += node.getLiteralValue();
							return false;
						}
					});
					CCJSqlParserManager pm = new CCJSqlParserManager();
					try {
						Statement statement = pm.parse(new StringReader(queryString));
						if (statement instanceof Select) {
							method.setAction("read");
						} else if (statement instanceof Insert) {
							method.setAction("write");
						} else if (statement instanceof Delete) {
							method.setAction("delete");
						}
					} catch (JSQLParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false;
			}
			
		});
		methodDesList.add(method);
		return false; // do not continue 
	}

	public List<MethodDes> getMethodList() {
		return methodDesList;
	}
	
}
