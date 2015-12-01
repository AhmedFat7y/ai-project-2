package models;

import java.util.ArrayList;
import java.util.Arrays;

import parsers.SymbolsChecker;
import enums.LogicalOperator;
import enums.Quantifier;

public class QuantifiedExpression extends GroupExpression {
	ArrayList<QuantifierWrapper> quantifiers;

	public QuantifiedExpression() {
		this(LogicalOperator.NONE, null, false);
	}

	public QuantifiedExpression(QuantifiedExpression qe) {
		super(qe);
		this.quantifiers = new ArrayList<>();
		for (QuantifierWrapper qw : qe.quantifiers) {
			this.quantifiers.add(new QuantifierWrapper(qw));
		}
	}

	public QuantifiedExpression(LogicalOperator o) {
		this(o, null, false);
	}

	public QuantifiedExpression(LogicalOperator o, Expression e,
			boolean isNegated) {
		super(o, e, isNegated);
		this.quantifiers = new ArrayList<>();
	}

	public void addQuantifier(Quantifier q, char... symbol) {
		quantifiers.add(new QuantifierWrapper(q, symbol));
	}

	public void addSymbol(char symbol) {
		quantifiers.get(quantifiers.size() - 1).addSymbol(symbol);
	}

	@Override
	public String toString() {

		String result = getNegationChar();
		for (QuantifierWrapper qw : quantifiers) {
			result += SymbolsChecker.getQuantifier(qw.quantifier);
			String symbolsStr = Arrays.toString(qw.symbols);
			result += symbolsStr.substring(1, symbolsStr.length() - 1).replace(
					' ', '\0');
		}
		result += '[';
		result += super.toString();
		result += ']';
		return result;
	}

	@Override
	public int getNumberOfChars() {
		// for opened & closed square bracket
		int result = 0;
		result += quantifiers.size();
		for (QuantifierWrapper qw : quantifiers) {
			result += (qw.symbols.length * 2) - 1;
		}
		result += super.getNumberOfChars();
		return result;
	}

	private class QuantifierWrapper {
		public Quantifier quantifier;
		public char[] symbols;

		public QuantifierWrapper(QuantifierWrapper qw) {
			this.quantifier = qw.quantifier;
			this.symbols = Arrays.copyOf(qw.symbols, qw.symbols.length);
		}

		public QuantifierWrapper(Quantifier q, char... c) {
			this.quantifier = q;
			this.symbols = c;
		}

		public void addSymbol(char c) {
			this.symbols = Arrays.copyOf(this.symbols, this.symbols.length + 1);
			this.symbols[this.symbols.length - 1] = c;
		}
	}// end QuantifierWrapper

	@Override
	public Expression shallowCopy() {
		return new QuantifiedExpression(this);
	}

	public boolean hasExistential() {
		for (QuantifierWrapper qw : quantifiers) {
			if (qw.quantifier == Quantifier.THERE_EXISTS) {
				return true;
			}
		}
		return false;
	}

	public boolean hasUniversal() {
		for (QuantifierWrapper qw : quantifiers) {
			if (qw.quantifier == Quantifier.FOR_ALL) {
				return true;
			}
		}
		return false;
	}

	public Character[] getExistentialQuantifiersSymbols() {
		ArrayList<Character> eq = new ArrayList<>();
		for (int i = 0; i < quantifiers.size(); i++) {
			QuantifierWrapper qw = quantifiers.get(i);
			if (qw.quantifier == Quantifier.THERE_EXISTS) {
				for (char s : qw.symbols) {
					eq.add(s);
				}
			}
		}
		return eq.toArray(new Character[eq.size()]);
	}

	public Character[] getUniversalQuantifiersSymbols() {
		ArrayList<Character> eq = new ArrayList<>();
		for (int i = 0; i < quantifiers.size(); i++) {
			QuantifierWrapper qw = quantifiers.get(i);
			if (qw.quantifier == Quantifier.FOR_ALL) {
				for (char s : qw.symbols) {
					eq.add(s);
				}
			}
		}
		return eq.toArray(new Character[eq.size()]);
	}

	public Character[] getQuantifiersSymobls() {
		ArrayList<Character> eq = new ArrayList<>();
		for (int i = 0; i < quantifiers.size(); i++) {
			QuantifierWrapper qw = quantifiers.get(i);
			for (char s : qw.symbols) {
				eq.add(s);
			}
		}
		return eq.toArray(new Character[eq.size()]);
	}

	public void replaceQuantifierSymbol(char toBeReplaced, char replacement) {
		for (int i = 0; i < quantifiers.size(); i++) {
			QuantifierWrapper qw = quantifiers.get(i);
			for (int j = 0; j < qw.symbols.length; j++) {
				if (qw.symbols[j] == toBeReplaced) {
					qw.symbols[j] = replacement;
				}
			}
		}
	}
}
