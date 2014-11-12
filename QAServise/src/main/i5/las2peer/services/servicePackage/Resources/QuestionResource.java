package i5.las2peer.services.servicePackage.Resources;

import i5.las2peer.services.servicePackage.DTO.AnswerDTO;
import i5.las2peer.services.servicePackage.DTO.QuestionDTO;
import i5.las2peer.services.servicePackage.Exceptions.CantDeleteException;
import i5.las2peer.services.servicePackage.Exceptions.CantInsertException;
import i5.las2peer.services.servicePackage.Manager.ManagerFacade;
import com.google.gson.Gson;
import i5.las2peer.restMapper.HttpResponse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Marv on 12.11.2014.
 */
public class QuestionResource extends AbstractResource {


    public QuestionResource(Connection conn) {
        super(conn);
    }

    public HttpResponse getQuestionCollection(String token) {
        HttpResponse response = new HttpResponse("");
        try {
            List<QuestionDTO> questions = ManagerFacade.getInstance().getQuestionList(token, conn);

            Gson gson = new Gson();
            String json = gson.toJson(questions);
            response = new HttpResponse(json);
            response.setStatus(200);

        } catch (SQLException e) {
           response.setStatus(500);
        }
        return response;
    }

    public HttpResponse addQuestion(String token, QuestionDTO question) {
        HttpResponse res;
        try {
            Long generatedId = ManagerFacade.getInstance().addQuestion(token, conn, question);

            res = new HttpResponse(generatedId.toString());
            res.setStatus(200);
            return res;
        } catch (SQLException e) {
           res = new HttpResponse("");
            res.setStatus(500);
        } catch (CantInsertException e) {
            res = new HttpResponse("");
            res.setStatus(500);
        }

        res = new HttpResponse("");
        res.setStatus(500);
        return res;
    }

    public HttpResponse getQuestion(String token, long questionId) {
        HttpResponse res;
        try {
            QuestionDTO question = ManagerFacade.getInstance().getQuestion(token, conn, questionId);
            if(question == null) {
                res = new HttpResponse("null");
                res.setStatus(404);
                return res;
            }
            Gson gson = new Gson();

            res = new HttpResponse(gson.toJson(question));
            res.setStatus(200);

            return  res;
        } catch (SQLException e) {
            res = new HttpResponse("not found");
            res.setStatus(500);
            return  res;
        }
    }

    public HttpResponse editQuestion(String token, QuestionDTO question) {
        throw new NotImplementedException();
    }

    public HttpResponse deleteQuestion(String token, long questionId) {
        HttpResponse res = new HttpResponse("");

        try {
            ManagerFacade.getInstance().deleteQuestion(token, conn, questionId);
            res.setStatus(200);
        } catch (SQLException e) {
            res.setStatus(500);
        } catch (CantDeleteException e) {
            res.setStatus(500);
        }

        return res;
    }

    public HttpResponse getAnswersToQuestion(String token,long questionId) {
        throw new NotImplementedException();
    }

    public HttpResponse addAnswerToQuestion(String token, AnswerDTO answer) {
        throw new NotImplementedException();
    }

    public HttpResponse getBookmarkUsersToQuestion(String token, long questionId) {
    	throw new NotImplementedException();
    }

}
