package controllers;

import models.Project;
import models.Task;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render(
        		Project.finder.all(),
        		Task.find.all()));
    }
  
}
