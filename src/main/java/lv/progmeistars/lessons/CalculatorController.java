package lv.progmeistars.lessons;
import javax.inject.Inject;
import lv.progmeistars.lessons.services.CalculatorBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/calculator")
public class CalculatorController {
	@Inject
	CalculatorBean cb;	
	public CalculatorBean getCb() {
		return cb;
	}
	public void setCb(CalculatorBean cb) {
		this.cb = cb;
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String startPage(Model model){
		model.addAttribute("a",0);
		model.addAttribute("b",0);
		return "calculator";
	}
	@RequestMapping(value = "/pi", method = RequestMethod.GET)
	public String sayPI(Model model){
		model.addAttribute("PI", Math.PI);
		return "pi";
	}
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public String calculate(@RequestParam String action,
			@RequestParam String a, @RequestParam String b, Model model) {
		if ("+".equalsIgnoreCase(action)){
			model.addAttribute("result", cb.sum(a, b));
		}
		if ("-".equalsIgnoreCase(action)){
			model.addAttribute("result", cb.sub(a, b));
		}
		if ("*".equalsIgnoreCase(action)){
			model.addAttribute("result", cb.mul(a, b));
		}
		if ("/".equalsIgnoreCase(action)){
			model.addAttribute("result", cb.div(a, b));
		}
		model.addAttribute("a",a);
		model.addAttribute("b",b);
		return "calculator";
	}
}
