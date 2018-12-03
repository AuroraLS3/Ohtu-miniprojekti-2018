package projekti.domain;

/**
 * Contains common properties that might be used in multiple different objects.
 *
 * @author Rsl1122
 */
public class CommonProperties {

    static final Property<Integer> ID = new Property<>("ID", Integer.class);
    static final Property<String> TITLE = new Property<>("NAME", String.class, title -> title != null && !title.isEmpty());
    static final Property<String> DESCRIPTION = new Property<>("DESCRIPTION", String.class);

}
