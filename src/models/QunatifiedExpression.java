package models;

import enums.LogicalOperator;
import enums.Quantifier;

public class QunatifiedExpression extends Expression {
	public Quantifier quantifier;
	public char[] symbols;
	public QunatifiedExpression(Quantifier q, char[] symbols) {
		super();
		this.quantifier = q;
		this.symbols = symbols;
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
