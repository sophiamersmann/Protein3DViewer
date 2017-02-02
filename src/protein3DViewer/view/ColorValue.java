package protein3DViewer.view;

import javafx.scene.paint.Color;

/**
 * Created by sophiamersmann on 01/02/2017.
 */
public enum ColorValue {

    DEFAULT(Color.GRAY),
    RIBBON(Color.YELLOW),
    HELIX(Color.GREEN),
    SHEET(Color.ORANGE),
    LOOPS(Color.GRAY),
    LARGE_HYDROPHOBIC(Color.DARKBLUE),
    SMALL_HYDROPHOBIC(Color.LIGHTBLUE),
    POLAR(Color.PALEGREEN),
    POSITIVE_CHARGED(Color.ORANGERED),
    NEGATIVE_CHARGED(Color.NAVAJOWHITE);

    // default color
    private Color defaultColor;

    // currently displayed color
    private Color color;

    ColorValue(Color defaultColor) {
        this.defaultColor = defaultColor;
        this.color = defaultColor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        switch (this) {
            case DEFAULT: return "Default";
            case RIBBON: return "Ribbon";
            case HELIX: return "Helix";
            case SHEET: return "Sheet";
            case LOOPS: return "Loops";
            case LARGE_HYDROPHOBIC: return "Large Hydrophobic Amino Acids";
            case SMALL_HYDROPHOBIC: return "Small Hydrophobic Amino Acids";
            case POLAR: return "Polar Amino Acids";
            case POSITIVE_CHARGED: return "Positive Charged Amino Acids";
            case NEGATIVE_CHARGED: return "Negative Charged Amino Acids";
            default: return "";
        }
    }

    /**
     * reset all colors to its default
     */
    public static void reset() {
        for (ColorValue colorValue: ColorValue.values()) {
            colorValue.color = colorValue.defaultColor;
        }
    }
}
