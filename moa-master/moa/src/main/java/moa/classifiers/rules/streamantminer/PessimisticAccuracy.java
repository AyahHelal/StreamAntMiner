package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Cost.Maximise;
import moa.classifiers.rules.streamantminer.Dataset.Instance;


/**
 * The <code>PessimisticAccuracy</code> class represents a rule quality function
 * based on the C4.5 error-based metric.
 * 
 * @author Fernando Esteban Barril Otero
 */
public class PessimisticAccuracy implements ListMeasure {
	 @Override
	    public Cost evaluate(Dataset dataset, RuleList list) {
		if (list.size() == 0) {
		    return new Maximise();
		}
		
		// updates the coverage of each rule
		list.apply(dataset);

		// we assume that we are dealing with classification rules, which
		// should be the case; there is nothing we can do if this is not
		// the case, apart from raising an exception
		ClassificationRule[] rules = (ClassificationRule[]) list.rules();

		double[] coverage = new double[list.size()];
		double[] errors = new double[list.size()];

		// coverage and errors of each rule

		for (int i = 0; i < coverage.length; i++) {
		    for (int j = 0; j < dataset.classLength(); j++) {
			coverage[i] += rules[i].covered()[j];

			if (j != rules[i].getConsequent().value()) {
			    errors[i] += rules[i].covered()[j];
			}
		    }
		}

		// predicted errors of the list (sum of the estimated errors
		// of each rule)

		double predicted = 0;

		for (int i = 0; i < coverage.length; i++) {
		    predicted += (errors[i] + Stats.errors(coverage[i], errors[i]));
		}

		return new Maximise(1.0 - (predicted / (double) dataset.size()));
	    }
}