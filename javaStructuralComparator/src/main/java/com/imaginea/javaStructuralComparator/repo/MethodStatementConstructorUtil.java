package com.imaginea.javaStructuralComparator.repo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.imaginea.javaStructuralComparator.domain.Type;
import com.imaginea.javaStructuralComparator.domain.node.AbstractTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.DeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.FieldDeclarationNode;
//import com.imaginea.javaStructuralComparator.domain.node.Line;
import com.imaginea.javaStructuralComparator.domain.node.LineNode;
import com.imaginea.javaStructuralComparator.domain.node.MethodDeclarationNode;

/*
 * Statements to be handled:-
 * AssertStatement, Block, BreakStatement, ConstructorInvocation, ContinueStatement, DoStatement, 
 * EmptyStatement, EnhancedForStatement, ExpressionStatement, ForStatement, IfStatement, LabeledStatement, 
 * ReturnStatement, SuperConstructorInvocation, SwitchCase, SwitchStatement, SynchronizedStatement, 
 * ThrowStatement, TryStatement, TypeDeclarationStatement, VariableDeclarationStatement, WhileStatement
 */
public class MethodStatementConstructorUtil {

	private MethodStatementConstructorUtil() {
	}

	private static class SingletonHolder {
		public static final MethodStatementConstructorUtil INSTANCE = new MethodStatementConstructorUtil();
	}

	public static MethodStatementConstructorUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void processMethodStatements(List<Statement> statements, List<Object> blockofStatements, CompilationUnit compilationUnit) {
		for (Statement stmt : statements) {
			Object stmtObject = null;
			if (stmt.getClass() == VariableDeclarationStatement.class) {
				stmtObject = processStatement((VariableDeclarationStatement) stmt, compilationUnit);
			} else if (stmt.getClass() == TypeDeclarationStatement.class) {
				stmtObject = processStatement((TypeDeclarationStatement) stmt, compilationUnit);
			} else {
				stmtObject = processStatement(stmt, compilationUnit);
			}
			blockofStatements.add(stmtObject);
		}
	}

	private LineNode processStatement(Statement stmt, CompilationUnit compilationUnit) {
		LineNode line = new LineNode();
		// Line line = new Line();
		line.setValue(stmt.toString());
		line.setLineNum(compilationUnit.getLineNumber(stmt.getStartPosition()));
		return line;
	}

	@SuppressWarnings("unchecked")
	private FieldDeclarationNode processStatement(VariableDeclarationStatement stmt, CompilationUnit compilationUnit) {
		FieldDeclarationNode fieldNode = new FieldDeclarationNode();
		fieldNode.setType(stmt.getType().toString());
		fieldNode.setCompleteNodeValue(stmt.toString());
		fieldNode.setLineNum(compilationUnit.getLineNumber(stmt.getStartPosition()));
		fieldNode.setModifiers(DeclarationConstructorUtil.extractModifiers(stmt.modifiers()));
		DeclarationConstructorUtil.setFragmentDetails((VariableDeclarationFragment) stmt.fragments().get(0), fieldNode);
		return fieldNode;
	}

	private AbstractTypeDeclarationNode processStatement(TypeDeclarationStatement stmt, CompilationUnit compilationUnit) {
		AbstractTypeDeclaration typeDeclaration = stmt.getDeclaration();
		return DeclarationConstructorUtil.processType(typeDeclaration, compilationUnit);
	}

	public void compareMethodNode(MethodDeclarationNode expectedNode, Type type) {
		List<Type> commonChilds = type.getCommonChilds();

		MethodDeclarationNode actualNode = (MethodDeclarationNode) type.getAbstractDeclarations(0);

		List<Object> expectedNodeBlocks = expectedNode.getBlockofStatements();

		List<Object> actualNodeBlocks = actualNode.getBlockofStatements();
		Iterator<Object> iterator = actualNodeBlocks.iterator();
		while (iterator.hasNext() && expectedNodeBlocks.size() > 0) {
			Type childType = new Type();
			Object actualChildObject = iterator.next();
			childType.setAbstractDeclarations((DeclarationNode) actualChildObject, 0);
			iterator.remove();
			int index = expectedNodeBlocks.indexOf(actualChildObject);
			if (index == -1) {
				childType.setDiff(ComparatorUtil.ACTUALDEFAULT);
				if (actualChildObject instanceof AbstractTypeDeclarationNode) {
					DeclarationConstructorUtil.processDifference2ChildNodes((AbstractTypeDeclarationNode) actualChildObject,
							ComparatorUtil.ACTUALDEFAULT);
				}
			} else {
				if (index > 0) {
					commonChilds.addAll(processFirstNElements(expectedNodeBlocks, index, ComparatorUtil.EXPECTEDDEFAULT));
				}
				Object expectedChildObject = expectedNodeBlocks.remove(0);
				if (expectedChildObject instanceof LineNode) {
					childType.setDiff(ComparatorUtil.NOMODIFICATION);
				} else if (expectedChildObject instanceof FieldDeclarationNode) {
					if (DeclarationConstructorUtil.areChildNodesSame(actualChildObject, expectedChildObject, null)) {
						childType.setDiff(ComparatorUtil.NOMODIFICATION);
					}
				} else if (expectedChildObject instanceof AbstractTypeDeclarationNode) {
					DeclarationConstructorUtil.compareChildNodes(expectedChildObject, childType);
					boolean flag = DeclarationConstructorUtil.areChildNodesSame(actualChildObject, expectedChildObject,
							childType.getCommonChilds());
					if (flag)
						childType.setDiff(ComparatorUtil.NOMODIFICATION);
				}
				childType.setAbstractDeclarations((DeclarationNode) expectedChildObject, 1);
			}
			commonChilds.add(childType);
		}

		if (actualNodeBlocks.size() > 0) {
			commonChilds.addAll(processFirstNElements(actualNodeBlocks, actualNodeBlocks.size(), ComparatorUtil.ACTUALDEFAULT));
		}
		if (expectedNodeBlocks.size() > 0) {
			commonChilds.addAll(processFirstNElements(expectedNodeBlocks, expectedNodeBlocks.size(), ComparatorUtil.EXPECTEDDEFAULT));
		}

	}

	private List<Type> processFirstNElements(List<Object> nodeBlocks, int index, short defaultValue) {
		List<Type> typeList = new ArrayList<Type>();
		for (int i = 0; i < index; i++) {
			Type childType = new Type();
			childType.setDiff(defaultValue);
			Object childObject = nodeBlocks.remove(0);
			if (childObject instanceof AbstractTypeDeclarationNode) {
				DeclarationConstructorUtil.processDifference2ChildNodes((AbstractTypeDeclarationNode) childObject, defaultValue);
			}
			if (defaultValue == ComparatorUtil.ACTUALDEFAULT)
				childType.setAbstractDeclarations((DeclarationNode) childObject, 0);
			else if (defaultValue == ComparatorUtil.EXPECTEDDEFAULT)
				childType.setAbstractDeclarations((DeclarationNode) childObject, 1);
			typeList.add(childType);
		}
		return typeList;
	}

}
