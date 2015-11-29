package parsers;

import models.Argument;
import models.EmptyExpression;
import models.Expression;
import models.FunctionCallExpression;
import models.QuantifiedExpression;

public class Parser {

	public static Expression parse(String str) throws Exception {
		str = str.replace(" ", "");
		System.out.println("Parse: " + str);
		Expression e = null;
		for (int i = 0; i < str.length() - 1; i += 2) {
			char first = str.charAt(i), second = str.charAt(i + 1);
			if (SymbolsChecker.isQuantifier(str.charAt(i))) {
				e = parseQuantifiers(str.substring(i));
			} else {
				throw new Exception("Unexpected Charatcer: " + first
						+ ", index: " + i + "str: " + str);
			}
			System.out.println("Expression found: " + e);
			i += e.getNumberOfChars();
		}
		return e;
	}

	public static QuantifiedExpression parseQuantifiers(String str)
			throws Exception {
		System.out.println("Parse Quantifier: " + str);
		boolean scopeStarted = false;
		boolean negation = false;
		QuantifiedExpression qe = new QuantifiedExpression();
		for (int i = 0; i < str.length() - 1; i += 2) {
			System.out.println("loop-start: " + str.charAt(i));
			char first = str.charAt(i), second = str.charAt(i + 1);
			Expression e = new EmptyExpression();
			if (SymbolsChecker.isQuantifier(first)) {
				if (scopeStarted) {
					e = parseQuantifiers(str.substring(i));
					qe.addExpression(e);
				} else {
					qe.addQuantifier(SymbolsChecker.getQuantifier(first),
							second);
				}
			} else if (SymbolsChecker.isComma(first)) {
				if (Character.isLetter(second)) {
					qe.addSymbol(second);
				} else {
					throw new Exception("Unexpected comma");
				}
			} else if (SymbolsChecker.isOpenedSquareBracket(first)) {
				scopeStarted = true;
				i--;
			} else if (SymbolsChecker.isClosedSquareBracket(first)) {
				return qe;
			} else if (Character.isLetter(first)) {
				e = parseFunctionCall(str.substring(i));
				qe.addExpression(e);
				i -= 2;
			} else if (SymbolsChecker.isOperator(first)) {
				qe.addOperator(SymbolsChecker.getOperator(first));
				i--;
			} else if (SymbolsChecker.isNegation(first)) {
				negation = true;
				i--;
				continue; // skip the falsifying of negation flag
			} else if (SymbolsChecker.isOpenedParantheses(first)) {
				e = parseGroupExpression(str.substring(i));
				qe.addExpression(e);
			}
			e.isNegated = negation;
			negation = false;
			i += e.getNumberOfChars();
			System.out.println("loop-end: " + str.charAt(i));
		}
		return qe;
	}

	public static Expression parseGroupExpression(String str) {
		Expression e = null;
		char first = str.charAt(1);
		if (Character.isLetter(first)) {
			
		}
		return e;
	}

	public static FunctionCallExpression parseFunctionCall(String str) {
		str = str.replace(" ", "");
		System.out.println("Parse Function Call: " + str);
		if (!str.contains("(")) {
			return null;
		}// endif
		FunctionCallExpression functionCallExpression = (FunctionCallExpression) SymbolsChecker
				.getArgument("" + str.charAt(0) + str.charAt(1));
		for (int i = 2; i < str.length() - 1; i += 2) {
			Argument arg = SymbolsChecker.getArgument("" + str.charAt(i)
					+ str.charAt(i + 1));
			if (arg instanceof FunctionCallExpression) {
				arg = parseFunctionCall(str.substring(i, str.length() - 1));
				// i is pointing at begining of the arg-FunctionCallExpression
				// we subtract 1 to account for th i+=2 in the for loop
				// also to bybass the comma after the FunctionCallExpression and
				// start from
				// next argument
				// and to evade breaking from the loop.
				i += arg.getNumberOfChars() - 1;
			}// endif
			functionCallExpression.addArgument(arg);
			if (str.charAt(i + 1) == ')') {
				break;
			}// endif
		}// endfor
		return functionCallExpression;
	}// end parse

}// end class
