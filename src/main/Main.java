package main;

import algorithms.CNFConverter;
import models.Expression;
import parsers.Parser;

public class Main {

	public static void main(String[] args) throws Exception {
		String cnf1 = "∃x[P(x) ∧ ∀x[Q(x)]]";
		String cnf2 = "∀y∃x[P(x) ∧ G(y)]";
		String cnf3 = "∃x[P(x) ∧ ∀x[Q(x) ⇒ ¬P(x)]]";
		String cnf4 = "∀x[P(x) ⇔ (Q(x) ∧ ∃y[Q(y) ∧ R(y, x)])]";
		Expression e1 = Parser.parse(cnf1);
		Expression e2 = Parser.parse(cnf2);
		Expression e3 = Parser.parse(cnf3);
		Expression e4 = Parser.parse(cnf4);
		System.out.println("Input is: " + cnf1 + ", Output is: " + e1);
		System.out.println("Are the same?: "
				+ cnf1.replace(" ", "").equals(e1.toString()));
		System.out.println("Input is: " + cnf2 + ", Output is: " + e2);
		System.out.println("Are the same?: "
				+ cnf2.replace(" ", "").equals(e2.toString()));
		System.out.println("Input is: " + cnf3 + ", Output is: " + e3);
		System.out.println("Are the same?: "
				+ cnf3.replace(" ", "").equals(e3.toString()));
		System.out.println("Input is: " + cnf4 + ", Output is: " + e4);
		System.out.println("Are the same?: "
				+ cnf4.replace(" ", "").equals(e4.toString()));
		CNFConverter cnfC1 = new CNFConverter(e1);
		CNFConverter cnfC2 = new CNFConverter(e2);
		CNFConverter cnfC3 = new CNFConverter(e3);
		CNFConverter cnfC4 = new CNFConverter(e4);
		cnfC1.convert();
		cnfC2.convert();
		cnfC3.convert();
		cnfC4.convert();
		// System.out.println(Arrays.asList("x,g(x),g(f(a))".split("\\w[^\\(].*?,.*?[^\\)]")));
	}
}
