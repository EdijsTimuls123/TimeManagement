package lv.progmeistars.lessons.services;
import org.springframework.stereotype.Service;
@Service
public class CalculatorBean{
	public String sum(String a, String b) {
		Double aDbl = Double.parseDouble(a);
		Double bDbl = Double.parseDouble(b);
		return Double.toString(aDbl+bDbl);
	}

	
	public String sub(String a, String b) {
		Double aDbl = Double.parseDouble(a);
		Double bDbl = Double.parseDouble(b);
		return Double.toString(aDbl-bDbl);
	}

	
	public String mul(String a, String b) {
		Double aDbl = Double.parseDouble(a);
		Double bDbl = Double.parseDouble(b);
		return Double.toString(aDbl*bDbl);
	}

	
	public String div(String a, String b) {
		Double aDbl = Double.parseDouble(a);
		Double bDbl = Double.parseDouble(b);
		return Double.toString(aDbl/bDbl);
	}
}
