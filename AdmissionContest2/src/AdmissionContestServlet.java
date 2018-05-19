

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.codec.json.JsonObjectDecoder;


/**
 * Servlet implementation class AdmissionContestServlet
 */

@WebServlet("/AdmissionContestServlet")
public class AdmissionContestServlet extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AmazonOperations ao = new AmazonOperations();
	
	String folder = "database/";
	
	String table;
	RequestParser.OPERATION operation;

    /**
     * Default constructor. 
     */
    public AdmissionContestServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		out.print("Only POST requests :D");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		 } catch (Exception e) { 
			 out.print("{\"response\": -1}");
			 response.setStatus(400);
			 
		 }

		try {
			
		    JSONObject jsonObject =  new JSONObject(jb.toString());
		    RequestParser requestParser = new RequestParser();
		    requestParser.parse(jsonObject);
		    
		    table = requestParser.getTable();
		    operation = requestParser.getOperation();
		    
		    requestController(requestParser,response, out);
		    
		  } catch (JSONException e) {
		    // crash and burn
		    System.out.println("Error parsing JSON request string");
		    out.print("{\"response\": -1}");
		  }
		
	}
	
	private boolean areTableAndOperationOK() {
		
		if(table == null  || table.isEmpty() || operation == null || operation == RequestParser.OPERATION.INVALID_OPERATION) {
			return false;
		}
		return true;
		
	}
	
	
	public void requestController(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
		//invariant
		assert areTableAndOperationOK() : "The tabel or operation is invalid";
		
		if(requestParser.getTable() == null  || requestParser.getOperation() == null) {
			response.print("{\"response\": -11}");
			httpResponse.setStatus(400);
			return ;
		}
		
		System.out.println("Table:" + requestParser.getTable());
	    System.out.println("Operation:" + requestParser.getOperation());
	    System.out.println("Condition1:" + (requestParser.getCondition1() != null ? (requestParser.getCondition1().key + "\n" + requestParser.getCondition1().op + "\n" + requestParser.getCondition1().stringValue) : ""));
	    System.out.println("Condition2:" + (requestParser.getCondition2() != null ? (requestParser.getCondition2().key + "\n" + requestParser.getCondition2().op + "\n" + requestParser.getCondition2().stringValue) : ""));
		System.out.println("Logical condition: " + (requestParser.getLogicalCondition() != null ? requestParser.getLogicalCondition() : ""));
		
		
		if (requestParser.getCondition1() != null) {
			
			//preconditions
			assert requestParser.getCondition1().op != RequestParser.OPERATOR.INVALID_OPERATOR: "The first operator is invalid";
			
			assert requestParser.getCondition1().key != null && !requestParser.getCondition1().key.isEmpty(): "The first key is invalid";
			
			assert requestParser.getCondition1().stringValue != null && !requestParser.getCondition1().stringValue.isEmpty(): "The first value is invalid";
			
			if (requestParser.getCondition1().op == RequestParser.OPERATOR.INVALID_OPERATOR) {
				response.print("{\"response\": -13}");
				httpResponse.setStatus(400);
				return ; 
			}
			
			if (requestParser.getCondition1().key == null) {
				response.print("{\"response\": -14}");
				httpResponse.setStatus(400);
				return ;
			} else if (requestParser.getCondition1().key.isEmpty()) {
				response.print("{\"response\": -14}");
				httpResponse.setStatus(400);
				return ;
			}
			
			if (requestParser.getCondition1().stringValue == null) {
				response.print("{\"response\": -15}");
				httpResponse.setStatus(400);
				return ;
			} else if (requestParser.getCondition1().stringValue.isEmpty()) {
				response.print("{\"response\": -15}");
				httpResponse.setStatus(400);
				return ;
			}
			
		}
		
		if (requestParser.getCondition2() != null) {
			
			//preconditions
			assert requestParser.getCondition2().op != RequestParser.OPERATOR.INVALID_OPERATOR: "The second operator is invalid";
			
			assert requestParser.getCondition2().key != null && !requestParser.getCondition2().key.isEmpty(): "The second key is invalid";
			
			assert requestParser.getCondition2().stringValue != null && !requestParser.getCondition2().stringValue.isEmpty(): "The second value is invalid";
			
			
			if (requestParser.getCondition2().op == RequestParser.OPERATOR.INVALID_OPERATOR) {
				response.print("{\"response\": -13}");
				httpResponse.setStatus(400);
				return ; 
			}
			
			if (requestParser.getCondition2().key == null) {
				response.print("{\"response\": -14}");
				httpResponse.setStatus(400);
				return ;
			} else if (requestParser.getCondition2().key.isEmpty()) {
				response.print("{\"response\": -14}");
				httpResponse.setStatus(400);
				return ;
			}
			
			if (requestParser.getCondition2().stringValue == null) {
				response.print("{\"response\": -15}");
				httpResponse.setStatus(400);
				return ;
			} else if (requestParser.getCondition2().stringValue.isEmpty()) {
				response.print("{\"response\": -15}");
				httpResponse.setStatus(400);
				return ;
			}
			
		}
		
		
		String jsonFile = "";
		
		String pathFile = folder + requestParser.getTable() + ".json";
	
		if (ao.fileExist(pathFile)) {
			jsonFile = ao.downloadJson(pathFile);
		} else {
			
			//precondition
			assert requestParser.getOperation() == RequestParser.OPERATION.CREATE: "The table does not exist";
			
			if (requestParser.getOperation() != RequestParser.OPERATION.CREATE) {
				response.print("{\"response\": -10}");
				httpResponse.setStatus(400);
				return;
			}
			
		}

		if (jsonFile.isEmpty() && requestParser.getOperation() != RequestParser.OPERATION.CREATE && requestParser.getOperation() != RequestParser.OPERATION.DROP &&  requestParser.getOperation() != RequestParser.OPERATION.INSERT) {
			response.print("{\"response\": -10}");
			httpResponse.setStatus(400);
			return;
		}
		
		JSONArray jsonData;
		try {
			
			
			switch (requestParser.getOperation()) {
			case SELECT: 
				
				if(requestParser.getTable().equals("criterii_insert_students")) {
					
					//String jsonCriteriiFile = ao.downloadJson(folder + "criterii_insert_students.json");
					response.print(Utils.jsonCriteriiFile);
					return;
				}
				
				jsonData = new JSONArray(jsonFile);
				handleSelect(requestParser, jsonData, httpResponse, response);
				break;
			case INSERT: 
				jsonData = new JSONArray(jsonFile);
				handleInsert(requestParser, jsonData, pathFile, httpResponse, response);
				break;
			case INSERT_ARRAY: 
				jsonData = new JSONArray(jsonFile);
				handleInsertArray(requestParser, jsonData, pathFile, httpResponse, response);
				break;
			case UPDATE: 
				jsonData = new JSONArray(jsonFile);
				handleUpdate(requestParser, jsonData, pathFile, httpResponse, response);
				break;
			case DELETE: 
				jsonData = new JSONArray(jsonFile);
				handleDelete(requestParser, jsonData, pathFile, httpResponse, response);
				break;
			case CREATE: 
				handleCreate(requestParser, httpResponse, response);
				break;
			case DROP:
				handleDrop(requestParser, httpResponse, response);
				break;
			case  INVALID_OPERATION: 
				response.print("{\"response\": -1}");
				break;
			default:
				break;
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.print("{\"response\": -12}");
		}
		
		
	}
	
	public void handleSelect(RequestParser requestParser, JSONArray jsonFile,  HttpServletResponse httpResponse, PrintWriter response) {
		
		JSONArray filteredObjects = Utils.getFilteredObjects(requestParser, jsonFile);
		
		//postconditions
		assert (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) : "Error select";
		
		if (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) {
			
			response.print(filteredObjects.toString());
			
		} else if(filteredObjects.toString().equals("[{\"response\":-13}]")) {
				response.print("{\"response\": -13}");
				httpResponse.setStatus(400);
			} else if(filteredObjects.toString().equals("[{\"response\":-23}]")) {
				response.print("{\"response\": -23}");
				httpResponse.setStatus(400);
			} else {
				response.print("{\"response\": -24}");
				httpResponse.setStatus(400);
			}
				
	}
	
	
	
	public void handleInsert(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
		JSONObject values = requestParser.getValues();
		
		//precondition
		assert values != null : "Values are invalid";
	
		if (values == null || table == null) {
			response.print("{\"response\": -16}");
			httpResponse.setStatus(400);
			return;
		}
		
		
		String jsonStructFile = "";
		
		if (table.equals("students")) {
			jsonStructFile = Utils.structStudents;
		} else if (ao.fileExist(folder + "struct_" + table + ".json")) {
			jsonStructFile = ao.downloadJson(folder + "struct_" + table + ".json");
		}
		
		try {
			values.put("id", Utils.getMaxId(jsonFile) + 1);
		
		
			if (!jsonStructFile.isEmpty()) {
				
				JSONObject jsonStruct = new JSONObject(jsonStructFile);
				
				Iterator<?> keys = jsonStruct.keys();
	
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if (!values.has(key)) {
				    	values.put(key, "");
				    } else {
				    	if (jsonStruct.getString(key).equals("number")) {
				    		
				    		if (values.get(key) instanceof String) {
				    			response.print("{\"response\": -30}");
				    			httpResponse.setStatus(400);
				    			return;
				    		}
				    		
				    	} else {
				    		if (values.get(key) instanceof Double) {
				    			response.print("{\"response\": -30}");
				    			httpResponse.setStatus(400);
				    			return;
				    		}
				    		if (values.get(key) instanceof Float) {
				    			response.print("{\"response\": -30}");
				    			httpResponse.setStatus(400);
				    			return;
				    		}
				    		if (values.get(key) instanceof Integer) {
				    			response.print("{\"response\": -30}");
				    			httpResponse.setStatus(400);
				    			return;
				    		}
				    	}
				    }
				}
				
				keys = values.keys();
				
				JSONObject valuesTemp = new JSONObject(values.toString());
				
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if (!jsonStruct.has(key) && !key.equals("id")) {
				    	
				    	valuesTemp.remove(key);
				    } 
				}
				
				values = valuesTemp;
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.print("{\"response\": -22}");
			httpResponse.setStatus(400);
			return;
		}
		
		
		if (requestParser.getTable().equals("students")) {
			
//			String jsonCriteriiFile = ao.downloadJson(folder + "criterii_insert_students.json");
			String jsonCriteriiFile = Utils.jsonCriteriiFile;
			
//			if (jsonCriteriiFile.isEmpty()) {
//				response.print("{\"response\": -17}");
//				httpResponse.setStatus(400);
//				return;
//			}
			
			JSONObject jsonCriterii;
			try {
				
				jsonCriterii = new JSONObject(jsonCriteriiFile);
				
				
				Double pondereBac = jsonCriterii.optDouble("pondere_bac");
				Double pondereExamen = jsonCriterii.optDouble("pondere_examen");
				
				
				if (!values.has("medie_bac")) {
					response.print("{\"response\": -18}");
					httpResponse.setStatus(400);
					return;
				}
				
				if (!values.has("nota_examen")) {
					response.print("{\"response\": -19}");
					httpResponse.setStatus(400);
					return;
				}
				
				Double medieBac = values.getDouble("medie_bac");
				Double notaExamen = values.getDouble("nota_examen");
				
				if (medieBac > 10 || medieBac < 5) {
					response.print("{\"response\": -20}");
					httpResponse.setStatus(400);
					return;
				}
				
				if (notaExamen > 10 || notaExamen < 0) {
					response.print("{\"response\": -21}");
					httpResponse.setStatus(400);
					return;
				}
				
				Double medie = medieBac * pondereBac + notaExamen * pondereExamen;
				
				values.put("medie", medie);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.print("{\"response\": -22}");
				httpResponse.setStatus(400);
				return;
			}
		
		}
		
			
		jsonFile.put(values);
		
		//postcondition
		assert !jsonFile.toString().isEmpty(): "Cannot insert empty student";
		
		ao.uploadJson(pathFile, jsonFile.toString());
		
		response.print("{\"response\": 1}");
		
		
	}
	
	public void handleInsertArray(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
		JSONArray values= requestParser.getValuesArray();
		
		//precondition
		assert values != null && values.length() != 0: "Values are invalid";
	
		if (values == null || table == null) {
			response.print("{\"response\": -16}");
			httpResponse.setStatus(400);
			return;
		}
		
		if (values.length() == 0) {
			response.print("{\"response\": -16}");
			httpResponse.setStatus(400);
			return;
		}
		
		String jsonStructFile = "";
		
		if (table.equals("students")) {
			jsonStructFile = Utils.structStudents;
		} else if (ao.fileExist(folder + "struct_" + table + ".json")) {
			jsonStructFile = ao.downloadJson(folder + "struct_" + table + ".json");
		}
		
	
		if (requestParser.getTable().equals("students")) {
			
//			String jsonCriteriiFile = ao.downloadJson(folder + "criterii_insert_students.json");
			
			String jsonCriteriiFile = Utils.jsonCriteriiFile;
			
//			if (jsonCriteriiFile.isEmpty()) {
//				response.print("{\"response\": -17}");
//				httpResponse.setStatus(400);
//				return;
//			}
			
			JSONObject jsonCriterii;
			try {
				
				jsonCriterii = new JSONObject(jsonCriteriiFile);
				
				
				Double pondereBac = jsonCriterii.optDouble("pondere_bac");
				Double pondereExamen = jsonCriterii.optDouble("pondere_examen");
				
				
				for(int i=0; i<values.length(); i++) {
					
					JSONObject value = values.optJSONObject(i);
					
					if (value == null) {
						response.print("{\"response\": -16}");
						httpResponse.setStatus(400);
						return;
					}
				
					if (!value.has("medie_bac")) {
						response.print("{\"response\": -18}");
						httpResponse.setStatus(400);
						return;
					}
					
					if (!value.has("nota_examen")) {
						response.print("{\"response\": -19}");
						httpResponse.setStatus(400);
						return;
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
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
						    		}
						    		
						    	} else {
						    		if (value.get(key) instanceof Double) {
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
						    		}
						    		if (value.get(key) instanceof Float) {
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
						    		}
						    		if (value.get(key) instanceof Integer) {
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
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
						response.print("{\"response\": -20}");
						httpResponse.setStatus(400);
						return;
					}
					
					if (notaExamen > 10 || notaExamen < 0) {
						response.print("{\"response\": -21}");
						httpResponse.setStatus(400);
						return;
					}
					
					Double medie = medieBac * pondereBac + notaExamen * pondereExamen;
					
					value.put("medie", medie);
					
					jsonFile.put(value);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.print("{\"response\": -22}");
				httpResponse.setStatus(400);
				return;
			}
		
		} else {
		
			try {
				
				for(int i=0; i<values.length(); i++) {
					
					JSONObject value = values.optJSONObject(i);
					
					if (value == null) {
						response.print("{\"response\": -16}");
						httpResponse.setStatus(400);
						return;
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
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
						    		}
						    		
						    	} else {
						    		if (value.get(key) instanceof Double) {
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
						    		}
						    		if (value.get(key) instanceof Float) {
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
						    		}
						    		if (value.get(key) instanceof Integer) {
						    			response.print("{\"response\": -30}");
						    			httpResponse.setStatus(400);
						    			return;
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
					jsonFile.put(value);
				}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.print("{\"response\": -22}");
				httpResponse.setStatus(400);
				return;
			}
		
		}
		
		//postcondition
		assert !jsonFile.toString().isEmpty(): "Cannot insert empty students ";
		
		ao.uploadJson(pathFile, jsonFile.toString());
			
		response.print("{\"response\": 1}");
		
		
	}
	
	
	public void handleUpdate(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
		String jsonStructFile = "";
		
		if (requestParser.getTable().equals("students")) {
			jsonStructFile = Utils.structStudents;
		} else if (ao.fileExist(folder + "struct_" + requestParser.getTable() + ".json")) {
			jsonStructFile = ao.downloadJson(folder + "struct_" + requestParser.getTable() + ".json");
		}
		
		//precondition
		assert !jsonStructFile.isEmpty(): "Struct file does not exist";
		
		if (jsonStructFile.isEmpty()) {
			response.print("{\"response\": -1}");
			return;
		}
		
		JSONObject jsonStruct = new JSONObject();
		try {
			jsonStruct = new JSONObject(jsonStructFile);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			response.print("{\"response\": -1}");
			httpResponse.setStatus(400);
			e.printStackTrace();
		}
		
		JSONArray updatedObjects = Utils.getUpdatedObjects(requestParser, jsonFile, jsonStruct);
		
		//postcondition
		assert (!updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -13}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -23}\"]") 
				&&  !updatedObjects.toString().equals("[\"{\\\"response\\\": -24}\"]") 
				&&  !updatedObjects.toString().equals("[\"{\\\"response\\\": -30}\"]")): "Error Update";
		
		if (!updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]") && !updatedObjects.toString().equals("[\"{\\\"response\\\": -13}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]") && !updatedObjects.toString().equals("[\"{\\\"response\\\": -23}\"]") 
				&&  !updatedObjects.toString().equals("[\"{\\\"response\\\": -24}\"]") &&  !updatedObjects.toString().equals("[\"{\\\"response\\\": -30}\"]")) {
			
			ao.uploadJson(pathFile, updatedObjects.toString());
			
			response.print("{\"response\": 1}");
		}else if (updatedObjects.toString().equals("[\"{\\\"response\\\": -1}\"]")) {
			response.print("{\"response\": -1}");
			httpResponse.setStatus(400);
		} else if (updatedObjects.toString().equals("[\"{\\\"response\\\": -13}\"]")) {
			response.print("{\"response\": -13}");
			httpResponse.setStatus(400);
		} else if (updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]")) {
			response.print("{\"response\": -16}");
			httpResponse.setStatus(400);
		} else if (updatedObjects.toString().equals("[\"{\\\"response\\\": -23}\"]")) {
			response.print("{\"response\": -23}");
			httpResponse.setStatus(400);
		} else if (updatedObjects.toString().equals("[\"{\\\"response\\\": -24}\"]")) {
			response.print("{\"response\": -24}");
			httpResponse.setStatus(400);
		} else if (updatedObjects.toString().equals("[\"{\\\"response\\\": -30}\"]")) {
			response.print("{\"response\": -30}");
			httpResponse.setStatus(400);
		}
		
	}
	
	
	public void handleDelete(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
		JSONArray filteredObjects = Utils.getDifferenceFilteredObjects(requestParser, jsonFile);
		
		//postcondition
		assert (!filteredObjects.toString().equals("[{\"response\":-13}]") && 
				!filteredObjects.toString().equals("[{\"response\":-23}]") && 
				!filteredObjects.toString().equals("[{\"response\":-24}]")) : "Error Delete";
		
		if (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) {
		
			ao.uploadJson(pathFile, filteredObjects.toString());
			response.print("{\"response\": 1}");
		} else if(filteredObjects.toString().equals("[{\"response\":-13}]")) {
			response.print("{\"response\": -13}");
			httpResponse.setStatus(400);
		} else if(filteredObjects.toString().equals("[{\"response\":-23}]")) {
			response.print("{\"response\": -23}");
			httpResponse.setStatus(400);
		} else {
			response.print("{\"response\": -24}");
			httpResponse.setStatus(400);
		}
			
	}
	
	public void handleCreate(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
		
		JSONObject values = requestParser.getValues();
		
		//precondition
		assert values != null: "Values are not valid";
		
		if (values == null) {
			response.print("{\"response\": -16}");
			httpResponse.setStatus(400);
			return;
		}
		
		String pathFile = folder + table + ".json";
		
		if (ao.fileExist(pathFile)) {
			response.print("{\"response\": 0}");
			httpResponse.setStatus(400);
			return;
		}
		
		Iterator<?> keys = values.keys();
		
		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    
		    try {
				if (!values.getString(key).equals("number") && !values.getString(key).equals("text")) {
					response.print("{\"response\": -29}");
					httpResponse.setStatus(400);
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				response.print("{\"response\": -29}");
				httpResponse.setStatus(400);
				return;
			}
		}
		
		ao.uploadJson(pathFile, "[]");
		
		pathFile = folder + "struct_"+table + ".json";
		
		//postcondition
		assert !values.toString().isEmpty(): "Error Create";
		
		ao.uploadJson(pathFile, values.toString());
		
		response.print("{\"response\": 1}");
			
	}
	
	public void handleDrop(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
		if (ao.deleteFiles(table)) {
			response.print("{\"response\": 1}");
		} else {
			response.print("{\"response\": 0}");
			httpResponse.setStatus(400);
		}
				
	}

	
}
