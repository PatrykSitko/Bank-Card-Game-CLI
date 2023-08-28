package games.card.player;

import java.util.*;

import games.card.Card;
import games.card.CardView;

public class Player {
	private Integer count;
	private final String playerName, nullPointerException = "Deck is empty...";
	private List<Card> player_PlayingDeck = new LinkedList<Card>();
	private List<Card> player_NonPlayingDeck = new LinkedList<Card>();
	private List<Card> player_HiddenDeck = new LinkedList<Card>();

	public Player(String name) {
		this.playerName = name;
	}

	public String getName() {
		return playerName;
	}

	public boolean allCardsHidden() {
		for (Card card : player_PlayingDeck) {
			if (card.getCardView()
				.equals(CardView.DISPLAYED)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkIfHidden(Card card) {
		if (card.getCardView()
			.equals(CardView.HIDDEN)) {
			return true;
		}
		return false;
	}

	public boolean checkIfDisplayed(Card card) {
		if (card.getCardView()
			.equals(CardView.DISPLAYED)) {
			return true;
		}
		return false;
	}

	public void removeCard_PlayingDeck(Card card) {
		player_PlayingDeck.remove(card);
	}

	public Card playCard(Integer index) throws NullPointerException {
		if (getPlayingDeck_Size().equals(0))
			throw new NullPointerException(nullPointerException);
		Card tempCard = player_PlayingDeck.get(index);
		player_PlayingDeck.remove(index);
		return tempCard;
	}

	public Card playCard(Integer index, Boolean remove) throws NullPointerException {
		if (getPlayingDeck_Size().equals(0))
			throw new NullPointerException(nullPointerException);
		Card tempCard = player_PlayingDeck.get(index);
		if (remove) {
			player_PlayingDeck.remove(index);
		}
		return tempCard;
	}

	public Card playCardIf(Card equalsThisCard, Integer playerChoise) {
		if (player_PlayingDeck.get(playerChoise)
			.getType()
			.equals(equalsThisCard.getType())) {
			return equalsThisCard;
		}
		return null;
	}

	public List<Card> getPlayer_PlayingDeck() {
		return player_PlayingDeck;
	}

	public void addCard_PlayingDeck(Card card, CardView cardView) throws NullPointerException {
		card.setView(cardView);
		player_PlayingDeck.add(card);
	}

	public Card getCard_PlayingDeck() throws NullPointerException {
		if (getPlayingDeck_Size().equals(0))
			throw new NullPointerException(nullPointerException);
		Card tempCard = player_PlayingDeck.get(0);
		player_PlayingDeck.remove(0);
		return tempCard;
	}

	public Card getCard_PlayingDeck(Integer index, Boolean remove) {
		Card tempCard = player_PlayingDeck.get(index);
		if (remove)
			player_PlayingDeck.remove(index);
		return tempCard;
	}

	public Card peekCard_PlayingDeck(Card card) {
		for (Card playerCard : player_PlayingDeck) {
			if (playerCard.getInfo()
				.equals(card.getInfo())) {
				return playerCard;
			}
		}
		return card;
	}

	public void addCard_NonPlayingDeck(Card card) {
		player_NonPlayingDeck.add(card);
	}

	public Card getCard_NonPlayingDeck() throws NullPointerException {
		if (getNonPlayingDeck_Size().equals(0))
			throw new NullPointerException(nullPointerException);
		Card tempCard = player_NonPlayingDeck.get(0);
		player_NonPlayingDeck.remove(0);
		return tempCard;
	}

	public Card getCard_NonPlayingDeck(Integer index) throws IndexOutOfBoundsException {
		if (index < getNonPlayingDeck_Size() || index > getNonPlayingDeck_Size())
			throw new IndexOutOfBoundsException();
		Card tempCard = player_NonPlayingDeck.get(0);
		player_NonPlayingDeck.remove(0);
		return tempCard;
	}

	public void addCard_HiddenDeck(Card card) {
		player_HiddenDeck.add(card);
	}

	public Card getCard_HiddenDeck() throws NullPointerException {
		if (getHiddenDeck_Size().equals(0))
			throw new NullPointerException(nullPointerException);
		Card tempCard = player_HiddenDeck.get(0);
		player_HiddenDeck.remove(0);
		return tempCard;
	}

	public Card getCard_HiddenDeck(Integer index) throws IndexOutOfBoundsException {
		if (index < getHiddenDeck_Size() || index > getHiddenDeck_Size())
			throw new IndexOutOfBoundsException();
		Card tempCard = player_HiddenDeck.get(index);
		player_HiddenDeck.remove(index);
		return tempCard;
	}

	public Boolean getIfDoppelgangersPresent_PlayingDeck(Card card) {
		for (Card playerCard : player_PlayingDeck) {
			if (card.getType() == playerCard.getType()) {
				return true;
			}
		}
		return false;

	}

	public Integer getPlayingDeck_Size() {
		return player_PlayingDeck.size();
	}

	public Integer getNonPlayingDeck_Size() {
		return player_NonPlayingDeck.size();
	}

	public Integer getHiddenDeck_Size() {
		return player_HiddenDeck.size();

	}

	public Integer allPlayerDecks_Size() {
		return player_PlayingDeck.size() + player_NonPlayingDeck.size() + player_HiddenDeck.size();
	}

	public void printPlayingDeck() {
		count = 0;
		for (Card card : player_PlayingDeck) {
			System.out.println(++count + "." + card.getInfo());
		}
	}

	public void printNonPlayingDeck() {
		System.out.println("Non-playing deck:");
		count = 0;
		for (Card card : player_NonPlayingDeck) {
			System.out.println(++count + "." + card.getInfo());
		}
	}

	public void printHiddenDeck(CardView cardView) {
		System.out.println("Hidden deck:");
		count = 0;
		for (Card card : player_HiddenDeck) {
			card.setView(cardView);
			System.out.println(++count + "." + card.getInfo());
			card.setView(CardView.HIDDEN);
		}
	}

}
