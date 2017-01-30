package protein3DViewer.model;

import javafx.scene.control.ProgressIndicator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class SeqResRecord {

    private Protein protein;

    private Map<Character, Chain> chains = new HashMap<>();

    /**
     * generated the whole amino acid sequence of all chains
     *
     * @return amino acid sequence
     */
    public String generateAminoAcidSequence() {
        StringBuilder sb = new StringBuilder();
        for (Chain chain : chains.values()) {
            sb.append(chain.generateAminoAcidSequence());
        }
        return sb.toString();
    }

    /**
     * annotates amino acid sequence with secondary structure information
     *
     * @param helixAnnotation annotation for helix residues
     * @param sheetAnnotation annotation for sheet residues
     * @param otherAnnotation annotation for the rest of the residues
     * @return annotation of the amino acid sequence
     */
    public String generateSecondaryStructureAnnotations(String helixAnnotation, String sheetAnnotation, String otherAnnotation) {
        StringBuilder sb = new StringBuilder();
        for (Chain chain : chains.values()) {
            sb.append(chain.generateSecondaryStructureAnnotations(helixAnnotation, sheetAnnotation, otherAnnotation));
        }
        return sb.toString();
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    public Map<Character, Chain> getChains() {
        return chains;
    }

    public Chain getChain(Character key) {
        return chains.get(key);
    }

    public void setChains(Map<Character, Chain> chains) {
        this.chains = chains;
    }

    public void setChain(Character key, Chain chain) {
        chains.put(key, chain);
    }

    @Override
    public String toString() {
        return "SeqResRecord{" +
                "chains=" + chains +
                '}';
    }
}
