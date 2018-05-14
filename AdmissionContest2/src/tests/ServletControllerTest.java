package tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServletControllerTest {

    RequestParser parser;
    JSONArray jsonFile;
    JSONObject input;

    @Before
    public void setUp() throws Exception {
        parser = new RequestParser();
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
    }

    @Test
    public void getSelectData() {
        try {
            JSONArray arrayResponseOk = new JSONArray("[{\"response\":1}]\"");
            JSONArray arrayResponseError = new JSONArray("[{\"response\":-13}]\"");
            assertEquals(ServletController.STATUS_OK, ServletController.getSelectData(arrayResponseOk).getStatus());
            assertEquals(ServletController.STATUS_NOT_FOUND, ServletController.getSelectData(arrayResponseError).getStatus());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void checkBasicData() {
         assertEquals(ServletController.STATUS_OK, ServletController.checkBasicData(parser).getStatus());
        
    }

    @Test
    public void checkBasicDataNoTable() {
        try {
            JSONObject noTable = new JSONObject("{\n" +
                    "\t\"operation\": \"select\",\n" +
                    "\t\"from\": \"\",\n" +
                    "\t\"cond1\": {\n" +
                    "\t\t\"key\": \"id\",\n" +
                    "\t\t\"operator\": \"<=\",\n" +
                    "\t\t\"value\": 10\n" +
                    "\t\t}\n" +
                    "}");
            RequestParser reqParser = new RequestParser();
            reqParser.parse(noTable);
            assertEquals("{\"response\": -11}", ServletController.checkBasicData(reqParser).getResponse());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void checkBasicDataNoOperation() {
        try {
            JSONObject noOperation = new JSONObject("{\n" +
                    "\t\"operation\": \"\",\n" +
                    "\t\"from\": \"students\",\n" +
                    "\t\"cond1\": {\n" +
                    "\t\t\"key\": \"id\",\n" +
                    "\t\t\"operator\": \"<=\",\n" +
                    "\t\t\"value\": 10\n" +
                    "\t\t}\n" +
                    "}");
            RequestParser reqParser = new RequestParser();
            reqParser.parse(noOperation);
            assertEquals("{\"response\": -11}", ServletController.checkBasicData(reqParser).getResponse());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void checkBasicDataWrongOperator() {
        try {
            JSONObject wrongInputOperator = new JSONObject("{\n" +
                    "\t\"operation\": \"select\",\n" +
                    "\t\"from\": \"students\",\n" +
                    "\t\"cond1\": {\n" +
                    "\t\t\"key\": \"id\",\n" +
                    "\t\t\"operator\": \"<>\",\n" +
                    "\t\t\"value\": 10\n" +
                    "\t\t}\n" +
                    "}");

            RequestParser wrongOpParser = new RequestParser();
            wrongOpParser.parse(wrongInputOperator);
            assertEquals("{\"response\": -13}", ServletController.checkBasicData(wrongOpParser).getResponse());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void checkBasicDataEmptyKey() {
        try {
            JSONObject emptyKey = new JSONObject("{\n" +
                    "\t\"operation\": \"select\",\n" +
                    "\t\"from\": \"students\",\n" +
                    "\t\"cond1\": {\n" +
                    "\t\t\"key\": \"\",\n" +
                    "\t\t\"operator\": \"<=\",\n" +
                    "\t\t\"value\": 10\n" +
                    "\t\t}\n" +
                    "}");
            RequestParser reqParser = new RequestParser();
            reqParser.parse(emptyKey);
            assertEquals("{\"response\": -14}", ServletController.checkBasicData(reqParser).getResponse());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void checkBasicDataEmptyValue() {
        try {
            JSONObject emptyValue = new JSONObject("{\n" +
                    "\t\"operation\": \"select\",\n" +
                    "\t\"from\": \"\",\n" +
                    "\t\"cond1\": {\n" +
                    "\t\t\"key\": \"\",\n" +
                    "\t\t\"operator\": \"<=\",\n" +
                    "\t\t\"value\": \"\"\n" +
                    "\t\t}\n" +
                    "}");
            RequestParser reqParser = new RequestParser();
            reqParser.parse(emptyValue);
            assertEquals("{\"response\": -11}", ServletController.checkBasicData(reqParser).getResponse());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
}