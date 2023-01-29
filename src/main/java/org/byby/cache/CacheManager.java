package org.byby.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.ml.math.primitives.vector.Vector;

public interface CacheManager {

    void fillTrain();

    void fillTest();

    IgniteCache<Integer, Vector> getTrainCache();

    IgniteCache<Integer, Vector> getTestCache();

    Ignite getIgnite();

    void destroyAll();
}
