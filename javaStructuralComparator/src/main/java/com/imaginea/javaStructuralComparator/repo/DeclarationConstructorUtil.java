package com.imaginea.javaStructuralComparator.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.imaginea.javaStructuralComparator.domain.Type;
import com.imaginea.javaStructuralComparator.domain.node.AbstractTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.ClassTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.DeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.EnumTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.FieldDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.MethodDeclarationNode;

public class DeclarationConstructorUtil {

	@SuppressWarnings("unchecked")
	public static AbstractTypeDeclarationNode processType(AbstractTypeDeclaration abstractDeclaration, CompilationUnit actualCompilationUnit) {
		AbstractTypeDeclarationNode declarationNode = null;
		if (abstractDeclaration.getClass() == TypeDeclaration.class)
			declarationNode = processClassType((TypeDeclaration) abstractDeclaration, actualCompilationUnit);
		else if (abstractDeclaration.getClass() == EnumDeclaration.class)
			declarationNode = processEnumType((EnumDeclaration) abstractDeclaration, actualCompilationUnit);

		declarationNode.setName(abstractDeclaration.getName().toString());
		declarationNode.setLineNum(actualCompilationUnit.getLineNumber(abstractDeclaration.getStartPosition()));
		declarationNode.setModifiers(extractModifiers(abstractDeclaration.modifiers()));
		declarationNode.setCompleteNodeValue(abstractDeclaration.toString());

		Map<String, Object> childs = declarationNode.getChildDeclarations();
		childs.putAll(processChilds(abstractDeclaration.bodyDeclarations(), actualCompilationUnit));

		return declarationNode;
	}

	private static Map<String, Object> processChilds(List<BodyDeclaration> bodyDeclarations, CompilationUnit compilationUnit) {
		Map<String, Object> childObjectsMap = new HashMap<String, Object>();
		for (BodyDeclaration declaration : bodyDeclarations) {
			String declarationName = "";
			Object declarationObject = null;
			if (declaration.getClass() == FieldDeclaration.class) {
				declarationObject = processFieldDeclaration((FieldDeclaration) declaration, compilationUnit);
				declarationName = ((FieldDeclarationNode) declarationObject).getName();
			} else if (declaration.getClass() == MethodDeclaration.class) {
				declarationObject = processMethodDeclaration((MethodDeclaration) declaration, compilationUnit);
				declarationName = ((MethodDeclarationNode) declarationObject).getName() + "_Method";
			} else if (declaration instanceof AbstractTypeDeclaration) {
				declarationObject = processType((AbstractTypeDeclaration) declaration, compilationUnit);
				declarationName = ComparatorUtil.addSuffix2TypeName((AbstractTypeDeclaration) declaration);
			}

			childObjectsMap.put(declarationName, declarationObject);
		}
		return childObjectsMap;
	}

	@SuppressWarnings("unchecked")
	private static MethodDeclarationNode processMethodDeclaration(MethodDeclaration declaration, CompilationUnit compilationUnit) {
		MethodDeclarationNode methodNode = new MethodDeclarationNode();
		methodNode.setName(declaration.getName().toString());
		methodNode.setConstructor(declaration.isConstructor());
		methodNode.setLineNum(compilationUnit.getLineNumber(declaration.getStartPosition()));
		methodNode.setModifiers(extractModifiers(declaration.modifiers()));
		methodNode.setParameters(extractMethodParameters(declaration.parameters()));
		methodNode.setReturnType(declaration.getReturnType2().toString());
		methodNode.setCompleteNodeValue(declaration.toString());
		methodNode.setThrownExceptions(extractThrownExceptions(declaration.thrownExceptions()));

		if (declaration.getBody() != null) {
			MethodStatementConstructorUtil methodStmtNode = MethodStatementConstructorUtil.getInstance();
			methodStmtNode.processMethodStatements(declaration.getBody().statements(), methodNode.getBlockofStatements(), compilationUnit);
		}
		return methodNode;
	}

	private static List<String> extractThrownExceptions(List<Name> thrownExceptions) {
		List<String> throwsList = new ArrayList<String>();
		for (Name thrownException : thrownExceptions)
			throwsList.add(thrownException.toString());
		return throwsList;
	}

	private static String extractMethodParameters(List<SingleVariableDeclaration> parameters) {
		String finalParameter = "";
		for (SingleVariableDeclaration parameter : parameters) {
			finalParameter += (finalParameter.equals("") ? "" : ", ") + parameter;
		}
		return finalParameter;
	}

	@SuppressWarnings("unchecked")
	private static FieldDeclarationNode processFieldDeclaration(FieldDeclaration declaration, CompilationUnit compilationUnit) {
		FieldDeclarationNode fieldNode = new FieldDeclarationNode();
		fieldNode.setType(declaration.getType().toString());
		fieldNode.setLineNum(compilationUnit.getLineNumber(declaration.getStartPosition()));
		fieldNode.setModifiers(extractModifiers(declaration.modifiers()));
		fieldNode.setCompleteNodeValue(declaration.toString());
		setFragmentDetails((VariableDeclarationFragment) declaration.fragments().get(0), fieldNode);
		return fieldNode;
	}

	static void setFragmentDetails(VariableDeclarationFragment fragment, FieldDeclarationNode fieldNode) {
		fieldNode.setName(fragment.getName().toString());
		if (fragment.getInitializer() != null)
			fieldNode.setValue(fragment.getInitializer().toString());
		else
			fieldNode.setValue("");
	}

	@SuppressWarnings("unchecked")
	private static ClassTypeDeclarationNode processClassType(TypeDeclaration abstractDeclaration, CompilationUnit compilationUnit) {
		ClassTypeDeclarationNode declarationNode = new ClassTypeDeclarationNode();

		declarationNode.setSuperInterfacelist(extractSuperInterfaces(abstractDeclaration.superInterfaceTypes()));

		declarationNode.setInterface(abstractDeclaration.isInterface());
		if (abstractDeclaration.getSuperclassType() != null)
			declarationNode.setSuperClass(abstractDeclaration.getSuperclassType().toString());
		else
			declarationNode.setSuperClass("");

		return declarationNode;
	}

	@SuppressWarnings("unchecked")
	private static EnumTypeDeclarationNode processEnumType(EnumDeclaration abstractDeclaration, CompilationUnit compilationUnit) {
		EnumTypeDeclarationNode declarationNode = new EnumTypeDeclarationNode();

		declarationNode.setSuperInterfacelist(extractSuperInterfaces(abstractDeclaration.superInterfaceTypes()));

		declarationNode.setEnumConstants(extractEnumConstants(abstractDeclaration.enumConstants()));

		return declarationNode;
	}

	static List<String> extractModifiers(List<Object> modifiers) {
		List<String> nodeModifiers = new ArrayList<String>();

		for (Object modifier : modifiers) {
			nodeModifiers.add(modifier.toString());
		}

		return nodeModifiers;
	}

	private static List<String> extractSuperInterfaces(List<org.eclipse.jdt.core.dom.Type> superInterfaceTypes) {
		List<String> nodeSuperInterfaces = new ArrayList<String>();
		for (org.eclipse.jdt.core.dom.Type type : superInterfaceTypes)
			nodeSuperInterfaces.add(type.toString());
		return nodeSuperInterfaces;
	}

	private static List<String> extractEnumConstants(List<EnumConstantDeclaration> enumConstants) {
		List<String> enumConstantsList = new ArrayList<String>();
		for (EnumConstantDeclaration enumConstant : enumConstants)
			enumConstantsList.add(enumConstant.toString());
		return enumConstantsList;
	}

	public static boolean areAbstractTypeNodesSame(AbstractTypeDeclarationNode expectedNode, AbstractTypeDeclarationNode actualNode,
			List<Type> subCommonChilds) {
		List<String> actualModifiers = actualNode.getModifiers();
		List<String> expectedModifiers = expectedNode.getModifiers();
		if (!(expectedModifiers.size() == actualModifiers.size()) || !expectedModifiers.containsAll(actualModifiers))
			return false;

		List<String> actualSuperInterfaces = actualNode.getSuperInterfacelist();
		List<String> expectedSuperInterfaces = expectedNode.getSuperInterfacelist();
		if (!(expectedSuperInterfaces.size() == actualSuperInterfaces.size())
				|| !expectedSuperInterfaces.containsAll(actualSuperInterfaces))
			return false;

		for (Type type : subCommonChilds)
			if (type.getDiff() != ComparatorUtil.NOMODIFICATION)
				return false;

		if (expectedNode.getClass() == ClassTypeDeclarationNode.class) {
			String actualSuperClass = ((ClassTypeDeclarationNode) actualNode).getSuperClass();
			String expectedSuperClass = ((ClassTypeDeclarationNode) expectedNode).getSuperClass();
			if (!actualSuperClass.equals(expectedSuperClass))
				return false;
		} else {
			List<String> actualEnumConstants = ((EnumTypeDeclarationNode) actualNode).getEnumConstants();
			List<String> expectedEnumConstants = ((EnumTypeDeclarationNode) expectedNode).getEnumConstants();
			if (!(actualEnumConstants.size() == expectedEnumConstants.size()) || !actualEnumConstants.containsAll(expectedEnumConstants))
				return false;
		}

		// Body Declaration checks
		return true;
	}

	static boolean areChildNodesSame(Object actual, Object expected, List<Type> subCommonChilds) {
		if (actual instanceof AbstractTypeDeclarationNode)
			return areAbstractTypeNodesSame((AbstractTypeDeclarationNode) expected, (AbstractTypeDeclarationNode) actual, subCommonChilds);
		else if (actual instanceof FieldDeclarationNode)
			return areFieldTypeNodesSame((FieldDeclarationNode) actual, (FieldDeclarationNode) expected);
		else if (actual instanceof MethodDeclarationNode)
			return areMethodTypeNodesSame((MethodDeclarationNode) actual, (MethodDeclarationNode) expected, subCommonChilds);

		return false;
	}

	private static boolean areMethodTypeNodesSame(MethodDeclarationNode actual, MethodDeclarationNode expected, List<Type> subCommonChilds) {
		if (!actual.getParameters().equals(expected.getParameters()))
			return false;
		if (!actual.getReturnType().equals(expected.getReturnType()))
			return false;

		List<String> actualModifiers = actual.getModifiers();
		List<String> expectedModifiers = expected.getModifiers();
		if (!(expectedModifiers.size() == actualModifiers.size()) || !expectedModifiers.containsAll(actualModifiers))
			return false;

		List<String> actualThrownExceptions = actual.getThrownExceptions();
		List<String> expectedThrownExceptions = expected.getThrownExceptions();
		if (!(actualThrownExceptions.size() == expectedThrownExceptions.size())
				|| !actualThrownExceptions.containsAll(expectedThrownExceptions))
			return false;

		for (Type type : subCommonChilds)
			if (type.getDiff() != ComparatorUtil.NOMODIFICATION)
				return false;

		return true;
	}

	private static boolean areFieldTypeNodesSame(FieldDeclarationNode actual, FieldDeclarationNode expected) {
		if (!actual.getType().equals(expected.getType()))
			return false;

		List<String> actualModifiers = actual.getModifiers();
		List<String> expectedModifiers = expected.getModifiers();
		if (!(expectedModifiers.size() == actualModifiers.size()) || !expectedModifiers.containsAll(actualModifiers))
			return false;

		if (!actual.getValue().equals(expected.getValue()))
			return false;

		return true;
	}

	public static void compareChildNodes(Object expectedDeclarationNode, Type type) {

		if (expectedDeclarationNode.getClass() == MethodDeclarationNode.class) {
			MethodStatementConstructorUtil methodStmtNode = MethodStatementConstructorUtil.getInstance();
			methodStmtNode.compareMethodNode((MethodDeclarationNode) expectedDeclarationNode, type);
			return;
		}

		Object actualTypeDeclarationNode = type.getAbstractDeclarations(0);
		List<Type> commonChilds = type.getCommonChilds();

		Map<String, Object> actualChilds = ((DeclarationNode) actualTypeDeclarationNode).getChildDeclarations();
		Map<String, Object> expectedChilds = ((DeclarationNode) expectedDeclarationNode).getChildDeclarations();

		Set<String> actualKeySet = new HashSet<String>(actualChilds.keySet());
		for (String actualKey : actualKeySet) {
			Type childType = new Type();
			Object actualChildObject = actualChilds.remove(actualKey);
			childType.setAbstractDeclarations((DeclarationNode) actualChildObject, 0);
			if (expectedChilds.containsKey(actualKey)) {
				Object expectedChildObject = expectedChilds.remove(actualKey);
				List<Type> subCommonChilds = childType.getCommonChilds();
				compareChildNodes(expectedChildObject, childType);
				boolean flag = areChildNodesSame(actualChildObject, expectedChildObject, subCommonChilds);
				if (flag)
					childType.setDiff(ComparatorUtil.NOMODIFICATION);
				childType.setAbstractDeclarations((DeclarationNode) expectedChildObject, 1);
			} else {
				childType.setDiff(ComparatorUtil.ACTUALDEFAULT);
				childType.getCommonChilds().addAll(
						processDifference2ChildNodes((DeclarationNode) actualChildObject, ComparatorUtil.ACTUALDEFAULT));
			}
			commonChilds.add(childType);
		}
		Set<String> expectedLeftOverKeySet = new HashSet<String>(expectedChilds.keySet());
		for (String expectedKey : expectedLeftOverKeySet) {
			Type childType = new Type();
			Object expectedChildObject = expectedChilds.remove(expectedKey);
			childType.setDiff(ComparatorUtil.EXPECTEDDEFAULT);
			childType.setAbstractDeclarations((DeclarationNode) expectedChildObject, 1);
			childType.getCommonChilds().addAll(
					processDifference2ChildNodes((DeclarationNode) expectedChildObject, ComparatorUtil.EXPECTEDDEFAULT));
			commonChilds.add(childType);
		}

		// return commonChilds;

	}

	static List<Type> processDifference2ChildNodes(DeclarationNode declarationObject, short diff) {
		List<Type> commonChilds = new ArrayList<Type>();
		Collection<Object> childs = null;
		if (declarationObject instanceof MethodDeclarationNode)
			childs = ((MethodDeclarationNode) declarationObject).getBlockofStatements();
		else
			childs = declarationObject.getChildDeclarations().values();
		for (Object child : childs) {
			Type childType = new Type();
			childType.setDiff(diff);
			if (diff == ComparatorUtil.ACTUALDEFAULT)
				childType.setAbstractDeclarations((DeclarationNode) child, 0);
			else
				childType.setAbstractDeclarations((DeclarationNode) child, 1);
			childType.getCommonChilds().addAll(processDifference2ChildNodes((DeclarationNode) child, diff));
			commonChilds.add(childType);
		}

		return commonChilds;
	}

}
