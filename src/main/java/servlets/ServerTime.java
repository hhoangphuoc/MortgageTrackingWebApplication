package servlets;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;

/** Example of a Servlet that gets an ISBN number and returns the book price
 */
@Api(value = "Time")
@Path("time")
public class ServerTime {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getTime()
  {
    Long now = new Date().getTime();
    return Long.toString(now);
  }
  
}
