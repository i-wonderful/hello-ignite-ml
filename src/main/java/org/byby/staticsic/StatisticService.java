package org.byby.staticsic;

import org.byby.predict.impl.SVMParams;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Write statistic to file.
 */
public class StatisticService {

    private static final String FILE_NAME = "./statistic/statistic.csv";

    private static final String HEADER = "Date,Iterations,LocIterations,Lambda,TrainSize,TimeFit(sec),Accuracy";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SVMParams params;
    private double accuracy;
    private int trainSize;
    private double timeFitSec;

    public void write() {
        boolean isAppend = new File(FILE_NAME).isFile();
        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(FILE_NAME, isAppend);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            if (!isAppend) {
                printWriter.println(HEADER);
            }

            printWriter.printf("%s, %d, %d, %.2f, %d, %.2f, %.4f \n",
                    DATE_TIME_FORMATTER.format(LocalDateTime.now()),
                    params.getAmountIterations(),
                    params.getAmountLocIterations(),
                    params.getLambda(),
                    trainSize,
                    timeFitSec,
                    accuracy);

            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StatisticService withParams(SVMParams params) {
        this.params = params;
        return this;
    }

    public StatisticService withAccuracy(double accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public StatisticService withTrainSize(int trainSize) {
        this.trainSize = trainSize;
        return this;
    }

    public StatisticService withTimeFitSec(double timeFitSec) {
        this.timeFitSec = timeFitSec;
        return this;
    }
}
