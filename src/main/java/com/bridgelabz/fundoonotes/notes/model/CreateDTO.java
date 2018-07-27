package com.bridgelabz.fundoonotes.notes.model;

import java.util.List;

public class CreateDTO {

	private String title;
	private String body;
	private String color;
	private DateDto reminder;
	private List<String> label;
	private boolean pin;
	private boolean archive;
	
	public DateDto getReminder() {
		return reminder;
	}
	public void setReminder(DateDto reminder) {
		this.reminder = reminder;
	}
	public List<String> getLabel() {
		return label;
	}
	public void setLabel(List<String> label) {
		this.label = label;
	}
	public boolean isPin() {
		return pin;
	}
	public void setPin(boolean pin) {
		this.pin = pin;
	}
	public boolean isArchive() {
		return archive;
	}
	public void setArchive(boolean archive) {
		this.archive = archive;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
