package part1;

/* 
 * Uppgift 1
 * 
 * Svenska språket har följande vokaler:
 * 
 * a, e, i, o, u, y, å, ä och ö
 * 
 * Samt följande konsonanter:
 * 
 * b, c, d, f, g, h, j, k, l, m, n, p, q, r, s, t, v, z och x
 * User story
 * 
 * Som användare vill jag kunna mata in en mening i ett program, och få presenterat för mig hur många vokaler 
 * respektive konsonanter som min mening innehåller.
 * 
 */

import java.util.Scanner;

public class Part1 {
	static final char[] vocals = { 'a', 'e', 'i', 'o', 'u', 'y', 'å', 'ä', 'ö' };
	static final char[] consonants = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 
			't', 'v', 'z', 'w', 'x' }; // w wasn't included as per the exercise, assuming that's a typo.

	public void run(Scanner sc) {
		System.out.println("Skriv en mening: ");
		String usrInp = sc.nextLine();
		int[] res = countChars(usrInp);
		
		System.out.println("Din mening innehåller " + res[0] + " vokaler och " + res[1] + " konsonanter.");
	}
	
	// Returns an int[] of length two; Should be done with tuple.
	private static int[] countChars(String inp) {
		int vocalCnt = 0;
		int consoCnt = 0;

		for ( char c : inp.toCharArray() ) {
			if ( isVocal(Character.toLowerCase(c)) ) {
				vocalCnt++;
			} else if ( isConso(Character.toLowerCase(c)) ) {
				consoCnt++;
			} 
		}

		int[] ret = { vocalCnt, consoCnt };
		
		return ret;
	}
	
	private static boolean isVocal(char inpChar) {
		for ( char c : vocals ) {
			if ( c == inpChar )
				return true;
		}
		return false;
	}

	private static boolean isConso(char inpChar) {
		for ( char c : consonants ) {
			if ( c == inpChar )
				return true;
		}
		return false;
	}
}