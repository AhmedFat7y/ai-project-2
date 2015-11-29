package models;

import enums.LogicalOperator;

public abstract class Argument extends Expression{

	public char symbol;

	public Argument(char symbol) {
		super();
		this.symbol = symbol;
	}

	public Argument(LogicalOperator o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	public Argument(LogicalOperator o, Expression e,
			boolean isNegated) {
		super(o, e, isNegated);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return super.toString() + symbol;
	}

	// handle case 2, 5, 6
	public boolean match(Argument otherArgument) {
		return this.symbol == otherArgument.symbol;
	}
	
	@Override
	public boolean equals(Object other) {
		Argument otherArgument = (Argument) other;
		return this.symbol == otherArgument.symbol;
	}
}
