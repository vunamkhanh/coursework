import java.io.*;
import java.util.*;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

public class Encoder extends JPanel{
	
	public static void main(String[] args){
		
		Encoder E = new Encoder();
		
		E.perform();		
		
	}
	
	// Perform the main methods
	void perform(){
		
		// Read the text
		ArrayList<String> text = readText();
		
		// Separate the text into cases
		ArrayList<ArrayList<String>> allCases = separateText(text);
		
		// Get the roots for each case
		ArrayList<Node> allRoots = new ArrayList<Node>();
		for (int i = 0; i < allCases.size(); i++){
			Node tmpRoot = getCode(allCases.get(i));
			allRoots.add(tmpRoot);
		}
		
		// Output the results
		try{
			
			PrintStream ps = new PrintStream("Output.txt");
			
			// Temporary node
			Node currentRoot;
			
			// Temporary text
			ArrayList<String> currentText = new ArrayList<String>();

			for (int i = 0; i < allRoots.size(); i++){
				
				// Print out the current case number
				ps.println("#" + (i+1));
				
				currentRoot = allRoots.get(i);
				currentText = allCases.get(i);
				
				// Output the coding scheme
				ArrayList<String> scheme = new ArrayList<String>();
				scheme = preorderPrint(scheme, currentRoot);
				int format = 0;
				for (int j = 0; j < scheme.size(); j++){
					ps.print(scheme.get(j));
					if (format == 0){
						ps.print("=");
						format = 1;
					} else{
						ps.print("; ");
						format = 0;
					}
				}
				ps.println();
				
				// Get the bit stream from the coding scheme
				String stream = new String();
				stream = bitStream(scheme, currentText);
				ps.print(stream);
				ps.println();
				ps.println();
				
				// Create the look up table using the coding scheme
				ArrayList<ArrayList<String>> lookUp = lookUpTable(scheme);
				
				// Convert the bit stream back to the text using the look up table
				// and print it out
				ArrayList<String> converted = new ArrayList<String>();
				converted = decode(lookUp, scheme, stream);
				for (int j = 0; j < converted.size(); j++){
					ps.print(converted.get(j));
				}
				ps.println();
				
				// Compare converted text and original text
				ps.println(compare(currentText, converted));
				ps.println();
				
				// Draw the Huffman tree
				drawTree(i+1, currentRoot);
			}
			
			ps.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	// Read the input as a String list
	ArrayList<String> readText(){
		
		ArrayList<String> text = new ArrayList<String>();
		
		try{
			Scanner sc = new Scanner(new File("Input.txt"));
			
			String temp = null;
			
			ArrayList<String> line = new ArrayList<String>();
			
			while (sc.hasNext()){
				temp = sc.nextLine();
				
				for (int i = 0; i < temp.length(); i++){
					text.add(Character.toString(temp.charAt(i)));
				}
				
				text.add("\n");
			}
			
			sc.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		return text;
	}

	// Separate the original input into cases
	ArrayList<ArrayList<String>> separateText(ArrayList<String> text){

		ArrayList<ArrayList<String>> allCases = new ArrayList<ArrayList<String>>();
		ArrayList<String> singleCase = new ArrayList<String>();
		int j = 3; // Start from the beginning of first case, not the hash
		while (j < text.size()){
			if (text.get(j).equals("#")){
				// Skip the hash, the case number and the line skip.
				j = j+3;
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < singleCase.size(); i++){
					temp.add(singleCase.get(i));
				}
				allCases.add(temp);
				singleCase.clear();
			}
			else{
				singleCase.add(text.get(j));
				j++;
			}
		}
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < singleCase.size(); i++){
			temp.add(singleCase.get(i));
		}
		allCases.add(temp);
		
		return allCases;
	}
	
	// Create a list of characters and their frequencies
	ArrayList<Node> getFreq(ArrayList<String> text){
		
		// String of unique characters
		ArrayList<String> unique = new ArrayList<String>();
		
		// String of frequencies corresponding to each character
		ArrayList<Integer> freq = new ArrayList<Integer>();
		
		// Counting index and value
		int ind = 0;
		int val = 0;
		
		// Add new characters to the list and increase their frequency count
		for (int i = 0; i < text.size(); i++){
			if (!unique.contains(text.get(i))){
				unique.add(text.get(i));
				freq.add(0);
			}
			
			// Set new frequency
			ind = unique.indexOf(text.get(i));
			val = freq.get(ind) + 1;
			freq.set(ind, val);	
		}
		
		// Node list
		ArrayList<Node> output = new ArrayList<Node>();
		for (int i = 0; i < unique.size(); i++){
			Node temp = new Node(freq.get(i), unique.get(i), null, null, null);
			output.add(temp);
		}
		
		return output;
	}

	// The the root of the Huffman tree for each case
	Node getCode(ArrayList<String> text){
		
		// Counting character frequencies
		ArrayList<Node> counted = getFreq(text);
		
		// Set up array for input into huffman class
		Node[] input = new Node[counted.size()*2];
		for (int i = 1; i <= counted.size(); i++){
			input[i] = counted.get(i-1);
		}
		Huffman construct = new Huffman(input, counted.size());
		construct.createHeap();
		Node root = construct.createTree();
		construct.preorder(root);
		
		return root;
	}

	// Print the coding scheme in increasing order using recursive preorder traversal
	ArrayList<String> preorderPrint(ArrayList<String> scheme, Node node){
		if (node.getLeft() != null){
			preorderPrint(scheme, node.getLeft());
		}
		if (node.getRight() != null){
			preorderPrint(scheme, node.getRight());
		}
		if (node.getValue().length() == 1){
			scheme.add(node.getValue());
			scheme.add(node.getCode().replaceAll("\\s",""));
		}
		return scheme;
	}
	
	// Getting the bit stream from the input text
	String bitStream(ArrayList<String> scheme, ArrayList<String> text){
		
		String result = new String();
		
		for (int i = 0; i < text.size(); i++){
			int ind = scheme.indexOf(text.get(i));
			result = result + scheme.get(ind+1);
		}
		
		return result;
	}
	
	// Create a look up table using the coding scheme
	ArrayList<ArrayList<String>> lookUpTable(ArrayList<String> scheme){
		
		// The entire table
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		
		// Number of characters
		int size = (int) scheme.size()/2;
		
		// Length of longest code
		int max = 0;
		for (int i = 0; i < scheme.size(); i++){
			if (max < scheme.get(i).length()) max = scheme.get(i).length();
		}

		// Create binary codes for the table, one list for each character
		for (int i = 0; i < size; i++){
			
			// Parameters of the current character
			int ind = i*2;
			
			// Extra bits to be added
			int extra = max - scheme.get(ind+1).length();
			
			// Number of entries for the current character
			int entries = (int) Math.pow(2, extra);
			
			// Code of the current character
			String code = scheme.get(ind+1);
			
			// Collection of table entries for one character
			ArrayList<String> collection = new ArrayList<String>();
			for (int j = 0; j < entries; j++){
				String entry = Integer.toBinaryString(j);
				while (entry.length() < extra){
					entry = "0" + entry;
				}
				// For maximum length cases
				if (extra > 0){
					collection.add(code + entry);
				} else{
					collection.add(code);
				}
			}
			
			table.add(collection);
		}
		
		return table;
	}

	// Decode the bit stream using the look up table
	ArrayList<String> decode(ArrayList<ArrayList<String>> table, ArrayList<String> scheme, String stream){
		
		ArrayList<String> solution = new ArrayList<String>();
		
		// Length of the buffer - all table entries have the same length
		int bufferSize = table.get(0).get(0).length();
		
		// Pointer to read through the bit stream
		int pointer = 0;
		
		// Stream length
		int entire = stream.length();
		
		// Read the whole stream till the end
		while (pointer < entire){
			// Preventing overflow
			String buffer = new String();
			if (pointer + bufferSize < entire){
				buffer = stream.substring(pointer, pointer + bufferSize);
			}
			else{
				buffer = stream.substring(pointer, entire);
				while (buffer.length() < bufferSize){
					buffer = buffer + "0";
				}
			}
			
			String word = new String();
			int codeLength = 0;
			
			outerloop:
			for (int i = 0; i < table.size(); i++){
				for (int j = 0; j < table.get(i).size(); j++){
					if (table.get(i).get(j).equals(buffer)){
						word = scheme.get(i*2);
						solution.add(word);
						codeLength = scheme.get(i*2+1).length();
						break outerloop;
					}
				}
			}

			pointer = pointer + codeLength;
			
		}
		
		return solution;
	}

	// Compare two string lists to see if they are equivalent
	boolean compare(ArrayList<String> text, ArrayList<String> decoded){
		
		if (text.size() != decoded.size()){
			return false;
		}
		
		for (int i = 0; i < text.size(); i++){
			if (!text.get(i).equals(decoded.get(i))){
				return false;
			}
		}
		
		return true;
	}

	// Draw a tree
	void drawTree(int caseNo, Node root){
		
		JFrame jFrame = new JFrame();
		jFrame.add(new DrawTree(caseNo, root));
		jFrame.setSize(1500, 1000);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}









