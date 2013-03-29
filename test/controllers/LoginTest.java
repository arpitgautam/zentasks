package controllers;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.List;
import java.util.Map;

import org.junit.*;

import play.libs.*;
import play.mvc.*;
import play.test.*;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;


public class LoginTest extends WithApplication {

	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
		Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

        // Insert users first
        Ebean.save(all.get("users"));
        Ebean.save(all.get("projects"));
        
        for(Object project: all.get("projects")) {
            // Insert the project/user relation
            Ebean.saveManyToManyAssociations(project, "members");
        }
        // Insert tasks
        Ebean.save(all.get("tasks"));

	}
	
	@Test
	public void authenticated() {
	    Result result = callAction(
	        controllers.routes.ref.Application.index(),
	        fakeRequest().withSession("email", "maxime@sample.com")
	    );
	    assertEquals(200, status(result));
	}  
	
	@Test
	public void notAuthenticated() {
	    Result result = callAction(
	        controllers.routes.ref.Application.index(),
	        fakeRequest()
	    );
	    assertEquals(303, status(result));
	    assertEquals("/login", header("Location", result));
	}
}
