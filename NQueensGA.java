/**
 * @author Matthew Coupal
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms
 * Due Date: 10/03/2016
 */
import java.util.Random;
import java.util.Arrays;
import java.awt.Point;

public class NQueensGA {
  //doubleom generator backend for the generalized random generator method.
  private static Random rand = new Random();

  private static final int BOARD_SIZE = 12;
  private static final double EPSILON = 0.0001;

  public static void main(String[] args) {
    // Set the default generation number.
    int generationNumber = 0;
    final int MAX_GENERATIONS = 1;

    final double SOLVED_SOLUTION = 1/EPSILON;

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

        // Determine the individual's fitness
        System.out.println("Assess Individual " + individual);
        individual.setFitness(assess(individual));

        // Did the individual solve the NQueens problem?
        if(individual.getFitness() == SOLVED_SOLUTION) {
          foundSolution = true;
          solved = individual;
          System.out.println("Solution Found");
          break;
        }
      }
      if(!foundSolution) {
        // Parent Selection
        // Create the mating pool
        double poolSize = generation.length * .1;
        System.out.println(poolSize);
        Solution[] matingPool = new Solution[ (int) poolSize];
        System.out.println(matingPool.length);
        int currentMember = 0;
        while (currentMember <= matingPool.length - 1) {
          System.out.println(currentMember);
          Solution individualOne = generation[getRand(generation.length - 1)];
          Solution individualTwo = generation[getRand(generation.length - 1)];
          Solution individualThree = generation[getRand(generation.length - 1)];

          // Set winner WHY NOT MEETING POOL SIZE
          Solution i = individualOne;
          if (individualTwo.getFitness() > i.getFitness()) {
            i = individualTwo;
          }
          if (individualThree.getFitness() > i.getFitness()) {
            i = individualThree;
          }
          // System.out.println(currentMember);
          matingPool[currentMember] = i;
          currentMember += 1;
        }
        System.out.println(Arrays.toString(matingPool));
        generationNumber += 1;
      }
    }
    if(foundSolution) {
      // Log the solution that found it;
    }
  }

  /**
   * Assesses the fitness of a solution.
   * @param  individual The solution to be evaluated.
   * @return            The evalutated fitness of the solution.
   */
  private static double assess(Solution individual) {
    // Extract the genotype of the individual
    int [] genotype = individual.getConfiguration();
    // set the base number of conflicts possible
    int conflicts = 0;

    /*
    normalization in positive slope;
    |   |   |   | / | y
    |   |   | / |   |
    |   | / |   |   |
    | / |   |   |   |
    x
     */
    Point[] normalQueensPos = new Point[genotype.length];
    for (int i = 0; i < genotype.length; i++) {
      normalQueensPos[i] = normalize(i ,genotype[i], 1);
      // System.out.println("(" + i + ", " + genotype[i] + "->" + normalQueensPos[i]);
    }
    // determine number of conflicts in the positive diagonal slope
    conflicts += numberOfConflicts(normalQueensPos);

    // Can't condense into using single Point array due to the method being
    // static.
    Point[] normalQueensNeg = new Point[genotype.length];

    /*
    normalization in negative slope;
    | \ |   |   |   | y
    |   | \ |   |   |
    |   |   | \ |   |
    |   |   |   | \ |
    x
     */
    for (int i = 0; i < genotype.length; i++) {
      normalQueensNeg[i] = normalize(i, genotype[i], -1);
      // System.out.println("(" + i + ", " + genotype[i] + "->" + normalQueensNeg[i]);
    }
    // Determine number of conflicts in the negative diagonal and add it to
    // the cumulative number of conflicts.
    conflicts += numberOfConflicts(normalQueensNeg);
    // System.out.println(conflicts);
    // System.out.println(Arrays.toString(normalQueens));
    return 1/(conflicts + EPSILON);
  }

  /**
   * Determines the number of conflicts in a given configuration.
   * @param  normalizedPositions The normalized positions of the queens.
   * @return                     The number of conflicts.
   */
  private static int numberOfConflicts(Point[] normalizedPositions) {
    int conflicts = 0;
    // System.out.println(Arrays.toString(normalizedPositions));
    for (int i = 0; i < normalizedPositions.length - 1; i++) {
      for (int j = i + 1; j < normalizedPositions.length; j++) {
        // System.out.println("Compare " + i + " to " + j);
        if ((normalizedPositions[i].x == normalizedPositions[j].x) &&
            (normalizedPositions[i].y == normalizedPositions[j].y)) {
          conflicts += 1;
        }
      }
    }
    return conflicts;
  }

  /**
   * Takes a point on the board and normalizes the vector based on direction.
   * @param   x The x component of the vector.
   * @param   y The y component of the vector.
   * @param   slope The slope of the vector (1 = positive; -1 = negative)
   * @return  The normalized vector.
   */
  private static Point normalize(int x, int y, int slope) {
    if (slope == 1) {
      if ((x == BOARD_SIZE - 1) || (y == BOARD_SIZE - 1)) {
        return new Point(x, y);
      }
      return normalize(x + 1, y + 1, slope);
    } else if (slope == -1) {
      if ((x == BOARD_SIZE - 1) || (y == 0)) {
        return new Point(x, y);
      }
      return normalize(x + 1, y - 1, slope);
    }
    System.err.println("Not a good slope");
    System.exit(1);
    return null;
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
  private double fitness;

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
  public void setFitness(double fitness) {
    this.fitness = fitness;
  }

  /**
   * Mutator for the solution's fitness.
   * @return The fitness of the solution.
   */
  public double getFitness() {
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
