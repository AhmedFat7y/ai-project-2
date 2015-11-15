package main;

import parsers.PredicateParser;
import classes.Predicate;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("hello world!");
		String input = "P(x,g(x),g(f(a,g(b),g(e))))";
		Predicate p1 = PredicateParser.parse(input);
		System.out.println(p1);
		System.out.println("Result == Input? " + p1.toString().equals(input));		
//		System.out.println(Arrays.asList("x,g(x),g(f(a))".split("\\w[^\\(].*?,.*?[^\\)]")));
	}
}
