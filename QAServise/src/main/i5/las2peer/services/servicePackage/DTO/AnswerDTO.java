package i5.las2peer.services.servicePackage.DTO;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Marv on 05.11.2014.
 */
public class AnswerDTO extends PostDTO {

    @Expose
    private int rating;
    @Expose
    private long idQuestion;

    public AnswerDTO(long id, Date timestamp, String text, long userId, int rating, long idQuestion) {
        super(id, timestamp, text, userId);
        this.rating = rating;
        this.idQuestion = idQuestion;
    }

    public AnswerDTO(){
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int  rating) {
        this.rating = rating;
    }

    public long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(long idQuestion) {
        this.idQuestion = idQuestion;
    }

}
