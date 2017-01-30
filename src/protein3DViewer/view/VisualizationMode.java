package protein3DViewer.view;

/**
 * Created by sophiamersmann on 25/01/2017.
 */
public enum VisualizationMode {

    STICKS("Sticks"),
    RIBBON("Ribbon"),
    CARTOON("Cartoon");

    private String description;

    VisualizationMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
