package protein3DViewer.view.modelVisualization;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import protein3DViewer.model.*;
import protein3DViewer.view.ColorValue;
import protein3DViewer.view.ModelView;

import java.util.*;

import static protein3DViewer.view.modelVisualization.MeshTools.*;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class RibbonVisualization extends AbstractModelVisualization {

    private Map<Integer, Map<AtomName, Integer>> mapResIdToAtomToIndex;

    private TriangleMesh ribbonMesh;
    private MeshView ribbonMeshView;

    public RibbonVisualization(Model model, ModelView modelView) {
        super(model, modelView);
    }

    @Override
    void createBottomGroup() {
        ribbonMesh = new TriangleMesh();
        ribbonMesh.getTexCoords().addAll(0, 0);
        ribbonMesh.getPoints().addAll(extractPoints());
        ribbonMesh.getFaces().addAll(generateFaces());

        ribbonMeshView = new MeshView(ribbonMesh);
        ribbonMeshView.setMaterial(new PhongMaterial(ColorValue.RIBBON.getColor()));
        bottomGroup.getChildren().add(ribbonMeshView);
    }

    /**
     * extract all points for triangle mesh
     *
     * @return points
     */
    private float[] extractPoints() {
        mapResIdToAtomToIndex = new HashMap<>();
        List<Float> points = new ArrayList<>();
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                points = extractPointsOfResidue(points, residue);
            }
        }
        return toFloatArray(points);
    }

    /**
     * extract all points of a specific for a triangle mesh
     *
     * @param points  already extracted points
     * @param residue residue
     * @return points
     */
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

    /**
     * extract points of a specific atom for a triangle mesh
     *
     * @param points    already extracted points
     * @param atom      atom
     * @param residueID ID of the residue to which the atom belongs
     * @return points
     */
    private List<Float> extractPointsOfAtom(List<Float> points, Atom atom, Integer residueID) {
        points.add((float) atom.getX());
        mapResIdToAtomToIndex.get(residueID).put(atom.getName(), (points.size() - 1) / 3);
        points.add((float) atom.getY());
        points.add((float) atom.getZ());
        return points;
    }

    /**
     * generate triangles of the triangle mesh
     *
     * @return faces of the triangle mesh
     */
    private int[] generateFaces() {
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


}
