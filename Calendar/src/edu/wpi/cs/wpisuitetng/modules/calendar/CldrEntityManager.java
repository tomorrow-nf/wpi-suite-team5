package edu.wpi.cs.wpisuitetng.modules.calendar;

import java.util.List;
import java.util.HashMap;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

/** The entity mananger for Calendar events.
 * 
 * @author cporell
 * 
 *
 */
public class CldrEntityManager implements EntityManager<EventCldr>{
	Data db;
	public CldrEntityManager(Data db){
		this.db = db;
	}
	
	//Methods for saving, updating, etc, will be called via IO ********
	
	@Override
	public void save(Session s, EventCldr model) throws WPISuiteException {
		db.save(model);
		System.out.println("Events saved.");
	}
	
	//Updates a Calendar using the content input.
	//Because of the occasional quirkiness of db4o, we delete the old Calendar and save the updated one as a new Cldr.
	public EventCldr updateCldr(Session s, EventCldr content) throws WPISuiteException{
		List<Model> oldEventData = db.retrieve(Calendar.class, null, 0);
		if(oldEventData.size() < 1){
			//Calendar existingCalendar = (Calendar)oldEventData.get(0);
			// ^^ We probably don't need this.
			db.save(content);
		}
		if((oldEventData.size() >= 1) && (content!=null)){
			db.delete(oldEventData);
			db.save(content);
		}
		else {
			db.save(content);
		}
		System.out.println("Events saved.");
		return content;
	}
	
	public EventCldr retrieveCldr(Session s, EventCldr content) throws WPISuiteException{
		List<Model> cldrData = db.retrieve(Calendar.class, null, 0);
		EventCldr cldr = (EventCldr) cldrData.get(0);
		return cldr;
	}
	
	public void deleteCldr(Session s, EventCldr content) throws WPISuiteException{
		db.delete(content);
		return;
	}
	
	@Override
	public EventCldr makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCldr[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCldr[] getAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCldr update(Session s, String content) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

