package ex11e;

public class SearchMetrics {
    private long visitedNodes = 0;
    private long maxOpenListSize = 0;
    private long startTime;
    private long endTime;
    private int maxDepthReached = 0;

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

    public void updateMaxDepth(int depth) {
        maxDepthReached = Math.max(maxDepthReached, depth);
    }

    public void reset() {
        visitedNodes = 0;
        maxOpenListSize = 0;
        maxDepthReached = 0;
    }

    public void printMetrics(String solverName) {
        System.out.println("\n" + solverName + "の性能評価:");
        System.out.println("訪問ノード数: " + visitedNodes);
        System.out.println("オープンリストの最大長: " + maxOpenListSize);
        System.out.println("到達最大深さ: " + maxDepthReached);
        System.out.println("実行時間: " + (endTime - startTime) + "ms");
    }
}