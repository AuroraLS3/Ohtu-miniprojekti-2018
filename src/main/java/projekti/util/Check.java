package projekti.util;

import java.util.function.Supplier;

/**
 * Utility class for various argument checks.
 *
 * @author Rsl1122
 */
public class Check {

    private Check() {
        /* Hide constructor, static method class should not be constructed. */
    }

    /**
     * Check if an object is null and throw a throwable if it is.
     *
     * @param object            Object to check.
     * @param exceptionSupplier Supplier for a throwable to use.
     * @param <T>               Type of the throwable.
     * @throws T Throwable supplied by the supplier if the object is null.
     */
    public static <T extends Throwable> void notNull(Object object, Supplier<T> exceptionSupplier) throws T {
        isFalse(object == null, exceptionSupplier);
    }

    /**
     * Check if a condition is true and throw a throwable if false.
     *
     * @param condition         boolean to check.
     * @param exceptionSupplier Supplier for a throwable to use.
     * @param <T>               Type of the throwable.
     * @throws T Throwable supplied by the supplier if false.
     */
    public static <T extends Throwable> void isTrue(boolean condition, Supplier<T> exceptionSupplier) throws T {
        if (!condition) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Check if a condition is false and throw a throwable if true.
     *
     * @param condition         boolean to check.
     * @param exceptionSupplier Supplier for a throwable to use.
     * @param <T>               Type of the throwable.
     * @throws T Throwable supplied by the supplier if true.
     */
    public static <T extends Throwable> void isFalse(boolean condition, Supplier<T> exceptionSupplier) throws T {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }
}
