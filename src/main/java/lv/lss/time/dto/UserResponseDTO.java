package lv.lss.time.dto;

public class UserResponseDTO {
	public String msg;
    public UserDTO user;
    
    public UserResponseDTO() {
    	this(null);
    }

    public UserResponseDTO(UserDTO user) {
    	this.msg = "success";
    	if (user != null) {
        	this.user = new UserDTO();
        	this.user.setId(user.getId());
        	this.user.setUsername(user.getUsername());
        	this.user.setPassword("********");
    	}
    }
    
}
