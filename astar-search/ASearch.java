import java.io.*;
import java.util.*;

public class ASearch{
	
	public static void main(String[] args){
		
		ASearch A = new ASearch();
		
		A.perform();
		
	}
	
	void perform(){
		
		try{
			Scanner sc = new Scanner(new File("input.txt"));
			PrintStream ps = new PrintStream("output.txt");
			//PrintStream mat = new PrintStream("matrix.txt"); //test
			
			// Case number tracking
			int caseNo = 0;
			
			// Reading line by line
			String temp = null;
			String[] split = null;
			
			while (sc.hasNext()){
				caseNo++;
				
				// Graph size, start point and end point
				temp = sc.nextLine();
				split = temp.split(" ");
				int size = Integer.parseInt(split[0]);
				int start = Integer.parseInt(split[1]);
				int end = Integer.parseInt(split[2]);
				
				// Adjcency matrix representation of the graph
				int[][] graph = new int[size][size];
				
				// Air distance is the self-weight
				temp = sc.nextLine();
				split = temp.split(" ");
				for (int i = 0; i < split.length; i++){
					graph[i][i] = Integer.parseInt(split[i]);
				}
				
				// Weight of each directed link
				// Direction goes from i to j for int[i][j];
				temp = sc.nextLine();
				split = temp.split(" ");
				while (Integer.parseInt(split[0]) != -1){
					graph[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = Integer.parseInt(split[2]);
					temp = sc.nextLine();
					split = temp.split(" ");
				}
				
				//printMatrix(graph, mat); // test
				
				// Get the collection of shortest paths
				ArrayList<Node> paths = aStarSearch(graph, start, end);
				
				// Get the lexicographical shortest path
				Node lexiPath = sortPaths(paths);
				
				// Print out the path to a text file
				printPath(lexiPath, ps);
				
			}
			ps.close();
			//mat.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	// Finding the shortest path using the a* search algorithm
	ArrayList<Node> aStarSearch(int[][] graph, int start, int end){
		
		// List of paths to the output
		ArrayList<Node> paths = new ArrayList<Node>();
		
		// Queue of nodes to be evaluated, sorted by smallest paths
		// Each node contains the predecessor to the previous node in the path
		ArrayList<Node> queue = new ArrayList<Node>();
						
		// Start node
		Node begin = new Node(start, graph[start][start]);
		Node buffer = new Node(-1, -1);
		begin.setPredecessor(buffer);
		begin.setCost(0);
		
		// Once a node is visited, set its heuristic to negative
		boolean check = true;
		queue.add(begin);
		while (check){
			Node current = getPriority(queue); // Get path with highest priority
			
			// Check if current node is the destination
			if (current.getID() != end){
				
				for (int i = 0; i < graph[current.getID()].length; i++){
					if ((graph[current.getID()][i] > 0) && (current.getID() != i)){
						int temp_h = graph[i][i];
						int temp_g = current.getCost() + graph[current.getID()][i];
						Node temp_node = new Node(i, temp_h);
						temp_node.setCost(temp_g);
						temp_node.setPredecessor(current);
						queue.add(temp_node);
					}
				}
				
				queue.remove(current);
			}
			else {
				paths.add(current);
				queue.remove(current);
				
				// Test for paths with the same minimum length
				if (!queue.isEmpty()){
					while((getPriority(queue).getCost() == current.getCost())
						&& getPriority(queue).getID() == current.getID()){
						Node test = getPriority(queue);
						paths.add(test);
						queue.remove(test);
					}
				}
				
				check = false;
			}
		}
		
		return paths;
		
	}
	
	
	// Get the highest priority element of the queue
	Node getPriority(ArrayList<Node> queue){
		
		int min = Integer.MAX_VALUE;
		Node top = new Node(Integer.MAX_VALUE, 0);
		for (int i = 0; i < queue.size(); i++){
			if (queue.get(i).getTotal() < min){
				min = queue.get(i).getTotal();
				top = queue.get(i);
			}
			else if (queue.get(i).getTotal() == min){
				if (queue.get(i).getID() < top.getID()){
					top = queue.get(i);
				}
			}
		}
		
		return top;
	}
	
	
	// Get the lexicographically shortest path from a collection of shortest paths
	Node sortPaths(ArrayList<Node> paths){
		
		if (paths.size() > 1){
			Node result = paths.get(0);
			for (int i = 1; i < paths.size(); i++){
				result = twoPathsComparison(result, paths.get(i));
			}
			return result;
		}
		else{
			return paths.get(0);
		}
	}
	
	
	// Lexicographically compare two paths and output the prioritised one
	Node twoPathsComparison(Node path1, Node path2){
		
		// Get the first path
		Node current = path1;
		int id = current.getID();
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		while (id > 0){
			list1.add(id);
			current = current.getPredecessor();
			id = current.getID();
		}
		
		// Get the second path
		current = path2;
		id = current.getID();
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		while (id > 0){
			list2.add(id);
			current = current.getPredecessor();
			id = current.getID();
		}
		

		// Lexicographically compare the two paths
		Node result = path1;
		int length = list1.size();
		if (list2.size() < length){
			length = list2.size();
			result = path2;
		}
		
		for (int i = 0; i < length; i++){
			if (list1.get(length - 1 - i) < list2.get(length - 1 - i)){
				return path1;
			}
			else if (list1.get(length - 1 - i) > list2.get(length - 1 - i)){
				return path2;
			}
		}
		
		return result;
	}
	
	
	// Print out the matrix
	void printMatrix(int[][] graph, PrintStream ps){
		
		for(int i = 0; i < graph.length; i++){
			for (int j = 0; j < graph[0].length; j++){
				ps.print(graph[i][j] + " ");
			}
			ps.println();
		}
		ps.println();
		
	}
	
	
	// Print out an arraylist of integer to external text output
	void printPath(Node path, PrintStream ps){
		
		// Output the results
		ArrayList<Integer> result = new ArrayList<Integer>();
		Node currentNode = path;
		
		// Add length of the path
		result.add(currentNode.getTotal());
		
		// Retrace the path based on the predecessor of each node
		int nodeIndex = currentNode.getID();
		while (nodeIndex > 0){
			result.add(nodeIndex);
			currentNode = currentNode.getPredecessor();
			nodeIndex = currentNode.getID();
		}
				
		
		for (int i = 0; i < result.size(); i++){
			ps.print(result.get(result.size()-1-i) + " ");
		}
		ps.println();
		
	}
}