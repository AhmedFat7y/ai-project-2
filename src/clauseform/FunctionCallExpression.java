package clauseform;

import enums.LogicalOperator;

public class FunctionCallExpression extends Expression {
	public String functionCall;

	public FunctionCallExpression(String funCall) {
		super();
		this.functionCall = funCall;
	}

	public FunctionCallExpression(LogicalOperator o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	public FunctionCallExpression(LogicalOperator o, Expression e,
			boolean isNegated) {
		super(o, e, isNegated);
		// TODO Auto-generated constructor stub
	}

}
