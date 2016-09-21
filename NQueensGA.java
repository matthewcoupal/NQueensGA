/**
 * @author Matthew Coupal
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms
 * Due Date: 10/03/2016
 */

public class NQueensGA {
  public static void main(String[] args) {
    // Put code here
  }
}

/**
 * Solution (not best solution) to the NQueens problem
 */
public class Solution {
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
  public getFitness() {
    return this.fitness;
  }

  /**
   * Accessor for the solution's configuration.
   */
  public int[] getConfiguration() {
    return this.configuration;
  }

}
