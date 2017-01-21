package protein3DViewer.view;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import protein3DViewer.model.*;
import protein3DViewer.presenter.SequencePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class SequenceView extends Group {

    private SeqResRecord seqResRecord;
    private SecondaryStructure secondaryStructure;

    private Map<Integer, Label> residueViews = new HashMap<>();
    private Map<Integer, Label> secondaryStructureAnnotations = new HashMap<>();

    private HBox residueViewBox = new HBox(2);
    private HBox secondaryStructureAnnotationBox = new HBox(2);

    public SequenceView(SequencePresenter sequencePresenter, SeqResRecord seqResRecord, SecondaryStructure secondaryStructures) {
        this.seqResRecord = seqResRecord;
        this.secondaryStructure = secondaryStructures;
        initLayout();
        initResidueViews();
        initSecondaryStructureAnnotation();
        getChildren().addAll(residueViewBox, secondaryStructureAnnotationBox);
    }

    private void initLayout() {  // TODO: align Text
//        secondaryStructureAnnotationBox.setLayoutX(residueViewBox.getLayoutX());
//        secondaryStructureAnnotationBox.setLayoutY(residueViewBox.getLayoutY() + 15);
        residueViewBox.setAlignment(Pos.BASELINE_LEFT);
        secondaryStructureAnnotationBox.setAlignment(Pos.BASELINE_LEFT);
        secondaryStructureAnnotationBox.setLayoutY(residueViewBox.getLayoutY() + 15);
    }

    private void initResidueViews() {
        for (Chain chain : seqResRecord.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                Label resLabel = new Label(residue.getName().toString());
                residueViews.put(residue.getId(), resLabel);
                residueViewBox.getChildren().add(resLabel);
            }
        }
    }

    private void initSecondaryStructureAnnotation() {
        for (Chain chain : seqResRecord.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                Label secStrLabel = new Label(" ");
                if (residue.isInHelix()) {
                    secStrLabel = new Label("H");
                } else if (residue.isInSheet()) {
                    secStrLabel = new Label("E");
                }
                secondaryStructureAnnotations.put(residue.getId(), secStrLabel);
                secondaryStructureAnnotationBox.getChildren().add(secStrLabel);
            }
        }
    }

}
