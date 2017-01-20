package protein3DViewer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class SeqResRecord {

    private List<SeqResChain> chains = new ArrayList<SeqResChain>();

    public List<SeqResChain> getChains() {
        return chains;
    }

    public void setChains(List<SeqResChain> chains) {
        this.chains = chains;
    }

    @Override
    public String toString() {
        return "SeqResRecord{" +
                "chains=" + chains +
                '}';
    }
}
