package com.bridgelabz.fundoonotes.notes.model;

import java.util.Date;
import java.util.List;

public class ViewNoteDto {

	private String title;
	private String body;
	private Date createdAt;
	private Date lastModified;
	private Date Reminder;
	private String color;
	private boolean isPinned;
	private boolean isArchived;
	private List<ViewLabelDto> labelList;
	
	
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
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public Date getReminder() {
		return Reminder;
	}
	public void setReminder(Date reminder) {
		Reminder = reminder;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isPinned() {
		return isPinned;
	}
	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	public boolean isArchived() {
		return isArchived;
	}
	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}
	public List<ViewLabelDto> getLabelList() {
		return labelList;
	}
	public void setLabelList(List<ViewLabelDto> labelList) {
		this.labelList = labelList;
	}
	
}
