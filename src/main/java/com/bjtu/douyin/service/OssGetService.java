package com.bjtu.douyin.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import java.io.InputStream;

import static com.bjtu.douyin.service.OssPushService.uploadFile;

public class OssGetService {

    private static final String ENDPOINT = "";
    private static final String BUCKET_NAME = "";
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";

    public static InputStream getFile(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            return ossClient.getObject(new GetObjectRequest(BUCKET_NAME, "video/" + objectName)).getObjectContent();
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }


//    public static void main(String[] args) {
//        String objectName = "video/examplevideo1.mp4";
//        String filePath = "D:/Users/NYLM/Desktop/QQ录屏20240616110917.mp4";
//
//        String url = uploadFile(objectName, filePath);
//        if (url != null) {
//            // 将URL传递给前端
//            System.out.println("URL to be sent to frontend: " + url);
//        }
//    }
}