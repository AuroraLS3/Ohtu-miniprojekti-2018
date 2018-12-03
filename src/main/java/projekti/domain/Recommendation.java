package projekti.domain;

import java.util.List;
import java.util.Optional;

/**
 * Common interface for all recommendations.
 *
 * @author Rsl1122
 */
public interface Recommendation {

    List<Property> getProperties();

    <T> void addProperty(Property<T> property, T value);

    <T> Optional<T> getProperty(Property<T> property);

    String toString();

    String toStringWithDescription();

    String getType();

}
