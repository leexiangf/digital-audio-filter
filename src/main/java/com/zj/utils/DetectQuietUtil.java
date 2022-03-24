package com.zj.utils;

import com.zj.filter.DetectSoundFilter;
import com.zj.mapper.TalkInfoMapper;
import com.zj.po.ResultDBDTO;
import com.zj.po.TalkInfo;
import org.apache.tomcat.util.file.Matcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Pattern;

/**
 * 识别音频中是否有声音
 * @author lxf
 *
 */
public class DetectQuietUtil {

	private static Logger logger = Logger.getLogger("DetectQuiet");


	//整段声音的阈值
	private final static double threshold = 0.33;

	//private static  String mp3Path = "http://music.163.com/song/media/outer/url?id=317151.mp3";

	 private final  static String reg = ".+(.mp3|.wav|.MP3|.MPEG|.m4a|.aac)$";

	 public final  static  String  PARENT_FILE_PATH="./data/";

	 public final  static  String  PARENT_URL_PATH="https://call-file-online.oss-cn-hangzhou.aliyuncs.com/";

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		//loadFile("./data/有声.wav");
		//loadFile("./data/安静.wav");
		//loadFile("data/有声音.mp3");
		//loadFile("data/2022/202202/20220224/lp20220224151622216.mp3");
		//File f = new File("data/2022/202202/20220224/lp20220224151622216.mp3");
		//loadFile("./data/有声音.mp3");
		//loadFile(".\\data\\有声音.mp3");
		//System.out.println(resultDBDTO);
		//loadFile("./data/0011234.mp3");
		//loadFile("data/无声音.mp3");
	    //loadUrl(mp3Path);

		System.out.println(LocalDateTime.now());
	}

	/**
	 * @Retrun  0：毫无声音 1：疑似有声音（有声音的帧数过少） 2：有声音（有声音的帧数多）
	 * 读取文件
	 */
	public static ResultDBDTO loadFile(String path)  {
		File f = new File(path);

		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(f);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		AudioFormat sourceFormat = audioStream.getFormat();
		/*
		 *	因为AudioInputStream不支持mp3格式，需要转换一下format配置
		 *  --> AudioFormat.Encoding.PCM_SIGNED // PCM_UNSIGNED  //  PCM_FLOAT
		 */
		AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(),
				sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);

		AudioInputStream mp3AudioInputStream = AudioSystem.getAudioInputStream(mp3tFormat, audioStream);

		return readData(mp3AudioInputStream);
	}

	
	/**
	 * 读取数据
	 */
	public static ResultDBDTO readData(AudioInputStream audioStream)  {

		/**
		 * PCM声音是重采样为无符号16bit的深度的，然后我们需要得到某一时间（一般是零点几毫秒）PCM所在内存的地址和PCM声音的大小，
		 * 而16bit也就是16bit/8bit=2byte，因此我们可以从PCM所在地址里面按顺序取出2个byte的数据然后转化成的值就可以拿到当前采样点的振幅了
		 */
		byte[] frame = new byte[1024];

		DetectSoundFilter soundFilter = new DetectSoundFilter();
		//总帧数
		int count =0;
		//有声音的帧数
		int isSliLence=0;

		//ArrayList<Double> list = new ArrayList(frame.length);
		ResultDBDTO resultDBDTO = new ResultDBDTO();
		while (true) {
			int read = 0;
			try {
				read = audioStream.read(frame);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (read < 0) {
				break;
			}
			count++;
			//int volume = soundFilter.calculateVolume(frame, 16);
			double[] doubleFrame = byteToDouble(frame);
			boolean silence = soundFilter.isSilence(doubleFrame);
			//double v = soundFilter.currentSPL(doubleFrame);
			//list.add(v);
			if(!silence){
				isSliLence++;
			}
		}
		resultDBDTO.setDbCount(count);
		resultDBDTO.setDbZero(count-isSliLence);
		resultDBDTO.setProportion(new BigDecimal((float)isSliLence/count)
				.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
		logger.info("声音占有比重："+resultDBDTO.getProportion());
		return resultDBDTO;
	}

	/**
	 * pcm转成浮点
	 * @param data
	 * @return
	 */
	public static double[] byteToDouble(byte[] data) {
		ByteBuffer buf = ByteBuffer.wrap(data);

		buf.order(ByteOrder.BIG_ENDIAN);
		int i = 0;
		//double[] dData = new double[data.length / 2];
		double[] dData = new double[data.length];
		while (buf.remaining() > 1) {
			short s = buf.getShort();
			dData[i] = (double) s / 32767.0; // real
			++i;
		}
		return dData;
	}


	/**
	 * 通过url地址查找到当前classpath下对应的文件
	 * @param srcUrl
	 * @return
	 */
	public static String urlToFile(String srcUrl){
		String[] split = srcUrl.split(".com/");
		String newFile =PARENT_FILE_PATH+split[1];
		return newFile;
	}


	/**
	 * 通过本地文件名查找对对应url
	 * @param fileName
	 * @return
	 */
	public static String fileToUrl(String fileName){
		 String[] split = fileName.split("data/");
		return  fileName=PARENT_URL_PATH+split[1];
	}


	/**
	 *  计算出list集合里面的数据的数量
	 * @param list
	 * @param resultDBDTO
	 * @return
	 */
	public static ResultDBDTO calculateResult(List<Double> list,ResultDBDTO resultDBDTO){
		if(list.size()==0) return resultDBDTO;
		resultDBDTO.setDbZero(list.stream().filter(s -> s==0).count());
		list.stream().filter(s -> s==0).count();
		resultDBDTO.setDbHigh(list.stream().filter(s -> s>-30).count());
		resultDBDTO.setDbLow(list.stream().filter(s -> s<-40).count());
		resultDBDTO.setDbMid(list.stream().filter(s -> s>=-40 && s<=-30).count());
		resultDBDTO.setProportion((float)((list.size()-resultDBDTO.getDbZero())/list.size()));
		return resultDBDTO;
	}

}
