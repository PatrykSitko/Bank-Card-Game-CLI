package games.card.game;

import java.util.*;

import games.card.Card;
import games.card.CardDeck;
import games.card.CardSuits;
import games.card.CardView;
import games.card.PlayingCards;
import games.card.logic.ContinueOrWin;
import games.card.logic.PlayOrEndTurn;
import games.card.exceptions.PlayerCountIsTooLow_Exception;
import games.card.player.Player;
import games.card.player.winners.Winner;

public class Game {

	private String gameRules = "=============================================\n" + "Rules:\n" + " Empty..."
			+ "\n=============================================";
	private List<Player> playerList = null;
	private List<Winner> winnersCountList = null;
	private CardDeck deck = null;
	private CardDeck playingDeck = null;
	private Scanner sc;
	private Boolean cardsDealed = false;
	private final Integer playerCount;
	private Integer playingCards = 0, nonPlayingCards = 0, hiddenCards = 0;

	public Game(Integer playerCount, boolean useOnlyOneDeck) throws PlayerCountIsTooLow_Exception {
		if (playerCount < 2)
			throw new PlayerCountIsTooLow_Exception("Min. 2 players!");

		playingDeck = new CardDeck(1);
		playingDeck.removeAll();

		this.playerCount = playerCount;
		playerList = new LinkedList<Player>();
		winnersCountList = new LinkedList<Winner>();

		if (useOnlyOneDeck)
			deck = new CardDeck(1);
		else
			deck = new CardDeck((int) (playerCount / 2));

		deck.remove(CardSuits.CLUBS, PlayingCards.JOKER, true);
		deck.remove(CardSuits.DIAMONDS, PlayingCards.JOKER, true);
		for (int count = 0; count < playerCount;) {
			playerList.add(new Player("Player" + ++count));
		}
	}

	public Integer minPlayingCards_Size() {
		return playingCards;
	}

	public Integer nonPlayingCards_Size() {
		return nonPlayingCards;
	}

	public Integer hiddenCards_Size() {
		return hiddenCards;
	}

	public Integer getDeckSize() {
		return deck.size();
	}

	public Integer getPlayerCount() {
		return playerCount;
	}

	public Boolean checkIfPlayerWon(ContinueOrWin wonOrNot, Player player) {
		if (wonOrNot.equals(ContinueOrWin.PLAYER_WON_THE_GAME)) {
			winnersCountList.add(new Winner(player.getName()));
			playerList.remove(player);
			return true;
		}
		return false;
	}

	public ContinueOrWin checkPlayerHand(Player player) {
		if (player.allPlayerDecks_Size() == 0 && deck.size() == 0) {
			return ContinueOrWin.PLAYER_WON_THE_GAME;
		}
		if (player.getPlayingDeck_Size() < minPlayingCards_Size()) {

			if (deck.size() > 0) {
				do {
					player.addCard_PlayingDeck(deck.getCard(), CardView.DISPLAYED);
				} while (deck.size() > 0 && player.getPlayingDeck_Size() < minPlayingCards_Size());
			} else if (player.getPlayingDeck_Size() == 0 && player.getNonPlayingDeck_Size() > 0) {
				do {
					player.addCard_PlayingDeck(player.getCard_NonPlayingDeck(), CardView.DISPLAYED);
				} while (player.getNonPlayingDeck_Size() > 0);
			} else if (player.getPlayingDeck_Size() == 0) {
				do {
					player.addCard_PlayingDeck(player.getCard_HiddenDeck(), CardView.HIDDEN);
				} while (player.getHiddenDeck_Size() > 0);
			}

		}
		return ContinueOrWin.CONTINUE_GAME;
	}

	public void dealCards(Integer playingCards, Integer nonPlayingCards, Integer hiddenCards) {
		PlayOrEndTurn.setDoppelgangersAreNotPresent();
		this.playingCards = playingCards;
		this.nonPlayingCards = nonPlayingCards;
		this.hiddenCards = hiddenCards;
		if (!cardsDealed) {
			cardsDealed = true;
			do {
				deck.shuffle();
			} while (deck.peekCard(0)
				.getType()
				.equals(PlayingCards.JOKER.getCardValue())
					|| deck.peekCard(0)
						.getType()
						.equals(PlayingCards.TEN.getCardType()));
			playingDeck.addCard(deck.getCard());

			for (Player player : playerList) {
				while (player.getPlayingDeck_Size() < playingCards) {
					player.addCard_PlayingDeck(deck.getCard(), CardView.DISPLAYED);
				}
				while (player.getNonPlayingDeck_Size() < nonPlayingCards) {
					player.addCard_NonPlayingDeck(deck.getCard());
				}
				while (player.getHiddenDeck_Size() < hiddenCards) {
					player.addCard_HiddenDeck(deck.getCard());
				}
			}
		}
	}

	public PlayOrEndTurn addGameCard(Card card, Player player) {
		if (deck.size()
			.equals(0)) {
			if (player.allPlayerDecks_Size() >= playingCards) {
				if (card.getCardView()
					.equals(CardView.HIDDEN)) {
					if (player.allCardsHidden()) {
						player.peekCard_PlayingDeck(card)
							.setView(CardView.DISPLAYED);
						card.setView(CardView.DISPLAYED);
					} else {
						PlayOrEndTurn.setDoppelgangersAreNotPresent();
						return PlayOrEndTurn.CARD_NOT_ALLOWED;
					}
				}
			}
		}
		Integer index = 0, stopIndex = 0, dopplegangersPlayingDeck = 0;
		if (playingDeck.size() != 0) {

			// Start look for doppelgangers in playingDeck.
			if (playingDeck.size() < 3) {
				if (playingDeck.size() != 0) {
					index = playingDeck.size() - 1;
				} else {
					index = playingDeck.size();
				}
				for (stopIndex = 0; index > stopIndex; index--) {
					if (card.getType()
						.equals(playingDeck.peekCard(index)
							.getType())) {
						++dopplegangersPlayingDeck;
					}
				}
			} else if (playingDeck.size() >= 3) {
				for (index = playingDeck.size() - 1, stopIndex = playingDeck.size() - 4; index > stopIndex; index--) {
					if (playingDeck.peekCard(index) != null) {
						if (card.getType()
							.equals(playingDeck.peekCard(index)
								.getType())) {
							++dopplegangersPlayingDeck;
						}
					}
				}
			}

			if (dopplegangersPlayingDeck >= 3) {
				playingDeck.addCard(card);
				player.removeCard_PlayingDeck(card);
				playingDeck.removeAll();
				return PlayOrEndTurn.PLAY_SECOND_CARD;
			}
			// End look for doppelgangers in playingDeck.

			if (card.getType()
				.equals(PlayingCards.JOKER.getCardType())) {
				playingDeck.addCard(card);
				playingDeck.removeAll();
				player.removeCard_PlayingDeck(card);
				PlayOrEndTurn.setDoppelgangersAreNotPresent();
				return PlayOrEndTurn.PLAY_SECOND_CARD;
			}

			if (playingDeck.peekCard(playingDeck.size() - 1)
				.getType()
				.equals(PlayingCards.SIX.getCardType())) {
				if (player.getIfDoppelgangersPresent_PlayingDeck(card)) {
					if (card.getValue() > PlayingCards.SIX.getCardValue()) {
						PlayOrEndTurn.setDoppelgangersAreNotPresent();
						return PlayOrEndTurn.CARD_NOT_ALLOWED;
					}
					playingDeck.addCard(card);
					player.removeCard_PlayingDeck(card);
					checkForDoppelgangers(card, player);
					return PlayOrEndTurn.END_TURN;
				}

				if (card.getValue() >= PlayingCards.SIX.getCardValue()) {
					PlayOrEndTurn.setDoppelgangersAreNotPresent();
					return PlayOrEndTurn.CARD_NOT_ALLOWED;
				}
				if (card.getValue() < PlayingCards.SIX.getCardValue()) {
					playingDeck.addCard(card);
					player.removeCard_PlayingDeck(card);
					checkForDoppelgangers(card, player);
					return PlayOrEndTurn.END_TURN;
				}
			}
			if (card.getType()
				.equals(PlayingCards.EIGHT.getCardType())) {
				if (playingDeck.peekCard(playingDeck.size() - 1)
					.getValue() <= PlayingCards.EIGHT.getCardValue()) {
					playingDeck.addCard(card);
					player.removeCard_PlayingDeck(card);
					checkForDoppelgangers(card, player);
					return PlayOrEndTurn.SKIP_NEXT_PLAYER_TURN;
				}
			}

			if (card.getType()
				.equals(PlayingCards.TEN.getCardType())) {
				if ((playingDeck.peekCard(playingDeck.size() - 1)
					.getValue() <= PlayingCards.TEN.getCardValue())) {
					playingDeck.addCard(card);
					playingDeck.removeAll();
					player.removeCard_PlayingDeck(card);
					PlayOrEndTurn.setDoppelgangersAreNotPresent();
					return PlayOrEndTurn.PLAY_SECOND_CARD;
				}
			}

			if (card.getType()
				.equals(PlayingCards.TWO.getCardType())) {
				playingDeck.addCard(card);
				player.removeCard_PlayingDeck(card);
				checkForDoppelgangers(card, player);
				return PlayOrEndTurn.END_TURN;
			} else if (playingDeck.peekCard(playingDeck.size() - 1)
				.getType()
				.equals(PlayingCards.TWO.getCardType())) {
				playingDeck.addCard(card);
				player.removeCard_PlayingDeck(card);
				checkForDoppelgangers(card, player);
				return PlayOrEndTurn.END_TURN;
			}

			if (card.getValue() >= playingDeck.peekCard(playingDeck.size() - 1)
				.getValue()) {
				playingDeck.addCard(card);
				player.removeCard_PlayingDeck(card);
				checkForDoppelgangers(card, player);
				return PlayOrEndTurn.END_TURN;
			} else if (card.getValue() < playingDeck.peekCard(playingDeck.size() - 1)
				.getValue()) {
				PlayOrEndTurn.setDoppelgangersAreNotPresent();
				return PlayOrEndTurn.CARD_NOT_ALLOWED;
			}

			PlayOrEndTurn.setDoppelgangersAreNotPresent();
			return PlayOrEndTurn.SOMETHING_WENT_WRONG;
		} else {
			playingDeck.addCard(card);
			player.removeCard_PlayingDeck(card);
			checkForDoppelgangers(card, player);
			if (card.getType()
				.equals(PlayingCards.TEN.getCardType())) {
				playingDeck.removeAll();
				PlayOrEndTurn.setDoppelgangersAreNotPresent();
				return PlayOrEndTurn.PLAY_SECOND_CARD;
			} else if (card.getType()
				.equals(PlayingCards.JOKER.getCardType())) {
				playingDeck.removeAll();
				PlayOrEndTurn.setDoppelgangersAreNotPresent();
				return PlayOrEndTurn.PLAY_SECOND_CARD;
			} else if (card.getType()
				.equals(PlayingCards.EIGHT.getCardType())) {
				return PlayOrEndTurn.SKIP_NEXT_PLAYER_TURN;
			} else {
				return PlayOrEndTurn.END_TURN;
			}
		}
	}

	public void startGame(Integer endGameIf_X_playersWon) {
		boolean skipPlayer = false;
		do {
			for (Player player : playerList) {
				{
					System.out.println("\n\n\n=============================================\n" + player.getName()
							+ ", Your turn started:" + "\n=============================================");
					if (!skipPlayer) {
						skipPlayer = stage2(stage1(player), player);
					} else {
						skipPlayer = false;
					}
					System.out.println("=============================================\n" + player.getName()
							+ ", Your turn ended.\n" + "\n=============================================");
					for (Integer i = 3; i > 0; i--) {
						try {
							System.out.println("next players turn in " + i);
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} while (winnersCountList.size() < endGameIf_X_playersWon);
		System.out.println("\tGame ended!");
		Integer place = 0;
		for (Winner winner : winnersCountList) {
			System.out.println("\t" + ++place + ".place:" + winner.getName());
		}
		for (Player player : playerList) {
			System.out.println("\t" + ++place + ".place:" + player.getName());
		}
	}

	private Integer stage1(Player player) {
		Boolean playerWon = false;
		Integer playerInput = -2;
		do {
			playerInput = -2;
			playerWon = checkIfPlayerWon(checkPlayerHand(player), player);

			if (playerWon) {
				playerWon = false;
				System.out
					.println("!!!Congratulations " + player.getName() + " you finished " + winnersCountList.size());
			}
			System.out.println("Deck size:" + deck.size() + ", ingame cards:" + playingDeck.size()
					+ "\n=============================================");

			if (playingDeck.size() > 0) {
				System.out.println("=============================================\n" + "Ingame "
						+ playingDeck.peekCard(playingDeck.size() - 1)
							.getInfo()
						+ "\n=============================================");
			} else {
				System.out.println("=============================================\n"
						+ "Ingame card: NONE, you\'re free to choose one!"
						+ "\n=============================================");
			}
			System.out.println("Your cards: ");
			player.printPlayingDeck();
			if (playingDeck.size() != 0) {
				System.out.println((player.getPlayingDeck_Size() + 1) + ".Option: Take all playing cards."
						+ "\n=============================================" + "\n0.Option: Show gameplay rules.");
				System.out.println("\t\t   =============\n" + "\t\t   Choose a card");
				System.out.println("\t\t\tOR\n\t\t    take cards: " + "\n\t\t    ==========");
			} else {
				System.out.println("\t\t   =============\n" + "\t\t   Choose a card" + "\n\t\t   =============");
			}
			playerInput = this.getPlayerInput(player);
			if (playerInput == -1) {
				System.out.println(toString_GameRules());
				playerInput = -2;
			}
		} while (playerInput == -2);
		if ((playerInput + 2) <= (player.getPlayingDeck_Size())) {
			System.out.println("You did choose option(nr." + (playerInput + 1) + "):\n"
					+ player.getCard_PlayingDeck(playerInput, false)
						.getInfo());
		}
		return playerInput;
	}

	private PlayOrEndTurn checkTurn(Integer stage1, Player player) {
		Card tempCard;
		Card choosenCard;
		PlayOrEndTurn checkTurn;
		Integer choise;
		if (stage1.equals(player.getPlayingDeck_Size())) {
			takeFromPool(player);
			System.out
				.println("You choose to pick all the cards that are in play.\nBecause of that, you loose your turn.");
			return PlayOrEndTurn.END_TURN;
		} else {
			choosenCard = player.getCard_PlayingDeck(stage1, false);
			checkTurn = this.addGameCard(player.getCard_PlayingDeck(stage1, false), player);
			if (checkTurn.getDoppelgangersArePresent()) {
				do {
					System.out.println("=============================================\n"
							+ "You have more cards with the value:" + choosenCard.getType()
							+ ".\nIf you want, you can add more cards too the game with the same value("
							+ choosenCard.getType()
							+ ").\nElse choose a card with a onther value... \n(The choosen card won\'t be added too the game if it don\'t match the value("
							+ choosenCard.getType() + "))" + "\n=============================================");
					choise = stage1(player);
					tempCard = player.playCard(choise, false);
					if (tempCard.getType()
						.equals(choosenCard.getType())) {
						checkTurn = this.addGameCard(tempCard, player);
					}
					if (player.getIfDoppelgangersPresent_PlayingDeck(choosenCard)
						.equals(false)) {
						System.out.println("You have no more cards of the same value, game will continue...");
						return checkTurn;
					}
					if (!tempCard.getType()
						.equals(choosenCard.getType())) {
						System.out.println("[!=]Values don't match, game will continue...[!=]");
					}
				} while (tempCard.getType()
					.equals(choosenCard.getType()));
			}
			return checkTurn;
		}

	}

	private Boolean stage2(Integer stage1, Player player) {
		PlayOrEndTurn checkTurn;
		do {
			checkTurn = checkTurn(stage1, player);
			switch (checkTurn) {
			case CARD_NOT_ALLOWED:
				do {
					System.out.println("=============================================\n"
							+ "Card not allowed!\nChoose another one instead."
							+ "\n=============================================");
					checkTurn = checkTurn(stage1(player), player);
				} while (checkTurn.equals(PlayOrEndTurn.CARD_NOT_ALLOWED));
				break;
			case PLAY_SECOND_CARD:
				do {
					System.out.println("=============================================\n"
							+ "You have to play a new card." + "\n=============================================");
					checkTurn = checkTurn(stage1(player), player);
				} while (checkTurn.equals(PlayOrEndTurn.PLAY_SECOND_CARD));
				break;
			case END_TURN:
			case SKIP_NEXT_PLAYER_TURN:
				break;
			case SOMETHING_WENT_WRONG:
			default:
				System.out.println("=============================================\n" + "Something went wrong!"
						+ "\n=============================================");
				break;
			}
		} while (checkTurn.equals(PlayOrEndTurn.PLAY_SECOND_CARD) || checkTurn.equals(PlayOrEndTurn.CARD_NOT_ALLOWED));
		if (checkTurn.equals(PlayOrEndTurn.SKIP_NEXT_PLAYER_TURN))
			return true;
		else
			return false;
	}

	private Integer getPlayerInput(Player player) {
		Integer tempInt = -2;
		try {
			do {
				sc = new Scanner(System.in);
				tempInt = sc.nextInt() - 1;
			} while (tempInt < -1 || tempInt > player.getPlayingDeck_Size());
			return tempInt;
		} catch (Exception e) {
			System.out.println("\t-================================-\n" + "\t|!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!|\n"
					+ "\t|!Please give me a valid number.!|" + "\n\t|!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!|"
					+ "\n\t-================================-" + "\n=============================================");
		}
		return tempInt;
	}

	private void takeFromPool(Player player) {
		if (!player.getPlayingDeck_Size()
			.equals(0)) {
			do {
				player.addCard_PlayingDeck(playingDeck.getCard(), CardView.DISPLAYED);
			} while (playingDeck.size() > 0);
		}
	}

	public void setToString_GameRules(String rules) {
		// TODO Auto-generated method stub
		this.gameRules = rules;
	}

	public String toString_GameRules() {
		// TODO Auto-generated method stub
		return gameRules;
	}

	public void checkForDoppelgangers(Card card, Player player) {
		if (player.getIfDoppelgangersPresent_PlayingDeck(card)) {
			PlayOrEndTurn.setDoppelgangersArePresent();
		} else {
			PlayOrEndTurn.setDoppelgangersAreNotPresent();
		}
	}
}
