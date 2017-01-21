package protein3DViewer.model;

import java.nio.file.Path;


/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Protein {

    private Path path;

    private String id;
    private String name;

    private SeqResRecord seqResRecord;
    private SecondaryStructure secondaryStructure;
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

    public SecondaryStructure getSecondaryStructure() {
        return secondaryStructure;
    }

    public void setSecondaryStructure(SecondaryStructure secondaryStructure) {
        this.secondaryStructure = secondaryStructure;
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
                ", secondaryStructure=" + secondaryStructure +
                ", model=" + model +
                '}';
    }
}
