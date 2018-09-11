package moa.classifiers.rules.streamantminer;

import java.util.Random;

import moa.classifiers.rules.streamantminer.Archive.DefaultArchive;



/**
* This class represents a local archive&mdash;i.e., a variable of a solution,
* not the complete solution. It is used to sample attribute values using an
* <code>ACO<sub>MV</sub></code> strategy.
* 
* @author Fernando Esteban Barril Otero
*/
public abstract class VariableArchive<E extends Number & Comparable<E>>
	implements Cloneable {
  

   /**
    * Default value for the weight calculation parameter <i>q</i>.
    */
   public  double DEFAULT_Q = 0.05099;
   
   public  Random RANDOM_GENERATOR = new Random();
   

   /**
    * Default value for the weight calculation parameter <i>q</i>.
    */
   public double DEFAULT_CONVERGENCE_SPEED = 0.6795;

   /**
    * Default value for the weight calculation parameter <i>q</i>.
    */
   public int ARCHIVE_SIZE = 5;

   /**
    * Default value for the precision parameter.
    */
   public double DEFAULT_PRECISION = 2;

   /**
    * Returns an attribute condition sampled using an
    * <code>ACO<sub>MV</sub></code> strategy. Note that
    * <code>ACO<sub>MV</sub></code> sampling only occurs when the archive is
    * complete; random sampling is used.
    * 
    * @return an attribute condition.
    */
   public abstract E sample();

   /**
    * Adds the specified value to the archive.
    * 
    * @param value
    *            the value to add.
    * @param quality
    *            the quality to be associated with the value.
    */
   public abstract void add(E value, double quality);

   /**
    * Updates the archive weights.
    */
   public abstract void update();

   /**
    * Updates the archive weights.
    * 
    * @param archive
    *            the solution archive.
    */
   private void update(DefaultArchive<? extends Entry<E>> archive) {
	double q = DEFAULT_Q;
	double k = ARCHIVE_SIZE;
	Object[] solutions = archive.solutions();

	for (int i = 0; i < solutions.length; i++) {
	    Entry<?> c = (Entry<?>) solutions[i];

	    double exp = -Math.pow((i + 1) - 1, 2) / (2 * q * q * k * k);
	    c.weight = (1 / (q * k * Math.sqrt(2 * Math.PI)))
		    * Math.pow(Math.E, exp);
	}
   }

   /**
    * This class represents the archive for continuous attributes.
    */
   public static class Continuous extends VariableArchive<Double> {
	/**
	 * The precision of the generated values.
	 */
	private final int precision;

	/**
	 * The solution archive.
	 */
	protected DefaultArchive<Entry<Double>> archive;

	/**
	 * The lower bound of values in the attribute domain.
	 */
	private double lower;

	/**
	 * The upper bound of values in the attribute domain.
	 */
	private double upper;

	/**
	 * Default constructor.
	 * 
	 * @param index
	 *            the attribute index.
	 */
	public Continuous(double lower, double upper) {
	    this.lower = lower;
	    this.upper = upper;

	    precision = (int) Math.pow(10, DEFAULT_PRECISION);
	    archive =
		    new DefaultArchive<Entry<Double>>(ARCHIVE_SIZE);
	}

	@Override
	public void add(Double value, double quality) {
	    archive.add(new Entry<Double>(value, quality));
	}

	@Override
	public Double sample() {
	    double sampled = 0.0;

	    if (!archive.isFull()) {
		sampled = (RANDOM_GENERATOR.nextDouble()
			* (upper - lower)) + lower;
	    } else {
		// roulette selection based on the weight of each value

		Comparable<Entry<Double>>[] solutions = archive.solutions();
		double[] probabilities = new double[archive.size()];
		double total = 0.0;

		for (int i = 0; i < probabilities.length; i++) {
		    probabilities[i] = ((Entry<Double>) solutions[i]).weight;
		    total += probabilities[i];
		}

		int selected = (probabilities.length - 1);
		double slot = RANDOM_GENERATOR.nextDouble();
		double cumulative = 0.0;

		for (int i = 0; i < probabilities.length; i++) {
		    probabilities[i] = cumulative + (probabilities[i] / total);
		    cumulative = probabilities[i];

		    if (slot <= probabilities[i]) {
			selected = i;
			break;
		    }
		}

		sampled = value(selected);
	    }

	    sampled = (int) (sampled * precision);
	    return sampled / precision;
	}

	@Override
	public void update() {
	    super.update(archive);
	}

	/**
	 * Returns a value sampled from the archive. The value is sampled using
	 * a gaussian function, where the mean value is set to the value of the
	 * selected solution.
	 * 
	 * @param selected
	 *            the selected solution from the archive.
	 * 
	 * @return a value sampled from the archive.
	 */
	private Double value(int selected) {
	    Comparable<Entry<Double>>[] solutions = archive.solutions();
	    double deviation = 0;

	    for (int i = 0; i < solutions.length; i++) {
		deviation += Math.abs(((Entry<Double>) solutions[i]).value
			- ((Entry<Double>) solutions[selected]).value);
	    }

	    deviation = DEFAULT_CONVERGENCE_SPEED
		    * (deviation / (solutions.length - 1));

	    return (RANDOM_GENERATOR.nextGaussian() * deviation)
		    + ((Entry<Double>) solutions[selected]).value;
	}

	@Override
	public VariableArchive.Continuous clone() {
	    return new VariableArchive.Continuous(lower, upper);
	}
   }

   /**
    * This class represents the archive for nominal attribute values. The value
    * are represented as indexes (e.g., integer values).
    */
   public static class Categorical extends VariableArchive<Integer> {
	/**
	 * The solution archive.
	 */
	protected DefaultArchive<Entry<Integer>> archive;

	/**
	 * The number of values in the nominal attribute domain.
	 */
	private int length;

	/**
	 * Default constructor.
	 * 
	 * @param length
	 *            the number of values in the nominal attribute domain.
	 */
	public Categorical(int length) {
	    this.length = length;
	    archive = new DefaultArchive<Entry<Integer>>(ARCHIVE_SIZE);
	}

	@Override
	public void add(Integer value, double quality) {
	    archive.add(new Entry<Integer>(value, quality));
	}

	@Override
	public Integer sample() {
	    if (!archive.isFull()) {
		// random sampling, since archive is not complete
		return RANDOM_GENERATOR.nextInt(length);
	    } else {
		double[] probabilities = new double[length];
		Comparable<Entry<Integer>>[] solutions = archive.solutions();

		double[] weight = new double[length];
		int n = 0;
		for (int i = 0; i < length; i++) {
		    for (int j = 0; j < solutions.length; j++) {
			Entry<Integer> s = (Entry<Integer>) solutions[j];

			if (i == s.value) {
			    if (weight[i] == 0) {
				// highest quality solution that uses value i
				weight[i] = s.weight;
			    }

			    // number of solutions that use value i
			    probabilities[i]++;
			}
		    }

		    // number of unused values
		    n += (probabilities[i] == 0) ? 1 : 0;
		}

		// calculates the weight of each value

		final double q = DEFAULT_PRECISION;
		double total = 0;

		for (int i = 0; i < length; i++) {
		    if (n > 0) {
			if (probabilities[i] > 0) {
			    probabilities[i] =
				    (weight[i] / probabilities[i]) + (q / n);
			} else {
			    probabilities[i] = (q / n);
			}
		    } else if (probabilities[i] > 0) {
			probabilities[i] = (weight[i] / probabilities[i]);
		    }

		    total += probabilities[i];
		}

		// roulette selection based on the weight of each value

		int value = (length - 1);
		double slot = RANDOM_GENERATOR.nextDouble();
		double cumulative = 0.0;

		for (int i = 0; i < length; i++) {
		    probabilities[i] = cumulative + (probabilities[i] / total);
		    cumulative = probabilities[i];

		    if (slot <= probabilities[i]) {
			value = i;
			break;
		    }
		}

		return value;
	    }
	}

	@Override
	public void update() {
	    super.update(archive);
	}

	@Override
	public VariableArchive.Categorical clone() {
	    return new VariableArchive.Categorical(length);
	}
   }

   /**
    * This class represents an entry on the archive.
    */
   private static class Entry<T> implements Comparable<Entry<T>> {
	/**
	 * The value of the attribute.
	 */
	T value;

	/**
	 * The weight of the value.
	 */
	double weight;

	/**
	 * The quality of the value.
	 */
	double quality;

	/**
	 * Default constructor.
	 * 
	 * @param value
	 *            the value of the attribute.
	 * @param quality
	 *            the quality of the value.
	 */
	Entry(T value, double quality) {
	    this.value = value;
	    this.quality = quality;
	}

	@Override
	public int compareTo(Entry<T> o) {
	    return Double.compare(quality, o.quality);
	}
   }
}