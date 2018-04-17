import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
public class logic {
	State  initial;
	
	public class Clause {
		ArrayList<Character> pos = new ArrayList<Character>();
		ArrayList<Character> neg = new ArrayList<Character>();

		Clause(ArrayList<Character> a_, ArrayList<Character> b_) {
			pos = a_;
			neg = b_;
		}

		public void removeRedundancy() {
			//fjern x, -x redundancy
			for(int h = 0; h < pos.size(); h++) {
				for(int k = 0; k < neg.size(); k++) {
					if(pos.get(h) == neg.get(k)) {
						pos.remove(h);
						neg.remove(k);
					}
				}
			}

			//fjern 'A V A' redundancy
			Set<Character> hs = new HashSet<>();
			hs.addAll(pos);
			pos.clear();
			pos.addAll(hs);

			hs.clear();
			hs.addAll(neg);
			neg.clear();
			neg.addAll(hs);



		}
	}

	public Clause resolution(Clause a, Clause b) {
		// sat op til kun at tjekke den ene vej 

		// find -x i a og b
		// find x i den anden
		// fjern x og -x og union a og b
		// fjern redundans



		for(int i = 0; i < a.neg.size(); i++) {
			//check om c giver noget godt
			for(int j = 0; j < b.pos.size(); j++) {
				if(a.neg.get(i) == b.pos.get(j)) {

					ArrayList<Character> tmpPos = new ArrayList<Character>();
					ArrayList<Character> tmpNeg = new ArrayList<Character>();

					tmpPos.addAll(b.pos);
					tmpPos.remove(j);
					tmpPos.addAll(a.pos);
					Collections.sort(tmpPos);

					tmpNeg.addAll(a.neg);
					tmpNeg.remove(i);
					tmpNeg.addAll(b.neg);
					Collections.sort(tmpNeg);

					Clause C = new Clause(tmpPos,tmpNeg);

					C.removeRedundancy();

					return C;
				}
			}
		}
		return null;
	}


	class State {
		ArrayList<Clause> cSet = new ArrayList<Clause>();
		int g;
		public State(ArrayList<Clause> cSet_ , int g_) {
			cSet = cSet_;
			g = g_;
		}

		public int getLastClauseSize() {
			return ((cSet.get(cSet.size()-1).neg.size()) + (cSet.get(cSet.size()-1).pos.size()));
		}

	}


	public boolean Astar(State initialState) {
		// find frontier fra mulige actions
		// vælg en fra frontier og expand den
		// lav ny frontier

		ArrayList<State> expanded = new ArrayList<State>();
		//ArrayList<State> frontier = new ArrayList<State>();

		PriorityQueue<State> frontier = new PriorityQueue<State>(99999, new Comparator<State>() {
			public int compare(State a, State b) {
				return a.getLastClauseSize() - b.getLastClauseSize() + a.g - b.g;
			}
		});

		frontier.add(initialState);

		Clause tmpC;
		State tmpState;
		State currentState;
		int g = 0;
		while(true) {
			currentState = frontier.poll();
			frontier.remove(0);
			g++;
			for(int i = 0; i < currentState.cSet.size(); i++) {
				for(int j = i; j < currentState.cSet.size(); j++) {
					
					tmpC = resolution(currentState.cSet.get(i),currentState.cSet.get(j));
					if(tmpC == null) {
						tmpC = resolution(currentState.cSet.get(j),currentState.cSet.get(i));

					}


					if(tmpC != null && tmpC.pos.isEmpty() && tmpC.neg.isEmpty()) {
						System.out.println("yay");
						return true;
					} else if(tmpC != null) {
						//for(Clause c : currentState.cSet) {
						for(int k = 0; k < currentState.cSet.size(); k++) {
							
							if(!tmpC.pos.equals(currentState.cSet.get(k).pos) || !tmpC.neg.equals(currentState.cSet.get(k).neg)) {
								
								tmpState = new State(currentState.cSet,g);
								tmpState.cSet.add(currentState.cSet.get(k));
								frontier.add(tmpState);
							}
						}
					}	
				}
			}

			expanded.add(currentState);

		}


	}

	public logic() {
		
		ArrayList<Clause> clauses = new ArrayList<Clause>();

		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('p','q')), new ArrayList<Character>())));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('p')), new ArrayList<Character>('q'))));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('r')), new ArrayList<Character>('p'))));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('s')), new ArrayList<Character>('p'))));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList()), new ArrayList<Character>(Arrays.asList('p','r','s')))));


		initial = new State(clauses,0);

	}
	

	public static void main(String args[]) {


		// initial state s0
		// Astar(s0) = bool

		
		logic l = new logic();
		
		System.out.println(l.Astar(l.initial));

	}


}


