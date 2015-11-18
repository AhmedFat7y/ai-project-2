package unification;

public abstract class Argument {

	public final char SYMBOL;

	public Argument(char symbol) {
		this.SYMBOL = symbol;
	}

	@Override
	public String toString() {
		return "" + SYMBOL;
	}

	// handle case 2, 5, 6
	public boolean match(Argument otherArgument) {
		return this.SYMBOL == otherArgument.SYMBOL;
	}
	
	@Override
	public boolean equals(Object other) {
		Argument otherArgument = (Argument) other;
		return this.SYMBOL == otherArgument.SYMBOL;
	}

	abstract public int getNumberOfChars();
}
