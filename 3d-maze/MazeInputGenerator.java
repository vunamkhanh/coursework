import java.io.*;
import java.util.*;


// Randomly generate NxN squares where the zeros inside are counted towards clusters


public class MazeInputGenerator{
	
	public static void main(String[] args){
		
		MazeInputGenerator IG = new MazeInputGenerator();
		
		IG.generate();
		
	}
	
	
	// This method will generate 20 test cases, in which the last one will have a size of 50;
	// Each test case will be an NxN matrix, where the first and last row, the first and last column will always be one
	void generate(){
		
		try{
			
			PrintStream ps = new PrintStream("Input.txt");

			Random r = new Random();
			
			// Size of input to be generated
			int size = 0;
			
			// Probability for a zero to appear
			double prob = 0;
			
			// Temporary test for each matrix element
			double tmp = 0;
			
			// 20 test cases
			for (int i = 0; i < 20; i++){
				
				size = r.nextInt(9) + 2;
				if (i == 19){
					size = 10;
				}
				
				prob = Math.random();
				
				ps.println(size);
				for (int j = 0; j < size; j++){
					for (int k = 0; k < size; k++){
						for (int l = 0; l < size; l++){
						
							tmp = Math.random();
							
							// Print 1s on the border of the matrix
							if ((j == 0) && (k == 0) && (l == 0)){
								ps.print(0);
							}
							else if ((j == (size-1)) && (k == (size-1)) && (l == (size-1))){
								ps.print(0);
							}
							else if (tmp < prob){
								ps.print(0);
							}
							else{
								ps.print(1);
							}
						}
						ps.println();
					}
				}
				ps.println();
			}
			
			ps.close();
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
}