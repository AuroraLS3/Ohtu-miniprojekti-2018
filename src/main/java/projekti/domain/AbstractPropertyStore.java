package projekti.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

}
