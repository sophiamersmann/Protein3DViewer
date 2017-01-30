package protein3DViewer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 25/01/2017.
 */
public enum AtomName {

    NITROGEN("N"),
    CARBON_ALPHA("CA"),
    CARBON("C"),
    OXYGEN("O"),
    CARBON_BETA("CB");

    private String pdbName;

    private static Map<String, AtomName> mapPDBNameToAtomName = new HashMap<String, AtomName>();
    static {
        for (AtomName atomName : AtomName.values()) {
            mapPDBNameToAtomName.put(atomName.pdbName, atomName);
        }
    }

    public static AtomName enumOf(String pdbName) {
        return mapPDBNameToAtomName.get(pdbName);
    }

    AtomName(String pdbName) {
        this.pdbName = pdbName;
    }

    public String getPdbName() {
        return pdbName;
    }

}
