package games.card;

public enum PlayingCards {
	ASE("Ase", 14), TWO("Two", 2), THREE("Three", 3), FOUR("Four", 4), FIVE("Five", 5), SIX("Six", 6), SEVEN("Seven",
			7), EIGHT("Eight", 8), NINE("Nine",
					9), TEN("Ten", 10), KNAVE("Knave", 11), QUEEN("Queen", 12), KING("King", 13), JOKER("Joker", 15);

	private final String cardName;
	private final Integer cardValue;
	private static boolean haveDoppelgangers = false;

	private PlayingCards(final String cardName, final Integer cardValue) {
		this.cardName = cardName;
		this.cardValue = cardValue;
	}

	public String getCardType() {
		return cardName;
	}

	public Integer getCardValue() {
		return cardValue;
	}

	public Boolean checkIfDoppelgangersPresent() {
		return PlayingCards.haveDoppelgangers;
	}

	public Boolean doppelgangersPresent() {
		return PlayingCards.haveDoppelgangers;
	}

	public void setTrueIfDoppelgangersPresent(boolean haveDoppelgangers) {
		PlayingCards.haveDoppelgangers = haveDoppelgangers;
	}
}
