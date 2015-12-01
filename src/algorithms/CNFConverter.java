package algorithms;

import java.util.ArrayList;

import models.EmptyExpression;
import models.Expression;
import models.FunctionCallExpression;
import models.GroupExpression;
import enums.LogicalOperator;

public class CNFConverter {
	public GroupExpression expression;
	ArrayList<ArrayList<FunctionCallExpression>> cnfForm;

	public CNFConverter(Expression e) {
		this((GroupExpression) e);
	}

	public CNFConverter(GroupExpression ge) {
		this.expression = ge;
		this.cnfForm = new ArrayList<>();
	}

	public void convert() {
		System.out.println("========== Start Converting ==========");
		System.out.println("Prepare the expression: " + expression);
		prepareExpression(this.expression);
		System.out.println("Starting Expression: " + expression);
		removeIFFs(this.expression);
		System.out.println("After removing IFFs: " + this.expression);
		removeImplications(this.expression);
		System.out.println("After removing Implications: " + this.expression);
		pushNegation(expression);
		System.out.println("After pushing negation: " + this.expression);
	}

	private void prepareExpression(GroupExpression expression) {
		for (Expression e : expression.expressions) {
			if (e instanceof GroupExpression) {
				prepareExpression((GroupExpression) e);
			}
		}

		for (int i = 0; i < expression.operators.size() - 1; i++) {
			LogicalOperator op = expression.operators.remove(i);
			GroupExpression ge = new EmptyExpression();
			ge.addExpression(expression.expressions.remove(i));
			ge.addExpression(expression.expressions.remove(i));
			ge.addOperator(op);
			expression.expressions.add(i, ge);
		}
	}

	public void removeIFFs(GroupExpression ge) {
		// System.out.println("-- Start to remove IFFs");
		Integer[] operatorIndecies = ge
				.getOperatorIndecies(LogicalOperator.IFF);
		for (int operatorIndex : operatorIndecies) {
			_removeIFF(ge, operatorIndex);
		}
		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				removeIFFs((GroupExpression) e);
			}
		}
	}

	private void _removeIFF(GroupExpression ge, int operatorIndex) {
		Expression e1 = ge.getExpression(operatorIndex);
		Expression e2 = ge.getExpression(operatorIndex + 1);
		EmptyExpression ge1 = new EmptyExpression();
		EmptyExpression ge2 = new EmptyExpression();

		ge.operators.remove(operatorIndex);
		ge.expressions.remove(operatorIndex);
		ge.expressions.remove(operatorIndex);

		ge.expressions.add(operatorIndex, ge1);
		ge.expressions.add(operatorIndex, ge2);
		ge.operators.add(operatorIndex, LogicalOperator.AND);

		ge1.addExpression(e1.shallowCopy());
		ge1.addExpression(e2.shallowCopy());
		ge1.addOperator(LogicalOperator.IMPLICATION);

		ge2.addExpression(e2.shallowCopy());
		ge2.addExpression(e1.shallowCopy());
		ge2.addOperator(LogicalOperator.IMPLICATION);

	}

	public void removeImplications(GroupExpression ge) {
		// System.out.println("-- Start to remove Implications");
		Integer[] operatorIndecies = ge
				.getOperatorIndecies(LogicalOperator.IMPLICATION);
		for (int operatorIndex : operatorIndecies) {
			_removeImplication(ge, operatorIndex);
		}
		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				removeImplications((GroupExpression) e);
			}
		}
	}

	private void _removeImplication(GroupExpression ge, int operatorIndex) {
		Expression e1 = ge.getExpression(operatorIndex);
		e1.isNegated = !e1.isNegated;
		ge.operators.set(operatorIndex, LogicalOperator.OR);

	}

	public void pushNegation(GroupExpression ge) {

		if (ge.isNegated) {
			_pushNegation(ge);
		}

		for (Expression e : ge.expressions) {
			if (e instanceof GroupExpression) {
				pushNegation((GroupExpression) e);
			}
		}
	}

	private void _pushNegation(GroupExpression ge) {
		if (ge instanceof QuantifiedExpression) {
			if (ge.operators.get(0) == Quantifier.THERE_EXISTS) {
				ge.operators.set(0, Quantifier.FOR_ALL);
			} else if(ge.operators.get(0) == Quantifier.FOR_ALL) {
				ge.operators.set(0, Quantifier.THERE_EXISTS);
			}
			for (int i = 0; i < ge.operators.size; i++) {
				ge.operators.get(i).negate();
			}
			ge.negate();
		}
		else {
			if (ge.operators.get(0) == LogicalOperator.AND) {
				ge.operators.set(0, LogicalOperator.OR);
			} else if (ge.operators.get(0) == LogicalOperator.OR) {
				ge.operators.set(0, LogicalOperator.AND);
			}else {
				System.err.println("NOOO PLEASE NO OMG STOP YOU #@$#@$!#");
				return;
			}
			ge.expressions.get(0).negate();
			ge.expressions.get(1).negate();
			ge.negate();
		}
	}

	public void standardize(GroupExpression ge) {
		// TODO Auto-generated method stub
	}

	public void skolemize(GroupExpression ge) {
		// TODO Auto-generated method stub
	}

	public void flatten(GroupExpression ge) {
		// TODO Auto-generated method stub
	}
}
