package com.zj.filter;

/**
 * 识别声音过滤器
 * 用于判断有没有声音，甚至可以判断声音的响度
 * 
 * @author ZJ
 *
 */
public class DetectSoundFilter {
	
	/**
	 * 声音响度阈值
	 */
	public double soundThreshold = -60; // db
	
	/**
	 * 是否安静
	 * @param buffer
	 * @param
	 * @return
	 */
	public boolean isSilence(final double[] buffer) {
		double currentSPL = soundPressureLevel(buffer);
		System.out.println("声压："+currentSPL);
		return currentSPL < soundThreshold;
	}

	public double currentSPL(final double[] buffer) {
		double currentSPL = soundPressureLevel(buffer);
		//System.out.println("声压："+(int)currentSPL);
		return currentSPL ;
	}

	/**
	 * 声压计算  (0.5次方取原数)取绝对值求和的平均数
	 * Math.pow（a,b) 计算a的b次方
	 *  value = 0.017489073377694987
	 * @param buffer
	 * @return
	 */
	private double soundPressureLevel(final double[] buffer) {
		double value = Math.pow(localEnergy(buffer), 0.5);
		//2个字节表示一个振幅，所以振幅个数为：size/2个
		//value = value / buffer.length;
		value = value / (buffer.length/2);
		return linearToDecibel(value);
	}

	/**
	 * 线性转换为dB值
	 * Math.log10(value)：
	 *  		如果参数是NaN或小于零，那么结果是NaN. //异常
	 *   		如果参数是正无穷大，那么结果为正无穷大.
	 * 			如果参数是正零或负零，那么结果是负无穷大.
	 * 			如果参数是等于10N整数n，那么结果是n
	 * @param value
	 * @return
	 */
	private double linearToDecibel(final double value) {
		return value==0? 0: 20.0 * Math.log10(value);
		//return  20.0 * Math.log10(value);
	}

	/**
	 * 音频帧去负数（求和，2次方去负数）
	 * @param buffer
	 * @return
	 */
	private double localEnergy(final double[] buffer) {
		double power = 0.0D;
		for (double element : buffer) {
			power += element * element;
		}
		return power;
	}


	/**
	 * 计算输入数据段的db值，按公式应该为20*Math.log10(当前振幅值/最大振幅值)；
	 * 位深为16bit，则代表两个字节表示一个音量采集单位；
	 * 此处用平方和平均值进行计算；
	 *
	 * @param data 输入pcm音频数据
	 * @param bit  位深，8或16
	 * @return 当前分贝值
	 */
	public int calculateVolume(byte[] data, int bit) {
		int[] newBuffer = null;
		int len = data.length;
		int index;

		//排列
		if (bit == 8) {
			newBuffer = new int[len];
			for (index = 0; index < len; ++index) {
				newBuffer[index] = data[index];
			}
		} else if (bit == 16) {
			newBuffer = new int[len / 2];
			for (index = 0; index < len / 2; ++index) {
				byte byteH= data[index * 2];
				byte byteL = data[index * 2 + 1];
				newBuffer[index] = bytesToShort(byteH, byteL);
			}
		}
		//平方和求平均值
		if (newBuffer != null && newBuffer.length != 0) {
			float avg = 0.0F;
			for (int i = 0; i < newBuffer.length; ++i) {
				avg += (float) (newBuffer[i] * newBuffer[i]);
			}
			avg /= (float) newBuffer.length;
			int db = (int) (10.0D * Math.log10(avg + 1));
			return db;
		} else {
			return 0;
		}
	}

	private static short bytesToShort(byte byteH, byte byteL) {
		short temp = 0;
		temp += (byteL & 0xFF);
		temp += (byteH & 0xFF) << 8;
		return temp;
	}

}
