package tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class UtilsTest {

    RequestParser parser;
    RequestParser parserUpdate;
    RequestParser parserUpdateWrong;
    JSONArray jsonFile;
    JSONObject input;
    JSONObject inputUpdate;
    JSONObject inputUpdateWrong;

    @Before
    public void setUp() throws Exception {
        parser = new RequestParser();
        parserUpdate = new RequestParser();
        parserUpdateWrong = new RequestParser();
        jsonFile = new JSONArray("[\n" +
                "    {\n" +
                "        \"medie\": 9.5,\n" +
                "        \"last_name\": \"Port\",\n" +
                "        \"nota_examen\": 9,\n" +
                "        \"id\": 3,\n" +
                "        \"medie_bac\": 10,\n" +
                "        \"first_name\": \"Ionel\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 8,\n" +
                "        \"last_name\": \"ionel\",\n" +
                "        \"nota_examen\": 8,\n" +
                "        \"id\": 5,\n" +
                "        \"medie_bac\": 8,\n" +
                "        \"first_name\": \"popescu\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 3.495,\n" +
                "        \"last_name\": \"test\",\n" +
                "        \"nota_examen\": 0.01,\n" +
                "        \"id\": 6,\n" +
                "        \"medie_bac\": 6.98,\n" +
                "        \"first_name\": \"test nou\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 7.49,\n" +
                "        \"last_name\": \"test now\",\n" +
                "        \"nota_examen\": 8,\n" +
                "        \"id\": 8,\n" +
                "        \"medie_bac\": 6.98,\n" +
                "        \"first_name\": \"test\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 7.99,\n" +
                "        \"last_name\": \"test now 2\",\n" +
                "        \"nota_examen\": 9,\n" +
                "        \"id\": 9,\n" +
                "        \"medie_bac\": 6.98,\n" +
                "        \"first_name\": \"test\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 5,\n" +
                "        \"last_name\": \"Ion\",\n" +
                "        \"nota_examen\": 5,\n" +
                "        \"id\": 10,\n" +
                "        \"medie_bac\": 5,\n" +
                "        \"first_name\": \"Dorian\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 7,\n" +
                "        \"last_name\": \"ion\",\n" +
                "        \"nota_examen\": 5,\n" +
                "        \"id\": 11,\n" +
                "        \"medie_bac\": 9,\n" +
                "        \"first_name\": \"ion\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 8.825,\n" +
                "        \"last_name\": \"Grigore\",\n" +
                "        \"nota_examen\": 9.2,\n" +
                "        \"id\": 12,\n" +
                "        \"medie_bac\": 8.45,\n" +
                "        \"first_name\": \"Vasile\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 8.825,\n" +
                "        \"last_name\": \"Grigore\",\n" +
                "        \"nota_examen\": 9.2,\n" +
                "        \"id\": 13,\n" +
                "        \"medie_bac\": 8.45,\n" +
                "        \"first_name\": \"Vasile\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 7.5,\n" +
                "        \"last_name\": \"Mihai\",\n" +
                "        \"nota_examen\": 7,\n" +
                "        \"id\": 14,\n" +
                "        \"medie_bac\": 8,\n" +
                "        \"first_name\": \"Mircea\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 8.5,\n" +
                "        \"last_name\": \"Grigore\",\n" +
                "        \"nota_examen\": 9,\n" +
                "        \"id\": 15,\n" +
                "        \"medie_bac\": 8,\n" +
                "        \"first_name\": \"Vasile\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 6,\n" +
                "        \"last_name\": \"Mihai\",\n" +
                "        \"nota_examen\": 7,\n" +
                "        \"id\": 16,\n" +
                "        \"medie_bac\": 8,\n" +
                "        \"first_name\": \"Mircea\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 8.5,\n" +
                "        \"last_name\": \"Grigore\",\n" +
                "        \"nota_examen\": 9,\n" +
                "        \"id\": 17,\n" +
                "        \"medie_bac\": 8,\n" +
                "        \"first_name\": \"Vasile\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"medie\": 10,\n" +
                "        \"last_name\": \"Cusmuliuc\",\n" +
                "        \"nota_examen\": 10,\n" +
                "        \"id\": 18,\n" +
                "        \"medie_bac\": 10,\n" +
                "        \"first_name\": \"Ciprian-Gabriel\"\n" +
                "    }\n" +
                "]");
        
        input = new JSONObject("{\n" +
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
        parser.parse(input);
        
        inputUpdate = new JSONObject("{\r\n" + 
        		"	\"operation\": \"update\",\r\n" + 
        		"	\"table\": \"students\",\r\n" + 
        		"	\"values\": {\r\n" + 
        		"		\"medie\": 8\r\n" + 
        		"	},\r\n" + 
        		"	\"cond1\": {\r\n" + 
        		"		\"key\": \"last_name\",\r\n" + 
        		"		\"operator\": \"=\",\r\n" + 
        		"		\"value\": \"Mihai\"\r\n" + 
        		"		},\r\n" + 
        		"	\"cond2\": {\r\n" + 
        		"		\"key\": \"id\",\r\n" + 
        		"		\"operator\": \">=\",\r\n" + 
        		"		\"value\": 15\r\n" + 
        		"		},\r\n" + 
        		"	\"logical_condition\": \"and\"\r\n" + 
        		"}");

        parserUpdate.parse(inputUpdate);
        
        inputUpdateWrong = new JSONObject("{\r\n" + 
        		"	\"operation\": \"update\",\r\n" + 
        		"	\"table\": \"students\",\r\n" + 
        		"	\"values\": {\r\n" + 
        		"		\"medie\": \"8\"\r\n" + 
        		"	},\r\n" + 
        		"	\"cond1\": {\r\n" + 
        		"		\"key\": \"last_name\",\r\n" + 
        		"		\"operator\": \"=\",\r\n" + 
        		"		\"value\": \"Mihai\"\r\n" + 
        		"		},\r\n" + 
        		"	\"cond2\": {\r\n" + 
        		"		\"key\": \"id\",\r\n" + 
        		"		\"operator\": \">=\",\r\n" + 
        		"		\"value\": 15\r\n" + 
        		"		},\r\n" + 
        		"	\"logical_condition\": \"and\"\r\n" + 
        		"}");

        parserUpdateWrong.parse(inputUpdateWrong);
        
        

    }

    /**
     * Test if returned all users with id 1 and first_name Mircea
     */
    @Test
    public void getFilteredObjects() {
        String result = "[{\"medie\":7.5,\"last_name\":\"Mihai\",\"nota_examen\":7,\"id\":14,\"medie_bac\":8,\"first_name\":\"Mircea\"},{\"medie\":6,\"last_name\":\"Mihai\",\"nota_examen\":7,\"id\":16,\"medie_bac\":8,\"first_name\":\"Mircea\"}]";
        assertEquals(result, Utils.getFilteredObjects(parser, jsonFile).toString());
    }


    /**
     * Test if returned all users with id different than 1 and first_name different than Mircea
     */
    @Test
    public void getDifferenceFilteredObjects() {
        String result = "[{\"medie\":9.5,\"last_name\":\"Port\",\"nota_examen\":9,\"id\":3,\"medie_bac\":10,\"first_name\":\"Ionel\"},{\"medie\":8,\"last_name\":\"ionel\",\"nota_examen\":8,\"id\":5,\"medie_bac\":8,\"first_name\":\"popescu\"},{\"medie\":3.495,\"last_name\":\"test\",\"nota_examen\":0.01,\"id\":6,\"medie_bac\":6.98,\"first_name\":\"test nou\"},{\"medie\":7.49,\"last_name\":\"test now\",\"nota_examen\":8,\"id\":8,\"medie_bac\":6.98,\"first_name\":\"test\"},{\"medie\":7.99,\"last_name\":\"test now 2\",\"nota_examen\":9,\"id\":9,\"medie_bac\":6.98,\"first_name\":\"test\"},{\"medie\":5,\"last_name\":\"Ion\",\"nota_examen\":5,\"id\":10,\"medie_bac\":5,\"first_name\":\"Dorian\"},{\"medie\":7,\"last_name\":\"ion\",\"nota_examen\":5,\"id\":11,\"medie_bac\":9,\"first_name\":\"ion\"},{\"medie\":8.825,\"last_name\":\"Grigore\",\"nota_examen\":9.2,\"id\":12,\"medie_bac\":8.45,\"first_name\":\"Vasile\"},{\"medie\":8.825,\"last_name\":\"Grigore\",\"nota_examen\":9.2,\"id\":13,\"medie_bac\":8.45,\"first_name\":\"Vasile\"},{\"medie\":8.5,\"last_name\":\"Grigore\",\"nota_examen\":9,\"id\":15,\"medie_bac\":8,\"first_name\":\"Vasile\"},{\"medie\":8.5,\"last_name\":\"Grigore\",\"nota_examen\":9,\"id\":17,\"medie_bac\":8,\"first_name\":\"Vasile\"},{\"medie\":10,\"last_name\":\"Cusmuliuc\",\"nota_examen\":10,\"id\":18,\"medie_bac\":10,\"first_name\":\"Ciprian-Gabriel\"}]";
        assertEquals(result, Utils.getDifferenceFilteredObjects(parser, jsonFile).toString());
    }

    /**
     * Test if one or both logical conditions are matched according to the logical operator
     */
    @Test
    public void evaluateLogicalCondition() {
    	
    	try {
			JSONObject studentId16 = new JSONObject("{\"medie\":6,\"last_name\":\"Mihai\",\"nota_examen\":7,\"id\":16,\"medie_bac\":8,\"first_name\":\"Mircea\"}");
			JSONObject studentId17 = new JSONObject("{\"medie\":8.5,\"last_name\":\"Grigore\",\"nota_examen\":9,\"id\":17,\"medie_bac\":8,\"first_name\":\"Vasile\"}");
			 assertEquals(true, Utils.evaluateLogicalCondition(true, studentId16, parser.getCondition2(), RequestParser.CONDITION_LOGIC.AND));
		     assertEquals(true, Utils.evaluateLogicalCondition(true, studentId17, parser.getCondition2(), RequestParser.CONDITION_LOGIC.OR));
			
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
       
    }
    
    @Test 
    public void evaluateMissingValues() {
    	JSONObject jsonStruct;
		try {
			jsonStruct = new JSONObject(Utils.structStudents);
			assertEquals("[\"{\\\"response\\\": -16}\"]", Utils.getUpdatedObjects(parser, jsonFile, jsonStruct).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test 
    public void getUpdatedObjects() {
    	JSONObject jsonStruct;
		try {
			jsonStruct = new JSONObject(Utils.structStudents);
			String result = "[{\"medie\":9.5,\"last_name\":\"Port\",\"nota_examen\":9,\"id\":3,\"medie_bac\":10,\"first_name\":\"Ionel\"},{\"medie\":8,\"last_name\":\"ionel\",\"nota_examen\":8,\"id\":5,\"medie_bac\":8,\"first_name\":\"popescu\"},{\"medie\":3.495,\"last_name\":\"test\",\"nota_examen\":0.01,\"id\":6,\"medie_bac\":6.98,\"first_name\":\"test nou\"},{\"medie\":7.49,\"last_name\":\"test now\",\"nota_examen\":8,\"id\":8,\"medie_bac\":6.98,\"first_name\":\"test\"},{\"medie\":7.99,\"last_name\":\"test now 2\",\"nota_examen\":9,\"id\":9,\"medie_bac\":6.98,\"first_name\":\"test\"},{\"medie\":5,\"last_name\":\"Ion\",\"nota_examen\":5,\"id\":10,\"medie_bac\":5,\"first_name\":\"Dorian\"},{\"medie\":7,\"last_name\":\"ion\",\"nota_examen\":5,\"id\":11,\"medie_bac\":9,\"first_name\":\"ion\"},{\"medie\":8.825,\"last_name\":\"Grigore\",\"nota_examen\":9.2,\"id\":12,\"medie_bac\":8.45,\"first_name\":\"Vasile\"},{\"medie\":8.825,\"last_name\":\"Grigore\",\"nota_examen\":9.2,\"id\":13,\"medie_bac\":8.45,\"first_name\":\"Vasile\"},{\"medie\":7.5,\"last_name\":\"Mihai\",\"nota_examen\":7,\"id\":14,\"medie_bac\":8,\"first_name\":\"Mircea\"},{\"medie\":8.5,\"last_name\":\"Grigore\",\"nota_examen\":9,\"id\":15,\"medie_bac\":8,\"first_name\":\"Vasile\"},{\"medie\":8,\"last_name\":\"Mihai\",\"nota_examen\":7,\"id\":16,\"medie_bac\":8,\"first_name\":\"Mircea\"},{\"medie\":8.5,\"last_name\":\"Grigore\",\"nota_examen\":9,\"id\":17,\"medie_bac\":8,\"first_name\":\"Vasile\"},{\"medie\":10,\"last_name\":\"Cusmuliuc\",\"nota_examen\":10,\"id\":18,\"medie_bac\":10,\"first_name\":\"Ciprian-Gabriel\"}]";
			assertEquals(result, Utils.getUpdatedObjects(parserUpdate, jsonFile, jsonStruct).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test 
    public void getUpdatedObjectsWrongFormat() {
    	JSONObject jsonStruct;
		try {
			jsonStruct = new JSONObject(Utils.structStudents);
			assertEquals("[\"{\\\"response\\\": -30}\"]", Utils.getUpdatedObjects(parserUpdateWrong, jsonFile, jsonStruct).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}