package protein3DViewer;

import javafx.geometry.Point3D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class PDBParser {

    private static final List<String> ATOM_NAMES = createAtomList();
    private static final Map<String, Integer[]> MAP_FIELD_TO_COLUMNS = createMap();

    File pdbFile;
    String title;
    Calendar date;
    String idCode;

    // map from chain id to sequence
    Map<Character, List<String>> seqResRecord = new HashMap<>();

    // map from helix id to specific feature
    Map<String, Character> helixChainNames = new HashMap<>();
    Map<String, String> helixInitResNames = new HashMap<>();
    Map<String, Integer> helixInitResNums = new HashMap<>();
    Map<String, String> helixEndResNames = new HashMap<>();
    Map<String, Integer> helixEndResNums = new HashMap<>();
    Map<String, Integer> helixClasses = new HashMap<>();

    // map from sheet id to map from strand id to specific strand feature
    Map<String, Map<Integer, Character>> strandChainNames = new HashMap<>();
    Map<String, Map<Integer, String>> strandInitResNames = new HashMap<>();
    Map<String, Map<Integer, Integer>> strandInitResNums = new HashMap<>();
    Map<String, Map<Integer, String>> strandEndResNames = new HashMap<>();
    Map<String, Map<Integer, Integer>> strandEndResNums = new HashMap<>();
    Map<String, Map<Integer, Integer>> strandSenses = new HashMap<>();

    // map from atom id to specific atom features
    Map<Integer, String> atomNames = new HashMap<>();
    Map<Integer, Character> atomChainNames = new HashMap<>();
    Map<Integer, String> atomResNames = new HashMap<>();
    Map<Integer, Integer> atomResNums = new HashMap<>();
    Map<Integer, String> atomElements = new HashMap<>();
    Map<Integer, Point3D> atomCoordinates = new HashMap<>();

    public PDBParser(File pdbFile) {
        try {
            this.pdbFile = pdbFile;
            BufferedReader bfr = new BufferedReader(new FileReader(pdbFile));
            parse(bfr);
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create list of atoms to be read in
     *
     * @return list of atoms to be read in
     */
    private static List<String> createAtomList() {
        List<String> list = new ArrayList<String>();
        list.add("N");
        list.add("CA");
        list.add("CB");
        list.add("C");
        list.add("O");
        return list;
    }

    /**
     * create map from field key to range of columns
     *
     * @return map from field key to range of columns
     */
    private static Map<String, Integer[]> createMap() {
        Map<String, Integer[]> map = new HashMap<String, Integer[]>();
        map.put("Record name", new Integer[]{1, 6});

        map.put("HEADER_Title", new Integer[]{11, 50});
        map.put("HEADER_Date", new Integer[]{51, 59});
        map.put("HEADER_IDcode", new Integer[]{63, 66});
        map.put("SEQRES_ChainID", new Integer[]{12, 12});
        map.put("SEQRES_ResidueNames", new Integer[]{20, 70});

        map.put("HELIX_HelixID", new Integer[]{12, 14});
        map.put("HELIX_InitResName", new Integer[]{16, 18});
        map.put("HELIX_InitChainID", new Integer[]{20, 20});
        map.put("HELIX_InitSeqNum", new Integer[]{22, 25});
        map.put("HELIX_EndResName", new Integer[]{28, 30});
        map.put("HELIX_EndChainID", new Integer[]{32, 32});
        map.put("HELIX_EndSeqNum", new Integer[]{34, 37});
        map.put("HELIX_HelixClass", new Integer[]{39, 40});

        map.put("SHEET_StrandID", new Integer[]{8, 10});
        map.put("SHEET_SheetID", new Integer[]{12, 14});
        map.put("SHEET_InitResName", new Integer[]{18, 20});
        map.put("SHEET_InitChainID", new Integer[]{22, 22});
        map.put("SHEET_InitSeqNum", new Integer[]{23, 26});
        map.put("SHEET_EndResName", new Integer[]{29, 31});
        map.put("SHEET_EndChainID", new Integer[]{33, 33});
        map.put("SHEET_EndSeqNum", new Integer[]{34, 37});
        map.put("SHEET_Sense", new Integer[]{39, 40});

        map.put("Atom_SerialNum", new Integer[]{7, 11});
        map.put("ATOM_Name", new Integer[]{13, 16});
        map.put("ATOM_ResName", new Integer[]{18, 20});
        map.put("ATOM_ChainID", new Integer[]{22, 22});
        map.put("ATOM_ResSeqNum", new Integer[]{23, 26});
        map.put("ATOM_X", new Integer[]{31, 38});
        map.put("ATOM_Y", new Integer[]{39, 46});
        map.put("ATOM_Z", new Integer[]{47, 54});
        map.put("ATOM_Element", new Integer[]{77, 78});
        return map;
    }

    /**
     * read in field specified by key
     *
     * @param line line of pdb file
     * @param key  specification of field to be read in
     * @return field
     */
    private static String readInColumns(String line, String key) {
        Integer[] boundaries = MAP_FIELD_TO_COLUMNS.get(key);
        if (boundaries == null) {
            System.err.println(key + " does not exist.");
            System.exit(1);
        }
        char[] lineArr = line.toCharArray();
        String column = "";
        for (int i = boundaries[0] - 1; i <= boundaries[1] - 1; i++) {
            column += lineArr[i];
        }
        return column;
    }

    /**
     * create date from string representation
     *
     * @param dateStr string representation of date
     * @return date
     */
    private static Calendar createDate(String dateStr) {
        String[] dateSplitted = dateStr.split("-");
        Integer day = Integer.parseInt(dateSplitted[0]);
        Integer month = translateMonth(dateSplitted[1]);
        Integer year = Integer.parseInt(dateSplitted[2]);
        return new GregorianCalendar(year, month, day);
    }

    /**
     * translates month to its numerical value
     *
     * @param month month
     * @return numerical value of month
     */
    private static Integer translateMonth(String month) {
        switch (month) {
            case "JAN":
                return 1;
            case "FEB":
                return 2;
            case "MAR":
                return 3;
            case "APR":
                return 4;
            case "MAY":
                return 5;
            case "JUN":
                return 6;
            case "JUL":
                return 7;
            case "AUG":
                return 8;
            case "SEP":
                return 9;
            case "OCT":
                return 10;
            case "NOV":
                return 11;
            case "DEC":
                return 12;
            default:
                System.err.println(month + " does not exist.");
                System.exit(1);
                return null;
        }
    }

    /**
     * parse pdb file
     *
     * @param bfr buffered reader of pdb file
     * @throws IOException
     */
    private void parse(BufferedReader bfr) throws IOException {
        String line = bfr.readLine();
        while (line != null) {
            switch (readInColumns(line, "Record name").trim()) {
                case "HEADER":
                    readHeader(line);
                    break;
                case "SEQRES":
                    readSeqResRecord(line);
                    break;
                case "HELIX":
                    readHelix(line);
                    break;
                case "SHEET":
                    readSheet(line);
                    break;
                case "ATOM":
                    String atomName = readInColumns(line, "ATOM_Name").trim();
                    if (!ATOM_NAMES.contains(atomName)) {
                        line = bfr.readLine();
                        continue;
                    }
                    readAtom(line, atomName);
                    break;
                case "TER":
                    return;
            }
            line = bfr.readLine();
        }

    }

    /**
     * read line of header section
     *
     * @param line line of header section
     */
    private void readHeader(String line) {
        title = readInColumns(line, "HEADER_Title").trim();
        date = createDate(readInColumns(line, "HEADER_Date"));
        idCode = readInColumns(line, "HEADER_IDcode");
    }

    /**
     * read line of seqres section
     *
     * @param line line of seqres section
     */
    private void readSeqResRecord(String line) {
        Character chainID = readInColumns(line, "SEQRES_ChainID").charAt(0);
        if (!seqResRecord.containsKey(chainID)) {
            seqResRecord.put(chainID, new ArrayList<>());
        }
        List<String> subsequence = new ArrayList<>();
        Collections.addAll(subsequence, readInColumns(line, "SEQRES_ResidueNames").trim().split(" "));
        seqResRecord.get(chainID).addAll(subsequence);
    }

    /**
     * read line of helix section
     *
     * @param line line of helix section
     */
    private void readHelix(String line) {
        String helixID = readInColumns(line, "HELIX_HelixID").trim();
        helixChainNames.put(helixID, readInColumns(line, "HELIX_InitChainID").trim().charAt(0));
        helixInitResNames.put(helixID, readInColumns(line, "HELIX_InitResName").trim());
        helixInitResNums.put(helixID, Integer.parseInt(readInColumns(line, "HELIX_InitSeqNum").trim()));
        helixEndResNames.put(helixID, readInColumns(line, "HELIX_EndResName").trim());
        helixEndResNums.put(helixID, Integer.parseInt(readInColumns(line, "HELIX_EndSeqNum").trim()));
        helixClasses.put(helixID, Integer.parseInt(readInColumns(line, "HELIX_HelixClass").trim()));
    }

    /**
     * read line of sheet section
     *
     * @param line line of sheet section
     */
    private void readSheet(String line) {
        String sheetId = readInColumns(line, "SHEET_SheetID").trim();
        if (!strandChainNames.containsKey(sheetId)) {
            strandChainNames.put(sheetId, new HashMap<>());
            strandInitResNames.put(sheetId, new HashMap<>());
            strandInitResNums.put(sheetId, new HashMap<>());
            strandEndResNames.put(sheetId, new HashMap<>());
            strandEndResNums.put(sheetId, new HashMap<>());
            strandSenses.put(sheetId, new HashMap<>());
        }
        Integer strandId = Integer.parseInt(readInColumns(line, "SHEET_StrandID").trim());
        strandChainNames.get(sheetId).put(strandId, readInColumns(line, "SHEET_InitChainID").trim().charAt(0));
        strandInitResNames.get(sheetId).put(strandId, readInColumns(line, "SHEET_InitResName").trim());
        strandInitResNums.get(sheetId).put(strandId, Integer.parseInt(readInColumns(line, "SHEET_InitSeqNum").trim()));
        strandEndResNames.get(sheetId).put(strandId, readInColumns(line, "SHEET_EndResName").trim());
        strandEndResNums.get(sheetId).put(strandId, Integer.parseInt(readInColumns(line, "SHEET_EndSeqNum").trim()));
        strandSenses.get(sheetId).put(strandId, Integer.parseInt(readInColumns(line, "SHEET_Sense").trim()));
    }

    /**
     * read line of atom section
     *
     * @param line     line of atom section
     * @param atomName name of atom
     */
    private void readAtom(String line, String atomName) {
        Integer atomId = Integer.parseInt(readInColumns(line, "Atom_SerialNum").trim());
        atomNames.put(atomId, atomName);
        atomChainNames.put(atomId, readInColumns(line, "ATOM_ChainID").trim().charAt(0));
        atomResNames.put(atomId, readInColumns(line, "ATOM_ResName").trim());
        atomResNums.put(atomId, Integer.parseInt(readInColumns(line, "ATOM_ResSeqNum").trim()));
        atomElements.put(atomId, readInColumns(line, "ATOM_Element").trim());
        atomCoordinates.put(atomId, new Point3D(
                Float.parseFloat(readInColumns(line, "ATOM_X").trim()),
                Float.parseFloat(readInColumns(line, "ATOM_Y").trim()),
                Float.parseFloat(readInColumns(line, "ATOM_Z").trim())
        ));
    }


}
