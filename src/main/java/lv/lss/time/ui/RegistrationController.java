package lv.lss.time.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import lv.progmeistars.lessons.HomeController;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private TimeCoreServiceInterface coreService;
	
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public void setCoreService(TimeCoreServiceInterface coreService) {
		this.coreService = coreService;
	}
	
	private UserDTO convertToDto(User user) {
		// User          -> UserDTO
		//   List(Event) ->   List(EventDTO)
		UserDTO userDto = new UserDTO();
		userDto.setUsername(user.getName());
		userDto.setPassword("********");
		List<EventDTO> eventsDto = new ArrayList<EventDTO>();
		for(Event source : user.getEvents()) {
			// katrs item (Event source) no List<Event> -> EventDTO
			EventDTO eventDto = new EventDTO();
			eventDto.setId(source.getId());
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
	
	
	@RequestMapping(value="/login", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<UserResponseDTO> verifyLogin(@RequestBody UserDTO userForm) {
		
		UserResponseDTO result = new UserResponseDTO(userForm);
		
		if (userForm == null || userForm.getUsername() == null || userForm.getPassword() == null ||
				userForm.getUsername().isEmpty() || userForm.getPassword().isEmpty()) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
				
		User user = coreService.loginUser(userForm.getUsername(), userForm.getPassword());
		if (user != null) {
			result.user = convertToDto(user);
		}
		
		return ResponseEntity.ok().body(result);
	}
		
	

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		request.getSession().invalidate();
		
		return "redirect:/";
	}
	
	
	
}



