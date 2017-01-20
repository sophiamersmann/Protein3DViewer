package protein3DViewer.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Protein {

    private Path path;

    private String id;
    private String name;

    private SeqResRecord seqResRecord;
    private Map<String, SecondaryStructure> secondaryStructures = new HashMap<>();
    private Model model;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SeqResRecord getSeqResRecord() {
        return seqResRecord;
    }

    public void setSeqResRecord(SeqResRecord seqResRecord) {
        this.seqResRecord = seqResRecord;
    }

    public Map<String, SecondaryStructure> getSecondaryStructures() {
        return secondaryStructures;
    }

    public SecondaryStructure getSecondaryStructure(String key) {
        return secondaryStructures.get(key);
    }

    public void setSecondaryStructures(Map<String, SecondaryStructure> secondaryStructures) {
        this.secondaryStructures = secondaryStructures;
    }

    public void setSecondaryStructure(String key, SecondaryStructure secondaryStructure) {
        secondaryStructures.put(key, secondaryStructure);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Protein{" +
                "path=" + path +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", seqResRecord=" + seqResRecord +
                ", secondaryStructures=" + secondaryStructures +
                ", model=" + model +
                '}';
    }
}
