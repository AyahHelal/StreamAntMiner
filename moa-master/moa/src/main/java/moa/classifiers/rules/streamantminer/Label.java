package moa.classifiers.rules.streamantminer;


/**
 * The <code>Label</code> represents the predicted value of a classification
 * algorithm.
 * 
 * @since 4.5
 * 
 * @author Fernando Esteban Barril Otero
 */
public final class Label implements Prediction {
    /**
     * The class value index represented by the label.
     */
    private int index;

    /**
     * Default constructor.
     */
    public Label() {
	this(Dataset.MISSING_VALUE_INDEX);
    }

    /**
     * Default constructor.
     * 
     * @param index
     *            the class value index.
     */
    public Label(int index) {
	this.index = index;
    }

    /**
     * Returns the class value index represented by the label.
     * 
     * @return the class value index represented by the label.
     */
    public int value() {
	return index;
    }

    @Override
    public String toString(Attribute target) {
	return target.value(index);
    }
}