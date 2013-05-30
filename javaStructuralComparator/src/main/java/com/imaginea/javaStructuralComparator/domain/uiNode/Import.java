package com.imaginea.javaStructuralComparator.domain.uiNode;

import com.imaginea.javaStructuralComparator.repo.ComparatorUtil;

public class Import {
	private com.imaginea.javaStructuralComparator.domain.Import imprt;
	
	public Import(com.imaginea.javaStructuralComparator.domain.Import imprt) {
		this.imprt = imprt;
	}

	public com.imaginea.javaStructuralComparator.domain.Import getImprt() {
		return imprt;
	}

	public void setImprt(com.imaginea.javaStructuralComparator.domain.Import imprt) {
		this.imprt = imprt;
	}

	@Override
	public String toString() {
		String obj = null;
		if (this.imprt.getDiff() != ComparatorUtil.EXPECTEDDEFAULT)
			obj = this.imprt.getLine(0).getValue();
		else
			obj = this.imprt.getLine(1).getValue();
		return obj;
	}
}
