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
		int g = 0;
		public State(ArrayList<Clause> cSet_ , int g_) {
			cSet = cSet_;
			g = g_;
		}

		public int getLastClauseSize() {
			return ((cSet.get(cSet.size()-1).neg.size()) + (cSet.get(cSet.size()-1).pos.size()));
		}
		
		public int getf() {
			return getLastClauseSize()*3 + g;
		}

	}


	public boolean Astar(State initialState) {
		// find frontier fra mulige actions
		// vælg en fra frontier og expand den
		// lav ny frontier

		ArrayList<State> expanded = new ArrayList<State>();

		PriorityQueue<State> frontier = new PriorityQueue<State>(99999, new Comparator<State>() {
			public int compare(State a, State b) {
				return a.getf()-b.getf();
			}
		});

		frontier.add(initialState);
		

		Clause tmpC;
		ArrayList<Clause> tmpcSet;
		State tmpState;
		State currentState;
		int expandedNodes = 0;
		while(true) { //ny fra frontier
			currentState = frontier.poll();
			frontier.remove(0);
			expandedNodes++;
			System.out.println("----------");
			System.out.println("g : " + currentState.g);
			for(Clause c : currentState.cSet) {
				for(Character chara : c.pos) {
					System.out.print(chara+", ");
				}
				System.out.print("   |   ");
				for(Character chara : c.neg) {
					System.out.print(chara+", ");
				}
				System.out.println();
			}
		
			for(int i = 0; i < currentState.cSet.size(); i++) { // kigge clauses igennem i forhold til hinanden
				for(int j = i; j < currentState.cSet.size(); j++) {
					tmpC = resolution(currentState.cSet.get(i),currentState.cSet.get(j));		//R(a,b)
					if(tmpC == null) { 															//else: R(b,a)
						tmpC = resolution(currentState.cSet.get(j),currentState.cSet.get(i));
						
					}

					
					if(tmpC != null && tmpC.pos.isEmpty() && tmpC.neg.isEmpty()) {				//tjek tom clause
						System.out.println("expanded nodes : "+expandedNodes);
						return true;
					} else if(tmpC != null) {													//else: add children
						boolean alreadyExists = false;
						for(int k = 0; k < currentState.cSet.size(); k++) {
							
							if(tmpC.pos.equals(currentState.cSet.get(k).pos) && tmpC.neg.equals(currentState.cSet.get(k).neg)) {
								alreadyExists = true;
								break;
							}
						}
						
						if(!alreadyExists) {
							
						
							tmpcSet = (ArrayList<Clause>) currentState.cSet.clone();
							tmpState = new State(tmpcSet,currentState.g + 1);
							tmpState.cSet.add(tmpC);
							
							boolean existsInExpanded = doesStateAlreadyExist(tmpState, expanded, frontier);
							
							
							if(!existsInExpanded){
								frontier.add(tmpState);
							}
													
						}
						
					}	
				}
			}
			
			if(frontier.isEmpty()){
				return false;
			}
			
			expanded.add(currentState);
			

		}


	}
	
	public boolean doesStateAlreadyExist(State tmpState, ArrayList<State> expanded, PriorityQueue<State> frontier) {
		for(State s : expanded){
			if(s.cSet.size() == tmpState.cSet.size()){
				for(int m = 0; m < s.cSet.size() ; m++){
					if(s.cSet.get(m).pos.size() == tmpState.cSet.get(m).pos.size() && s.cSet.get(m).neg.size() == tmpState.cSet.get(m).neg.size()){
						if(s.cSet.get(m).pos.equals( tmpState.cSet.get(m).pos )){
							return true;
						}
						if(s.cSet.get(m).neg.equals( tmpState.cSet.get(m).neg )){
							return true;
						}
					}
				}
			}
		}
		
		for(State s : frontier){
			if(s.cSet.size() == tmpState.cSet.size()){
				for(int m = 0; m < s.cSet.size() ; m++){
					if(s.cSet.get(m).pos.size() == tmpState.cSet.get(m).pos.size() && s.cSet.get(m).neg.size() == tmpState.cSet.get(m).neg.size()){
						if(s.cSet.get(m).pos.equals( tmpState.cSet.get(m).pos )){
							return true;
						}
						if(s.cSet.get(m).neg.equals( tmpState.cSet.get(m).neg )){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	

	public logic() {
		
		ArrayList<Clause> clauses = new ArrayList<Clause>();

		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('p','q')), new ArrayList<Character>())));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('p')), new ArrayList<Character>(Arrays.asList('q')))));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('r')), new ArrayList<Character>(Arrays.asList('p')))));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList('s')), new ArrayList<Character>(Arrays.asList('p')))));
		clauses.add((new Clause(new ArrayList<Character>(Arrays.asList()), new ArrayList<Character>(Arrays.asList('p','q','r')))));


		initial = new State(clauses,0);

	}
	

	public static void main(String args[]) {


		// initial state s0
		// Astar(s0) = bool

		
		logic l = new logic();
		
		System.out.println(l.Astar(l.initial));

	}


}


