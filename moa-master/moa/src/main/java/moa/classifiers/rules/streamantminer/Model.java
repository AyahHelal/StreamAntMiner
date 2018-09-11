package moa.classifiers.rules.streamantminer;


public interface Model {
    /**
     * Returns the predicted class value of the specified instance.
     * 
     * @param dataset
     *            the current dataset.
     * @param instance
     *            the instance index.
     * 
     * @return the predicted class value of the specified instance.
     */
    public Prediction predict(Dataset dataset, int instance);

    /**
     * Returns the string representation of the model. The dataset might be used
     * to retrieve the attributes' metadata.
     * 
     * @param dataset
     *            the current dataset.
     * 
     * @return the string representation of the model.
     */
    public String toString(Dataset dataset);

    /**
     * Returns the string representation of the model suitable to export to a
     * file. The dataset might be used to retrieve the attributes' metadata.
     * 
     * @param dataset
     *            the current dataset.
     * 
     * @return the string representation of the model.
     */
    public String export(Dataset dataset);
}