package com.imaginea.javaStructuralComparator.domain.node;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractTypeDeclarationNode extends DeclarationNode {
	private List<String> superInterfacelist = new ArrayList<String>();
	private List<Object> bodyDeclarations = new ArrayList<Object>();

	public List<String> getSuperInterfacelist() {
		return superInterfacelist;
	}

	public void setSuperInterfacelist(List<String> superInterfacelist) {
		this.superInterfacelist = superInterfacelist;
	}

	public List<Object> getBodyDeclarations() {
		return bodyDeclarations;
	}

	public void setBodyDeclarations(List<Object> bodyDeclarations) {
		this.bodyDeclarations = bodyDeclarations;
	}

	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		if (obj instanceof AbstractTypeDeclarationNode) {
			AbstractTypeDeclarationNode node = (AbstractTypeDeclarationNode) obj;
			retVal = node.getName().equals(this.getName());
		}
		return retVal;
	}

}
