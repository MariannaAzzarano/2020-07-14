package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		
		model.creGrafo();
		System.out.println("GRAFO CREATO");
		System.out.println(model.nVertici() + " VERTICI");
		System.out.println(model.nArchi() + " ARCHI");
		
		Team team = model.getTeam(43);
		System.out.println("\nSQUADRE MIGLIORI");
		List<Team> migliori = model.getSquadreMigliori(team);
		for(Team s : migliori) {
			System.out.println(s.getName() + " ("+ (s.getPunti()-team.getPunti())+")");
		}
		
		System.out.println("\nSQUADRE PEGGIORI");
		List<Team> peggiori = model.getSquadrePeggiori(team);
		for(Team s : peggiori) {
			System.out.println(s.getName() + " ("+ (team.getPunti()-s.getPunti())+")");
		}
		
		
		model.simula(20, 4);
		System.out.println("MEDIA REPORTER PER PARTITA: " + model.getMediaReporter());
		System.out.println("PARTITE SOTTO SOGLIA: " + model.getSottoSoglia());

	}

}
