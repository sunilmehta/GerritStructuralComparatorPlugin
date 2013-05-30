package com.imaginea.javaStructuralComparator.domain;

import com.imaginea.javaStructuralComparator.domain.node.Line;
import com.imaginea.javaStructuralComparator.repo.ComparatorUtil;

public class Package {
	private short diff = ComparatorUtil.MODIFIEDDEFAULT;
	private Line[] lines = new Line[2];

	public short getDiff() {
		return diff;
	}

	public void setDiff(short diff) {
		this.diff = diff;
	}

	public Line getLines(int lineNum) {
		return lines[lineNum];
	}

	public void setLines(Line line, int lineNum) {
		this.lines[lineNum] = line;
	}
	
	@Override
	public String toString() {
		return "PackageNode [isPackageSame=" + getDiff() + ", ActualPackage=" + lines[0] + ", " + "ExpectedPackage=" + lines[1] + "]";
	}

	public Line[] getLines() {
		return lines;
	}

	public void setLines(Line[] lines) {
		this.lines = lines;
	}

}
