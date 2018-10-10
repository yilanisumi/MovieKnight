package com.example.nathan.movieknight;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Samuel Wang on 4/17/2016.
 */
public class ConnectToServer extends AsyncTask<Void, Void, ClientListener> {
    private ClientListener c;
    private MovieKnightAppli application;
    protected ClientListener doInBackground(Void... voids) {
        try {
            //connect to local server
           c = new ClientListener(new Socket("10.0.2.2", 5000), application);
            //connect to hotspot
           // c = new ClientListener(new Socket("192.168.43.30", 5000), application);
            return c;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
    ConnectToServer(MovieKnightAppli application){
        this.application = application;
    }

}
