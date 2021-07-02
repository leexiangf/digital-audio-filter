package com.zj;

/**
 * 双二次滤波器之低通、高通
 * 
 * @author ZJ
 *
 */
public class AudioFilter {

	double b0, b1, b2, a0, a1, a2;
	double x1, x2, y, y1, y2;

	/**
	 * 采样率
	 */
	private float sample_rate = 44100f;
	/**
	 * 中心频率
	 */
	private double center_freq = 0;
	/**
	 * Q值
	 */
	private double Q = 1;

	public AudioFilter(float sample_rate, double center_freq) {
		super();
		this.sample_rate = sample_rate;
		this.center_freq = center_freq;
	}

	public AudioFilter(float sample_rate, double center_freq, double q) {
		super();
		this.sample_rate = sample_rate;
		this.center_freq = center_freq;
		Q = q;
	}

	/**
	 * 低通滤波
	 */
	public void initLowpass() {
		Q = (Q == 0) ? 1e-9 : Q;

		double ov = 2 * Math.PI * center_freq / sample_rate;
		double sn = Math.sin(ov);
		double cs = Math.cos(ov);
		double alpha = sn / (2 * Q);

		b0 = (1 - cs) / 2;
		b1 = 1 - cs;
		b2 = (1 - cs) / 2;
		a0 = 1 + alpha;
		a1 = -2 * cs;
		a2 = 1 - alpha;

		gcompute();
	}

	/**
	 * 高通滤波
	 */
	public void initHighpass() {
		Q = (Q == 0) ? 1e-9 : Q;

		double ov = 2 * Math.PI * center_freq / sample_rate;
		double sn = Math.sin(ov);
		double cs = Math.cos(ov);
		double alpha = sn / (2 * Q);

		b0 = (1 + cs) / 2;
		b1 = -(1 + cs);
		b2 = (1 + cs) / 2;
		a0 = 1 + alpha;
		a1 = -2 * cs;
		a2 = 1 - alpha;

		gcompute();
	}

	/**
	 * 计算
	 */
	private void gcompute() {
		// a0归一化为1
		b0 /= a0;
		b1 /= a0;
		b2 /= a0;
		a1 /= a0;
		a2 /= a0;
	}

	/**
	 * 过滤
	 * 
	 * @param x
	 * @return
	 */
	public double filter(double x) {
		y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
		x2 = x1;
		x1 = x;
		y2 = y1;
		y1 = y;
		return y;
	}

}
