package utils;

import com.michealharker.saraswati.irc.MircColors;

/**
 * Helper class to convert messages (colours and formatting) between (m)IRC and
 * Minecraft.
 * 
 * @author Fuchs
 *
 */
public class IRCMinecraftConverter {

	/**
	 * Converts an IRC message to Minecraft by replacing formatting with the
	 * corresponding (m)IRC variant. The non existing k (garbage) and m
	 * (strikethrough) are marked.
	 * 
	 * @param message
	 *            message to be converted
	 * @return original message with colours replaced
	 */
	public static String convIRCtoMinecraft(String message) {

		if (message != null) {
			message = escapeMinecraftColour(message);
			message = mIRCtoMinecraftColourify(message);
		}

		return message;

	}

	/**
	 * Converts a Minecraft message to IRC by replacing formatting with the
	 * corresponding Minecraft variant. The non-printable § character is replaced
	 * with the look-alike ⨕ to preserve meaning as good as possible.
	 * 
	 * @param message
	 *            message to be converted
	 * @return original message with colours replaced
	 */
	public static String convMinecraftToIRC(String message) {

		if (message != null) {
			// IRC colours don't have to be stripped as they can't be typed anyway
			message = minecraftTomIRCcolourify(message);
		}

		return message;
	}

	/**
	 * if(message == null) { return message; } Helper method to replace Minecraft
	 * colours with (m)IRC colours, taken from com.michealharker.saraswati.irc
	 * 
	 * @param message
	 *            message to convert
	 * @return message with (m)IRC Colours
	 */
	public static String minecraftTomIRCcolourify(String message) {

		// TODO: Do some state tracking
		// The marks for non-existing IRC modes are not perfect,
		// as they would only last until the next reset, §r.
		// To catch that, regex instead of a simple replace would
		// be needed, which is not warranted at this point.

		return message//
				.replace("§0", MircColors.BLACK)//
				.replace("§1", MircColors.BLUE)//
				.replace("§2", MircColors.GREEN)//
				.replace("§3", MircColors.LIGHT_CYAN)//
				.replace("§4", MircColors.LIGHT_RED)//
				.replace("§5", MircColors.PURPLE)//
				.replace("§6", MircColors.ORANGE)//
				.replace("§7", MircColors.LIGHT_GRAY)//
				.replace("§8", MircColors.GRAY)//
				.replace("§9", MircColors.LIGHT_BLUE)//
				.replace("§a", MircColors.LIGHT_GREEN)//
				.replace("§b", MircColors.LIGHT_CYAN)//
				.replace("§c", MircColors.LIGHT_RED)//
				.replace("§d", MircColors.PINK)//
				.replace("§e", MircColors.YELLOW)//
				.replace("§f", MircColors.WHITE)//
				.replace("§l", MircColors.BOLD)//
				.replace("§n", MircColors.UNDERLINE)//
				.replace("§o", MircColors.ITALIC)//
				.replace("§r", MircColors.NORMAL)//
				.replace("§k", "-§k-") // mark random garbage
				.replace("§m", "-§m-"); // mark strikethrough
	}

	/**
	 * Helper method to replace (m)IRC colours with Minecraft colours, modified from
	 * com.michealharker.saraswati.irc
	 * 
	 * @param message
	 *            message to convert
	 * @return message with (m)IRC colours
	 */
	public static String mIRCtoMinecraftColourify(String message) {

		// TODO: Do some state tracking
		// In Minecraft if a colour code is used after formatting,
		// the formatting code will be disabled after the colour code point.
		// Therefore, when using a colour code together with formatting,
		// it has to be ensured the colour code is used first
		// and to reuse the formatting code when changing colours.
		// Also (m)IRC resets a format on using it twice, e.g. %Bbold%B normal,
		// whilst Minecraft in this case just continues in bold.

		return message//
				.replace(MircColors.WHITE, "§f")//
				.replace(MircColors.BLACK, "§0")//
				.replace(MircColors.BLUE, "§1")//
				.replace(MircColors.GREEN, "§2")//
				.replace(MircColors.LIGHT_CYAN, "§3")//
				.replace(MircColors.LIGHT_RED, "§4")//
				.replace(MircColors.PURPLE, "§5")//
				.replace(MircColors.ORANGE, "§6")//
				.replace(MircColors.LIGHT_GRAY, "§7")//
				.replace(MircColors.GRAY, "§8")//
				.replace(MircColors.LIGHT_BLUE, "§9")//
				.replace(MircColors.LIGHT_GREEN, "§a")//
				.replace(MircColors.LIGHT_CYAN, "§b")//
				.replace(MircColors.LIGHT_RED, "§c")//
				.replace(MircColors.PINK, "§d")//
				.replace(MircColors.YELLOW, "§e")//
				.replace(MircColors.BOLD, "§l")//
				.replace(MircColors.UNDERLINE, "§n")//
				.replace(MircColors.ITALIC, "§o").replace(MircColors.NORMAL, "§r");
	}

	/***
	 * Helper message to replace the non-valid § in minecraft with the look-alike ⨕
	 * character to preserve meaning. Might be extended in the future for other
	 * things that are valid on IRC but not in Minecraft.
	 * 
	 * @param message
	 *            Message to escape
	 * @return message with replaced § characters.
	 */
	public static String escapeMinecraftColour(String message) {
		// Closest lookalike accepted by Minecraft
		// and unlikely to be used, integral around point.
		return message.replace("§", "⨕");
	}

}
