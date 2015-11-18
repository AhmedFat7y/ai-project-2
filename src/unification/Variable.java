package unification;

public class Variable extends Argument {
	public boolean isConstant;

	public Variable(char symbol) {
		super(symbol);
		isConstant = Character.isUpperCase(symbol);
	}

	@Override
	public int getNumberOfChars() {
		// TODO Auto-generated method stub
		return 2;
	}

	/**
	 * checks if otherArgument is variable then it compares symbols checks if
	 * otherArgument is predicate then it checks if this predicate contains this
	 * variable if it contains this variable then returns false;
	 * **/
	@Override
	public boolean match(Argument otherArgument) {
		if (otherArgument instanceof Variable) {
			return super.match(otherArgument);
		} else if (otherArgument instanceof Predicate) {
			Predicate p = (Predicate) otherArgument;
			return !p.hasVariable(SYMBOL);
		}
		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Variable) {
			Variable otherVariable = (Variable) other;
			return this.SYMBOL == otherVariable.SYMBOL;
		}

		return false;
	}
}
