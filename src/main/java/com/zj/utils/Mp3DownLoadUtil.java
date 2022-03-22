package com.zj.utils;

import com.sun.istack.internal.Nullable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname Mp3DownLoadUtil
 * @Description
 * @Date 2022/3/18 14:24
 * @Created by lxf
 */
public class Mp3DownLoadUtil {


        //默认文件路径
        public final static String DEFAULT_PATH="data/";


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

        public static ArrayList<String> SearchFile(String root,ArrayList<String> list){
            File file = new File(root);
            //获取全部File
            //返回目录名加文件名
            //添加过滤器
            String[] strings = file.list();

            //这些路径名表示此抽象路径名所表示目录中的文件。
            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            });
            for (int i = 0; i < files.length; i++) {
                //判断是否是目录，是的话继续递归
                if (files[i].isDirectory()) {
                    SearchFile(files[i].getAbsolutePath(), list);
                } else {
                    //否则添加到list
                    //获取全部文件名
                    //list.add(files[i].getName());
                    //获取全部包+文件名
                    String absolutePath = files[i].getAbsolutePath();
                    String[] split = absolutePath.split("filter");
                    String o =split[1];
                    String replace = o.replace("\\", "/");
                    list.add(replace);
                }
            }
            return list;
        }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list= SearchFile(DEFAULT_PATH,list);
       list.forEach(System.out::println);
        //System.out.println(list.get(0));
    }

}
