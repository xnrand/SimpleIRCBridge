package simpleircbridge;

/**
 * Holds message formats. The number in the constant names represents the number
 * of formatting parameters.
 */
public class SIBConstants {
	/* == MC FORMATS, 1 PARAMETER == */
	public static final String FORMAT1_MC_LOGIN = "--> %s joined the game";
	public static final String FORMAT1_MC_LOGOUT = "<-- %s left the game";
	public static final String FORMAT1_MC_DEATH = "RIP: %s";

	/* == MC FORMATS, 2 PARAMETERS == */
	public static final String FORMAT2_MC_EMOTE = "* %s %s";
	public static final String FORMAT2_MC_BROADCAST = "[%s] %s";
	public static final String FORMAT2_MC_CHAT = "<%s> %s";

	/* == IRC FORMATS, 1 PARAMETER == */
	public static final String FORMAT1_IRC_JOIN = "[IRC] --> %s has joined";

	/* == IRC FORMATS, 2 PARAMETERS == */
	public static final String FORMAT2_IRC_QUIT = "[IRC] <-- %s has quit (%s)";
	public static final String FORMAT2_IRC_PART = "[IRC] <-- %s has left (%s)";
	public static final String FORMAT2_IRC_EMOTE = "[IRC] * %s %s";
	public static final String FORMAT2_IRC_CHAT = "[IRC] <%s> %s";
	public static final String FORMAT2_IRC_NICKCHG = "[IRC] Nick change: %s -> %s";

	/* == IRC FORMATS, 3 PARAMETERS == */
	public static final String FORMAT3_IRC_KICK = "[IRC] <-- %s was kicked by %s (%s)";
}
