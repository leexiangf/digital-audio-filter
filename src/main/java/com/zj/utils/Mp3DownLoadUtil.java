package com.zj.utils;

import com.sun.istack.internal.Nullable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Classname Mp3DownLoadUtil
 * @Description TODO
 * @Date 2022/3/18 14:24
 * @Created by lxf
 */
public class Mp3DownLoadUtil {


        //默认文件路径
        public final static String DEFAULT_PATH="./data/";


        /**
         *  通过url下载文件到本地
         * @param path  url地址
         * @param localPath  本地地址
         */
        public static boolean downLoadUrl(String urlPath,String localPath)  {
             byte[] bytes = new byte[1024];
            URL url ;
            InputStream inputStream ;
            FileOutputStream fileOutputStream ;
            int n;
            try {
                url = new URL(urlPath);
                inputStream = url.openStream();
                fileOutputStream = new FileOutputStream(localPath);
                while ((n=inputStream.read(bytes)) != -1){
                    fileOutputStream.write(bytes);
                }
                inputStream.close();
                fileOutputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }


    /**
     *  删除文件夹下的某个文件
     * @param fileName  文件名
     * @param FilePath   文件路径
     * @return
     * @throws FileNotFoundException
     */
        public static boolean deleteFile(String fileName,String FilePath) throws FileNotFoundException {
            File file = new File(FilePath+fileName);
           if(!file.exists()){
            throw new FileNotFoundException(fileName+": is  not found");
           }
           return file.delete();
        }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(deleteFile("1234", DEFAULT_PATH));
    }


}
