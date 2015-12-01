package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import models.EmptyExpression;
import models.Expression;
import models.FunctionCallExpression;
import models.GroupExpression;
import models.QuantifiedExpression;
import models.Variable;
import enums.LogicalOperator;
import enums.Quantifier;

public class CNFConverter {
	public GroupExpression expression;
	ArrayList<ArrayList<FunctionCallExpression>> cnfForm;
	public ArrayList<Character> usedChars;
	public HashMap<Character, Character> replacements;

	public CNFConverter(Expression e) {
		this((GroupExpression) e);
	}

	public CNFConverter(GroupExpression ge) {
		this.expression = ge;
		this.cnfForm = new ArrayList<>();
		usedChars = new ArrayList<>();
	}

	public void convert() {
		System.out.println("========== Start Converting ==========");
		System.out.println("Prepare the expression: " + expression);
		prepareExpression(this.expression);
		System.out.println("Starting Expression: " + expression);
		removeIFFs(this.expression);
		System.out.println("After removing IFFs: " + this.expression);
		removeImplications(this.expression);
		System.out.println("After removing Implications: " + this.expression);
		pushNegation(this.expression);
		System.out.println("After pushing negation: " + this.expression);
		serialize(this.expression);
		standardizeQuantifiers(this.expression);
		System.out.println("After standardizing quantifiers: "
				+ this.expression);
		skolemize(this.expression, null);
		System.out.println("After skolemizing: " + this.expression);
		
		removeUniversal(expression);
		System.out.println("After dropping universals: " + this.expression);
	}

	private void serialize(GroupExpression ge) {
		ge.serialize();
		HashSet<Character> temp = new HashSet<>();
		for (FunctionCallExpression fe : ge.funExpressions) {
			usedChars.addAll(Arrays.asList(fe.getUsedChars()));
		}
		temp.addAll(usedChars);
		usedChars.clear();
		usedChars.addAll(temp);
		// System.out.println("----- Serialized: " + ge.funExpressions);
		// System.out.println("-----  " + usedChars);
		// GroupExpression temp = new GroupExpression();
		// temp.addExpression(ge);
		// for (Expression e : temp.expressions) {
		// if (e instanceof GroupExpression) {
		// ((QuantifiedExpression) e).serialize();
		// }
		// }
	}

	private void prepareExpression(GroupExpression expression) {
		for (Expression e : expression.expressions) {
			if (e instanceof GroupExpression) {
				prepareExpression((GroupExpression) e);
			}
		}

		for (int i = 0; i < expression.operators.size() - 1; i++) {
			LogicalOperator op = expression.operators.remove(i);
			GroupExpression ge = new EmptyExpression();
			ge.addExpression(expression.expressions.remove(i));
			ge.addExpression(expression.expressions.remove(i));
			ge.addOperator(op);
			expression.expressions.add(i, ge);
		}
	}

	public void removeIFFs(GroupExpression ge) {
		// System.out.println("-- Start to remove IFFs");
		Integer[] operatorIndecies = ge
				.getOperatorIndecies(LogicalOperator.IFF);
		for (int operatorIndex : operatorIndecies) {
			_removeIFF(ge, operatorIndex);
		}
		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				removeIFFs((GroupExpression) e);
			}
		}
	}

	private void _removeIFF(GroupExpression ge, int operatorIndex) {
		Expression e1 = ge.getExpression(operatorIndex);
		Expression e2 = ge.getExpression(operatorIndex + 1);
		EmptyExpression ge1 = new EmptyExpression();
		EmptyExpression ge2 = new EmptyExpression();

		ge.operators.remove(operatorIndex);
		ge.expressions.remove(operatorIndex);
		ge.expressions.remove(operatorIndex);

		ge.expressions.add(operatorIndex, ge1);
		ge.expressions.add(operatorIndex, ge2);
		ge.operators.add(operatorIndex, LogicalOperator.AND);

		ge1.addExpression(e1.shallowCopy());
		ge1.addExpression(e2.shallowCopy());
		ge1.addOperator(LogicalOperator.IMPLICATION);

		ge2.addExpression(e2.shallowCopy());
		ge2.addExpression(e1.shallowCopy());
		ge2.addOperator(LogicalOperator.IMPLICATION);

	}

	public void removeImplications(GroupExpression ge) {
		// System.out.println("-- Start to remove Implications");
		Integer[] operatorIndecies = ge
				.getOperatorIndecies(LogicalOperator.IMPLICATION);
		for (int operatorIndex : operatorIndecies) {
			_removeImplication(ge, operatorIndex);
		}
		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				removeImplications((GroupExpression) e);
			}
		}
	}

	private void _removeImplication(GroupExpression ge, int operatorIndex) {
		Expression e1 = ge.getExpression(operatorIndex);
		e1.isNegated = !e1.isNegated;
		ge.operators.set(operatorIndex, LogicalOperator.OR);

	}

	public void pushNegation(GroupExpression ge) {

		if (ge.isNegated) {
			_pushNegation(ge);
		}

		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				pushNegation((GroupExpression) e);
			}
		}
	}

	private void _pushNegation(GroupExpression ge) {
		if (ge instanceof QuantifiedExpression) {
			// if (ge.operators.get(0) == Quantifier.THERE_EXISTS) {
			// ge.operators.set(0, Quantifier.FOR_ALL);
			// } else if (ge.operators.get(0) == Quantifier.FOR_ALL) {
			// ge.operators.set(0, Quantifier.THERE_EXISTS);
			// }
			// for (int i = 0; i < ge.operators.size; i++) {
			// ge.operators.get(i).negate();
			// }
			// ge.negate();
			System.err.println("No negation for quantifiers yet");
		} else {
			if (ge.operators.get(0) == LogicalOperator.AND) {
				ge.operators.set(0, LogicalOperator.OR);
			} else if (ge.operators.get(0) == LogicalOperator.OR) {
				ge.operators.set(0, LogicalOperator.AND);
			} else {
				System.err.println("NOOO PLEASE NO OMG STOP YOU #@$#@$!#");
				return;
			}
			ge.expressions.get(0).negate();
			ge.expressions.get(1).negate();
			ge.negate();
		}
	}

	public void standardizeQuantifiers(GroupExpression ge) {

		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				standardizeQuantifiers((GroupExpression) e);
			}
		}

		if (ge instanceof QuantifiedExpression) {
			QuantifiedExpression qe = (QuantifiedExpression) ge;
			if (qe.toString().equals("∃y[¬Q(y)|¬R(y,x)]")) {
				System.out.println("im here");
			}
			_standardizeQuantifiers(qe);
		}
	}

	public void _standardizeQuantifiers(QuantifiedExpression qe) {
		replacements = new HashMap<>();
		Character[] symbols = qe.getQuantifiersSymobls();
		for (char c : symbols) {
			char replacement = getUnusedChar();
			// System.out.println("------- " + replacement + ", " + usedChars);
			replacements.put(c, replacement);
		}

		for (FunctionCallExpression fe : qe.funExpressions) {
			for (char c : symbols) {
				if (fe.hasVariable(c)) {
					fe.substitute(c, replacements.get(c));
				}
			}
		}
		for (char c : symbols) {
			qe.replaceQuantifierSymbol(c, replacements.get(c));
		}
	}

	private char getUnusedChar() {
		return getUnusuedChar(false);
	}

	private char getUnusuedChar(boolean isCapital) {
		for (int i = 'f'; i <= 'z'; i++) {
			Character temp = new Character((char) i);
			if (!usedChars.contains(temp)) {
				usedChars.add(temp);
				if (isCapital) {
					return Character.toUpperCase(temp);
				} else {
					return Character.toLowerCase(temp);
				}
			}
		}
		return '%';
	}

	public void skolemize(GroupExpression ge, QuantifiedExpression tempParent) {
		QuantifiedExpression parent = tempParent;
		if (ge instanceof QuantifiedExpression
				&& ((QuantifiedExpression) ge).hasUniversal()) {
			tempParent = (QuantifiedExpression) ge;
		}
		for (Expression e : ge.expressions) {
			if (e instanceof QuantifiedExpression) {
				skolemize((GroupExpression) e, tempParent);
			}
		}
		if (ge instanceof QuantifiedExpression
				&& ((QuantifiedExpression) ge).hasExistential()) {
			_skolemize((QuantifiedExpression) ge, parent);
		}
	}

	private void _skolemize(QuantifiedExpression qe, QuantifiedExpression parent) {
		Character[] symbolsE = qe.getExistentialQuantifiersSymbols();
		Character[] symbolsA = qe.getUniversalQuantifiersSymbols();
		qe.removeExistentialQuantifiers();
		if (parent != null && symbolsA.length == 0) {
			symbolsA = parent.getUniversalQuantifiersSymbols();
		}
		for (char c : symbolsE) {
			for (FunctionCallExpression fe : qe.funExpressions) {
				if (fe.hasVariable(c)) {
					FunctionCallExpression replacement = new FunctionCallExpression(
							getUnusuedChar(true));
					for (char c2 : symbolsA) {
						replacement.addArgument(new Variable(c2));
					}
					if (replacement.arguments.size() == 0) {
						replacement.addArgument(new Variable(getUnusedChar()));
					}
					fe.substitute(new Variable(c), replacement);
				}
			}
		}
	}

	public void removeUniversal(GroupExpression ge) {

		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				removeUniversal((GroupExpression) e);
			}
		}

		if (ge instanceof QuantifiedExpression) {
			((QuantifiedExpression) ge).removeUniversalQuantifiers();
		}
	}

	public void flatten(GroupExpression ge) {
		// TODO Auto-generated method stub
	}
}
