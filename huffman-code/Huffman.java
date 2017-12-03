// Huffman Node implementation

public class Huffman{
	
	// Use Binary Heap to store the Huffman tree
	private Node[] a;
	private	int N;
	
	public Huffman(Node[] harray, int initialSize){
		a = harray;
		N = initialSize;
	}
	
	public int size() { return N; }
	
	// Compare two frequencies
	private boolean greater(int i, int j){
		return a[i].getKey()>a[j].getKey();
	}
	
	// Swap two keys
	private void swap(int i, int j) {
		Node temp = a[i];	
		a[i] = a[j];	
		a[j] = temp;
	}
	
	private void downheap(int i){
		while (2*i <= N) {
			int k = 2*i;
			if (k < N && greater(k, k+1)) k++;
			if (!greater(i, k)) break;
			swap(i, k);
			i = k;
		}
	}
	
	private void upheap(int j){
		while (j > 1 && greater(j/2, j)){
			swap(j/2, j);
			j = j/2;
		}
	}
	
	public void createHeap(){
		for(int i = N/2; i > 0; i--){
			downheap(i);
		}
	}
	
	// Insert new key into the binary heap
	public void insert(Node temp){
		a[++N] = temp;
		upheap(N);
	}
	
	public Node deleteMin() {
		Node min = a[1];
		swap(1, N--);
		a[N+1] = null;
		downheap(1);
		return min;
	}
	
	public Node createTree(){
		while(size() > 1){
			Node e1 = deleteMin();
			Node e2 = deleteMin();
			Node temp = new Node (e1.getKey() + e2.getKey(),
									e1.getValue() + e2.getValue(),
									e1, e2, " ");
			insert(temp);
		}
		return deleteMin();
	}
	
	// Generating the tree in preorder
	public void preorder(Node node){
		if (node.getLeft() != null){
			node.getLeft().setCode(node.getCode()+"0");
			preorder(node.getLeft());
		}
		if (node.getRight() != null){
			node.getRight().setCode(node.getCode()+"1");
			preorder(node.getRight());
		}
		/*if (node.getValue().length() == 1){
			System.out.print(node.getValue() + ":" + node.getCode() + " ");
		}*/
	}
}