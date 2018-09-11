package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;
import moa.classifiers.rules.streamantminer.Dataset.Instance;

/**
 * Base class for all rule quality functions.
 * 
 * @author Fernando Esteban Barril Otero
 */
public abstract class RuleFunction {
    /**
     * The config key for the default rule function instance.
     */
    public final static ConfigKey<RuleFunction> DEFAULT_FUNCTION =
	    new ConfigKey<RuleFunction>();

    /**
     * Evaluates the specified rule.
     * 
     * @param dataset
     *            the current dataset.
     * @param rule
     *            the rule to be evaluated.
     * @param instances
     *            the instaces flag array.
     * 
     * @return the quality of the rule.
     */
    public abstract Cost evaluate(Dataset dataset,
				  Rule rule,
				  Instance[] instances);
}