package moa.classifiers.rules.streamantminer;

import java.util.Arrays;


import moa.classifiers.rules.streamantminer.Attribute.Condition;


public class Graph  {
	 /**
     * The index of the (final) end vertex.
     */
    public static final int END_INDEX = 1;
    
    /**
     * The index of the (initial) start vertex.
     */
    public static final int START_INDEX = 0;

    /**
     * The pheromone matrix.
     */
    protected Entry[][] matrix;

    /**
     * The vertices of the graph.
     */
    protected Vertex[] vertices;


    /**
     * Default constructor. Subclasses are responsible for initialising the
     * properties of the graph.
     */
    protected Graph() {
    }

    /**
     * Creates a new graph based on the characteristics of the specified
     * dataset.
     * 
     * @param dataset
     *            the current dataset.
     */
    //TODO Need to Fix this
    public Graph(Dataset dataset) {
    Attribute[] attributes = dataset.attributes();
	vertices = new Vertex[attributes.length + 1];
	// start and end virtual vertices
	vertices[START_INDEX] = new Vertex(null);
	vertices[END_INDEX] = new Vertex(null);

	int index = 2;

	for (int i = 0; i < (attributes.length - 1); i++) {
	    switch (attributes[i].getType()) {
	    case NOMINAL: {
		Vertex v =
			new Vertex(new Variable.Nominal(attributes[i].size()));
		v.attribute = i;

		vertices[index] = v;
		index++;

		break;
	    }

	    case CONTINUOUS: {
		Vertex v = new Vertex(new Variable.Continuous(attributes[i]
			.lower(), attributes[i].upper()));
		v.attribute = i;

		vertices[index] = v;
		index++;

		break;
	    }
	    }
	}

	// creates the pheromone matrix

	matrix = new Entry[vertices.length][vertices.length];

	for (int i = 0; i < matrix.length; i++) {
	    for (int j = 0; j < matrix[i].length; j++) {
		if (i == END_INDEX) {
		    matrix[i][j] = null;
		} else if (i == START_INDEX && j == END_INDEX) {
		    matrix[i][j] = null;
		} else if (i != j && j > 0) {
		    matrix[i][j] = new Entry();
		} else {
		    matrix[i][j] = null;
		}
	    }
	}

	matrix[START_INDEX][END_INDEX] = null;
    }
    /**
     * Returns the number of vertices of the graph.
     * 
     * @return the number of vertices of the graph.
     */
    public int size() {
	return vertices.length;
    }

    /**
     * Returns the structure representing the pheromone matrix.
     * 
     * @return the structure representing the pheromone matrix.
     */
    public Entry[][] matrix() {
	return matrix;
    }



    /**
     * Returns the index of the vertex represented by the attribute and value
     * indexes.
     * 
     * @param attribute
     *            the attribute index.
     * @param value
     *            the attribute value's index.
     * 
     * @return the index of the vertex.
     */
    public int indexOf(int attribute, int value) {
	for (int i = 0; i < vertices.length; i++) {
	    if (vertices[i].attribute == attribute) {
		if (vertices[i].condition == null) {
		    return i;
		} else if (vertices[i].condition.value[0] == value) {
		    return i;
		}
	    }
	}

	throw new IllegalArgumentException("Could not find vertex for attribute ["
		+ attribute + "] and value [" + value + "].");
    }

    /**
     * Decreases all pheromone values by the specified factor.
     * 
     * @param factor
     *            the evaporation factor.
     * @param level
     *            the maximum level used during evaporation.
     */
    public void evaporate(double factor, int level) {
	for (int i = 0; i < vertices.length; i++) {
	    Entry[] neighbours = matrix[i];

	    for (int j = 0; j < neighbours.length; j++) {
		if (neighbours[j] != null) {
		    Entry entry = neighbours[j];
		    int size = entry.size();

		    if (size < level) {
			entry.set(level - 1, entry.initial());
		    }

		    for (int k = 0; k < level; k++) {
			if (k < size) {
			    entry.set(k, entry.value(k) * factor);
			} else {
			    entry.set(k, entry.initial() * factor);
			}
		    }
		}
	    }
	}
    }

    /**
     * Returns the vertices of the graph.
     * 
     * @return the vertices of the graph.
     */
    public Vertex[] vertices() {
	return (Vertex[]) vertices;
    }

    /**
     * This (struct-like) class represents a vertex of the construction graph.
     * 
     * @author Fernando Esteban Barril Otero
     */
    public static class Vertex {
    	/**
    	 * The index of the attribute that this vertex represents.
    	 */
    	public int attribute;

    	/**
    	 * The attribute-value condition.
    	 */
    	public Condition condition;

    	
    	/**
	 * The attribute-value solution archive array to support multiple
	 * pheromone levels.
	 */
	public Variable[] archive;

	/**
	 * The variable to initialise each pheromone level. This is not updated
	 * throughout the run of the algorithm.
	 */
	public Variable initial;

	/**
	 * Default constructor.
	 */
	public Vertex(Variable initial) {
	    super();
	    archive = new Variable[0];
	    this.initial = initial;
	}

	/**
	 * Samples a new condition using the archive.
	 * 
	 * @param level the current archive level.
	 * 
	 * @return a new condition.
	 */
	public Condition condition(int level) {
	    Condition condition = null;

	    if (level < archive.length) {
		condition = archive[level].sample();
	    } else {
		condition = initial.sample();
	    }

	    condition.attribute = attribute;
	    return condition;
	}
	
	/**
	 * Updates the archive.
	 * 
	 * @param level the current archive level.
	 * @param condition the condition.
	 * @param quality the quality of the condition.
	 */
	public void update(int level, Condition condition, double quality) {
		 if (archive.length <= level) {
			archive = Arrays.copyOf(archive, level + 1);
			for(int i = level; i >= 0; i--){
				if(archive[i] == null)
					archive[i] = (Variable) initial.clone();
			}
		}


	    archive[level].add(condition, quality);
	
	}
    }
    
    /**
     * This (struct-like) class represents an entry in the pheromone matrix.
     * 
     * @author Fernando Esteban Barril Otero
     */
    public static class Entry implements Cloneable {
	/**
	 * The array of values. Each index correspond to a different level.
	 */
	private double[] values;

	/**
	 * The initial value.
	 */
	private double initial;

	/**
	 * Default constructor.
	 */
	public Entry() {
	    this(Double.NaN, new double[0]);
	}

	/**
	 * Creates a new entry.
	 * 
	 * @param initial
	 *            the initial value.
	 * @param values
	 *            the array of values.
	 */
	public Entry(double initial, double... values) {
	    this.initial = initial;
	    this.values = values;
	}

	/**
	 * Returns the initial value.
	 * 
	 * @return the initial value.
	 */
	public double initial() {
	    return initial;
	}

	/**
	 * Returns the value at the specified level. If there is no value at the
	 * specified level, returns the initial value.
	 * 
	 * @param level
	 *            the value level.
	 * 
	 * @return the value at the specified level.
	 */
	public double value(int level) {
	    if (level < values.length) {
		return values[level];
	    }

	    return initial;
	}

	/**
	 * Sets the initial value.
	 * 
	 * @param initial
	 *            the value to set.
	 */
	public void setInitial(double initial) {
	    this.initial = initial;
	}

	public void set(int level, double value) {
	    if (level < values.length) {
		values[level] = value;
	    } else {
		values = Arrays.copyOf(values, level + 1);
		values[level] = value;
	    }
	}

	public void setAll(double value) {
	    for (int i = 0; i < values.length; i++) {
		values[i] = value;
	    }
	}

	public int size() {
	    return values.length;
	}

	@Override
	public String toString() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<");

	    for (int i = 0; i < values.length; i++) {
		if (i > 0) {
		    buffer.append(",");
		}

		buffer.append(values[i]);
	    }

	    buffer.append(">");
	    return buffer.toString();
	}

	@Override
	public Entry clone() {
	    try {
		Entry clone = (Entry) super.clone();
		clone.values = values.clone();
		return clone;
	    } catch (CloneNotSupportedException exception) {
		throw new RuntimeException(exception);
	    }
	}

	/**
	 * Deep clones an arrays of objects.
	 * 
	 * @param array
	 *            the array to be clone.
	 * 
	 * @return a clone of the array.
	 */
	public static Entry[] deepClone(Entry[] array) {
	    Entry[] clone = new Entry[array.length];

	    for (int i = 0; i < clone.length; i++) {
		clone[i] = array[i].clone();
	    }

	    return clone;
	}

	/**
	 * Deep clones an arrays of objects.
	 * 
	 * @param array
	 *            the array to be clone.
	 * 
	 * @return a clone of the array.
	 */
	public static Entry[] initialise(Entry[] array) {
	    for (int i = 0; i < array.length; i++) {
		array[i] = new Entry(0.0, 0.0);
	    }

	    return array;
	}
    }
}