package theater;

/**
 * Represents a play with its name and type.
 *
 * @null fields in this class are not expected to be null.
 */

public class Play {

    private final String name;
    private final String type;

    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
