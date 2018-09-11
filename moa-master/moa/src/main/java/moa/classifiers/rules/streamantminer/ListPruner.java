package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;

/**
 * The <code>ListPruner</code> should be implemented by classes that are
 * responsible for pruning list of rules.
 * 
 * @author Fernando Esteban Barril Otero
 */
public interface ListPruner {
    /**
     * The config key for the default pruning instance.
     */
    public final static ConfigKey<ListPruner> DEFAULT_LIST_PRUNER =
	    new ConfigKey<ListPruner>();

    /**
     * Removes irrelevant terms from the antecedent of rules and even entires
     * rules from the specified list of rules.
     * 
     * @param dataset
     *            the current dataset.
     * @param list
     *            the list of rules to be pruned.
     */
    public abstract void prune(Dataset dataset, RuleList list);

    public static class None implements ListPruner {
	/**
	 * This implementation leaves the list of rules unchanged.
	 */
	public void prune(Dataset dataset, RuleList list) {
	};
    }
}