package com.imaginea.javaStructuralComparator.domain.node;

public class Line {
	private int lineNum;
	private String value;
	private String color;

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		if (obj instanceof Line) {
			Line node = (Line) obj;
			retVal = node.getValue().equals(this.getValue());
		}
		return retVal;
	}
	
	@Override
	public String toString() {
		return "Line [Name=" + getValue() + ", LineNum=" + getLineNum() + "]";
	}
}
