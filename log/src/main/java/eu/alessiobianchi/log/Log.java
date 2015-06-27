package eu.alessiobianchi.log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {

	private static final int MAXIMUM_LINE_LENGTH = 4000;

	private static boolean enabled;

	public static void setEnabled(boolean enabled) {
		Log.enabled = enabled;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void v(String msg, Throwable t, Object obj) {
		log(android.util.Log.VERBOSE, getTag(obj), msg, t);
	}

	public static void d(String msg, Throwable t, Object obj) {
		log(android.util.Log.DEBUG, getTag(obj), msg, t);
	}

	public static void i(String msg, Throwable t, Object obj) {
		log(android.util.Log.INFO, getTag(obj), msg, t);
	}

	public static void w(String msg, Throwable t, Object obj) {
		log(android.util.Log.WARN, getTag(obj), msg, t);
	}

	public static void e(String msg, Throwable t, Object obj) {
		log(android.util.Log.ERROR, getTag(obj), msg, t);
	}

	public static void v(String msg, Object obj) {
		v(msg, null, obj);
	}

	public static void d(String msg, Object obj) {
		d(msg, null, obj);
	}

	public static void i(String msg, Object obj) {
		i(msg, null, obj);
	}

	public static void w(String msg, Object obj) {
		w(msg, null, obj);
	}

	public static void e(String msg, Object obj) {
		e(msg, null, obj);
	}

	private static void log(int level, String tag, String msg, Throwable t) {
		if (!enabled) {
			return;
		}
		final int msgLen = msg.length();
		if (msgLen > MAXIMUM_LINE_LENGTH) {
			for (int i = 0; i < msgLen; i++) {
				int newline = msg.indexOf('\n', i);
				newline = newline != -1 ? newline : msgLen;
				do {
					int end = Math.min(newline, i + MAXIMUM_LINE_LENGTH);
					String part = msg.substring(i, end);
					invokeLogger(level, tag, part);
					i = end;
				} while (i < newline);
			}
		} else {
			invokeLogger(level, tag, msg);
		}
		if (t != null) {
			log(level, tag, getStackTraceString(t), null);
		}
	}

	private static void invokeLogger(int level, String tag, String msg) {
		android.util.Log.println(level, tag, msg);
	}

	private static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.append('\n');
		tr.printStackTrace(pw);
		return sw.toString();
	}

	private static String getTag(Object obj) {
		if (obj != null) {
			String tag;
			if (obj instanceof String) {
				tag = (String) obj;
			} else if (obj instanceof Class) {
				Class<?> c = (Class<?>) obj;
				tag = c.getSimpleName();
			} else {
				Class<?> c = obj.getClass();
				tag = c.getSimpleName();
			}
			return tag;
		} else {
			return "(null)";
		}
	}
}