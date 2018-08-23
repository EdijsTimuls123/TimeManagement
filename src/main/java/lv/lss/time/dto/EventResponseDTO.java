package lv.lss.time.dto;

public class EventResponseDTO {
	public String msg;
    public EventDTO event;
    
    public EventResponseDTO(EventDTO event) {
    	this.msg = "success";
    	this.event = new EventDTO();
    	this.event.setId(event.getId());
    	this.event.setUserId(event.getUserId());
    	this.event.setTitle(event.getTitle());
    	this.event.setStart(event.getStart());
    	this.event.setEnd(event.getEnd());
    }
    
}
