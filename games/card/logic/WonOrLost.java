package games.card.logic;

public enum WonOrLost {
	FIRST("You finished FIRST!", 1), SECOND("You finished Second..", 2), THIRD("You finished third...",
			3), YOU_LOST("!!!-you lost-!!!", -1);

	private final String message;
	private final Integer finished;

	private WonOrLost(String message, Integer finished) {
		this.message = message;
		this.finished = finished;
	}

	public String toString() {
		return message;
	}

	public Integer getPlace() {
		return finished;
	}
}