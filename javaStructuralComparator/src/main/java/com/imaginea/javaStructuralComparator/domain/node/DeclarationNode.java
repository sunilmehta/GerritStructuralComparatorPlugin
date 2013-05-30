package com.imaginea.javaStructuralComparator.domain.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeclarationNode {
	private int lineNum;
	private String name;
	private List<String> modifiers;
	private Map<String, Object> childDeclarations = new HashMap<String, Object>();
	
	private String completeNodeValue;

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public Map<String, Object> getChildDeclarations() {
		return childDeclarations;
	}

	public String getCompleteNodeValue() {
		return completeNodeValue;
	}

	public void setCompleteNodeValue(String completeNodeValue) {
		this.completeNodeValue = completeNodeValue;
	}
}
