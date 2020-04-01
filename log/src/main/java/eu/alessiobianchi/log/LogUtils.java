package eu.alessiobianchi.log;

class LogUtils {
	static String getTag(Object obj) {
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
			return tag != null ? tag : "(null)";
		} else {
			return "(null)";
		}
	}
}
