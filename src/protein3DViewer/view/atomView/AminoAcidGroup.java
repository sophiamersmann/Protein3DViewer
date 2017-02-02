package protein3DViewer.view.atomView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 28/01/2017.
 */
public enum AminoAcidGroup {

    SMALL_HYDROPHOBIC("AG"),
    LARGE_HYDROPHOBIC("VLIMPFW"),
    POLAR("STNQCY"),
    POS_CHARGED("KRH"),
    NEG_CHARGED("DE");

    private String aminoAcids;

    private static Map<Character, AminoAcidGroup> mapAminoAcidToGroup = new HashMap<>();

    static {
        for (AminoAcidGroup aminoAcidGroup : AminoAcidGroup.values()) {
            for (Character aminoAcid : aminoAcidGroup.aminoAcids.toCharArray()) {
                mapAminoAcidToGroup.put(aminoAcid, aminoAcidGroup);
            }
        }
    }

    public static AminoAcidGroup aminoAcidGroupOf(Character aminoAcid) {
        return mapAminoAcidToGroup.get(aminoAcid);
    }

    AminoAcidGroup(String aminoAcids) {
        this.aminoAcids = aminoAcids;
    }

    public String getAminoAcids() {
        return aminoAcids;
    }
}
