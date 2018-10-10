package com.example.nathan.movieknight;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.nathan.movieknight.activities.LoginActivity;
import com.example.nathan.movieknight.models.MovieEvent;
import com.example.nathan.movieknight.models.Profile;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Created by Kevin on 4/16/2016.
 */

public class ClientListener extends Thread {

    private Socket mSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private MovieKnightAppli application;
    public ClientListener(Socket inSocket, MovieKnightAppli application) {
        mSocket = inSocket;
        boolean socketReady = initializeVariables();
        this.application = application;
    }
    public Object clientRequest(Object[] objects){

        ClientRequest cr = new ClientRequest();
        cr.execute(objects);
        Object object = null;
        try{
            object =  cr.get();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        }
        return object;
    }

    private boolean initializeVariables() {
        try {
            ois = new ObjectInputStream(mSocket.getInputStream());
            oos = new ObjectOutputStream(mSocket.getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    public void run() {
        boolean friend;
        boolean event;
        String username = application.getUserName();
        try {
            while (true) {
                Thread.sleep(5000);
                try {
                   if(HasSeenRequestsRequest(username)){
                       application.FriendRequestPopUp();
                   }

                    if(HasSeenInvitesRequest(username)){
                        application.EventInvitedPopUp();
                    }

                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public void sendObject(Object obj) {
        try {
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    synchronized public Profile ProfileRequest(String username) throws IOException, ClassNotFoundException{
    	sendObject(ProfileRequestDialogue(username));
    	return (Profile)ois.readObject();
    }
    synchronized public MovieEvent MovieEventRequest(String eventID) throws IOException, ClassNotFoundException{
    	sendObject(MovieEventRequestDialogue(eventID));
    	return (MovieEvent)ois.readObject();
    }
    synchronized public Profile LoginRequest(String username, String password) throws IOException, ClassNotFoundException{
    	sendObject(LoginRequestDialogue(username, password));
        Profile prof = null;
        try{
           prof  = (Profile)ois.readObject();
        } catch(EOFException e){

        } catch(NetworkOnMainThreadException nw){
            Log.d("network", "network exception");
        }
        return prof;
    }
    synchronized public Profile RegisterRequest(String username, String password, int zip) throws IOException, ClassNotFoundException{
    	sendObject(RegisterRequestDialogue(username, password, zip));

    	Profile newUser = (Profile)ois.readObject();
    	return newUser;
    }
    synchronized public MovieEvent MakeEventRequest(String owner, int goingToWatch
    		, String EventTitle,  boolean public_private,String time
    		, String location, Vector<String> invitations) throws IOException, ClassNotFoundException{
    	sendObject(MakeEventRequestDialogue(owner, goingToWatch,
                EventTitle, public_private, time, location, invitations));
    	return (MovieEvent)ois.readObject();
    }
    synchronized public boolean FriendRequestRequest(String subject, String object) throws IOException, ClassNotFoundException{
    	sendObject(FriendRequestRequestDialogue(subject, object));
    	return (boolean)ois.readObject();
    }
    synchronized public boolean FriendRequestReplyRequest(String subject, String object, boolean reply) throws IOException, ClassNotFoundException{
    	sendObject(FriendRequestReplyRequestDialogue(subject, object, reply));
        return (boolean)ois.readObject();
    }
    synchronized public boolean EventReplyRequest(String eventID, String username, boolean reply) throws IOException, ClassNotFoundException{
    	sendObject(EventReplyRequestDialogue(eventID, username, reply));
    	return (boolean)ois.readObject();
    }
    synchronized public boolean AddToToWatchListRequest(int movieID, String username, String movieName) throws IOException, ClassNotFoundException{
    	sendObject(AddToToWatchListRequestDialogue(movieID, username, movieName));
    	return (boolean)ois.readObject();
    }
    synchronized public boolean AddToLikedListRequest(int movieID, String username, String movieName) throws IOException, ClassNotFoundException{
    	sendObject(AddToLikedListRequestDialogue(movieID, username, movieName));
    	return (boolean)ois.readObject();
    }
    synchronized public boolean AddToWatchedListRequest(int movieID, String username, String movieName) throws IOException, ClassNotFoundException{
    	sendObject(AddToWatchedListRequestDialogue(movieID, username, movieName));
    	return (boolean)ois.readObject();
    }
    synchronized public boolean UpdatePersonalDescriptionRequest(String description, String username) throws IOException, ClassNotFoundException{
    	sendObject(UpdatePersonalDescriptionRequestDialogue(description, username));
    	return (boolean)ois.readObject();
    }
    synchronized public boolean  EditMovieEventRequest(MovieEvent me) throws IOException, ClassNotFoundException{
    	sendObject(EditMovieEventRequestDialogue(me));
    	return (boolean)ois.readObject();
    }
    synchronized public Vector<String> ListAllUsersRequest() throws ClassNotFoundException, IOException{
    	sendObject(ListAllUsersRequestDialogue());
    	return (Vector<String>)ois.readObject();
    }
    synchronized public boolean HasSeenRequestsRequest(String username) throws ClassNotFoundException, IOException{
        sendObject(HasSeenRequestsDialogue(username));
        return (boolean)ois.readObject();
    }
    synchronized public boolean HasSeenInvitesRequest(String username) throws ClassNotFoundException, IOException{
        sendObject(HasSeenInvitesDialogue(username));
        return (boolean)ois.readObject();
    }
    synchronized public Vector<String> GetPublicEventsRequest() throws ClassNotFoundException, IOException{
        sendObject(GetPublicEventsDialogue());
        return (Vector<String>)ois.readObject();
    }
    
   /* public ServerClientDialogue MovieRequest(String title){
    	return new ServerClientDialogue(MovieConstants.MovieRequest, title);
    }*/
    private ServerClientDialogue ProfileRequestDialogue(String name){
    	return new ServerClientDialogue(MovieConstants.ProfileRequest, name);
    }
    private ServerClientDialogue MovieEventRequestDialogue(String eventID){
    	return new ServerClientDialogue(MovieConstants.MovieEventRequest, eventID);
    }
    private ServerClientDialogue LoginRequestDialogue(String name, String password){
    	return new ServerClientDialogue(MovieConstants.LoginRequest
    			, name + "\b" + password);
    }
    private ServerClientDialogue RegisterRequestDialogue(String name, String password, int zip){
    	return new ServerClientDialogue(MovieConstants.RegisterRequest
    			, name + "\b" + password + "\b" + zip);
    }
    private ServerClientDialogue MakeEventRequestDialogue(String owner, int goingToWatch
    		, String EventTitle, boolean public_private, String time
    		, String location, Vector<String> invitations){
    	MovieEvent me = new MovieEvent(owner, goingToWatch);
    	me.setDescription(EventTitle);
        me.setPublic_private(public_private);
    	me.setMovieTime(time);
    	me.setTheater(location);
    	me.setInvited(invitations);
    	return new ServerClientDialogue(MovieConstants.MakeEventRequest, me);
    }
    private ServerClientDialogue FriendRequestRequestDialogue(String subject, String object){
    	return new ServerClientDialogue(MovieConstants.FriendRequestRequest
    			, subject + "\b" + object);
    	//subject sends friend request to object
    }
    private ServerClientDialogue FriendRequestReplyRequestDialogue(String subject, String object, boolean reply){
    	return new ServerClientDialogue(MovieConstants.FriendRequestReplyRequest
    			, subject+"\b"+object+"\b"+reply);
    	//subject replies to object's friend request with reply(true = yes)
    }
    /*public ServerClientDialogue EventInviteRequest(String eventID, String name){
    	return new ServerClientDialogue(MovieConstants.MovieRequest, title);
    }*/
    private ServerClientDialogue EventReplyRequestDialogue(String eventID, String name, boolean reply){
    	return new ServerClientDialogue(MovieConstants.EventReplyRequest
    			, eventID+"\b"+name+"\b"+reply);
    }
    private ServerClientDialogue AddToToWatchListRequestDialogue(int movieID, String name, String movieName){
    	return new ServerClientDialogue(MovieConstants.AddToToWatchListRequest
    			, movieID+"\b"+name+"\b"+movieName);
    }
    private ServerClientDialogue AddToLikedListRequestDialogue(int movieID, String name, String movieName){
    	return new ServerClientDialogue(MovieConstants.AddToLikedListRequest
    			, movieID+"\b"+name+"\b"+movieName);
    }
    private ServerClientDialogue AddToWatchedListRequestDialogue(int movieID, String name, String movieName){
    	return new ServerClientDialogue(MovieConstants.AddToWatchedListRequest
    			, movieID+"\b"+name+"\b"+movieName);
    }
    private ServerClientDialogue UpdatePersonalDescriptionRequestDialogue(String description, String name){
    	return new ServerClientDialogue(MovieConstants.UpdatePersonalDescriptionRequest
    			, description+"\b"+name);
    }
    private ServerClientDialogue EditMovieEventRequestDialogue(MovieEvent me){
    	return new ServerClientDialogue(MovieConstants.EditMovieEventRequest
    			, me);
    }
    private ServerClientDialogue ListAllUsersRequestDialogue() {
    	return new ServerClientDialogue(MovieConstants.ListAllUsersRequest,"");
    }
    private ServerClientDialogue HasSeenRequestsDialogue(String name) {
        return new ServerClientDialogue(MovieConstants.HasSeenRequestsRequest,name);
    }
    private ServerClientDialogue HasSeenInvitesDialogue(String name) {
        return new ServerClientDialogue(MovieConstants.HasSeenInvitesRequest,name);
    }
    private ServerClientDialogue GetPublicEventsDialogue(){
        return new ServerClientDialogue(MovieConstants.GetPublicEventsRequest,"");
    }
    class ClientRequest extends AsyncTask<Object, Void, Object>{
        protected Object doInBackground(Object... objects) {
            String code = (String) objects[0];
            if(code.equals("Login")){
                String username = (String)objects[1];
                String password = (String)objects[2];
                try {
                    return LoginRequest(username,password);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Register")){
                String username = (String)objects[1];
                String password = (String)objects[2];
                int zipcode = (Integer)objects[3];
                try {
                    return RegisterRequest(username, password, zipcode);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if (code.equals("Make Event")){
                String owner = (String)objects[1];
                int movieID = (Integer)objects[2];
                String EventTitle = (String) objects[3];
                boolean public_private = (Boolean) objects[4];
                String time = (String) objects[5];
                String location = (String) objects[6];
                Vector<String> invitations = (Vector<String>) objects[7];

                try {
                    return MakeEventRequest(owner,movieID,EventTitle,public_private,time,location,invitations);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }else if(code.equals("Update Personal Description")){
                String description = (String) objects[1];
                String username = (String) objects[2];
                try {
                    return UpdatePersonalDescriptionRequest(description,username);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Profile Request")){
                String username = (String) objects[1];
                try {
                    return ProfileRequest(username);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Movie Event Request")) {
                String eventID = (String) objects[1];
                try {
                    return MovieEventRequest(eventID);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Friend Request Reply")) {
                String username = (String) objects[1];
                String friend = (String) objects[2];
                boolean accept = (boolean) objects[3];
                try {
                    return FriendRequestReplyRequest(username, friend, accept);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("List All Users")) {
                try {
                    return ListAllUsersRequest();
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Add To To Watch")) {
                int movieID = (Integer) objects[1];
                String username = (String) objects[2];
                String movieName = (String) objects[3];
                try {
                    return AddToToWatchListRequest(movieID, username, movieName);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Add To Watched")) {
                int movieID = (Integer) objects[1];
                String username = (String) objects[2];
                String movieName = (String) objects[3];
                try {
                    return AddToToWatchListRequest(movieID, username, movieName);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Add To Liked")) {
                int movieID = (Integer) objects[1];
                String username = (String) objects[2];
                String movieName = (String) objects[3];
                try {
                    return AddToLikedListRequest(movieID, username, movieName);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Friend Request")){
                String myUsername = (String) objects[1];
                String friendUsername = (String) objects[2];
                try{
                    return FriendRequestRequest(myUsername,friendUsername);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if(code.equals("Event Reply Request")){
                String eventID = (String) objects[1];
                String username = (String) objects[2];
                Boolean reply = (Boolean) objects[3];
                try{
                    return EventReplyRequest(eventID,username,reply);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }

            } else if(code.equals("Get Public Events Request")){
                try{
                    return GetPublicEventsRequest();
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            } else if (code.equals("Edit Movie Request")) {
             MovieEvent me = (MovieEvent)objects[1];
                try {
                    Log.d("Edit", "Edit");
                   return EditMovieEventRequest(me);
                } catch (ClassNotFoundException cne) {
                    cne.printStackTrace();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
            return null;
        }
    }
}
