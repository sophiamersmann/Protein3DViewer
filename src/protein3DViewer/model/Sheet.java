package protein3DViewer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Sheet extends SecondaryStructure {

    List<Strand> strands = new ArrayList<Strand>();

    public Sheet(String id) {
        super(id);
    }

    public List<Strand> getStrands() {
        return strands;
    }

    public void setStrands(List<Strand> strands) {
        this.strands = strands;
    }

    @Override
    public String toString() {
        return "Sheet{" +
                "strands=" + strands +
                '}';
    }
}
