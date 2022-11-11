package com.heima.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MinIOTest.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Slf4j
public class MinIOTest {

    public static void main(String[] args) {

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream("E:\\Users\\Desktop\\index.js");

            // 创建minio链接客户端
            MinioClient minioClient = MinioClient
                    .builder()
                    .credentials("minio", "minio123") // 用户名和密码
                    .endpoint("http://192.168.200.130:9000")
                    .build();

            // 上传
            PutObjectArgs putObjectArgs = PutObjectArgs
                    .builder()
                    .object("plugins/js/index.js") // 文件名
                    .contentType("text/js") // 文件类型
                    .bucket("leadnews") // 桶名称, 与minio创建的桶名称一致
                    .stream(fileInputStream, fileInputStream.available(), -1) // 文件流
                    .build();

            minioClient.putObject(putObjectArgs);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        }

    }
}
