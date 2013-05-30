package com.imaginea.javaStructuralComparator.domain.node;

public class ClassTypeDeclarationNode extends AbstractTypeDeclarationNode {

	private boolean isInterface;
	private String superClass;

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public String getSuperClass() {
		return superClass;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	@Override
	public String toString() {
		String name = isInterface() ? "InterfaceName" : "ClassName";
		return "ClassType [" + name + "=" + getName() + ", Modifiers=" + getModifiers() + ", " + "SuperInterfaces="
				+ getSuperInterfacelist() + ", " + "SuperClass=" + getSuperClass() + "]";
	}

}
