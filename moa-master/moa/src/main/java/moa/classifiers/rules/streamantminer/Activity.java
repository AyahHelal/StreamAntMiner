package moa.classifiers.rules.streamantminer;


/**
 * The <code>Activity</code> interface specifies the steps of a basic ACO
 * algorithm. An implementation of this interface will usually be wrapped in a
 * <code>Scheduler</code>, which will determine the order of the steps.
 * 
 * @author Fernando Esteban Barril Otero
 * 
 * @see Scheduler
 * 
 * @param <T>
 *            the type of solution created by the activity.
 */
public interface Activity<T extends Comparable<T>> {
    /**
     * Creates a single solution to the problem. This implementation must be
     * thread-safe in order to be executed by the {@link ParallelScheduler}.
     * 
     * @return the created solution.
     */
    public T create();

    /**
     * Applies local search to the candidate solutions. More generally, local
     * search is one example of what have been called daemon actions.
     * 
     * @param archive
     *            the solution archive.
     * 
     * @return <code>true</code> if the solutions in the archive have been
     *         modified; <code>false</code> otherwise.
     */
    public boolean search(Archive<T> archive);

    /**
     * Performs the initialisation step.
     */
    public void initialise();

    /**
     * Indicates if the search should stop.
     * 
     * @return <code>true</code> if the search should stop; <code>false</code>
     *         otherwise.
     */
    public boolean terminate();

    /**
     * Updates the state of the activity. In most cases, this involves updating
     * the pheromone values using the iteration-best solution.
     * 
     * @param archive
     *            the solution archive.
     */
    public void update(Archive<T> archive);
}