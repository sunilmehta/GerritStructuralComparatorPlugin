package com.imaginea.javaStructuralComparator.domain.node;

public class LineNode extends DeclarationNode {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		setCompleteNodeValue(value);
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		if (obj instanceof LineNode) {
			LineNode node = (LineNode) obj;
			retVal = node.getValue().equals(this.getValue());
		}
		return retVal;
	}
	

	
	@Override
	public String toString() {
		return "LineNode [Name=" + getValue() + ", LineNum=" + getLineNum() + "]";
	}

}
