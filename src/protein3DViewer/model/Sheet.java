package protein3DViewer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Sheet extends SecondaryStructure {

    Map<Integer, Strand> strands = new HashMap<>();

    public Sheet(String id) {
        super(id);
    }

    public Map<Integer, Strand> getStrands() {
        return strands;
    }

    public Strand getStrand(Integer key) {
        return strands.get(key);
    }

    public void setStrands(Map<Integer, Strand> strands) {
        this.strands = strands;
    }

    public void setStrand(Integer key, Strand strand) {
        strands.put(key, strand);
    }

    @Override
    public String toString() {
        return "Sheet{" +
                "strands=" + strands +
                '}';
    }
}
