package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Attribute.Condition;
import moa.classifiers.rules.streamantminer.Dataset.Instance;
import moa.classifiers.rules.streamantminer.Rule.Term;
import moa.classifiers.rules.streamantminer.Config.ConfigKey;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.Dataset.COVERED;
import static moa.classifiers.rules.streamantminer.Assignator.ASSIGNATOR;
/**
 * This class represents a pruning procedure that evaluates all terms at once
 * (single pass over the dataset) and removes terms, from the end to the start
 * of the rule, as long as the quality improves.
 * <p>
 * Note that the rule might be empty at the end of the pruning, meaning that no
 * term covered the minimum number of instances required.
 * </p>
 * 
 * @author Fernando Esteban Barril Otero
 */
public class SinglePassPruner extends Pruner {
	
	
	public final static ConfigKey<Integer> MINIMUM_CASES = new ConfigKey<>();
    
    public int prune(Dataset dataset,
		     Rule rule,
		     Instance[] instances,
		     RuleFunction function) {
	Term[] terms = rule.terms();
	Coverage[] coverage = new Coverage[terms.length];

	for (int j = 0; j < coverage.length; j++) {
	    coverage[j] = new Coverage(dataset.classLength());
	}

	// (1) determines the coverage of each term

		int start = 0;

		while (start < coverage.length) {
		    for (int i = 0; i < dataset.size(); i++) {
			// only considers instances not covered
			if (instances[i].flag != COVERED) {
			    int c = (int) dataset.value(i, dataset.classIndex());

			    for (int j = start; j < terms.length; j++) {
				if (terms[j].isEnabeld()) {
				    Condition condition = terms[j].condition();
				    double v = dataset.value(i, condition.attribute);

				    if (condition.satisfies(v)) {
					coverage[j].covered[c]++;
				    } else {
					coverage[j].uncovered[c]++;
					// stops checking the remaining terms
					break;
				    }
				}
			    }
			}
		    }

		    // checks that the first term of the rule cover the minimum number
		    // of cases,
		    // otherwise disables it and repeat the coverage of the rule
		    if (coverage[start].total() < CONFIG.get(MINIMUM_CASES)) {
			terms[start].setEnabeld(false);
			start++;
			// reset coverage for all terms
			for (int j = 0; j < coverage.length; j++) {
			    coverage[j] = new Coverage(dataset.classLength());
			}
		    } else {
			// when the rule covers the minimum number of cases, stop the
			// coverage test
			break;
		    }
		}
	// (2) determines the quality of each term

	Assignator assignator = CONFIG.get(ASSIGNATOR);
	ClassificationRule cRule = (ClassificationRule) rule;

	int selected = -1;
	Cost best = null;

	for (int i = 0; i < coverage.length; i++) {
	    // the rule must cover a minimum number of cases, therefore
	    // only terms that cover more than the limit are considered
	    if (coverage[i].total() >= CONFIG.get(MINIMUM_CASES)) {
		cRule.covered(coverage[i].covered);
		cRule.uncovered(coverage[i].uncovered);
		assignator.assign(dataset, rule, instances);

		Cost current = function.evaluate(dataset, rule, instances);

		if (best == null || current.compareTo(best) >= 0) {
		    selected = i;
		    best = current;
		}
	    } else {
		// stops the procedure, since any remaining term will not
		// cover the minimum number
		break;
	    }
	}

	// (3) disables and removes unused terms

	for (int i = selected + 1; i < terms.length; i++) {
	    terms[i].setEnabeld(false);
	}

	rule.setQuality(best);
	rule.compact();
	rule.apply(dataset, instances);

	return assignator.assign(dataset, rule, instances);
    }

    /**
     * Class to store the coverage information of a term.
     * 
     * @author Fernando Esteban Barril Otero
     */
    private static class Coverage {
	/**
	 * Covered instances information.
	 */
	int[] covered;

	/**
	 * Uncovered instances information.
	 */
	int[] uncovered;

	/**
	 * Default constructor.
	 * 
	 * @param length
	 *            the number of classes.
	 */
	Coverage(int length) {
	    covered = new int[length];
	    uncovered = new int[length];
	}

	/**
	 * Returns the total number of covered examples.
	 * 
	 * @return the total number of covered examples.
	 */
	int total() {
	    int total = 0;

	    for (int i = 0; i < covered.length; i++) {
		total += covered[i];
	    }

	    return total;
	}
    }
}