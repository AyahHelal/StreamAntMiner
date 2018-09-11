package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Graph.Vertex;
import moa.classifiers.rules.streamantminer.Rule.Term;

public class ArchivePheromonePolicy extends LevelPheromonePolicy {
    @Override
    public void update(Graph graph, RuleList list) {
	// updates the pheromone on selected vertices 
	super.update(graph, list);


	final int size = list.size() - (list.hasDefault() ? 1 : 0);
	double delta = list.getQuality().raw() / 5.0;
	int level = 0;

	for (int i = 0; i < size; i++) {
	    Rule rule = list.rules()[i];

	    for (int j = 0; j < rule.size(); j++) {
		Term term = rule.terms()[j];
		Vertex vertex = graph.vertices()[term.index()];
		vertex.update(level, term.condition(), delta);
	    }

	    level++;
	}
    }
}
