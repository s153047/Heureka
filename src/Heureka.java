import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Heureka {
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	
	public class Node {
		int x,y;
		HashMap<Node, String> egdes = new HashMap<Node, String>();
		
		Node (int x_, int y_, HashMap<Node, String> e_){
			x = x_;
			y = y_;
			egdes = e_;
		}
		
	}
	
	public Heureka(String file) {
		Scanner input = new Scanner(file);
		System.out.println(input.next());
		
		int x1 = Integer.parseInt(input.next());
		int y1 = Integer.parseInt(input.next());
		String name = input.next();
		int x2 = Integer.parseInt(input.next());
		int y2 = Integer.parseInt(input.next());
		
		Node current1 = new Node(x1,y1, null);
		nodes.add(current1);
		Node current2 = new Node(x2,y2, null);
		nodes.add(current2);
		
		current1.egdes.put(current2, name);
		
		int x1Current;
		int y1Current;
		int x2Current;
		int y2Current;
		while(input.hasNext()) {
			current1 = null;
			current2 = null;			
			
			x1Current = Integer.parseInt(input.next());
			y1Current = Integer.parseInt(input.next());
			name = input.next();
			x2Current = Integer.parseInt(input.next());
			y2Current = Integer.parseInt(input.next());
			
			for(Node n : nodes) {
				if(n.x == x1Current && n.y == y1Current) {
					// den findes
					current1 = n;
				}
	
			}
			for(Node n : nodes) {
				if(n.x == x2Current && n.y == y2Current) {
					// den findes
					current2 = n;
				}
	
			}
			if(current1 == null) {
				current1 = new Node(x1Current,y1Current,null);
				nodes.add(current1);
			}
			
			if(current2 == null) {
				current2 = new Node(x2Current,y2Current,null);
				nodes.add(current2);
			}
			
			current1.egdes.put(current2, name);
			
		}
		


		
	}
	
	public static void main(String args[]) {
		Heureka h = new Heureka(args[0]);
		System.out.println(h.nodes.size());
	}
	
	
}
