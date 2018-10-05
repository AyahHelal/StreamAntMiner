package moa.classifiers.rules.streamantminer;

import com.yahoo.labs.samoa.instances.Instance;

import moa.AbstractMOAObject;
import moa.classifiers.rules.streamantminer.Attribute.Type;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.Dataset.NOT_COVERED;
import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.BUFFER_SIZE;
import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.RANDOM_GENERATOR;

import java.util.ArrayList;
import java.util.Arrays;

import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.BUFFER_TRIGGER;


public class sAntMiner extends AbstractMOAObject {
	
	

	Dataset dataset;
	
	int buffersize;
	ArchiveFindRuleListActivity activity;
	
	RuleList Model;
	boolean full;
	int counter;
	Scheduler<RuleList> scheduler;
	int[] classes;
	ArrayList<Integer> rulescounts;
	ArrayList<Double> termscount;
	double[] votes;

	public void Initialise(Instance inst)
	{
		counter = 0;
		rulescounts = new ArrayList<Integer>();
		termscount = new ArrayList<Double>();
		dataset = new Dataset();
		classes = new int[inst.numClasses()];
		for(int i = 0 ; i < inst.numAttributes() ; i++)
		{
			if(inst.attribute(i).isNominal())
			{
				Attribute att = new Attribute(Type.NOMINAL,inst.attribute(i).name());
				att.setIndex(i);
				for(int j=0; j < inst.attribute(i).numValues() ; j++)
					att.add(inst.attribute(i).value(j));
				dataset.add(att);
			}
			else
			{
				Attribute att = new Attribute(Type.CONTINUOUS,inst.attribute(i).name());
				att.setIndex(i);
				att.lower(inst.value(i));
				att.upper(inst.value(i));
				dataset.add(att);
			}
		}
		activity = new ArchiveFindRuleListActivity(new Graph(dataset), dataset);
		scheduler = Scheduler.newInstance(1);
		scheduler.setActivity(activity);
		scheduler.initialise();
	}
	
	public double[] vote(Instance inst)
	{
		votes = new double[inst.classAttribute().numValues()];
		if(Model != null)
		{
			Label label = (Label) Model.predictInstance(dataset,inst.toDoubleArray());
			votes[label.value()]++;
		}
		else
		{
			if(dataset.size() > 0)
			{
				Dataset.Instance[] instances = Dataset.Instance.newArray(dataset.size());
				Dataset.Instance.markAll(instances, NOT_COVERED);
				int index = dataset.findMajority(instances,NOT_COVERED);
				votes[index]++;
			}
			else
			{
				int length = dataset.classLength(); 
				votes[CONFIG.get(RANDOM_GENERATOR).nextInt(length)]++;
			}
		}
		return votes;
	}
	
	public void train(Instance inst)
	{	
		boolean dotrigger = triggerAnt();
		if(dataset.size() < CONFIG.get(BUFFER_SIZE) && dotrigger)
		{
			dataset.add(inst.toDoubleArray());
			updateClassesCounter(inst);
		
		}
		else
		{
			
			dataset.add(inst.toDoubleArray());
			activity.setActivity(dataset,Model);
			scheduler.run();
			Model = activity.getBest();			
			dataset.removeInstances();
			classes = new int[dataset.classLength()];
			counter++;
			rulescounts.add(Model.size());
			termscount.add(Model.termscount());
		}
	}

	
	private boolean triggerAnt() {
		
		for(int i=0 ; i < dataset.classLength(); i++)
		{
			if(classes[i] > (CONFIG.get(BUFFER_TRIGGER)*CONFIG.get(BUFFER_SIZE)))
			{
				return false;
			}
		}
		return true;
	}

	private void updateClassesCounter(Instance inst) {

		if(votes[(int)inst.classValue()] == 0)
		{
			classes[(int)inst.classValue()]++;
		}
		
	}

	@Override
	public void getDescription(StringBuilder sb, int indent) {
		// TODO Auto-generated method stub
		
	}

	public String export() {
		
		return Model.export(dataset);
	}

	public double averageRuleSize() {
		double x = rulescounts.size();
		double avg = 0;
		for(int i=0; i < rulescounts.size();i++)
		{
			avg += rulescounts.get(i);
		}
		
		return (double) avg/x;
	}

	public double averageTermSize() {
		double x = termscount.size();
		double avg = 0;
		for(int i=0; i < termscount.size();i++)
		{
			avg += termscount.get(i);
		}
		
		return (double) avg/x;
	}

	public double averageTriggers() {
		return counter;
	}

}