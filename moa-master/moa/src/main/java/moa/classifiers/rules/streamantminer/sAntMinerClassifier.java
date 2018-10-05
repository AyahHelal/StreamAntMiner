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

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import moa.classifiers.rules.streamantminer.Config.ConfigKey;


public class sAntMinerClassifier extends AbstractClassifier {
	
    /**
	 * 
	 */
	public FloatOption QOption = new FloatOption(
			"QOption",
			'q',
			"The local search archive option.",
			0.05099);
 
	public FloatOption convergenceOption = new FloatOption(
			"convergenceOption",
			'c',
			"The convergence option.",
			0.6795);
 
	public IntOption archiveSizeOption = new IntOption(
			"archiveSizeOption",
			'a',
			"The local search option.",
			10);
	
	 public IntOption BufferSize = new IntOption(
				"BufferSize",
				'b',
				"The Buffer Size to create new rule set Option.",
				1000);
	
	public IntOption ColonySizeOption = new IntOption(
				"ColonySize",
				'r',
				"The Ants numbers to create new rule set.",
				5);
	 
	public IntOption itrationsOption = new IntOption(
				"itrationsNumberOption",
				'i',
				"The itrations numbers to create new rule set.",
				25);
	
	public FloatOption BufferTrigger = new FloatOption(
			"BufferTriggerinstance",
			't',
			"The precentage of the buffer filled with one miss classified instance ",
			0.20);
	
	
	private static final long serialVersionUID = 1L;
	public final static ConfigKey<Random> RANDOM_GENERATOR = new ConfigKey<>();
    public final static ConfigKey<Integer> BUFFER_SIZE = new ConfigKey<>();

    public final static ConfigKey<Double> BUFFER_TRIGGER = new ConfigKey<>();
    
 
 
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
		CONFIG.set(BUFFER_TRIGGER, BufferTrigger.getValue());
		CONFIG.set(Rule.DEFAULT_RULE, ClassificationRule.class);

		// default configuration values
		CONFIG.set(BUFFER_SIZE, BufferSize.getValue());
		CONFIG.set(COLONY_SIZE, ColonySizeOption.getValue());
		CONFIG.set(ARCHIVE_SIZE, archiveSizeOption.getValue());
		CONFIG.set(Q, QOption.getValue());
		CONFIG.set(CONVERGENCE_SPEED, convergenceOption.getValue());
		CONFIG.set(PRECISION, 2.0);
		CONFIG.set(MAX_ITERATIONS, itrationsOption.getValue());
		CONFIG.set(MINIMUM_CASES,5);
		CONFIG.set(EVAPORATION_FACTOR, 0.9);
		CONFIG.set(DEFAULT_MEASURE, new PessimisticAccuracy());
		CONFIG.set(UNCOVERED, 0.01);
		CONFIG.set(STAGNATION, 2);
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
		double averageRuleSize = algorithm.averageRuleSize();
		double averageTermCount = algorithm.averageTermSize();
		double averageTriggers = algorithm.averageTriggers();
		return new Measurement[]{
				new Measurement("Rule Count", averageRuleSize)
				,new Measurement("Term Count", averageTermCount)
				,new Measurement("Number of Triggers ", averageTriggers)
		};

	}

	@Override
	public void getModelDescription(StringBuilder out, int indent) {
	//	out.append(algorithm.export());
	}

}
