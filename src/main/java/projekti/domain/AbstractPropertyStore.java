package projekti.domain;

import java.util.*;

/**
 * Object representing something that has properties.
 * The object has to define its properties.
 *
 * @author Rsl1122
 */
public abstract class AbstractPropertyStore {

    private final Map<Property, Object> propertyMap;

    public AbstractPropertyStore() {
        propertyMap = new HashMap<>();
    }

    public <T> void addProperty(Property<T> property, T object) {
        propertyMap.put(property, object);
    }

    public <T> Optional<T> getProperty(Property<T> property) {
        return Optional.ofNullable((T) propertyMap.get(property));
    }

    public abstract List<Property> getProperties();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPropertyStore that = (AbstractPropertyStore) o;
        return Objects.equals(propertyMap, that.propertyMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyMap);
    }
}
