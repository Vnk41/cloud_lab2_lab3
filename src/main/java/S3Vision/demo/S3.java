package S3Vision.demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class S3 {
    public AmazonS3 getAmazonS3Client() {
        return amazonS3Client;
    }

    public void setAmazonS3Client(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }

    AmazonS3 amazonS3Client;
    String bucketname;

    public S3(){
        try {
            AWSCredentials credentials = new BasicAWSCredentials("fdLrBHqPiEai8cCvpAGRQ7", "67t6ZAXzrgyMHX244XQqKgyTHSvSGGf4dPMVbMAgMte");
            amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://hb.bizmrg.com", "ru-msk"))
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
            bucketname = "buckettest1";
            System.out.println(bucketname);
            //System.out.println(amazonS3Client.listBuckets());
            if (!amazonS3Client.doesBucketExistV2(bucketname)) {
                System.out.println(bucketname);
                amazonS3Client.createBucket(new CreateBucketRequest(bucketname));
            }
        }
     catch (AmazonServiceException e) {
        // The call was transmitted successfully, but Amazon S3 couldn't process
        // it and returned an error response.
        e.printStackTrace();
     } catch (SdkClientException e) {
        // Amazon S3 couldn't be contacted for a response, or the client
        // couldn't parse the response from Amazon S3.
        e.printStackTrace();
     }
    }

    public void createBucket (String bucketname)
    {
        if (!amazonS3Client.doesBucketExistV2(bucketname)) {
            System.out.println("Bucket exists!");
        }
        else{
            try {
                amazonS3Client.createBucket(bucketname);
            }
            catch (AmazonServiceException e)
            {
                System.out.println(e.getErrorMessage());
            }
        }
    }

    public void deleteBucket(String bucketname)
    {
        ObjectListing object_listing = amazonS3Client.listObjects(bucketname);
        while (true) {
            for (Iterator<?> iterator =
                 object_listing.getObjectSummaries().iterator();
                 iterator.hasNext(); ) {
                S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                amazonS3Client.deleteObject(bucketname, summary.getKey());
            }

            // more object_listing to retrieve?
            if (object_listing.isTruncated()) {
                object_listing = amazonS3Client.listNextBatchOfObjects(object_listing);
            } else {
                break;
            }
        }
    }

    public List<String> getBucketList()
    {
        List<Bucket> buckets = amazonS3Client.listBuckets();
        LinkedList<String> bucketNames = new LinkedList<String>();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
            bucketNames.add(b.getName());
        }
        return bucketNames;
    }


    public void loadFileToBucket(String bucketname, String file)
    {
        amazonS3Client.putObject(bucketname, file, file);
        System.out.println("File loaded to bucket!");
    }

    public void loadFileToBucket(String bucketname, String filename, File file)
    {
        amazonS3Client.putObject(bucketname, filename,file);
        System.out.println("File loaded to bucket!");
    }


    public void deleteFileFromBucket(String bucketname, String file)
    {
        amazonS3Client.deleteObject(bucketname, file);
        System.out.println("File deleted from bucket!");
    }

    public List<String> getBucketListFromFile(String bucketname)
    {
        LinkedList<String> filename = new LinkedList<String>();
        ListObjectsV2Result listObjects = amazonS3Client.listObjectsV2(bucketname);
        List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
        System.out.println("Your Amazon S3 buckets are:");
        for (S3ObjectSummary objectSummary : objectSummaries) {
            filename.add(objectSummary.getKey());
        }
        return filename;
    }
}
