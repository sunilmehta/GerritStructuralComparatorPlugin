package com.imaginea.gerritPlugin.model;

import java.util.ArrayList;
import java.util.List;

public class PatchSet {
	private List<Patch> patchList = new ArrayList<Patch>();
	private int patchSetId;
	private int changeId;

	public List<Patch> getPatchList() {
		return patchList;
	}

	public void setPatchList(List<Patch> patchList) {
		this.patchList = patchList;
	}

	public int getPatchSetId() {
		return patchSetId;
	}

	public void setPatchSetId(int patchSetId) {
		this.patchSetId = patchSetId;
	}

	public int getChangeId() {
		return changeId;
	}

	public void setChangeId(int changeId) {
		this.changeId = changeId;
	}

}
