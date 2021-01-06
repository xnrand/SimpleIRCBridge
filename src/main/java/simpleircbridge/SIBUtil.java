package simpleircbridge;

public class SIBUtil {
	private static final String UNICODE_ZWNJ = "\u200c";

	private SIBUtil() {
		// utility class
	}

	/** joins a number of Strings together with a given delimiter */
	public static String join(String delim, String... strings) {
		if (strings.length == 0)
			return "";
		if (strings.length == 1)
			return strings[0];
		else {
			StringBuilder sb = new StringBuilder(strings[0]);
			for (int i = 1; i < strings.length; i++) {
				sb.append(delim);
				sb.append(strings[i]);
			}
			return sb.toString();
		}
	}

	/** for Strings at least two chars long, inserts a ZWNJ at position 1 */
	public static String mangle(String nick) {
		if (nick.length() > 1) {
			return nick.charAt(0) + UNICODE_ZWNJ + nick.substring(1);
		}
		return nick;
	}
}
