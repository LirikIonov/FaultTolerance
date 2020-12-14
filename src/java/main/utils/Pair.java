package utils;

import java.util.Objects;

public class Pair<F extends Comparable<F>, S extends Comparable<S>> implements Comparable<Pair<F,S>>
{
	public F first;
	public S second;

	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}

	@Override
	public int compareTo(Pair<F, S> o)
	{
		if (!this.first.equals(o.first)) {
			return first.compareTo(o.first);
		}
		return second.compareTo(o.second);
	}

	@Override
	public boolean equals(Object o) {
		Pair<?,?> p = (Pair<?, ?>) o;
		if (this == p) {
			return true;
		}
		if (!this.first.equals(p.first)) {
			return first.equals(p.first);
		}
		return second.equals(p.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public String toString() {
		return "(" + this.first + "," + this.second + ")";
	}
}