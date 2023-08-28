package games.card;

import java.util.*;

public class CardDeck {
	private List<Card> cardDeck;

	public CardDeck(Integer ammountOfDecks) {
		cardDeck = new LinkedList<Card>();
		for (; ammountOfDecks > 0; ammountOfDecks--) {
			for (CardSuits suit : CardSuits.values()) {
				for (PlayingCards cardType : PlayingCards.values()) {
					cardDeck.add(new Card(suit, cardType, CardView.HIDDEN));
				}
			}
		}
	}

	public void removeAll() {
		cardDeck.clear();
	}

	public void remove(CardSuits suit) {
		for (Card card : cardDeck) {
			if (card.getSuit()
				.equals(suit.getCardSuit())) {
				cardDeck.remove(card);
			}
		}
	}

	public void remove(PlayingCards cardType, Boolean removeAll) {
		for (Card card : cardDeck) {
			if (card.getType()
				.equals(cardType.getCardType())) {
				cardDeck.remove(card);
				if (removeAll == false)
					return;
			}
		}
	}

	public void remove(CardSuits suit, PlayingCards cardType, Boolean removeAll) {
		for (Card card : cardDeck) {
			if (card.getSuit()
				.equals(suit.getCardSuit())
					&& card.getType()
						.equals(cardType.getCardType())) {
				cardDeck.remove(card);
				if (removeAll == false)
					return;
			}
		}
	}

	public void shuffle() {
		List<Card> tempDeck = new ArrayList<Card>();
		Integer index = null;

		for (Integer repeat = cardDeck.size(); repeat > 0; repeat--) {
			index = (int) (Math.random() * cardDeck.size());
			tempDeck.add(cardDeck.get(index));
			cardDeck.remove(index);
		}

		this.cardDeck = tempDeck;
		tempDeck = null;
	}

	public void addCard(Card card) {
		cardDeck.add(card);
	}

	public Integer getCardValue(Integer index) {
		Card tempCard = cardDeck.get(index);
		tempCard.setView(CardView.DISPLAYED);
		return tempCard.getValue();
	}

	public Card peekCard(Integer index) {
		if (cardDeck.size() == 0) {
			return null;
		}
		Card tempCard = cardDeck.get(index);
		tempCard.setView(CardView.DISPLAYED);
		return tempCard;
	}

	public Card getCard() throws NullPointerException {
		if (cardDeck.size() == 0) {
			return null;
		}
		Card tempCard = cardDeck.get(0);
		cardDeck.remove(0);
		return tempCard;
	}

	public Card getCard(Integer index) throws NullPointerException {
		if (cardDeck.size() == 0) {
			return null;
		}
		Card tempCard = cardDeck.get(index);
		cardDeck.remove(index);
		return tempCard;
	}

	public Integer size() {
		return cardDeck.size();
	}

	public void printCardDeck() {
		int counter = 0;
		for (Card card : cardDeck) {
			card.setView(CardView.DISPLAYED);
			System.out.println(++counter + card.getInfo());
			card.setView(CardView.HIDDEN);
		}
	}
}