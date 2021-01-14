package S3Vision.demo;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Controller
public class AppController {
    S3 amazonS3Client;

    @RequestMapping("/")
    public String homepage(Model model)
    {
        amazonS3Client = new S3();
        List<String> bucketlist = amazonS3Client.getBucketList();
        model.addAttribute("bucketlist", bucketlist);

        amazonS3Client.loadFileToBucket("buckettest1", "123");
        amazonS3Client.loadFileToBucket("buckettest1", "test2", new File("test2.jpg"));
        List<String> bucketListFiles = amazonS3Client.getBucketListFromFile("buckettest1");
        System.out.println(bucketListFiles);
        model.addAttribute("bucketlistFiles", bucketListFiles);

        //LinkedList<String> response = new Vision().
        //File FileFile = new File("test.jpg");
        //System.out.println(FileFile.getAbsolutePath());

        String response = new Vision().detector(new File ("test.jpg"));
        model.addAttribute("response", response);
        System.out.println(response);


        return "/home";
    }
}
