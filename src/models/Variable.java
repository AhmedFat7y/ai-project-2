package models;

public class Variable extends Argument {
	public boolean isConstant;

	public Variable(char symbol) {
		super(symbol);
		isConstant = symbol >= 'a' && symbol <= 'd';
	}

	@Override
	public int getNumberOfChars() {
		return 2;
	}

	/**
	 * checks if otherArgument is variable then it compares symbols checks if
	 * otherArgument is FunctionCallExpression then it checks if this FunctionCallExpression contains this
	 * variable if it contains this variable then returns false;
	 * **/
	@Override
	public boolean match(Argument otherArgument) {
		if (otherArgument instanceof Variable) {
			return super.match(otherArgument);
		} else if (otherArgument instanceof FunctionCallExpression) {
			FunctionCallExpression p = (FunctionCallExpression) otherArgument;
			return !p.hasVariable(symbol);
		}
		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Variable) {
			Variable otherVariable = (Variable) other;
			return this.symbol == otherVariable.symbol;
		}

		return false;
	}
}
