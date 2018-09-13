package moa.classifiers.rules.streamantminer;

import com.yahoo.labs.samoa.instances.Instance;

import moa.AbstractMOAObject;
import moa.classifiers.rules.streamantminer.Attribute.Type;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import static moa.classifiers.rules.streamantminer.Dataset.NOT_COVERED;
import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.BUFFER_SIZE;
import static moa.classifiers.rules.streamantminer.sAntMinerClassifier.RANDOM_GENERATOR;

public class sAntMiner extends AbstractMOAObject {
	
	Dataset dataset;
	
	int buffersize;
	ArchiveFindRuleListActivity activity;
	
	RuleList Model;
	
	Scheduler<RuleList> scheduler;
	
	public void Initialise(Instance inst)
	{
		dataset = new Dataset();
		for(int i=0;i < inst.numAttributes();i++)
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
		double[] votes = new double[inst.classAttribute().numValues()];
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
		if(dataset.size() < CONFIG.get(BUFFER_SIZE))
		{
			dataset.add(inst.toDoubleArray());
			
		}
		else
		{
			activity.setDataset(dataset);
			scheduler.run();
			Model = activity.getBest();
			dataset.removeInstances();
		}
	}

	@Override
	public void getDescription(StringBuilder sb, int indent) {
		// TODO Auto-generated method stub
		
	}
}