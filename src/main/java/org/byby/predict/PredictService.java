package org.byby.predict;

import org.apache.commons.math3.util.Precision;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.ml.math.primitives.vector.Vector;
import org.apache.ignite.ml.multiclass.MultiClassModel;
import org.byby.cache.CacheManager;

import javax.cache.Cache;

public abstract class PredictService<T extends PredictService.Params> {

    public abstract void fitAndPredict(CacheManager cacheService, T params);

    protected double predict(MultiClassModel mdl, CacheManager cacheManager) {
        System.out.println(">>> Start predict...");
        IgniteCache<Integer, Vector> dataCacheTest = cacheManager.getTestCache();

        int amountOfErrors = 0;
        int totalAmount = 0;
        QueryCursor<Cache.Entry<Integer, Vector>> cc = dataCacheTest.query(new ScanQuery<>());
        for (Cache.Entry<Integer, Vector> item : cc) {

            Vector value = item.getValue();
            Vector inputs = value.copyOfRange(1, value.size());
            double groundTruth = value.get(0);
            Double prediction = mdl.predict(inputs);
            // System.out.printf(">>> \t %d \t %.4f \t| %.4f \t\n", key, groundTruth, prediction);

            totalAmount++;
            if (!Precision.equals(groundTruth, prediction, Precision.EPSILON))
                amountOfErrors++;
        }

        double accuracy = (1 - amountOfErrors / (double) totalAmount);
        System.out.println("Amount of errors: " + amountOfErrors);
        System.out.println("Accuracy: " + accuracy);

        return accuracy;
    }

    public static abstract class Params {}
}
