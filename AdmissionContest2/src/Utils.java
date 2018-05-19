import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	
	public static String jsonCriteriiFile = "{  \r\n" + 
			"	\"pondere_bac\": 0.5,\r\n" + 
			"	\"pondere_examen\": 0.5,\r\n" + 
			"	\"nota_min_admis\": 5,\r\n" + 
			"	\"nr_locuri_buget\": 5,\r\n" + 
			"	\"nr_locuri_taxa\": 3\r\n" + 
			"}";
	
	public static String structStudents = "{\r\n" + 
			"medie: \"number\",\r\n" + 
			"last_name: \"text\",\r\n" + 
			"nota_examen: \"number\",\r\n" + 
			"id: \"number\",\r\n" + 
			"medie_bac: \"number\",\r\n" + 
			"first_name: \"text\"\r\n" + 
			"}";
	
	public static JSONArray getFilteredObjects(RequestParser requestParser, JSONArray jsonFile) {
		
			if (requestParser.getCondition1() == null && requestParser.getCondition2() == null) {
				return jsonFile;
			}
			
			JSONArray jsonResponse = new JSONArray();
			
			
			RequestParser.Condition condition1 = requestParser.getCondition1();
			RequestParser.Condition condition2 = requestParser.getCondition2();
			
			RequestParser.CONDITION_LOGIC conditionLogic = requestParser.getLogicalCondition();
			
			Boolean ok;
			Boolean twoConditions = false;
			
			if (requestParser.getCondition1() != null && requestParser.getCondition2() != null && requestParser.getLogicalCondition() != null) {
				
				//precondition
				assert conditionLogic != RequestParser.CONDITION_LOGIC.INVALID_CONDITION && 
						conditionLogic != RequestParser.CONDITION_LOGIC.NOT_NEEDED: "The logical condition is invalid";
				
				if (conditionLogic == RequestParser.CONDITION_LOGIC.INVALID_CONDITION || conditionLogic == RequestParser.CONDITION_LOGIC.NOT_NEEDED) {
					jsonResponse.put("{\"response\": -23}");
					return jsonResponse;
				}
			}
			
			RequestParser.Condition condition = condition1;
			
			if (condition == null && condition2 != null) {
				condition = condition2;
			}
			
			if (condition1 != null && condition2 != null) {
				twoConditions = true;
			}
			
			
			for(int i=0; i<jsonFile.length(); i++) {
				
				JSONObject object = jsonFile.optJSONObject(i);
				if (object == null) {
					jsonResponse = new JSONArray();
					jsonResponse.put("{\"response\": -24}");
					return jsonResponse;
				}
					
				ok = false;
			
				switch (condition.op) {
				case E:
					
					ok = object.optString(condition.key).equals(condition.stringValue);
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonResponse.put(object);
						}
					} else if (ok) {
						jsonResponse.put(object);
					}
					
					break;
					
				case L:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) < intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) < 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonResponse.put(object);
						}
					} else if (ok) {
						jsonResponse.put(object);
					}	
					
					break;
					
				case G:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) > intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) > 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonResponse.put(object);
						}
					} else if (ok) {
						jsonResponse.put(object);
					}
					
					break;
					
				case GE:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) >= intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) >= 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonResponse.put(object);
						}
					} else if (ok) {
						jsonResponse.put(object);
					}
					
					break;
					
				case LE:
				
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) <= intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) <= 0) {
							ok = true;
						}
					}
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonResponse.put(object);
						}
					} else if (ok) {
						jsonResponse.put(object);
					}
					
					break;
				default:
					jsonResponse = new JSONArray();
					jsonResponse.put("{\"response\": -13}");
					break;
				}
					
			}
			
			return jsonResponse;
		}
	
		public static JSONArray getDifferenceFilteredObjects(RequestParser requestParser, JSONArray jsonFile) {
			
			JSONArray jsonError =  new JSONArray();
			
			if (requestParser.getCondition1() == null && requestParser.getCondition2() == null) {
				return new JSONArray();
			}
			
			
			RequestParser.Condition condition1 = requestParser.getCondition1();
			RequestParser.Condition condition2 = requestParser.getCondition2();
			
			RequestParser.CONDITION_LOGIC conditionLogic = requestParser.getLogicalCondition();
			
			Boolean ok;
			Boolean twoConditions = false;
			
			if (requestParser.getCondition1() != null && requestParser.getCondition2() != null && requestParser.getLogicalCondition() != null) {
				
				//precondition
				assert conditionLogic != RequestParser.CONDITION_LOGIC.INVALID_CONDITION && 
						conditionLogic != RequestParser.CONDITION_LOGIC.NOT_NEEDED: "The logical condition is invalid";
				
				if (conditionLogic == RequestParser.CONDITION_LOGIC.INVALID_CONDITION || conditionLogic == RequestParser.CONDITION_LOGIC.NOT_NEEDED) {
					jsonError.put("{\"response\": -23}");
					return jsonError;
				}
			}
			
			RequestParser.Condition condition = condition1;
			
			if (condition == null && condition2 != null) {
				condition = condition2;
			}
			
			if (condition1 != null && condition2 != null) {
				twoConditions = true;
			}
			
			
			for(int i=0; i<jsonFile.length(); i++) {
				
				JSONObject object = jsonFile.optJSONObject(i);
				if (object == null) {
					jsonError.put("{\"response\": -24}");
					return jsonError;
				}
					
				ok = false;
			
				switch (condition.op) {
				case E:
					
					ok = object.optString(condition.key).equals(condition.stringValue);
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonFile.remove(i);
							i--;
						}
					} else if (ok) {
						jsonFile.remove(i);
						i--;
					}
					
					break;
					
				case L:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) < intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) < 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonFile.remove(i);
							i--;
						}
					} else if (ok) {
						jsonFile.remove(i);
						i--;
					}	
					
					break;
					
				case G:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) > intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) > 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonFile.remove(i);
							i--;
						}
					} else if (ok) {
						jsonFile.remove(i);
						i--;
					}
					
					break;
					
				case GE:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) >= intValue) {
							ok = true;
						}
						
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) >= 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonFile.remove(i);
							i--;
						}
					} else if (ok) {
						jsonFile.remove(i);
						i--;
					}
					
					break;
					
				case LE:
				
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) <= intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) <= 0) {
							ok = true;
						}
					}
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							jsonFile.remove(i);
							i--;
						}
					} else if (ok) {
						jsonFile.remove(i);
					}
					
					break;
				default:
					jsonError.put("{\"response\": -13}");
					i--;
					return jsonError;
				}
					
			}
			
			return jsonFile;
		}
		
		
		public static Boolean evaluateLogicalCondition(Boolean ok1, JSONObject object, RequestParser.Condition condition2, RequestParser.CONDITION_LOGIC conditionLogic) {
			
			Boolean ok2 = false;
			
			switch (condition2.op) {
			case E:
				
				if ((ok1 && object.optString(condition2.key).equals(condition2.stringValue) && conditionLogic == RequestParser.CONDITION_LOGIC.AND) ||
						((ok1 || object.optString(condition2.key).equals(condition2.stringValue)) && conditionLogic == RequestParser.CONDITION_LOGIC.OR)) {
					return true;
				}
				break;
				
			case L:
				
				try {
					int intValue = Integer.parseInt(condition2.stringValue);
					if (object.optInt(condition2.key) < intValue) {
						ok2 = true;
					}
				} catch(NumberFormatException e) { 
					if (object.optString(condition2.key).compareTo(condition2.stringValue) < 0) {
						ok2 = true;
					}
				}
				
				if ((ok1 && ok2 && conditionLogic == RequestParser.CONDITION_LOGIC.AND) || ((ok1 || ok2) && conditionLogic == RequestParser.CONDITION_LOGIC.OR)) {
					return true;
				}
				break;
				
			case G:
				
				try {
					int intValue = Integer.parseInt(condition2.stringValue);
					if (object.optInt(condition2.key) > intValue) {
						ok2 = true;
					}
				} catch(NumberFormatException e) { 
					if (object.optString(condition2.key).compareTo(condition2.stringValue) > 0) {
						ok2 = true;
					}
				}
				
				if ((ok1 && ok2 && conditionLogic == RequestParser.CONDITION_LOGIC.AND) || ((ok1 || ok2) && conditionLogic == RequestParser.CONDITION_LOGIC.OR)) {
					return true;
				}
				break;
				
			case GE:
				
				try {
					int intValue = Integer.parseInt(condition2.stringValue);
					if (object.optInt(condition2.key) >= intValue) {
						ok2 = true;
					}
				} catch(NumberFormatException e) { 
					if (object.optString(condition2.key).compareTo(condition2.stringValue) >= 0) {
						ok2 = true;
					}
				}
				
				if ((ok1 && ok2 && conditionLogic == RequestParser.CONDITION_LOGIC.AND) || ((ok1 || ok2) && conditionLogic == RequestParser.CONDITION_LOGIC.OR)) {
					return true;
				}
				break;
			
			case LE:
				
				try {
					int intValue = Integer.parseInt(condition2.stringValue);
					if (object.optInt(condition2.key) <= intValue) {
						ok2 = true;
					}
				} catch(NumberFormatException e) { 
					if (object.optString(condition2.key).compareTo(condition2.stringValue) <= 0) {
						ok2 = true;
					}
				}
				
				if ((ok1 && ok2 && conditionLogic == RequestParser.CONDITION_LOGIC.AND) || ((ok1 || ok2) && conditionLogic == RequestParser.CONDITION_LOGIC.OR)) {
					return true;
				}
				break;
			
			default:
				break;
			}
			
			return false;
		}
		
		
		public static JSONArray getUpdatedObjects(RequestParser requestParser, JSONArray jsonFile, JSONObject jsonStruct) {
			
			JSONObject values = requestParser.getValues();
			
			//precondition
			assert values != null : "The values are invalid";
			
			JSONArray jsonResponse = new JSONArray();
			if (values == null) {
				jsonResponse.put("{\"response\": -16}");
				return jsonResponse;
			}
			
			if (requestParser.getCondition1() == null && requestParser.getCondition2() == null) {
				
				for(int i=0; i<jsonFile.length(); i++) {
					
					JSONObject object = jsonFile.optJSONObject(i);
					if (object == null) {
						jsonResponse = new JSONArray();
						jsonResponse.put("{\"response\": -24}");
						return jsonResponse;
					}
					
					UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
					
					if (updatedObject.ok) {
						jsonResponse.put(updatedObject.object);
					} else {
						jsonResponse = new JSONArray();
						jsonResponse.put("{\"response\": -30}");
						return jsonResponse;
					}
				}
				
				return jsonResponse;
			}
			
			
			RequestParser.Condition condition1 = requestParser.getCondition1();
			RequestParser.Condition condition2 = requestParser.getCondition2();
			
			RequestParser.CONDITION_LOGIC conditionLogic = requestParser.getLogicalCondition();
			
			Boolean ok;
			Boolean twoConditions = false;
			
			if (requestParser.getCondition1() != null && requestParser.getCondition2() != null && requestParser.getLogicalCondition() != null) {
				
				//precondition
				assert conditionLogic != RequestParser.CONDITION_LOGIC.INVALID_CONDITION && 
						conditionLogic != RequestParser.CONDITION_LOGIC.NOT_NEEDED: "The logical condition is invalid";
				
				if (conditionLogic == RequestParser.CONDITION_LOGIC.INVALID_CONDITION || conditionLogic == RequestParser.CONDITION_LOGIC.NOT_NEEDED) {
					jsonResponse.put("{\"response\": -23}");
					return jsonResponse;
				}
			}
			
			RequestParser.Condition condition = condition1;
			
			if (condition == null && condition2 != null) {
				condition = condition2;
			}
			
			if (condition1 != null && condition2 != null) {
				twoConditions = true;
			}
			
			
			for(int i=0; i<jsonFile.length(); i++) {
				
				JSONObject object = jsonFile.optJSONObject(i);
				if (object == null) {
					jsonResponse = new JSONArray();
					jsonResponse.put("{\"response\": -1}");
					return jsonResponse;
				}
					
				ok = false;
			
				switch (condition.op) {
				case E:
					
					ok = object.optString(condition.key).equals(condition.stringValue);
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							
//							Iterator<?> keys = values.keys();
//							
//							while( keys.hasNext() ) {
//							    String key = (String)keys.next();
//							    if (object.has(key)) {
//							    	try {
//							    		
//							    		if(!isValueCorrect(jsonStruct, values, key)) {
//							    			jsonResponse = new JSONArray();
//											jsonResponse.put("{\"response\": -30}");
//											return jsonResponse;
//							    		}
//							    		
//										object.put(key, values.get(key));
//									} catch (JSONException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//							    }
//							}
//							
//							jsonResponse.put(object);
							
							UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
							
							if (updatedObject.ok) {
								jsonResponse.put(updatedObject.object);
							} else {
								jsonResponse = new JSONArray();
								jsonResponse.put("{\"response\": -30}");
								return jsonResponse;
							}
							
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
						
						if (updatedObject.ok) {
							jsonResponse.put(updatedObject.object);
						} else {
							jsonResponse = new JSONArray();
							jsonResponse.put("{\"response\": -30}");
							return jsonResponse;
						}
						
						
					} else {
						jsonResponse.put(object);
					}
					
					break;
					
				case L:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) < intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) < 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							
							UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
							
							if (updatedObject.ok) {
								jsonResponse.put(updatedObject.object);
							} else {
								jsonResponse = new JSONArray();
								jsonResponse.put("{\"response\": -30}");
								return jsonResponse;
							}
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
						
						if (updatedObject.ok) {
							jsonResponse.put(updatedObject.object);
						} else {
							jsonResponse = new JSONArray();
							jsonResponse.put("{\"response\": -30}");
							return jsonResponse;
						}
					} else {
						jsonResponse.put(object);
					}	
					
					break;
					
				case G:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) > intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) > 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							
							UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
							
							if (updatedObject.ok) {
								jsonResponse.put(updatedObject.object);
							} else {
								jsonResponse = new JSONArray();
								jsonResponse.put("{\"response\": -30}");
								return jsonResponse;
							}
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
						
						if (updatedObject.ok) {
							jsonResponse.put(updatedObject.object);
						} else {
							jsonResponse = new JSONArray();
							jsonResponse.put("{\"response\": -30}");
							return jsonResponse;
						}
					} else {
						jsonResponse.put(object);
					}
					
					break;
					
				case GE:
					
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) >= intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) >= 0) {
							ok = true;
						}
					}
					
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							
							UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
							
							if (updatedObject.ok) {
								jsonResponse.put(updatedObject.object);
							} else {
								jsonResponse = new JSONArray();
								jsonResponse.put("{\"response\": -30}");
								return jsonResponse;
							}
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
						
						if (updatedObject.ok) {
							jsonResponse.put(updatedObject.object);
						} else {
							jsonResponse = new JSONArray();
							jsonResponse.put("{\"response\": -30}");
							return jsonResponse;
						}
					} else {
						jsonResponse.put(object);
					}
					
					break;
					
				case LE:
				
					try {
						int intValue = Integer.parseInt(condition.stringValue);
						if (object.optInt(condition.key) <= intValue) {
							ok = true;
						}
					} catch(NumberFormatException e) { 
						if (object.optString(condition.key).compareTo(condition.stringValue) <= 0) {
							ok = true;
						}
					}
					if (twoConditions) {
						if (evaluateLogicalCondition(ok, object, condition2, conditionLogic)) {
							
							UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
							
							if (updatedObject.ok) {
								jsonResponse.put(updatedObject.object);
							} else {
								jsonResponse = new JSONArray();
								jsonResponse.put("{\"response\": -30}");
								return jsonResponse;
							}
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						UpdatedJsonObject updatedObject = updateJsonObject(object, values, jsonStruct);
						
						if (updatedObject.ok) {
							jsonResponse.put(updatedObject.object);
						} else {
							jsonResponse = new JSONArray();
							jsonResponse.put("{\"response\": -30}");
							return jsonResponse;
						}
					} else {
						jsonResponse.put(object);
					}
					
					break;
				default:
					jsonResponse = new JSONArray();
					jsonResponse.put("{\"response\": -13}");
					break;
				}
					
			}
			
			return jsonResponse;
		}
		
		public static class UpdatedJsonObject {
		    boolean ok;
		    JSONObject object;
		    
		    public UpdatedJsonObject(boolean ok, JSONObject object) {
				this.ok = ok;
				this.object = object;
			}
		    
		}
		
		public static UpdatedJsonObject updateJsonObject(JSONObject object, JSONObject values, JSONObject jsonStruct) {
			
			Iterator<?> keys = values.keys();
			
			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    if (object.has(key)) {
			    	try {
			    		
			    		if (!isValueCorrect(jsonStruct, values, key)) {
			    			
			    			return new UpdatedJsonObject(false, object);
			    		}
			    		
						object.put(key, values.get(key));
					} catch (JSONException e) {
						e.printStackTrace();
						return new UpdatedJsonObject(false, object);
					}
			    }
			}
			
			return new UpdatedJsonObject(true, object);
		}
		
		public static boolean isValueCorrect(JSONObject jsonStruct, JSONObject values, String key) {
			
			try {
				if (jsonStruct.getString(key).equals("number")) {
					
					if (values.get(key) instanceof String) {
						return false;
					}
					
				} else {
					if (values.get(key) instanceof Double) {
						return false;
					}
					if (values.get(key) instanceof Float) {
						return false;
					}
					if (values.get(key) instanceof Integer) {
						return false;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		
		public static int getMaxId(JSONArray jsonFile) {
			
			int maxId = -1;
			
			for(int i=0; i<jsonFile.length(); i++) {
				
				JSONObject object = jsonFile.optJSONObject(i);
				if (object == null) {
					return -1;
				}
				
				int id;
				
				try {
					if(object.has("id")) {
						id = object.getInt("id");
						if (id > maxId) {
							maxId = id;
						}
					} else {
						return -1;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return -1;
				}
				
			}
			return maxId;
		}

}
