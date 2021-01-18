/*Battleship
Benjamin Rockman
Mr. Krnic
April 5, 2019

In this program, file IO is used for scoring. The computer reads the data from the leaderboard
txt when displaying the highscores, and also when getting them to store them in an array to be sorted.
The program takes a new score from the recent game, stores it with the other scores that were just read,
and then outputs all of these scores into a txt file.


The basis of this game is a 2D array. The boards are essentially grids with horizontal and vertical
axes. Each player places their ships on the 2D array, either across one or multiple rows. Each player guesses a square on a
2D array based on where they think the opponent's ship is.
 */

/*************************
*****ARRAYS USED HERE*****
 ***************************/
/*************************
 *****SELECTION USED HERE*****
 ***************************/
/*************************
 *****REPETITION USED HERE*****
 ***************************/
/*************************
 *****SCANNER USED HERE*****
 ***************************/


//package Battleship;
import java.io.*; //For file IO
import java.util.*; //For Scanner and sorting

public class Main {

    //Global variables so they can be reset in a separate function
    public static Scanner scan = new Scanner(System.in);//Scanner for user input
    public static boolean askAgain = false;//Bool to represent whether the user needs to reposition their ship
    public static int score = 0;//Int to hold score
    public static String[][] pOneBoard;//Player one's board
    public static String[][] pTwoBoard;//Player two's board
    public static String[][] pOneGuessBoard;//Player one's guess board
    public static String[][] pTwoGuessBoard;//Player two's guess board
    public static String pOneName;//Player one name
    public static String pTwoName;//Player two name
    public static boolean shipSetupOneDone = false;//Bool to represent if P1 is done setting up ships
    public static boolean shipSetupTwoDone = false;//Bool to represent if P2, is done setting up ships
    public static int setupCount = 0;//Counter variable to cycle through ships array when setting up
    public static String winner = "";//Name of the winner

    public static void cls()
    {
        /*
        This function is for clearing the CMD screen when running the exe version of the game.
         */
        try
        {
            System.out.println("Press enter key to continue...");
            if(scan.nextLine() != null) //Waits for the user to hit the enter key before clearing the screen
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();//Sends a cls command to CMD
        }catch(Exception E)
        {
            System.out.println(E);
        }
    }

    public static void PrintBoard(String[][] board){
       /*
       This function is for printing the board out in a nice looking way.
       Parameters:
       board: the 2D array that is printed out in a grid format;
        */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        String[] verticalAxis;//Array to hold all the y axis letters
        verticalAxis = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};//Initializing y axis array
        System.out.printf("%-5s", ""); //Space before the first horizontal marking, makes it so it lines up with grid
        for (int x = 0; x < board[0].length; x++) { //Loops as long as first row
            System.out.printf("%-5d", x);//Print the x axis numbers
        }

        System.out.println();//Empty line before printing the board
        for (int i = 0; i < board.length; i++) {//Loops for length of board
            System.out.printf("%-5s", verticalAxis[i]);//Print out the y axis letter
            for (int j = 0; j < board[0].length; j++) {//Loops as long as row length
                System.out.printf("%-5s", board[i][j]);//Print the row
            }
            System.out.println();//Empty line so each row is on its own line
        }
    }
    public static int RowToInt(String rowString){
        /*
        Function for converting user input row string into a row int
        Parameters:
        rowString: a string that holds the row the user inputted when enter coords
        Returns a converted y axis value, in int form instead of string
         */
        /*************************
         *****SELECTION USED HERE*****
         ***************************/

        int rowInt = -1;//Set initial value to -1
        //For each correspondig y axis letter, set the integer to the actually row number
        if (rowString.equals("A"))rowInt = 0;
        else if ((rowString.equals("B")))rowInt = 1;
        else if ((rowString.equals("C")))rowInt = 2;
        else if ((rowString.equals("D")))rowInt = 3;
        else if ((rowString.equals("E")))rowInt = 4;
        else if ((rowString.equals("F")))rowInt = 5;
        else if ((rowString.equals("G")))rowInt = 6;
        else if ((rowString.equals("H")))rowInt = 7;
        else if ((rowString.equals("I")))rowInt = 8;
        else if ((rowString.equals("J")))rowInt = 9;

        return rowInt;//Return the row as an integer
    }


    public static String[][] SetShip(String[][] board, String shipName, int shipLength, String shipLetter) {
        /*
        Variable for setting the ship and coodinates up before placing it on the board. This has multiple checks to
        see if coords are valid.
        Parameters:
        board: the board that is having a ship set on it
        shipName: the name of the ship being set up
        shipLength: the length of the ship being set up
        shipLetter: the letter that goes on the board to represent the ship
        Return the board with the ship placed on it
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        /*************************
         *****SCANNER USED HERE*****
         ***************************/
        boolean isErrorInCords;//Bool to represent if the is an error in the user's coords
        int startRow = 0;//Int for the starting row of ship
        int startCol = 0;//Int for the ending col of ship
        int endRow = 0;//Int for the starting row of ship
        int endCol = 0;//Int for the ending col of ship
        askAgain = false;//Initialize ask again to false;
        String placement;//String to hold user placement input
        String confirm;//String to hold user's confirmation answer
        do {
            isErrorInCords = false;//Sett error to false
            //Prompt the user for a placement
            System.out.println("Enter the start and end coordinates of your " + shipName + ", Length: " + shipLength + ", separated by colons and commas ('A:1,A:4')");
            placement = scan.nextLine();//Scan for placement
            if(!FormatChecker(placement, shipLength)){//Call on the format checker to make sure the format is correct, if its not run code below
                isErrorInCords = true;//Set error to true
                //Tell user they made a mistake
                System.out.println("Error. Please make sure coordinates are properly formatted, valid, and are matching length of the ship.");
                continue;//Continue to the top of the loop instead of getting an error by running code below
            }
            //If format is correct, run code below
            String[] input = placement.split(",");//Split coords up by commas
            String[] startCoords = input[0].split(":");//Set start coords to the first split
            String[] endCoords = input[1].split(":");//Set end coors to the sceond split
            startRow = RowToInt(startCoords[0]);//Convert the start row to and int
            startCol = Integer.parseInt(startCoords[1]);//Convert start col to int
            endRow = RowToInt(endCoords[0]);//Convert end row to int
            endCol = Integer.parseInt(endCoords[1]);//Convert end col to int
            if (PlaceShip(startRow, endRow, startCol, endCol, board, shipLetter) == null){//Checks if ship is overlapping another one
                isErrorInCords = true;//Set error to true
                System.out.println("Error. Ship is overlapping another ship.");//Tell user they made a mistake
            }
        }while(isErrorInCords);//Loop while there is an error.

        do {
            PrintBoard(board);//Print the board
            System.out.println("Is this where you want to place your ship? Yes: 'Y', No: 'N'");//Ask the user for placement confirmation
            confirm = scan.nextLine();//Scan for answer
            if (confirm.equals(("Y"))){//If confirmed
                askAgain = false;//Ask again to false, this will make it so the program moves onto the next ship to be place
                break;//Break from the do while loop
            }
            else if (confirm.equals("N")) {//If unconfirmed
               board = ReplaceShip(startRow, endRow, startCol, endCol, board);//Replace the previously placed ship with water symbol
               askAgain = true;//Makes it so the user will be asked for placement of same ship again
            }
            else {//If invalid answer
                System.out.println("Please enter a valid answer");//Tells user to enter a valid answer
                askAgain = false;//Set ask again to false
            }
        }while(!askAgain);//Loop while ask again is false (while user does not have proper answer)

        return board;//Return the updated board
    }

    public static boolean FormatChecker(String placement, int length){
        /*
        Function for checking the format of user placement. Makes sure its proper so the program can proceed
        Parameters:
        placement: user's ship placement
        length: length of ship
        Returns bool representing whether or not placement is proper format
         */
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        boolean isPlacementValid = false;//Bool represents placement validity, starts as false
        String startRow;//Starting row
        String colonOne;//First colon
        String startCol;//Starting col
        String comma;//Comma in between start and end coords
        String endRow;//End row
        String colonTwo;//Second colon
        String endCol;//End col
        if (placement.length() == 7){//Checks if placement is 7 characters long
            //Below placement is separated by character
            startRow = placement.substring(0,1);
            colonOne = placement.substring(1,2);
            startCol = placement.substring(2,3);
            comma = placement.substring(3,4);
            endRow = placement.substring(4,5);
            colonTwo = placement.substring(5,6);
            endCol = placement.substring(6,7);

            //The below checks that the first character is a string from A-J, that the second is a comma, that the third
            //is a digit from 1-9, that there is a comma after that, and apply the first three rules to the last 3 checks
            if ((startRow.matches("[A-J]")) && (colonOne.equals(":")) && (startCol.matches("\\d")) && (comma.equals(","))&&
                    (endRow.matches("[A-J]")) && (colonTwo.equals(":")) && (endCol.matches("\\d"))){
                //If all that is true, the below if statement just checks that the ship is placed correctly horizontally or vertically
                //and that the user has put coords with the proper length of the ship
                if (RowToInt(startRow) == RowToInt(endRow) && Integer.parseInt(endCol) - Integer.parseInt(startCol) == (length - 1) ||
                        Integer.parseInt(startCol) == Integer.parseInt(endCol) && RowToInt(endRow) - RowToInt(startRow) == (length - 1)){
                    isPlacementValid = true;//If all this is true, then placement is valid
                }
            }
        }

    return isPlacementValid;//Return placement validity
    }
    public static String[][] ReplaceShip (int startRow, int endRow, int startCol, int endCol, String[][] board){
        /*
        Function for replacing a placed ship with the water symbol
        Parameters:
        startRow: the start row
        endRow: the end row
        startCol: the start col
        endCol: the end col
        board: the board that needs to have things replaced
        Returns the updated board
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        int row = startRow;//set row to user start row
        int col = startCol;//Set col to user end col

        if(startRow == endRow){//If ship is horizontal
            for (col = startCol; col <= endCol; col++) {//Loop through cols in between start and end cols
                board[row][col] = "~";//Replace each letter with water symbol
            }
        }
        else if(startCol == endCol){//Else if ship is vertical
            for (row = startRow; row <= endRow; row++) {//Loop through rows in between start col and end col
                board[row][col] = "~";//Replace each letter with water symbol
            }
        }

        return board;//Return the updated board
    }
    public static String[][] PlaceShip(int startRow, int endRow, int startCol, int endCol, String[][] board, String shipLetter){
        /*
        Function that places the ship on the board
        Parameters:
        startRow: the start row
        endRow: the end row
        startCol: the start col
        endCol: the end col
        board: the board that needs to have things replaced
        shipLetter: the letter that represents the ship
        Returns updated board with ship placed on if its not already taken, or null if it is
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        int row = startRow;//Set row to start row
        int col = startCol;//Set col to start col
        boolean isTaken = false;//Bool for is the space already taken by another ship
        String[][] takenArray = null;//A 2D array for returning if spot is taken

        if(startRow == endRow){//If horizontal
            for (col = startCol; col <= endCol; col++) {//Loops through cols between start and end row
                if(!(board[row][col].equals("~"))){//If the character is not a water symbol
                    isTaken = true;//Set taken to true
                    break;//Break from loop
                }
            }
        }
        else if(startCol == endCol){//If vertical
            for (row = startRow; row <= endRow; row++) {//Loops through rows between start and end col
                if(!(board[row][col].equals("~"))){//If the character is not a water symbol
                    isTaken = true;//Set taken to true
                    break;//Break from loop
                }
            }
        }
    if (!isTaken){//If it's not taken
        //Below is same code as above, except just changed slightly to incorporate last row or column of ship. If implemented
        //above, the ship is not place properly.
        if(startRow == endRow){
            for (col = startCol; col <= endCol; col++) {
                board[row][col] = shipLetter;//Place the ship letter in the 2D array
            }
        }
        else if(startCol == endCol){
            for (row = startRow; row <= endRow; row++) {
                board[row][col] = shipLetter;
            }
        }
    }

        if (isTaken)return takenArray;//If the spot is taken, return a null array
        else return board;//Else return updated board
    }

    public static String StartGuessing(String[][]pOneGuessBoard, String[][]pOneBoard,String[][]pTwoGuessBoard, String[][]pTwoBoard, String pOneName, String pTwoName, String[] shipNames, int[] shipLivesOne, int[] shipLivesTwo, String[] shipLetters){
       /*
       Function that runs the guessing process. It will switch between users guessing until someone wins
       Parameters:
       pOneGuessBoard, pTwoGuessBoard: The guess boards of P1 and P2
       pOneBoard, pTwoBoard: The boards with ships on them of P1 and P2
       pOneName, pTwoName: P1 and P2 names
       shipNames: array with names of ships
       shipLivesOne, shipLivesTwo: int arrays with the amount of lives each ship has
       shipLetters: Letter representing each ship
        */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        int x = 0;//Counter var
        boolean win;//Bool for if someone won
        String[][] guessBoard = pOneGuessBoard; //Setting guessboard initially to P1's guess board
        String[][] attackBoard = pTwoBoard;//Setting attackboard initially to P2's board
        String[][] userBoard = pOneBoard;//Setting user board intially to P1's board
        int guessCountOne = 0;//Counter for P1 guesses
        int guessCountTwo = 0;//Counter for P2 guesses
        int[] shipLives = shipLivesOne;//Set shiplives to shiplives of P1 initially
        String name = "";//Winning name intially to blank
        score= 0;//Set score as 0 initially
        do {
            win = true;//Set a win to true
            if (x%2 == 0){//If x is even
                //Set all general vars to P1's vars
                guessBoard = pOneGuessBoard;
                attackBoard = pTwoBoard;
                userBoard = pOneBoard;
                name = pOneName;
                guessCountOne++;
                shipLives = shipLivesTwo;
            }
            else{//If x is odd
                //Set all general vars to P2's vars
                guessBoard = pTwoGuessBoard;
                attackBoard = pOneBoard;
                userBoard = pTwoBoard;
                name = pTwoName;
                shipLives = shipLivesOne;
                guessCountTwo++;
            }
            //Set shiplives as retunr value from Guess function
            shipLives = Guess(guessBoard, attackBoard, userBoard, name, shipNames, shipLives, shipLetters);
            x++;//Add 1 to counter

            for (int i = 0; i < shipLives.length; i++) {//Loops as long as shipLives array
                if (shipLives[i] != 0)win = false;//If any lives are not 0, win is false
            }
        }while(!win);//While there is no win

        if (name.equals(pOneName))score = guessCountOne;//If P1 won, score = # of guesses
        else if(name.equals(pTwoName)) score = guessCountTwo;//If P2 won, score = # of guesses
        return name;//Return name of winner
    }
    public static boolean GuessChecker(String guess) {
        /*
        Format checker for Guess coords
        Parameters:
        guess: the user's coords for their guess
        Returns a bool whether or not guess is valid
         */
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        boolean isGuessValid = false;//Sets guess validity to false
        String row; //Guess row
        String colon;//Guess colon
        String col;//Guess col
        if (guess.length() == 3) {//If Guess is 3 characters
            //Split guess up into each character
            row = guess.substring(0, 1);
            colon = guess.substring(1, 2);
            col = guess.substring(2, 3);

            //Below makes sure guess is as letter from A-J, then a colon, then a digit from 1-9
            if ((row.matches("[A-J]")) && (colon.equals(":")) && (col.matches("\\d"))) {
                    isGuessValid = true;//Guess is valid
            }
        }
        return isGuessValid;//Return guess validity
    }
    public static int[] Guess(String[][] guessBoard, String[][] attackBoard, String[][] userBoard, String name, String[] shipNames, int[] shipLives, String[] shipLetters){
        /*
        This is the function that take's the user's guess, makes sure its valid, and then sends it off to test for a hit or miss
        Paramaters:
        guessBoard: the user's guess board
        attackBoard: the board user is attacking
        userBoard: user's own board
        name: player name
        shipNames: Array with ship names
        shipLives: array with ship lives amounts
        shipLetters: array with letters of each ship
        Returns an int array of updated ship lives
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        /*************************
         *****SCANNER USED HERE*****
         ***************************/
        boolean isErrorInCords = false;//Set error to false
        String placement;//User input placement
        int row = 0;//guess row
        int col = 0;//Guess col
        //Prompts user for input
        System.out.println("Turn the screen so only " + name + " can see it.");
        System.out.println("Here is your current board:");
        PrintBoard(userBoard);//Prints current board
        cls();//Clear screen
        System.out.println("Here is a board showing your guesses:");
        PrintBoard(guessBoard);//Prints guess board
        do {
            isErrorInCords = false;//Set error to false
            //Prompt user for placement
            System.out.println("Enter the coordinates of your guess, separated by colons ('A:1')");
            placement = scan.nextLine();//Scan for guess placement
            if(!GuessChecker(placement)){//If placement format is invalid
                isErrorInCords = true;//Set error to true
                //Tell user they made a mistake
                System.out.println("Error. Please make sure coordinates are properly formatted");
                continue;//Skip code below and go back to top of loop
            }
            String[] coords = placement.split(":");//Split placement by colon
            //Split coors into row and col
            row = RowToInt(coords[0]);
            col = Integer.parseInt(coords[1]);
            if (!(guessBoard[row][col].equals("~"))){//If the guess is not water, this means user has guessed here before
                isErrorInCords = true;//Set error to true
                //Tell user they already guessed here
                System.out.println("You already guessed this spot!");
            }

        }while(isErrorInCords);//While error in coords is true
        //Shiplives is updated by hit or miss functions
        shipLives = HitOrMiss(guessBoard, attackBoard, row, col, shipNames, shipLives, shipLetters);
        return shipLives;//Return array with ship lives
    }

    public static int[] HitOrMiss(String[][] guessBoard, String[][] attackBoard, int row, int col, String[] shipNames, int[] shipLives, String[] shipLetters){
        /*
        Function for determining whether guess is a hit or miss
        Parameters:
        guessBoard: the user's guess board
        attackBoard: the board user is attacking
        row: guess row
        col: guess col
        shipNames: Array with ship names
        shipLives: array with ship lives amounts
        shipLetters: array with letters of each ship
        Returns int array with updates ship lives
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        boolean hit = false;//Set bool for hit or not to false
        int shipCounter = 0;//Counter varaible for looping through ship letters
        boolean sunkShip = false;//Set bool for sunkship or not to false

        if (!(attackBoard[row][col].equals("~"))){//If the attack square is not water
            for (shipCounter = 0; shipCounter < shipLives.length; shipCounter++) {//Loop 5 times
                for (int testRow = 0; testRow < attackBoard.length; testRow++) {//Loops for amount of rows
                    for (int testCol = 0; testCol < attackBoard[testRow].length; testCol++) {//Loop for row length
                        if (attackBoard[row][col].equals(shipLetters[shipCounter])){//If the hit box is a ship letter
                            if (shipLives[shipCounter] == 1) {//If that ship has one life left
                                sunkShip = true;//Set sunk ship to true
                            }
                            shipLives[shipCounter]--;//Subtract one life from ship lives
                            hit = true;//Set hit to true
                            break;//break from loop
                        }
                    }
                    if (hit)break;//If hit, break from loop
                }
                if (hit)break;//If hit, break from loop
            }

            attackBoard[row][col] = "X";//Set attack board box to an X
            guessBoard[row][col] = "X";//Set guess board box to an X
            System.out.println("Hit!");//Tell user they got a hit
            if (sunkShip){//If they sunk a ship
                //Tell them which ship they sunk
                System.out.println("You sunk your opponent's " + shipNames[shipCounter]);
            }
        }
        else{//If miss
            attackBoard[row][col] = "O";//Set attack board box to an O
            guessBoard[row][col] = "O";//Set guess board box to an O
            System.out.println("Miss!");//Tell user they missed
        }
        System.out.println("Here is your current guess board:");
        PrintBoard(guessBoard);//Print guess board
        cls();
        return shipLives;//Return updated ship lives
    }

    public static void PrintMenu(){
        /*
        Function for printing main menu
         */
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        try{
            FileReader frLogo = new FileReader("logo.txt");//New file reader
            BufferedReader brLogo = new BufferedReader(frLogo);//new buffered reader
            for (int i = 0; i < 6; i++) {//For how many lines of logo
                System.out.println(brLogo.readLine());//print the line
            }
            brLogo.close();//Close the buffered reader
        }catch(Exception e){
            System.out.println(e.getMessage());//Print error message
        }
        //3 emtpy lines for spacing
        System.out.println();
        System.out.println();
        System.out.println();
        //Options and prompt
        System.out.println("-----------------------");
        System.out.println("1.       Play the game");
        System.out.println("2.       Leaderboard");
        System.out.println("3.       Help");
        System.out.println("4.       Exit");
        System.out.println("-----------------------");
        System.out.println("Please enter an option:");

    }

    public static void ResetGame(){
        /*
        This function resets all variables to their original state if user wants to play another game
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        pOneBoard = new String[][]{
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" }
        };
        pTwoBoard = new String[][]{
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" }
        };
        pOneGuessBoard = new String[][]{
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" }
        };
        pTwoGuessBoard = new String[][]{
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" },
                { "~", "~", "~", "~", "~", "~", "~", "~", "~", "~" }
        };
        pOneName = "";
        pTwoName = "";
        score = 0;
        shipSetupOneDone = false;
        shipSetupTwoDone = false;
        setupCount = 0;
        winner = "";
    }

    public static void EnterScore(int score){
        /*
        This function is used for entering the score of the user into the leaderboard
        Parameters:
        score: user's score
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        /*************************
         *****FILE IO*****
         ***************************/
        try{
            String line = "";//Set line as blank
            Integer[] scoresArray;//New integer array of scores
            String[] splitLine;//String array to hold split line elements
            int lineCount = 0;//Counter for lines

            BufferedWriter bwScores = new BufferedWriter(new FileWriter("leaderboard.txt", true));//New buffered writer

            bwScores.write("0:" + score + "\r\n");//Add the newest score to file
            bwScores.close();//Close buffered writer
            FileReader frScores= new FileReader("leaderboard.txt");//New filereader
            BufferedReader brScores = new BufferedReader(frScores);//New buffered reader
            while ((line = brScores.readLine()) != null){//Runs as long as the line is not null
                lineCount++;//Count the # of lines
            }
            frScores= new FileReader("leaderboard.txt");//reset filereader
            brScores = new BufferedReader(frScores);//reset buffered reader
            scoresArray = new Integer[lineCount];//set scores array to size of # of lines
            for (int i = 0; i < scoresArray.length; i++) {//Loops as long as scores array
                line = brScores.readLine();//Line is equal to the line being read
                splitLine = line.split(":");//Split the line by three space
                scoresArray[i] = Integer.parseInt(splitLine[1]);//Add score to array
            }
            bwScores = new BufferedWriter(new FileWriter("leaderboard.txt", false));//Reset buffered writer down here or else it deltes file contents

            Arrays.sort(scoresArray, Collections.reverseOrder());//Sort in descending order
            for (int j = 0; j < scoresArray.length; j++) {//Loops as long as scores array
                bwScores.write((j + 1) + "." + "   " + scoresArray[j] + "\r\n");//print score with ranking
            }
            brScores.close();//Close buffered reader
            bwScores.close();//Close buffered writer
        }catch(Exception e){
            System.out.println(e.getMessage());//Print exception message
        }

    }
    public static void main(String[] args) {
        /*
        Main function
         */
        /*************************
         *****ARRAYS USED HERE*****
         ***************************/
        /*************************
         *****SELECTION USED HERE*****
         ***************************/
        /*************************
         *****REPETITION USED HERE*****
         ***************************/
        /*************************
         *****FILE IO USED HERE*****
         ***************************/
        /*************************
         *****SCANNER USED HERE*****
         ***************************/
        //Final strings for game states
        final String GAMEPLAY = "1";
        final String LEADERBOARD = "2";
        final String HELP = "3";
        final String EXIT = "4";
        final String MENU = "5";

        String menuChoice = "";//Input for menu
        String line = "";//Line for printing scores
        String[] shipNamesArray;//Array that holds ship names
        int[] shipLengthsArray;//Array that holds ship lengths
        String[] shipLettersArray;//Array that holds ship letters
        int[] pOneShipLivesArray;//Array that holds P1 ship lives
        int[] pTwoShipLivesArray;//Array that holds P2 ship lives

        shipNamesArray = new String[]{"Carrier", "Battleship", "Patroller", "Submarine", "Destroyer"};//All ship names
        shipLengthsArray = new int[]{5, 4, 3, 3, 2};//All ship lengths
        pOneShipLivesArray = new int[]{5, 4, 3, 3, 2};//All ship lives
        pTwoShipLivesArray = new int[]{5, 4, 3, 3, 2};//All ship lives
        shipLettersArray = new String[]{"C", "B", "P", "S", "D"};//All ship letters
        boolean menuChoiceValid;//Bool for if menu choice is valid or not
        String gameState = MENU;//Initial game state

        do {
            switch (gameState){//Switch for each gamestate

                case MENU://Menu states
                    menuChoiceValid = false;//Set choice to invalid
                    do {
                        PrintMenu();//Print main menu
                        menuChoice = scan.nextLine();//Scan for choice

                        //Below if else statement contains 4 if elses. Each one just sets that game state
                        //to the corresponding input, sets choice validity to valid, and clears the screen
                        if (menuChoice.equals(GAMEPLAY)){
                            gameState = GAMEPLAY;
                            menuChoiceValid = true;
                            cls();
                        }
                        else if (menuChoice.equals(LEADERBOARD)){
                            gameState = LEADERBOARD;
                            menuChoiceValid = true;
                            cls();
                        }
                        else if (menuChoice.equals(HELP)){
                            gameState = HELP;
                            menuChoiceValid = true;
                            cls();
                        }
                        else if (menuChoice.equals(EXIT)){
                            gameState = EXIT;
                            menuChoiceValid = true;
                            cls();
                        }
                        else {//If user has invalid choice
                            //Let them know it
                            System.out.println("Invalid choice.");
                            cls();//Clear screen
                        }
                    }while(!menuChoiceValid);//Loop while their choice is invalid
                    break;
                case GAMEPLAY://Actual game play game state
                    ResetGame();//Reset the game variable
                    //Get players' names
                    System.out.println("Player One, please enter your name: ");
                    pOneName = scan.nextLine();
                    System.out.println("Player Two, please enter your name: ");
                    pTwoName = scan.nextLine();
                    cls();

                    System.out.println("Please turn the monitor so only " + pOneName + " can see it.");
                    System.out.println(pOneName + ", this is your current board.");
                    PrintBoard(pOneBoard);//print P1 board

                    do {
                        //Set P1 ships up
                        pOneBoard = SetShip(pOneBoard, shipNamesArray[setupCount], shipLengthsArray[setupCount], shipLettersArray[setupCount]);
                        if (!askAgain)setupCount++;//If ask again is false, add 1 to counter var
                        cls();
                        if (!askAgain && setupCount == 5) shipSetupOneDone = true;//If counter is 5 and ask again if false, set up is done
                    }while(!shipSetupOneDone);//Loop while set up is not done
                    cls();

                    setupCount = 0;//Set setup count to 0
                    //Below code is same as above, just for P2
                    System.out.println("Please turn the monitor so only " + pTwoName + " can see it.");
                    System.out.println(pTwoName + ", this is your current board.");
                    PrintBoard(pTwoBoard);

                    do {
                        pTwoBoard = SetShip(pTwoBoard, shipNamesArray[setupCount], shipLengthsArray[setupCount], shipLettersArray[setupCount]);
                        if (!askAgain)setupCount++;
                        cls();
                        if (!askAgain && setupCount == 5) shipSetupTwoDone = true;
                    }while(!shipSetupTwoDone);
                    cls();

                    //Tells users to start guessing
                    System.out.println("Turn the monitor so both players can see it.");
                    System.out.println(pOneName + " and " + pTwoName + " have chosen their ship positions, let's start guessing!");

                    //Set winner as return value of start guessing function
                    winner = StartGuessing(pOneGuessBoard, pOneBoard, pTwoGuessBoard, pTwoBoard, pOneName, pTwoName, shipNamesArray, pOneShipLivesArray, pTwoShipLivesArray, shipLettersArray);
                    //Congratulates winner
                    System.out.println("Congratulations " + winner + ", you won! Your score is " + score);
                    score = 19;
                    EnterScore(score);//Passes score to enter score function
                    cls();
                    gameState = MENU;//Set game state to menu
                    break;

                case LEADERBOARD://Leaderboard game state

                    try{
                        FileReader frScores = new FileReader("leaderboard.txt");//new FR
                        BufferedReader brScores = new BufferedReader(frScores);//new BR

                        System.out.println("Here are the highest scores achieved so far!");
                        while((line = brScores.readLine()) != null){//Loop while line being read is not null
                            System.out.println(line);//print line
                        }
                        brScores.close();//Close BR
                    }catch(Exception e){
                        System.out.println(e.getMessage());//print error message
                    }
                    cls();
                    gameState = MENU;//Set game state to menu
                    break;

                case HELP://Help game state
                    try{
                        java.awt.Desktop.getDesktop().browse(new java.net.URI("http://sdsscomputers.com/UsmanFaraan/assignments/completed/battleship/screens.html"));//Redirect to help page on website
                    }catch (Exception e){
                        System.out.println(e.getMessage());//Print error message
                    }
                    gameState = MENU;//Set gamestate to menu
                    cls();
                    break;

                case EXIT://Exit game state
                    gameState = EXIT;//Set game state to exit
                    break;
            }
        }while(!gameState.equals(EXIT));//While the gamestate is not exit
        System.out.println("Thanks for playing, have a great day!");//Thanks user for playing
        cls();//Waits for user to press enter before closing
    }
}
