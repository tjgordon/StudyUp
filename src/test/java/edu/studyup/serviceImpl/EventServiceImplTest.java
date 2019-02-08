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
}
