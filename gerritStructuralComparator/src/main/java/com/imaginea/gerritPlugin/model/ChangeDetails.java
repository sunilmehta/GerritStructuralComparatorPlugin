package com.imaginea.gerritPlugin.model;

import java.util.List;

public class ChangeDetails {
	
	List<ChangeID> changeIDs;
	
	Change change;

	public List<ChangeID> getChangeIDs() {
		return changeIDs;
	}

	public void setChangeIDs(List<ChangeID> changeIDs) {
		this.changeIDs = changeIDs;
	}

	public Change getChange() {
		return change;
	}

	public void setChange(Change change) {
		this.change = change;
	}
	
}
