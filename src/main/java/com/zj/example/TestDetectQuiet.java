package com.zj.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.zj.filter.DetectSoundFilter;

/**
 * 识别音频中是否有声音
 * @author ZJ
 *
 */
public class TestDetectQuiet {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		
		//loadFile("./data/安静.wav");
		loadFile("./data/有声.wav");
		//loadFile("./data/无声音.mp3");
	}
	
	/**
	 * 读取文件
	 */
	public static void loadFile(String path) throws UnsupportedAudioFileException, IOException {
		File f = new File(path);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(f);
		
		readData(audioStream);
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
		/*if(!Pattern.compile(reg).matcher(url.getFile()).find()){
			throw new UnsupportedAudioFileException("this type is unsupport!");
		}*/
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

		//double code = readData(audioInputStream);

		//return code==0 ? 0 : code < threshold ? 1: 2;
		return 0;
	}
	
	/**
	 * 读取数据
	 * @throws IOException 
	 */
	public static void readData(AudioInputStream audioStream) throws IOException {
		byte[] frame = new byte[2048];
		
		DetectSoundFilter soundFilter = new DetectSoundFilter();
		
		while (true) {
			int read = audioStream.read(frame);
			if (read < 0) {
				break;
			}
			
			double[] byteToDouble = byteToDouble(frame);
			boolean silence = soundFilter.isSilence(byteToDouble);
			if(silence) {
				System.out.println("这是安静的");
			} else {
				System.out.println("这是有声音的");
			}
		}
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
		double[] dData = new double[data.length / 2];

		while (buf.remaining() > 1) {
			short s = buf.getShort();

			dData[i] = (double) s / 32767.0; // real
			++i;
		}
		return dData;
	}
	
}
