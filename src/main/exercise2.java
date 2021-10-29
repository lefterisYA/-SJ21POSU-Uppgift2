package main;

import java.util.Scanner;

import part1.*;
import part2.*;
import part3.*;

public class exercise2
{
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int usrChoice = 1;
		boolean debug = false;
		
		String debugUsrStr; //
		 
		Part1 part1 = new Part1(); 
		Part2 part2 = new Part2(); 
		Part3 part3;

		loop: while ( true ) {
			debugUsrStr = debug ? "Inaktivera" : "Aktivera";
			System.out.println("Var god välj uppgift att köra:");
			System.out.println(
					"0. Avsluta\n" +
					"\n" +
					"1. Bokstavsräknare\n" +
					"2. Lotteri\n" +
					"3. Lotteri Bonus\n" +
					"4. Multiplikationspyramid\n" + 
					"\n" +
					"5. " + debugUsrStr + " fusk, extra info och extra val."
					);
			
			usrChoice = Integer.parseInt(sc.next());
			
			sc.nextLine(); // "Clear" scanner buffer for some reason....
			
			long startTime = System.nanoTime();

			switch (usrChoice) {
				case 0:
					break loop;
	
				case 1:
					part1.run(sc);
					break;
	
				case 2:
					part2.run(sc, debug, false);
					break;
	
				case 3:
					part2.run(sc, debug, true);
					break;
	
				case 4:
					if ( debug ) {
						System.out.println("Ange önskad pyramid-djup. 8 ger max 2187.");
						int depth = Integer.parseInt(sc.next());
						for ( int i=1; i<=depth; i++) {
							part3 = new Part3(i, debug); 
							part3.run(PadSide.LEFT);
						}
					} else {
						part3 = new Part3(8, debug); 
						part3.run(PadSide.LEFT);
					}
					break;
	
				case 5:
					debug = !debug;
					continue;
	
				default:
					System.out.println("Ogiltig val!");
			}

			long endTime = System.nanoTime();
			long dur = endTime - startTime;
			long durMil = dur/1000000;		
			System.out.println("Körning klar! Tid: " + durMil + "millisekunder\n");

		}
		sc.close();
	}
}
