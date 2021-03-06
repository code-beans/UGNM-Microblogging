package i5.las2peer.services.servicePackage.Resources;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import i5.las2peer.services.servicePackage.DTO.QuestionDTO;
import i5.las2peer.services.servicePackage.DTO.UserDTO;
import i5.las2peer.services.servicePackage.Exceptions.CantFindException;
import i5.las2peer.services.servicePackage.Exceptions.CantInsertException;
import i5.las2peer.services.servicePackage.Exceptions.CantUpdateException;
import i5.las2peer.services.servicePackage.Exceptions.NotWellFormedException;
import i5.las2peer.services.servicePackage.Manager.ManagerFacade;
import i5.las2peer.restMapper.HttpResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

/**
 * Created by Marv on 12.11.2014.
 */
public class UserResource extends AbstractResource {

    public UserResource(Connection conn) {
        super(conn);
    }

    public HttpResponse getUser(long userId) {
        if(userId == 0)
            return new HttpResponse("You have to be logged in to retrieve your profile.", 401);
        try {
            UserDTO user = ManagerFacade.getInstance().getUser(conn, userId);
            return new HttpResponse(getDefaultGson().toJson(user), 200);
        } catch (SQLException e) {
           return new HttpResponse(e.toString(), 500);
        } catch (CantFindException e) {
            return new HttpResponse(e.toString(), 404);
        }
    }

    public HttpResponse editUser(long userId, String content) {
        if(userId == 0)
            return new HttpResponse("You have to be logged in to change your profile.", 401);
        try {
            UserDTO data = new Gson().fromJson(content, UserDTO.class);
            ManagerFacade.getInstance().editUser(conn, userId, data);
            return new HttpResponse("Profile successfully changed.", 200);
        } catch (JsonParseException e) {
            return new HttpResponse(e.toString(), 400);
        } catch (SQLException e) {
            return new HttpResponse(e.toString(), 500);
        } catch (CantUpdateException e) {
            return new HttpResponse(e.toString(), 500);
        }
    }

    public HttpResponse bookmarkedQuestions(long userId) {
        if(userId == 0)
           return new HttpResponse("You have to be logged in to favour a question.", 401);
        try {
            String json = getDefaultGson().toJson(ManagerFacade.getInstance().bookmarkedQuestions(conn, userId));
            return new HttpResponse(json, 200);
        } catch (SQLException e) {
            return new HttpResponse(e.toString(), 500);
        }
    }

    public HttpResponse getExpertiseQuestions(long userId) {
        if(userId == 0)
            return new HttpResponse("You have to be logged in to favour a question.", 401);
        try {
            String json = getDefaultGson().toJson(ManagerFacade.getInstance().getExpertiseQuestions(conn, userId));
            return new HttpResponse(json, 200);
        } catch (SQLException e) {
            return new HttpResponse(e.toString(), 500);
        }
    }

    public HttpResponse addBookmark(long userId, long questionId) {
        if(userId == 0)
            return new HttpResponse("You have to be logged in to favour a question.", 401);
        try {
            ManagerFacade.getInstance().bookmark(conn, userId, questionId);
            return new HttpResponse("Bookmark added.", 200);
        } catch (CantFindException e) {
            return new HttpResponse(e.toString(), 404);
        } catch (SQLException e) {
            return new HttpResponse(e.toString(), 500);
        } catch (CantInsertException e) {
            return new HttpResponse(e.toString(), 500);
        }
    }

    // function that adds a user if he does not exist yet
    public HttpResponse registerUser(UserDTO userDTO) {

        try {
            if(ManagerFacade.getInstance().registerUser(conn, userDTO))
                return new HttpResponse("User is registered in User-table.", 200);
            else
                return new HttpResponse("User is no registered in User-table.", 500);
        } catch (SQLException e) {
            return new HttpResponse(e.toString(), 500);
        } catch (CantInsertException e) {
            return new HttpResponse(e.toString(), 500);
        }
    }
}
