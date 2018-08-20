package lv.lss.time.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
	private String username;
	private String password;
	private List<EventDTO> events = new ArrayList<>();

	public List<EventDTO> getEvents() {
		return events;
	}
	public void setEvents(List<EventDTO> events) {
		this.events = events;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
