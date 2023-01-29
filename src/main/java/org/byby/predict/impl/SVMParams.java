package org.byby.predict.impl;

import org.byby.predict.PredictService;

public class SVMParams extends PredictService.Params {

    private int amountIterations;
    private int amountLocIterations;
    private double lambda;
    private long seed;

    public SVMParams withAmountIterations(int amountIterations) {
        this.amountIterations = amountIterations;
        return this;
    }

    public SVMParams withAmountLocIterations(int amountLocIterations) {
        this.amountLocIterations = amountLocIterations;
        return this;
    }

    public SVMParams withLambda(double lambda) {
        this.lambda = lambda;
        return this;
    }

    public SVMParams withSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public int getAmountIterations() {
        return amountIterations;
    }

    public int getAmountLocIterations() {
        return amountLocIterations;
    }

    public double getLambda() {
        return lambda;
    }


    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "SVMParams{" +
                "amountIterations=" + amountIterations +
                ", amountLocIterations=" + amountLocIterations +
                ", lambda=" + lambda +
                '}';
    }
}
