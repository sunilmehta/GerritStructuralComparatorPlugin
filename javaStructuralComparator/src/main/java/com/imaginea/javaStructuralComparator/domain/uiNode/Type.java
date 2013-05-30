package com.imaginea.javaStructuralComparator.domain.uiNode;

import com.imaginea.javaStructuralComparator.domain.node.DeclarationNode;
import com.imaginea.javaStructuralComparator.repo.ComparatorUtil;

public class Type {

	com.imaginea.javaStructuralComparator.domain.Type type;

	public Type(com.imaginea.javaStructuralComparator.domain.Type type) {
		this.type = type;
	}

	public com.imaginea.javaStructuralComparator.domain.Type getType() {
		return type;
	}

	public void setType(com.imaginea.javaStructuralComparator.domain.Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		String obj = null;
		System.out.println();
		if (type.getDiff() != ComparatorUtil.EXPECTEDDEFAULT) {
			if (this.type.getAbstractDeclarations(0).getName() == null)
				obj = "Line";
			else
				obj = ((DeclarationNode) this.type.getAbstractDeclarations(0)).getName();
		} else {
			if (this.type.getAbstractDeclarations(1).getName() == null)
				obj = "Line";
			else
				obj = ((DeclarationNode) this.type.getAbstractDeclarations(1)).getName();
		}
		return obj;
	}
}
