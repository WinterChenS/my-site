package cn.luischen.api;

import cn.luischen.utils.Commons;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

/**
 * 七牛云对象存储API
 * Created by Donghua.Chen on 2018/3/25.
 */
@Service
public class QCloudService {

    public static final String SECRETID = "AKIDZ2KRludhKn4FHAGQo4B8xeBXwGPNalBQ";
    public static final String SECRETKEY = "CPWUJg8UhniS3TmuwmRvEOQK5jFcopfJ";
    //bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
    public static final String BUCKET = "pic-1253516459";
    public static final String UPLOAD_URL = "http://" + BUCKET + ".cos.ap-shanghai.myqcloud.com/";


    public COSClient getCOSClient() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(SECRETID, SECRETKEY);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);

        return cosclient;
    }

    public String fileUpLoad(MultipartFile file) {

        COSClient cosClient = this.getCOSClient();
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        // 指定要上传到 COS 上的路径
        String key = Commons.getFileRename(file.getOriginalFilename());
        PutObjectRequest putObjectRequest = null;
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置输入流长度为 500
            objectMetadata.setContentLength(500);
            // 设置 Content type, 默认是 application/octet-stream
            objectMetadata.setContentType(file.getContentType());
            putObjectRequest = new PutObjectRequest(BUCKET, key, file.getInputStream(),objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(BUCKET, key, HttpMethodName.GET);
        URL url = cosClient.generatePresignedUrl(req);

        cosClient.shutdown();

        return url.toString();

    }
}
