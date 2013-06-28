package com.imaginea.gerritPlugin.model;

public class Patch {
	private String patchFileName;
	private String patchParentKey;
	private String changeType;
	private String patchType;
	private String fileURL;
	
	public Patch(){
		
	}
	
	public Patch( com.google.gerrit.reviewdb.client.Patch patch, String fileURL){
		this.patchFileName = patch.getFileName();
		this.patchParentKey = patch.getKey().getParentKey().toString();
		this.changeType = patch.getChangeType().name();
		this.patchType = patch.getPatchType().name();
		this.fileURL = fileURL;
	}

	public String getPatchFileName() {
		return patchFileName;
	}

	public void setPatchFileName(String patchFileName) {
		this.patchFileName = patchFileName;
	}

	public String getPatchParentKey() {
		return patchParentKey;
	}

	public void setPatchParentKey(String patchParentKey) {
		this.patchParentKey = patchParentKey;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getPatchType() {
		return patchType;
	}

	public void setPatchType(String patchType) {
		this.patchType = patchType;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	
}
