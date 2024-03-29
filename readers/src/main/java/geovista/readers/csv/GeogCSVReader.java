/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.readers.csv;

import geovista.readers.example.GeoData48States;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

public class GeogCSVReader {

    public static final int DATA_TYPE_INT = 0;
    public static final int DATA_TYPE_DOUBLE = 1;
    public static final int DATA_TYPE_STRING = 2;

    public static final double NULL_DOUBLE = Double.NaN;
    public static final int NULL_INT = -1 * Integer.MAX_VALUE;
    public static final String[] NULL_STRINGS = new String[3];
    public static final String NULL_STRING = "";
    public static final String NULL_STRING_TWO = "-999";
    public static final String NULL_STRING_THREE = "NA";

    final static Logger logger = Logger
	    .getLogger(GeogCSVReader.class.getName());

    private char commaDelimiter = ",".toCharArray()[0];
    private char tabDelimiter = "\t".toCharArray()[0];
    private char defaultDelimiter = commaDelimiter;
    private char currDelimiter = defaultDelimiter;

    /**
   
     */

    public GeogCSVReader() {
	NULL_STRINGS[0] = NULL_STRING;
	NULL_STRINGS[1] = NULL_STRING_TWO;
	NULL_STRINGS[2] = NULL_STRING_THREE;
    }

    public GeogCSVReader(char delimiter) {
	NULL_STRINGS[0] = NULL_STRING;
	NULL_STRINGS[1] = NULL_STRING_TWO;
	NULL_STRINGS[2] = NULL_STRING_THREE;
	this.currDelimiter = delimiter;

    }

    public Object[] readFileStreaming(InputStream is, ArrayList<Integer> columns) {

	BufferedReader in = new BufferedReader(new InputStreamReader(is));
	Iterable<CSVRecord> parser = null;
	try {
	    parser = CSVFormat.DEFAULT.withDelimiter(this.currDelimiter).parse(
		    in);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	int count = 0;
	for (CSVRecord rec : parser) {

	    // eDays.add(rec.get(0));
	    // type.add(rec.get(10) + " - " + rec.get(8));

	    System.out.println(rec.get(0));
	    System.out.println(rec.toString());
	    count++;
	}
	// CSVParser shredder = new CSVParser()
	// CSVParser shredder = new CSVParser(is);

	// shredder.setCommentStart("#;!");
	// shredder.setEscapes("nrtf", "\n\r\t\f");
	String[] headers = null;
	String[] types = null;
	int[] dataTypes = null;
	String[][] fileContent = null;
	int dataBegin;
	Object[] data;
	try {
	    // fileContent = shredder.getAllValues();

	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	types = fileContent[0];// first line tells us types
	dataTypes = new int[types.length];
	int len;
	if (types[0].equalsIgnoreCase("int")
		|| types[0].equalsIgnoreCase("double")
		|| types[0].equalsIgnoreCase("string")) {
	    dataBegin = 2;
	    headers = fileContent[1];
	    data = new Object[headers.length + 1];// plus one for the headers
						  // themselves
	    len = fileContent.length - dataBegin;
	    for (int i = 0; i < headers.length; i++) {
		if (types[i].equalsIgnoreCase("int")) {
		    data[i + 1] = new int[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_INT;
		} else if (types[i].equalsIgnoreCase("double")) {
		    data[i + 1] = new double[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_DOUBLE;
		} else if (types[i].equalsIgnoreCase("string")) {
		    data[i + 1] = new String[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_STRING;
		} else {
		    throw new IllegalArgumentException(
			    "GeogCSVReader.readFile, unknown type = "
				    + types[i]);
		}
	    }
	} else {
	    dataBegin = 1;
	    headers = fileContent[0];
	    data = new Object[headers.length + 1];// plus one for the headers
						  // themselves
	    len = fileContent.length - dataBegin;
	    for (int i = 0; i < headers.length; i++) {
		String firstString = fileContent[1][i];
		String secondString = fileContent[2][i];
		String thirdString = fileContent[3][i];
		String lastString = fileContent[fileContent[0].length][i];

		if (isNumeric(firstString) && isNumeric(secondString)
			&& isNumeric(thirdString) && isNumeric(lastString)) {
		    if (isInt(fileContent, i) == false) {
			// if (isDouble(firstString) || isDouble(secondString)
			// || isDouble(thirdString) || isDouble(lastString)) {
			data[i + 1] = new double[len];
			dataTypes[i] = GeogCSVReader.DATA_TYPE_DOUBLE;
		    } else {
			data[i + 1] = new int[len];
			dataTypes[i] = GeogCSVReader.DATA_TYPE_INT;
		    }
		} else {
		    data[i + 1] = new String[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_STRING;
		}
	    }
	}
	data[0] = headers;

	String[] line = null;

	for (int row = dataBegin; row < len + dataBegin; row++) {

	    line = fileContent[row];

	    int[] ints = null;
	    double[] doubles = null;
	    String[] strings = null;

	    for (int column = 0; column < line.length; column++) {
		String item = line[column];
		if (dataTypes[column] == GeogCSVReader.DATA_TYPE_INT) {

		    if (Arrays.binarySearch(GeogCSVReader.NULL_STRINGS, item) >= 0) {
			ints = (int[]) data[column + 1];
			ints[row - dataBegin] = GeogCSVReader.NULL_INT;
		    } else {
			ints = (int[]) data[column + 1];
			try {
			    ints[row - dataBegin] = Integer.parseInt(item);
			} catch (NumberFormatException nfe) {
			    logger.warning("could not parse " + item
				    + " in column " + column);
			    // nfe.printStackTrace();
			    ints[row - dataBegin] = GeogCSVReader.NULL_INT;
			}
		    }
		} else if (dataTypes[column] == GeogCSVReader.DATA_TYPE_DOUBLE) {
		    if (Arrays.binarySearch(GeogCSVReader.NULL_STRINGS, item) >= 0) {
			doubles = (double[]) data[column + 1];
			doubles[row - dataBegin] = GeogCSVReader.NULL_DOUBLE;
		    } else {
			doubles = (double[]) data[column + 1];
			doubles[row - dataBegin] = parseDouble(item);
		    }
		} else if (dataTypes[column] == GeogCSVReader.DATA_TYPE_STRING) {
		    strings = (String[]) data[column + 1];
		    strings[row - dataBegin] = item;
		} else {
		    throw new IllegalArgumentException(
			    "GeogCSVReader.readFile, unknown type = "
				    + types[row]);
		}// end if

	    }// next column
	} // next row
	return data;

    }

    public Object[] readFileNew(InputStream is) {
	// get first line

	BufferedReader in = new BufferedReader(new InputStreamReader(is));
	Iterable<CSVRecord> parser = null;
	try {
	    parser = CSVFormat.DEFAULT.withDelimiter(this.currDelimiter).parse(
		    in);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * An example of reading using CsvListReader.
     */
    private static void readWithCsvListReader(String fileName) throws Exception {

	ICsvListReader listReader = null;
	try {
	    listReader = new CsvListReader(new FileReader(fileName),
		    CsvPreference.STANDARD_PREFERENCE);

	    listReader.getHeader(true); // skip the header (can't be used with
					// CsvListReader)
	    final CellProcessor[] processors = null;

	    List<Object> customerList;
	    while ((customerList = listReader.read(processors)) != null) {
		System.out.println(String.format(
			"lineNo=%s, rowNo=%s, customerList=%s",
			listReader.getLineNumber(), listReader.getRowNumber(),
			customerList));
	    }

	} finally {
	    if (listReader != null) {
		listReader.close();
	    }
	}
    }

    private String[] readLine(ICsvListReader reader) {
	List<String> lineList = null;
	try {
	    lineList = reader.read();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SuperCsvException e) {
	    e.printStackTrace();
	}
	if (lineList == null) {
	    return new String[0];
	}
	String[] array = lineList.toArray(new String[lineList.size()]);
	return array;
    }

    public String[][] readFileToStrings(InputStream is) {
	String[][] returnStrings = (String[][]) this.readFile(is);
	return returnStrings;
    }

    private CsvPreference prefs() {
	CsvPreference standard = CsvPreference.STANDARD_PREFERENCE;

	CsvPreference.Builder prefBuilder = new CsvPreference.Builder(
		(char) standard.getQuoteChar(), (int) this.currDelimiter,
		standard.getEndOfLineSymbols());
	// prefBuilder.useQuoteMode(new AlwaysQuoteMode());
	CsvPreference prefs = prefBuilder.build();

	return prefs;

    }

    public Object[] readFile(InputStream is) {
	ICsvListReader listReader = null;
	// CSVParser shredder = new CSVParser(is);
	// shredder.setCommentStart("#;!");
	// shredder.setEscapes("nrtf", "\n\r\t\f");
	String[] headers = null;
	String[] types = null;
	int[] dataTypes = null;
	String[][] fileContent = null; // not including headers
	int dataBegin;
	Object[] data;
	try {

	    listReader = new CsvListReader(new InputStreamReader(is),
		    this.prefs());
	    // Thread.dumpStack();

	    headers = this.readLine(listReader);

	    String[] line = null;
	    ArrayList<String[]> lines = new ArrayList<String[]>();
	    while ((line = this.readLine(listReader)).length > 0) {
		lines.add(line);
	    }
	    // String[] firstLine = lines.get(0);
	    int nColumns = headers.length;
	    int nRows = lines.size();

	    fileContent = new String[nRows][nColumns];
	    for (int row = 0; row < nRows; row++) {
		for (int column = 0; column < nColumns; column++) {
		    String aString = lines.get(row)[column];
		    fileContent[row][column] = aString;
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	// types = this.readLine(listReader);// first line tells us types
	// (maybe)
	// types = fileContent[0];
	types = headers;
	dataTypes = new int[types.length];
	int len;
	if (types[0].equalsIgnoreCase("int")
		|| types[0].equalsIgnoreCase("double")
		|| types[0].equalsIgnoreCase("string")) {
	    dataBegin = 1;
	    headers = fileContent[0];
	    data = new Object[headers.length + 1];// plus one for the headers
						  // themselves
	    len = fileContent.length - dataBegin;
	    for (int i = 0; i < headers.length; i++) {
		if (types[i].equalsIgnoreCase("int")) {
		    data[i + 1] = new int[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_INT;
		} else if (types[i].equalsIgnoreCase("double")) {
		    data[i + 1] = new double[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_DOUBLE;
		} else if (types[i].equalsIgnoreCase("string")) {
		    data[i + 1] = new String[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_STRING;
		} else {
		    throw new IllegalArgumentException(
			    "GeogCSVReader.readFile, unknown type = "
				    + types[i]);
		}
	    }
	} else {
	    // sniff the types (is there a better way?)
	    dataBegin = 0;
	    // headers = firstLine;
	    data = new Object[headers.length + 1];// plus one for the headers
						  // themselves
	    len = fileContent.length - dataBegin;
	    for (int i = 0; i < headers.length; i++) {
		String firstString = fileContent[1][i];
		String secondString = fileContent[2][i];
		String thirdString = fileContent[3][i];
		int lastRowNum = fileContent.length - 1;// -2
		String lastString = fileContent[lastRowNum][i];

		if (isNumeric(firstString) && isNumeric(secondString)
			&& isNumeric(thirdString) && isNumeric(lastString)) {
		    if (isInt(fileContent, i) == false) {
			// if (isDouble(firstString) || isDouble(secondString)
			// || isDouble(thirdString) || isDouble(lastString)) {
			data[i + 1] = new double[len];
			dataTypes[i] = GeogCSVReader.DATA_TYPE_DOUBLE;
		    } else {
			data[i + 1] = new int[len];
			dataTypes[i] = GeogCSVReader.DATA_TYPE_INT;
		    }
		} else {
		    data[i + 1] = new String[len];
		    dataTypes[i] = GeogCSVReader.DATA_TYPE_STRING;
		}
	    }
	}
	data[0] = headers;

	String[] line = null;

	for (int row = dataBegin; row < len + dataBegin; row++) {

	    line = fileContent[row];

	    int[] ints = null;
	    double[] doubles = null;
	    String[] strings = null;

	    for (int column = 0; column < line.length; column++) {
		String item = line[column];
		if (item == null) {
		    item = ""; // horrid hack
		}
		if (dataTypes[column] == GeogCSVReader.DATA_TYPE_INT) {

		    if (Arrays.binarySearch(GeogCSVReader.NULL_STRINGS, item) >= 0) {
			ints = (int[]) data[column + 1];
			ints[row - dataBegin] = GeogCSVReader.NULL_INT;
		    } else {
			ints = (int[]) data[column + 1];
			try {
			    ints[row - dataBegin] = Integer.parseInt(item);
			} catch (NumberFormatException nfe) {
			    logger.warning("could not parse " + item
				    + " in column " + column);
			    // nfe.printStackTrace();
			    ints[row - dataBegin] = GeogCSVReader.NULL_INT;
			}
		    }
		} else if (dataTypes[column] == GeogCSVReader.DATA_TYPE_DOUBLE) {
		    if (Arrays.binarySearch(GeogCSVReader.NULL_STRINGS, item) >= 0) {
			doubles = (double[]) data[column + 1];
			doubles[row - dataBegin] = GeogCSVReader.NULL_DOUBLE;
		    } else {
			doubles = (double[]) data[column + 1];
			doubles[row - dataBegin] = parseDouble(item);
		    }
		} else if (dataTypes[column] == GeogCSVReader.DATA_TYPE_STRING) {
		    strings = (String[]) data[column + 1];
		    strings[row - dataBegin] = item;
		} else {
		    throw new IllegalArgumentException(
			    "GeogCSVReader.readFile, unknown type = "
				    + types[row]);
		}// end if

	    }// next column
	} // next row
	return data;

    }

    private static boolean isInt(String[][] data, int col) {
	for (int i = 1; i < data.length; i++) {
	    if (isDouble(data[i][col]) == true) {
		return false;
	    }
	}
	return true;
    }

    private static boolean isDouble(String firstString) {
	if (firstString == null) {
	    return true;
	}
	return firstString.lastIndexOf(".") >= 0 || firstString.equals("");
    }

    private static boolean isNumeric(String str) {
	if (str == null) {
	    str = ""; // XXX horrid hack
	    return true;
	}
	for (String nullStr : NULL_STRINGS) {
	    if (str.equals(nullStr)) {
		return true;
	    }
	}
	return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
						// '-' and decimal.
    }

    private static double parseDouble(String input) {
	double returnVal = Double.NaN;
	try {
	    returnVal = Double.parseDouble(input);
	} catch (NumberFormatException ex) {
	    logger.fine("forced " + input + " to NaN");
	}

	return returnVal;
    }

    public static void main(String[] args) {
	GeoData48States geodata = new GeoData48States();
	logger.info("n obs = " + geodata.getDataForApps().getNumObservations());
	logger.info("all done!");

    }

}