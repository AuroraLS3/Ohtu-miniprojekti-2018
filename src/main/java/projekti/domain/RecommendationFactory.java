package projekti.domain;

import projekti.util.Check;

import java.util.List;
import java.util.function.Function;

/**
 * Factory for creating different Recommendation objects.
 * <p>
 * Correct usage:
 * {@code
 * recommendation = RecommendationFactory.create()
 * .selectType(String)
 * .whileMissingProperties(property -> code that asks for a value from user)
 * .build()
 * }
 *
 * @author Rsl1122
 */
public class RecommendationFactory {

    private List<Property> typeSpecificProperties;
    private int typeSpecificPropertiesSize;
    private int currentlyRequesting;

    private Recommendation createdObject;

    private RecommendationFactory() {
        /* Hide constructor to force call to RecommendationFactory.create() */
    }

    /**
     * Creates a new RecommendationFactory.
     *
     * @return a new RecommendationFactory.
     */
    public static RecommendationFactory create() {
        return new RecommendationFactory();
    }

    private List<Property> getTypeSpecificProperties(String type) {
        switch (type) {
            case "BOOK":
                return Book.Properties.getAll();
            case "BLOG":
                return Blog.Properties.getAll();
            case "OTHER":
                return Other.Properties.getAll();
            default:
                throw new IllegalArgumentException("Invalid recommendation type given: '" + type + "'");
        }
    }

    private Recommendation createObject(String type) {
        switch (type) {
            case "BOOK":
                return new Book();
            case "BLOG":
                return new Blog();
            case "OTHER":
            default: // Default case is never reached as above case throws exception.
                return new Other();
        }
    }

    /**
     * Call this method to select the type of recommendation.
     *
     * @param type Type given by the user.
     * @return This factory.
     * @throws IllegalArgumentException If invalid type is given.
     */
    public RecommendationFactory selectType(String type) {
        typeSpecificProperties = getTypeSpecificProperties(type.toUpperCase().trim());
        // Skip ID as that should not be defined by the user.
        typeSpecificProperties.remove(CommonProperties.ID);

        typeSpecificPropertiesSize = typeSpecificProperties.size();
        createdObject = createObject(type.toUpperCase().trim());
        return this;
    }

    /**
     * Call this method to fill all missing properties for the item.
     *
     * @param requestProperty Function that asks the user for input value to be placed as value of the property.
     * @return This factory after all properties have been filled out.
     * @throws IllegalArgumentException If the Function (user) gives invalid property 3 times in a row.
     */
    public RecommendationFactory whileMissingProperties(Function<Property, String> requestProperty) {
        propertyLoop:
        while (hasNextProperty()) {
            Property wantedProperty = getNextProperty();
            // Attempt to get a property from user 3 times.
            for (int i = 0; i < 3; i++) {
                // Ask for property
                String givenValue = requestProperty.apply(wantedProperty).trim();
                if (givenValue.isEmpty()) {
                    givenValue = null;
                }
                if (suggestProperty(wantedProperty, givenValue)) {
                    // Check that the property is valid and exit loop if valid
                    continue propertyLoop;
                }
            }
            throw new IllegalArgumentException("Invalid value given as '" + wantedProperty.getName() + "' 3 times in a row.");
        }
        return this;
    }

    private boolean hasNextProperty() {
        return currentlyRequesting < typeSpecificPropertiesSize;
    }

    private Property getNextProperty() {
        Property property = typeSpecificProperties.get(currentlyRequesting);
        currentlyRequesting++;
        return property;
    }

    private boolean suggestProperty(Property property, String value) {
        if (property.isValid(value)) {
            createdObject.addProperty(property, value);
            return true;
        }
        return false;
    }

    /**
     * Retrieve the finished object.
     *
     * @return Recommendation that matches the build parameters given.
     * @throws IllegalStateException If selectType has not yet been called.
     */
    public Recommendation build() {
        Check.notNull(createdObject, () -> new IllegalStateException("Object has not been created yet, call selectType first."));
        return createdObject;
    }

}
