package part2;

/*
 * Ett lottoprogram slumpar fram ett tvåsiffrigt tal mellan 01 och 99. Om användare gissar rätt siffra i rätt ordning, 
 * vinner användaren 10 000 kr. Om användaren gissar rätt siffra, men i fel ordning, vinner användaren 5 000 kr. 
 * Om användare gissar ett rätt siffra av de två, vinner användaren 1 000 kr.
 * 
 * Exempel: Slumpen är 23. Jag gissar på 12 ger en vinst på 1000.
 * Exempel: Slumpen är 23. Jag gissar 32 ger vinst på 5000
 */

import java.util.Random;
import java.util.Scanner;

public class Part2 {
	public void run(Scanner sc, boolean debug) {
		run(sc, debug, false);
	}

	public void run(Scanner sc, boolean debug, boolean isBonus) {
		NumPair rndNums;
		rndNums = RndGen.getTwoRandomDigtis(isBonus);

		if (debug) System.out.println("Rätt svar: " + rndNums.lft + rndNums.rgh);

		NumPair usrInp = getUsrInput(sc);
		
		int wonAmount = chkWin(rndNums, usrInp);
		
		if ( wonAmount > 0 )
			System.out.println("Grattis! \nDu vann " + wonAmount + "kr!!!");
		else
			System.out.println(
					"Tyvärr! Båda tal var fel. :(\n"+
					"Rätt svar var: " + rndNums.lft + rndNums.rgh
					);
	}

	private static int chkWin(NumPair rndNums, NumPair usrInp) {
		if      ( usrInp.lft == rndNums.lft && usrInp.rgh == rndNums.rgh )
			return 10000;

		else if ( usrInp.lft == rndNums.rgh && usrInp.rgh == rndNums.lft )
			return 5000;

		else if ( usrInp.lft == rndNums.lft || usrInp.rgh == rndNums.rgh ||
				  usrInp.lft == rndNums.rgh || usrInp.rgh == rndNums.lft )
			return 1000;
		
		/* 
		 * I also solved the last case by treating the user input and random numbers as arrays of length 2 and iterating
		 * through them. The code was neither clearer nor more efficient since we test for the other cases already....
		 * 
		 * I think this would be the most efficient but code readability suffers:
		 * if      ( usrInp.lft == rndNums.lft )
		 * 		return ( usrInp.rgh == rndNums.rgh ) ? 10000 : 1000;
		 * 
		 * else if ( usrInp.lft == rndNums.rgh ) 
		 *  	return ( usrInp.rgh == rndNums.lft ) ? 5000 : 1000;
		 *  
		 * else if ( usrInp.rgh == rndNums.rgh || usrInp.rgh == rndNums.lft )
		 *   	return 1000;
		 */
		return 0;
	}

	private static NumPair getUsrInput(Scanner sc) {
		while ( true ) {
			System.out.println("Skriv ett tal mellan 01 till 99:");

			// We want the user to explicitly input 02,03... instead of 2,3 etc... so we won't use nextInt().
			String usrInpStr = sc.next();

			try { // parseInt will check for non numeric chars.
				if ( usrInpStr.length() != 2 )
					throw new Exception("Wrong length");

				int lft = Integer.parseInt(usrInpStr.substring(0, 1));
				int rgh = Integer.parseInt(usrInpStr.substring(1, 2));
				
				if ( lft == 0 && rgh == 0 )
					throw new Exception("Wrong range.");

				return new NumPair(lft, rgh);
			} catch ( Exception e ) {
				System.out.println("Felaktig inmatning.");
			}
		}
	}
	
}

class RndGen {
	private static Random rnd = new Random();
	
	public static NumPair getTwoRandomDigtis(boolean unique) {
		return unique ? getTwoUniqRndDigits() : getTwoRndDigits();
	}

	private static NumPair getTwoRndDigits() {
		int twoDigNum = rnd.nextInt(99)+1; // 1-99
		return new NumPair(twoDigNum/10, twoDigNum%10);
	}

	// Bonus
	private static NumPair getTwoUniqRndDigits() {
		int fstNum = rnd.nextInt(10);
		int[] bag = createBagWithout(fstNum);
		// System.out.println("Bag:" + Arrays.toString(bag));
		return new NumPair( fstNum, bag[rnd.nextInt(9)] );
	}
	
	// Returns an array of length 9 with all nums 0-9 except the parameter "removedNum".
	private static int[] createBagWithout(int removedNum) {
		int[] bag = new int[9];
		int u=0;
		for ( int i=0; i<10; i++ ) {
			if ( i != removedNum ) {
				bag[u] = i;
				u++;
			} 
		}
		return bag;
	}
}

class NumPair {
	public final int lft;
	public final int rgh;

	public NumPair(int fst, int scd) {
		lft = fst;
		rgh = scd;
	}
}
