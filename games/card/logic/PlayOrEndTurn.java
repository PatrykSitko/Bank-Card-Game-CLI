package games.card.logic;

public enum PlayOrEndTurn {
	PLAY_SECOND_CARD, CARD_NOT_ALLOWED, END_TURN, SKIP_NEXT_PLAYER_TURN, SOMETHING_WENT_WRONG;

	private static Boolean haveDoppelgangers = false;

	public Boolean getDoppelgangersArePresent() {
		return PlayOrEndTurn.haveDoppelgangers;
	}

	public static Boolean getDoppelgangersArePresent_Static() {
		return PlayOrEndTurn.haveDoppelgangers;
	}

	public static void setDoppelgangersArePresent() {
		PlayOrEndTurn.haveDoppelgangers = true;
	}

	public static void setDoppelgangersAreNotPresent() {
		PlayOrEndTurn.haveDoppelgangers = false;
	}
}
