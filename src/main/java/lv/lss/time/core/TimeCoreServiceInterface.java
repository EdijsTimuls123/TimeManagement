package lv.lss.time.core;

import javax.ejb.Remote;

import lv.lss.time.jpa.Event;
import lv.lss.time.jpa.User;

@Remote
public interface TimeCoreServiceInterface {
	public void saveUser(User u);
	public User getUserById(Integer id);
	public User getUserByName(String name);
	public User loginUser(String name, String password);
	public void deleteEventById(Integer id);
	public void saveEvent(Event e);
	public Integer createEvent(Event e);
	public boolean validateEvent(Event e);
	public void savePassword(User u);
}
