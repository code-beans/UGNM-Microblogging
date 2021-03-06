package i5.las2peer.services.servicePackage.Manager;

import i5.las2peer.services.servicePackage.DTO.AnswerDTO;
import i5.las2peer.services.servicePackage.DTO.HashtagDTO;
import i5.las2peer.services.servicePackage.DTO.QuestionDTO;
import i5.las2peer.services.servicePackage.DTO.UserDTO;
import i5.las2peer.services.servicePackage.Exceptions.CantInsertException;
import i5.las2peer.services.servicePackage.database.DatabaseManagerTest;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.text.ParseException;

import static org.junit.Assert.*;

public class AnswerManagerTest extends AbstractManagerTest {

    private static AnswerManager manager;

    @BeforeClass
    public static void initClass() throws ParseException {
        manager = new AnswerManager();
    }


    @Test
    public void testUpvoteAnswer() throws Exception {
        manager.upvoteAnswer(conn, 3, 3);
        try {
            manager.upvoteAnswer(conn, 3, 3);
            fail("Double rating should lead to an exception.");
        }
        catch (CantInsertException e) {
            // oK
        }
        manager.upvoteAnswer(conn, 3, 4);
        assertEquals(
                DatabaseManagerTest.getTestUsers()[1].getElo() + 2,
                new UserManager().getElo(conn, 2)
        );
    }
}