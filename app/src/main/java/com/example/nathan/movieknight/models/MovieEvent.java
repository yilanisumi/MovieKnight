package com.example.nathan.movieknight.models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;
import java.util.Vector;

public class MovieEvent implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static class MovieComparator implements Comparator<MovieEvent>, Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		public int compare(MovieEvent arg0, MovieEvent arg1) {
			return arg0.getGoingToWatch() - arg1.getGoingToWatch();
		}
	}
	public static class MovieTimeComparator implements Comparator<MovieEvent>, Serializable{
		private static final long serialVersionUID = 1L;
		public int compare(MovieEvent arg0, MovieEvent arg1) {
			return arg0.getMovieTime().compareTo(arg1.getMovieTime());
		}
	}

	private String owner;
	private int goingToWatch;
	private Vector<String> participants, invited;
	private String description, movieTime, theater;
	private MovieComparator mc = new MovieComparator();
	private MovieTimeComparator mtc = new MovieTimeComparator();
	private String eventID;
	private boolean public_private; //true for public 

	public MovieEvent(){
		owner = "";
		goingToWatch = -1;
		participants = new Vector<String>();
		invited = new Vector<String>();
		description = "No description";
		movieTime = "To be decided";
		theater = "To be decided";
		UUID eid = UUID.randomUUID();
		eventID = eid.toString();
		public_private = true;
	}

	public MovieEvent(String owner, int goingToWatch){
		this.owner = owner;
		this.goingToWatch = goingToWatch;
		participants = new Vector<String>();
		invited = new Vector<String>();
		description = "No description";
		movieTime = "To be decided";
		theater = "To be decided";
		UUID eid = UUID.randomUUID();
		eventID = eid.toString();
		public_private = true;
	}

	//Getters
	public String getOwner() {
		return owner;
	}
	public int getGoingToWatch() {
		return goingToWatch;
	}
	public Vector<String> getParticipants() {
		return participants;
	}
	public Vector<String> getInvited() {
		return invited;
	}
	public String getMovieTime() {
		return movieTime;
	}
	public String getTheater() {
		return theater;
	}
	public String getDescription() {
		return description;
	}
	public String getEventID() {
		return eventID;
	}
	public MovieComparator getMovieComparator() {
		return mc;
	}
	public MovieTimeComparator getMovieTimeCompartor() {
		return mtc;
	}
	public boolean isPublic_private() {
		return public_private;
	}

	//Mutators
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setGoingToWatch(int goingToWatch) {
		this.goingToWatch = goingToWatch;
	}
	public void setParticipants(Vector<String> participants) {
		this.participants = participants;
	}
	public void setInvited(Vector<String> invited) {
		this.invited = invited;
	}
	public void setMovieTime(String movieTime) {
		this.movieTime = movieTime;
	}
	public void setTheater(String theater) {
		this.theater = theater;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public void addParticipant(String newParticipant){
		participants.add(newParticipant);
	}
	public void addInvited(String newInvitee){
		invited.add(newInvitee);
	}
	public void removeParticipant(Profile quitter){
		participants.remove(quitter);
	}
	public void removeInvited(Profile tfti){
		invited.remove(tfti);
	}
	public void setPublic_private(boolean public_private) {
		this.public_private = public_private;
	}
}
