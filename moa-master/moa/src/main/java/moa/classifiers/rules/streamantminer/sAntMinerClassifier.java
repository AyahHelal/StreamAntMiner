package moa.classifiers.rules.streamantminer;

import java.util.Random;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import moa.classifiers.rules.streamantminer.Config.ConfigKey;


public class sAntMinerClassifier extends AbstractClassifier {
	
    public final static ConfigKey<Random> RANDOM_GENERATOR = new ConfigKey<>();
    
    
    public FloatOption convergenceOption = new FloatOption(
			"convergenceOption",
			'c',
			"The convergence archive option.",
			0.05099);
 
    public FloatOption localsearchOption = new FloatOption(
			"localsearchOption",
			'l',
			"The local search option.",
			0.6795);
 

    public IntOption startRulesPerClassSizeOption = new IntOption(
			"startRulesPerClassSizeOption",
			's',
			"The start Rules per class max size Option.",
			20);
 
    public IntOption BufferSize = new IntOption(
			"BufferSize",
			'b',
			"The Buffer Size to create new rule set Option.",
			1000);

    public IntOption AntsNumberOption = new IntOption(
			"AntsNumberOption",
			'r',
			"The Ants numbers to create new rule set.",
			2);
 
    public IntOption itrationsNumberOption = new IntOption(
			"itrationsNumberOption",
			't',
			"The itrations numbers to create new rule set.",
			5);
 
    public FloatOption PruningConfidenceOption = new FloatOption(
			"pruningConfidencepercentage",
			'p',
			"The pruning Confidence set Option.", 
			0.0001);
 
 

	@Override
	public boolean isRandomizable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double[] getVotesForInstance(Instance inst) {
		return null;
	}

	@Override
	public void resetLearningImpl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trainOnInstanceImpl(Instance inst) {
		
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
