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

    public static <T extends Throwable> void notNull(Object object, Supplier<T> exceptionSupplier) throws T {
        isFalse(object == null, exceptionSupplier);
    }

    public static <T extends Throwable> void isTrue(boolean condition, Supplier<T> exceptionSupplier) throws T {
        if (!condition) {
            throw exceptionSupplier.get();
        }
    }

    public static <T extends Throwable> void isFalse(boolean condition, Supplier<T> exceptionSupplier) throws T {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }
}
