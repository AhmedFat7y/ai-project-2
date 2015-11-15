package classes;

public abstract class Argument {
	
	public static final int AGUMENT_PREDICATE = 1;
	public static final int AGUMENT_VARIABLE = 2;
	
	public final char SYMBOL;	
	public Argument(char symbol) {
		this.SYMBOL = symbol;
	}
	@Override
	public String toString() {
		return "" + SYMBOL;
	}
	abstract public int getNumberOfChars();
}
