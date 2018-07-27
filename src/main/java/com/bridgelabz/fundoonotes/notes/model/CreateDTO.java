package com.bridgelabz.fundoonotes.notes.model;

import java.util.List;

public class CreateDTO {

	private String title;
	private String body;
	private String color;
	private DateDto reminder;
	private List<String> label;
	
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
