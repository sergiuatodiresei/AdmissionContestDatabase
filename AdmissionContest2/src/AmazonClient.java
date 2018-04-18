import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AmazonClient {
	
	private static final AmazonClient instance = new AmazonClient();
	
	
	
	
	private AmazonS3 s3client;
	
	private AmazonClient() {
		
		s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();

	}
	
	public static AmazonClient getInstance() {
		return instance;
	}
	
	public AmazonS3 getS3Client() {
		return s3client;
	}

}
