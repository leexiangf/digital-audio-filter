package com.zj.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import com.zj.filter.AudioFilter;

/**
 * java视频转码之音频滤波实现
 * 
 * @author ZJ
 *
 */
public class JavacvStreamAudioFilter {

	static int sampleFormat;
	static int sampleRate;
	static int audioChannels;

	public static void start() throws IOException, ExecutionException, LineUnavailableException {
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("d:/flv/file.mp4");
		grabber.start();

		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("d:/flv/fileaudio.flv", 
				grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());

		// 设置flv格式
		recorder.setFormat("flv");
		recorder.setFrameRate(25);// 设置帧率
		recorder.setGopSize(25);// 设置gop
		recorder.setVideoQuality(1);
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// 这种方式也可以
		recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);// 设置音频编码

		recorder.start();

		CanvasFrame canvas = new CanvasFrame("视频预览");// 新建一个窗口
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame frame = null;

		sampleFormat = grabber.getSampleFormat();
		sampleRate = grabber.getSampleRate();
		audioChannels = grabber.getAudioChannels();

		// 初始化扬声器
		final AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true,
				true);
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		final SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
		soundLine.open(audioFormat);
		soundLine.start();

		//线程池
		ExecutorService executor = Executors.newSingleThreadExecutor();

		// 低通滤波
		AudioFilter lowpass = new AudioFilter(sampleRate, 1000);
		lowpass.initLowpass();
		// 高通
		AudioFilter highpass = new AudioFilter(sampleRate, 800);
		highpass.initHighpass();

		// 只抓取图像画面
		for (; (frame = grabber.grab()) != null;) {

			// 判断是否是音频帧
			if (frame.samples != null) {
				// 一般音频都是16位非平面型左右声道在一个buffer中，如果不是得自己解析
				ShortBuffer channelSamples = (ShortBuffer) frame.samples[0];

				// 扬声器采样数据
				channelSamples.rewind();
				ByteBuffer bufferToSourceDataLine = ByteBuffer.allocate(channelSamples.capacity() * 2);

				// recorder采样数据
				channelSamples.rewind();
				ShortBuffer bufferToRecorder = ShortBuffer.allocate(channelSamples.capacity());

				for (int i = 0; i < channelSamples.capacity(); i++) {
					short val = channelSamples.get(i);

					/**
					 * 滤波计算 可能是javacv的缘故，重新编码声音貌似有bug，偶尔会有噪音，我直接用java自己解析就不会，算法是没有问题的
					 */
					val = (short) lowpass.filter(val);
					val = (short) highpass.filter(val);

					bufferToSourceDataLine.putShort(val);
					bufferToRecorder.put(val);
				}

				/**
				 * 写入到扬声器 并准备下一次读取
				 */
				try {
					executor.submit(new Runnable() {
						public void run() {
							soundLine.write(bufferToSourceDataLine.array(), 0, bufferToSourceDataLine.capacity());
							bufferToSourceDataLine.clear();
						}
					}).get();
				} catch (InterruptedException interruptedException) {
					Thread.currentThread().interrupt();
				}

				bufferToRecorder.rewind();
				recorder.recordSamples(sampleRate, audioChannels, bufferToRecorder);
				bufferToRecorder.clear();
			} else {
				// 画面帧直接录制/推流
				recorder.record(frame);
				// 显示画面
				canvas.showImage(frame);
			}
		}

		recorder.close();// close包含stop和release方法。录制文件必须保证最后执行stop()方法，才能保证文件头写入完整，否则文件损坏。
		grabber.close();// close包含stop和release方法
		canvas.dispose();
		soundLine.close();
		System.exit(0);
	}

	public static void main(String[] args) throws IOException, ExecutionException, LineUnavailableException {
		start();
	}

}
