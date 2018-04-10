import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Heureka {
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	int c = 0;
	
	public class Node {
		int x,y;
		HashMap<Node, String> egdes = new HashMap<Node, String>();
		int g=0;
		int h=0;
		
		Node (int x_, int y_, HashMap<Node, String> map){
			x = x_;
			y = y_;
			egdes = map;
		}
		
		public int getf() {
			return g+h;
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
	
	public boolean Astar(ArrayList<Node> nodes, Node start, Node end) {
		//expandedNodes & frontier, lav frontier en queue på h+g
		ArrayList<Node> expanded = new ArrayList<Node>();
		
		start.g = 0;
		start.h = (int) Math.sqrt((start.x-end.x)^2 + (start.y-end.y)^2);
		
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(nodes.size(), new Comparator<Node>() {
			public int compare(Node a, Node b) {
				return (int) (a.getf()-b.getf());
			}
		});
		
		frontier.add(start);
		
		//ArrayList<Node> frontier = new ArrayList<Node>();
		
		//loop på frontier
		Node current;
		while(true) {
			//if empty failure		
			for(Node n : frontier) {
				System.out.print(n.x + ","+n.y +":"+ n.g+"+"+n.h+ "  ");
			}
			System.out.println();
			
			if(frontier.isEmpty()) {
				return false; //tilføj failure return
			}
			// remove n from frontier, add to expanded
			current = frontier.poll();
			expanded.add(current);
			
			// check if n = goal
			if(current==end) {
				System.out.println("goal: "+current.x + "," + current.y);
				System.out.println("expanded: "+ expanded);
				return true; //tilføj success return
			}
			
			// add children to frontier
			
			for(Node n : current.egdes.keySet()) {
				if(!expanded.contains(n)) {
					if(!frontier.contains(n)) {	
						n.g =  (int) (current.g + Math.sqrt(Math.pow((n.x-current.x),2) + Math.pow((n.y-current.y),2)));
						n.h =  (int) Math.sqrt(Math.pow(n.x-end.x,2) + Math.pow(n.y-end.y,2));
						frontier.add(n);
					} else {
						n.g = (int) Math.min(n.g,(int) (current.g + Math.sqrt(Math.pow((n.x-current.x),2) + Math.pow((n.y-current.y),2))));
					}
				
				}
			}
		}	
	}
	
	
	public static void main(String args[]) {
		Heureka h = new Heureka("./gader.txt");
		
		System.out.println(h.Astar(h.nodes, h.nodes.get(0), h.nodes.get(3)));
	}
	
	
}
