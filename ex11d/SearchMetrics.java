package ex11d;

public class SearchMetrics {
    private long visitedNodes = 0;
    private long maxOpenListSize = 0;
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

    public long getVisitedNodes() {
        return visitedNodes;
    }

    public long getMaxOpenListSize() {
        return maxOpenListSize;
    }

    public long getExecutionTime() {
        return endTime - startTime;
    }

    public void printMetrics(String solverName) {
        System.out.println("\n" + solverName + "の性能評価:");
        System.out.println("訪問ノード数: " + visitedNodes);
        System.out.println("オープンリストの最大長: " + maxOpenListSize);
        System.out.println("実行時間: " + getExecutionTime() + "ms");
    }
}