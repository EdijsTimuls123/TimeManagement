package lv.lss.time.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
	private Integer id;
	private String username;
	private String password;
	private String newPassword;
	private List<EventDTO> events = new ArrayList<>();

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<EventDTO> getEvents() {
		return events;
	}
	public void setEvents(List<EventDTO> events) {
		this.events = events;
	}
}
