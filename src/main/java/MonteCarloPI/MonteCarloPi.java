package MonteCarloPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MonteCarloPi {

    static final int Radius = 3000;
    static final long NUM_POINTS = 50_000_000L;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        // Without Threads
        System.out.println("Single threaded calculation started: ");
        long startTime = System.nanoTime();
        double piWithoutThreads = estimatePiWithoutThreads(NUM_POINTS);
        long endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (single thread): " + piWithoutThreads);
        System.out.println("Time taken (single threads): " + (endTime - startTime) / 1_000_000 + " ms");

        // With Threads
        System.out.printf("Multi threaded calculation started: (your device has %d logical threads)\n",NUM_THREADS);
        startTime = System.nanoTime();
        double piWithThreads = estimatePiWithThreads(NUM_POINTS, NUM_THREADS);
        endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (Multi-threaded): " + piWithThreads);
        System.out.println("Time taken (Multi-threaded): " + (endTime - startTime) / 1_000_000 + " ms");

        // TODO: After completing the implementation, reflect on the questions in the description of this task in the README file
        //       and include your answers in your report file.
    }

    // Monte Carlo Pi Approximation without threads
    public static double estimatePiWithoutThreads(long numPoints)
    {
        // TODO: Implement this method to calculate Pi using a single thread
        long insideCircle = 0;

        for (long i = 0; i < numPoints; i++) {
            int x = ThreadLocalRandom.current().nextInt(-Radius, Radius + 1);
            int y = ThreadLocalRandom.current().nextInt(-Radius, Radius + 1);

            if (x * x + y * y <= Radius * Radius) {
                insideCircle++;
            }
        }

        return 4.0 * insideCircle / numPoints;
    }

    // Monte Carlo Pi Approximation with threads
    public static double estimatePiWithThreads(long numPoints, int numThreads) throws InterruptedException, ExecutionException
    {
        // TODO: Implement this method to calculate Pi using multiple threads

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Long>> futures = new ArrayList<>();

        long pointsPerThread = numPoints / numThreads;
        long remaining = numPoints % numThreads;

        for (int i = 0; i < numThreads; i++) {
            long points = pointsPerThread + (i < remaining ? 1 : 0); // Distribute remaining points
            futures.add(executor.submit(() -> {
                long localInside = 0;
                for (long j = 0; j < points; j++) {
                    int x = ThreadLocalRandom.current().nextInt(-Radius, Radius + 1);
                    int y = ThreadLocalRandom.current().nextInt(-Radius, Radius + 1);
                    if (x * x + y * y <= Radius * Radius) {
                        localInside++;
                    }
                }
                return localInside;
            }));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS); // Enough time for large simulations

        long totalInside = 0;
        for (Future<Long> future : futures) {
            totalInside += future.get();
        }

        return 4.0 * totalInside / numPoints;

        // HINT: You may need to create a variable to *safely* keep track of points that fall inside the circle
        // HINT: Each thread should generate and process a subset of the total points

        // TODO: After submitting all tasks, shut down the executor to prevent new tasks
        // TODO: wait for the executor to be fully terminated
        // TODO: Calculate and return the final estimation of Pi
    }
}