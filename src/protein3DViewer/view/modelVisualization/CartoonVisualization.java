package protein3DViewer.view.modelVisualization;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import protein3DViewer.model.*;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.bondView.Line;

import java.util.*;

import static protein3DViewer.view.modelVisualization.RibbonVisualization.toFloatArray;
import static protein3DViewer.view.modelVisualization.RibbonVisualization.toIntArray;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class CartoonVisualization extends AbstractModelVisualization {

    private final static Double CC_BOND_LENGTH = 1.54;

    private List<Cylinder> helices;
    private List<MeshView> strands;
    private List<Line> connections;

    private Group helixGroup;
    private Group strandGroup;
    private Group connectionGroup;

    private Map<Integer, Map<AtomName, Integer>> mapResIdToAtomToIndex;

    public CartoonVisualization(Model model, ModelView modelView) {
        super(model, modelView);
    }

    @Override
    void createBottomGroup() {
        createHelixViews();
        createStrandViews();
        createConnections();
        bottomGroup.getChildren().addAll(helixGroup, strandGroup, connectionGroup);
    }

    private void createHelixViews() {  // TODO use line
        helices = new ArrayList<>();
        helixGroup = new Group();
        for (Helix helix: model.getProtein().getSecondaryStructure().getHelices().values()) {
            Cylinder cylinder = new Cylinder();
            Point3D initPoint = new Point3D(
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getX(),
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getY(),
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getZ()
            );
            Point3D endPoint = new Point3D(
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getX(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getY(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getZ()
            );
            Point3D direction = endPoint.subtract(initPoint);
            Point3D midPoint = endPoint.midpoint(initPoint);
            Point3D yAxis = new Point3D(0, 1, 0);
            Point3D axisOfRotation = direction.crossProduct(yAxis);
            double angle = yAxis.angle(direction);

            cylinder.setTranslateX(midPoint.getX());
            cylinder.setTranslateY(midPoint.getY());
            cylinder.setTranslateZ(midPoint.getZ());
            cylinder.setRotationAxis(axisOfRotation);
            cylinder.setRotate(-angle);
            cylinder.setHeight(direction.magnitude());
            cylinder.setRadius(1);
            cylinder.setMaterial(new PhongMaterial(AbstractAtomView.HELIX_COLOR));
            helices.add(cylinder);
            helixGroup.getChildren().add(cylinder);
        }
    }

    private void createStrandViews() {
        strands = new ArrayList<>();
        strandGroup = new Group();
//        for (Chain chain: model.getChains().values()) {
//            List<Integer> residueIDs = new ArrayList<>(chain.getResidues().keySet());
//            Collections.sort(residueIDs);
//            for (int i = 0; i < residueIDs.size() - 1; i++) {
//                Residue currRes = chain.getResidues().get(residueIDs.get(i));
//                Residue nextRes = chain.getResidues().get(residueIDs.get(i+1));
//                if (currRes.isInSheet() && nextRes.isInSheet()) {
//
//                } else if (currRes.isInSheet())
//            }
//        }
        mapResIdToAtomToIndex = new HashMap<>();
        for (Sheet sheet: model.getProtein().getSecondaryStructure().getSheets().values()) {
            for (Strand strand: sheet.getStrands().values()) {
                TriangleMesh strandMesh = new TriangleMesh();
                strandMesh.getTexCoords().addAll(0, 0);
                strandMesh.getPoints().addAll(extractPoints(strand));
                strandMesh.getFaces().addAll(generateFaces(strand));
//                int[] smoothingGroups = new int[strandMesh.getFaces().size() / 6];
//                Arrays.fill(smoothingGroups, 1);
//                for (int i = 0; i < smoothingGroups.length / 2; i++) {
//                    smoothingGroups[i] = 2;
//                }
//                strandMesh.getFaceSmoothingGroups().addAll(smoothingGroups);
                mapResIdToAtomToIndex.clear();
                MeshView strandMeshView = new MeshView(strandMesh);
                strandMeshView.setMaterial(new PhongMaterial(Color.YELLOW));
                strands.add(strandMeshView);
                strandGroup.getChildren().add(strandMeshView);
            }
        }
    }

    private float[] extractPoints(Strand strand) {  // TODO: copy pasted from Ribbon
        List<Float> points = new ArrayList<>();
        Integer startResID = strand.getInitResidue().getId();
        Integer endResID = strand.getEndResidue().getId();
        for (int resID = startResID; resID <= endResID; resID++) {
            Residue residue = model.getChain(strand.getChainName()).getResidue(resID);
            points = extractPointsOfResidue(points, residue);
        }
        return toFloatArray(points);
    }

    private int[] generateFaces(Strand strand) {
        List<Integer> faces = new ArrayList<>();
        List<Integer> residueIDs = new ArrayList<>(mapResIdToAtomToIndex.keySet());
        Collections.sort(residueIDs);
        for (int i = 0; i < residueIDs.size() - 1; i++) {
            int indexCBi = mapResIdToAtomToIndex.get(residueIDs.get(i)).get(AtomName.CARBON_BETA);
            int indexCBj = mapResIdToAtomToIndex.get(residueIDs.get(i + 1)).get(AtomName.CARBON_BETA);
            int indexCAi = mapResIdToAtomToIndex.get(residueIDs.get(i)).get(AtomName.CARBON_ALPHA);
            int indexCAj = mapResIdToAtomToIndex.get(residueIDs.get(i + 1)).get(AtomName.CARBON_ALPHA);
            faces.addAll(Arrays.asList(indexCBi, 0, indexCBj, 0, indexCAi, 0));
            faces.addAll(Arrays.asList(indexCBi, 0, indexCBj, 0, indexCAj, 0));
            faces.addAll(Arrays.asList(indexCBi, 0, indexCAi, 0, indexCBj, 0));
            faces.addAll(Arrays.asList(indexCBi, 0, indexCAj, 0, indexCBj, 0));
        }
        return toIntArray(faces);
    }

    private List<Float> extractPointsOfResidue(List<Float> points, Residue residue) {
        mapResIdToAtomToIndex.put(residue.getId(), new HashMap<>());
        points = extractPointsOfAtom(points, residue.getAtom(AtomName.CARBON_ALPHA), residue.getId());
        if (residue.getName3().equals("GLY")) {
            points = extractPointsOfAtom(points, createPseudoAtom(residue), residue.getId());
        } else {
            points = extractPointsOfAtom(points, residue.getAtom(AtomName.CARBON_BETA), residue.getId());
        }
        return points;

    }

    private List<Float> extractPointsOfAtom(List<Float> points, Atom atom, Integer residueID) {
        points.add((float) atom.getX());
        mapResIdToAtomToIndex.get(residueID).put(atom.getName(), (points.size() - 1) / 3);
        points.add((float) atom.getY());
        points.add((float) atom.getZ());
        return points;
    }

    private Atom createPseudoAtom(Residue residue) {
        Atom atomCA = residue.getAtom(AtomName.CARBON_ALPHA);
        Atom atomN = residue.getAtom(AtomName.NITROGEN);
        Atom atomC = residue.getAtom(AtomName.CARBON);
        Point3D directionCN = new Point3D(
                atomCA.getX() - atomN.getX(),
                atomCA.getY() - atomN.getY(),
                atomCA.getZ() - atomN.getZ()
        );
        Point3D directionCC = new Point3D(
                atomC.getX() - atomCA.getX(),
                atomC.getY() - atomCA.getY(),
                atomC.getZ() - atomCA.getZ()
        );
        Point3D normal = directionCN.crossProduct(directionCC).normalize().multiply(CC_BOND_LENGTH);
        Atom pseudoAtom = new Atom(-1, "CB", "pseudo");
        pseudoAtom.setX(atomCA.getX() + normal.getX());
        pseudoAtom.setY(atomCA.getY() + normal.getY());
        pseudoAtom.setZ(atomCA.getZ() + normal.getZ());
        return pseudoAtom;
    }

    private void createConnections() {
        connections = new ArrayList<>();
        connectionGroup = new Group();
        for (Chain chain: model.getChains().values()) {
            List<Integer> residueIDs = new ArrayList<>(chain.getResidues().keySet());
            Collections.sort(residueIDs);
            for (int i = 0; i < residueIDs.size() - 1; i++) {
                Residue currRes = chain.getResidues().get(residueIDs.get(i));
                Residue nextRes = chain.getResidues().get(residueIDs.get(i+1));
                if (!currRes.isInHelix() && !currRes.isInSheet() || !nextRes.isInHelix() && !nextRes.isInSheet()) {
                    Line connection = new Line(
                            currRes.getAtom(AtomName.CARBON_ALPHA).getX(),
                            currRes.getAtom(AtomName.CARBON_ALPHA).getY(),
                            currRes.getAtom(AtomName.CARBON_ALPHA).getZ(),
                            nextRes.getAtom(AtomName.CARBON_ALPHA).getX(),
                            nextRes.getAtom(AtomName.CARBON_ALPHA).getY(),
                            nextRes.getAtom(AtomName.CARBON_ALPHA).getZ()
                    );
                    connection.setRadius(0.25);
                    connections.add(connection);
                    connectionGroup.getChildren().addAll(connection);
                }
            }
        }

    }

    @Override
    void createTopGroup() {

    }
}
