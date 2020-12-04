package com.jk.lightPole.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zerofwen
 * @CreateDate: 20/11/19 14:49
 */
@Service
public class ClientServiceDefault implements ClientService {
    Logger logger = LoggerFactory.getLogger(ClientServiceDefault.class);

    @Value("${client.file.json:F:\\Program Files (x86)\\desktop\\temp.txt}")
    String jsonFile;

    @Value("${client.file.img}")
    String imageFile;

    @Value("${service.uri}")
    String sendUri;

    String tempData;

    Boolean success = false;

    public static void main(String[] args) {
        ClientServiceDefault clientServiceDefault = new ClientServiceDefault();
        clientServiceDefault.sendJson();
    }

    @Override
    @Scheduled(cron = "0/5 * *  * * ? ")
    public void sendJson() {
        System.out.println("start");
	System.out.println(jsonFile);
        //读取文件
        File file = new File(jsonFile);
		
        BufferedInputStream bis = null;

        FileInputStream fis = null;

        try {
            //第一步 通过文件路径来创建文件实例
            fis = new FileInputStream(file);

          /*把FileInputStream实例 传递到 BufferedInputStream
            目的是能快速读取文件
           */

            bis = new BufferedInputStream(fis);

            byte[] tempChar = new byte[bis.available()];
            /*available检查是不是读到了文件末尾 */
            System.out.println(bis.available());
            while (bis.available() > 0) {
                System.out.println("" + bis.read(tempChar));
                System.out.println(new String(tempChar));
            }
            String str = new String(tempChar);
            if (tempData == null || !success || !tempData.equals(str)) {//是否提交数据
                JSONObject json = JSONObject.parseObject(str);
                System.out.println(json.toJSONString());
                Map item = new HashMap();
                item.put("lightPoleSn", json.getString("lightPoleSn"));
                item.put("lightPoleName", json.getString("lightPoleName"));
                item.put("lightPoleLocation", json.getString("lightPoleLocation"));
                item.put("totalParking", json.getString("totalParking"));
                item.put("recognitionData", json.getJSONArray("recognitionData"));
                HttpRequest httpRequest = HttpUtil.createPost(sendUri);
                String result = httpRequest.body(JSONObject.toJSONString(item)).execute().body();
                System.out.println(result);
                if ("success".equals(result)) success = true;
                else success = false;
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println("文件不存在" + fnfe);
        } catch (IOException ioe) {
            System.out.println("I/O 错误: " + ioe);
        } catch (Exception e) {
            System.out.println("其他异常 : " + e);
        } finally {
            try {
                if (bis != null && fis != null) {
                    fis.close();
                    bis.close();
                }
            } catch (IOException ioe) {
                System.out.println("关闭InputStream句柄错误: " + ioe);
            }
        }
    }

    @Override
    public void sendFile() {

    }

}
