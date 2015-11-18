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
			}// endif
		}// endfor
		result += ')';
		return result;
	}// end tostring

	public void addArgument(Argument arg) {
		this.arguments.add(arg);
	}

	@Override
	public int getNumberOfChars() {
		int result = 2; // for first symbol and opening paren => 'p('

		// for inner arguments
		for (Argument argument : arguments) {
			result += argument.getNumberOfChars();
		}// endfor
			// closing paren => ')' is accounted for in the last variable call.
			// check if last argument is not variable then add 1
		if (arguments.get(arguments.size() - 1) instanceof Predicate) {
			result += 1;
		}
		return result;
	}

	public boolean hasVariable(char c) {
		for (Argument argument : arguments) {
			if (argument instanceof Variable && argument.SYMBOL == c) {
				return true;
			} else if (argument instanceof Predicate
					&& ((Predicate) argument).hasVariable(c)) {
				return true;
			}// endif
		}// endfor
		return false;
	}

	@Override
	public boolean match(Argument otherArgument) {
		if (otherArgument instanceof Variable) {
			return !this.hasVariable(otherArgument.SYMBOL);
		} else if (otherArgument instanceof Predicate) {
			return this.SYMBOL == otherArgument.SYMBOL
					&& this.arguments.size() == ((Predicate) otherArgument).arguments
							.size();
		}
		return false;
	}// end match

	@Override
	public boolean equals(Object other) {
		if (other instanceof Predicate) {
			Predicate otherPredicate = (Predicate) other;
			return this.SYMBOL == otherPredicate.SYMBOL;
		}

		return false;
	}

	// replace arg1 with arg2
	public void substitute(Argument arg1, Argument arg2) {
		for (int i = 0; i < arguments.size(); i++) {
			Argument arg = arguments.get(i);
			if (arg.equals(arg1)) {
				arguments.set(i, arg2);
			} else if (arg instanceof Predicate) {
				((Predicate) arg).substitute(arg1, arg2);
			}
		}
	}
}// end class
