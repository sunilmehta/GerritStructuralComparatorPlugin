package com.imaginea.javaStructuralComparator.repo;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.imaginea.javaStructuralComparator.domain.ComparisonResult;
import com.imaginea.javaStructuralComparator.domain.Import;
import com.imaginea.javaStructuralComparator.domain.Package;
import com.imaginea.javaStructuralComparator.domain.Type;
import com.imaginea.javaStructuralComparator.domain.node.Line;


public class ComparatorImpl implements Comparator {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(ComparatorImpl.class);

	ComparisonResult result = new ComparisonResult();
	
	public ComparatorImpl() {
	}

	public ComparisonResult compare(String actualFile, String expectedFile) {
		log.debug("File comparison is going on...");
		CompilationUnit actualCompilationUnit = null, expectedCompilationUnit = null;
		actualCompilationUnit = JavaParser.parse(actualFile.toCharArray());
		expectedCompilationUnit = JavaParser.parse(expectedFile.toCharArray());
		processPackage(actualCompilationUnit, expectedCompilationUnit);
		processImports(actualCompilationUnit, expectedCompilationUnit);
		processTypes(actualCompilationUnit, expectedCompilationUnit);
		
		return result;
	}

	private void processPackage(CompilationUnit actualCompilationUnit, CompilationUnit expectedCompilationUnit) {
		Package pkg = new Package();
		Line actualLine = ComparatorUtil.processPackage(actualCompilationUnit);
		Line expectedLine = ComparatorUtil.processPackage(expectedCompilationUnit);
		pkg.setLines(actualLine, 0);
		pkg.setLines(expectedLine, 1);

		if (isLineNull(actualLine) && isLineNull(expectedLine))
			pkg.setDiff(ComparatorUtil.NOMODIFICATION);
		else if (isLineNull(actualLine))
			pkg.setDiff(ComparatorUtil.EXPECTEDDEFAULT);
		else if (isLineNull(expectedLine))
			pkg.setDiff(ComparatorUtil.ACTUALDEFAULT);
		else if (actualLine.getValue().equals(expectedLine.getValue()))
			pkg.setDiff(ComparatorUtil.NOMODIFICATION);

		result.setPkg(pkg);
	}

	private boolean isLineNull(Line line) {
		if (line == null || line.getValue() == null)
			return true;
		return false;
	}

	private void processImports(CompilationUnit actualCompilationUnit, CompilationUnit expectedCompilationUnit) {
		Map<String, Import> bufferImports = ComparatorUtil.getBufferImports(actualCompilationUnit);
		List<Import> imports =  ComparatorUtil.compareWithBufferedimports(bufferImports, expectedCompilationUnit);
		
		result.setImports(imports);
	}
	


	private void processTypes(CompilationUnit actualCompilationUnit, CompilationUnit expectedCompilationUnit) {
		Map<String, Type> bufferTypes = ComparatorUtil.getBufferTypes(actualCompilationUnit);
		List<Type> types = ComparatorUtil.compareWithBufferedTypes(bufferTypes, expectedCompilationUnit);
		result.setTypes(types);
		
	}
}