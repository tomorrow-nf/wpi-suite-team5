/*********************************************************************************************
 * Copyright (c) 2013 WPI Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * List of Category pulled from the server
 * 
 * Adapted from RequirementModel.java by Team Underscore Database Crew
 * 
 * Contributors: Team _
 *
 *********************************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.calendar.models.category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.calendar.categorycontroller.UpdateCategoryController;
import edu.wpi.cs.wpisuitetng.modules.calendar.refresh.CategoryRefreshListener;

import javax.swing.AbstractListModel;

/**
 * List of Categories pulled from the server
 * 
 * @author Team Underscore
 * 
 * @version $Revision: 1.0 $
 */
public class CategoryModel extends AbstractListModel<Category> {

	private static final long serialVersionUID = 8555534911453497404L;

	/**
	 * The list in which all the category data for a single project is contained
	 */
	private final List<Category> categories;

	private int nextID; // the next available ID number for the category data
						// that are added

	// the static object that allows the category data model to be globally
	// accessible.
	private static CategoryModel instance = null;

	/**
	 * Constructs an empty list of category Sets a default ID of 0 to the
	 * category
	 */
	private CategoryModel() {
		categories = new ArrayList<Category>();
		nextID = 0;
	}

	// **********************************************************************
	// Manipulate category

	/**
	 * @return the instance of the category model singleton
	 */
	public static CategoryModel getInstance() {
		if (instance == null) {
			instance = new CategoryModel();
			instance.addListDataListener(new CategoryRefreshListener());
		}
		return instance;
	}

	/**
	 * Adds a single category to the data of the project
	 * 
	 * @param newCategory
	 *            The category to be added to the list of category in
	 *            the project
	 */
	public void addCategory(Category newCategory) {
		categories.add(newCategory);

		this.fireIntervalAdded(this, 0, 0);
	}

	/**
	 * Returns the category with the given ID
	 * 
	 * @param id
	 *            The ID number of the category to be returned
	 * 
	 * @return the category for the ID, or null if the data is not found.
	 */
	public Category getCategory(int id) {
		Category temp = null;
		Category out = null;
		// iterate through the categories in order to find the matching ID
		// break the loop once the ID is found
		for (int i = 0; i < categories.size(); i++) {
			temp = categories.get(i);
			if (temp.getId() == id) {
				out = temp;
				break;
			}
		}
		this.fireContentsChanged(this, 0, 0);
		return out;
	}

	/**
	 * Returns the category with the given Name
	 * 
	 * @param name
	 *            The name string of the category to be returned
	 * 
	 * 
	 * @return the category for the ID, or null if the data is not found.
	 */
	public Category getCategory(String name) {
		Category temp = null;
		Category out = null;
		// iterate through the category in order to find the matching Name
		// break the loop once the Name is found
		for (int i = 0; i < categories.size(); i++) {
			temp = categories.get(i);
			System.out.println(temp.getName());
			System.out.println(name);
			System.out.println(categories.size());
			if (temp != null && !(temp.getName() == null)) {
				if (temp.getName().equals(name)) {
					out = temp;
					break;
				}
			}
		}
		this.fireContentsChanged(this, 0, 0);
		return out;
	}

	/**
	 * Removes the category with the given ID
	 * 
	 * @param removeID
	 *            The ID number of the category to be removed
	 */
	public void removeCategory(int removeID) {
		// iterate through the categoriesto find the given ID
		// break the loop once that element has been found and removed
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getId() == removeID) {
				categories.remove(i);
				break;
			}
		}
		this.fireContentsChanged(this, 0, 0);
	}

	/**
	 * Removes all the Category from a model Each Category is removed
	 * individually
	 */
	public void emptyModel() {
		final int oldSize = getSize();
		final Iterator<Category> iterator = categories.iterator();
		// in case the iterator has data, remove each element individually
		// in order to make sure the model is empty
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}

	/**
	 * Adds the given array of categories to the list
	 * 
	 * @param categories
	 *            the array of data to add
	 */
	public void addcategories(Category[] categories) {
		// iterate through the added array, adding each element to
		// the model and assigning each element a unique ID as it is added.
		for (int i = 0; i < categories.length; i++) {
			this.categories.add(categories[i]);
			if (categories[i].getId() >= nextID) {
				nextID = categories[i].getId() + 1;
			}
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
	}

	// ******************************************************************
	// Getters for the category model

	/**
	 * Method that receives a category identification number as input and
	 * returns either name of the category that is associated with that
	 * particular identification number or a default name of
	 * "No category selected" if there is no such identification number that is
	 * present within the list of categories contained in the local
	 * CategoryModel.
	 * 
	 * @param catId
	 *            The identification number of a particular category.
	 * 
	 * @return the name of the category that has the associated ID number.
	 */
	public String getNameOfCatId(int catId) {

		// Iterate over the entire list of categories contained within
		// the local CategoryModel.
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getId() == catId
					&& !categories.get(i).isDeleted()) {
				return categories.get(i).getName();
			}
			
		}

		// If the input category id does not correspond to the identification
		// number of a category that is currently contained within the local
		// CategoryModel, then provide the appropriate string.
		return "No category selected";
	}

	/**
	 * Provides the number of elements in the list of category for this
	 * project. Elements are returned from the newest to the oldest.
	 * 
	 * @return the number of categories in the project
	 */
	public int getSize() {
		return categories.size();
	}

	/**
	 * Provides the next ID number the should be used for the next category
	 * generated.
	 * 
	 * @return the next avail. ID number
	 */
	public int getNextID() {
		return nextID++;
	}

	/**
	 * Takes in an index and find the category in the list for the project.
	 * 
	 * @param index
	 *            The index of the category to be returned
	 * @return the category associated with the given index
	 */
	public Category getElementAt(int index) {
		return categories.get(categories.size() - 1 - index);
	}

	/**
	 * Set an updated category in the model
	 * 
	 * @param c
	 *            the updated category
	 */
	public void updateCategory(Category c) {
		for (int i = 0; i < categories.size(); i++) {
			if (c.getId() == categories.get(i).getId()) {
				categories.set(i, c);
			}
		}
		this.fireContentsChanged(this, 0, 0);
	}

	/**
	 * Returns the list of category
	 * 
	 * @return the requirements held within the category model.
	 */
	public List<Category> getAllcategories() {
		return categories;
	}

	/**
	 * Returns the list of category
	 * 
	 * @return the requirements held within the category model.
	 */
	public List<Category> getAllNondeletedCategories() {
		final List<Category> categories = new ArrayList<Category>();

		for (Category cat : this.getAllcategories()) {

			if (!cat.isDeleted()) {
				System.out.println("Got category: " + cat.getName());
				System.out.println("Got category status: " + cat.isDeleted());
				categories.add(cat);
			}
		}
		this.fireContentsChanged(this, 0, 0);
		return categories;
	}

	/**
	 * Returns the list of categories that aren't deleted and will be applied
	 * as filters.
	 * 
	 * @return the requirements held within the category model.
	 */
	public List<Category> getAllNondeletedCategoriesAsFilters() {
		final List<Category> categories = new ArrayList<Category>();

		for (Category cat : this.getAllcategories()) {

			if (!cat.isDeleted() && cat.getHasFilter()) {
				System.out.println( "Got category: " + cat.getName() );
				System.out.println( "Got category status: " + cat.isDeleted() );
				categories.add(cat);
			}
		}
		
		return categories;
	}
	
	/**
	 * Returns the list of category
	 * 
	 * @return the requirements held within the category model.
	 */
	public List<Category> getAllNonfilterCategories() {
		final List<Category> categories = new ArrayList<Category>();

		for (Category cat : this.getAllcategories()) {

			if (!cat.getHasFilter()) {
				categories.add(cat);
			}
		}
		
		return categories;
	}

	/**
	 * Get all the categories for the team that the user can access
	 * 
	 * @param userId
	 *            The id of the user attempting to access the categories
	 * @return A list of all team categories the user has access to
	 */
	public List<Category> getTeamCategories(String userId) {
		final List<Category> teamCategories = new ArrayList<Category>();
		Category currentCategory;

		for (int i = 0; i < categories.size(); i++) {

			currentCategory = categories.get(i);
			if (!currentCategory.isDeleted() && currentCategory.isTeamCat()) {
				teamCategories.add(currentCategory);
			}
		}
		
		return teamCategories;

	}

	/**
	 * Get all the categories that are classified as personal and accessible to
	 * the user.
	 * 
	 * @param userId
	 *            The id of the user attempting to access the categories
	 * @return A list of all personal categories the user has access to
	 */
	public List<Category> getPersonalCategories(String userId) {
		final List<Category> personalCategories = new ArrayList<Category>();
		Category currentCategory;

		for (int i = 0; i < categories.size(); i++) {

			currentCategory = categories.get(i);
			if (!currentCategory.isDeleted() && !currentCategory.isTeamCat()) {
				personalCategories.add(currentCategory);
			}
		}
		
		return personalCategories;

	}

	/**
	 * Get all the categories for the user that the user can access This
	 * includes both team and individual categories
	 * 
	 * @param userId
	 *            The id of the user attempting to access the categories
	 * @return A list of all categories the user has access to
	 */
	public List<Category> getuserCategories(String userId) {
		final List<Category> userCategories = new ArrayList<Category>();
		Category currentCategory;

		for (int i = 0; i < categories.size(); i++) {

			currentCategory = categories.get(i);
			if (!currentCategory.isDeleted()) {
				userCategories.add(currentCategory);
			}
		}
		
		return userCategories;

	}

	/**
	 * builds a list of strings of the model's categories' names
	 * 
	 * @return Returns a list of categories
	 */
	public List<String> getAllCategoryNames() {
		final List<String> categoryNames = new ArrayList<String>();

		for (Category cat : this.getAllcategories()) {
			categoryNames.add(cat.getName());
		}
		
		return categoryNames;
	}

	/**
	 * builds a list of strings of the model's categories' names for non-deleted
	 * categories
	 * 
	 * @return returns a list of categories
	 */
	public List<String> getAllNondeletedCategoryNames() {
		final List<String> categoryNames = new ArrayList<String>();

		for (Category cat : this.getAllcategories()) {
			if (!cat.isDeleted()) {
				categoryNames.add(cat.getName());
			}
		}
		
		return categoryNames;
	}

	/**
	 * Builds A list of categories based on the list of category names given
	 * 
	 * @param categoryNames
	 * 
	 * @return returns a list of categories
	 */
	public List<Category> getCategoriesFromListOfNames(
			List<String> categoryNames) {
		final List<Category> categories = new ArrayList<Category>();

		for (String categoryName : categoryNames) {
			categories.add(this.getCategory(categoryName));
		}
		this.fireContentsChanged(this, 0, 0);
		return categories;
	}

	/**
	 * Sets all categories hasFilter to false
	 * 
	 * @return the requirements held within the category model.
	 */
	public void setAllCategoriesNonFilter() {
		final List<Category> categories = this.getAllNondeletedCategoriesAsFilters();

		for (Category cat : categories) {
			cat.setHasFilter(false);
			UpdateCategoryController.getInstance().updateCategory(cat);
		}
		this.fireContentsChanged(this, 0, 0);
	}
}
