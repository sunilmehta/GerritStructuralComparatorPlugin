package com.imaginea.javaStructuralComparator.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.imaginea.javaStructuralComparator.domain.Import;
import com.imaginea.javaStructuralComparator.domain.Type;
import com.imaginea.javaStructuralComparator.domain.node.AbstractTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.ClassTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.DeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.ImportNode;
import com.imaginea.javaStructuralComparator.domain.node.Line;

public class ComparatorUtil {

	final static public short ACTUALDEFAULT = 1;
	final static public short EXPECTEDDEFAULT = -1;
	final static public short NOMODIFICATION = 0;
	final static public short MODIFIEDDEFAULT = 10;

	public static Line processPackage(CompilationUnit compilationUnit) {
		Line line = new Line();
		PackageDeclaration packageDeclaration = compilationUnit.getPackage();

		line.setLineNum(compilationUnit.getLineNumber(packageDeclaration.getStartPosition()));
		if( null != packageDeclaration ){
			line.setValue(packageDeclaration.toString());
		}

		return line;
	}


	@SuppressWarnings("unchecked")
	public static Map<String, Import> getBufferImports(CompilationUnit actualCompilationUnit) {
		Map<String, Import> bufferImports = new HashMap<String, Import>();
		List<ImportDeclaration> importDeclarations = actualCompilationUnit.imports();
		for (ImportDeclaration importDeclaration : importDeclarations) {
			Import imprt = new Import();
			String importName = importDeclaration.getName().toString();

			ImportNode importNode = new ImportNode();
			importNode.setValue(importName);
			importNode.setLineNum(actualCompilationUnit.getLineNumber(importDeclaration.getStartPosition()));
			importNode.setStatic(importDeclaration.isStatic());

			imprt.setLines(importNode, 0);

			bufferImports.put(importName, imprt);
		}

		return bufferImports;
	}

	@SuppressWarnings("unchecked")
	public static List<Import> compareWithBufferedimports(Map<String, Import> bufferImports, CompilationUnit expectedCompilationUnit) {

		List<Import> finalImports = new ArrayList<Import>();

		List<ImportDeclaration> importDeclarations = expectedCompilationUnit.imports();
		for (ImportDeclaration importDeclaration : importDeclarations) {
			String importName = importDeclaration.getName().toString();

			ImportNode importNode = new ImportNode();
			importNode.setValue(importName);
			importNode.setLineNum(expectedCompilationUnit.getLineNumber(importDeclaration.getStartPosition()));
			importNode.setStatic(importDeclaration.isStatic());

			Import imprt = null;
			if (bufferImports.containsKey(importName)) {
				imprt = bufferImports.remove(importName);
				if (importNode.isStatic() == imprt.getLine(0).isStatic())
					imprt.setDiff(NOMODIFICATION);
			} else {
				imprt = new Import();
				imprt.setDiff(EXPECTEDDEFAULT);
			}

			imprt.setLines(importNode, 1);
			finalImports.add(imprt);
		}
		List<Import> leftOuts = new ArrayList<Import>(bufferImports.values());
		for (Import leftOutImports : leftOuts) {
			leftOutImports.setDiff(ACTUALDEFAULT);
			finalImports.add(leftOutImports);
		}

		return finalImports;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Type> getBufferTypes(CompilationUnit actualCompilationUnit) {
		Map<String, Type> bufferTypes = new HashMap<String, Type>();
		List<AbstractTypeDeclaration> abstractDeclarations = actualCompilationUnit.types();

		for (AbstractTypeDeclaration abstractDeclaration : abstractDeclarations) {
			Type type = new Type();
			String typeName = addSuffix2TypeName(abstractDeclaration);
			
			DeclarationNode abstractTypeDeclarationNode = DeclarationConstructorUtil.processType(abstractDeclaration,
					actualCompilationUnit);
			type.setAbstractDeclarations(abstractTypeDeclarationNode, 0);
			bufferTypes.put(typeName, type);
		}

		return bufferTypes;
	}

	static String addSuffix2TypeName(AbstractTypeDeclaration abstractDeclaration) {
		String typeName =  abstractDeclaration.getName().toString();
		if (abstractDeclaration.getClass() == TypeDeclaration.class){
			if(((TypeDeclaration)abstractDeclaration).isInterface())
				typeName += "_Interface";
			else
				typeName += "_Class";
		} else
			typeName += "_Enum";
		return typeName;
	}

	static String addSuffix2TypeName(AbstractTypeDeclarationNode abstractDeclaration) {
		String typeName =  abstractDeclaration.getName();
		if (abstractDeclaration.getClass() == ClassTypeDeclarationNode.class){
			if(((ClassTypeDeclarationNode)abstractDeclaration).isInterface())
				typeName += "_Interface";
			else
				typeName += "_Class";
		} else
			typeName += "_Enum";
		return typeName;
	}

	@SuppressWarnings("unchecked")
	public static List<Type> compareWithBufferedTypes(Map<String, Type> bufferTypes, CompilationUnit expectedCompilationUnit) {
		List<Type> finalTypes = new ArrayList<Type>();

		List<AbstractTypeDeclaration> typeDeclarations = expectedCompilationUnit.types();
		for (AbstractTypeDeclaration typeDeclaration : typeDeclarations) {
			String typeName = addSuffix2TypeName(typeDeclaration);

			AbstractTypeDeclarationNode expectedTypeDeclarationNode = DeclarationConstructorUtil.processType(typeDeclaration,
					expectedCompilationUnit);

			Type type = null;
			if (bufferTypes.containsKey(typeName)) {
				type = bufferTypes.remove(typeName);
				DeclarationConstructorUtil.compareChildNodes(expectedTypeDeclarationNode, type);
				boolean flag = DeclarationConstructorUtil.areAbstractTypeNodesSame(expectedTypeDeclarationNode,
						(AbstractTypeDeclarationNode) type.getAbstractDeclarations(0), type.getCommonChilds());
				if (flag) {
					type.setDiff(NOMODIFICATION);
				}
			} else {
				type = new Type();
				type.setDiff(EXPECTEDDEFAULT);
				type.getCommonChilds().addAll(DeclarationConstructorUtil.processDifference2ChildNodes(expectedTypeDeclarationNode, EXPECTEDDEFAULT));
			}

			type.setAbstractDeclarations(expectedTypeDeclarationNode, 1);
			finalTypes.add(type);
		}

		List<Type> leftOuts = new ArrayList<Type>(bufferTypes.values());
		for (Type leftOutType : leftOuts) {
			leftOutType.setDiff(ACTUALDEFAULT);
			leftOutType.getCommonChilds().addAll(DeclarationConstructorUtil.processDifference2ChildNodes(leftOutType.getAbstractDeclarations(0), ACTUALDEFAULT));
			finalTypes.add(leftOutType);
		}

		return finalTypes;
	}
}