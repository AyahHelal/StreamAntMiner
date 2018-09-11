package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;

/**
 * This class is the base class for all list quality measures.
 * 
 * @author Fernando Esteban Barril Otero
 */
public interface ListMeasure {
    /**
     *
     * Config key for the default list measure instance.
     */
    public static final ConfigKey<ListMeasure> DEFAULT_MEASURE =
	    new ConfigKey<ListMeasure>();

    /**
     * Returns the quality of the specified list of rules.
     * 
     * @param dataset
     *            the current dataset.
     * @param list
     *            the list of rules.
     * 
     * @return the quality of the specified list of rules.
     */
    public Cost evaluate(Dataset dataset, RuleList list);
}