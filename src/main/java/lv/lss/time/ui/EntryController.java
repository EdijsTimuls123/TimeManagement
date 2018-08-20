package lv.lss.time.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EntryController {
	@RequestMapping(value="/home")
	public String resolve(){
		System.out.println("In resolve");
		return "index";
	}
}
