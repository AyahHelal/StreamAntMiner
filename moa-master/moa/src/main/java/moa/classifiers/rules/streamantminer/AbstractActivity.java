package moa.classifiers.rules.streamantminer;



/**
 * Skeleton activity implementation.
 * 
 * @author Fernando Esteban Barril Otero
 */
public abstract class AbstractActivity<T extends Comparable<T>>
	implements Activity<T> {
    /**
     * Default (empty) implementation.
     */
    @Override
    public boolean search(Archive<T> archive) {
	return false;
    }
}