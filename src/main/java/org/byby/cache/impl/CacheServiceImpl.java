package org.byby.cache.impl;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.ml.math.primitives.vector.Vector;
import org.byby.cache.CacheManager;
import org.byby.cache.dataset.MLDatasets;
import org.byby.cache.dataset.SandboxMLCache;

import java.io.FileNotFoundException;

public class CacheServiceImpl implements CacheManager {

    private final Ignite ignite;

    public CacheServiceImpl(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public void fillTrain() {
        fill(getTrainSet());
    }

    @Override
    public void fillTest() {
        fill(getTestSet());
    }

    @Override
    public IgniteCache<Integer, Vector> getTrainCache() {
        return ignite.cache(getTrainSet().name());
    }

    @Override
    public IgniteCache<Integer, Vector> getTestCache() {
        return ignite.cache(getTestSet().name());
    }

    @Override
    public Ignite getIgnite() {
        return ignite;
    }

    @Override
    public void destroyAll() {
        var testCache = getTestCache();
        if (testCache != null)
            testCache.destroy();
        var trainCache = getTrainCache();
        if (trainCache != null)
            trainCache.destroy();
    }


    private void fill(MLDatasets dataset) {
        System.out.println(">>> Start fill cache ...");
        long startMs = System.currentTimeMillis();
        try {
            var cache = new SandboxMLCache(ignite).fillCacheWith(dataset);
            System.out.printf(">>> Complete fill cache, cache.size() = %d, time = %.4f sec \n", cache.size(), (System.currentTimeMillis() - startMs) / 1000.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private MLDatasets getTrainSet() {
        return MLDatasets.MNIST_TRAIN;
    }

    private MLDatasets getTestSet() {
        return MLDatasets.MNIST_TEST;
    }
}
