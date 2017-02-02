package protein3DViewer.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import protein3DViewer.model.Residue;

/**
 * Created by sophiamersmann on 30/01/2017.
 */
public class SelectableLabel extends Label {

    private Residue residue;

    private BooleanProperty selected = new SimpleBooleanProperty();
    private BooleanProperty shiftSelected = new SimpleBooleanProperty();

    private Paint prevPaint;
    private ObjectProperty<Paint> currPaint = new SimpleObjectProperty<>();

    public SelectableLabel(Residue residue, String text) {
        super(text);
        this.residue = residue;
        prevPaint = getTextFill();
        currPaint.bindBidirectional(textFillProperty());
    }

    public void setTextPaint(Paint paint) {
        prevPaint = currPaint.get();
        currPaint.set(paint);
    }

    public void resetTextPaint() {
        Paint tmpPaint = currPaint.get();
        currPaint.set(prevPaint);
        prevPaint = tmpPaint;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isShiftSelected() {
        return shiftSelected.get();
    }

    public BooleanProperty shiftSelectedProperty() {
        return shiftSelected;
    }

    public void setShiftSelected(boolean shiftSelected) {
        this.shiftSelected.set(shiftSelected);
    }

    public Residue getResidue() {
        return residue;
    }
}
