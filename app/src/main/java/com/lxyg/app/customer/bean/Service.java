package com.lxyg.app.customer.bean;

import java.util.List;

public class Service {
	private String startTime;
	private String currentTime;
	private String endTime;
	private String date;
	private List<ServiceTime> times;
	
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<ServiceTime> getTimes() {
		return times;
	}
	public void setTimes(List<ServiceTime> times) {
		this.times = times;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
}
