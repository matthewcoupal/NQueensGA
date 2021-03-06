/**
 * @author Matthew Coupal
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms
 * Due Date: 10/03/2016
 */
import java.util.Random;
import java.util.Arrays;
import java.awt.Point;
import java.lang.Comparable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;

public class NQueensGA {
  // Generator backend for the generalized random generator method.
  private static Random rand = new Random();

  private static final int BOARD_SIZE = 12;
  private static final double EPSILON = 0.0001;
  final static double MUTATION_CHANCE = .1;

  public static void main(String[] args) {
    // Set the default generation number.
    int generationNumber = 0;
    final int MAX_GENERATIONS = 1000;

    final double SOLVED_SOLUTION = 1/EPSILON;

    // Set to initial population size.
    int populationSize = BOARD_SIZE * 12;

    // Flag to set when a solution has been found.
    boolean foundSolution = false;

    // Place to store the solution that solves NQueens.
    Solution solved = null;

    // Initialize the population with random solutions.
    Solution[] generation = initialize(populationSize);

    while (!foundSolution && generationNumber < MAX_GENERATIONS) {
      for (Solution individual : generation) {

        // Determine the individual's fitness
        individual.setFitness(assess(individual));

        // Did the individual solve the NQueens problem?
        if(individual.getFitness() == SOLVED_SOLUTION) {
          foundSolution = true;
          solved = individual;
          System.out.println("Solution Found");
          System.out.println(individual + " " + generationNumber);
          break;
        }
      }
      if(!foundSolution) {
        // Parent Selection
        // Create the mating pool
        double poolSize = generation.length * .1;
        int finalPoolSize = (int) poolSize;
        // Make sure the size of the mating pool is even.
        if (finalPoolSize % 2 != 0) {
          finalPoolSize -= 1;
        }
        Solution[] matingPool = new Solution[ (int) finalPoolSize];

        // Populate the pool.
        int currentMember = 0;
        while (currentMember <= matingPool.length - 1) {
          Solution individualOne = generation[getRand(generation.length - 1)];
          Solution individualTwo = generation[getRand(generation.length - 1)];
          Solution individualThree = generation[getRand(generation.length - 1)];

          // Set winner
          Solution i = individualOne;
          if (individualTwo.getFitness() > i.getFitness()) {
            i = individualTwo;
          }
          if (individualThree.getFitness() > i.getFitness()) {
            i = individualThree;
          }
          // Add winner to the pool
          matingPool[currentMember] = i;
          currentMember += 1;
        }

        // Sort the mating pool for pairing purposes.
        Arrays.sort(matingPool);


        Solution[] newPopulation = new Solution[generation.length + matingPool.length];
        // Parent Pairing - The two best, the next two best...
        for (int i = 0; i < matingPool.length / 2; i++) {
          Solution[] children = crossover(matingPool[2 * i], matingPool[(2 * i) + 1]);
          children[0] = mutate(children[0]);
          children[1] = mutate(children[1]);

          // Calculate fitness before adding to the final array.
          children[0].setFitness(assess(children[0]));
          children[1].setFitness(assess(children[1]));
          // Copy all of the solutions to a new population array
          System.arraycopy(generation, 0, newPopulation, 0, generation.length);
          System.arraycopy(children, 0, newPopulation, generation.length + (2 * i), children.length);
        }

        // Fitness-biased survivor selection
        // Sort the population by fitness
        Arrays.sort(newPopulation);
        // Move only the top 90 percent (size of generation) to the next generation
        System.arraycopy(newPopulation, newPopulation.length - generation.length, generation, 0, generation.length);
      }
      // Increase Generation Count
      generationNumber += 1;
    }
    // Did we find a solution?
      try {
        PrintStream sout = new PrintStream(new FileOutputStream("out.txt", true));
        if(foundSolution) {
          // Output the solution to a file with the generation Number
          sout.println(generationNumber + " " + solved);
        } else {
          // Ouput that we could not find one.
          sout.println(generationNumber + " N/A");
        }
      } catch (FileNotFoundException e) {
        System.err.println(e);
      }
    }

  /**
   * Mutates an individual at random; 10% chance of mutation occurring.
   * @param  individual The solution to be mutated
   * @return            The possibly mutated individual.
   */
  private static Solution mutate(Solution individual) {
    int[] genotype = individual.getConfiguration();
    // Determine if the individual will mutate. 1 in 10 chance.
    int willMutate = getRand(10);
    if (willMutate <= MUTATION_CHANCE * 10 && willMutate != 0) {
      // Pick two pieces of the genotype and swap them.
      int value1 = getRand(genotype.length - 1);
      int value2 = getRand(genotype.length - 1);
      swap(genotype, value1, value2);
      individual.setConfiguration(genotype);
    }
    return individual;
  }

  /**
   * Performs genetic crossover of two parents.
   * @param parentA The first chosen parent to mate.
   * @param parentB The parent chosen to mate with the first chosen parent.
   * @return Two new children with characteristics of each parent.
   */
  private static Solution[] crossover(Solution parentA, Solution parentB) {
    // Grab the genes from the parents
    int[] aGenes = parentA.getConfiguration();
    int[] bGenes = parentB.getConfiguration();

    // Find the crossover point
    int crossoverPoint = getRand(aGenes.length - 1);
    // Create storage for the child genes
    int[] cGenes = new int[aGenes.length];
    int[] dGenes = new int[aGenes.length];
    // Place pre-crossover genes from parents a and b to children c and d respectively.
    for (int i = 0; i < crossoverPoint; i++) {
      cGenes[i] = aGenes[i];
      dGenes[i] = bGenes[i];
    }

    for (int i = crossoverPoint; i < aGenes.length; i++) {
      cGenes[i] = -1;
      dGenes[i] = -1;
    }

    // mark the gene insertion points starting from the crossover point.
    int currentC = crossoverPoint;
    int currentD = crossoverPoint;

    // Check to see which genes were already inserted and add the rest
    // child C
    for (int i = 0; i < BOARD_SIZE; i++) {
      boolean geneFound = false;
      for (int j = 0; j < cGenes.length; j++) {
        if (cGenes[j] == bGenes[i]) {
          geneFound = true;
        }
      }
      if (!geneFound) {
        cGenes[currentC] = bGenes[i];
        currentC += 1;
      }
      if (currentC == cGenes.length) {
        break;
      }
      if (geneFound) {
        continue;
      }
    }

    // child D
    for (int i = 0; i < BOARD_SIZE; i++) {
      boolean geneFound = false;
      for (int j = 0; j < dGenes.length; j++) {
        if (dGenes[j] == aGenes[i]) {
          geneFound = true;
        }
      }
      if (!geneFound) {
        dGenes[currentD] = aGenes[i];
        currentD += 1;
      }
      if (currentD == dGenes.length) {
        break;
      }
      if (geneFound) {
        continue;
      }
    }

    // Package the two configurations into separate solutions.
    Solution[] children = {new Solution(cGenes), new Solution(dGenes)};
    // Return both
    return children;
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
    }
    // Determine number of conflicts in the negative diagonal and add it to
    // the cumulative number of conflicts.
    conflicts += numberOfConflicts(normalQueensNeg);
    return 1/(conflicts + EPSILON);
  }

  /**
   * Determines the number of conflicts in a given configuration.
   * @param  normalizedPositions The normalized positions of the queens.
   * @return                     The number of conflicts.
   */
  private static int numberOfConflicts(Point[] normalizedPositions) {
    int conflicts = 0;
    for (int i = 0; i < normalizedPositions.length - 1; i++) {
      for (int j = i + 1; j < normalizedPositions.length; j++) {
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
class Solution implements Comparable<Solution> {
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
   * Compares two solutions by their fitness.
   * @param  otherSolution The solution to compare to.
   * @return               -1 if this is less fit; 1 if this is more fit; 0 if
   *                       they have the same fitness.
   */
  public int compareTo(Solution otherSolution) {
    // Less fit?
    if(this.fitness < otherSolution.getFitness()) {
      return -1;
    // More fit?
    } else if (this.fitness > otherSolution.getFitness()) {
      return 1;
    }
    return 0;
  }

  /**
   * Method to override the default toString method for debugging purposes.
   * @return String containing the configuration points.
   */
  public String toString() {
    String output = "<" + this.configuration[0];
    for (int i = 1; i < this.configuration.length; i++) {
      output += ", " + this.configuration[i];
    }
    output += ">";
    // Use this if you want to show the solutions fitness
    /* if (this.fitness > 0) {
      return output += " Fit: " + this.fitness;
    } */
    return output;
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
