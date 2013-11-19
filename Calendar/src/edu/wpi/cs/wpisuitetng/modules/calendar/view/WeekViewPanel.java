package edu.wpi.cs.wpisuitetng.modules.calendar.view;

import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WeekViewPanel extends JPanel {
	
	// Millis for day in Calendar class
	// Going to use this to calculate first day of week
	private final static long ONE_DAY = 86400000; 
	
	private DayView dayOne;
	private DayView dayTwo;
	private DayView dayThree;
	private DayView dayFour;
	private DayView dayFive;
	private DayView daySix;
	private DayView daySeven;
	
	private final JButton nextWeek;
	private final JButton prevWeek;
	
	private Calendar shiftWeek; 

	/**
	 * Create the panel.
	 */
	public WeekViewPanel() {
		
		shiftWeek = Calendar.getInstance();
		
	    int day = shiftWeek.get(Calendar.DAY_OF_YEAR); 
	     // While loop through the week to obtain the first day of the week
	     // Why is this a thiiiiiiiiiiiiingggggggg
	    while(shiftWeek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	         shiftWeek.setTimeInMillis(shiftWeek.getTimeInMillis() - ONE_DAY);  
	    }  
		
		JPanel weekContainer = new JPanel();
		JPanel buttonContainer = new JPanel();

		weekContainer.setLayout(new GridLayout(0, 7, 0, 0));
		buttonContainer.setLayout(new GridLayout(0, 2, 0, 0));
		
		// Just set the days to the calculated week above
		// Maybe I should have kept an array?
		
		dayOne = new DayView();
		dayOne.refreshDay(shiftWeek);
		
		dayTwo = new DayView();
		
		shiftWeek.add(Calendar.DATE, 1);
		dayTwo.refreshDay(shiftWeek);
		
		dayThree = new DayView(); 
		shiftWeek.add(Calendar.DATE, 1);
		dayThree.refreshDay(shiftWeek);
		
		dayFour = new DayView();
		shiftWeek.add(Calendar.DATE, 1);
		dayFour.refreshDay(shiftWeek);
		
		dayFive = new DayView();
		shiftWeek.add(Calendar.DATE, 1);
		dayFive.refreshDay(shiftWeek);
		
		daySix = new DayView();
		shiftWeek.add(Calendar.DATE, 1);
		daySix.refreshDay(shiftWeek);
		
		daySeven = new DayView();
		shiftWeek.add(Calendar.DATE, 1);
		daySeven.refreshDay(shiftWeek);
		
		nextWeek = new JButton(">");
		nextWeek.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changeWeek(1);
			}
		});
		prevWeek = new JButton("<");
		prevWeek.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				changeWeek(-1); 
			}
		});
		
		buttonContainer.add(prevWeek);
		buttonContainer.add(nextWeek);
		
		setLayout(new MigLayout("", "[626px,grow]", "[29.00px][grow]"));

		weekContainer.add(dayOne);
		weekContainer.add(dayTwo);
		weekContainer.add(dayThree);
		weekContainer.add(dayFour);
		weekContainer.add(dayFive);
		weekContainer.add(daySix);
		weekContainer.add(daySeven);
		
		this.add(buttonContainer, "cell 0 0,alignx center"); 
		this.add(weekContainer, "cell 0 1,grow");
		weekContainer.setVisible(true);

	}
	
	
	// Given an integer x, if the x is negative
	// All collected day views will be updated 
	// To display the previous week, and a positive
	// Will display the next week
	private void changeWeek(int x) {
		int dayWeight;
		if (x > 0) {
			dayWeight = 1;
		}
		else {
			dayWeight = -13; 
		}
		shiftWeek.add(Calendar.DATE, dayWeight);
		dayOne.refreshDay(shiftWeek);
		shiftWeek.add(Calendar.DATE, 1);
		dayTwo.refreshDay(shiftWeek);
		shiftWeek.add(Calendar.DATE, 1);
		dayThree.refreshDay(shiftWeek);
		shiftWeek.add(Calendar.DATE, 1);
		dayFour.refreshDay(shiftWeek);
		shiftWeek.add(Calendar.DATE, 1);
		dayFive.refreshDay(shiftWeek);
		shiftWeek.add(Calendar.DATE, 1);
		daySix.refreshDay(shiftWeek);
		shiftWeek.add(Calendar.DATE, 1);
		daySeven.refreshDay(shiftWeek);
	}

}