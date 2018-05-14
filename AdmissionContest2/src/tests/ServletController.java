package tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class ServletController {

    public static class ServletBasicResponse {

        private String response;
        private int status;

        public ServletBasicResponse(String response, int status) {
            this.response = response;
            this.status = status;
        }

        public String getResponse() {
            return response;
        }

        public int getStatus() {
            return status;
        }
    }

    public static final int STATUS_NOT_FOUND = 400;
    public static final int STATUS_OK = 200;

    public static ServletBasicResponse getSelectData(JSONArray filteredObjects) {

        if (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) {
            return new ServletBasicResponse(filteredObjects.toString(), STATUS_OK);
        } else if(filteredObjects.toString().equals("[{\"response\":-13}]")) {
            return new ServletBasicResponse("{\"response\": -13}", STATUS_NOT_FOUND);
        } else if(filteredObjects.toString().equals("[{\"response\":-23}]")) {
            return new ServletBasicResponse("{\"response\": -23}", STATUS_NOT_FOUND);
        }
        return new ServletBasicResponse("{\"response\": -24}", STATUS_NOT_FOUND);
    }

    public static ServletBasicResponse checkBasicData(RequestParser requestParser) {
        if(requestParser.getTable() == null || requestParser.getTable().isEmpty() ||
                requestParser.getOperation() == null || requestParser.getOperation() == RequestParser.OPERATION.INVALID_OPERATION) {
            return new ServletBasicResponse("{\"response\": -11}", STATUS_NOT_FOUND);
        }

//        System.out.println("Table:" + requestParser.getTable());
//        System.out.println("Operation:" + requestParser.getOperation());
//        System.out.println("Condition1:" + (requestParser.getCondition1() != null ? (requestParser.getCondition1().key + "\n" + requestParser.getCondition1().op + "\n" + requestParser.getCondition1().stringValue) : ""));
//        System.out.println("Condition2:" + (requestParser.getCondition2() != null ? (requestParser.getCondition2().key + "\n" + requestParser.getCondition2().op + "\n" + requestParser.getCondition2().stringValue) : ""));
//        System.out.println("Logical condition: " + (requestParser.getLogicalCondition() != null ? requestParser.getLogicalCondition() : ""));


        if (requestParser.getCondition1() != null) {
            if (requestParser.getCondition1().op == RequestParser.OPERATOR.INVALID_OPERATOR) {
                return new ServletBasicResponse("{\"response\": -13}", STATUS_NOT_FOUND);
            }

            if (requestParser.getCondition1().key == null || requestParser.getCondition1().key.isEmpty()) {
                return new ServletBasicResponse("{\"response\": -14}", STATUS_NOT_FOUND);
            }

            if (requestParser.getCondition1().stringValue == null || requestParser.getCondition1().stringValue.isEmpty()) {
                return new ServletBasicResponse("{\"response\": -15}", STATUS_NOT_FOUND);
            }

        }

        if (requestParser.getCondition2() != null) {
            if (requestParser.getCondition2().op == RequestParser.OPERATOR.INVALID_OPERATOR) {
                return new ServletBasicResponse("{\"response\": -13}", STATUS_NOT_FOUND);
            }

            if (requestParser.getCondition2().key == null || requestParser.getCondition2().key.isEmpty()) {
                return new ServletBasicResponse("{\"response\": -14}", STATUS_NOT_FOUND);
            }

            if (requestParser.getCondition2().stringValue == null || requestParser.getCondition2().stringValue.isEmpty()) {
                return new ServletBasicResponse("{\"response\": -15}", STATUS_NOT_FOUND);
            }
        }
        return new ServletBasicResponse("", STATUS_OK);
    }

    public static Object checkCriterii(String criterii, JSONArray values, String jsonStructFile, JSONArray jsonFile) {
        try {

            JSONObject jsonCriterii = new JSONObject(criterii);


            Double pondereBac = jsonCriterii.optDouble("pondere_bac");
            Double pondereExamen = jsonCriterii.optDouble("pondere_examen");


            for(int i=0; i<values.length(); i++) {

                JSONObject value = values.optJSONObject(i);

                if (value == null) {
                    return new ServletBasicResponse("{\"response\": -16}", STATUS_NOT_FOUND);
                }

                if (!value.has("medie_bac")) {
                    return new ServletBasicResponse("{\"response\": -18}", STATUS_NOT_FOUND);
                }

                if (!value.has("nota_examen")) {
                    return new ServletBasicResponse("{\"response\": -19}", STATUS_NOT_FOUND);
                }

                value.put("id", Utils.getMaxId(jsonFile) + 1);


                if (!jsonStructFile.isEmpty()) {

                    JSONObject jsonStruct = new JSONObject(jsonStructFile);

                    Iterator<?> keys = jsonStruct.keys();

                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if (!value.has(key)) {
                            value.put(key, "");
                        } else {
                            if (jsonStruct.getString(key).equals("number")) {

                                if (value.get(key) instanceof String) {
                                    return new ServletBasicResponse("{\"response\": -30}", STATUS_NOT_FOUND);
                                }

                            } else {
                                Object objKey = value.get(key);
                                if (objKey instanceof Double || objKey instanceof Float || objKey instanceof Integer) {
                                    return new ServletBasicResponse("{\"response\": -30}", STATUS_NOT_FOUND);
                                }
                            }
                        }
                    }

                    keys = value.keys();

                    JSONObject valueTemp = new JSONObject(value.toString());

                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if (!jsonStruct.has(key) && !key.equals("id")) {
                            valueTemp.remove(key);
                        }
                    }

                    value = valueTemp;
                }


                Double medieBac = value.getDouble("medie_bac");
                Double notaExamen = value.getDouble("nota_examen");

                if (medieBac > 10 || medieBac < 5) {
                    return new ServletBasicResponse("{\"response\": -20}", STATUS_NOT_FOUND);
                }

                if (notaExamen > 10 || notaExamen < 0) {
                    return new ServletBasicResponse("{\"response\": -21}", STATUS_NOT_FOUND);
                }

                Double medie = medieBac * pondereBac + notaExamen * pondereExamen;

                value.put("medie", medie);
                jsonFile.put(value);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ServletBasicResponse("{\"response\": -22}", STATUS_NOT_FOUND);
        }
        return new ServletBasicResponse("", STATUS_OK);
    }

}
