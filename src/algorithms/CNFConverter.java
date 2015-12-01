package algorithms;

import java.util.ArrayList;

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
		System.out.println(expression);
		System.out.println("-- Going to remove IFFs");
		removeIFFs(this.expression);
		System.out.println(expression);
		System.out.println("-- Going to remove Implications");
	}

	public void removeIFFs(GroupExpression ge) {
		if (ge.hasMultipleIFFs()) {
			manageMultipleIFFs(ge);
		} else {
			manageSingleIFF(ge);
		}

	}

	private void manageSingleIFF(GroupExpression ge) {
		int operatorIndex = ge.getOperatorIndex(LogicalOperator.IFF);
		Expression e1 = ge.getExpression(operatorIndex);
		Expression e2 = ge.getExpression(operatorIndex + 1);
		ge.operators.add(operatorIndex, LogicalOperator.IMPLICATION);
		ge.operators.add(operatorIndex + 1, LogicalOperator.IMPLICATION);
		ge.expressions.add(operatorIndex + 2, e2);
		System.out.println(expression);
		ge.expressions.add(operatorIndex + 3, e1);

	}

	private void manageMultipleIFFs(GroupExpression ge) {
		// TODO Auto-generated method stub

	}

	public void removeImplications(GroupExpression ge) {
		// TODO Auto-generated method stub
	}

	public void pushNegation(GroupExpression ge) {
		// TODO Auto-generated method stub
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
