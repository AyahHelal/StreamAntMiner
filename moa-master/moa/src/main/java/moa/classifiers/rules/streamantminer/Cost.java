package moa.classifiers.rules.streamantminer;



/**
 * The <code>Cost</code> interface represents the cost of a solution.
 * 
 * @since 4.5
 * 
 * @author Fernando Esteban Barril Otero
 */
public interface Cost extends Comparable<Cost> {
    /**
     * Returns the numeric value of the cost.
     * 
     * @return the numeric value of the cost.
     */
    public abstract double raw();

    /**
     * Returns the adjusted numeric value of the cost. This is usually used for
     * pheromone update.
     * 
     * @return the adjusted numeric value of the cost.
     */
    public abstract double adjusted();

    /**
     * Class to represent a cost value that should be minimised, where the lower
     * the value the better the solution.
     */
    public static class Minimise implements Cost {
	/**
	 * The cost value.
	 */
	private double cost;

	/**
	 * Creates a new <code>Minimise</code> object.
	 */
	public Minimise() {
	    this(Double.MAX_VALUE);
	}

	/**
	 * Creates a new <code>Minimise</code> object.
	 * 
	 * @param cost
	 *            the cost value.
	 */
	public Minimise(double cost) {
	    this.cost = cost;
	}

	@Override
	public int compareTo(Cost o) {
	    return compareTo((Minimise) o);
	}

	/**
	 * Compares this cost with the specified cost <code>o</code> for order.
	 * Returns a negative integer, zero, or a positive integer as this cost
	 * is less than, equal to, or greater than the specified cost.
	 * 
	 * @param o
	 *            the cost to be compared.
	 * 
	 * @return a negative integer, zero, or a positive integer as this
	 *         object is less than, equal to, or greater than the specified
	 *         object.
	 */
	public int compareTo(Minimise o) {
	    return Double.compare(o.cost, cost);
	}

	@Override
	public double raw() {
	    return cost;
	}

	/**
	 * @return the value of <code>1/cost</code>.
	 */
	@Override
	public double adjusted() {
	    return 1 / cost;
	}
    }

    /**
     * Class to represent a cost value that should be maximised, where the
     * higher the value the better a solution.
     */
    public static class Maximise implements Cost {
	/**
	 * The cost value.
	 */
	private double cost;

	/**
	 * Creates a new <code>Maximise</code> object.
	 */
	public Maximise() {
	    this(Double.MIN_VALUE);
	}

	/**
	 * Creates a new <code>Maximise</code> object.
	 * 
	 * @param cost
	 *            the cost value.
	 */
	public Maximise(double cost) {
	    this.cost = cost;
	}

	@Override
	public int compareTo(Cost o) {
	    return compareTo((Maximise) o);
	}

	/**
	 * Compares this cost with the specified cost <code>o</code> for order.
	 * Returns a negative integer, zero, or a positive integer as this cost
	 * is less than, equal to, or greater than the specified cost.
	 * 
	 * @param o
	 *            the cost to be compared.
	 * 
	 * @return a negative integer, zero, or a positive integer as this
	 *         object is less than, equal to, or greater than the specified
	 *         object.
	 */
	public int compareTo(Maximise o) {
	    return Double.compare(cost, o.cost);
	}

	@Override
	public double adjusted() {
	    return cost;
	}

	/**
	 * @return the value of the cost.
	 */
	@Override
	public double raw() {
	    return cost;
	}
    }
}