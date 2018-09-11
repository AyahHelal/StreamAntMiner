package moa.classifiers.rules.streamantminer;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.Assignator.ASSIGNATOR;
import static moa.classifiers.rules.streamantminer.RuleFunction.DEFAULT_FUNCTION;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;
import moa.classifiers.rules.streamantminer.Dataset.Instance;

public  abstract class Pruner {
    /**
     * The config key for the default pruning instance.
     */
    public final static ConfigKey<Pruner> DEFAULT_PRUNER =
	    new ConfigKey<Pruner>();

    /**
     * Removes irrelevant terms from the antecedent of a rule. This method uses
     * the default rule evaluation function specified by
     * {@link RuleFunction#DEFAULT_FUNCTION}.
     * 
     * @param dataset
     *            the current dataset.
     * @param rule
     *            the rule to be pruned.
     * @param instances
     *            the instaces flag array.
     * 
     * @return the number of uncovered instances remaining.
     */
    public int prune(Dataset dataset, Rule rule, Instance[] instances) {
	return prune(dataset, rule, instances, CONFIG.get(DEFAULT_FUNCTION));
    }

    /**
     * Removes irrelevant terms from the antecedent of a rule.
     * 
     * @param dataset
     *            the current dataset.
     * @param rule
     *            the rule to be pruned.
     * @param instances
     *            the instaces flag array.
     * @param function
     *            the rule evaluation function.
     * 
     * @return the number of uncovered instances remaining.
     */
    public abstract int prune(Dataset dataset,
			      Rule rule,
			      Instance[] instances,
			      RuleFunction function);

    /**
     * A "no-pruner" procedure - i.e., it does not modify the rule.
     */
    public static class None extends Pruner {
	@Override
	public int prune(Dataset dataset,
			 Rule rule,
			 Instance[] instances,
			 RuleFunction function) {
	    Assignator assignator = CONFIG.get(ASSIGNATOR);
	    return assignator.assign(dataset, rule, instances);
	}
    }
}