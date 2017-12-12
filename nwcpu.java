public class nwcpu {
    private static int[][] brd = new int[15][15];

    public nwcpu() {
        for (int x[] : brd) {
            for (int y : x) {
                y = 0;
            }
        }
    }

    public void getDecision(final int lastStep) {
        brd[(lastStep % 100) - 1][(lastStep / 100) - 1] = 1;
        int[][] decisionStatistics = new int[15][15];
        for (int x = 0; x < brd.length; x++)
            for (int y = 0; y < brd[x].length; y++)
                if (brd[x][y] != 0) decisionStatistics[x][y] = -1;
                else decisionStatistics[x][y] = 0;
    }
}
