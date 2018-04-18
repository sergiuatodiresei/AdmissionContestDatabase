import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AmazonOperations {
	
	private AmazonS3 s3client = AmazonClient.getInstance().getS3Client();
	
	private String bucketName = "admission-contest";
	String folder = "database/";
	
public Boolean fileExist(String file) {
		
		ListObjectsV2Result result = s3client.listObjectsV2(bucketName);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		
		for (S3ObjectSummary os: objects) {
		    if (os.getKey().equals(file)) {
		    	return true;
		    }
		}
		
		return false;
	}
	

	
	public void uploadJson(String filename, String file) {
		
		byte[] bytesToWrite = file.getBytes();
		
		ObjectMetadata omd = new ObjectMetadata();
		omd.setContentLength(bytesToWrite.length);
		
		s3client.putObject(new PutObjectRequest(bucketName, filename, new ByteArrayInputStream(bytesToWrite), omd).withCannedAcl(CannedAccessControlList.PublicRead));		
	}
	
	public String downloadJson(String filename) {
		
		try {
		    S3Object o = s3client.getObject(bucketName, filename);
		    S3ObjectInputStream s3is = o.getObjectContent();
		    

		    byte[] read_buf = new byte[1024];
		    StringBuilder sb = new StringBuilder();
		    int read_len = 0;
		    while ((read_len = s3is.read(read_buf)) > 0) {
		        sb.append(new String(read_buf, "UTF-8"));
		    }
		    s3is.close();

		    return sb.toString();

		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		    return "";
		   
		} catch (FileNotFoundException e) {
		    System.err.println(e.getMessage());
		    return "";
		  
		} catch (IOException e) {
		    System.err.println(e.getMessage());
		    return "";
		}
	}
	
	public boolean deleteFiles(String table) {
		
		try {
		    
			String pathFileTable = folder + table + ".json";
			
			String pathFileStructTable = folder + "struct_"+table + ".json";
			
			DeleteObjectsRequest dor = new DeleteObjectsRequest(bucketName).withKeys(pathFileTable, pathFileStructTable);
		    s3client.deleteObjects(dor);
		    
		    return true;
		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		    return false;
		}
	}

}
