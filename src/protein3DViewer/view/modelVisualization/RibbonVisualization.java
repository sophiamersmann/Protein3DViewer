package protein3DViewer.view.modelVisualization;

import com.sun.javafx.scene.traversal.SubSceneTraversalEngine;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import protein3DViewer.model.*;

import java.util.*;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class RibbonVisualization extends AbstractModelVisualization {

    private final static Double CC_BOND_LENGTH = 1.54;

    private Map<Integer, Map<AtomName, Integer>> mapResIdToAtomToIndex;
//    private Map<Integer, Map<String, Integer>> mapResIdToAtomToIndex;

    private TriangleMesh ribbonMesh;
    private MeshView ribbonMeshView;

    public RibbonVisualization(Model model) {
        super(model);
    }

    @Override
    void createBottomGroup() {
        ribbonMesh = new TriangleMesh();
        ribbonMesh.getTexCoords().addAll(0, 0);
        ribbonMesh.getPoints().addAll(extractPoints());
        ribbonMesh.getFaces().addAll(generateFaces());

        ribbonMeshView = new MeshView(ribbonMesh);
        ribbonMeshView.setMaterial(new PhongMaterial(Color.YELLOW));
        bottomGroup.getChildren().add(ribbonMeshView);
    }

//    private float[] extractPoints() {
//        mapResIdToAtomToIndex = new HashMap<>();
//        List<Float> points = new ArrayList<>();
//        for (Chain chain : model.getChains().values()) {
//            for (Residue residue : chain.getResidues().values()) {
//                mapResIdToAtomToIndex.put(residue.getId(), new HashMap<>());
//                // get carbon beta (or pseudo if GLY)
//                Atom atomCB = residue.getName3().equals("GLY") ? createPseudoAtom(residue) : residue.getAtom(AtomName.CARBON_BETA);
//                points.add((float) atomCB.getX());
//                mapResIdToAtomToIndex.get(residue.getId()).put("CB", (points.size() - 1) / 3);
//                points.add((float) atomCB.getY());
//                points.add((float) atomCB.getZ());
//                // mirror CB
//                Point3D directionCACB = new Point3D(
//                        atomCB.getX() - residue.getAtom(AtomName.CARBON_ALPHA).getX(),
//                        atomCB.getY() - residue.getAtom(AtomName.CARBON_ALPHA).getY(),
//                        atomCB.getZ() - residue.getAtom(AtomName.CARBON_ALPHA).getZ()
//                );
//                Point3D mirroredCB = new Point3D(
//                        residue.getAtom(AtomName.CARBON_ALPHA).getX() - directionCACB.getX(),
//                        residue.getAtom(AtomName.CARBON_ALPHA).getY() - directionCACB.getY(),
//                        residue.getAtom(AtomName.CARBON_ALPHA).getZ() - directionCACB.getZ()
//                );
//                points.add((float) mirroredCB.getX());
//                mapResIdToAtomToIndex.get(residue.getId()).put("CBm", (points.size() - 1) / 3);
//                points.add((float) mirroredCB.getY());
//                points.add((float) mirroredCB.getZ());
//            }
//        }
//        return toFloatArray(points);
//    }

//    private int[] generateFaces() {
//        List<Integer> faces = new ArrayList<>();
//        List<Integer> residueIDs = new ArrayList<>(mapResIdToAtomToIndex.keySet());
//        Collections.sort(residueIDs);
//        for (int i = 0; i < residueIDs.size() - 1; i++) {
//            int indexCBi = mapResIdToAtomToIndex.get(residueIDs.get(i)).get("CB");
//            int indexCBj = mapResIdToAtomToIndex.get(residueIDs.get(i + 1)).get("CB");
//            int indexCBmi = mapResIdToAtomToIndex.get(residueIDs.get(i)).get("CBm");
//            int indexCBmj = mapResIdToAtomToIndex.get(residueIDs.get(i + 1)).get("CBm");
//            faces.addAll(Arrays.asList(indexCBi, 0, indexCBj, 0, indexCBmi, 0));
//            faces.addAll(Arrays.asList(indexCBmi, 0, indexCBj, 0, indexCBmj, 0));
//            faces.addAll(Arrays.asList(indexCBi, 0, indexCBmi, 0, indexCBmj, 0));
//            faces.addAll(Arrays.asList(indexCBi, 0, indexCBj, 0, indexCBmj, 0));
//        }
//        return toIntArray(faces);
//    }

    @Override
    void createTopGroup() {
    }

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
//            faces.addAll(Arrays.asList(indexCAi, 0, indexCBi, 0, indexCAj, 0));
//            faces.addAll(Arrays.asList(indexCAi, 0, indexCAj, 0, indexCBj, 0));

            faces.addAll(Arrays.asList(indexCBi, 0, indexCAi, 0, indexCBj, 0));
            faces.addAll(Arrays.asList(indexCBi, 0, indexCAj, 0, indexCBj, 0));
//            faces.addAll(Arrays.asList(indexCAi, 0, indexCAj, 0, indexCBi, 0));
//            faces.addAll(Arrays.asList(indexCAi, 0, indexCBj, 0, indexCAj, 0));
        }
        return toIntArray(faces);
}

//    private static <T> T[] toArray(Class<T> classT, List<T> list) {
//        @SuppressWarnings("unchecked")
//        T[] arr = (T[]) Array.newInstance(classT, list.size());
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = list.get(i);
//        }
//        return arr;
//    }

    static float[] toFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    static int[] toIntArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }


}
