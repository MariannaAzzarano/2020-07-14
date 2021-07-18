package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private Map<Integer, Team> idMap;
	private Simulator sim;
	private List<Match> matches;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		sim = new Simulator();
		matches = dao.listAllMatches();
	}
	

	public List<Team> getAllTeams(){
		List<Team> teams = dao.listAllTeams();
		Collections.sort(teams);
		for(Team t : teams) {
			idMap.put(t.getTeamID(), t);
		}
		return teams;
	}
	
	public void creGrafo() {
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//aggiungo tutti i vertici
		Graphs.addAllVertices(this.grafo, this.getAllTeams());
		this.assegnaPunteggi();
		for(Team s1 : idMap.values()) {
			for(Team s2 : idMap.values()) {
				if(s1.getPunti() > s2.getPunti()) {
					//da s1 a s2
					Graphs.addEdge(this.grafo, s1, s2, s1.getPunti()-s2.getPunti());
				}
				else if(s1.getPunti() < s2.getPunti()) {
					//da s2 a s1
					Graphs.addEdge(this.grafo, s2, s1, s2.getPunti()-s1.getPunti());
				}
			}
		}
	}
	
	public void assegnaPunteggi() {
		List<Match> matches = dao.listAllMatches();
		for(Match m : matches) {
			//controlla che idMap non sia nulla
			Team s1 = idMap.get(m.getTeamHomeID());
			Team s2 = idMap.get(m.getTeamAwayID());
			if(m.getResultOfTeamHome() == 1) {
				s1.vittoria();  //aggiungo 3 al vincitore
			}
			else if(m.getResultOfTeamHome() == -1) {
				s2.vittoria();
			}
			else {
				//aggiungi 1 a entrambi
				s1.pareggio();
				s2.pareggio();
			}
		}
	}
	
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<Team> getSquadrePeggiori(Team squadra){
		List<Team> peggiori = new ArrayList<>();
		for(Team s : idMap.values()) {
			if(s.getPunti()<squadra.getPunti()) {
				//aggiungi alla lista da restituire
				peggiori.add(s);
			}
		}
		Collections.sort(peggiori, new Comparator<Team>() {

			@Override
			public int compare(Team o1, Team o2) {
				return o2.getPunti()-o1.getPunti();
			}
			
		});
		return peggiori;
	}
	
	
	public List<Team> getSquadreMigliori(Team squadra){
		List<Team> migliori = new ArrayList<>();
		for(Team s : idMap.values()) {
			if(s.getPunti()>squadra.getPunti()) {
				migliori.add(s);
			}
		}
		Collections.sort(migliori, new Comparator<Team>() {

			@Override
			public int compare(Team o1, Team o2) {
				return o1.getPunti()-o2.getPunti();
			}
		});
		return migliori;
	}
	
	
	public Team getTeam(Integer ID) {
		return idMap.get(ID);
	}
	
	
	public Graph<Team, DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}
	
//	public Integer getPunteggioSquadra(Integer ID) {
//		return idMap.get(ID).getPunti();
//	}
	
	
	
	
	public Map<Integer, Team> getMapTeams(){
		return idMap;
	}
	
	
	public void simula(int numReporter, int soglia) {
		if(grafo != null) {
			sim.setNumReporter(numReporter);
			sim.setSoglia(soglia);
			sim.run(matches, grafo, idMap);
		}
	}
	
	
	public Double getMediaReporter() {
		return sim.getMediaReporterPartita();
	}
	
	
	public int getSottoSoglia() {
		return sim.getSottoSoglia();
	}
	
	
	
}
