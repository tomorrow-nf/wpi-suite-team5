/*********************************************************************************************
 * Copyright (c) 2013 WPI Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors: Team _
 *  
 *********************************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.calendar.categorycontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.calendar.categoryobserver.GetCategoryRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.category.Category;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.category.CategoryModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller coordinates retrieving all of the Categories
 * from the server.
 *
 * @version $Revision: 1.0 $
 * @author Team Underscore
 */
public class GetCategoryController implements ActionListener {

	private final GetCategoryRequestObserver observer;
	private static GetCategoryController instance = null;
	
	/**
	 * Constructs the controller given a CategoryModel
	 */
	private GetCategoryController() {
		observer = new GetCategoryRequestObserver( this );
	}
	
	/**
	 * @return the instance of the GetCategoryController or creates one if it does not
	 * exist.
	 */
	public static GetCategoryController getInstance() {
		
		if(instance == null)
		{
			instance = new GetCategoryController();
		}
		
		return instance;
	}

	/**
	 * Sends an HTTP request to store a Category when the
	 * button for event creation is pressed
	 * @param e ActionEvent
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) 
	 */
	public void actionPerformed(ActionEvent e) {
		// Send a request to the core to save this category
		final Request request = 
				Network.getInstance().makeRequest(
						"calendar/category", 
						HttpMethod.GET); // GET is read
		request.addObserver( observer ); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Sends an HTTP request to retrieve all Category instances
	 */
	public void retrieveCategory() {
		final Request request = 
				Network.getInstance().makeRequest(
						"calendar/category", 
						HttpMethod.GET); // GET is read
		request.addObserver( observer ); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Add the given Categories to the local model (they were received from the core).
	 * This method is called by the GetCategoryRequestObserver
	 * 
	 * @param categories array of Category instances received from the server
	 */
	public void receivedCategory(Category[] categories) {
		// Make sure the response was not null
		if (categories != null) 
		{
			// add the Event instances to the local model
			CategoryModel.getInstance().addcategories( categories );
		}
	}
	
	
}
