package com.imaginea.javaStructuralComparator.domain.node;

public class ImportNode extends Line {
	private boolean isStatic;

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	@Override
	public String toString() {
		return "ImportNode [ImportNodeName=" + getValue() + ", LineNum=" + getLineNum() + ", isStatic=" + isStatic() + "]";
	}
}
