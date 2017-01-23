package protein3DViewer.view;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class Line extends Cylinder {

    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty startZ;

    private DoubleProperty endX;
    private DoubleProperty endY;
    private DoubleProperty endZ;

    public Line(Double startX, Double startY, Double startZ, Double endX, Double endY, Double endZ) {
        this.startX = new SimpleDoubleProperty(startX);
        this.startY = new SimpleDoubleProperty(startY);
        this.startZ = new SimpleDoubleProperty(startZ);
        this.endX = new SimpleDoubleProperty(endX);
        this.endY = new SimpleDoubleProperty(endY);
        this.endZ = new SimpleDoubleProperty(endZ);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        material.setSpecularColor(Color.BLACK);
        setMaterial(material);

        createLine();
        setUpListeners();
    }

    private void createLine() {
        Point3D startPoint = new Point3D(startX.get(), startY.get(), startZ.get());
        Point3D endPoint = new Point3D(endX.get(), endY.get(), endZ.get());

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D direction = endPoint.subtract(startPoint);

        Point3D midPoint = endPoint.midpoint(startPoint);
        setTranslateX(midPoint.getX());
        setTranslateY(midPoint.getY());
        setTranslateZ(midPoint.getZ());

        Point3D axisOfRotation = direction.crossProduct(yAxis);
        setRotationAxis(axisOfRotation);

        double angle = yAxis.angle(direction);
        setRotate(-angle);

        setRadius(0.025);
        setHeight(direction.magnitude());
    }

    private void setUpListeners() {

        startX.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                createLine();
            }
        });

        startY.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                createLine();
            }
        });

        startZ.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                createLine();
            }
        });

        endX.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                createLine();
            }
        });

        endY.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                createLine();
            }
        });

        endZ.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                createLine();
            }
        });
    }

    public double getStartX() {
        return startX.get();
    }

    public DoubleProperty startXProperty() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX.set(startX);
    }

    public double getStartY() {
        return startY.get();
    }

    public DoubleProperty startYProperty() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY.set(startY);
    }

    public double getStartZ() {
        return startZ.get();
    }

    public DoubleProperty startZProperty() {
        return startZ;
    }

    public void setStartZ(double startZ) {
        this.startZ.set(startZ);
    }

    public double getEndX() {
        return endX.get();
    }

    public DoubleProperty endXProperty() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX.set(endX);
    }

    public double getEndY() {
        return endY.get();
    }

    public DoubleProperty endYProperty() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY.set(endY);
    }

    public double getEndZ() {
        return endZ.get();
    }

    public DoubleProperty endZProperty() {
        return endZ;
    }

    public void setEndZ(double endZ) {
        this.endZ.set(endZ);
    }

    @Override
    public String toString() {
        return "Line{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", startZ=" + startZ +
                ", endX=" + endX +
                ", endY=" + endY +
                ", endZ=" + endZ +
                '}';
    }
}
