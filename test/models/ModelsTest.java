package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class ModelsTest extends WithApplication{
	@Before
	public void setup(){
		start(fakeApplication(inMemoryDatabase()));
	}
	
	@Test
	public void createAndRetrieveUser(){
		
		new User("bob@gmail.com", "Bob", "secret").save();
		User bob = User.find.where().eq("email", "bob@gmail.com").findUnique();
		assertNotNull(bob);
		assertEquals("Bob", bob.name);
	}
	
	@Test
	public void tryAuthenticateUser(){
		new User("bob@gmail.com", "Bob", "secret").save();
		
		assertNotNull(User.authenticate("bob@gmail.com","secret"));
		assertNull(User.authenticate("bobd@gmail.com","secret"));
		assertNull(User.authenticate("bob@gmail.com","secrer"));
	}
	
	@Test
	public void findPeopleInvolved(){
		new User("bob@gmail.com", "Bob", "secret").save();
		new User("jane@gmail.com", "Jane", "secret").save();
		
		Project.create("Play 2","play","bob@gmail.com");
		Project.create("Play 1","play","jane@gmail.com");
		
		List<Project> projects = Project.findInvolving("bob@gmail.com");
		assertEquals(1, projects.size());
		assertEquals("Play 2", projects.get(0).name);
		
	}
	
	@Test
    public void findTodoTasksInvolving() {
	
		User bob = new User("bob@gmail.com", "Bob", "secret");
        bob.save();

        Project project = Project.create("Play 2", "play", "bob@gmail.com");
        Task t1 = new Task();
        t1.title = "Write tutorial";
        t1.assignedTo = bob;
        t1.done = true;
        t1.save();
        
        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();
        
        List<Task> results = Task.findTodoInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Release next version", results.get(0).title);
	}
	
}

