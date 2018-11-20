package projekti.UI;

import java.util.Scanner;

/*
 * Handle input and output to Sys.in.
 */
public class TextIO implements IO{
	private Scanner scanner;
	/**
	 * Create TextIO object.
	 */
	public TextIO() {
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * "SOUT".
	 * @param s string to print.
	 */
	@Override
	public void print(String s) {
		System.out.print(s);
	}
	
	/**
	 * Get input from Sys.in.
	 * @return String representing input.
	 */
	@Override
	public String GetInput() {
		String value = "";
		value = scanner.nextLine();
		return value;
	}


}
