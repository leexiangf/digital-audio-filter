package com.zj.common;

/**
 * 归一化处理 归一化的缩放是“拍扁”统一到区间（仅由极值决定），而标准化的缩放是更加“弹性”和“动态”的，和整体样本的分布有很大的关系
 * 归一化：缩放仅仅跟最大、最小值的差别有关
 * 标准化：缩放和每个点都有关系，通过方差（variance）体现出来。与归一化对比，标准化中所有数据点都有贡献（通过均值和标准差造成影响）
 * 归一化：输出范围在0-1之间
 * 标准化：输出范围是负无穷到正无穷
 * 
 * 如果对输出结果范围有要求，用归一化 如果数据较为稳定，不存在极端的最大最小值，用归一化
 * 如果数据存在异常值和较多噪音，用标准化，可以间接通过中心化避免异常值和极端值的影响
 * 
 * @author ZJ
 *
 */
public class Normal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * min-max归一化
	 * 也称为离差标准化，是对原始数据的线性变换，使结果值映射到[0 – 1]之间。转换函数如下：
	 * x∗=(x−min)/(max−min)
	 */
	public void normalization(double[] sample) {
		
	}

	/**
	 * Z-score标准化
	 * 这种方法给予原始数据的均值（mean）和标准差（standard deviation）进行数据的标准化。
	 * 经过处理的数据符合标准正态分布，即均值为0，标准差为1，转化函数为： x∗=(x−μ)/σ 其中 μ为所有样本数据的均值，σ为所有样本数据的标准差。
	 */
	public void standardization() {

	}

	/**
	 * 计算标准差σ=sqrt(s^2)
	 * 
	 * @param x
	 * @return
	 */
	public static double StandardDiviation(double[] x) {
		int m = x.length;
		double sum = 0;
		for (int i = 0; i < m; i++) {// 求和
			sum += x[i];
		}
		double dAve = sum / m;// 求平均值
		double dVar = 0;
		for (int i = 0; i < m; i++) {// 求方差
			dVar += (x[i] - dAve) * (x[i] - dAve);
		}
		// reture Math.sqrt(dVar/(m-1));
		return Math.sqrt(dVar / m);
	}
}
