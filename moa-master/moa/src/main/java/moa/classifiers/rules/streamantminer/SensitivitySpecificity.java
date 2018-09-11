package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Cost.Maximise;
import moa.classifiers.rules.streamantminer.Dataset.Instance;

/**
 * The <code>SensitivitySpecificity</code> class represents a rule quality
 * function based on the Sensitivity and Specificity measure. The quality of a
 * rule is equivalent to <i>Sensitivity</i> x <i>Specificity</i>.
 * 
 * @author Fernando Esteban Barril Otero
 */
public class SensitivitySpecificity extends ClassificationRuleFunction {
    
	@Override
    public Maximise evaluate(Dataset dataset,
    		ClassificationRule rule,
			     Instance[] instances) {
	BinaryConfusionMatrix m = fill(rule);

	double value = (m.TP / (m.TP + m.FN)) * (m.TN / (m.TN + m.FP));

	return new Maximise(Double.isNaN(value) ? 0.0 : value);
    }

}