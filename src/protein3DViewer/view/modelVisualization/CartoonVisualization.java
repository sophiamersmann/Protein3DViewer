package protein3DViewer.view.modelVisualization;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import protein3DViewer.model.*;
import protein3DViewer.view.ColorValue;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.bondView.Line;

import java.util.*;

import static protein3DViewer.view.modelVisualization.MeshTools.*;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class CartoonVisualization extends AbstractModelVisualization {

    private List<Line> helices;
    private List<MeshView> strands;
    private List<Line> loops;

    private Group helixGroup;
    private Group strandGroup;
    private Group loopGroup;

    private Map<Integer, Map<AtomName, Integer>> mapResIdToAtomToIndex;

    public CartoonVisualization(Model model, ModelView modelView) {
        super(model, modelView);
    }

    @Override
    void createBottomGroup() {
        createHelixViews();
        createStrandViews();
        createLoops();
        bottomGroup.getChildren().addAll(helixGroup, strandGroup, loopGroup);
    }

    /**
     * create helices as cylinder
     */
    private void createHelixViews() {
        helices = new ArrayList<>();
        helixGroup = new Group();
        for (Helix helix : model.getProtein().getSecondaryStructure().getHelices().values()) {
            Line line = new Line(
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getX(),
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getY(),
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getZ(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getX(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getY(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getZ()
            );
            line.setRadius(1);
            line.setMaterial(new PhongMaterial(ColorValue.HELIX.getColor()));
            helices.add(line);
            helixGroup.getChildren().add(line);
        }
    }

    /**
     * create strands as triangle meshes
     */
    private void createStrandViews() {
        strands = new ArrayList<>();
        strandGroup = new Group();
        mapResIdToAtomToIndex = new HashMap<>();
        for (Sheet sheet : model.getProtein().getSecondaryStructure().getSheets().values()) {
            for (Strand strand : sheet.getStrands().values()) {
                TriangleMesh strandMesh = new TriangleMesh();
                strandMesh.getTexCoords().addAll(0, 0);
                strandMesh.getPoints().addAll(extractPoints(strand));
                strandMesh.getFaces().addAll(generateFaces(strand));
                mapResIdToAtomToIndex.clear();
                MeshView strandMeshView = new MeshView(strandMesh);
                strandMeshView.setMaterial(new PhongMaterial(ColorValue.SHEET.getColor()));
                strands.add(strandMeshView);
                strandGroup.getChildren().add(strandMeshView);
            }
        }
    }

    /**
     * extract points for the triangle mesh of a specific strand
     *
     * @param strand strand to be displayed as triangle mesh
     * @return points of the triangle mesh
     */
    private float[] extractPoints(Strand strand) {
        List<Float> points = new ArrayList<>();
        Integer startResID = strand.getInitResidue().getId();
        Integer endResID = strand.getEndResidue().getId();
        for (int resID = startResID; resID <= endResID; resID++) {
            Residue residue = model.getChain(strand.getChainName()).getResidue(resID);
            points = extractPointsOfResidue(points, residue);
        }
        return toFloatArray(points);
    }

    /**
     * generate triangles of the triangle mesh of a specific strand
     *
     * @param strand strand to be displayed as triangle mesh
     * @return faces of the triangle mesh
     */
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
     * create loops as lines between CAs of neighbouring residues
     */
    private void createLoops() {
        loops = new ArrayList<>();
        loopGroup = new Group();
        for (Chain chain : model.getChains().values()) {
            List<Integer> residueIDs = new ArrayList<>(chain.getResidues().keySet());
            Collections.sort(residueIDs);
            for (int i = 0; i < residueIDs.size() - 1; i++) {
                Residue currRes = chain.getResidues().get(residueIDs.get(i));
                Residue nextRes = chain.getResidues().get(residueIDs.get(i + 1));
                if (!(currRes.isInHelix() || currRes.isInSheet()) || !(nextRes.isInHelix() || nextRes.isInSheet())) {
                    Line loop = new Line(
                            currRes.getAtom(AtomName.CARBON_ALPHA).getX(),
                            currRes.getAtom(AtomName.CARBON_ALPHA).getY(),
                            currRes.getAtom(AtomName.CARBON_ALPHA).getZ(),
                            nextRes.getAtom(AtomName.CARBON_ALPHA).getX(),
                            nextRes.getAtom(AtomName.CARBON_ALPHA).getY(),
                            nextRes.getAtom(AtomName.CARBON_ALPHA).getZ()
                    );
                    loop.setMaterial(new PhongMaterial(ColorValue.LOOPS.getColor()));
                    loop.setRadius(0.25);
                    loops.add(loop);
                    loopGroup.getChildren().addAll(loop);
                }
            }
        }

    }
}
