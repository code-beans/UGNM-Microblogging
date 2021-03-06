package i5.las2peer.services.servicePackage.Manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import i5.las2peer.services.servicePackage.database.DatabaseManagerTest;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import static org.junit.Assert.*;

public class AbstractManagerTest {
    protected Connection conn;


    @Before
    public void setUp() throws Exception {
        conn = DatabaseManagerTest.getTestTable();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void mockConnectionTest() throws Exception {

    }

    // From Jackson javadoc-documentation: "used for POJO fields (note: does not apply to Map serialization!)"
    protected String normalize(Object serializable) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY, true);
        if (serializable instanceof String) {
            // In JSON-format
            serializable = om.readValue((String)serializable, Object.class);
        }
        if ((serializable instanceof Map) && !(serializable instanceof TreeMap)) {
            serializable = new TreeMap((Map) serializable);
        }
        return om.writeValueAsString(serializable);
    }
}