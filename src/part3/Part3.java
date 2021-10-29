package part3;

/* 
 * Uppgift 3
 *  
 *  
 *                     |   1|
 *                 |  1|   3|  1|
 *             |  1|  3|   9|  3|  1|
 *          | 1|  3|  9|  27|  9|  3| 1|
 *       | 1| 3|  9| 27|  81| 27|  9| 3| 1|
 *     |1| 3| 9| 27| 81| 243| 81| 27| 9| 3|1|
 *   |1|3| 9|27| 81|243| 729|243| 81|27| 9|3|1|
 * |1|3|9|27|81|243|729|2187|729|243|81|27|9|3|1|
 * 
 * Som multiplikationsnörd och escapesekvensfantast vill jag ha ett program som med nästlade loopar skriver ut 
 * ovanstående pyramid i konsolen.
 * 
 */

public class Part3 {
	private static int[][] table;
	private static int calcedDepth = -1;
	private final int depth;
	private final int[] colWdhs; 
	
	public Part3(int maxDep, boolean debug) {
		depth = maxDep;

		// We use a static table and only grow it when an object of larger max depth is created. 
		if ( maxDep > Part3.calcedDepth ) {
			Part3.calcedDepth = maxDep;
			Part3.table = genTable(maxDep, debug);
		}

		// We only need to calculate the paddings of the last row, as this contains the largest numbers and the rows
		// above it will align according to these widths.
		colWdhs = Formatter.calcColPadWidths(table[depth-1]);
	}
	
	public void run(PadSide padSide) {
		Formatter.printTable(table, depth, colWdhs, padSide); 
	}

	private int[][] genTable(int maxDepth, boolean debug) {
		int staIdx = Part3.table != null ? table.length : 0;

		if ( debug ) System.out.println("Generating new table from index " + staIdx +"...");

		int[][] table = new int[maxDepth][];

		// The last row contains all the powers that are going to be used in the pyramid, so we calculate it first and 
		// we get the last row for free!
		int[] pwrs = genPowers(maxDepth-1);

		// Copy previous table without generation
		for ( int rowIdx = 0; rowIdx < staIdx; rowIdx++ ) {
			table[rowIdx] = Part3.table[rowIdx];
		}

		// Continue on existing row and generate the rest...
		for ( int rowIdx = staIdx; rowIdx < maxDepth-1; rowIdx++ ) {
			table[rowIdx] = genRow(rowIdx, pwrs);
		}
		
		// The last row is already calculated to get all the powers.
		table[table.length-1] = pwrs;

		return table;
	}
	
	private int[] genRow(int rowIdx, int[] pwrs) {
		int rowLen = getRowLen(rowIdx);
		int midIdx = getRowMid(rowIdx);
		int[] row = new int[rowLen];

		// Each row is symmetric, only iterate first half of the list....
		for ( int exn = 0; exn < midIdx; exn++ ) {
			// pwrs index corresponds with the exponent! We ju
			int power = pwrs[exn];

			row[exn] = power;
			// ... and add the value counting indexes from the right too and iterate only half row.
			row[getIdxFrRgh(rowLen, exn)] = power;
		}

		return row;
	}
	
	private int[] genPowers(int lstRowIdx) {
		int[] pwrs = new int[getRowLen(lstRowIdx)];

		for ( int i = 0; i<getRowMid(lstRowIdx); i++ ) {
			pwrs[i] = (int) Math.pow(3, i);
			pwrs[getIdxFrRgh(pwrs.length, i)] = (int) Math.pow(3, i);
		}
		return pwrs;
	}
	
	private int getRowLen(int rowIdx) {
		return 2*rowIdx+1; // As seen in the example at the end of this file, row length follows the pattern 2*rowInd+1. 
	}

	private int getRowMid(int rowIdx) {
		return rowIdx+1; // Since the total row length is 2*rowIdx, this is middle with right-way bias.
	}
	
	private int getIdxFrRgh(int rowLen, int idx) {
		return rowLen - idx - 1;
	}
}

class Formatter {
	private static int depth;
	private static int[] colWdhs;

	public static void printTable(int[][] table, int depth, int[] colWdhs, PadSide padSide) {
		String colDelim = " ";

		Formatter.depth = depth;
		Formatter.colWdhs = colWdhs;

		for ( int rowIdx = 0; rowIdx<depth; rowIdx++ ) {
			int[] row = table[rowIdx];
			String rowIndentPad = getRowIndentPad(row, rowIdx, colDelim.length());
			String rowPretty = getRowStr(row, rowIdx, colDelim, padSide); 
			System.out.println( rowIndentPad + rowPretty );
		}
		System.out.println("");
	}
		
	public static int[] calcColPadWidths(int[] row) {
		int[] colPaddings = new int[row.length];

		for ( int i=0; i<row.length; i++) {
			int strLen = getIntPrintLen(row[i]);
			colPaddings[i] = strLen;
		}
		
		return colPaddings;
	}

	private static String getRowStr(int[] row, int crtDep, String colDelim, PadSide padSide) {
		String ret = colDelim;
		
		int colWdhStaIdx = depth - crtDep - 1;

		for ( int colIdx = 0; colIdx < row.length; colIdx++ ) {
			int paddingIdx = colWdhStaIdx+colIdx;

			int colVal = row[colIdx];
			int colWdh = colWdhs[paddingIdx];
			
			ret += padNum(colVal, colWdh, padSide)+colDelim;
		}
		
		return ret;
	}
		
	private static String padNum(int num, int wdh, PadSide padside) {
		int strLen = getIntPrintLen(num);
		int padRep = wdh-strLen;
			
		if ( padside == PadSide.MID ) {
			int lftPadLen = (padRep/2)+(padRep%2);
			int rghPadLen = padRep/2;
			return " ".repeat(lftPadLen) + num + " ".repeat(rghPadLen);
		} 

		String padding = " ".repeat(padRep);	
		return padside == PadSide.RIGHT ? num + padding : padding + num;
	}
	
	static private int getIntPrintLen(int num) { 
		return num == 0 ? 1 : (int) Math.log10(num) + 1; 
	}
	
	private static String getRowIndentPad(int[] row, int crtDep, int DelimLen) {
		int reps = 0;
		int colWdhMid = colWdhs.length / 2;

		int staIdx = 0;
		int endIdx = colWdhMid-crtDep;

		// Iterate over the columnWidths array and get the length sum:
		for (int i=staIdx; i<endIdx; i++) {
			reps+=colWdhs[i]+DelimLen;
		}
		
		return " ".repeat(reps);
	}
	
}

//		debugInfoList.add(staIdx + ", " + endIdx + " | " + Arrays.toString(Arrays.copyOfRange(colWdhs, staIdx, endIdx)));
//	int[] debugInfo = new int[1000];
//	List<String> debugInfoList=new ArrayList<String>();  
//	void printDebugInfo() {
//		debugInfoList.add("List:");
//		System.out.println("DebugInfo:");
//		System.out.println(Arrays.toString(debugInfo));
//		for ( String line : debugInfoList ) {
//			System.out.println(line);
//		}
//		System.out.print(Arrays.toString(colWdhs));
//	}

/*
 * 0 1                  1
 * 1 3              1   3  1
 * 2 5           1  3   9  3  1
 * 3 7        1  3  9  27  9  3 1
 * 4 9      1 3  9 27  81 27  9 3 1
 * 5     1 3 9 27 81 243 81 27 9 3 1
 * 
 * 0 1              0
 * 1 3           0  1  0
 * 2 5        0  1  2  1  0
 * 3 7     0  1  2  3  2  1  0
 * 4 9  0  1  2  3  4  3  2  1  0
 * 
 *          
 * 
 * depth     row]dx => colWdhIdx
 *     length
 * 00) [01]: 00=>07
 * 01) [03]: 00=>06 01=>07 02=>08                
 * 02) [05]: 00=>05 01=>06 02=>07 03=>08 04=>09
 * 03) [07]: 00=>04 01=>05 02=>06 03=>07 04=>08 05=>09 06=>10
 * 04) [09]: 00=>03 01=>04 02=>05 03=>06 04=>07 05=>08 06=>09 07=>10 08=>11 
 * 05) [11]:
 * 06) [13]:
 * 07) [15]:
 * 
 * colWdhIdx:
 * [15] 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14
 *      [ a, b, c, d, e, f, g, h, i, i, j, k, l, m, n ]
 *      [ 1, 1, 1, 2, 2, 3, 3, 4, 3, 3, 2, 2, 1, 1, 1]
 *     
 *                         729   729
 *      | 1| 3| 9|27|81|  |  |  |  |  |81|27| 9| 3| 1|
 *                     243    2187  243
 *                     
 * BRAINSTORM:
 * (int) 15/2 = 7    ### colWdhMid
 *                     
 * staIdx = rowIdx + (maxDep - curDep)
 */