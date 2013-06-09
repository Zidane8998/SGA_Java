import java.util.Random;
import java.lang.Math;

public class SGA_Java_Main {

	private static int POPULATION_SIZE=30;    // population size - number of strings
	private static int CHROM_LENGTH=16;       // binary string length of each individual
	private static double PMUT=0.05;          // probability of flipping each bit
	private static int MAX_GEN=30;            // GA stops after this many generations
	private static int GEN_REP=10;             // report is generated at these intervals
	private static int ELITE=1;               // 1=elitism,  0=no elitism
	private static int MAXMIN=-1;             // -1=minimize, 1=maximize
	public static Random rand;
	static Individual beststring = new Individual(CHROM_LENGTH);
	static Individual verybest = new Individual(CHROM_LENGTH);
	static int[] selected = new int[POPULATION_SIZE];
	public static void main (String args[]){
		int curGen=0;
		rand=new Random();
		if (MAXMIN==-1)verybest.setFitness(999999);
		else verybest.setFitness(-999999);
		//create and initialize population to random vals(only once)
		Individual[] population=init_pop();
		//give population their initial values and fitness
		for (Individual i : population){
			i.setValue(decode(i));
			i.setFitness(evaluate(i));
		}
		//
		//run statistics (print out)
		
		//main loop
		while (curGen < MAX_GEN){
		   //get previous best
			getPreviousBest(population);
			//3-2 torunament selection
			selection(population);
			//1-pt crossover
			for (int i=0; i < POPULATION_SIZE; i=i+2){
		   	crossover(selected[i], selected[i+1], population);
			}
			//mutation
		   mutation(population);
			//evaluation
	 	   for (Individual i : population){
				i.setValue(decode(i));
				i.setFitness(evaluate(i));
			}
			//elitism
			if (ELITE==1)elite(population);
			if (curGen%GEN_REP==0)statistics(population, selected, curGen);
		curGen++;
		}
		//final report
		finalReport(population);
	}
	
	//initialize individual strings with starting random values
	private static Individual init_indiv(){
		Individual indiv=new Individual(CHROM_LENGTH);
		for (int i=0; i < CHROM_LENGTH; i++){
		   if (rand.nextDouble()>=0.5)indiv.setChromosome(0, i);
			else indiv.setChromosome(1, i);
		}
	return indiv;
	}
	
	//initialize entire population
	private static Individual[] init_pop(){
	   //create new array of type Individual to hold population
		Individual population[]=new Individual[POPULATION_SIZE];
		for (int i=0; i < POPULATION_SIZE; i++){
			population[i]=init_indiv();
		}
	return population;
	}
	
	//determine and display best string from previous generation
	//maintain best string seen so far, overwrite if better
	private static void getPreviousBest(Individual[] population){
		//iterate through population pool and evaluate the best
		for (Individual i : population){
		   //if the current individual in the old pool is better than the best, set best to that indiv
			if ( (MAXMIN*i.getFitness()) > (MAXMIN*beststring.getFitness()) ){
				beststring.setValue(i.getValue());
				beststring.overwriteChrom(i.getChromosome());
				beststring.setFitness(i.getFitness());
			} 
		}
		
		//if current best is better than the global best, overwrite global best
		if ( (MAXMIN*beststring.getFitness()) > (MAXMIN*verybest.getFitness() ) ){
			verybest.setValue( beststring.getValue() );
			verybest.overwriteChrom(beststring.getChromosome());
			verybest.setFitness(beststring.getFitness());
		}
		
	}
	
	//convert chromosome bitstring to positive integer value
	private static int decode(Individual indiv){
		int value=0;
		//for each bit in the chromosome, determine its decimal value and sum
		for (int i=0; i < CHROM_LENGTH; i++){
			 value += (int) Math.pow( 2.0,(double)i ) * indiv.getIndivChrome( CHROM_LENGTH-1-i );
		}
		return value;
	}
	
	//evaluate the string according to the input function (problem-specific)
	private static double evaluate(Individual indiv){
	   //get value of Individual
	   int value = indiv.getValue();
	   //convert to current range of the problem
		double convDec=convRange(value);
		//return the value of the input function for the individual's converted binary number
		double ans = (double) ( (0.1*Math.abs(convDec) ) - Math.sin(convDec) );
		
	return ans;
	}
	
	//convert the decimal value to desired floating point range
	private static double convRange(int raw){
   	double outval = ((((double)raw)/65535.0)*120.0)-60.0;
   return outval;
   }
	
	//"flip a coin" to randomize chromosomes
	private static int coinFlip(double prob){
		double rand2 = rand.nextDouble();
		if (rand2 < prob)return 0;
		else return 1;
	}
	
	//3-2 tournament selection, creates an index array of selected individuals (selected[])
	private static void selection(Individual[] population){
		for (int i=0; i<POPULATION_SIZE; i+=2){
    		int r = (int) (rand.nextDouble()*POPULATION_SIZE);
    		int s = (int) (rand.nextDouble()*POPULATION_SIZE);
    		int t = (int) (rand.nextDouble()*POPULATION_SIZE);
			
			if ( ((MAXMIN*population[r].getFitness()) >= (MAXMIN*population[s].getFitness())) || ((MAXMIN*population[r].getFitness()) >= (MAXMIN*population[t].getFitness()))){
      		if ((MAXMIN*population[s].getFitness()) > (MAXMIN*population[t].getFitness())){ 
					selected[i] = r; 
					selected[i+1] = s; 
				}
      		else{ 
					selected[i] = r; 
					selected[i+1] = t; 
				}
   	 	}
    		else{
      		if ( ((MAXMIN*population[s].getFitness()) >= (MAXMIN*population[r].getFitness())) || ((MAXMIN*population[s].getFitness()) >= (MAXMIN*population[t].getFitness()))){
        			if ((MAXMIN*population[r].getFitness()) > (MAXMIN*population[t].getFitness())){ 
						selected[i] = s; 
						selected[i+1] = r; 
					}
        			else{
						selected[i] = s; 
						selected[i+1] = t; 
					}
      		}
      		else{
      			if ( ((MAXMIN*population[t].getFitness()) >= (MAXMIN*population[r].getFitness())) || ((MAXMIN*population[t].getFitness()) >= (MAXMIN*population[s].getFitness()))){
        				if ((MAXMIN*population[r].getFitness()) > (MAXMIN*population[s].getFitness())){
							selected[i] = t; 
							selected[i+1] = r; 
						}
          			else{
							selected[i] = t; 
							selected[i+1] = s;
						}
					} 
				} 
			} 
		}
	}
	//mutation mechanism
	private static void mutation(Individual[] population){
	   //for each individual in the population
		for (int i=0; i < population.length; i++){
			Individual indiv=population[i];
			//for each chromosome of that individual
			for (int j=0; j < CHROM_LENGTH; j++){
			   //flip any bits that are determined to be mutated
				if (coinFlip(PMUT)==0){
					if (indiv.getIndivChrome(j)==1)indiv.setChromosome(0, j);
					else indiv.setChromosome(0, j);
				}
			}
		}
	}
	
	//crossover two individuals, creating 2 new children with their genes using 1-pt x-over.
	//this replaces the parents in the gene pool of selected individuals
	private static void crossover (int p1, int p2, Individual[] population){
		Individual parent1=population[p1];
		Individual parent2=population[p2];
		//create two new Individuals (genes to be modified)
		Individual child1 = new Individual(CHROM_LENGTH);
		Individual child2 = new Individual(CHROM_LENGTH);
		//choose a location for 1-pt crossover
		int site = (int)(rand.nextDouble()*CHROM_LENGTH);
		//crossover the parents into two new children using the gene location
		for (int i=0; i < CHROM_LENGTH; i++){
		   int curParent1Chrome=parent1.getIndivChrome(i);
			int curParent2Chrome=parent2.getIndivChrome(i);
			if ( (i<=site) || (site==0) ){
				child1.setChromosome(curParent1Chrome, i);
				child2.setChromosome(curParent2Chrome, i);
			}
			else{
				child1.setChromosome(curParent2Chrome, i);
				child2.setChromosome(curParent1Chrome, i);
			}
		}
		//replace the parents with the children, over-writing the parent's chromosome with children
		//this is faster than finding the parents in population[], deleting and adding to list
		for (int i=0; i < CHROM_LENGTH; i++){
			int curChild1Chrome=child1.getIndivChrome(i);
			int curChild2Chrome=child2.getIndivChrome(i);
			parent1.setChromosome(curChild1Chrome, i);
			parent2.setChromosome(curChild2Chrome, i);
		}
	}
	
	//elitism mechanism - copies best so far into population (overwriting 0th member of population[])
	private static void elite(Individual[] population){
		if ((MAXMIN*beststring.getFitness()) > (MAXMIN*evaluate(population[0]))){
    		population[0].setFitness(beststring.getFitness());
   		population[0].setValue(beststring.getValue());
    		population[0].overwriteChrom(beststring.getChromosome());
  		}
	}
	
	private static void statistics(Individual[] population, int[] selected, int curGen){
	   System.out.printf("\n\nGeneration: %d\nSelected Strings: ", curGen);
		for (int s : selected){population[s].printChromosome();}
		System.out.println("\n");
		System.out.println("         x          f(x)            new_str               X");
		for (Individual i : population){
			System.out.printf("\n   ");
			System.out.printf("%f\t%f\t", convRange(i.getValue()), i.getFitness());
			for (int j=0; j < CHROM_LENGTH; j++){
				System.out.print(i.getIndivChrome(j));
			}
			System.out.printf("\t%f", convRange(decode(i)));
		}
		System.out.println("\n\nBest string\n------------");
		for (int i=0; i<CHROM_LENGTH; i++){
			System.out.print(beststring.getIndivChrome(i));
		}
		System.out.println("\nValue: " + convRange(beststring.getValue()));
		System.out.println("Fitness: " + beststring.getFitness());
	}
	private static void finalReport(Individual population[]){
		System.out.println("=======================================================");
		System.out.println("Best result of all generations:\n");
		for (int i : verybest.getChromosome()){
			System.out.print(i);
		}
		System.out.println("\nDecoded value: " + convRange(verybest.getValue())+"\n");
		System.out.println("Fitness: " + verybest.getFitness());
	}
}