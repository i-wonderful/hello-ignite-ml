package org.byby.predict.impl;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.ml.dataset.feature.extractor.impl.DummyVectorizer;
import org.apache.ignite.ml.math.primitives.vector.Vector;
import org.apache.ignite.ml.multiclass.MultiClassModel;
import org.apache.ignite.ml.multiclass.OneVsRestTrainer;
import org.apache.ignite.ml.preprocessing.Preprocessor;
import org.apache.ignite.ml.preprocessing.minmaxscaling.MinMaxScalerTrainer;
import org.apache.ignite.ml.svm.SVMLinearClassificationModel;
import org.apache.ignite.ml.svm.SVMLinearClassificationTrainer;
import org.byby.cache.CacheManager;
import org.byby.predict.PredictService;
import org.byby.staticsic.StatisticService;


public class SVMPredictService extends PredictService<SVMParams> {

    @Override
    public void fitAndPredict(CacheManager cacheManager, SVMParams params) {
        Ignite ignite = cacheManager.getIgnite();
        IgniteCache<Integer, Vector> cache = cacheManager.getTrainCache();

        System.out.println(">>> Start fit model...");
        long startMs = System.currentTimeMillis();

        var preprocessor = createPreprocessor(ignite, cache);
        var oneVsRestTrainer = new OneVsRestTrainer<>(createSVMTrainer(params));
        MultiClassModel<SVMLinearClassificationModel> model = oneVsRestTrainer.fit(ignite,
                cache,
                preprocessor);

        double timeFitSec = (System.currentTimeMillis() - startMs) / 1000.0;
        System.out.printf(">>> Complete fit model, time = %.4f sec, params = %s \n", timeFitSec, params);

        double accuracy = predict(model, cacheManager);

        statisticWrite(params, accuracy, cache.size(), timeFitSec);
    }

    private SVMLinearClassificationTrainer createSVMTrainer(SVMParams params) {
        return new SVMLinearClassificationTrainer()
                .withAmountOfIterations(params.getAmountIterations())
                .withAmountOfLocIterations(params.getAmountLocIterations())
                .withLambda(params.getLambda())
                .withSeed(params.getSeed());
    }

    private Preprocessor<Integer, Vector> createPreprocessor(Ignite ignite, IgniteCache<Integer, Vector> cache) {
        MinMaxScalerTrainer<Integer, Vector> minMaxScalerTrainer = new MinMaxScalerTrainer<>();
        Preprocessor<Integer, Vector> preprocessor = minMaxScalerTrainer.fit(
                ignite,
                cache,
                new DummyVectorizer<Integer>().labeled(0)
        );
        return preprocessor;
    }

    private void statisticWrite(SVMParams params, double accuracy, int trainSize, double timeFitSec) {
        new StatisticService()
                .withParams(params)
                .withAccuracy(accuracy)
                .withTrainSize(trainSize)
                .withTimeFitSec(timeFitSec)
                .write();
    }

}
