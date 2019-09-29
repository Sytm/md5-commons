package de.md5lukas.commons.tuples;

public class Tuple3<L, M, R> {

	private final L l;
	private final M m;
	private final R r;

	public Tuple3(L l, M m, R r) {
		this.l = l;
		this.m = m;
		this.r = r;
	}

	public L getL() {
		return l;
	}

	public M getM() {
		return m;
	}

	public R getR() {
		return r;
	}

	public static <L, M, R> Tuple3<L, M, R> of(L l, M m, R r) {
		return new Tuple3<>(l, m, r);
	}
}
