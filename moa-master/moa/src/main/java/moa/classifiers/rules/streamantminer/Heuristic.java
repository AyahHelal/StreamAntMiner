package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;
import moa.classifiers.rules.streamantminer.Dataset.Instance;
import moa.classifiers.rules.streamantminer.Graph.Entry;

/**
 * The <code>Heuristic</code> represents the background knowledge that can be
 * incorporated in the construction graph.
 */
public interface Heuristic {
    /**
     *
     * Config key for the default <code>Heuristic</code> instance.
     */
    public static final ConfigKey<Heuristic> DEFAULT_HEURISTIC =
	    new ConfigKey<Heuristic>();

    /**
     * The config key to indicate if the heuristic value is recomputed at each
     * level of the tree.
     */
    public final static ConfigKey<Boolean> DYNAMIC_HEURISTIC =
	    new ConfigKey<Boolean>();

    /**
     * Computes the heuristic information.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     * @param instances
     *            the covered instances flags.
     * 
     * @return an array with the heuristic values for each vertex of the graph.
     */
    public Entry[] compute(Graph graph, Dataset dataset, Instance[] instances);

    /**
     * Computes the heuristic information.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     * @param instances
     *            the covered instances flags.
     * @param used
     *            indicates the vertices already used.
     * 
     * @return an array with the heuristic values for each vertex of the graph.
     */
    public Entry[] compute(Graph graph,
			   Dataset dataset,
			   Instance[] instances,
			   boolean[] used);

    /**
     * Computes the heuristic information.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     * @param instances
     *            the covered instances flags.
     * @param target
     *            the target class value.
     * 
     * @return an array with the heuristic values for each vertex of the graph.
     */
    public Entry[] compute(Graph graph,
			   Dataset dataset,
			   Instance[] instances,
			   int target);

    /**
     * Computes the heuristic information.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     * @param instances
     *            the covered instances flags.
     * @param used
     *            indicates the vertices already used.
     * @param target
     *            the target class value.
     * 
     * @return an array with the heuristic values for each vertex of the graph.
     */
    public Entry[] compute(Graph graph,
			   Dataset dataset,
			   Instance[] instances,
			   boolean[] used,
			   int target);

    /**
     * No heuristic information implementation - e.g., heuristic value is set to
     * 1 for all unused attributes.
     * 
     * @author Fernando Esteban Barril Otero
     */
    public static class None implements Heuristic {
	@Override
	public Entry[] compute(Graph graph,
			       Dataset dataset,
			       Instance[] instances) {
	    return compute(graph, dataset, instances, new boolean[0]);
	}

	@Override
	public Entry[] compute(Graph graph,
			       Dataset dataset,
			       Instance[] instances,
			       boolean[] used) {
	    Entry[] heuristic = Entry.initialise(new Entry[graph.size()]);

	    for (int i = 0; i < heuristic.length; i++) {
		double value = 1.0;

		heuristic[i].setInitial(value);
		heuristic[i].set(0, value);
	    }

	    return heuristic;
	}

	@Override
	public Entry[] compute(Graph graph,
			       Dataset dataset,
			       Instance[] instances,
			       int target) {
	    return compute(graph, dataset, instances, new boolean[0], target);
	}

	@Override
	public Entry[] compute(Graph graph,
			       Dataset dataset,
			       Instance[] instances,
			       boolean[] used,
			       int target) {
	    return compute(graph, dataset, instances, used);
	}
    }
}