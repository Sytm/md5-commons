package de.md5lukas.commons.tuples;

/**
 * Simple tuple to pass two values within one object
 *
 * @param <L> The type of the left value
 * @param <R> The type of the right value
 */
public final class Tuple2<L, R> {

    private final L l;
    private final R r;

    private Tuple2(L l, R r) {
        this.l = l;
        this.r = r;
    }

    /**
     * @return The left value
     */
    public L getL() {
        return l;
    }

    /**
     * @return The right value
     */
    public R getR() {
        return r;
    }

    /**
     * Creates a new tuple object with the two given values
     *
     * @param l   The left value
     * @param r   The right value
     * @param <L> The left type
     * @param <R> The right type
     * @return The created tuple object
     */
    public static <L, R> Tuple2<L, R> of(L l, R r) {
        return new Tuple2<>(l, r);
    }
}
