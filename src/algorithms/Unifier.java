package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import models.Argument;
import models.FunctionCallExpression;
import models.Variable;

public class Unifier {

	public FunctionCallExpression functionExpression1;
	public FunctionCallExpression functionExpression2;
	// public ArrayList<UnificationPair> pairs;
	public HashMap<Argument, Argument> unifiers;

	public Unifier(FunctionCallExpression FunctionCallExpression1,
			FunctionCallExpression FunctionCallExpression2) {
		this.functionExpression1 = FunctionCallExpression1;
		this.functionExpression2 = FunctionCallExpression2;
		// this.pairs = new ArrayList<>();
		unifiers = new HashMap<>();
	}

	public boolean unify() {
		ArrayList<UnificationPair> pairs = new ArrayList<>();
		pairs.add(new UnificationPair(functionExpression1, functionExpression2));
		return _unify2(pairs);
		// return _unify(FunctionCallExpression1, FunctionCallExpression2) ;
	}

	private boolean _unify2(ArrayList<UnificationPair> pairs) {
		System.out.println("Unify these pairs: " + pairs);
		for (UnificationPair pair : pairs) {
			if (!pair.isValid()) {
				System.out.println("-- This Pair is not valid: " + pair);
				System.out.println("-- This Pair couldn't be unified " + pair);
				return false;
			} else if (pair.areEqual()) {
				System.out.println("This Pair is Equal: " + pair);
				System.out.println("Skip");
				continue;
			} else if (pair.areFunctionCallExpressions()) {
				System.out.println("-- Unify Functions:  " + pair);
				FunctionCallExpression fe1 = (FunctionCallExpression) pair.arg1;
				FunctionCallExpression fe2 = (FunctionCallExpression) pair.arg2;
				if (!_unify2(pairTerms(fe1.arguments, fe2.arguments))) {
					System.out.println("-- Functions couldn't be Unified: "
							+ pair);
					return false;
				}
			} else if (isAlreadyUnified(pair.arg1)) {
				System.out.println("-- Pair is already unified: " + pair);
				System.out.println("-- Unifiers List: " + unifiers);
				Argument replacement = unifiers.get(pair.arg1);
				pair.update(replacement);
				return _unify2(pair.getListWrapper());
			} else if (pair.canSubstitute()) {
				substitute(pair);
				if (isUnifierValuIncludeKey()) {
					return false;
				}
			}
		}// end foreach
		return true;
	}// end _unify2

	private void substituteInUnifiers(UnificationPair pair) {
		for (Argument value : unifiers.values()) {
			if (value instanceof FunctionCallExpression) {
				FunctionCallExpression fe = (FunctionCallExpression) value;
				System.out.println("------ Argument: " + fe + " is a function");
				if (fe.hasVariable(pair.arg1)) {
					System.out.println("------ It has " + pair.arg1
							+ " in its arguments");
					fe.substitute(pair.arg1, pair.arg2);
				} else {
					System.out.println("------ It doesnt have " + pair.arg1
							+ " in its arguments");
				}
			}
		}
	}// end susbstituteInUnifiers

	private void substituteFromUnifiers(UnificationPair pair) {
		for (Argument key : unifiers.keySet()) {
			Variable v = (Variable) key;
			System.out.println("------ Argument: " + v + " is a variable");
			if (pair.arg2 instanceof FunctionCallExpression) {
				System.out.println("------ It doesnt exist in " + pair.arg2);
				FunctionCallExpression fe = (FunctionCallExpression) pair.arg2;
				if (fe.hasVariable(key)) {
					System.out.println("------ It exists in " + pair.arg2);
					fe.substitute(key, unifiers.get(key));
				} else {
					System.out
							.println("------ It doesnt exist in " + pair.arg2);
				}
			}
		}

	}// end substituteFromUnifiers

	// private boolean _unify(FunctionCallExpression f1, FunctionCallExpression
	// f2) {
	// for (int i = 0; i < f1.arguments.size(); i++) {
	// Argument arg1 = f1.arguments.get(i), arg2 = f2.arguments.get(i);
	// System.out.println("match: " + arg1 + ", " + arg2);
	// UnificationPair pair = new UnificationPair(arg1, arg2);
	// if (!pair.isValid()) {
	// System.out.println("args are not valid pair");
	// System.out.println("unifying args failed");
	// return false;
	// } else if (pair.areEqual()) {
	// System.out.println("args are equal");
	// continue;
	// } else if (pair.canSubstitute()) {
	// System.out.println("can be substituted");
	// if (!substitute(pair)) {
	// System.out.println("unifying args failed");
	// return false;
	// }
	// } else if (pair.areFunctionCallExpressions()) {
	// System.out.println("args are both FunctionCallExpressions");
	// if (!_unify((FunctionCallExpression) arg1,
	// (FunctionCallExpression) arg2)) {
	// System.out.println("unifying args failed");
	// return false;
	// }
	// } else {
	// System.out.println("sth wrong happened");
	// }
	// }
	// return true;
	// }

	private boolean isAlreadyUnified(Argument arg) {
		if (unifiers.containsKey(arg)) {
			System.out.println("---- " + arg + " is in keys: " + unifiers);
			return true;
		}
		System.out.println("---- " + arg + " is not in keys: " + unifiers);
		return false;
	}

	private boolean substitute(UnificationPair pair) {
		substituteInUnifiers(pair);
		substituteFromUnifiers(pair);

		unifiers.put(pair.arg1, pair.arg2);
		System.out.println("---- Added pair to unifiers: " + pair);
		System.out.println("---- Unifiers after update: " + unifiers);
		return true;
	}

	private boolean isUnifierValuIncludeKey() {
		for (Argument key : unifiers.keySet()) {
			if (unifiers.get(key) instanceof FunctionCallExpression) {
				FunctionCallExpression fe = (FunctionCallExpression)unifiers.get(key);
				if(fe.hasVariable(key)) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<UnificationPair> pairTerms(ArrayList<Argument> args1,
			ArrayList<Argument> args2) {
		ArrayList<UnificationPair> pairs = new ArrayList<>();
		for (int i = 0; i < args1.size(); i++) {
			Argument arg1 = args1.get(i);
			Argument arg2 = args2.get(i);
			pairs.add(new UnificationPair(arg1, arg2));
		}
		return pairs;
	}

	class UnificationPair {
		public Argument arg1;// to be replaced
		public Argument arg2; // replacement

		// handle case 1, 4
		public UnificationPair(Argument arg1, Argument arg2) {
			// String arg1Str = arg1 == null ? "N/A" : arg1.toString();
			// String arg2Str = arg2 == null ? "N/A" : arg2.toString();
			// if(arg1Str.equals("a") && arg2Str.equals("u")) {
			// System.out.println("yo");
			// }
			// System.out.println("------ " + arg1 + " / " + arg2);
			if (arg1 instanceof Variable && ((Variable) arg1).isConstant) {
				this.arg1 = arg2;
				this.arg2 = arg1;
			} else if (arg2 instanceof Variable && ((Variable) arg2).isConstant) {
				this.arg1 = arg1;
				this.arg2 = arg2;
			} else {
				this.arg1 = arg1 instanceof Variable ? arg1 : arg2;
				this.arg2 = arg1 instanceof Variable ? arg2 : arg1;
			}
			// System.out.println("-------- " + arg1 + " / " + arg2);
		}// end constructor

		public ArrayList<UnificationPair> getListWrapper() {
			ArrayList<UnificationPair> temp = new ArrayList<>();
			temp.add(this);
			return temp;
		}

		public void update(Argument arg1) {
			this.arg1 = arg1;

		}

		// handle cases 6, 2
		// TODO revise this method
		public boolean isValid() {
			return arg1.match(arg2);
		}

		// handle case 3
		public boolean areEqual() {
			return arg1.equals(arg2);
		}

		// handle case 5
		// TODO revise this method
		// check this method after the previous ones and you will be always able
		// to substitute;
		public boolean canSubstitute() {
			return true;
		}

		public boolean areFunctionCallExpressions() {
			return arg1 instanceof FunctionCallExpression
					&& arg2 instanceof FunctionCallExpression;
		}

		@Override
		public String toString() {
			String arg1Str = arg1 == null ? "N/A" : arg1.toString();
			String arg2Str = arg2 == null ? "N/A" : arg2.toString();
			// if(arg1Str.equals("u") && arg2Str.equals("a")) {
			// System.out.println("yo");
			// }
			return arg1Str + " = " + arg2Str;
		}
	}// end class unificationPair
}// end class Unifier