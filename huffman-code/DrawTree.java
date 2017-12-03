import java.io.*;
import java.util.*;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

public class DrawTree extends JPanel{
	
	int number;
	Node node;
	
	public void paintComponent(Graphics g){
		
		// Display current case
		String currentCase = String.valueOf(number);
		g.drawString("#" + currentCase, 80, 80);
		
		// List of nodes to traverse and their coordinates
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		
		// Set origin at x = 450, y = 150
		// Vertical difference: 50
		// Horizontal difference: 160, decreasing as the node is further away
		// Text verticality: 20 below previous, 20 above subsequent
		int orgX = 750;
		int orgY = 150;
		
		// Initialize
		queue.add(node);
		x.add(orgX);
		y.add(orgY);
		
		// Level Order
		while (!queue.isEmpty()){
			// Draw the node
			Node tmpNode = queue.get(0);
			int tmpX = x.get(0);
			int tmpY = y.get(0);
			g.drawString(tmpNode.getValue(), tmpX, tmpY);
			
			// Height modification to the tree
			double modDouble = 320.0/(Math.pow(2, (tmpY-orgY)/70));
			int mod = (int) modDouble;
			
			// Fill the queues
			if (tmpNode.getLeft() != null){
				queue.add(tmpNode.getLeft());
				x.add(tmpX - mod);
				y.add(tmpY + 70);
				g.drawLine(tmpX, tmpY + 10, tmpX - mod, tmpY + 50);
				g.drawString("0", tmpX - mod/2 - 5, tmpY + 30);
			}
			if (tmpNode.getRight() != null){
				queue.add(tmpNode.getRight());
				x.add(tmpX + mod);
				y.add(tmpY + 70);
				g.drawLine(tmpX, tmpY + 10, tmpX + mod, tmpY + 50);
				g.drawString("1", tmpX + mod/2 - 5, tmpY + 30);
			}
			
			// Remove current node
			queue.remove(0);
			x.remove(0);
			y.remove(0);
		}
		

		
	}
	
	public DrawTree(int caseNo, Node root){
		number = caseNo;
		node = root;
	}
}