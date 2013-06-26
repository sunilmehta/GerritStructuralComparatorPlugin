package com.imaginea.gerritPlugin.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Change {
	
	private int changeID;
	private String changeKey;
	private String subject;
	private String projectName;
	private Timestamp creationDate;
	private Timestamp lastUpdationDate;
	private String branch;
	private String owner;
	private String changeStatus;
	
	private List<PatchSet> patchSets = new ArrayList<PatchSet>();
	
	public Change(){}
	
	public Change(com.google.gerrit.reviewdb.client.Change change){
		this.branch = change.getDest().getShortName();
		this.changeID = change.getChangeId();
		this.changeKey = change.getKey().get();
		this.creationDate = change.getCreatedOn();
		this.lastUpdationDate = change.getLastUpdatedOn();
		this.projectName = change.getProject().get();
		this.subject = change.getSubject();
		this.changeStatus = change.getStatus().name();
	}

	public int getChangeID() {
		return changeID;
	}

	public void setChangeID(int changeID) {
		this.changeID = changeID;
	}

	public String getChangeKey() {
		return changeKey;
	}

	public void setChangeKey(String changeKey) {
		this.changeKey = changeKey;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getLastUpdationDate() {
		return lastUpdationDate;
	}

	public void setLastUpdationDate(Timestamp lastUpdationDate) {
		this.lastUpdationDate = lastUpdationDate;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	public List<PatchSet> getPatchSets() {
		return new ArrayList<PatchSet>(patchSets);
	}

	public void setPatchSets(List<PatchSet> patchSets) {
		this.patchSets.addAll(patchSets);
	}

}
