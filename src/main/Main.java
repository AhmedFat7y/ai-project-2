package main;

import algorithms.Unifier;
import parsers.PredicateParser;
import unification.Predicate;

public class Main {

	public static void main(String[] args) {
		System.out.println("hello world!");
		String u1 = "p(a,X,h(g(Z)))";
		String u2 = "p(Z,h(Y),h(Y))";
		Predicate p1 = PredicateParser.parse(u1);
		Predicate p2 = PredicateParser.parse(u2);
		System.out.println(p1);
		System.out.println("Result == Input? " + p1.toString().equals(u1));
		System.out.println(p2);
		System.out.println("Result == Input? " + p2.toString().equals(u2));
		Unifier u = new Unifier(p1, p2);
		System.out.println("Unifiable? " + u.unify());
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(u.substitutes);
		// System.out.println(Arrays.asList("x,g(x),g(f(a))".split("\\w[^\\(].*?,.*?[^\\)]")));
	}
}
