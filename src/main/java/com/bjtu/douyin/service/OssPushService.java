package com.bjtu.douyin.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class OssPushService {

    //使用群里的阿里云密钥替换
    private static final String ENDPOINT = "";
    private static final String BUCKET_NAME = "";
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";

    public static String uploadFile(String objectName, String filePath) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            System.out.println("Starting file upload...");
            InputStream inputStream = new FileInputStream(new File(filePath));
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, inputStream);
            ossClient.putObject(putObjectRequest);
            System.out.println("File uploaded successfully to OSS.");

            // 设置文件权限为公共可读
            ossClient.setObjectAcl(BUCKET_NAME, objectName, CannedAccessControlList.PublicRead);

            // 生成文件的永久URL-
            String url = String.format("https://%s.%s/%s", BUCKET_NAME, ENDPOINT.replace("https://", ""), objectName);
            System.out.println("File URL: " + url);
            return url;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught a ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
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