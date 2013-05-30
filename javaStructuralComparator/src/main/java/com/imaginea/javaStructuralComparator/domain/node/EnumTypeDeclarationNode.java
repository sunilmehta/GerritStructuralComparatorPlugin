package com.imaginea.javaStructuralComparator.domain.node;

import java.util.List;

public class EnumTypeDeclarationNode extends AbstractTypeDeclarationNode {

	private List<String> enumConstants;

	public List<String> getEnumConstants() {
		return enumConstants;
	}

	public void setEnumConstants(List<String> enumConstants) {
		this.enumConstants = enumConstants;
	}

	@Override
	public String toString() {
		return "EnumType [EnumName=" + getName() + ", Modifiers=" + getModifiers() + ", " + "SuperInterfaces=" + getSuperInterfacelist()
				+ ", " + "EnmuConstants=" + getEnumConstants() + "]";
	}

}
