package softDevExam;

import javax.ws.rs.Path;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

@Path("test")
public class TestResource {
    @GET
    @Path("/working")
    public Response getWorkingState() {
        return Response.ok().build();
    }
}
