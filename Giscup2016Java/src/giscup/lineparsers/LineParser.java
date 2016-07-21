package giscup.lineparsers;

public interface LineParser {
	/**
	 * 
	 * Parse a line
	 * 
	 * @param xyt result vector
	 * @param line line to be parsed
	 * @return line contains a valid/invalid record
	 * 
	 */
	
	boolean parseLine(int[] xyt, String line);
	
}
