package lv.lss.time.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lv.lss.time.core.TimeCoreServiceInterface;
import lv.lss.time.dto.EventDTO;
import lv.lss.time.dto.UserDTO;
import lv.lss.time.dto.UserResponseDTO;
import lv.lss.time.jpa.Event;
import lv.lss.time.jpa.User;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	
	private TimeCoreServiceInterface coreService;
	
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public void setCoreService(TimeCoreServiceInterface coreService) {
		this.coreService = coreService;
	}
	
	private UserDTO convertToDto(User user) {
		// User          -> UserDTO
		//   List(Event) ->   List(EventDTO)
		UserDTO userDto = new UserDTO();
		userDto.setId(user.getId());
		userDto.setUsername(user.getName());
		userDto.setPassword("********");
		List<EventDTO> eventsDto = new ArrayList<EventDTO>();
		for(Event source : user.getEvents()) {
			// katrs item (Event source) no List<Event> -> EventDTO
			EventDTO eventDto = new EventDTO();
			eventDto.setId(source.getId());
			eventDto.setUserId(user.getId());
			eventDto.setStart(source.getStartDate().format(formatter));
			eventDto.setEnd(source.getEndDate().format(formatter));
			eventDto.setTitle(source.getInfo());
            // List(EventDTO) pievieno sagatavoto EventDTO
			eventsDto.add(eventDto);
		}
		// UserDTO objektam uzseto sagatavoto List(EventDTO)
		userDto.setEvents(eventsDto);
	    return userDto;
	}
	
	private User convertToEntity(UserDTO userDto) {
    	User u  = new User();
		u.setId(userDto.getId());
		u.setName(userDto.getUsername());
		u.setPassword(userDto.getPassword());
		return u;
    }
	
	@RequestMapping(value="/new", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<UserResponseDTO> registerNewUser(@RequestBody UserDTO userForm) {

		UserResponseDTO result = new UserResponseDTO(userForm);
		
		// Check mandatory parameters
		if (userForm.getUsername().isEmpty() || userForm.getPassword().isEmpty()) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
		
		// Check duplicates
		if (coreService.getUserByName(userForm.getUsername()) != null) {
			result.msg = "exists";
			return ResponseEntity.badRequest().body(result);
		}

		// Save user
		User u = new User();
		u.setName(userForm.getUsername());
		u.setPassword(userForm.getPassword());
		coreService.saveUser(u);

		return ResponseEntity.ok().body(result);
	}
	
	@RequestMapping(value="/session", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<UserResponseDTO> setupSession(HttpSession session) {
		
		logger.info("##### setup session");
		
		UserResponseDTO result = new UserResponseDTO();

		// Check existing session, retrieve user id
		Integer userId = (Integer) session.getAttribute("id");
		if (userId == null) {
			// Respond OK with empty user object
			return ResponseEntity.ok().body(result);
		}
		
		User user = coreService.getUserById(userId);

		// Check if user is found
		if (user == null) {
			result.msg = "notfound";
			return ResponseEntity.badRequest().body(result);
		}

		logger.info(user.getId().toString());
		result.user = convertToDto(user);
		
		return ResponseEntity.ok().body(result);
	}

	@RequestMapping(value="/login", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<UserResponseDTO> verifyLogin(@RequestBody UserDTO userForm, HttpSession session) {
		
		logger.info("##### login");
		
		UserResponseDTO result = new UserResponseDTO(userForm);
		
		// Check mandatory parameters
		if (userForm == null || userForm.getUsername() == null || userForm.getPassword() == null ||
				userForm.getUsername().isEmpty() || userForm.getPassword().isEmpty()) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
				
		User user = coreService.loginUser(userForm.getUsername(), userForm.getPassword());

		// Check if user is found
		if (user == null) {
			result.msg = "notfound";
			return ResponseEntity.badRequest().body(result);
		}

		logger.info(user.getId().toString());
		result.user = convertToDto(user);
		
		// Setup session
		session.setAttribute("id", user.getId());
		session.setAttribute("username", user.getName());
		session.setAttribute("password", user.getPassword());
		
		return ResponseEntity.ok().body(result);
	}
		
	@Scope("session")
	@RequestMapping(value="/updatePassword", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<UserResponseDTO> updatePassword(@RequestBody UserDTO userForm) {

		UserResponseDTO result = new UserResponseDTO(userForm);
		
		// Check mandatory parameters
		if (userForm.getId() == null || userForm.getId() == 0 ||
				userForm.getPassword() == null || userForm.getPassword().isEmpty() ||
				userForm.getNewPassword() == null || userForm.getNewPassword().isEmpty()) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
		
		// Check existing password
		User user = coreService.getUserById(userForm.getId());		
		if (!user.getPassword().equals(userForm.getPassword())) {
			result.msg = "wrong";
			return ResponseEntity.badRequest().body(result);
		}

		// Update password
		userForm.setPassword(userForm.getNewPassword());
		coreService.savePassword(convertToEntity(userForm));

		return ResponseEntity.ok().body(result);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<String> logout(HttpSession session) {

		// Invalidate session
		session.invalidate();
		
		return ResponseEntity.ok().body("{}");
	}
	
}



