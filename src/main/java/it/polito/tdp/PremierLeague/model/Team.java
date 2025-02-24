package it.polito.tdp.PremierLeague.model;

public class Team implements Comparable<Team>{
	Integer teamID;
	String name;
	Integer punti;
	Integer numReporter;

	public Team(Integer teamID, String name) {
		super();
		this.teamID = teamID;
		this.name = name;
		this.punti = 0;
	}
	
	public Integer getNumReporter() {
		return numReporter;
	}

	public void setNumReporter(Integer numReporter) {
		this.numReporter = numReporter;
	}

	public Integer getTeamID() {
		return teamID;
	}
	
	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	public Integer getPunti() {
		return punti;
	}

	public void setPunti(Integer punti) {
		this.punti = punti;
	}
	
	public void vittoria() {
		this.punti = punti +3;
	}
	
	public void pareggio() {
		this.punti++;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((teamID == null) ? 0 : teamID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (teamID == null) {
			if (other.teamID != null)
				return false;
		} else if (!teamID.equals(other.teamID))
			return false;
		return true;
	}

	@Override
	public int compareTo(Team o) {
		return this.name.compareTo(o.name);
	}
	
	public void perdi(Integer num) {
		this.numReporter = this.numReporter - num;
	}
	
	
	public void acquisisci(Integer num) {
		this.numReporter = this.numReporter + num;
	}
	
}
