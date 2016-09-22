/**
 * @author Matthew Coupal
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms
 * Due Date: 10/03/2016
 */
import java.util.Random;
import java.util.Arrays;

public class NQueensGA {
  // Random generator backend for the generalized random generator method.
  private static Random rand = new Random();

  private static final int BOARD_SIZE = 3;

  public static void main(String[] args) {
    // Set the default generation number.
    int generationNumber = 0;
    final int MAX_GENERATIONS = 1000;

    // Set to initial population size.
    int populationSize = BOARD_SIZE * 10;

    // Flag to set when a solution has been found.
    boolean foundSolution = false;

    // Place to store the solution that solves NQueens.
    Solution solved = null;

    // Initialize the population with random solutions.
    Solution[] generation = initialize(populationSize);

    while (!foundSolution && generationNumber < MAX_GENERATIONS) {
      for (Solution individual : generation) {
        /*
        if individual has found the solution:
        1 set flag to true
        2 assign solution to solved
        3 break
        */
      }
      if(!foundSolution) {
        generationNumber += 1;
      }
    }
    if(foundSolution) {
      // Log the solution that found it;
    }
  }

  /**
   * Initializes the generation zero population.
   * @param  size The population size of generation zero.
   * @return      The entire generation.
   */
  private static Solution[] initialize(int size) {
    Solution[] population = new Solution[size];
    for (int i = 0; i < size; i++) {
      int[] genotype = new int[BOARD_SIZE];
      for (int j = 0; j < BOARD_SIZE; j++) {
        genotype[j] = j;
      }
      for (int j = 0; j < BOARD_SIZE; j++) {
        swap(genotype, getRand(BOARD_SIZE), getRand(BOARD_SIZE));
      }
      Solution individual = new Solution(genotype);
      population[i] = individual;
    }
    return population;
  }

  /**
   * Swaps two genes in the genotype.
   * @param  genotype The genotype of the solution.
   * @param  geneA The first gene to swap.
   * @param  geneB The second gene to swap.
   */
  private static void swap(int[] genotype, int geneA, int geneB) {
    int temp = genotype[geneA];
    genotype[geneA] = genotype[geneB];
    genotype[geneB] = temp;
  }

  /**
   * Generalized Random Generator
   * @param  maxValue The maximum value to be possibly generated
   * @return          An value between 0 and the max value;
   */
  private static int getRand(int maxValue) {
    float randInitial = rand.nextFloat();
    int randScaled = (int) (randInitial * maxValue);
    return randScaled;
  }
}

/**
 * Solution (not always THE best solution) to the NQueens problem
 */
class Solution {
  private int[] configuration;
  private float fitness;

  /**
   * Solution Constructor.
   * @param  configuration Positions of the queens.
   */
  public Solution(int[] configuration) {
    this.configuration = configuration;
  }

  /**
   * Method to override the default toString method for debugging purposes.
   * @return [description]
   */
  public String toString() {
    String output = "<" + this.configuration[0];
    for (int i = 1; i < this.configuration.length; i++) {
      output += ", " + this.configuration[i];
    }
    return output += ">";
  }

  /**
   * Mutator for the solution's fitness.
   * @param fitness The evaluated fitness of this solution.
   */
  public void setFitness(int fitness) {
    this.fitness = fitness;
  }

  /**
   * Mutator for the solution's fitness.
   * @return The fitness of the solution.
   */
  public float getFitness() {
    return this.fitness;
  }

  /**
   * Accessor for the solution's configuration.
   */
  public int[] getConfiguration() {
    return this.configuration;
  }

  /**
   * Mutator for the solution's configuration.
   * @param configuration the new configuration of the solution
   */
  public void setConfiguration(int[] configuration) {
    this.configuration = configuration;
  }

}
