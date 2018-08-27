package lv.lss.time.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import lv.lss.time.dto.EventResponseDTO;
import lv.lss.time.jpa.Event;
import lv.lss.time.jpa.User;

@Controller
@RequestMapping(value="/event")
public class EventController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	
	private TimeCoreServiceInterface coreService;
	
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public void setCoreService(TimeCoreServiceInterface coreService) {
		this.coreService = coreService;
	}
    
    private Event convertToEntity(EventDTO eventDto) {
    	Event e = new Event();
		e.setId(eventDto.getId());
		e.setStartDate(LocalDateTime.parse(eventDto.getStart(), formatter));
		e.setEndDate(LocalDateTime.parse(eventDto.getEnd(), formatter));
		e.setInfo(eventDto.getTitle());
		User user = coreService.getUserById(eventDto.getUserId());
		e.setUser(user);
		return e;
    }

    @Scope("session")
    @RequestMapping(value="/delete", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<EventResponseDTO> deleteEvent(@RequestBody EventDTO eventForm) {

		EventResponseDTO result = new EventResponseDTO(eventForm);
		
		// Check mandatory parameters
		if (eventForm.getId() == null || eventForm.getId() == 0) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
		
		// Delete event
		coreService.deleteEventById(eventForm.getId());

		return ResponseEntity.ok().body(result);
	}
	
    @Scope("session")
    @RequestMapping(value="/update", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<EventResponseDTO> updateEvent(@RequestBody EventDTO eventForm) {

		EventResponseDTO result = new EventResponseDTO(eventForm);
		
		// Check mandatory parameters
		if (eventForm.getId() == null || eventForm.getId() == 0 ||
				eventForm.getTitle() == null || eventForm.getTitle().isEmpty()) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
		
		Event event = convertToEntity(eventForm);

		// Check cross
		if (!coreService.validateEvent(event)) {
			result.msg = "cross";
			return ResponseEntity.badRequest().body(result);
		}

		// Update event
		coreService.saveEvent(convertToEntity(eventForm));

		return ResponseEntity.ok().body(result);
	}
	
    @Scope("session")
	@RequestMapping(value="/create", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventDTO eventForm) {

		EventResponseDTO result = new EventResponseDTO(eventForm);
		
		// Check mandatory parameters
		if (eventForm.getTitle() == null || eventForm.getTitle().isEmpty()) {
			result.msg = "empty";
			return ResponseEntity.badRequest().body(result);
		}
		
		Event event = convertToEntity(eventForm);
		
		// Check cross
		if (!coreService.validateEvent(event)) {
			result.msg = "cross";
			return ResponseEntity.badRequest().body(result);
		}

		// Add event
		Integer id = coreService.createEvent(event);
		result.event.setId(id);

		return ResponseEntity.ok().body(result);
	}
}
