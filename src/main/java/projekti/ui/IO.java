package projekti.ui;

public interface IO {
    /**
     * Print to output.
     *
     * @param s The string to print.
     */
    void print(String s);

    default void println() {
        print("\n");
    }

    default void println(String s) {
        print(s);
        print("\n");
    }

    /**
     * Get input.
     *
     * @return String input.
     */
    String getInput();

}
