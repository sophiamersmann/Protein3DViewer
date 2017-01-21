package protein3DViewer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Model {

    private Integer id;
    private Map<Character, Chain> chains = new HashMap<>();

    public void createBonds() {
        for (Chain chain : chains.values()) {
            chain.createBonds();
        }
    }

    public Model(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "Model{" +
                "id=" + id +
                ", chains=" + chains +
                '}';
    }
}
