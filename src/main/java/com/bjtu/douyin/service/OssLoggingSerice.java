package com.bjtu.douyin.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public class OssLoggingSerice {
    private static final String ENDPOINT = "";
    private static final String BUCKET_NAME = "";
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";
    private static final String TARGET_BUCKET_NAME = "";
    private static final String TARGET_PREFIX = "";

    public static void enableBucketLogging(String bucketName, String targetBucketName, String targetPrefix) throws Exception {
        String method = "PUT";
        String contentType = "application/xml";
        String date = getCurrentDate();

        String resource = "/" + bucketName + "/?logging";
        String stringToSign = method + "\n\n" + contentType + "\n" + date + "\n" + resource;

        String signature = generateSignature(stringToSign);

        String authorization = "OSS " + ACCESS_KEY_ID + ":" + signature;

        String requestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<BucketLoggingStatus>\n" +
                "  <LoggingEnabled>\n" +
                "    <TargetBucket>" + targetBucketName + "</TargetBucket>\n" +
                "    <TargetPrefix>" + targetPrefix + "</TargetPrefix>\n" +
                "  </LoggingEnabled>\n" +
                "</BucketLoggingStatus>";

        URL url = new URL("http://" + bucketName + "." + ENDPOINT + "/?logging");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Date", date);
        connection.setRequestProperty("Authorization", authorization);

        byte[] outputBytes = requestXml.getBytes("UTF-8");
        connection.setRequestProperty("Content-Length", String.valueOf(outputBytes.length));

        System.out.println("Sending request to " + url);
        System.out.println("Request headers:");
        System.out.println("Content-Type: " + contentType);
        System.out.println("Date: " + date);
        System.out.println("Authorization: " + authorization);

        System.out.println("Request body:");
        System.out.println(requestXml);

        long startTime = System.currentTimeMillis();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(outputBytes);
        }

        int responseCode = connection.getResponseCode();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        InputStream inputStream;
        if (responseCode >= 200 && responseCode < 300) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String responseBody = result.toString("UTF-8");

        System.out.println("Response code: " + responseCode);
        System.out.println("Response body:");
        System.out.println(responseBody);
        System.out.println("Request duration: " + duration + " ms");

        if (responseCode == 200) {
            System.out.println("Bucket logging enabled successfully.");
        } else {
            System.out.println("Failed to enable bucket logging. Response code: " + responseCode);
        }
    }

    //格式化当前日期为HTTP请求头需要的日期格式
    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(new Date());
    }

    //使用HMAC-SHA1算法对待签名字符串进行签名，然后使用Base64编码生成签名字符串
    private static String generateSignature(String stringToSign) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(ACCESS_KEY_SECRET.getBytes("UTF-8"), "HmacSHA1"));
        byte[] rawHmac = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(rawHmac);
    }

//    public static void main(String[] args) {
//        try {
//            enableBucketLogging(BUCKET_NAME, TARGET_BUCKET_NAME, TARGET_PREFIX);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
