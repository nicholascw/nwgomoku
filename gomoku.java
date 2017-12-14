public class gomoku {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_BELL = "\007";
    private static int[] stoneHistory = new int[225];
    private static short currentStep = 0;
    private static int chessBoard[][] = new int[15][15];
    private static nwcpu cpuplayer;
    //chessBoard definition: 0 unset, 1 player1, 2 player2/cpuplayer

    public static void main(String[] args) {
        boolean sPlayer = false;
        if (args.length > 0) {
            if (args[0].equals("single"))
                sPlayer = true;
            else if (args[0].equals("version")) {
                System.out.println("NW Gomoku v1.0.0");
                return;
            } else {
                System.out.println(ANSI_CYAN + "A Gomoku Game by Nicholas@UIUC.\n\n" +
                        "USAGE: gomoku [OPTIONS] ...\n" +
                        "To activate single player mode, use argument 'single'.\n" +
                        "For bugs report please touch " + ANSI_YELLOW + ANSI_BOLD +
                        "chenkai3@illinois.edu" + ANSI_RESET + ANSI_CYAN +
                        ".\n======================================================\n\n" + ANSI_RESET);
                return;
            }
        }
        while (true) {
            for (int iBoard[] : chessBoard)
                for (int jBoard : iBoard) jBoard = 0;
            initBoard();
            cpuplayer = new nwcpu();
            for (currentStep = 0; currentStep < 225; currentStep++) {
                if (currentStep % 2 == 0) { //Player 1
                    cursorMoveTo(1, 31);
                    System.out.print(ANSI_GREEN + "Player 1's turn" + ANSI_RESET);
                    cursorMoveTo(2, 33);
                    while (!proceedPlayerCmd(1, coordinateParser(TextIO.getln()))) notifyInvalidCmd();
                    cursorMoveTo(1, 31);
                    System.out.print("                                   ");
                    cursorMoveTo(2, 33);
                } else {
                    if (!sPlayer) { //Multi Player: Player 2
                        cursorMoveTo(1, 31);
                        System.out.print(ANSI_GREEN + "Player 2's turn" + ANSI_RESET);
                        cursorMoveTo(2, 33);
                        while (!proceedPlayerCmd(2, coordinateParser(TextIO.getln()))) notifyInvalidCmd();
                        cursorMoveTo(1, 31);
                        System.out.print("                                   ");
                        cursorMoveTo(2, 33);
                    } else {//Single Player: CPU Player
                        proceedCPUCmd();
                    }
                }
                if (winDetect() != 0) break;
            }
            int gameResult = winDetect();
            cursorMoveTo(1, 31);
            if (gameResult == 0)
                System.out.print(ANSI_YELLOW + "A draw! Nobody wins. " + ANSI_RESET);
            else
                System.out.printf(ANSI_BOLD + ANSI_GREEN + "Player %d" + ANSI_RESET + " wins! ", gameResult);
            System.out.print(ANSI_BELL + ANSI_BOLD + ANSI_CYAN + "Play another round? (Y/n)" + ANSI_RESET);
            cursorMoveTo(1, 33);
            System.out.print(":                         ");
            cursorMoveTo(2, 33);
            if (TextIO.getln().toLowerCase().trim().equals("n")) return;//or return
        }
    }

    private static void notifyInvalidCmd() {
        cursorMoveTo(1, 31);
        System.out.print(ANSI_RED + ANSI_BOLD + "Coordinate invalid, please retry." + ANSI_BELL + ANSI_RESET);
        cursorMoveTo(1, 33);
        System.out.print(":                         ");
        cursorMoveTo(2, 33);
    }

    private static boolean proceedPlayerCmd(final int Player, final int Coordinate) {
        int x, y;
        if (Coordinate == 0) {
            return false;
        }
        x = Coordinate % 100;
        y = Coordinate / 100;
        if (chessBoard[x - 1][y - 1] != 0) {
            return false;
        }
        chessBoard[x - 1][y - 1] = Player;
        putStoneOnBoard(Player, x, y);
        stoneHistory[currentStep] = Coordinate;
        return true;
    }

    private static void proceedCPUCmd() {
        proceedPlayerCmd(2, cpuplayer.getDecision(stoneHistory[currentStep - 1]));
    }

    private static int winDetect() {
        int x, y;
        x = (stoneHistory[currentStep] % 100) - 1;
        y = (stoneHistory[currentStep] / 100) - 1;
        int[][] rowData = new int[4][9];
        for (int i = 0; i < rowData.length; i++)
            for (int j = 0; j < rowData[i].length; j++)
                rowData[i][j] = 0;
        int xMin, xMax, yMin, yMax;
        xMin = (x >= 4 ? x - 4 : 0);
        xMax = (x <= 10 ? x + 4 : 14);
        yMin = (y >= 4 ? y - 4 : 0);
        yMax = (y <= 10 ? y + 4 : 14);
        for (int iX = xMin; iX <= xMax; iX++) {
            rowData[0][iX - xMin] = chessBoard[iX][y];
        }

        System.arraycopy(chessBoard[x], yMin, rowData[1], 0, yMax + 1 - yMin);

        int tmpI = 0;
        while (tmpI <= 4 && x + tmpI <= xMax && y + tmpI <= yMax) {
            rowData[2][4 + tmpI] = chessBoard[x + tmpI][y + tmpI];
            tmpI++;
        }
        tmpI = 1;
        while (tmpI <= 4 && x - tmpI >= xMin && y - tmpI >= yMin) {
            rowData[2][4 - tmpI] = chessBoard[x - tmpI][y - tmpI];
            tmpI++;
        }

        tmpI = 0;
        while (tmpI <= 4 && x - tmpI >= xMin && y + tmpI <= yMax) {
            rowData[3][4 - tmpI] = chessBoard[x - tmpI][y + tmpI];
            tmpI++;
        }
        tmpI = 1;
        while (tmpI <= 4 && x + tmpI <= xMax && y - tmpI >= yMin) {
            rowData[3][4 + tmpI] = chessBoard[x + tmpI][y - tmpI];
            tmpI++;
        }

        for (int[] aRowData : rowData) {
            int tmpCounter = 1;
            int winningPlayer;
            for (int j = 1; j < aRowData.length; j++) {
                if (aRowData[j - 1] == aRowData[j] && aRowData[j] != 0) {
                    tmpCounter++;
                    winningPlayer = aRowData[j];
                } else {
                    tmpCounter = 1;
                    winningPlayer = 0;
                }
                if (tmpCounter >= 5) {
                    return winningPlayer;
                }
            }
        }
        return 0;   //No winning detected.
    }


    private static void initBoard() {
        System.out.print("\033[H\033[2J" +
                "1   2   3   4   5   6   7   8   9   a   b   c   d   e   f\n" +
                "┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐A\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤B\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤C\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤D\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤E\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤F\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤G\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤H\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤I\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤J\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤K\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤L\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤M\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤N\n" +
                "│   │   │   │   │   │   │   │   │   │   │   │   │   │   │\n" +
                "└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘O\n\n" +
                "Enter your decision coordinate in format like \"7J\"\n:");
    }

    private static void putStoneOnBoard(final int Player, final int x, final int y) {
        int termX, termY;
        termX = 4 * (x - 1) + 1;//0,4,8,12...
        termY = y * 2;//1,3,5,7...
        String strStone = "";
        switch (Player) {
            case 1:
                strStone = "●";
                break;
            case 2:
                strStone = "○";
                break;
        }
        cursorMoveTo(termX, termY);
        System.out.print(strStone);
        cursorMoveTo(1, 33);
        System.out.print(":                  ");
        cursorMoveTo(2, 33);
    }

    private static int coordinateParser(final String Coordinate) {
        if (Coordinate.trim().length() != 2) {
            return 0;
        }
        int rVal;
        int x = (int) Coordinate.toLowerCase().trim().charAt(0);
        if (x > 48 && x < 58) {
            rVal = x - 48;
        } else if (x > 96 && x < 103) {
            rVal = x - 87;
        } else {
            return 0;
        }
        int y = (int) Coordinate.toLowerCase().trim().charAt(1);
        if (y > 96 && y < 112) {
            rVal += (y - 96) * 100;
        } else {
            return 0;
        }
        return rVal;
    }

    private static void cursorMoveTo(final int x, final int y) {
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df", escCode, y, x));
    }
}
