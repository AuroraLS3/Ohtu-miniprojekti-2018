package projekti.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CheckTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void notNullDoesNotThrowExeptionWhenNotNull() {
        Check.notNull("", () -> new IllegalStateException("Test failed"));
    }

    @Test
    public void notNullThrowsExceptionWhenNull() {
        expected.expect(IllegalStateException.class);
        expected.expectMessage("Expected");
        Check.notNull(null, () -> new IllegalStateException("Expected"));
    }

    @Test
    public void isTrueDoesNotThrowExeptionWhenTrue() {
        Check.isTrue(true, () -> new IllegalStateException("Test failed"));
    }

    @Test
    public void isTrueThrowsExeptionWhenFalse() {
        expected.expect(IllegalStateException.class);
        expected.expectMessage("Expected");
        Check.isTrue(false, () -> new IllegalStateException("Expected"));
    }

    @Test
    public void isFalseDoesNotThrowExeptionWhenFalse() {
        Check.isFalse(false, () -> new IllegalStateException("Test failed"));
    }

    @Test
    public void isFalseThrowsExeptionWhenTrue() {
        expected.expect(IllegalStateException.class);
        expected.expectMessage("Expected");
        Check.isFalse(true, () -> new IllegalStateException("Expected"));
    }

}