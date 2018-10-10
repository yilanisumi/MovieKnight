package com.example.nathan.movieknight.models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Vector;

public class Profile implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static class UsernameComparator implements Comparator<Profile>, Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		public int compare(Profile arg0, Profile arg1) {
			return arg0.getUsername().compareTo(arg1.getUsername());
		}
	}

	private String username, profilePicture, description;
	private Vector<String> friends, friendRequests;
	private Vector<Integer> toWatch, watched, liked;
	private Vector<String> toWatchName, watchedName, likedName;
	private Vector<String> events, eventRequests;
	private int zipcode;
	private UsernameComparator ucomp = new UsernameComparator();

	//constructor for generic com.example.nathan.movieknight.models.Profile
	public Profile(){
		username = "*Name unavailable";
		profilePicture = "noimageavailable.png"; //This needs to be changed
		description = "";
		friends = new Vector<String>();
		friendRequests = new Vector<String>();
		toWatch = new Vector<Integer>();
		watched = new Vector<Integer>();
		toWatchName = new Vector<String>();
		watchedName = new Vector<String>();
		liked = new Vector<Integer>();
		likedName = new Vector<String>();
		events = new Vector<String>();
		eventRequests = new Vector<String>();
		zipcode = 0;
	}

	public Profile(String name, String pic, int zip){
		username = name;
		profilePicture = pic;
		description = "";
		friends = new Vector<String>();
		friendRequests = new Vector<String>();
		toWatch = new Vector<Integer>();
		watched = new Vector<Integer>();
		toWatchName = new Vector<String>();
		watchedName = new Vector<String>();
		liked = new Vector<Integer>();
		likedName = new Vector<String>();
		events = new Vector<String>();
		eventRequests = new Vector<String>();
		zipcode = zip;
	}
	//Getters
	public String getUsername() {
		return username;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public String getDescription() {
		return description;
	}
	public Vector<String> getFriends() {
		return friends;
	}
	public Vector<String> getFriendRequests() {
		return friendRequests;
	}
	public Vector<Integer> getToWatch() {
		return toWatch;
	}
	public Vector<Integer> getWatched() {
		return watched;
	}
	public Vector<String> getToWatchName() { return toWatchName; }
	public Vector<String> getWatchedName() { return watchedName; }
	public Vector<Integer> getLiked() {
		return liked;
	}
	public Vector<String> getLikedName() {
		return likedName;
	}
	public Vector<String> getEvents() {
		return events;
	}
	public Vector<String> getEventRequests() {
		return eventRequests;
	}
	public int getZipcode() {
		return zipcode;
	}
	public UsernameComparator getUsernameComparator() {
		return ucomp;
	}

	//Mutators
	public void setUsername(String username) {
		this.username = username;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setFriends(Vector<String> friends) {
		this.friends = friends;
	}
	public void setFriendRequests(Vector<String> friendRequests) {
		this.friendRequests = friendRequests;
	}
	public void setToWatch(Vector<Integer> toWatch) {
		this.toWatch = toWatch;
	}
	public void setWatched(Vector<Integer> watched) {
		this.watched = watched;
	}
	public void setToWatchName(Vector<String> toWatchName) { this.toWatchName = toWatchName; }
	public void setWatchedName(Vector<String> watchedName) { this.watchedName = watchedName; }
	public void setLiked(Vector<Integer> liked) { this.liked = liked; }
	public void setLikedName(Vector<String> likedName) {
		this.likedName =  likedName;
	}
	public void setEvents(Vector<String> events) {
		this.events = events;
	}
	public void setEventRequests(Vector<String> eventRequests) {
		this.eventRequests = eventRequests;
	}
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}



}
