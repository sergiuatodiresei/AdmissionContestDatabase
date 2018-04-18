

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
		PrintWriter out = response.getWriter();
		
		out.print("Only POST requests :D");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		 } catch (Exception e) { 
			 out.print("{\"response\": -1}");
		 }

		try {
			
		    JSONObject jsonObject =  new JSONObject(jb.toString());
		    RequestParser requestParser = new RequestParser();
		    requestParser.parse(jsonObject);
		    
		    requestController(requestParser, out);
		    
		  } catch (JSONException e) {
		    // crash and burn
		    System.out.println("Error parsing JSON request string");
		    out.print("{\"response\": -1}");
		  }
		
	}
	
	
	public void requestController(RequestParser requestParser, PrintWriter response) {
		
		if(requestParser.getTable() == null  || requestParser.getOperation() == null) {
			response.print("{\"response\": -11}");
			return ;
		}
		
		System.out.println("Table:" + requestParser.getTable());
	    System.out.println("Operation:" + requestParser.getOperation());
	    System.out.println("Condition1:" + (requestParser.getCondition1() != null ? (requestParser.getCondition1().key + "\n" + requestParser.getCondition1().op + "\n" + requestParser.getCondition1().stringValue) : ""));
	    System.out.println("Condition2:" + (requestParser.getCondition2() != null ? (requestParser.getCondition2().key + "\n" + requestParser.getCondition2().op + "\n" + requestParser.getCondition2().stringValue) : ""));
		System.out.println("Logical condition: " + (requestParser.getLogicalCondition() != null ? requestParser.getLogicalCondition() : ""));
		
		
		if (requestParser.getCondition1() != null) {
			if (requestParser.getCondition1().op == RequestParser.OPERATOR.INVALID_OPERATOR) {
				response.print("{\"response\": -13}");
				return ; 
			}
			
			if (requestParser.getCondition1().key == null) {
				response.print("{\"response\": -14}");
				return ;
			} else if (requestParser.getCondition1().key.isEmpty()) {
				response.print("{\"response\": -14}");
				return ;
			}
			
			if (requestParser.getCondition1().stringValue == null) {
				response.print("{\"response\": -15}");
				return ;
			} else if (requestParser.getCondition1().stringValue.isEmpty()) {
				response.print("{\"response\": -15}");
				return ;
			}
			
		}
		
		if (requestParser.getCondition2() != null) {
			if (requestParser.getCondition2().op == RequestParser.OPERATOR.INVALID_OPERATOR) {
				response.print("{\"response\": -13}");
				return ; 
			}
			
			if (requestParser.getCondition2().key == null) {
				response.print("{\"response\": -14}");
				return ;
			} else if (requestParser.getCondition2().key.isEmpty()) {
				response.print("{\"response\": -14}");
				return ;
			}
			
			if (requestParser.getCondition2().stringValue == null) {
				response.print("{\"response\": -15}");
				return ;
			} else if (requestParser.getCondition2().stringValue.isEmpty()) {
				response.print("{\"response\": -15}");
				return ;
			}
			
		}
		
		
		String jsonFile = "";
		
		String pathFile = folder + requestParser.getTable() + ".json";
	
		if (ao.fileExist(pathFile)) {
			jsonFile = ao.downloadJson(pathFile);
		} else {
			
			if (requestParser.getOperation() != RequestParser.OPERATION.CREATE) {
				response.print("{\"response\": -10}");
				return;
			}
			
		}

		if (jsonFile.isEmpty() && requestParser.getOperation() != RequestParser.OPERATION.CREATE && requestParser.getOperation() != RequestParser.OPERATION.DROP &&  requestParser.getOperation() != RequestParser.OPERATION.INSERT) {
			response.print("{\"response\": -10}");
			return;
		}
		
		JSONArray jsonData;
		try {
			
			
			switch (requestParser.getOperation()) {
			case SELECT: 
				jsonData = new JSONArray(jsonFile);
				handleSelect(requestParser, jsonData, response);
				break;
			case INSERT: 
				jsonData = new JSONArray(jsonFile);
				handleInsert(requestParser, jsonData, pathFile, response);
				break;
			case UPDATE: 
				jsonData = new JSONArray(jsonFile);
				handleUpdate(requestParser, jsonData, pathFile, response);
				break;
			case DELETE: 
				jsonData = new JSONArray(jsonFile);
				handleDelete(requestParser, jsonData, pathFile, response);
				break;
			case CREATE: 
				handleCreate(requestParser, response);
				break;
			case DROP:
				handleDelete(requestParser, response);
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
	
	public void handleSelect(RequestParser requestParser, JSONArray jsonFile, PrintWriter response) {
		
		JSONArray filteredObjects = Utils.getFilteredObjects(requestParser, jsonFile);
		
		if (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) {
			
			response.print(filteredObjects.toString());
			
		} else if(filteredObjects.toString().equals("[{\"response\":-13}]")) {
				response.print("{\"response\": -13}");
			} else if(filteredObjects.toString().equals("[{\"response\":-23}]")) {
				response.print("{\"response\": -23}");
			} else {
				response.print("{\"response\": -24}");
			}
				
	}
	
	
	
	public void handleInsert(RequestParser requestParser, JSONArray jsonFile, String pathFile, PrintWriter response) {
		
		JSONObject values = requestParser.getValues();
		
		String table = requestParser.getTable();
	
		if (values == null || table == null) {
			response.print("{\"response\": -16}");
			return;
		}
		
		String jsonStructFile = "";
		
		if (ao.fileExist(folder + "struct_" + table + ".json")) {
			jsonStructFile = ao.downloadJson(folder + "struct_" + table + ".json");
		}
		
		
		if (requestParser.getTable().equals("students")) {
			
			String jsonCriteriiFile = ao.downloadJson(folder + "criterii_insert_students.json");
			
			if (jsonCriteriiFile.isEmpty()) {
				response.print("{\"response\": -17}");
				return;
			}
			
			JSONObject jsonCriterii;
			try {
				
				jsonCriterii = new JSONObject(jsonCriteriiFile);
				
				
				Double pondereBac = jsonCriterii.optDouble("pondere_bac");
				Double pondereExamen = jsonCriterii.optDouble("pondere_examen");
				
				
				if (!values.has("medie_bac")) {
					response.print("{\"response\": -18}");
					return;
				}
				
				if (!values.has("nota_examen")) {
					response.print("{\"response\": -19}");
					return;
				}
				
				Double medieBac = values.getDouble("medie_bac");
				Double notaExamen = values.getDouble("nota_examen");
				
				if (medieBac > 10 || medieBac < 5) {
					response.print("{\"response\": -20}");
					return;
				}
				
				if (notaExamen > 10 || notaExamen < 0) {
					response.print("{\"response\": -21}");
					return;
				}
				
				Double medie = medieBac * pondereBac + notaExamen * pondereExamen;
				
				values.put("medie", medie);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.print("{\"response\": -22}");
				return;
			}
		
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
				    }
				}
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.print("{\"response\": -22}");
			return;
		}
		
			
		jsonFile.put(values);
		
		ao.uploadJson(pathFile, jsonFile.toString());
		
		response.print("{\"response\": 1}");
		
		
	}
	
	
	public void handleUpdate(RequestParser requestParser, JSONArray jsonFile, String pathFile, PrintWriter response) {
		
		JSONArray updatedObjects = Utils.getUpdatedObjects(requestParser, jsonFile);
		
		if (!updatedObjects.toString().equals("[{\"response\":-13}]") && !updatedObjects.toString().equals("[{\"response\":-16}]") && !updatedObjects.toString().equals("[{\"response\":-23}]") &&  !updatedObjects.toString().equals("[{\"response\":-24}]")) {
			
			ao.uploadJson(pathFile, updatedObjects.toString());
			
			response.print("{\"response\": 1}");
		} else if (updatedObjects.toString().equals("[{\"response\":-13}]")) {
			response.print("{\"response\": -13}");
		} else if (updatedObjects.toString().equals("[{\"response\":-16}]")) {
			response.print("{\"response\": -16}");
		} else if (updatedObjects.toString().equals("[{\"response\":-23}]")) {
			response.print("{\"response\": -23}");
		} else if (updatedObjects.toString().equals("[{\"response\":-24}]")) {
			response.print("{\"response\": -24}");
		}
		
		
		
//		response.print("{\"response\": 1}");
	}
	
	
	public void handleDelete(RequestParser requestParser, JSONArray jsonFile, String pathFile, PrintWriter response) {
		
		JSONArray filteredObjects = Utils.getDifferenceFilteredObjects(requestParser, jsonFile);
		
		if (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) {
		
			ao.uploadJson(pathFile, filteredObjects.toString());
			response.print("{\"response\": 1}");
		} else if(filteredObjects.toString().equals("[{\"response\":-13}]")) {
			response.print("{\"response\": -13}");
		} else if(filteredObjects.toString().equals("[{\"response\":-23}]")) {
			response.print("{\"response\": -23}");
		} else {
			response.print("{\"response\": -24}");
		}
			
	}
	
	public void handleCreate(RequestParser requestParser, PrintWriter response) {
		
		
		if (requestParser.getTable() == null) {
			response.print("{\"response\": -11}");
			return;
		}
		
		String table = requestParser.getTable();
		
		JSONObject values = requestParser.getValues();
		
		if (values == null) {
			response.print("{\"response\": -16}");
			return;
		}
		
		String pathFile = folder + table + ".json";
		
		if (ao.fileExist(pathFile)) {
			response.print("{\"response\": 0}");
			return;
		}
		
		
		ao.uploadJson(pathFile, "[]");
		
		pathFile = folder + "struct_"+table + ".json";
		ao.uploadJson(pathFile, values.toString());
		
		response.print("{\"response\": 1}");
			
	}
	
	public void handleDelete(RequestParser requestParser, PrintWriter response) {
		
		if (requestParser.getTable() == null) {
			response.print("{\"response\": -11}");
			return;
		}
		
		String table = requestParser.getTable();
		
		if (ao.deleteFiles(table)) {
			response.print("{\"response\": 1}");
		} else {
			response.print("{\"response\": 0}");
		}
				
	}

	
}
