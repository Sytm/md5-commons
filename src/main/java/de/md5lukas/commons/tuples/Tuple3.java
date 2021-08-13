package de.md5lukas.commons.tuples;

/**
 * Simple tuple to pass three values within one object
 *
 * @param <L> The type of the left value
 * @param <M> The type of the middle value
 * @param <R> The type of the right value
 */
public final class Tuple3<L, M, R> {

    private final L l;
    private final M m;
    private final R r;

    private Tuple3(L l, M m, R r) {
        this.l = l;
        this.m = m;
        this.r = r;
    }

    /**
     * @return The left value
     */
    public L getL() {
        return l;
    }

    /**
     * @return The middle value
     */
    public M getM() {
        return m;
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
     * @param m   The middle value
     * @param r   The right value
     * @param <L> The left type
     * @param <M> The middle type
     * @param <R> The right type
     * @return The created tuple object
     */
    public static <L, M, R> Tuple3<L, M, R> of(L l, M m, R r) {
        return new Tuple3<>(l, m, r);
    }
}
