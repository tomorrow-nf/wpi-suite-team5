/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team _
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.calendar.models.entry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;


/**List of Calendars pulled from the server
 * 
 * @author srkodzis, adapted from RequirementModel.java
 *
 * @version $Revision: 1.0 $
 */
public class EventModel extends AbstractListModel<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8998701695357104361L;

	// ********************************************************************
	// Construct the Calendar Model
	
	/**
	 * The list in which all the events for a single project
	 * are contained
	 */
	private final List<Event> events;
	
	// TODO: Research if and how this is maintained between different instances of the program
	private int nextID; // the next available ID number for the event
						// that are added
	// the static object that allows the event model to be
	private static EventModel instance = null;
	
	/**
	 * Constructs an empty list of events
	 * Sets a default ID of 0 to the event
	 */
	private EventModel(){
		events = new ArrayList<Event>();
		nextID = 0;
	}
	
	// **********************************************************************
	// Manipulate events
	
	/**
	 * @return the instance of the event model singleton
	 */
	public static EventModel getInstance(){
		if(instance == null){
			instance = new EventModel();
		}
		return instance;
	}
	
	/**
	 * Adds a single calendar datum to the data of the project
	 * 
	 * @param newEvent The calendar datum to be added to the list
	 * 						of events in the project
	 */
	public void addEvent(Event newEvent){
		events.add(newEvent);
		
		this.fireIntervalAdded(this, 0, 0);
	}
	
	/**
	 * Returns the Calendar Data with the given ID
	 * 
	 * @param id The ID number of the event to be returned
	 * 
	
	 * @return the event for the ID, or null if the data is not
	 * 			found. */
	public Event getEvent(int id){
		Event temp = null;
		// iterate through the events in order to find the matching ID
		// break the loop once the ID is found
		for(int i = 0; i < events.size(); i++){
			temp = events.get(i);
			if(temp.getId() == id){
				break;
			}
		}
		return temp;
	}
	
	/**
	 * Removes the event with the given ID
	 * 
	 * @param removeID The ID number of the cldr data to be removed
	 */
	public void removeEvent(int removeID){
		// iterate through the events to find the given ID
		// break the loop once that element has been found and removed
		for(int i = 0; i < events.size(); i++){
			if(events.get(i).getId() == removeID){
				events.remove(i);
				break;
			}
		}
	}
		
	/**
	 * Removes all the event from a model
	 * Each event is removed individually
	 */
	public void emptyModel() {
		final int oldSize = getSize();
		final Iterator<Event> iterator = events.iterator();
		// in case the iterator has data, remove each element individually
		// in order to make sure the model is empty
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}
	
	/**
	 * Adds the given array of events to the list
	 * @param events the array of data to add
	 */
	public void addEvents(Event[] events){
		// iterate through the added array, adding each element to 
		// the model and assigning each element a unique ID as it is added.
		for (int i = 0; i < events.length; i++) {
			this.events.add(events[i]);
			if(events[i].getId() >= nextID){ 
				nextID = events[i].getId() + 1;
			}
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
	}
	
	// ******************************************************************
	// Getters for the events
	
	/**
	 * Provides the number of elements in the list of events for 
	 * this project. Elements are returned from the newest to the oldest.
	 * 
	 * @return the number of events in the project
	 */
	public int getSize() {
		return events.size();
	}
	
	/**
	 * Provides the next ID number the should be used for the next 
	 * event generated.
	 * 
	 * @return the next avail. ID number
	 */
	public int getNextID(){
		return nextID++;
	}

	/**
	 * Takes in an index and find the event in the list for the
	 * project. 
	 * 
	 * @param index The index of the event to be returned
	 * @return the event associated with the given index
	 */
	public Event getElementAt(int index) {
		return events.get(events.size() - 1 - index);
	}
		
	/**
	 * Returns the list of events
	 * @return the requirements held within the event model. */
	public List<Event> getAllEvents() {
		return events;
	}
	
	/**
	 * Get all the events for the team that the user can access
	 * @param userId The id of the user attempting to access the events
	 * @param year the year to check
	 * @return A list of all events the user has access to
	 */
	public List<Event> getTeamEvents( String userId, int year) {
		final List< Event > teamEvents = new ArrayList< Event >();
		Event currentEvent;
		
		for ( int i = 0; i < events.size(); i++ ) {
			
			currentEvent = events.get( i );
			if  ( !currentEvent.isDeleted() && 
					currentEvent.isTeamEvent() &&
					currentEvent.hasAccess( userId ) &&
					currentEvent.occursOnYear( year ) ) {
				teamEvents.add( currentEvent );
			}
		}
		
		return teamEvents;
		
	}
	
	/**
	 * Get all the events for the team that the user can access
	 * @param userId The id of the user attempting to access the events
	 * @param year the year to check
	 * @param month the month to check (0-11)
	 * @return A list of all events the user has access to
	 */
	public List<Event> getTeamEvents( String userId, int year, int month) {
		final List< Event > teamEvents = new ArrayList< Event >();
		Event currentEvent;
		
		for ( int i = 0; i < events.size(); i++ ) {
			
			currentEvent = events.get( i );
			if  ( !currentEvent.isDeleted() && 
					currentEvent.isTeamEvent() &&
					currentEvent.hasAccess( userId ) &&
					currentEvent.occursOnMonth( year, month ) ) {
				teamEvents.add( currentEvent );
			}
		}
		
		return teamEvents;
		
	}
	
	/**
	 * Get all the events for the team that the user can access
	 * @param userId The id of the user attempting to access the events
	 * @param year the year to check
	 * @param month the month to check (0-11)
	 * @param day the day to check
	 * @return A list of all events the user has access to
	 */
	public List<Event> getTeamEvents( String userId, int year, int month, int day) {
		final List< Event > teamEvents = new ArrayList< Event >();
		Event currentEvent;
		
		for ( int i = 0; i < events.size(); i++ ) {
			
			currentEvent = events.get( i );
			if  ( !currentEvent.isDeleted() && 
					currentEvent.isTeamEvent() &&
					currentEvent.hasAccess( userId ) &&
					currentEvent.occursOnDate( year, month, day ) ) {
				teamEvents.add( currentEvent );
			}
		}
		
		return teamEvents;
		
	}
	
	/**
	 * Get all the events for the user that the user can access
	 * This includes both team and individual events
	 * @param userId The id of the user attempting to access the events
	 * @param year the year to check
	 * @return A list of all events the user has access to
	 */
	public List<Event> getUserEvents( String userId, int year) {
		final List< Event > userEvents = new ArrayList< Event >();
		Event currentEvent;
		
		for ( int i = 0; i < events.size(); i++ ) {
			
			currentEvent = events.get( i );
			if  ( !currentEvent.isDeleted() &&
					currentEvent.hasAccess( userId ) &&
					currentEvent.occursOnYear( year ) ) {
				userEvents.add( currentEvent );
			}
		}
		
		return userEvents;
		
	}
	
	/**
	 * Get all the events for the user that the user can access
	 * This includes both team and individual events
	 * @param userId The id of the user attempting to access the events
	 * @param year the year to check
	 * @param month the month to check (0-11)
	 * @return A list of all events the user has access to
	 */
	public List<Event> getUserEvents( String userId, int year, int month) {
		final List< Event > userEvents = new ArrayList< Event >();
		Event currentEvent;
		
		for ( int i = 0; i < events.size(); i++ ) {
			
			currentEvent = events.get( i );
			if  ( !currentEvent.isDeleted() &&
					currentEvent.hasAccess( userId ) &&
					currentEvent.occursOnMonth( year, month ) ) {
				userEvents.add( currentEvent );
			}
		}
		
		return userEvents;
		
	}
	
	/**
	 * Get all the events for the user that the user can access
	 * This includes both team and individual events
	 * @param userId The id of the user attempting to access the events
	 * @param year the year to check
	 * @param month the month to check (0-11)
	 * @param day the day to check
	 * @return A list of all events the user has access to
	 */
	public List<Event> getUserEvents( String userId, int year, int month, int day) {
		final List< Event > userEvents = new ArrayList< Event >();
		Event currentEvent;
		
		for ( int i = 0; i < events.size(); i++ ) {
			
			currentEvent = events.get( i );
			if  ( !currentEvent.isDeleted() &&
					currentEvent.hasAccess( userId ) &&
					currentEvent.occursOnDate( year, month, day ) ) {
				userEvents.add( currentEvent );
			}
		}
		
		return userEvents;
		
	}
	
}