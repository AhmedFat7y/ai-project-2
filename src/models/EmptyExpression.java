package models;

import enums.LogicalOperator;

public class EmptyExpression extends Expression {

	public EmptyExpression() {
		// TODO Auto-generated constructor stub
	}

	public EmptyExpression(LogicalOperator o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	public EmptyExpression(LogicalOperator o, Expression e, boolean isNegated) {
		super(o, e, isNegated);
		// TODO Auto-generated constructor stub
	}

}
