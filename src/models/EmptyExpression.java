package models;

import enums.LogicalOperator;

public class EmptyExpression extends GroupExpression {

	public EmptyExpression() {
		this(LogicalOperator.NONE, null, false);
	}

	public EmptyExpression(LogicalOperator o) {
		this(o, null, false);

	}

	public EmptyExpression(LogicalOperator o, Expression e, boolean isNegated) {
		super(o, e, isNegated);
	}

	@Override
	public int getNumberOfChars() {
		if (operators.size() == 0) {
			return 0;
		}
		int result = 0;
		result += super.getNumberOfChars();
		return result;
	}

	@Override
	public String toString() {
		String result = getNegationChar();
		result += '(';
		result += super.toString();
		result += ')';
		return result;
	}
}
