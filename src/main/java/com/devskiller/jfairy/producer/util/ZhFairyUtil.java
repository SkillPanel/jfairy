package com.devskiller.jfairy.producer.util;

import com.devskiller.jfairy.producer.BaseProducer;

/**
 * com.devskiller.jfairy.producer.util.ZhFairyUtil
 *
 * @author lhfcws
 * @since 2017/3/10
 */
public final class ZhFairyUtil {

	private ZhFairyUtil() {}

	/**
	 * Codes of China provinces
	 */
	public static final String[] PROV_LIST = {
		"11",    // Beijing
		"12",    // Tianjin
		"13",    // Hebei
		"14",    // Shanxi
		"15",    // Neimenggu
		"21",    // Liaoning
		"22",    // Jilin
		"23",    // Heilongjiang
		"31",    // Shanghai
		"32",    // Jiangsu
		"33",    // Zhejiang
		"34",    // Anhui
		"35",    // Fujian
		"36",    // Jiangxi
		"41",    // Henan
		"42",    // Hubei
		"43",    // Hunan
		"44",    // Guangdong
		"45",    // Guangxi
		"46",    // Hainan
		"50",    // Chongqing
		"51",    // Sichuan
		"52",    // Guizhou
		"53",    // Yunnan
		"54",    // Xizang
		"61",    // Shaanxi
		"62",    // Gansu
		"63",    // Qinghai
		"64",    // Ningxia
		"65",    // Xinjiang
		"71",    // Taiwan
		"81",    // Hong Kong
		"82",    // Macau
	};

	/**
	 * Max code of city
	 */
	public static final int CITY_MAX = 30;
	/**
	 * Max code of city district
	 */
	public static final int DISTRICT_MAX = 12;

	/**
	 * Get random number from 1 to max in 0 leading string format.
	 * @param baseProducer BaseProducer
	 * @param max         upper bound of number
	 * @param paddingSize padding size
	 * @return A number smaller than #max with #paddingSize digits in string format
	 */
	public static String getRandomNumStr(BaseProducer baseProducer, int max, int paddingSize) {
		int rndNum = baseProducer.randomBetween(1, max);
		StringBuilder numStr = new StringBuilder("" + rndNum);
		while (numStr.length() < paddingSize) {
			numStr.insert(0, "0");
		}
		return numStr.toString();
	}
}
