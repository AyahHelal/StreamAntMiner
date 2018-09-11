package moa.classifiers.rules.streamantminer;

import moa.classifiers.rules.streamantminer.Attribute;

/**
 * Interface to represent a predicted value.
 * 
 * @since 4.5
 * 
 * @author Fernando Esteban Barril Otero
 */
public interface Prediction {
    public String toString(Attribute target);
}