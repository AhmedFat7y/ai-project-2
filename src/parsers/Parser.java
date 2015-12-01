package parsers;

import models.Argument;
import models.EmptyExpression;
import models.Expression;
import models.FunctionCallExpression;
import models.GroupExpression;
import models.QuantifiedExpression;

public class Parser {

	public static Expression parse(String str) throws Exception {
		System.out.println("Parse: " + str);
		Expression e = null;
		boolean negation = false;
		for (int i = 0; i < str.length() - 1; i += 2) {
			char first = str.charAt(i);
			// char second = str.charAt(i + 1);
			if (SymbolsChecker.isNegation(first)) {
				negation = true;
				i--;
				continue;
			} else if (SymbolsChecker.isQuantifier(first)) {
				e = parseQuantifiers(str.substring(i));
				e.isNegated = negation;
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
		// boolean scopeStarted = false;

		QuantifiedExpression qe = new QuantifiedExpression();
		qe.isNegated = SymbolsChecker.isNegation(str.charAt(0));

		for (int i = qe.isNegated ? 1 : 0; i < str.length() - 1; i += 2) {

			char first = str.charAt(i), second = str.charAt(i + 1);
			Expression e = new EmptyExpression();
			if (SymbolsChecker.isQuantifier(first)) {
				qe.addQuantifier(SymbolsChecker.getQuantifier(first), second);
			} else if (SymbolsChecker.isComma(first)) {
				if (Character.isLetter(second)) {
					qe.addSymbol(second);
				} else {
					throw new Exception("Unexpected comma");
				}
			} else if (SymbolsChecker.isOpenedSquareBracket(first)) {
				e = parseGroupExpression(str.substring(i + 1));
				qe.expressions = ((GroupExpression) e).expressions;
				qe.operators = ((GroupExpression) e).operators;
			} else if (SymbolsChecker.isClosedSquareBracket(first)) {
				return qe;
			} else if (SymbolsChecker.isOpenedParantheses(first)) {
				e = parseGroupExpression(str.substring(i + 1));
				qe.addExpression(e);
			} else if (SymbolsChecker.isClosedParantheses(first)) {
				i--;
				continue;
			}
			// e.isNegated = negation;
			// negation = false;
			i += e.getNumberOfChars();

		}
		return qe;
	}

	public static Expression parseGroupExpression(String str) throws Exception {
		boolean negation = false;
		GroupExpression ge = new GroupExpression();
		for (int i = 0; i < str.length() - 1; i += 2) {
			Expression e = new EmptyExpression();
			char first = str.charAt(i);
			// char second = str.charAt(i + 1);
			if (SymbolsChecker.isQuantifier(first)) {
				e = parseQuantifiers(str.substring(i));
				ge.addExpression(e);
			} else if (Character.isLetter(first)) {
				e = parseFunctionCall(str.substring(i));
				ge.addExpression(e);
				i -= 2;
			} else if (SymbolsChecker.isOperator(first)) {
				ge.addOperator(SymbolsChecker.getOperator(first));
				i--;
			}
			if (SymbolsChecker.isOpenedParantheses(first)) { // empty expression
				GroupExpression tempGE = (GroupExpression) parseGroupExpression(str
						.substring(i + 1));

				EmptyExpression tempEE = new EmptyExpression();
				e = tempEE;
				tempEE.operators = tempGE.operators;
				tempEE.expressions = tempGE.expressions;
				ge.addExpression(e);
			} else if (SymbolsChecker.isNegation(first)) {
				negation = true;
				i--;
				continue; // skip the falsifying of negation flag
			}
			e.isNegated = negation;
			negation = false;
			i += e.getNumberOfChars();
		}
		return ge;
	}

	public static FunctionCallExpression parseFunctionCall(String str) {
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
