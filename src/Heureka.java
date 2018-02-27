import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Heureka {
	
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
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		int x1 = Integer.parseInt(input.next());
		int y1 = Integer.parseInt(input.next());
		String name = input.next();
		int x2 = Integer.parseInt(input.next());
		int y2 = Integer.parseInt(input.next());
		
		
		
		Node start1 = new Node(x1,y1, null);
		nodes.add(start1);
		Node start2 = new Node(x2,y2, null);
		nodes.add(start2);
		
		start1.egdes.put(start2, name);
		
		int xCurrent;
		int yCurrent;
		while(true) {
			xCurrent = Integer.parseInt(input.next());
			yCurrent = Integer.parseInt(input.next());
			
			for(Node n : nodes) {
				if(n.x == xCurrent && n.y == yCurrent) {
					// den findes
					break;
				}
				
			}
			
			
			
			
			
			
		}
		
	}
	
	public static void main(String args[]) {
		Heureka h = new Heureka(args[0]);
	}
	
	
}
