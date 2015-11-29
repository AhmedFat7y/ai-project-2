package models;

import java.util.ArrayList;

import enums.LogicalOperator;

public class FunctionCallExpression extends Argument {
	
	public ArrayList<Argument> arguments;
	
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
	
	public boolean hasVariable(char c) {
		for (Argument argument : arguments) {
			if (argument instanceof Variable && argument.symbol == c) {
				return true;
			} else if (argument instanceof FunctionCallExpression
					&& ((FunctionCallExpression) argument).hasVariable(c)) {
				return true;
			}// endif
		}// endfor
		return false;
	}//end hasVariable
	
	
	
	@Override
	public boolean match(Argument otherArgument) {
		if (otherArgument instanceof Variable) {
			return !this.hasVariable(otherArgument.symbol);
		} else if (otherArgument instanceof FunctionCallExpression) {
			return this.symbol == otherArgument.symbol
					&& this.arguments.size() == ((FunctionCallExpression) otherArgument).arguments
							.size();
		}
		return false;
	}// end match

	// replace arg1 with arg2
	public void substitute(Argument arg1, Argument arg2) {
		System.out.println("replace: " + arg1 + " with: " + arg2 +" in " + this);
		for (int i = 0; i < arguments.size(); i++) {
			Argument arg = arguments.get(i);
			if (arg.equals(arg1)) {
				arguments.set(i, arg2);
			} else if (arg instanceof FunctionCallExpression) {
				((FunctionCallExpression) arg).substitute(arg1, arg2);
			}
		}
	}//end substitute
	
	@Override
	public int getNumberOfChars() {
		int result = 2 + super.getNumberOfChars() ; // for first symbol and opening paren => 'p('
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
		if (other instanceof FunctionCallExpression) {
			FunctionCallExpression otherFunctionCallExpression = (FunctionCallExpression) other;
			return this.symbol == otherFunctionCallExpression.symbol;
		}

		return false;
	}//end equals
}
