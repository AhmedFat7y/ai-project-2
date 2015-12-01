package models;

import java.util.ArrayList;
import java.util.Arrays;

import enums.LogicalOperator;

public class FunctionCallExpression extends Argument {

	public ArrayList<Argument> arguments;

	public FunctionCallExpression(FunctionCallExpression fe) {
		super(fe.symbol);
		this.arguments = new ArrayList<>();
		for (Argument arg : fe.arguments) {
			this.arguments.add((Argument) arg.shallowCopy());
		}
		this.isNegated = fe.isNegated;
	}

	public FunctionCallExpression(char symbol) {
		super(symbol);
		this.arguments = new ArrayList<>();
	}

	public FunctionCallExpression(LogicalOperator o) {
		super(o);
	}

	public FunctionCallExpression(LogicalOperator o, Expression e,
			boolean isNegated) {
		super(o, e, isNegated);
	}

	public void addArgument(Argument arg) {
		this.arguments.add(arg);
	}

	public boolean hasVariable(Argument arg) {
		return hasVariable(arg.symbol);
	}// end hasVariable

	public boolean hasVariable(char symbol) {
		for (Argument argument : arguments) {
			if (argument instanceof Variable && argument.symbol == symbol) {
				return true;
			} else if (argument instanceof FunctionCallExpression
					&& ((FunctionCallExpression) argument).hasVariable(symbol)) {
				return true;
			}// endif
		}// endfor
		return false;
	}// end hasVariable

	@Override
	public boolean match(Argument otherArgument) {
		if (otherArgument instanceof Variable) {
			return !this.hasVariable(otherArgument);
		} else if (otherArgument instanceof FunctionCallExpression) {
			FunctionCallExpression otherFunction = ((FunctionCallExpression) otherArgument);
			return this.symbol == otherArgument.symbol
					&& this.arguments.size() == otherFunction.arguments.size();
		}
		return false;
	}// end match

	// replace arg1 with arg2
	public void substitute(Argument toBeReplaced, Argument replacement) {
		// System.out.println("-------- replace: " + toBeReplaced + " with "
		// + replacement + " in " + this);
		for (int i = 0; i < arguments.size(); i++) {
			Argument arg = arguments.get(i);
			if (arg.equals(toBeReplaced)) {
				arguments.set(i, replacement);
			} else if (arg instanceof FunctionCallExpression) {
				((FunctionCallExpression) arg).substitute(toBeReplaced,
						replacement);
			}
		}
	}// end substitute

	public void substitute(char toBeReplaced, char replacement) {
		// System.out.println("-------- replace: " + toBeReplaced + " with "
		// + replacement + " in " + this);
		for (int i = 0; i < arguments.size(); i++) {
			Argument arg = arguments.get(i);
			if (arg.symbol == toBeReplaced) {
				arg.symbol = replacement;
			} else if (arg instanceof FunctionCallExpression) {
				((FunctionCallExpression) arg).substitute(toBeReplaced,
						replacement);
			}
		}
	}// end substitute

	@Override
	public int getNumberOfChars() {
		int result = 2 + super.getNumberOfChars(); // for first symbol and
													// opening paren => 'p('
		// for inner arguments
		for (Argument argument : arguments) {
			result += argument.getNumberOfChars();
		}// endfor
			// closing paren => ')' is accounted for in the last variable call.
			// check if last argument is not variable then add 1
		if (arguments.get(arguments.size() - 1) instanceof FunctionCallExpression) {
			result += 1;
		}
		return result;
	}// getNumberOfChars

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

	@Override
	public boolean equals(Object other) {
		if (super.equals(other)) {
			return true;
		}
		if (other instanceof FunctionCallExpression) {
			FunctionCallExpression otherFExp = (FunctionCallExpression) other;
			if (this.symbol == otherFExp.symbol
					&& this.arguments.size() == otherFExp.arguments.size()) {
				for (int i = 0; i < this.arguments.size(); i++) {
					Argument arg1 = this.arguments.get(i);
					Argument arg2 = otherFExp.arguments.get(i);
					if (!arg1.equals(arg2)) {
						return false;
					}
				}
				return true;
			}
		}

		return false;
	}// end equals

	@Override
	public Expression shallowCopy() {
		return new FunctionCallExpression(this);
	}

	public Character[] getUsedChars() {
		ArrayList<Character> usedChars = new ArrayList<>();
		for (Argument arg : this.arguments) {
			if (arg instanceof Variable) {
				usedChars.add(arg.symbol);
			} else if (arg instanceof FunctionCallExpression) {
				usedChars.addAll(Arrays.asList(((FunctionCallExpression) arg)
						.getUsedChars()));
			}
		}
		return usedChars.toArray(new Character[0]);
	}

}
