package S3Vision.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Vision {

    public String detector(File imageFile){
        String request = " curl -k -v \"https://smarty.mail.ru/api/v1/objects/detect?oauth_provider=mcs&oauth_token=okbu1UvYAdcFtc1uRZVTwap62tjwFfAWSJGtxnNayopygdufh\" \n" +
                "   -F file_0=@"+ imageFile.getPath() + " " +
                "-F meta=\"{\\\"mode\\\":[\\\"object\\\"],\\\"images\\\":[{\\\"name\\\":\\\"file_0\\\"}]}\"";
        String response = "";
        System.out.println(request);
        //LinkedList<String> response = new LinkedList<String>();
        String str;
        Process proc;
        try{
            proc = Runtime.getRuntime().exec(request);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((str = bufferedReader.readLine())!= null){
                response += str;
            }
            proc.waitFor();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
