package org.byby.cache.dataset;

public enum MLDatasets {
    MNIST_TRAIN("dataset/mnist_train.csv", true, ","),
    MNIST_TRAIN_5("dataset/mnist_train_5.csv", true, ","),
    MNIST_TRAIN_10("dataset/mnist_train_10.csv", true, ","),
    MNIST_TRAIN_15("dataset/mnist_train_15.csv", true, ","),
    MNIST_TEST("dataset/mnist_test.csv", true, ",");

    /** Filename. */
    private final String filename;

    /** The csv file has header. */
    private final boolean hasHeader;

    /** The separator between words. */
    private final String separator;

    /**
     * @param filename Filename.
     * @param hasHeader The csv file has header.
     * @param separator The special sign to separate the line on words.
     */
    MLDatasets(final String filename, boolean hasHeader, String separator) {
        this.filename = filename;
        this.hasHeader = hasHeader;
        this.separator = separator;
    }

    /** */
    public String getFileName() { return filename; }

    /** */
    public boolean hasHeader() {
        return hasHeader;
    }

    /** */
    public String getSeparator() {
        return separator;
    }
}
