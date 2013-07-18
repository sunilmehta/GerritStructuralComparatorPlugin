package com.imaginea.javaStructuralComparator.domain;

import java.sql.Timestamp;


public class DraftMessage {

	private String message;
	private int side;
	private int line;
	private Timestamp writtenOn;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getSide() {
		return side;
	}
	public void setSide(int side) {
		this.side = side;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public Timestamp getWrittenOn() {
		return writtenOn;
	}
	public void setWrittenOn(Timestamp writtenOn) {
		this.writtenOn = writtenOn;
	}
	
}
