package dev.mirror.library.utils;

import java.text.DecimalFormat;

public class PriceUtil {

	/**
	 * 两位小数点类型toString
	 * @param
	 * @return
	 */
	public static String floatToString(int price){
		float i = (float)price/100;

//		DecimalFormat   fnum  =   new  DecimalFormat("##0.00");
		DecimalFormat   fnum  =   new  DecimalFormat("##0.0");
		return fnum.format(i);
	}
}
