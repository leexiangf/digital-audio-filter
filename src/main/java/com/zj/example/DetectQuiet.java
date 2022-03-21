package com.zj.example;

import com.zj.filter.DetectSoundFilter;
import com.zj.po.TalkInfo;
import com.zj.result.ResultDB;
import lombok.val;
import org.apache.tomcat.util.file.Matcher;

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
public class DetectQuiet {

	//整段声音的阈值
	private final static double threshold = 0.33;

	/**
	 * 声音响度阈值
	 */
	public static double soundThreshold = -60; // db

	public static double soundLow = -60; // db
	public static double soundHigh = -30; // db

	private static  String mp3Path = "http://music.163.com/song/media/outer/url?id=317151.mp3";
	private static  String mp3Path1 = "http://music.163.com/song/media/outer/url?id=317151.mp3";

	 private final  static String reg = ".+(.mp3|.wav|.MP3|.MPEG)$";

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {

		//loadFile("./data/有声.wav");
		//loadFile("./data/安静.wav");
		//loadFile("./data/有声音.mp3");
		//loadFile("./data/0011234.mp3");
		//loadFile("./data/16k-001.mp3");
		//loadFile("./data/无声音.mp3");
	    //loadUrl(s);
	}

	/**
	 * @Retrun  0：毫无声音 1：疑似有声音（有声音的帧数过少） 2：有声音（有声音的帧数多）
	 * 读取文件
	 */
	public static int loadFile(String path) throws UnsupportedAudioFileException, IOException {
		File f = new File(path);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(f);
		AudioFormat sourceFormat = audioStream.getFormat();
		/*
		 *	因为AudioInputStream不支持mp3格式，需要转换一下format配置
		 *  --> AudioFormat.Encoding.PCM_SIGNED // PCM_UNSIGNED  //  PCM_FLOAT
		 */
		AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(),
				sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);

		AudioInputStream mp3AudioInputStream = AudioSystem.getAudioInputStream(mp3tFormat, audioStream);

		//double code = readData(mp3AudioInputStream);
		ResultDB resultDB = readData01(mp3AudioInputStream);
		System.out.println(resultDB);
		//return code==0 ? 0 : code < threshold ? 1: 2;
		return 0;
	}


	/**
	 * 通过url地址来
	 * @param path  url地址i
	 * @return
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public static int loadUrl(String path) throws IOException, UnsupportedAudioFileException {
		URL url = new URL(path);
		//判断是否是mp3格式的文件
		if(!Pattern.compile(reg).matcher(url.getFile()).find()){
			throw new UnsupportedAudioFileException("this type is unsupport!");
		}
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
		AudioFormat sourceFormat = audioStream.getFormat();
		/*
		 *	因为AudioInputStream不支持mp3格式，需要转换一下format配置
		 *  --> AudioFormat.Encoding.PCM_SIGNED // PCM_UNSIGNED  //  PCM_FLOAT
		 */
		AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				//Channels : 1 是单声道 2 是双声道
				sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(),
				sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(mp3tFormat, audioStream);

		double code = readData(audioInputStream);

		return code==0 ? 0 : code < threshold ? 1: 2;
	}
	
	/**
	 * 读取数据
	 */
	public static double readData(AudioInputStream audioStream) throws IOException {

		/**
		 * PCM声音是重采样为无符号16bit的深度的，然后我们需要得到某一时间（一般是零点几毫秒）PCM所在内存的地址和PCM声音的大小，
		 * 而16bit也就是16bit/8bit=2byte，因此我们可以从PCM所在地址里面按顺序取出2个byte的数据然后转化成的值就可以拿到当前采样点的振幅了
		 *
		 */
		byte[] frame = new byte[1024];

		DetectSoundFilter soundFilter = new DetectSoundFilter();
		//总帧数
		int count =0;
		//有声音的帧数
		int isSliLence=0;
		ArrayList<Double> list = new ArrayList();
		while (true) {
			int read = audioStream.read(frame);
			if (read < 0) {
				break;
			}
			count++;
/*			int volume = soundFilter.calculateVolume(frame, 16);
			System.out.println(volume);*/
			double[] byteToDouble = byteToDouble(frame);
			boolean silence = soundFilter.isSilence(byteToDouble);
			list.add(soundFilter.currentSPL(byteToDouble));
			if(silence) {

			} else {
				isSliLence++;
			}
		}
		System.out.println("总帧数："+count);
		System.out.println("有声音的帧数："+isSliLence);
		System.out.println("声音占有比重："+new BigDecimal((float)isSliLence/count)
				.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
		return isSliLence==0 ? 0 :
				new BigDecimal((float)isSliLence/count)
						.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 读取数据
	 */
	public static ResultDB readData01(AudioInputStream audioStream) throws IOException {

		/**
		 * PCM声音是重采样为无符号16bit的深度的，然后我们需要得到某一时间（一般是零点几毫秒）PCM所在内存的地址和PCM声音的大小，
		 * 而16bit也就是16bit/8bit=2byte，因此我们可以从PCM所在地址里面按顺序取出2个byte的数据然后转化成的值就可以拿到当前采样点的振幅了
		 *
		 */
		byte[] frame = new byte[1024];

		DetectSoundFilter soundFilter = new DetectSoundFilter();
		//总帧数
		int count =0;
		//有声音的帧数
		int isSliLence=0;
		ArrayList<Double> list = new ArrayList();
		while (true) {
			int read = audioStream.read(frame);
			if (read < 0) {
				break;
			}
			count++;
/*			int volume = soundFilter.calculateVolume(frame, 16);
			System.out.println(volume);*/
			double[] byteToDouble = byteToDouble(frame);
			boolean silence = soundFilter.isSilence(byteToDouble);
			list.add(soundFilter.currentSPL(byteToDouble));
			if(silence) {

			} else {
				isSliLence++;
			}
		}
		System.out.println("总帧数："+count);
		System.out.println("有声音的帧数："+isSliLence);
		System.out.println("集合长度："+list.size());
		//System.out.println("声音占有比重："+new BigDecimal((float)isSliLence/count).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
		return resultDB(list);
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
	 * 计算集合存储信息的数量
	 * @param list
	 * @return
	 */
	public static ResultDB resultDB(List<Double> list){

		ResultDB resultDB = new ResultDB();
		if(list.isEmpty()){
			return resultDB;
		}
		resultDB.setDbCount(list.size());

		long low = list.stream().filter(s -> s < soundLow).count();
		long zero = list.stream().filter(s -> s == 0 ).count();
		long high = list.stream().filter(s -> s > soundHigh && s<0).count();
		long mid = list.stream().filter(s -> s >= soundLow && s <= soundHigh).count();
		resultDB.setDbHigh(high);
		resultDB.setDbLow(low);
		resultDB.setDbMid(mid);
		resultDB.setDbZero(zero);
		resultDB.setProportion(new BigDecimal((float)(list.size()-zero-low)/list.size()).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
		return resultDB;
	}


	public void sp(){
		String s = "https://call-file-online.oss-cn-hangzhou.aliyuncs.com/2022/202203/20220317/D16757260322_1647512966019_220317182940880.m4a";
		String ss = "https://call-file-online.oss-cn-hangzhou.aliyuncs.com/2022/202203/20220317/D16757260322_1647513062977_220317183126642.m4a";
		String s1;
		String 	s2;
		String[] split ;
		List<String> list =new ArrayList();
		list.add(s);
		list.add(ss);
		for (int i = 0; i <list.size() ; i++) {
			s1 = list.get(i);
			split = s1.split(".com");
			s2=	split[1];
			list.set(i, s2);
		}
		System.out.println(list);
	}
}
