package inhibition;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class ResWriter {

    private PrintWriter printWriter;
    private static final String FILENAME = "results.txt";

    /**
     * Print the results in the result file
     */
    public void printResults(List<Double> teddyBearTime, List<Double> squaresTime) {

        try {
            FileWriter temp = new FileWriter(FILENAME, true);
            printWriter = new PrintWriter(temp);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }

        printWriter.println("\n\n");

        printWriter.println("Stars:");
        stats(teddyBearTime);

        printWriter.println("\n\nSquares:");
        stats(squaresTime);

        printWriter.println("\n\n");
        printWriter.println("\n#####");
        printWriter.close();
    }

    /**
     * Prints a list of statistics for a given list of double
     */
    private void stats(List<Double> list) {

        if (list.isEmpty()) {
            printWriter.println("list empty");
            return;
        }

        Collections.sort(list);
        double min = list.get(0);
        double max = list.get(list.size() - 1);
        double med = list.get(list.size() / 2);
        double avg = 0.0;
        for (double e : list) {
            avg += e;
        }
        avg /= list.size();

        double std_dev = 0.0;
        for (double e : list) {
            std_dev += Math.pow(e - avg, 2);
        }
        std_dev /= list.size();
        std_dev = Math.sqrt(std_dev);

        min = (double) Math.round(min * 100) / 100;
        max = (double) Math.round(max * 100) / 100;
        med = (double) Math.round(med * 100) / 100;
        avg = (double) Math.round(avg * 100) / 100;
        std_dev = (double) Math.round(std_dev * 100) / 100;

        printWriter.println("number of elements: " + list.size());
        printWriter.println("min = " + min);
        printWriter.println("max = " + max);
        printWriter.println("med = " + med);
        printWriter.println("avg = " + avg);
        printWriter.println("std dev = " + std_dev);
    }
}
