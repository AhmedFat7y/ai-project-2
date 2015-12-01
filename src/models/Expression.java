package models;

import enums.LogicalOperator;

public abstract class Expression {
	// public LogicalOperator operator;
	// public Expression otherExpression;
	public boolean isNegated;

	public Expression() {
		this(LogicalOperator.NONE);
	}

	public Expression(LogicalOperator o) {
		this(o, null, false);
	}

	public Expression(LogicalOperator o, Expression e, boolean isNegated) {
		// this.operator = o;
		// this.otherExpression = e;
		this.isNegated = isNegated;
	}

	public int getNumberOfChars() {
		return isNegated ? 1 : 0;
	}

	@Override
	public String toString() {
		return isNegated ? "¬" : "";
	}

	public abstract Expression shallowCopy();

	public void negate() {
		this.isNegated = !this.isNegated;
	}
}