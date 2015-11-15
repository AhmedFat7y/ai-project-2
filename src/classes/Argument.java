package classes;

public abstract class Argument {

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
