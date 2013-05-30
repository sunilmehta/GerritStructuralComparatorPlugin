package com.imaginea.javaStructuralComparator.repo;

import com.imaginea.javaStructuralComparator.domain.ComparisonResult;;

public interface Comparator {
	ComparisonResult compare(String actualFile, String expectedFile);
}
