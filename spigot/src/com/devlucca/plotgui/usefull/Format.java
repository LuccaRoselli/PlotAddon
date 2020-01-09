package com.devlucca.plotgui.usefull;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.devlucca.plotgui.Main;

public class Format {

	private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
	
	static {
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "B");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "Q");
		suffixes.put(1_000_000_000_000_000_000L, "QQ");
	}

	public static String format(long value) {
		if (Main.get().getConfig().getInt("Formatacao") == 1) {
			if (value == Long.MIN_VALUE)
				return format(Long.MIN_VALUE + 1);
			if (value < 0)
				return "-" + format(-value);
			if (value < 1000)
				return Long.toString(value);
			Entry<Long, String> e = suffixes.floorEntry(value);
			Long divideBy = e.getKey();
			String suffix = e.getValue();
			long truncated = value / (divideBy / 10);
			boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
			return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
		} else if (Main.get().getConfig().getInt("Formatacao") == 2){
	        final String formatado = NumberFormat.getNumberInstance(Locale.GERMANY).format(value);
	        return formatado;
		} else {
			return String.valueOf(value);
		}
	}

}