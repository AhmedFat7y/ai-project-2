package parsers;

import unification.Argument;
import unification.Predicate;
import unification.Variable;
import enums.ArgumentType;

public class PredicateParser {

	public static Predicate parse(String str) {
		str = str.replace(" ", "");
		if (!str.contains("(")) {
			return null;
		}// endif
		Predicate predicate = (Predicate) getArgument("" + str.charAt(0)
				+ str.charAt(1));
		for (int i = 2; i < str.length() - 1; i += 2) {
			Argument arg = getArgument("" + str.charAt(i) + str.charAt(i + 1));
			if (arg instanceof Predicate) {
				arg = parse(str.substring(i, str.length() - 1));
				// i is pointing at begining of the arg-predicate
				// we subtract 1 to account for th i+=2 in the for loop
				// also to bybass the comma after the predicate and start from
				// next argument
				// and to evade breaking from the loop.
				i += arg.getNumberOfChars() - 1;
			}// endif
			predicate.addArgument(arg);
			if (str.charAt(i + 1) == ')') {
				break;
			}// endif
		}// endfor
		return predicate;
	}// end parse

	private static ArgumentType getArgumentType(String str) {
		return getArgumentType(str.charAt(1));
	}// end getArgumentType

	private static ArgumentType getArgumentType(char c) {
		if (c == '(') {
			return ArgumentType.PREDICATE;
		} else if (c == ')' || c == ',') {
			return ArgumentType.VARIABLE;
		}// end else

		return ArgumentType.NONE;
	}// end getArgumentType

	private static Argument getArgument(String str) {
		Argument arg = null;
		ArgumentType argumentType = getArgumentType(str);
		if (argumentType == ArgumentType.PREDICATE) {
			arg = new Predicate(str.charAt(0));

		} else if (argumentType == ArgumentType.VARIABLE) {
			arg = new Variable(str.charAt(0));
		}
		return arg;
	}// end getArguments

}// end class
