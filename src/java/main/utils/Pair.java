package utils;

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
}