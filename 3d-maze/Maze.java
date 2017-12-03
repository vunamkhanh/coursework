import java.io.*;
import java.util.*;

public class Maze{
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		Maze M = new Maze();
		
		M.perform();
		
	}
	
	
	// Store the test cases in 3D arrays, and perform maze traversion
	void perform() throws FileNotFoundException{
		
		Scanner sc = new Scanner(new File("Input.txt"));
		PrintStream ps = new PrintStream("Output.txt");
		
		// Test case number
		int caseNo = 0;
		
		try{
			
			while (sc.hasNext()){
				caseNo++;
				
				// Dimension of the cube NxNxN
				int size = sc.nextInt();
				
				// Storing the maze in an array
				int[][][] maze = new int[size][size][size];
				
				// Each test case has NxN lines, each
				// i is a layer, each j is a row, each k is a column
				for (int i = 0; i < size; i++){
					for (int j = 0; j < size; j++){
						String tmp = sc.next();
						for (int k = 0; k < tmp.length(); k++){
							maze[i][j][k] = Character.getNumericValue(tmp.charAt(k));
						}
					}
				}
				
				int shortPath = traverse(maze, size);
				
				// Output the result to a text file
				ps.println("#" + caseNo + " " + shortPath);
				ps.println();
			}
			sc.close();
			ps.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	// Traverse the 3D array and find the shortest path from one end to the other
	int traverse(int[][][] map, int size){
		
		// Initialize the maze
		int[][][] maze = map;
		
		// BFS queue to go the the end of the matrix
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		// Coordinates within the maze of each zero
		int i = 0;
		int j = 0;
		int k = 0;
		
		// End of the maze
		int end = getID(size-1, size-1, size-1, size);
		
		// Start from the top left corner of the upper layer
		path.add(0);
		int top = 0;
		maze[0][0][0] = 1;
		
		// For each node being considered, go through 6 directions
		// of traversal in this order: 
		// right, down, drop one layer, left, up, up one layer
		boolean check = true;
		while (check){
			// Get the coordinates of the top of the queue
			top = path.get(0);
			i = getCoord(top, size)[0];
			j = getCoord(top, size)[1];
			k = getCoord(top, size)[2];
			
			// Current distance from the root
			int dist = maze[i][j][k];
			
			// Traverse in six directions
			// Add the adjacent node to path if:
			// There is an adjacent node
			// The node is 0
			// The node is not already added
			// Add distance from the root
			
			// Traverse right
			if ((k != (size-1)) &&
				(maze[i][j][k+1] == 0)){
				
				path.add(getID(i,j,k+1,size));
				maze[i][j][k+1] = dist + 1;
			} 
			// Traverse down
			if ((j != (size-1)) &&
				(maze[i][j+1][k] == 0)){
				
				path.add(getID(i,j+1,k,size));
				maze[i][j+1][k] = dist + 1;
			}
			// Traverse down one layer
			if ((i != (size-1)) &&
				(maze[i+1][j][k] == 0)){
				
				path.add(getID(i+1,j,k,size));
				maze[i+1][j][k] = dist + 1;
			}
			// Traverse left
			if ((k != 0) &&
				(maze[i][j][k-1] == 0)){
				
				path.add(getID(i,j,k-1,size));
				maze[i][j][k-1] = dist + 1;
			}
			// Traverse up
			if ((j != 0) &&
				(maze[i][j-1][k] == 0)){
				
				path.add(getID(i,j-1,k,size));
				maze[i][j-1][k] = dist + 1;
			}
			// Traverse up one layer
			if ((i != 0) &&
				(maze[i-1][j][k] == 0)){
				
				path.add(getID(i-1,j,k,size));
				maze[i-1][j][k] = dist + 1;
			}
			
			// Remove the top of the queue
			path.remove(0);
			
			// Check if the end of the matrix is reached, or
			// if there is no possible path
			if ((path.size() == 0)){
				check = false;
			}
			else if (path.contains(end)){
				check = false;
			}
		}
		
		// If there is no path, distance is 0
		// If there is a path, distance is the
		// value of the destination node in the matrix
		return maze[size-1][size-1][size-1];
	}
	
	// Get the id from the coordinates of the node
	int getID(int i, int j, int k, int size){
		int id = i*size*size + j*size + k;
		return id;
	}
	
	// Get the node coordinates from the ID
	// Return the 3D coordinates as an array
	// i at [0]; j at [1]; k at [2]
	int[] getCoord(int id, int size){
		
		int[] coord = new int[3]; 
		
		coord[0] = (int) (id/(size*size));
		coord[1] = (int) ((id%(size*size))/size);
		coord[2] = id%size;
		
		return coord;
	}
	
	
}