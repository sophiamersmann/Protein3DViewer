package protein3DViewer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Model {

    private Integer id;

    private Map<Character, Chain> chains = new HashMap<>();

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

    public void setChains(Map<Character, Chain> chains) {
        this.chains = chains;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", chains=" + chains +
                '}';
    }
}
