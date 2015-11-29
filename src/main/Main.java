package main;

import models.Expression;
import parsers.Parser;

public class Main {

	public static void main(String[] args) throws Exception {
		// String u1 = "p(a,X,h(g(Z)))";
		// String u2 = "p(Z,h(Y),h(Y))";
		// FunctionCallExpression p1 = Parser.parseFunctionCall(u1);
		// FunctionCallExpression p2 = Parser.parseFunctionCall(u2);
		// System.out.println(p1);
		// System.out.println("Result == Input? " + p1.toString().equals(u1));
		// System.out.println(p2);
		// System.out.println("Result == Input? " + p2.toString().equals(u2));
		// Unifier u = new Unifier(p1, p2);
		// System.out.println("Unifiable? " + u.unify());
		// System.out.println(p1);
		// System.out.println(p2);
		// System.out.println(u.substitutes);
		String cnf1 = "∃x[P(x) ∧ ∀x[Q(x)]]";
		String cnf2 = "∀y∃x[P(x) ∧ G(y)]";
		String cnf3 = "∃x[P(x) ∧ ∀x[Q(x) ⇒ ¬P(x)]]";
		String cnf4 = "∀x[P(x) ⇔ (Q(x) ∧ ∃y[Q(y) ∧ R(y, x)])]";
		Expression e1 = Parser.parse(cnf1);
		Expression e2 = Parser.parse(cnf2);
		Expression e3 = Parser.parse(cnf3);
		Expression e4 = Parser.parse(cnf4);
		System.out.println("Input is: " + cnf1 + ", Output is: " + e1);
		System.out.println("Input is: " + cnf2 + ", Output is: " + e2);
		System.out.println("Input is: " + cnf3 + ", Output is: " + e3);
		System.out.println("Input is: " + cnf4 + ", Output is: " + e4);

		// System.out.println(Arrays.asList("x,g(x),g(f(a))".split("\\w[^\\(].*?,.*?[^\\)]")));
	}
}
