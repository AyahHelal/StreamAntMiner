package moa.classifiers.rules.streamantminer;

import static moa.classifiers.rules.streamantminer.Config.CONFIG;
import moa.classifiers.rules.streamantminer.Config.ConfigKey;

public abstract class IterativeActivity<T extends Comparable<T>> extends AbstractActivity<T> {
	/**
	 * The config key for the maximum number of iterations.
	 */
	public final static ConfigKey<Integer> MAX_ITERATIONS = new ConfigKey<>();

	/**
	 * The config key for the stagnation test. If no improvement in the global best
	 * is observed in <code>STAGNATION</code> iterations, the creation process is
	 * stops. This class does not include this as a termination criteria, but it is
	 * available to subclasses.
	 */
	public final static ConfigKey<Integer> STAGNATION = new ConfigKey<Integer>();

	/**
	 * The iteration number;
	 */
	protected int iteration;

	/**
	 * The stagnation counter. It represents the number of iterations without an
	 * improvement in the global best solution.
	 */
	protected int stagnation;

	/**
	 * The best-so-far candidate solution.
	 */
	protected T globalBest;

	/**
	 * Initialises the iteration number to <code>0</code>.
	 */
	@Override
	public void initialise() {
		iteration = 0;
		stagnation = 0;
		//globalBest = null;
		
	}

	/**
	 * Checks whether the maximum number of iterations has been reached.
	 */
	@Override
	public boolean terminate() {
		return iteration >= CONFIG.get(MAX_ITERATIONS);
	}

	@Override
	public void update(Archive<T> archive) {
		iteration++;

		T candidate = archive.highest();

		// updates the global best

		if (globalBest == null || candidate.compareTo(globalBest) > 0) {
			globalBest = candidate;
			stagnation = 0;
		} else if (candidate.compareTo(globalBest) == 0) {
			stagnation++;
		}
	}

	/**
	 * Returns the best solution found over all iterations.
	 * 
	 * @return the best solution found over all iterations.
	 */
	public T getBest() {
		return globalBest;
	}
}
