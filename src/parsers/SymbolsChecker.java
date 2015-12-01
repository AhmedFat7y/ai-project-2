/**
 * 
 */
package parsers;

import models.Argument;
import models.FunctionCallExpression;
import models.Variable;
import enums.ArgumentType;
import enums.LogicalOperator;
import enums.Quantifier;

/**
 * @author MacBookAir
 *
 */
public class SymbolsChecker {

	/**
	 * 
	 */
	public SymbolsChecker() {
		// TODO Auto-generated constructor stub
	}

	public static Quantifier getQuantifier(char c) {
		switch (c) {
		case '∃':
			return Quantifier.THERE_EXISTS;
		case '∀':
			return Quantifier.FOR_ALL;
		default:
			return Quantifier.NONE;
		}// end switch
	}// end getQuantifier

	public static boolean isQuantifier(char c) {

		return getQuantifier(c) != Quantifier.NONE;
	}

	public static boolean isComma(char c) {
		return c == ',';
	}

	public static boolean isOpenedSquareBracket(char c) {
		return c == '[';
	}

	public static boolean isClosedSquareBracket(char c) {
		return c == ']';
	}

	public static boolean isOpenedParantheses(char c) {
		return c == '(';
	}

	public static boolean isClosedParantheses(char c) {
		return c == ')';
	}

	public static char getQuantifier(Quantifier q) {
		if (q == Quantifier.FOR_ALL) {
			return '∀';
		} else if (q == Quantifier.THERE_EXISTS) {
			return '∃';
		}
		return '#';
	}

	public static LogicalOperator getOperator(char c) {
		switch (c) {
		case '|':
			return LogicalOperator.OR;
		case '⇔':
			return LogicalOperator.IFF;
		case '∧':
			return LogicalOperator.AND;
		case '⇒':
			return LogicalOperator.IMPLICATION;
		default:
			return LogicalOperator.NONE;
		}// end switch
	}// end getOperator

	public static boolean isNegation(char c) {
		return c == '¬';
	}

	public static boolean isOperator(char c) {
		return getOperator(c) != LogicalOperator.NONE;
	}

	public static ArgumentType getArgumentType(String str) {
		return getArgumentType(str.charAt(1));
	}// end getArgumentType

	// decide argument type based on the next char
	private static ArgumentType getArgumentType(char c) {
		switch (c) {
		case '(':
			return ArgumentType.FUNCTION_CALL;
		case ')':
		case ',':
		case ']':
			return ArgumentType.VARIABLE;
		default:
			return ArgumentType.NONE;
		}// end switch
	}// end getArgumentType

	public static Argument getArgument(String str) {
		Argument arg = null;
		ArgumentType argumentType = getArgumentType(str);
		if (argumentType == ArgumentType.FUNCTION_CALL) {
			arg = new FunctionCallExpression(str.charAt(0));

		} else if (argumentType == ArgumentType.VARIABLE) {
			arg = new Variable(str.charAt(0));
		}
		return arg;
	}// end getArguments

	public static char getOperator(LogicalOperator op) {
		switch (op) {
		case AND:
			return '∧';
		case IFF:
			return '⇔';
		case IMPLICATION:
			return '⇒';
		case NONE:
			return 0;
		case OR:
			return '|';
		}
		return 0;

	}

	public static boolean isIFF(LogicalOperator logicalOperator) {
		return logicalOperator == LogicalOperator.IFF;
	}

	public static boolean isImplication(LogicalOperator logicalOperator) {
		return logicalOperator == LogicalOperator.IMPLICATION;
	}
}
