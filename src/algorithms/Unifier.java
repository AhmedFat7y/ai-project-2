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
			UnificationPair pair = new UnificationPair(arg1, arg2);
			if (!pair.isValid()) {
				return false;
			} else if (pair.areEquals()) {
				continue;
			} else if (pair.canSubstitute()) {
				if (!substitute(pair)) {
					return false;
				}
			} else if (pair.arePredicates()) {
				if (!_unify((Predicate) arg1, (Predicate) arg2)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean substitute(UnificationPair pair) {
		if (substitutes.containsKey(pair.arg1)) {
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
	public Argument arg1;
	public Argument arg2;

	// handle case 1, 4
	public UnificationPair(Argument arg1, Argument arg2) {
		this.arg1 = arg1 instanceof Variable ? arg1 : arg2;
		this.arg2 = arg1 instanceof Variable ? arg2 : arg1;
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