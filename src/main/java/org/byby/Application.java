package org.byby;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.byby.init.Const;
import org.byby.init.ConstBest;
import org.byby.init.IgniteInit;
import org.byby.cache.CacheManager;
import org.byby.cache.impl.CacheServiceImpl;
import org.byby.predict.impl.SVMParams;
import org.byby.predict.impl.SVMPredictService;

public class Application {
    public static void main(String[] args) throws IgniteException {

        try (Ignite ignite = IgniteInit.start()) {
            System.out.println(">>> Ignite started.");

            CacheManager cacheManager = new CacheServiceImpl(ignite);
            cacheManager.fillTrain();
            cacheManager.fillTest();

            runBest(cacheManager);
            //runGrid(cacheManager);

            cacheManager.destroyAll();
        } finally {
            System.out.println(">>> Ignite closed.");
            System.out.flush();
        }
    }

    /**
     * Train series model.
     */
    private static void runGrid(CacheManager cacheManager) {

        SVMParams params = new SVMParams()
                .withSeed(Const.SEED);

        int iterMin = Const.AMOUNT_ITERATIONS;
        int iterMax = iterMin + 30;
        double lambdaMin = Const.LAMBDA;
        double lambdaMax = lambdaMin + 0.2;
        int iterLocMin = Const.AMOUNT_LOC_ITERATIONS;
        int iterLocMax = iterLocMin + 20;

        for (int iterations = iterMin; iterations <= iterMax; iterations += 15) {
            for (double lambda = lambdaMin; lambda <= lambdaMax; lambda += 0.05) {
                for (int iterLoc = iterLocMin; iterLoc <= iterLocMax; iterLoc += 10) {
                    params.withAmountIterations(iterations);
                    params.withLambda(lambda);
                    params.withAmountLocIterations(iterLoc);
                    new SVMPredictService().fitAndPredict(cacheManager, params);
                }
            }
        }
    }

    /**
     * Train one model.
     */
    private static void runBest(CacheManager cacheManager) {
        SVMParams params = new SVMParams()
                .withAmountLocIterations(ConstBest.AMOUNT_LOC_ITERATIONS)
                .withAmountIterations(ConstBest.AMOUNT_ITERATIONS)
                .withLambda(ConstBest.LAMBDA)
                .withSeed(ConstBest.SEED);

        new SVMPredictService().fitAndPredict(cacheManager, params);
    }
}
