package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;
import moa.classifiers.rules.streamantminer.Dataset.Instance;

/**
 * The <code>Assignator</code> should be implemented by classes that are
 * responsible of assigning the consequent of a rule.
 * 
 * @author Fernando Esteban Barril Otero
 */
public interface Assignator {
    /**
     * The config key for the default assignator instance.
     */
    public final static ConfigKey<Assignator> ASSIGNATOR =
	    new ConfigKey<Assignator>();

    /**
     * Assignes the consequent of the rule.
     * 
     * @param dataset
     *            the current dataset.
     * @param rule
     *            the rule.
     * @param instances
     *            the instaces flag array.
     * 
     * @return the number of uncovered instances.
     */
    public int assign(Dataset dataset, Rule rule, Instance[] instances);
}