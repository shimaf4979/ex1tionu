package ex12c;

public class SearchMetrics {
    private long visitedNodes = 0;
    private long maxOpenListSize = 0;
    private long maxClosedListSize = 0;
    private long startTime;
    private long endTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTime = System.currentTimeMillis();
    }

    public void incrementVisitedNodes() {
        visitedNodes++;
    }

    public void updateMaxOpenListSize(int currentSize) {
        maxOpenListSize = Math.max(maxOpenListSize, currentSize);
    }

    public void updateMaxClosedListSize(int currentSize) {
        maxClosedListSize = Math.max(maxClosedListSize, currentSize);
    }

    public void reset() {
        visitedNodes = 0;
        maxOpenListSize = 0;
        maxClosedListSize = 0;
        startTime = 0;
        endTime = 0;
    }

    public void printMetrics(String solverName) {
        long executionTime = endTime - startTime;
        System.out.println("\n" + solverName + "の性能評価:");
        System.out.println("訪問ノード数: " + visitedNodes);
        System.out.println("オープンリストの最大長: " + maxOpenListSize);
        System.out.println("クローズドリストの最大長: " + maxClosedListSize);
        System.out.println("実行時間: " + executionTime + "ms");

        if (executionTime > 180000) { // 3分 = 180000ms
            System.out.println("※実行時間が3分を超えました（測定不能）");
        }
    }
}