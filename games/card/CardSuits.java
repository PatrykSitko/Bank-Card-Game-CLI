package games.card;

public enum CardSuits {
	CLUBS("Clubs", 0, 0, 0), DIAMONDS("Diamonds", 255, 59, 48), HEARTS("Hearts", 255, 59, 48), SPADES("Spades", 0, 0,
			0);

	private final String cardSuit, rgb;

	private CardSuits(final String cardSuit, final Integer r, final Integer g, final Integer b) {
		this.cardSuit = cardSuit;
		this.rgb = r + ", " + g + ", " + b;
	}

	public String getRGB() {
		return rgb;
	}

	public String getJokerSuit() {
		return "Jokers";
	}

	public String getCardSuit() {
		return cardSuit;
	}

	public String getCardColor() {
		switch (cardSuit) {
		case "Clubs":
			return "Black";
		case "Diamonds":
			return "Red";
		case "Hearts":
			return "Red";
		case "Spades":
			return "Black";
		default:
			return null;
		}
	}
}
