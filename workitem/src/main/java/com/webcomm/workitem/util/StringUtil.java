package com.webcomm.workitem.util;

public class StringUtil {

	public static void tryAppend(StringBuilder sb, String... appendStrs) {
		if (appendStrs != null)
			for (String appendStr : appendStrs)
				sb.append(appendStr == null ? "" : appendStr);
	}
}
