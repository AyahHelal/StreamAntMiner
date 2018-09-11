package moa.classifiers.rules.streamantminer;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.Dataset.NOT_COVERED;
import static moa.classifiers.rules.streamantminer.Dataset.RULE_COVERED;
import static moa.classifiers.rules.streamantminer.Pruner.DEFAULT_PRUNER;
import static moa.classifiers.rules.streamantminer.Assignator.ASSIGNATOR;
import static moa.classifiers.rules.streamantminer.Heuristic.DEFAULT_HEURISTIC;
import static moa.classifiers.rules.streamantminer.ListMeasure.DEFAULT_MEASURE;
import static moa.classifiers.rules.streamantminer.ListPruner.DEFAULT_LIST_PRUNER;

import moa.classifiers.rules.streamantminer.Config.ConfigKey;
import moa.classifiers.rules.streamantminer.Dataset.Instance;
import moa.classifiers.rules.streamantminer.Graph.Entry;

public class ArchiveFindRuleListActivity extends IterativeActivity<RuleList> {
    /**
     * The config key for the percentage of uncovered instances allowed.
     */
    public static final ConfigKey<Double> UNCOVERED = new ConfigKey<Double>();

    /**
     * The current dataset.
     */
    private Dataset dataset;

    /**
     * The construction graph.
     */
    private Graph graph;

    /**
     * The ACO pheromone policy.
     */
    private ArchivePheromonePolicy policy;

    /**
     * The ACO rule factory.
     */
    private ArchiveRuleFactory factory;

    /**
     * The convergence termination criteria counter.
     */
    private boolean reset;


    /**
     * Creates a new <code>FindRuleListActivity</code> object.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     */
    public ArchiveFindRuleListActivity(Graph graph, Dataset dataset) {
	this(graph, dataset, new ArchiveRuleFactory());
    }

    /**
     * Creates a new <code>FindRuleListActivity</code> object.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     * @param factory
     *            the rule factory.
     */
    public ArchiveFindRuleListActivity(Graph graph,
				Dataset dataset,
				ArchiveRuleFactory factory) {
	this(graph, dataset, factory, new ArchivePheromonePolicy());
    }

    /**
     * Creates a new <code>FindRuleListActivity</code> object.
     * 
     * @param graph
     *            the construction graph.
     * @param dataset
     *            the current dataset.
     * @param factory
     *            the rule factory.
     * @param policy
     *            the pheromone policy.
     */
    public ArchiveFindRuleListActivity(Graph graph,
				Dataset dataset,
				ArchiveRuleFactory factory,
				ArchivePheromonePolicy policy) {
	this.graph = graph;
	this.dataset = dataset;
	this.factory = factory;
	this.policy = policy;
    }

    @Override
    public RuleList create(Dataset dataset) {
    this.dataset = dataset;
	Instance[] instances = Instance.newArray(dataset.size());
	Instance.markAll(instances, NOT_COVERED);
	

	RuleList list = new RuleList();
	list.setIteration(iteration);

	int available = dataset.size();
	int uncovered = (int) ((dataset.size() * CONFIG.get(UNCOVERED)) + 0.5);

	while (available >= uncovered) {

	    // creates a rule for the current level

	    Rule rule = factory
		    .create(list.size(), graph, dataset, instances);

	    available =
		    CONFIG.get(DEFAULT_PRUNER).prune(dataset, rule, instances);

	    list.add(rule);

	    if (rule.size() == 0) {
		break;
	    }

	    // marks the instances covered by the current rule as
	    // COVERED, so they are not available for the next
	    // iterations
	    Dataset.markCovered(instances);
	}

	if (!list.hasDefault()) {
	    if (available == 0) {
		Instance.markAll(instances, NOT_COVERED);
	    }

	    Rule rule = Rule.newInstance();
	    rule.apply(dataset, instances);
	    CONFIG.get(ASSIGNATOR).assign(dataset, rule, instances);
	    list.add(rule);
	}

	// global pruning
	CONFIG.get(DEFAULT_LIST_PRUNER).prune(dataset, list);
	// evaluates the list
	list.setQuality(CONFIG.get(DEFAULT_MEASURE).evaluate(dataset, list));

	return list;
    }

    @Override
    public void initialise() {
	super.initialise();
	policy.initialise(graph);

	reset = true;

	// (initial) heuristic of the whole dataset

	Instance[] instances = Instance.newArray(dataset.size());
	Instance.markAll(instances, RULE_COVERED);

    }

    @Override
    public boolean terminate() {
	if (stagnation > CONFIG.get(STAGNATION)) {
	    if (reset) {
		policy.initialise(graph);
		stagnation = 0;
		reset = false;
	    } else {
		return true;
	    }
	}

	return super.terminate();
    }

    @Override
    public void update(Archive<RuleList> archive) {
	super.update(archive);
	policy.update(graph, archive.highest());
    }
}
