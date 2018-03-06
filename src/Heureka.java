import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Heureka {
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	int c = 0;
	
	public class Node {
		int x,y;
		HashMap<Node, String> egdes = new HashMap<Node, String>();
		
		Node (int x_, int y_, HashMap<Node, String> map){
			x = x_;
			y = y_;
			egdes = map;
		}
		
	}
	
	public Heureka(String file) {
		try {
			Scanner input = new Scanner(new File(file), "UTF-8");

			
			int x1 = Integer.parseInt(input.next());
			int y1 = Integer.parseInt(input.next());
			String name = input.next();
			int x2 = Integer.parseInt(input.next());
			int y2 = Integer.parseInt(input.next());
			
			Node current1 = new Node(x1,y1, new HashMap<Node,String>());
			nodes.add(current1);
			Node current2 = new Node(x2,y2, new HashMap<Node,String>());
			nodes.add(current2);
			
			current1.egdes.put(current2, name);
			
			c++;
			
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
					current1 = new Node(x1Current,y1Current,new HashMap<Node,String>());
					nodes.add(current1);
				}
				
				if(current2 == null) {
					current2 = new Node(x2Current,y2Current,new HashMap<Node,String>());
					nodes.add(current2);
				}
				
				current1.egdes.put(current2, name);
				c++;
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error loading in file " + file);
		}
		


		
	}
	
	public static void main(String args[]) {
		Heureka h = new Heureka("./gader.txt");
		System.out.println(h.nodes.size()+" / " + h.c);
	}
	
	
}
