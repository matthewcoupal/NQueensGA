/**
 * @author Matthew Coupal
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms
 * Due Date: 10/03/2016
 */
import java.util.Random;

public class NQueensGA {
  private static Random rand = new Random();
  public static void main(String[] args) {
    // Put code here
  }

  /**
   * Generalized Random Generator
   * @param  maxValue The maximum value to be possibly generated
   * @return          An value between 0 and the max value;
   */
  public static int getRand(int maxValue) {
    float randInitial = rand.nextFloat();
    int randScaled = (int) (randInitial * maxValue);
    return randScaled;
  }
}

/**
 * Solution (not best solution) to the NQueens problem
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

}
