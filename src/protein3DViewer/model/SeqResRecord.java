package protein3DViewer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class SeqResRecord {

    private Map<Character, Chain> chains = new HashMap<>();

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
