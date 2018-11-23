package projekti.domain;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Generics "Key" object that also allows validating given properties.
 *
 * @param <T> Type of the property this key gives.
 * @author Rsl1122
 */
public class Property<T> {

    private final String name;
    private final Class<T> type;
    private final Predicate<T> validator;

    /**
     * Create a new Property.
     *
     * @param name      Name of the property.
     * @param type      Type of the property.
     * @param validator Validation {@link Predicate} for the property.
     */
    public Property(String name, Class<T> type, Predicate<T> validator) {
        this.name = name;
        this.type = type;
        this.validator = validator;
    }

    /**
     * Create a new Property without validation.
     * <p>
     * isValid will return true always if created using this method.
     *
     * @param name Name of the property.
     * @param type Type of the property.
     */
    public Property(String name, Class<T> type) {
        this(name, type, property -> true);
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    /**
     * Check if a given object is valid for this property.
     *
     * @param givenProperty Object to check validity of.
     * @return true if valid. Please note that a Property without validator will always return true.
     */
    public boolean isValid(T givenProperty) {
        return validator.test(givenProperty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property<?> property = (Property<?>) o;
        return Objects.equals(name, property.name) &&
                Objects.equals(type, property.type) &&
                Objects.equals(validator, property.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, validator);
    }
}
