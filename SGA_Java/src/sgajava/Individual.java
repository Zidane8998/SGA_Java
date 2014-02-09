package sgajava;

public class Individual{
	private int value;
	private double fitness;
	private int[] chromosome;
	
	public Individual(int chromLength){
		chromosome=new int[chromLength];
		value=0;
		fitness=0;
	}
	
	//accessors
	public int getValue(){
		return value;
	}
	public double getFitness(){
		return fitness;
	}
	public int[] getChromosome(){
		return chromosome;
	}
	public String toString(){
	   String str="";
		for (int i : chromosome){
			str=str+i;
		}
		return str;
	}
	//get chromosome at a specific position
	public int getIndivChrome(int index){
		return chromosome[index];
	}
	public void overwriteChrom(int[] newChrom){
		for (int i=0; i < chromosome.length; i++){
			chromosome[i]=newChrom[i];
		}
	}
	public void printChromosome(){
	   System.out.println();
		for (int i : chromosome){System.out.print(i);}
		
	}
	//mutators
	public void setValue(int val){
		value = val;
	}
	public void setFitness(double val){
		fitness = val;
	}
	public void setChromosome(int val, int position){
		chromosome[position] = val;
	}

}