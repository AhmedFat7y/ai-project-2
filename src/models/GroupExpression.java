/**
 * 
 */
package models;

import java.util.ArrayList;

import parsers.SymbolsChecker;
import enums.LogicalOperator;

/**
 * @author MacBookAir
 *
 */
public class GroupExpression extends Expression {
	ArrayList<Expression> expressions;
	ArrayList<LogicalOperator> operators;

	/**
	 * 
	 */
	public GroupExpression() {
		this(LogicalOperator.NONE, null, false);
	}

	/**
	 * @param o
	 */
	public GroupExpression(LogicalOperator o) {
		this(o, null, false);
	}

	/**
	 * @param o
	 * @param e
	 * @param isNegated
	 */
	public GroupExpression(LogicalOperator o, Expression e, boolean isNegated) {
		super(o, e, isNegated);
		this.expressions = new ArrayList<>();
		this.operators = new ArrayList<>();
	}

	public String getNegationChar() {
		return super.toString();
	}

	public void addExpression(Expression e) {
		expressions.add(e);
	}

	public void addOperator(LogicalOperator op) {
		this.operators.add(op);
	}

	@Override
	public int getNumberOfChars() {
		int result = super.getNumberOfChars();
		result += operators.size();
		for (Expression e : this.expressions) {
			result += e.getNumberOfChars();
		}
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < operators.size(); i++) {
			Expression e = this.expressions.get(i);
			LogicalOperator op = this.operators.get(i);
			result += e.toString() + SymbolsChecker.getOperator(op);
		}
		result += this.expressions.get(this.expressions.size() - 1);
		return result;
	}

}
