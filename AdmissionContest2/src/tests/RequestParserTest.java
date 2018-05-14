package tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class RequestParserTest {

    RequestParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new RequestParser();
    }

    @Test
    public void parse() {
        RequestParser localParser = new RequestParser();
        try {
            JSONObject input = new JSONObject("{\n" +
                    "\t\"operation\": \"select\",\n" +
                    "\t\"from\": \"students\",\n" +
                    "\t\"cond1\": {\n" +
                    "\t\t\"key\": \"id\",\n" +
                    "\t\t\"operator\": \"=\",\n" +
                    "\t\t\"value\": \"1\"\n" +
                    "\t\t},\n" +
                    "\t\"cond2\": {\n" +
                    "\t\t\"key\": \"first_name\",\n" +
                    "\t\t\"operator\": \"=\",\n" +
                    "\t\t\"value\": \"Mircea\"\n" +
                    "\t\t},\n" +
                    "\t\"logical_condition\": \"or\"\n" +
                    "}");
            localParser.parse(input);
            assertEquals(RequestParser.OPERATION.SELECT, localParser.getOperation());
            assertEquals("students", localParser.getTable());
            assertEquals("id", localParser.getCondition1().key);
            assertEquals(RequestParser.OPERATOR.E, localParser.getCondition1().op);
            assertEquals("1", localParser.getCondition1().stringValue);
            assertEquals("first_name", localParser.getCondition2().key);
            assertEquals(RequestParser.OPERATOR.E, localParser.getCondition2().op);
            assertEquals("Mircea", localParser.getCondition2().stringValue);
            assertEquals(RequestParser.CONDITION_LOGIC.OR, localParser.getLogicalCondition());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void handleOperation() {
        assertEquals(RequestParser.OPERATION.SELECT, parser.handleOperation("select"));
        assertEquals(RequestParser.OPERATION.CREATE, parser.handleOperation("create"));
        assertEquals(RequestParser.OPERATION.INSERT, parser.handleOperation("insert"));
        assertEquals(RequestParser.OPERATION.INSERT_ARRAY, parser.handleOperation("insert_array"));
        assertEquals(RequestParser.OPERATION.DELETE, parser.handleOperation("delete"));
        assertEquals(RequestParser.OPERATION.UPDATE, parser.handleOperation("update"));
        assertEquals(RequestParser.OPERATION.DROP, parser.handleOperation("drop"));
        assertEquals(RequestParser.OPERATION.INVALID_OPERATION, parser.handleOperation("get"));
    }

    @Test
    public void handleLogicalCondition() {
        assertEquals(RequestParser.CONDITION_LOGIC.AND, parser.handleLogicalCondition("and"));
        assertEquals(RequestParser.CONDITION_LOGIC.OR, parser.handleLogicalCondition("or"));
        assertEquals(RequestParser.CONDITION_LOGIC.INVALID_CONDITION, parser.handleLogicalCondition("maybe"));
    }

    @Test
    public void handleOperator() {
        assertEquals(RequestParser.OPERATOR.E, parser.handleOperator("="));
        assertEquals(RequestParser.OPERATOR.G, parser.handleOperator(">"));
        assertEquals(RequestParser.OPERATOR.L, parser.handleOperator("<"));
        assertEquals(RequestParser.OPERATOR.GE, parser.handleOperator(">="));
        assertEquals(RequestParser.OPERATOR.LE, parser.handleOperator("<="));
        assertEquals(RequestParser.OPERATOR.INVALID_OPERATOR, parser.handleOperator("<>"));
    }
}