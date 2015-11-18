package algorithms;

import java.util.HashMap;

import unification.Argument;
import unification.Predicate;
import unification.Variable;

public class Unifier {

	public Predicate predicate1;
	public Predicate predicate2;
	// public ArrayList<UnificationPair> pairs;
	public HashMap<Argument, Argument> substitutes;

	public Unifier(Predicate predicate1, Predicate predicate2) {
		this.predicate1 = predicate1;
		this.predicate2 = predicate2;
		// this.pairs = new ArrayList<>();
		substitutes = new HashMap<>();
	}

	public boolean unify() {
		return _unify(predicate1, predicate2);

	}

	private boolean _unify(Predicate predicate1, Predicate predicate2) {
		for (int i = 0; i < predicate1.arguments.size(); i++) {
			Argument arg1 = predicate1.arguments.get(i), arg2 = predicate2.arguments
					.get(i);
			System.out.println("match: " + arg1 + ", " + arg2);
			UnificationPair pair = new UnificationPair(arg1, arg2);
			if (!pair.isValid()) {
				System.out.println("args are not valid pair");
				System.out.println("unifying args failed");
				return false;
			} else if (pair.areEquals()) {
				System.out.println("args are equal");
				continue;
			} else if (pair.canSubstitute()) {
				System.out.println("can be substituted");
				if (!substitute(pair)) {
					System.out.println("unifying args failed");
					return false;
				}
			} else if (pair.arePredicates()) {
				System.out.println("args are both predicates");
				if (!_unify((Predicate) arg1, (Predicate) arg2)) {
					System.out.println("unifying args failed");
					return false;
				}
			} else {
				System.out.println("sth wrong happened");
			}
		}
		return true;
	}

	private boolean substitute(UnificationPair pair) {
		if (substitutes.containsKey(pair.arg1)) {
			System.out.println(pair.arg1 + " already substitued");
			System.out.println("substitution failed");
			return false;
		}
		substitutes.put(pair.arg1, pair.arg2);
		this.predicate1.substitute(pair.arg1, pair.arg2);
		this.predicate2.substitute(pair.arg1, pair.arg2);
		return true;
	}
	
	// public boolean unify() {
	// pairTerms();
	// for (UnificationPair pair : new ArrayList<>(pairs)) {
	// if (!pair.isValid()) {
	// return false;
	// } else if (pair.areEquals()) {
	// pairs.remove(pair);
	// } else if (pair.canSubstitute()) {
	//
	// }
	// }
	// return false;
	// }
	//
	//
	// private void pairTerms() {
	// _pairTerms(predicate1, predicate2);
	// }
	//
	// private void _pairTerms(Predicate p1, Predicate p2) {
	// for (int i = 0; i < p1.arguments.size(); i++) {
	// Argument arg1 = p1.arguments.get(i), arg2 = p2.arguments.get(i);
	// if (arg1 instanceof Predicate && arg2 instanceof Predicate) {
	// _pairTerms((Predicate) arg1, (Predicate) arg2);
	// } else {
	// pairs.add(new UnificationPair(arg1, arg2));
	// }
	// }
	// }
	//
	// private boolean _unify(Argument arg1, Argument arg2) {
	//
	// return false;
	// }
}

class UnificationPair {
	public Argument arg1;// to be replaced
	public Argument arg2; // replacement

	// handle case 1, 4
	public UnificationPair(Argument arg1, Argument arg2) {
		if (arg1 instanceof Variable && ((Variable) arg1).isConstant) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		} else if (arg2 instanceof Variable && ((Variable) arg2).isConstant) {
			this.arg1 = arg2;
			this.arg2 = arg1;
		} else {
			this.arg1 = arg1 instanceof Variable ? arg1 : arg2;
			this.arg2 = arg1 instanceof Variable ? arg2 : arg1;
		}

	}

	// handle cases 6, 2,
	public boolean isValid() {
		return arg2 instanceof Variable || arg2.match(arg1);
	}

	// handle case 3
	public boolean areEquals() {
		return arg1 instanceof Variable && arg1.equals(arg2);
	}

	// handle case 5
	public boolean canSubstitute() {
		return arg1 instanceof Variable
				&& (!(arg2 instanceof Predicate) || arg1.match(arg2));
	}

	public boolean arePredicates() {
		return arg1 instanceof Predicate && arg2 instanceof Predicate;
	}
}