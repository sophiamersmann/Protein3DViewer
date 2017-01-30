package protein3DViewer.view;

/**
 * Created by sophiamersmann on 25/01/2017.
 */
public enum ColorMode {

    UNICOLOR("single color"),
    BY_ATOM_TYPE("atom type"),
    BY_SECONDARY_STRUCTURE("secondary structure"),
    BY_PROPERTIES("properties");

    private String description;

    ColorMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
