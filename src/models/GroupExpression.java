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
	public ArrayList<Expression> expressions;
	public ArrayList<LogicalOperator> operators;
	public ArrayList<FunctionCallExpression> funExpressions; // not to be
																// processed

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

	public GroupExpression(GroupExpression ge) {
		this.expressions = new ArrayList<>();
		for (Expression e : ge.expressions) {
			this.expressions.add(e.shallowCopy());
		}
		this.operators = new ArrayList<>(ge.operators);
		this.isNegated = ge.isNegated;
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
		result += 2; // brackets;
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

	public boolean hasMultipleIFFs() {
		return this.operators.indexOf(LogicalOperator.IFF) != this.operators
				.lastIndexOf(LogicalOperator.IFF);
	}

	public int getOperatorIndex(LogicalOperator op) {
		return this.operators.indexOf(op);
	}

	public Integer[] getOperatorIndecies(LogicalOperator op) {
		ArrayList<Integer> indecies = new ArrayList<>();
		for (int i = 0; i < this.operators.size(); i++) {
			if (operators.get(i) == op) {
				indecies.add(i);
			}
		}
		return indecies.toArray(new Integer[indecies.size()]);
	}

	public Expression getExpression(int i) {
		return this.expressions.get(i);
	}

	public void update(GroupExpression ge) {
		this.expressions = new ArrayList<>(ge.expressions);
		this.operators = new ArrayList<>(ge.operators);
	}

	@Override
	public Expression shallowCopy() {
		return new GroupExpression(this);
	}

	public void serialize() {
		funExpressions = new ArrayList<>();
		for (Expression e : this.expressions) {
			if (e instanceof FunctionCallExpression) {
				funExpressions.add((FunctionCallExpression) e);
			} else if (e instanceof GroupExpression) {
				((GroupExpression) e).serialize();
				funExpressions.addAll(((GroupExpression) e).funExpressions);
			}
		}
	}
}
