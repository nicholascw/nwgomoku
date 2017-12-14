public class nwcpu {
    private int[][] brd = new int[15][15];
    private int[][] decisionStatistics = new int[15][15];
    private int[][] sectionData;
    private int lastX, lastY;

    public nwcpu() {
        for (int x[] : brd) {
            for (int y : x) {
                y = 0;
            }
        }
        sectionData = new int[9][9];
    }

    public int getDecision(final int lastStep) {
        int x = (lastStep % 100) - 1, y = (lastStep / 100) - 1;
        lastX = x;
        lastY = y;

        brd[x][y] = 1;
        int xMin = (x >= 4 ? x - 4 : 0),
                xMax = (x <= 10 ? x + 4 : 14),
                yMin = (y >= 4 ? y - 4 : 0),
                yMax = (y <= 10 ? y + 4 : 14);

        for (int ix = 0; ix < brd.length; ix++)
            for (int iy = 0; iy < brd[ix].length; iy++)
                if (brd[ix][iy] != 0) decisionStatistics[ix][iy] = -99;
                else decisionStatistics[ix][iy] = 0;

        sectionData = new int[9][9];

        for (int i = xMin; i <= xMax; i++)
            for (int j = yMin; j <= yMax; j++) sectionData[i - xMin][j - yMin] = brd[i][j];

        for (int[] sdx : sectionData) for (int sdy : sdx) if (sdy != 0 && sdy != 1 && sdy != 2) sdy = -1;

        defendScan();
        attackScan();
        return getDecisionTablePeak();
    }

    private void defendScan() {
        //look N/S && E/W direction
        for (int i = 0; i < 9; i++) {
            int nsPassed = -1, ewPassed = -1;
            boolean nsSkip = false, ewSkip = false;
            int nsLast = -1, ewLast = -1;
            int nsCount = 0, ewCount = 0;
            for (int j = 0; j < 9; j++) {
                switch (sectionData[i][j]) {
                    case 0:
                        if (nsLast == -1 || nsLast == 0) {
                            nsPassed = j;
                        } else if (nsLast == 1) {
                            if (!nsSkip) {
                                switch (nsCount) {
                                    case 1:
                                        putAbsTable(i, j, 3);
                                        if (nsPassed >= 0) putAbsTable(i, nsPassed, 3);
                                        nsPassed = -1;
                                        break;
                                    case 2:
                                        putAbsTable(i, j, 7);
                                        if (nsPassed >= 0) putAbsTable(i, nsPassed, 7);
                                        nsPassed = -1;
                                        break;
                                    case 3:
                                        putAbsTable(i, j, 20);
                                        if (nsPassed >= 0) putAbsTable(i, nsPassed, 20);
                                        nsPassed = -1;
                                        break;
                                }
                            } else if (nsCount > 3) {
                                putAbsTable(i, j, 40);
                                if (nsPassed >= 0) putAbsTable(i, nsPassed, 40);
                                nsPassed = -1;
                            }
                        } else if (nsLast == 2) {
                            nsSkip = false;
                            nsPassed = j;
                        }
                        nsLast = 0;
                        nsCount = 0;
                        break;
                    case 1:
                        nsCount++;
                        nsLast = 1;
                        break;
                    case 2:
                        nsSkip = true;
                        nsPassed = -1;
                        nsLast = 2;
                        nsCount = 0;
                        break;
                }
                switch (sectionData[j][i]) {
                    case 0:
                        if (ewLast == -1 || ewLast == 0) {
                            ewPassed = i;
                        } else if (ewLast == 1) {
                            if (!ewSkip) {
                                switch (ewCount) {
                                    case 1:
                                        putAbsTable(j, i, 3);
                                        if (ewPassed >= 0) putAbsTable(ewPassed, j, 3);
                                        ewPassed = -1;
                                        break;
                                    case 2:
                                        putAbsTable(j, i, 7);
                                        if (ewPassed >= 0) putAbsTable(ewPassed, j, 7);
                                        ewPassed = -1;
                                        break;
                                    case 3:
                                        putAbsTable(j, i, 20);
                                        if (ewPassed >= 0) putAbsTable(ewPassed, j, 20);
                                        ewPassed = -1;
                                        break;
                                }
                            } else if (ewCount > 3) {
                                putAbsTable(j, i, 40);
                                if (ewPassed >= 0) putAbsTable(ewPassed, j, 40);
                                ewPassed = -1;
                            }
                        } else if (ewLast == 2) {
                            ewSkip = false;
                            ewPassed = i;
                        }
                        ewLast = 0;
                        ewCount = 0;
                        break;
                    case 1:
                        ewCount++;
                        ewLast = 1;
                        break;
                    case 2:
                        ewSkip = true;
                        ewPassed = -1;
                        ewLast = 2;
                        ewCount = 0;
                        break;
                }
            }
        }
        //Finished NW && EW detect


    }

    private void attackScan() {

    }

    private void putAbsTable(final int relX, final int relY, final int statVal) {
        decisionStatistics[lastX - 4 + relX][lastY - 4 + relY] += statVal;
    }

    private int getDecisionTablePeak() {
        int tmpHigh = -99;
        int rVal = -1;
        for (int p = 0; p < decisionStatistics.length; p++) {
            for (int q = 0; q < decisionStatistics[p].length; q++) {
                if (decisionStatistics[p][q] > tmpHigh) {
                    tmpHigh = decisionStatistics[p][q];
                    rVal = q + 1 + (p + 1) * 100;
                }
            }
        }
        return rVal;
    }
}
