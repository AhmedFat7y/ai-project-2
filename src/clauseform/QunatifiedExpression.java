package clauseform;

import enums.LogicalOperator;
import enums.Quantifier;

public class QunatifiedExpression extends Expression {
	public Quantifier quantifier;
	public QunatifiedExpression(Quantifier q) {
		super();
		this.quantifier = q;
	}

	public QunatifiedExpression(LogicalOperator o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	public QunatifiedExpression(LogicalOperator o, Expression e,
			boolean isNegated) {
		super(o, e, isNegated);
		// TODO Auto-generated constructor stub
	}

}
