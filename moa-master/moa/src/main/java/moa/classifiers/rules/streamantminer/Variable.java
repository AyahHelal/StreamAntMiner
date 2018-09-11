package moa.classifiers.rules.streamantminer;

import static moa.classifiers.rules.streamantminer.Attribute.EQUAL_TO;
import static moa.classifiers.rules.streamantminer.Attribute.GREATER_THAN;
import static moa.classifiers.rules.streamantminer.Attribute.LESS_THAN_OR_EQUAL_TO;

import moa.classifiers.rules.streamantminer.Attribute.Condition;
import moa.classifiers.rules.streamantminer.VariableArchive;

/**
 * This class represents a term (variable) for the archive construction
 * procedure.
 * 
 * @author Fernando Esteban Barril Otero
 */
public abstract class Variable implements Cloneable {
    /**
     * Returns a condition to this variable. The condition is sampled from a
     * variable archive.
     * 
     * @return a condition to this variable.
     */
    public abstract Condition sample();

    /**
     * Adds the specified condition to the archive.
     * 
     * @param condition
     *            the condition to add.
     * @param quality
     *            the quality to be associated with the value.
     */
    public abstract void add(Condition condition, double quality);

    @Override
    public Variable clone() {
	try {
	    return (Variable) super.clone();
	} catch (CloneNotSupportedException e) {
	    throw new InternalError(e);
	}
    }

    /**
     * This class represents a continuous attribute term.
     */
    public static class Continuous extends Variable {
	/**
	 * Operator archive with 2 possible values (0=less than or equal to,
	 * 1=grater than).
	 */
	private VariableArchive.Categorical operator;

	/**
	 * Value archive.
	 */
	private VariableArchive.Continuous value;

	/**
	 * Default constructor.
	 * 
	 * @param lower
	 *            lower bound for the sampling procedure.
	 * @param upper
	 *            upper bound for the sampling procedure.
	 */
	public Continuous(double lower, double upper) {
	    operator = new VariableArchive.Categorical(2);
	    value = new VariableArchive.Continuous(lower, upper);
	}

	@Override
	public Condition sample() {
	    Condition condition = new Condition();

	    condition.relation =
		    (operator.sample() == 0) ? LESS_THAN_OR_EQUAL_TO
			    : GREATER_THAN;
	    condition.value[0] = value.sample();

	    return condition;
	}

	@Override
	public void add(Condition condition, double quality) {
	    operator.add(Integer.valueOf(condition.relation), quality);
	    value.add(condition.value[0], quality);
	    operator.update();
	    value.update();
	}

	@Override
	public Continuous clone() {
	    Continuous clone = (Continuous) super.clone();
	    clone.operator = operator.clone();
	    clone.value = value.clone();
	    return clone;
	}
    }

    /**
     * This class represents a nominal attribute term.
     */
    public static class Nominal extends Variable {
	/**
	 * Value archive.
	 */
	private VariableArchive.Categorical value;

	/**
	 * Default constructor.
	 * 
	 * @param length
	 *            the number of different nominal values.
	 */
	public Nominal(int length) {
	    value = new VariableArchive.Categorical(length);
	}

	@Override
	public Condition sample() {
	    Condition condition = new Condition();
	    condition.relation = EQUAL_TO;
	    condition.value[0] = value.sample();

	    return condition;
	}

	@Override
	public void add(Condition condition, double quality) {
	    value.add(Integer.valueOf((int) condition.value[0]), quality);
	    value.update();
	}

	@Override
	public Nominal clone() {
	    Nominal clone = (Nominal) super.clone();
	    clone.value = value.clone();
	    return clone;
	}
    }
}