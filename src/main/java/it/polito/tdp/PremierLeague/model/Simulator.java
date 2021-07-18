package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	
	static Random random;
	
	
	//Eventi
	private PriorityQueue<Match> queue;
			
	
	
	//Parametri di simulazione
	private int N;  //numero di reporter iniziali per squadra
	private int X;  //soglia di criticità per reporter totali
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	//Misure in uscita
	List<Integer> numReporterPartita = new ArrayList<>();    //ogni volta che si passa da un match aggiungere qui
	
	//impostazione paramentri iniziali
	public void setNumReporter(Integer N) {
		this.N = N;
	}
	
	public void setSoglia (Integer X) {
		this.X = X;
	}
	
	
	public void run(List<Match> matches, Graph<Team, DefaultWeightedEdge> grafo, Map<Integer, Team> idMap) {
		
		Collections.sort(matches);
		this.queue = new PriorityQueue<>(matches);//CONTROLLA CHE FUNZIONI
		 
		this.grafo = grafo;
		Set<Team> teams = this.grafo.vertexSet();
		for(Team t : teams) {
			t.setNumReporter(N);
		}
		
		while(!this.queue.isEmpty()) {
			Match match = this.queue.poll();
			System.out.println(match);
			processEvent(match, idMap);
			
		}
	}

	private void processEvent(Match match, Map<Integer, Team> idMap) {
		Team vincitrice = null;
		Team perdente = null;
		numReporterPartita.add(idMap.get(match.getTeamHomeID()).getNumReporter() + idMap.get(match.getTeamAwayID()).getNumReporter());
		if(match.getResultOfTeamHome() == 1) {
			vincitrice = idMap.get(match.getTeamHomeID());
			perdente = idMap.get(match.getTeamAwayID());
		}
		else if(match.getResultOfTeamHome() == -1){
			perdente = idMap.get(match.getTeamHomeID());
			vincitrice = idMap.get(match.getTeamAwayID());
		}
		
		
		switch (match.getResultOfTeamHome()) {
		case 0:
			//in caso di pareggio non succede nulla
			break;
			
			
		default:  
			//se c'è una vittoria da parte di una delle due squadre
			
			
			if(Graphs.vertexHasPredecessors(this.grafo, vincitrice) && 
					vincitrice.getNumReporter() >0){
				//la squadra vincente ha il 50% di possibilità di promuovere UNO dei reporter
				Double perc = Math.random();
				if(perc < 0.5) {
					//promuovo  --> vincitrice perde uno
					//              una squadra a caso delle migliori ne prende uno
					vincitrice.perdi(1);
					List<Team> teams = Graphs.predecessorListOf(this.grafo, vincitrice);
					Team t = getRandomTeam(teams);
					t.acquisisci(1);
				}
			}
			if(Graphs.vertexHasSuccessors(this.grafo, perdente) &&
					perdente.getNumReporter() > 0) {
				//la squadra perdente ha il 20% di possibilità di bocciare 1 O PIU' reporter
				Double perc = Math.random();
				List<Team> teams = Graphs.successorListOf(this.grafo, perdente);
				Integer numDaBocciare = (int) (Math.random()*(teams.size()));
				if(perc<0.2) {
					//boccio   --> perdente perde numDaBocciare
					//             una squadra a caso ne prende
					perdente.perdi(numDaBocciare);
					Team t = getRandomTeam(teams);
					t.acquisisci(numDaBocciare);
				}
			}
			break;
		}
		
	}
	
	
	
	public static Team getRandomTeam(List<Team> teams) {
		random = new Random();
		Team t = teams.get(random.nextInt(teams.size()));
		return t;
	}
	
	
	
	
	public Double getMediaReporterPartita() {
		Double somma = 0.0;
		for(Integer n : numReporterPartita) {
			somma += n;
		}
		return (somma / numReporterPartita.size());
	}
	
	
	public int getSottoSoglia() {
		int sottoSoglia = 0;
		for(Integer n : numReporterPartita) {
			if(n<X) {
				sottoSoglia++;
			}
		}
		return sottoSoglia;
	}
	
	
	
	

}
