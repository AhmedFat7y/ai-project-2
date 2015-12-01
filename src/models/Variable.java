package models;

public class Variable extends Argument {
	public boolean isConstant;

	public Variable(Variable var) {
		super(var.symbol);
		this.isConstant = var.isConstant;
		this.isNegated = var.isNegated;
	}

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
	 * otherArgument is FunctionCallExpression then it checks if this
	 * FunctionCallExpression contains this variable if it contains this
	 * variable then returns false;
	 * **/
	@Override
	public boolean match(Argument otherArgument) {
		if (otherArgument instanceof Variable) {
			Variable otherVariable = (Variable) otherArgument;
			return !(otherVariable.isConstant && this.isConstant && this.symbol != otherVariable.symbol);
		} else if (otherArgument instanceof FunctionCallExpression) {
			FunctionCallExpression fe = (FunctionCallExpression) otherArgument;
			return !fe.hasVariable(this);
		}
		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other)) {
			return true;
		}
		if (other instanceof Variable) {
			Variable otherVariable = (Variable) other;
			return this.symbol == otherVariable.symbol;
		}
		return false;
	}

	@Override
	public Expression shallowCopy() {
		return new Variable(this);
	}
}
