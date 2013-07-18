package com.imaginea.javaStructuralComparator.domain;

import java.util.List;

public class ComparisonResult {
	private Package pkg;
	private List<Import> imports;
	private List<Type> types;
	List<DraftMessage> draftMessage;

	public Package getPkg() {
		return pkg;
	}

	public void setPkg(Package pkg) {
		this.pkg = pkg;
	}

	public List<Import> getImports() {
		return imports;
	}

	public void setImports(List<Import> imports) {
		this.imports = imports;
	}

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public List<DraftMessage> getDraftMessage() {
		return draftMessage;
	}

	public void setDraftMessage(List<DraftMessage> draftMessage) {
		this.draftMessage = draftMessage;
	}

	@Override
	public String toString() {
		return "ComparisonResult [package=" + getPkg() + ", Imports=" + getImports() + ", " + "Types=" + getTypes() + ", DraftMessage="+getDraftMessage() + "]";
	}

}
