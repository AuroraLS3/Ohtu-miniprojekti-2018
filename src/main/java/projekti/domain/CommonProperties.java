package projekti.domain;

/**
 * TODO Class Javadoc comment
 *
 * @author Rsl1122
 */
public class CommonProperties {

    static Property<Integer> ID = new Property<>("db_id", Integer.class);
    static Property<String> TITLE = new Property<>("title", String.class, title -> title != null && !title.isEmpty());

}
