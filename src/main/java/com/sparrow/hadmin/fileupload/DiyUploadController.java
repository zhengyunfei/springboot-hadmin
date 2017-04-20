package com.sparrow.hadmin.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


/**
 * DiyUploadController图片上传
 * @author 贤云
 * @createDate 2017-03-30
 */
@Controller
public class DiyUploadController {
    private static final long serialVersionUID = 1L;
    @Autowired
    private Environment env;
    //上传文件的保存路径
    protected String dirTemp = "upload/temp";


    @RequestMapping(value = "/html5/upload.html", method = RequestMethod.POST)
    @ResponseBody
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        //文件保存目录路径
        //文件保存目录路径
        String localPath = env.getProperty("REAL_SAVE_IMAGE_PATH");
        String preImageDomain = env.getProperty("UPLOAD_IMAGE_SERVER_PRE_URL");

        String compon= localPath;
        String savePath = compon;

        // 临时文件目录
        String tempPath =savePath+ dirTemp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String ymd = sdf.format(new Date());
        String imageDate="/image/" + ymd + "/";
        savePath += imageDate;
        //回调路径
        // hdUrl+=configPath + ymd + "/";
        //创建文件夹
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        tempPath += "/" + ymd + "/";
        //创建临时文件夹
        File dirTempFile = new File(tempPath);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        try{
            Part part = request.getPart("file");// myFileName是文件域的name属性值
            // 文件类型限制
            String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png" };
            boolean allowed = Arrays.asList(allowedType).contains(part.getContentType());
            if (!allowed) {
                response.getWriter().write("error|不支持的类型");
                return;
            }
            String SizeThreshold=env.getProperty("FILE_SIZE_MAX");
            long maxSize=Long.parseLong(SizeThreshold);

            // 图片大小限制
            if (part.getSize() > maxSize) {
                response.getWriter().write("error|图片大小不能超过5M");
                return;
            }
            // 包含原始文件名的字符串
            String fi = part.getHeader("content-disposition");
            // 提取文件拓展名
            String fileNameExtension = fi.substring(fi.indexOf("."), fi.length() - 1);
            // 生成实际存储的真实文件名
            String realName = UUID.randomUUID().toString() + fileNameExtension;
            // 图片存放的真实路径
            String realPath = savePath + "/" + realName;
            // 将文件写入指定路径下
            part.write(realPath);
            // 返回图片的URL地址
            response.getWriter().write(preImageDomain+imageDate+ realName);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

}
