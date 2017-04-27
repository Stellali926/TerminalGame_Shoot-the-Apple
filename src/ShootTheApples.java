// Team members: Yuxuan (Stella) Li, Chenzi Song, Kim Loan Tran
// Project from 03/2016 - 06/2016
import java.util.Scanner;

public class ShootTheApples{

	public static void main(String[] args) {
		
		// use a loop to check whether the user wants to re-play the game after the game finishes each time
		String playAgain ;
		char again;
		
		do {
			
			// game instructions
			System.out.println("-------------------\nShoot the Apples!\n-------------------\n"
					+ "Game Rules:\nThere are 5 apples hidden in the 5X5 trees, "
					+ "you have ten arrows to shoot the apple. (O represents apple, E represents empty spot)"
							+ "\nIf you cannot shoot at least three apples within 10 arrows,"
					+ "you lose!\nIf you can shoot at least 3 apples within 10 arrows,"
					+ "you win!\nIf you shoot all apples within 10 arrows, you are now the master archer!!\n"
					+ "HINT: Everytime you miss the target, we will tell you how many apples are hidden next to the the spot that you guessed\n\n");
	
			// declare the array of the game board, five randomly generated apples, and the index of the shoot
			int[][] board = new int[5][5];
			int[][] apples = new int[5][2];
			int[] shoot = new int[2];
			int[][] checkRepeat = new int[10][2];
			int attempts = 0, // count attempts (remain arrows)
				shotHit=0;	 // count the number of successful hits
			
			initBoard(board); // assigns the value to the game board
			showBoard(board); // prints the board
			initApples(apples); // randomly assigns five spot to five apples
			
			System.out.println("Separately enter the row number and the column number to indicate the spot you want to shoot\n");
			
			// give user 10 times to try
			while (attempts < 10) {
	
				System.out.println("You have " + (10 - attempts) + " arrows");
				shoot(shoot,checkRepeat,attempts); // prompt user to enter the row and column index they want to guess
				
				// check whether the indices user entered is same with apples' indices
				if (hit(shoot,apples)) { 
					System.out.printf("You hit the apple located in (%d,%d)", shoot[0]+1,shoot[1]+1); // if the user hit the apple
					System.out.println();
					shotHit++;
				}
				else { 
					System.out.println("Oops, you miss the target");
					hint(apples,shoot); // if no, give user a hint
				}
				
				// if user find all 5 apples within 10 attempts, the game directly ends
				if (shotHit == 5) {	
					System.out.println("\nBig win! You were born to be an archer! You hit 5 apples within 10 attempts;)" );
					break;
				}
				
				// if the user hit the apple, ready to print "O". ready to print "E"
				changeboard(shoot,apples,board); 
				showBoard(board); // print the board after the change
				
				attempts++;
				
				System.out.println();
			}
			
			// judge whether the user win or lose after 10 attempts
			if (shotHit >= 3 && shotHit < 5)
				System.out.println("\nYou win! You hit " + shotHit + " apples within 10 attempts:)");
			else if (shotHit == 5)
				System.out.println();
			else
				System.out.println("\nYou lose, could not even shoot 3 apples within 10 attempts:(" );

			// show where the apple hide after 10 attempts
			System.out.println("\n\nShow result: ");
			showAllApples(board, apples); // change the value of the spots where all apples hide
			showBoard(board); // print the board where all apples hide
			
			// check whether the player want to play again
			System.out.println("\nDo you want to play again? Enter 'Y'(yes) or 'N'(no)");
			Scanner input = new Scanner(System.in);
			playAgain = input.nextLine();
			again = playAgain.charAt(0);
			
		}while(again == 'Y' || again == 'y');
		
		System.out.printf("|||Thank you for playing the game, bye!|||\n\n\n");
	}
	
	// assign the initial value -1 to all 5x5 array
	public static void initBoard(int[][] board){
		for(int row = 0 ; row < 5 ; row++ )
			for(int column=0 ; column < 5 ; column++ )
				board[row][column]=-1;
	}
	
	// print the board every time after the user enter the indices
	public static void showBoard(int[][] board) {
		System.out.println("    1   2   3   4   5 "); // print the column number
		
		for(int row = 0; row < 5; row++){
			System.out.print((row+1)); // print the row number
			
			for(int column = 0; column < 5; column++){
				System.out.printf("%3s", " | ");
				

				if(board[row][column] == -1){ // print " " (nothing) if the initial value does not change
					System.out.print(" ");
				}
				else if (board[row][column] == 0){ // print "E" if the initial value change from -1 to 0
					System.out.print("E");
				}
				else if (board[row][column] == 1){ // print "O" if the initial value change from -1 to 1
					System.out.print("O");
				}
			}
			System.out.println(" |"); // print last " | " in every even index row
			System.out.println("  |---|---|---|---|---|"); // print the split line in every odd index row
		}
	}
	
	// randomly generate 5 apples, and store its indices in an array
	public static void initApples(int[][] apples){
		
		for (int apple = 0; apple < 5; apple++) {	
			apples[apple][0] = (int)(Math.random() * 5);
			apples[apple][1] = (int)(Math.random() * 5);
			
			// check whether the new randomly generate indices have been already created before
			for (int last = 0; last < apple; last++) {
				if (apple != 0 && apples[apple][0] == apples[last][0] 
						&& apples[apple][1] == apples[last][1])
					apple--;
			}
		}
	}
	
	// prompt user to enter the indices to shoot the apple
	public static void shoot(int[] shoot,int[][] checkRepeat, int attempts) {
		boolean correctInput = false; // check whether the input is valid
		
		while(!correctInput) {
			Scanner input = new Scanner(System.in); // prompt user to separately enter the index of row and column
			
			System.out.print("Row: ");
			shoot[0] = input.nextInt();
			shoot[0]--;
			
			System.out.print("Column: ");
			shoot[1] = input.nextInt();
			shoot[1]--;

			checkRepeat[attempts][0] = shoot[0];
			checkRepeat[attempts][1] = shoot[1];
			
			if (shoot[0] < 0 || shoot[1] < 0 || shoot[0] > 4 || shoot[1] > 4) {
				System.out.print("Invalid input, input again\n");
				continue;
			}
			else
				correctInput = true;
			
			for(int i = 0; i < attempts; i++){
				if (shoot[0] == checkRepeat[i][0] && shoot[1] == checkRepeat[i][1]){
					System.out.println("You already shot this spot, please change one");
					correctInput = false;
					break;
				}
				else
					correctInput = true;
			}

		}
	}
	
	// use boolean method to check whether the shoot indices are same with one of the apples' indices
	public static boolean hit(int[] shoot, int[][] apples) {
		for (int apple=0; apple < apples.length; apple++){
			if ( shoot[0] == apples[apple][0] && shoot[1] == apples[apple][1]){
					return true;
			}
		}
			return false;
	}
	
	
	// if the user didn't hit the apple, give him a hint
	public static void hint(int[][] apples, int[] shoot){
		int appleAround = 0; // count how many apples are around the spot the user guess (max number is 8)
				
		// create an array to represent the difference of the indices of possible 8 spots
		int[][] hintIndex = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}}; 
		for (int i = 0; i < hintIndex.length; i++){
			int hintRow = shoot[0] + hintIndex[i][0];
			int hintColumn = shoot[1] + hintIndex[i][1];
			
			// check if the spot around the guessed spot are out of bound
			if (hintRow >= 0 && hintRow < 5 && hintColumn >= 0 && hintColumn < 5) {
				// if not out of bound, check whether there are apples in every indices
				for (int apple = 0; apple < apples.length; apple++){
					if (hintRow == apples[apple][0] && hintColumn == apples[apple][1]) 
						appleAround++;					
				}
			}
			
		}
		
		System.out.println("Hint: there are " + appleAround + " apples around the spot you guessed (include apples you already guessed)");
	}
	
	// change the initial value -1 to 1 if the user hit the apple, change -1 to 0 if the user does not hit the apple
	public static void changeboard(int[] shoot, int[][] apples, int[][] board){
		if(hit(shoot,apples))
			board[shoot[0]][shoot[1]]=1;
		else 
			board[shoot[0]][shoot[1]]=0;
	}
	
	// change initial values of all places apple hided to 1
	public static void showAllApples(int[][] board, int[][] apples) {
		
		int n,m;
		for(int apple = 0; apple < 5; apple++ ) {
			n = apples[apple][0];
			m = apples[apple][1];
			board[n][m] = 1;
		}
	}
}

