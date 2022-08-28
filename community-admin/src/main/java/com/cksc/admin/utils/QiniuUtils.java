package com.cksc.admin.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class QiniuUtils {

    //public static  final String url = "https://static.mszlu.com/";
    public static  final String url = "https://static.cmyshare.cn/";

    @Value("${qiniu.accessKey}") //根据maven中qiniu配置 得到值
    private  String accessKey;
    @Value("${qiniu.accessSecretKey}")
    private  String accessSecretKey;

    public  boolean upload(MultipartFile file,String fileName){

        //构造一个带指定 Region 对象的配置类 根据空间位置设置Region.huanan()
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String bucket = "cmyshare";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            //字节数组上传
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
                Response response = uploadManager.put(uploadBytes, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return false;
    }
}
