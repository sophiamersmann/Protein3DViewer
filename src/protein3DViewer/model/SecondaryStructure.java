package protein3DViewer.model;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class SecondaryStructure {

    private Protein protein;

    private Map<String, Helix> helices = new HashMap<>();
    private Map<String, Sheet> sheets = new HashMap<>();

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    public Map<String, Helix> getHelices() {
        return helices;
    }

    public void setHelices(Map<String, Helix> helices) {
        this.helices = helices;
    }

    public Map<String, Sheet> getSheets() {
        return sheets;
    }

    public void setSheets(Map<String, Sheet> sheets) {
        this.sheets = sheets;
    }

    public Helix getHelix(String key) {
        return helices.get(key);
    }

    public void setHelix(String key, Helix helix) {
        helices.put(key, helix);
    }

    public Sheet getSheet(String key) {
        return sheets.get(key);
    }

    public void setSheet(String key, Sheet sheet) {
        sheets.put(key, sheet);
    }

    @Override
    public String toString() {
        return "SecondaryStructure{" +
                "helices=" + helices +
                ", sheets=" + sheets +
                '}';
    }
}
