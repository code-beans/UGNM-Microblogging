package Project.Resources;

import i5.las2peer.services.servicePackage.DTO.UserDTO;
import com.google.gson.Gson;
import i5.las2peer.restMapper.HttpResponse;
import i5.las2peer.restMapper.annotations.*;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Created by Marv on 05.11.2014.
 * This class is an implementation of a restful service responsible for all interactions concerning users
 */

@Path("user")
@Version("0.1")
public class UserResource extends RestResource{



    @GET
    @Path("getUser/{userId}")
    public HttpResponse getUser(@PathParam("userId") long userId) {
        HttpResponse response;
        try {
            UserDTO user = mf.getUser(getConnection(), userId);
            Gson gson = new Gson();
            String json = gson.toJson(user);
            LOGGER.log(Level.INFO, "Request: " + json);
            response = new HttpResponse(json);
            //successful
            response.setStatus(200);
        } catch (SQLException e) {
            response = new HttpResponse("");
            response.setStatus(404);
        }
        return response;
    }



}
