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
	
	
	// Ideas:
	// - Returns event as active when it isn't / vv
	// - Not even checking if event is active
	
	@Test 
	void testGetActiveEvents_notCheckingActive_badCase() {
		
		
		//Create Event3
//		Event event3 = new Event();
//		event3.setEventID(3);
//		// Create known past event
//		event3.setDate(new Date(1995, 7, 9));
//		event3.setName("Event 3");
//		Location location = new Location(-122, 37);
//		event.setLocation(location);
//		List<Student> eventStudents = new ArrayList<>();
//		eventStudents.add(student);
//		event.setStudents(eventStudents);
		
//		DataStorage.eventData.put(event3.getEventID(), event3);
		
		
//		List<Event> pastEventList = new ArrayList<>();
//		pastEventList.add(event3);
		
		// Compare to empty list
		List<Event> futureEventList = new ArrayList<>();

		
		// Should fail because no checking for past events is done
		Assertions.assertEquals(eventServiceImpl.getActiveEvents(), futureEventList);
	}

}
