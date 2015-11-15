package unification;

import java.util.ArrayList;
import java.util.Arrays;

public class Predicate extends Argument {
	public ArrayList<Argument> arguments;

	public Predicate(char symbol, Argument... arguments) {
		super(symbol);
		this.arguments = new ArrayList<Argument>(Arrays.asList(arguments));
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += '(';
		for (Argument argument : arguments) {
			result += argument.toString();
			if (arguments.get(arguments.size() - 1) != argument) {
				result += ',';
			}//endif
		}//endfor
		result += ')';
		return result;
	}//end tostring

	public void addArgument(Argument arg) {
		this.arguments.add(arg);		
	}
	
	@Override
	public int getNumberOfChars() {
		int result = 2; // for first symbol and opening paren => 'p('
		
		//for inner arguments
		for (Argument argument : arguments) {
			result += argument.getNumberOfChars();
		}//endfor
		// closing paren => ')' is accounted for in the last variable call.
		//check if last argument is not variable then add 1
		if (arguments.get(arguments.size() - 1) instanceof Predicate) {
			result += 1;
		}
		return result;
	}
}//end class
