package protein3DViewer.view.modelVisualization;

import javafx.geometry.Point3D;
import protein3DViewer.model.Atom;
import protein3DViewer.model.AtomName;
import protein3DViewer.model.Residue;

import java.util.List;

/**
 * Created by sophiamersmann on 02/02/2017.
 */
public class MeshTools {

    private final static Double CC_BOND_LENGTH = 1.54;

    /**
     * make list of floats to float array
     *
     * @param list list of floats
     * @return float array
     */
    static float[] toFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * make list of ints to int array
     *
     * @param list list of ints
     * @return int array
     */
    static int[] toIntArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * create pseudo atom in the CB position if not present (GLY)
     *
     * @param residue residue for which pseudo atom ist to be created
     * @return atom
     */
    static Atom createPseudoAtom(Residue residue) {
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
}
