package com.imaginea.javaStructuralComparator.domain;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.javaStructuralComparator.domain.node.DeclarationNode;
import com.imaginea.javaStructuralComparator.repo.ComparatorUtil;

public class Type {
	private short diff = ComparatorUtil.MODIFIEDDEFAULT;
	private DeclarationNode[] declarations = new DeclarationNode[2];
	private List<Type> commonChilds = new ArrayList<Type>();

	public short getDiff() {
		return diff;
	}

	public void setDiff(short diff) {
		this.diff = diff;
	}

	public DeclarationNode getAbstractDeclarations(int objectNum) {
		return declarations[objectNum];
	}

	public void setAbstractDeclarations(DeclarationNode declaration, int objectNum) {
		this.declarations[objectNum] = declaration;
	}

	public List<Type> getCommonChilds() {
		return commonChilds;
	}

	@Override
	public String toString() {
		return "Type [AreTypesSame=" + diff + ", ActualNode=" + declarations[0] + ", " + "ExpectedNode=" + declarations[1] + ", "
				+ "ChildNodes=" + commonChilds + "]";
	}

	public DeclarationNode[] getDeclarations() {
		return declarations;
	}

	public void setDeclarations(DeclarationNode[] declarations) {
		this.declarations = declarations;
	}

	public void setCommonChilds(List<Type> commonChilds) {
		this.commonChilds = commonChilds;
	}

}
