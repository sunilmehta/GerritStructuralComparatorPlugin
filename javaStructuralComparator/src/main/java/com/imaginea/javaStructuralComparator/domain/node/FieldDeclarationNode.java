package com.imaginea.javaStructuralComparator.domain.node;

public class FieldDeclarationNode extends DeclarationNode {

	private String type;
	private String value;

	// private Frag

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		if (obj instanceof FieldDeclarationNode) {
			FieldDeclarationNode node = (FieldDeclarationNode) obj;
			retVal = node.getName().equals(this.getName());
		}
		return retVal;
	}

	@Override
	public String toString() {
		return "FieldType [FieldName=" + getName() + ", Type=" + getType() + ", Modifiers=" + getModifiers() + ", " + "SuperInterfaces="
				+ getValue() + "]";
	}

}
