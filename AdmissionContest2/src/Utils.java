import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	
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
						}
					} else if (ok) {
						jsonFile.remove(i);
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
						}
					} else if (ok) {
						jsonFile.remove(i);
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
						}
					} else if (ok) {
						jsonFile.remove(i);
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
						}
					} else if (ok) {
						jsonFile.remove(i);
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
						}
					} else if (ok) {
						jsonFile.remove(i);
					}
					
					break;
				default:
					jsonError.put("{\"response\": -13}");
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
		
		
		public static JSONArray getUpdatedObjects(RequestParser requestParser, JSONArray jsonFile) {
			
			JSONArray jsonResponse = new JSONArray();
			
			JSONObject values = requestParser.getValues();
			
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
					
					Iterator<?> keys = values.keys();
					
					while( keys.hasNext() ) {
					    String key = (String)keys.next();
					    if (object.has(key)) {
					    	try {
								object.put(key, values.get(key));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    }
					}
					
					jsonResponse.put(object);
				}
				
				return jsonResponse;
			}
			
			
			RequestParser.Condition condition1 = requestParser.getCondition1();
			RequestParser.Condition condition2 = requestParser.getCondition2();
			
			RequestParser.CONDITION_LOGIC conditionLogic = requestParser.getLogicalCondition();
			
			Boolean ok;
			Boolean twoConditions = false;
			
			if (requestParser.getCondition1() != null && requestParser.getCondition2() != null && requestParser.getLogicalCondition() != null) {
				
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
							
							Iterator<?> keys = values.keys();
							
							while( keys.hasNext() ) {
							    String key = (String)keys.next();
							    if (object.has(key)) {
							    	try {
										object.put(key, values.get(key));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    }
							}
							
							jsonResponse.put(object);
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						Iterator<?> keys = values.keys();
						
						while( keys.hasNext() ) {
						    String key = (String)keys.next();
						    if (object.has(key)) {
						    	try {
									object.put(key, values.get(key));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						}
						
						jsonResponse.put(object);
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
							
							Iterator<?> keys = values.keys();
							
							while( keys.hasNext() ) {
							    String key = (String)keys.next();
							    if (object.has(key)) {
							    	try {
										object.put(key, values.get(key));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    }
							}
							
							jsonResponse.put(object);
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						Iterator<?> keys = values.keys();
						
						while( keys.hasNext() ) {
						    String key = (String)keys.next();
						    if (object.has(key)) {
						    	try {
									object.put(key, values.get(key));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						}
						
						jsonResponse.put(object);
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
							
							Iterator<?> keys = values.keys();
							
							while( keys.hasNext() ) {
							    String key = (String)keys.next();
							    if (object.has(key)) {
							    	try {
										object.put(key, values.get(key));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    }
							}
							
							jsonResponse.put(object);
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						Iterator<?> keys = values.keys();
						
						while( keys.hasNext() ) {
						    String key = (String)keys.next();
						    if (object.has(key)) {
						    	try {
									object.put(key, values.get(key));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						}
						
						jsonResponse.put(object);
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
							
							Iterator<?> keys = values.keys();
							
							while( keys.hasNext() ) {
							    String key = (String)keys.next();
							    if (object.has(key)) {
							    	try {
										object.put(key, values.get(key));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    }
							}
							
							jsonResponse.put(object);
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						Iterator<?> keys = values.keys();
						
						while( keys.hasNext() ) {
						    String key = (String)keys.next();
						    if (object.has(key)) {
						    	try {
									object.put(key, values.get(key));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						}
						
						jsonResponse.put(object);
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
							
							Iterator<?> keys = values.keys();
							
							while( keys.hasNext() ) {
							    String key = (String)keys.next();
							    if (object.has(key)) {
							    	try {
										object.put(key, values.get(key));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    }
							}
							
							jsonResponse.put(object);
							
						} else {
							jsonResponse.put(object);
						}
					} else if (ok) {
						
						Iterator<?> keys = values.keys();
						
						while( keys.hasNext() ) {
						    String key = (String)keys.next();
						    if (object.has(key)) {
						    	try {
									object.put(key, values.get(key));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						}
						
						jsonResponse.put(object);
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
		
		
		public static int getMaxId(JSONArray jsonFile) {
			
			int maxId = 0;
			
			for(int i=0; i<jsonFile.length(); i++) {
				
				JSONObject object = jsonFile.optJSONObject(i);
				if (object == null) {
					return -1;
				}
				
				int id;
				
				try {
					id = object.getInt("id");
					if (id > maxId) {
						maxId = id;
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
