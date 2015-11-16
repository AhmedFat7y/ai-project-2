package clauseform;

import enums.LogicalOperator;

public class Expression {
	public LogicalOperator operator;
	public Expression otherExpression;
	public boolean isNegated;
	
	public Expression() {
		this(LogicalOperator.NONE);
	}
	
	public Expression(LogicalOperator o) {
		this(o, null, false);
	}
	
	public Expression(LogicalOperator o, Expression e, boolean isNegated) {
		this.operator = o;
		this.otherExpression = e;
		this.isNegated = isNegated;
	}
}