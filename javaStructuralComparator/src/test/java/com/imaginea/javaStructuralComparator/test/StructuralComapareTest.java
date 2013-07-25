package com.imaginea.javaStructuralComparator.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imaginea.javaStructuralComparator.domain.ComparisonResult;
import com.imaginea.javaStructuralComparator.domain.Import;
import com.imaginea.javaStructuralComparator.domain.Package;
import com.imaginea.javaStructuralComparator.domain.Type;
import com.imaginea.javaStructuralComparator.domain.node.DeclarationNode;
import com.imaginea.javaStructuralComparator.domain.node.EnumTypeDeclarationNode;
import com.imaginea.javaStructuralComparator.repo.ComparatorImpl;

interface abc {

}

abstract class bac {

}

enum en {
	apple, appl;
}

public class StructuralComapareTest extends bac implements abc {

	int value = 0;
	int[] arr = { 1, 2 };

	@Test
	public final void assertPackageEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		com.imaginea.javaStructuralComparator.domain.Package pkg = result.getPkg();
		isPackageDifferent(pkg);

	}

	@Test
	public final void assertImportsEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result =comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		List<Import> imports = result.getImports();
		isImportsDifferent(imports);
	}

	private void isPackageDifferent(Package pkg) {
		if (pkg.getDiff() != 0)
			Assert.fail("Package is different: " + pkg);
	}

	void isImportsDifferent(List<Import> imports) {
		for (Import imp : imports)
			if (imp.getDiff() != 0)
				Assert.fail("Imports are different: " + imp);
	}
	
	private static String loadFile( String filePath ){
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = null;
		String sCurrentLine;
		 
		try {
			br = new BufferedReader(new FileReader( filePath ));
			while ((sCurrentLine = br.readLine()) != null) {
				buffer.append(sCurrentLine+'\n');
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if ( br != null )
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return buffer.toString().trim();
	}

	@Test
	public final void assertTypesEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		List<Type> types = result.getTypes();
		isTypesDifferent(types);
	}

	private void isTypesDifferent(List<Type> types) {
		for (Type type : types)
			if (type.getDiff() != 0)
				Assert.fail("Imports are different: " + type);
	}

	@Test
	public final void assertJavaFileStructuresEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		isPackageDifferent(result.getPkg());
		isImportsDifferent(result.getImports());
		isTypesDifferent(result.getTypes());
	}

	static void testTypes() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		List<Type> types = result.getTypes();
		for (Type type : types) {
			System.out.println(type);
		}
	}

	static void testImports() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		List<Import> imports = result.getImports();
		for (Import imp : imports) {
			System.out.println(imp);
		}
	}

	public static void main(String[] args) {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result12 = comparator.compare(
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"),
				loadFile("src/test/java/com/imaginea/javaStructuralComparator/test/StructuralComapareTest.java"));
		Gson gson = new Gson();
		String json = gson.toJson(result12);
		try {
			writeToFile(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n\n" + json + "\n\n*********************");
		ComparisonResult result1 = gson.fromJson(json, ComparisonResult.class);
		System.out.println("" + result1.getImports().size());
		System.out.println("" + result1.getTypes().get(3).getClass());
		System.out.println("" + result1.getTypes().get(3).getAbstractDeclarations(0).getClass());
		System.out.println("" + result1.getImports().size());
	}

	private static void writeToFile(String json) throws IOException {
		File file = new File("C:/Users/vinod/Desktop/dummy.json");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(json);
		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
	}

	class mnop {
		class bvd {
			void temp() {
			}
		}
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("value", value);
		if (arr != null)
			builder.append("arr", Arrays.toString(arr));
		return builder.toString();
	}

	class mp {
		class bvdes {
			void temp() {
			}
		}
	}

}
