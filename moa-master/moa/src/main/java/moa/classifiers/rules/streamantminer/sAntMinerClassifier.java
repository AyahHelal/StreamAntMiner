package moa.classifiers.rules.streamantminer;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.IterativeActivity.MAX_ITERATIONS;
import static moa.classifiers.rules.streamantminer.IterativeActivity.STAGNATION;
import static moa.classifiers.rules.streamantminer.Scheduler.COLONY_SIZE;
import static moa.classifiers.rules.streamantminer.VariableArchive.ARCHIVE_SIZE;
import static moa.classifiers.rules.streamantminer.VariableArchive.CONVERGENCE_SPEED;
import static moa.classifiers.rules.streamantminer.VariableArchive.PRECISION;
import static moa.classifiers.rules.streamantminer.VariableArchive.Q;
import static moa.classifiers.rules.streamantminer.Assignator.ASSIGNATOR;
import static moa.classifiers.rules.streamantminer.ListMeasure.DEFAULT_MEASURE;
import static moa.classifiers.rules.streamantminer.ListPruner.DEFAULT_LIST_PRUNER;
import static moa.classifiers.rules.streamantminer.Pruner.DEFAULT_PRUNER;
import static moa.classifiers.rules.streamantminer.RuleFunction.DEFAULT_FUNCTION;
import static moa.classifiers.rules.streamantminer.ArchiveFindRuleListActivity.UNCOVERED;
import static moa.classifiers.rules.streamantminer.ArchivePheromonePolicy.EVAPORATION_FACTOR;
import static moa.classifiers.rules.streamantminer.ArchivePheromonePolicy.P_BEST;
import static moa.classifiers.rules.streamantminer.SinglePassPruner.MINIMUM_CASES;


import java.util.Random;


import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import moa.classifiers.rules.streamantminer.Config.ConfigKey;


public class sAntMinerClassifier extends AbstractClassifier {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static ConfigKey<Random> RANDOM_GENERATOR = new ConfigKey<>();
    public final static ConfigKey<Integer> BUFFER_SIZE = new ConfigKey<>();

    
    
 
 
    boolean initialise = false;
    
    sAntMiner algorithm;
    
	@Override
	public boolean isRandomizable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double[] getVotesForInstance(Instance inst) {
		if(!initialise)
		{
			defaults();
			algorithm = new sAntMiner();
			algorithm.Initialise(inst);
			initialise = true;
		}
		return algorithm.vote(inst);
	}

	private void defaults() {
		// configuration not set via command line
		CONFIG.set(RANDOM_GENERATOR, new Random());
		CONFIG.set(ASSIGNATOR, new MajorityAssignator());
		CONFIG.set(P_BEST, 0.05);
		CONFIG.set(Rule.DEFAULT_RULE, ClassificationRule.class);

		// default configuration values
		CONFIG.set(BUFFER_SIZE, 500);
		CONFIG.set(COLONY_SIZE, 5);
		CONFIG.set(ARCHIVE_SIZE, 5);
		CONFIG.set(Q, 0.05099);
		CONFIG.set(CONVERGENCE_SPEED, 0.6795);
		CONFIG.set(PRECISION, 2.0);
		CONFIG.set(MAX_ITERATIONS, 50);
		CONFIG.set(MINIMUM_CASES, 5);
		CONFIG.set(EVAPORATION_FACTOR, 0.9);
		CONFIG.set(DEFAULT_MEASURE, new PessimisticAccuracy());
		CONFIG.set(UNCOVERED, 0.001);
		CONFIG.set(STAGNATION, 5);
		CONFIG.set(DEFAULT_PRUNER, new SinglePassPruner());
		CONFIG.set(DEFAULT_LIST_PRUNER, new ListPruner.None());
		CONFIG.set(DEFAULT_FUNCTION, new SensitivitySpecificity());
	
	}

	@Override
	public void resetLearningImpl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trainOnInstanceImpl(Instance inst) {
		algorithm.train(inst);
	}

	@Override
	protected Measurement[] getModelMeasurementsImpl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getModelDescription(StringBuilder out, int indent) {
		// TODO Auto-generated method stub
		
	}

}
