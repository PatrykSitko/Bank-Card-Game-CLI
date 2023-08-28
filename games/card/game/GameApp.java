package games.card.game;

public class GameApp {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Game game = new Game(2, false);
			game.dealCards(5, 5, 5);
			game.startGame(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
