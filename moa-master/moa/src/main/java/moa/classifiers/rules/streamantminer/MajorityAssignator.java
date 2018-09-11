package moa.classifiers.rules.streamantminer;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.RANDOM_GENERATOR;
import java.util.Arrays;

import moa.classifiers.rules.streamantminer.Dataset.Instance;


public class MajorityAssignator implements Assignator {
    @Override
    public int assign(Dataset dataset, Rule rule, Instance[] instances) {
	if (!(rule instanceof ClassificationRule)) {
	    throw new IllegalArgumentException("Expecting a classification rule: "
		    + rule.getClass().getName());
	}

	ClassificationRule cRule = (ClassificationRule) rule;

	int[] covered = cRule.covered();
	int[] uncovered = cRule.uncovered();

	boolean[] candidates = new boolean[covered.length];
	int size = 0;

	int available = 0;
	int majority = -1;

	for (int i = 0; i < covered.length; i++) {
	    if (majority == -1 || covered[majority] < covered[i]) {
		Arrays.fill(candidates, false);

		majority = i;
		candidates[majority] = true;
		size = 1;
	    } else if (covered[majority] == covered[i]) {
		candidates[i] = true;
		size++;
	    }

	    available += uncovered[i];
	}

	if (size == 1) {
	    cRule.setConsequent(new Label(majority));
	} else {
	    if (cRule.getConsequent() == null
		    || !candidates[cRule.getConsequent().value()]) {
		double[] probabilities = new double[covered.length];
		double accumulated = 0.0;
		int last = 0;

		for (int i = 0; i < probabilities.length; i++) {
		    if (candidates[i]) {
			probabilities[i] = accumulated + (1.0 / (double) size);
			accumulated = probabilities[i];
			last = i;
		    } else {
			probabilities[i] = 0;
		    }
		}

		probabilities[last] = 1.0;

		double slot = CONFIG.get(RANDOM_GENERATOR).nextDouble();

		for (int i = 0; i < probabilities.length; i++) {
		    if (slot < probabilities[i]) {
			majority = i;
			break;
		    }
		}

		cRule.setConsequent(new Label(majority));
	    }
	}

	return available;
    }
}