
// Node class for Huffman code


public class Node{
	
	// Variables related to a character
	private	int		frequency;
	private	String	word;
	private Node	left;
	private	Node	right;
	private	String	code;
	
	// Constructor
	public Node(int newFreq, String newValue, Node l, Node r, String s){
		frequency = newFreq;
		word = newValue;
		left = l;
		right = r;
		code = s;
	}
	
	// Get values related to the character
	public 	int		getKey()	{ return frequency; }
	public 	String	getValue()	{ return word; }
	public	String	getCode()	{ return code; }
	public 	Node	getLeft()	{ return left; }
	public	Node	getRight()	{ return right; }
	
	public 	void	setCode(String newCode) { code = newCode; }
	
}