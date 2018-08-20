package lv.lss.time.core;

import javax.ejb.Remote;

import lv.lss.time.jpa.User;

@Remote
public interface TimeCoreServiceInterface {
	public void saveUser(User u);
	public User getUserByName(String name);
	public User loginUser(String name, String password);
	
	

}
