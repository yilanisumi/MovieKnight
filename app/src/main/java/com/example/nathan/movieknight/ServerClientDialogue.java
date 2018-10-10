package com.example.nathan.movieknight;

import java.io.Serializable;

public class ServerClientDialogue implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int requestType;
	private Object DialogueContent;

	public ServerClientDialogue(int type, Object content){
		requestType = type;
		DialogueContent = content;
	}

	public int getRequestType(){
		return requestType;
	}
	public Object getDialogueContent(){
		return DialogueContent;
	}
}
