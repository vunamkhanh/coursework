// Class Node for use within the A* search algorithm
import java.io.*;
import java.util.*;

public class Node{
	
	private int index;
	private Node predecessor;
	
	private int g; // Cost from start node
	private int h; // Heuristic
	
	// Constructor
	public Node(int ind, int heuristic){
		index = ind;
		predecessor = null;
		h = heuristic;
		g = Integer.MAX_VALUE;
	}
	
	// Getters
	public int getID() { return index; }
	public Node getPredecessor() { return predecessor; }
	public int getCost() { return g; }
	public int getHeuristic() { return h; }
	public int getTotal() { return g+h; }
	
	// Setters
	public void setCost(int cost) { g = cost; }
	public void setPredecessor(Node pre) { predecessor = pre; }
}