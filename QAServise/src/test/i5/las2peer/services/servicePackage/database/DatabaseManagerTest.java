package i5.las2peer.services.servicePackage.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import i5.las2peer.services.servicePackage.DTO.*;
import i5.las2peer.services.servicePackage.ServiceClass;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private static BasicDataSource dataSource = null;

    private static void createFreshLocalTables() throws SQLException {
        try {
            //String executeSqlCommand = "mysql -h 127.0.0.1 -P 3306 -u root --password= ugnm1415g2 < /tmp/file.sql";
            //System.out.println(executeSqlCommand);

            ProcessBuilder ps=new ProcessBuilder("mysql","--host=127.0.0.1","--port=3306","--user=root","--password=","ugnm1415g2");
            //ProcessBuilder ps=new ProcessBuilder("mysql","--host=buche.informatik.rwth-aachen.de","--port=3306","--user=ugnm1415g2","--password=bf3244PRAN","ugnm1415g2");
            //ProcessBuilder ps=new ProcessBuilder("ls", "-l", "./src/test/i5/las2peer/services/servicePackage/database");

            ps.redirectErrorStream(true);
            ps.redirectInput(new File("./src/test/i5/las2peer/services/servicePackage/database/testdatabase.sql"));

            System.out.println("Start local mysql-program...");
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = pr.waitFor();

            in.close();

            System.out.println("Exited with error code " + exitVal);
            assertEquals(exitVal, 0);

        } catch (Exception e) {
            throw new SQLException(e.toString());
        }
    }

    public static Date getDummyDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2000-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object[] subArray(Object[] all, int... indices) {
        if(indices.length == 0)
            return all;
        else {
            LinkedList<Object> result = new LinkedList<>();
            for (int i : indices) {
                result.add(all[i]);
            }
            return result.toArray((Object[]) Array.newInstance(all[0].getClass(), indices.length));
        }
    }

    public static HashtagDTO[] getTestHashtags(int... indices) {
        return (HashtagDTO[]) subArray( new HashtagDTO[] {
                new HashtagDTO(1,"Java"),
                new HashtagDTO(2,"Assembler"),
                new HashtagDTO(3,"For-Loop"),
                new HashtagDTO(4,"Analysis"),
                new HashtagDTO(5,"Polynome"),
                new HashtagDTO(6,"Lagrange-Restglied")
        }, indices);
    }

    public static QuestionDTO[] getTestQuestions(int... indices) throws ParseException {
        return (QuestionDTO[]) subArray( new QuestionDTO[] {
                new QuestionDTO(1, getDummyDate(), "How do I write a for-loop?", 1, Arrays.asList(getTestHashtags(0,2)), 1),
                new QuestionDTO(2, getDummyDate(), "Where can I find the toilet?", 4, new LinkedList<HashtagDTO>(), 2),
                new QuestionDTO(4, getDummyDate(), "How does the JFrame-constructor work?", 2, Arrays.asList(getTestHashtags(0)), 1)
        }, indices);
    }

    public static AnswerDTO[] getTestAnswers(int... indices) {
        return (AnswerDTO[]) subArray( new AnswerDTO[] {
                new AnswerDTO(3, getDummyDate(), "In the building E2, first floor.", 2, 100, 2),
                new AnswerDTO(5, getDummyDate(), "I think he is right", 5, 0, 2),
                new AnswerDTO(6, getDummyDate(), "You should google for it.", 3, 0, 4),
                new AnswerDTO(7, getDummyDate(), "I want to recherche it...", 5, 0, 4),
                new AnswerDTO(8, getDummyDate(), "I already googled, but couldn'nt find anything :(", 2, 0, 4)
        }, indices);
    }

    public static UserDTO[] getTestUsers(int... indices) {
        return (UserDTO[]) subArray( new UserDTO[] {
                new UserDTO(1, 10, Arrays.asList(getTestHashtags(3, 4, 5))),
                new UserDTO(2, 10, Arrays.asList(getTestHashtags(3,4,5))),
                new UserDTO(3, 10, Arrays.asList(getTestHashtags(0,1,2))),
                new UserDTO(4, 10, Arrays.asList(getTestHashtags(0,2))),
                new UserDTO(5, 10, Arrays.asList(getTestHashtags(0,1,2,3,4,5)))
        }, indices);
    }

    public static Connection getTestTable() throws SQLException {
        if( dataSource == null ) {
            dataSource = new BasicDataSource();
            dataSource.setDefaultAutoCommit(true);
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
/*
            dataSource.setUsername("ugnm1415g2");
            dataSource.setPassword("bf3244PRAN");
            dataSource.setUrl("jdbc:mysql://buche.informatik.rwth-aachen.de:3306/ugnm1415g2");
*/
            dataSource.setUsername("root");
            dataSource.setPassword("");
            dataSource.setUrl("jdbc:mysql://localhost:3306/ugnm1415g2");

            dataSource.setValidationQuery("SELECT 1");
            dataSource.setDefaultQueryTimeout(1000);
            dataSource.setMaxConnLifetimeMillis(100000);
        }

        createFreshLocalTables();

        return dataSource.getConnection();
    }

    @Test
    public void connectionToLocalDatabase() throws Exception {
        Connection conn = getTestTable();
    }

    @Test
    public void testDatabaseConfiguration() throws Exception {
        ServiceClass s = new ServiceClass();
        Class c = ServiceClass.class;
        Field f;
        f = c.getDeclaredField("jdbcDriverClassName"); f.setAccessible(true);
        String jdbcDriverClassName = (String)f.get(s);
        f = c.getDeclaredField("jdbcLogin"); f.setAccessible(true);
        String jdbcLogin = (String)f.get(s);
        f = c.getDeclaredField("jdbcPass"); f.setAccessible(true);
        String jdbcPass = (String)f.get(s);
        f = c.getDeclaredField("jdbcUrl"); f.setAccessible(true);
        String jdbcUrl = (String)f.get(s);
        f = c.getDeclaredField("jdbcSchema"); f.setAccessible(true);
        String jdbcSchema = (String)f.get(s);

        DatabaseManager dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl,jdbcSchema);
        assertNotEquals(null, dbm.getConnection());
    }
}



