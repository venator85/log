package eu.alessiobianchi.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Log {
	private static final int MAXIMUM_LINE_LENGTH = 4000;
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
	private static final String MSG_FORMAT = "%s [%s][%s]:   %s\n";
	private static final Object lock = new Object();

	private static File logFile;
	private static BufferedWriter writer;
	private static boolean enabled = true;

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean enabled) {
		Log.enabled = enabled;
	}

	public static void init(boolean enableLogcat, File logFile) {
		enabled = enableLogcat;

		Log.logFile = logFile;
		if (logFile != null) {
			synchronized (lock) {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException ignore) {
					}
				}
				try {
					writer = new BufferedWriter(new FileWriter(logFile, false));
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}

	public static File getLogFile() {
		synchronized (lock) {
			if (writer != null) {
				try {
					writer.flush();
				} catch (IOException e) {
					logcat(android.util.Log.ERROR, "Log", "Error flushing log file");
					logcat(android.util.Log.ERROR, "Log", getStackTraceString(e));
				}
			}
			return logFile;
		}
	}

	public static void v(String msg, Throwable t, Object tag) {
		log(android.util.Log.VERBOSE, getTag(tag), msg, t);
	}

	public static void d(String msg, Throwable t, Object tag) {
		log(android.util.Log.DEBUG, getTag(tag), msg, t);
	}

	public static void i(String msg, Throwable t, Object tag) {
		log(android.util.Log.INFO, getTag(tag), msg, t);
	}

	public static void w(String msg, Throwable t, Object tag) {
		log(android.util.Log.WARN, getTag(tag), msg, t);
	}

	public static void e(String msg, Throwable t, Object tag) {
		log(android.util.Log.ERROR, getTag(tag), msg, t);
	}

	public static void v(String msg, Object tag) {
		v(msg, null, tag);
	}

	public static void d(String msg, Object tag) {
		d(msg, null, tag);
	}

	public static void i(String msg, Object tag) {
		i(msg, null, tag);
	}

	public static void w(String msg, Object tag) {
		w(msg, null, tag);
	}

	public static void e(String msg, Object tag) {
		e(msg, null, tag);
	}

	private static void log(int level, String tag, String msg, Throwable t) {
		final String stacktrace = t != null ? getStackTraceString(t) : null;

		logcat(level, tag, msg);
		if (stacktrace != null) {
			logcat(level, tag, stacktrace);
		}

		if (writer != null) {
			synchronized (lock) {
				logToFile(level, tag, msg);
				if (stacktrace != null) {
					logToFile(level, tag, stacktrace);
				}
			}
		}
	}

	private static void logcat(int level, String tag, String msg) {
		final int msgLen = msg.length();
		if (msgLen > MAXIMUM_LINE_LENGTH) {
			for (int i = 0; i < msgLen; i++) {
				int newline = msg.indexOf('\n', i);
				newline = newline != -1 ? newline : msgLen;
				do {
					int end = Math.min(newline, i + MAXIMUM_LINE_LENGTH);
					String part = msg.substring(i, end);
					android.util.Log.println(level, tag, part);
					i = end;
				} while (i < newline);
			}
		} else {
			android.util.Log.println(level, tag, msg);
		}
	}

	private static void logToFile(int level, String tag, String msg) {
		try {
			String sLevel;
			if (level == android.util.Log.VERBOSE) {
				sLevel = "V";
			} else if (level == android.util.Log.DEBUG) {
				sLevel = "D";
			} else if (level == android.util.Log.INFO) {
				sLevel = "I";
			} else if (level == android.util.Log.WARN) {
				sLevel = "W";
			} else if (level == android.util.Log.ERROR) {
				sLevel = "E";
			} else {
				sLevel = String.valueOf(level);
			}
			writer.write(String.format(Locale.US, MSG_FORMAT, TIMESTAMP_FORMAT.format(new Date()), sLevel, tag, msg));
		} catch (IOException e) {
			logcat(android.util.Log.ERROR, "Log", "Error writing log to file");
			logcat(android.util.Log.ERROR, "Log", getStackTraceString(e));
		}
	}

	private static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter(256);
		PrintWriter pw = new PrintWriter(sw, false);
		tr.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	private static String getTag(Object obj) {
		if (obj != null) {
			String tag;
			if (obj instanceof String) {
				tag = (String) obj;
				if (tag.isEmpty()) {
					tag = "(no-tag)";
				}
			} else {
				Class<?> c;
				if (obj instanceof Class) {
					c = (Class<?>) obj;
				} else {
					c = obj.getClass();
				}
				if (c.isAnonymousClass()) {
					String name = c.getName();
					final int pos = name.lastIndexOf('.');
					if (pos == -1 || pos == name.length() - 1) {
						tag = name;
					} else {
						tag = name.substring(pos + 1);
					}
				} else {
					tag = c.getSimpleName();
				}
				if (tag.isEmpty()) {
					tag = c.getName();
				}
			}
			return tag;
		} else {
			return "(null)";
		}
	}
}
