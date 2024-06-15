package com.bjtu.douyin.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.FileInputStream;
import java.io.InputStream;

public class OssUtil {

//    <!--阿里云OSS上传-->
//		<dependency>
//			<groupId>com.aliyun.oss</groupId>
//			<artifactId>aliyun-sdk-oss</artifactId>
//			<version>3.10.2</version> <!-- 使用最新的版本 -->
//		</dependency>

//    public static void main(String[] args) throws Exception {
//        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
//        EnvironmentVariableCredentialsProvider credentialsProvider = new EnvironmentVariableCredentialsProvider();
//        String bucketName = "examplebucket";
//        String objectName = "exampledir/examplevideo.mp4";
//        String filePath = "D:\\localpath\\examplevideo.mp4";
//
//        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
//
//        try {
//            InputStream inputStream = new FileInputStream(filePath);
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
//            PutObjectResult result = ossClient.putObject(putObjectRequest);
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught a ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
}
