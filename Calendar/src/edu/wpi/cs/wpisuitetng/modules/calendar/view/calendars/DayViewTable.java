/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Team _ 
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.calendar.view.calendars;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.calendar.globalButtonVars.GlobalButtonVars;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.DateInfo;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.EventFilter;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.EventSorter;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.category.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.entry.Event;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.entry.EventModel;

/**
 * 
 * 
 * @author Team _
 * 
 * @version $Revision: 1.0 $
 * DayViewTable manages the application of building events for display in the DayView window
 */
public class DayViewTable extends JTable {

	private static final long serialVersionUID = -4325747905323115241L;
	private List<Event> events; // current day's events to display
	boolean isUpdated; // whether or not the event list is updated
	DayView dayView; // instance of day view for updating current events

	private final List<EventRectangle> rectangles;

	/**
	 * @param defaultTableModel
	 */
	public DayViewTable(DefaultTableModel defaultTableModel) {
		super(defaultTableModel);

		isUpdated = false;

		rectangles = new ArrayList<EventRectangle>();
	}

	/**
	 * Paint the DayViewTable with events on top
	 * 
	 * @see javax.swing.JTable#paintComponents(Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (!isUpdated) {
			updateEvents();

			// Take updated events and put them in rectangles
			if (rectangles.size() > 0) {
				rectangles.clear();
			}

			for (int i = 0; i < events.size(); i++) {
				rectangles.add(new EventRectangle(events.get(i)));
			}

			isUpdated = true;
		}
		updateRectangles();
		paintRectangles(g);
	}

	/**
	 * Iterate through the rectangles and paint them
	 * 
	 * @param g
	 *            the graphics class to use in rendering
	 */
	private void paintRectangles(Graphics g) {
		for (int i = 0; i < rectangles.size(); i++) {
			paintEventRectangle(g, rectangles.get(i));
		}
	}

	/**
	 * Trim a string into a fitting length and append '...' to the end
	 * 
	 * @param s
	 *            the Original unedited string
	 * @param metric
	 *            the statistics of the font being used
	 * @param maxWidth
	 *            the maximum width allowed in pixels
	 * 
	 * @return the trimmed string
	 */
	public String trimString(String s, FontMetrics metric, int maxWidth) {

		// If string already fits, return it
		if (metric.stringWidth(s) < maxWidth) {
			return s;
		}

		int i;
		// one by one, trim the last characters off until the string fits
		for (i = s.length() - 1; i > 0; i--) {

			if (metric.stringWidth(s.substring(0, i)) < maxWidth) {
				break;
			}

		}

		// if < 3 characters trimmed, replace last 3 characters with "..."
		if (i > s.length() - 3) {
			return (s.substring(0, Math.max(1, s.length() - 3)) + "...");
		} else {
			return (s.substring(0, Math.max(1, i - 3)) + "...");
		}
	}

	/**
	 * 
	 * @return the day's events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * 
	 * @param isUpdated
	 *            true if the DayView is updated, false otherwise
	 */
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	/**
	 * Update the stored events by retrieving and sorting them
	 */
	public void updateEvents() {
		final DateInfo eventDay = new DateInfo(dayView.getRealDay());
		if (GlobalButtonVars.getInstance().isPersonalView()
				&& GlobalButtonVars.getInstance().isTeamView()) {
			events = EventModel.getInstance().getUserEvents(
					ConfigManager.getConfig().getUserName(),
					eventDay.getYear(), eventDay.getMonth(), eventDay.getDay());
		} else if (GlobalButtonVars.getInstance().isPersonalView()) {
			events = EventModel.getInstance().getPersonalEvents(
					ConfigManager.getConfig().getUserName(),
					eventDay.getYear(), eventDay.getMonth(), eventDay.getDay());

		} else if (GlobalButtonVars.getInstance().isTeamView()) {
			events = EventModel.getInstance().getTeamEvents(
					ConfigManager.getConfig().getUserName(),
					eventDay.getYear(), eventDay.getMonth(), eventDay.getDay());
		}
		//Filter View's events by applied filters
		events = EventFilter.filterEventsByCategory(events, CategoryModel
				.getInstance().getAllNondeletedCategoriesAsFilters());
		events = EventSorter.sortEventsByDate(events);
	}

	/**
	 * Paint a single EventRectangle
	 * 
	 * @param g
	 *            The Graphics object to handle the painting
	 * @param rect
	 *            An EventRectangle to draw
	 */
	void paintEventRectangle(Graphics g, EventRectangle rect) {

		// Convert Calendar to DateInfo
		final DateInfo displayedDay = new DateInfo(dayView.getRealDay());

		final int CURVE_SIZE = 16; // Size of curves for rounded rectangles

		final int stringHeight; // height of string being printed
		String printString; // string to print for Event
		final Color textColor; // color to print text for an event

		final Event e = rect.getEvent();
		final int x = rect.getX();
		final int y = rect.getY();
		final int width = rect.getWidth();
		final int height = rect.getHeight();

		// draw the event rectangle
		g.setColor(e.getColor());
		g.fillRoundRect(x, y, width, height, CURVE_SIZE, CURVE_SIZE);

		// Determine and set text/border color
		textColor = new Color(Math.max(e.getColor().getRed() - 120, 0),
				Math.max(e.getColor().getGreen() - 120, 0), Math.max(e
						.getColor().getBlue() - 120, 0));

		g.setColor(textColor);
		g.drawRoundRect(x, y, width, height, CURVE_SIZE, CURVE_SIZE);

		stringHeight = g.getFontMetrics().getMaxAscent();

		printString = e.getName();

		// if event started before today, preface the string with (cont'd)
		displayedDay.setHalfHour(0);
		if (e.getStartDate().compareTo(displayedDay) < 0) {
			printString = "(cont'd) " + printString;
		}

		// trim event name to fit as necessary
		printString = trimString(printString, g.getFontMetrics(), width);

		

		g.drawString(printString, x + 2, y + stringHeight);

	}

	/**
	 * Update the list of rectangles according to updated events Important:
	 * rectangles and events should have a 1:1 correspondence before entering
	 * this function
	 */
	public void updateRectangles() {

		// Convert Calendar to DateInfo
		final DateInfo displayedDay = new DateInfo(dayView.getRealDay());

		// Set the half-hour field to 0 to signify the beginning of the day
		displayedDay.setHalfHour(0);

		final int X_OFFSET = getColumnModel().getColumn(0).getWidth();
		final int Y_OFFSET = 0;

		// Maximum width an event can be
		final int MAX_WIDTH = getColumnModel().getColumn(1).getWidth() - 1;
		final int ROW_HEIGHT = getRowHeight();
		int x;
		int y;

		// Actual height and width values for the current event
		int width;
		int height;

		/* number of events already occurring in a given slot */
		final int[] numPriorEvents = new int[48];
		final int PRIOR_EVENT_WIDTH = 8; // Number of pixels to reserve for each
											// prior event

		// Set number of prior events to 0 for all slots
		for (int i = 0; i < numPriorEvents.length; i++) {
			numPriorEvents[i] = 0;
		}

		Event e;// the current event
		DateInfo startDate;
		DateInfo endDate;
		int numEventsInRow; // Number of events in current row
		EventRectangle r;
		int startHour;
		for (int i = 0; i < events.size(); i++) {
			e = events.get(i);

			startDate = e.getStartDate();
			endDate = e.getEndDate();

			// Set the half-hour field to 0 to signify the beginning of the day
			displayedDay.setHalfHour(0);

			// if event started before today, begin drawing at the top
			if (e.getStartDate().compareTo(displayedDay) < 0) {
				y = Y_OFFSET;
				startHour = 0;
			} else {
				y = Y_OFFSET + (startDate.getHalfHour() * ROW_HEIGHT);
				startHour = startDate.getHalfHour();
			}

			numEventsInRow = 1;
			for (int j = i + 1; j < events.size(); j++) {
				// check if date has same start time, or begins before the
				// current day
				if (events.get(j).getStartDate().equals(startDate)
						|| events.get(j).getStartDate().compareTo(displayedDay) < 0) {
					numEventsInRow++;
				} else { // since events are sorted, break
							// at the first different start time
					break;
				}
			}

			// calculate the width of all events in the row
			width = (MAX_WIDTH - (numPriorEvents[startHour] * PRIOR_EVENT_WIDTH))
					/ numEventsInRow;

			displayedDay.setHalfHour(48);

			// Add 1 to each row that this event occupies for future events
			// If this runs into the next day, it's the same as occupying all
			// remaining rows
			if (e.getEndDate().compareTo(displayedDay) >= 0) {
				for (int j = startHour + 1; j < 48; j++) {
					numPriorEvents[j]++;
				}
			} else {
				for (int j = startHour + 1; j < endDate.getHalfHour(); j++) {
					numPriorEvents[j]++;
				}
			}

			// draw all events in row
			for (int j = 0; j < numEventsInRow; j++) {
				e = events.get(i + j);
				r = rectangles.get(i + j);

				endDate = e.getEndDate();

				// Calculate offset of x depending on number of prior events
				// and also which event in the row this is
				x = X_OFFSET + (numPriorEvents[startHour] * PRIOR_EVENT_WIDTH)
						+ (width * j);

				// if event ends after current day, draw to the bottom
				if (endDate.compareTo(displayedDay) >= 0) {
					height = Y_OFFSET + (48 * ROW_HEIGHT) - y;
				} else {
					height = (Y_OFFSET + (endDate.getHalfHour() * ROW_HEIGHT))
							- y;
				}

				// Apply the calculated bounds to the selected rectangle
				r.setX(x);
				r.setY(y);
				r.setWidth(width);
				r.setHeight(height);
				rectangles.set(i + j, r);
			}

			// increment i to skip over the drawn events
			i += numEventsInRow - 1;

		}
	}

	/**
	 * Determines whether or not the index of a cell is visible
	 * 
	 * @param rowIndex
	 *            the index of the cell in question
	 * 
	 * @return true if the cell is visible, false otherwise
	 */
	public boolean isCellVisible(int rowIndex) {
		if (!(getParent() instanceof JViewport)) {
			return false;
		}
		final JViewport viewport = (JViewport) getParent();
		final Rectangle rect = getCellRect(rowIndex, 1, true);
		final Point pt = viewport.getViewPosition();
		rect.setLocation(rect.x - pt.x, rect.y - pt.y);
		return new Rectangle(viewport.getExtentSize()).contains(rect);
	}

	/**
	 * 
	 * @param dayView
	 *            the instance of the day view holding this table
	 */
	public void setDayView(DayView dayView) {
		this.dayView = dayView;
	}

	/**
	 * 
	 * @param _x
	 * @param _y
	 * 
	 * @return rectangle
	 */
	public EventRectangle getRectangle(int _x, int _y) {
		for (int i = rectangles.size() - 1; i >= 0; i--) {
			if (rectangles.get(i).isAtPoint(_x, _y)) {
				return rectangles.get(i);
			}
		}
		return null;
	}

}
