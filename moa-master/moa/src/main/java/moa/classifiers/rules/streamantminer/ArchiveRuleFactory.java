package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Dataset.Instance;
import moa.classifiers.rules.streamantminer.Graph.Entry;
import moa.classifiers.rules.streamantminer.Graph.Vertex;
import moa.classifiers.rules.streamantminer.Rule.Term;

import static moa.classifiers.rules.streamantminer.Graph.START_INDEX;
import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.RANDOM_GENERATOR;

public class ArchiveRuleFactory {
   

    /**
     * Creates a classification rule. Note that this method does not determine
     * the consequent of the rule.
     * 
     * @param level
     *            the id (sequence) of the rule.
     * @param graph
     *            the construction graph.
     * @param heuristic
     *            the heuristic values.
     * @param dataset
     *            the current dataset.
     * @param instances
     *            the covered instances flag.
     * 
     * @return a classification rule.
     */
    public Rule create(int level,
		       Graph graph,
		       Dataset dataset,
		       Instance[] instances) {
	// the rule being created (empty at the start)
	Rule rule = Rule.newInstance(graph.size() / 2);
	int previous = START_INDEX;

	double[] pheromone = new double[graph.size()];
	boolean[] incompatible = new boolean[graph.size()];
	incompatible[START_INDEX] = true;

	while (true) {
	    double total = 0.0;
	    Entry[] neighbours = graph.matrix()[previous];

	    // calculates the probability of visiting vertex i by
	    // multiplying the pheromone and heuristic information (only
	    // compatible vertices are considered)
	    for (int i = 0; i < neighbours.length; i++) {
		if (!incompatible[i] && neighbours[i] != null) {
		    pheromone[i] =
			    neighbours[i].value(level);

		    total += pheromone[i];
		} else {
		    pheromone[i] = 0.0;
		}
	    }

	    if (total == 0.0) {
		// there are no compatible vertices, the creation process
		// is stopped
		break;
	    }

	    // prepares the roulette by accumulation the probabilities,
	    // from 0 to 1
	    double cumulative = 0.0;

	    for (int i = 0; i < pheromone.length; i++) {
		if (pheromone[i] > 0) {
		    pheromone[i] = cumulative + (pheromone[i] / total);
		    cumulative = pheromone[i];
		}
	    }

	    for (int i = (pheromone.length - 1); i >= 0; i--) {
		if (pheromone[i] > 0) {
		    pheromone[i] = 1.0;
		    break;
		}
	    }

	    // roulette selection
	    double slot = CONFIG.get(RANDOM_GENERATOR).nextDouble();
	    int selected = Graph.END_INDEX;

	    for (int i = 0; i < pheromone.length; i++) {
		if (slot < pheromone[i]) {
		    selected = i;
		    break;
		}
	    }

	    if (selected == Graph.END_INDEX) {
		break;
	    }

	    Vertex vertex = graph.vertices()[selected];
	    Term term = new Term(selected, vertex.condition(level));
	    rule.push(term);

	    previous = selected;
	    // make the vertex unavailable
	    incompatible[selected] = true;
	}

	rule.compact();

	return rule;
    }
}