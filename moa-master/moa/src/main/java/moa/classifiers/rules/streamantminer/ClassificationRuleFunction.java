package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Dataset.Instance;

public  abstract class ClassificationRuleFunction extends RuleFunction {
    @Override
    public final Cost evaluate(Dataset dataset,
			       Rule rule,
			       Instance[] instances) {
	return evaluate(dataset, (ClassificationRule) rule, instances);
    }

    /**
     * Evaluates the specified classification rule.
     * 
     * @param dataset
     *            the current dataset.
     * @param rule
     *            the rule to be evaluated.
     * @param instances
     *            the instaces flag array.
     * 
     * @return the cost of the rule.
     */
    public abstract Cost evaluate(Dataset dataset,
				  ClassificationRule rule,
				  Instance[] instances);

    /**
     * Returns a confusion matrix based on the covered/uncovered instances
     * information of the rule.
     * 
     * @param rule
     *            a rule.
     * 
     * @return a confusion matrix based on the covered/uncovered instances
     *         information of the rule.
     */
    public BinaryConfusionMatrix fill(ClassificationRule rule) {
	BinaryConfusionMatrix m = new BinaryConfusionMatrix();
	int[] covered = rule.covered();
	int[] uncovered = rule.uncovered();

	for (int i = 0; i < covered.length; i++) {
	    if (i == rule.getConsequent().value()) {
		m.TP += covered[i];
		m.FN += uncovered[i];
	    } else {
		m.FP += covered[i];
		m.TN += uncovered[i];
	    }
	}

	return m;
    }

    /**
     * Struct-like class to represent a binary confusion matrix.
     * 
     * @author Fernando Esteban Barril Otero
     */
    public static class BinaryConfusionMatrix {
	/**
	 * The true-positive value.
	 */
	public double TP = 0;

	/**
	 * The false-positive value.
	 */
	public double FP = 0;

	/**
	 * The false-negative value.
	 */
	public double FN = 0;

	/**
	 * The true-negative value.
	 */
	public double TN = 0;
    }
}