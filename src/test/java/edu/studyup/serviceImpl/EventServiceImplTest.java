package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	

	@Test
	void testUpdateEventName_emptyName_badCase() {
		int eventID = 1; 
		String eventName = ""; 
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, eventName); 
		}); 
	}
	
	@Test
	void testUpdateEventName_longName_badCase() {
		int eventID = 1; 
		String eventName = "This is a very very very very loooong event name"; 
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, eventName); 
		}); 
	}
	
	@Test
	void testUpdateEventName_NameLength20_goodCase() throws StudyUpException{
		int eventId = 1; 
		String name = "This has length 20!!"; 
		eventServiceImpl.updateEventName(eventId, name); 
		assertEquals(name, DataStorage.eventData.get(eventId).getName());
		
	}
	
	@Test 
	void testGetActiveEvents_notCheckingActive_goodCase() {
		List<Event> futureEventList = new ArrayList<>(); 
		// Should fail because no checking for past events is done, and the
		// only event in the list is in the past
		Assertions.assertEquals(eventServiceImpl.getActiveEvents(), futureEventList);
	}
	
	//this test has a mixture of future and past events, and we only want to
	//return the events that are in the future. 
	@Test 
	void testGetActiveEvents_NotAllEventsFuture_badCase() {
		// add an event that is in the future and add it to the data storage
		Event futureEvent = new Event(); 
		futureEvent.setEventID(2); 
		//using deprecated constructor for ease of creation
		futureEvent.setDate(new Date(2020, 3, 23));
		futureEvent.setName("Event 2");
		Location location = new Location(-122, 37);
		futureEvent.setLocation(location);
		
		DataStorage.eventData.put(futureEvent.getEventID(), futureEvent); 
		
		List<Event> eventList = new ArrayList<>(); 
		eventList.add(futureEvent); 
	
		//test to make sure that only events that are in the future are included
		Assertions.assertEquals(eventList, eventServiceImpl.getActiveEvents()); 
	}
	
	//if the DataStorage has events that are all in the future, we want to make 
	//sure that all of them are returned
	@Test
	void testGetActiveEvents_OnlyFutureEvents_goodCase() {
		DataStorage.eventData.clear(); //clear the data storage to be able to add future events
		
		Event event1 = new Event(); 
		event1.setEventID(1); 
		event1.setDate(new Date(2020, 3, 23));
		event1.setName("Event 1");
		Location location = new Location(-122, 37);
		event1.setLocation(location);
		
		Event event2 = new Event(); 
		event2.setEventID(2); 
		event2.setDate(new Date(2020, 3, 24));
		event2.setName("Event 2");
		event2.setLocation(location);
		
		DataStorage.eventData.put(event1.getEventID(), event1); 
		DataStorage.eventData.put(event2.getEventID(), event2); 
		
		List<Event> activeEvents = new ArrayList<>();
		activeEvents.add(event1); 
		activeEvents.add(event2); 
		
		Assertions.assertEquals(activeEvents, eventServiceImpl.getActiveEvents());
	}
	
	@Test 
	void testGetPastEvents_goodCase() {
		int eventID = 1; 
		List<Event> pastEvents = new ArrayList<>(); 
		pastEvents.add(DataStorage.eventData.get(eventID)); 
		
		Assertions.assertEquals(pastEvents, eventServiceImpl.getPastEvents()); 
	}
	
	@Test
	void testGetPastEvents_hasAFutureEvent_goodCase() {
		// add a future event to data storage to test that only the 
		// future events are returned in this method
		int pastEventID = 1; 
		Event event1 = new Event(); 
		event1.setEventID(2); 
		event1.setDate(new Date(2020, 3, 23));
		event1.setName("Event 2");
		Location location = new Location(-122, 37);
		event1.setLocation(location);
		
		DataStorage.eventData.put(event1.getEventID(), event1); 
		
		List<Event> pastEvents = new ArrayList<>();
		pastEvents.add(DataStorage.eventData.get(pastEventID)); 
		
		Assertions.assertEquals(pastEvents, eventServiceImpl.getPastEvents());	
	}	
	
	@Test
	void testAddStudentToEvent_addOneStudent_goodCase() throws StudyUpException{
		int eventID = 1; 
		
		Student student = DataStorage.eventData.get(eventID).getStudents().get(0); 
		// create the new student that will be added to the event
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);

		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		eventStudents.add(student2); 
		
		
		Assertions.assertEquals(eventStudents, eventServiceImpl.addStudentToEvent(student2, eventID).getStudents());
	}
	
	// want to throw and exception when trying to add students
	// to an event that already has two students in it -- each event is 
	// only supposed to have a max of two students
	@Test 
	void testAddStudentToEvent_moreThan2StudentsAdded_badCase() throws StudyUpException {
		int eventID = 1; 
		
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);
		
		Student student3 = new Student();
		student2.setFirstName("Jacob");
		student2.setLastName("Doe");
		student2.setEmail("JacobDoe@email.com");
		student2.setId(3);
		
		eventServiceImpl.addStudentToEvent(student2, eventID); 
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student3, eventID); 
		}); 
		
	}
	
	//test that an exception is thrown when you try to query an event
	// that does not exist
	@Test 
	void testAddStudentToEvent_nullEvent_badCase() {
		int eventID = 3; 
		
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student2, eventID); 
		}); 
	}
		
	
	// test that adding a student to an event with no students in it 
	// currently works as expected.
	@Test 
	void testAddStudentToEvent_emptyEvent_goodCase() throws StudyUpException{
		// create a new event, with no students in that event
		int eventID = 2; 
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);
		
		DataStorage.eventData.put(eventID, event); 
		List<Student> students = new ArrayList<>(); 
		students.add(student2); 
		
		Assertions.assertEquals(students,
				eventServiceImpl.addStudentToEvent(student2, eventID).getStudents()); 
	}
	
	@Test
	void testDeleteEvent_goodCase() {
		int eventID = 1; 
		eventServiceImpl.deleteEvent(eventID); 
		// check that the event storage is empty
		Assertions.assertEquals(0, DataStorage.eventData.size()); 
	}
	
	@Test 
	void testDeleteEvent_eventDoesNotExist_badCase() {
		int eventID = 4; 
		Assertions.assertEquals(null,
				eventServiceImpl.deleteEvent(eventID)); 
	}
	
}
