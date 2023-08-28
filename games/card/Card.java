package games.card;

public class Card {
	private String hidden = "hidden";
	private CardSuits cardSuit;
	private PlayingCards cardType;
	private CardView cardView;

	public Card(CardSuits cardSuit, PlayingCards cardType, CardView cardView) {
		this.cardSuit = cardSuit;
		this.cardType = cardType;
		this.cardView = cardView;
	}

	public void setView(CardView cardView) {
		this.cardView = cardView;
	}

	public CardView getCardView() {
		return cardView;
	}

	public String getType() {
		switch (cardView) {
		case HIDDEN:
			return hidden;
		default:
			return cardType.getCardType();
		}
	}

	public Integer getValue() {
		switch (cardView) {
		case HIDDEN:
			return null;
		default:
			return cardType.getCardValue();
		}
	}

	public String getSuit() {
		switch (cardView) {
		case HIDDEN:
			return hidden;
		default:
			if (cardType.getCardType()
				.equals(PlayingCards.JOKER.getCardType())) {
				return cardSuit.getJokerSuit();
			} else {
				return cardSuit.getCardSuit();
			}
		}
	}

	public String getColor() {
		switch (cardView) {
		case HIDDEN:
			return hidden;
		default:
			return cardSuit.getCardColor();
		}
	}

	public String getRGB() {
		switch (cardView) {
		case HIDDEN:
			return hidden;
		default:
			return cardSuit.getRGB();
		}
	}

	public String getInfo() {
		switch (cardView) {
		case HIDDEN:
			return "Card: " + hidden + ".";
		default:
			return "Card: (" + getValue() + ")" + getType() + " of " + getSuit() + " (" + getColor() + ").";
		}
	}
}
