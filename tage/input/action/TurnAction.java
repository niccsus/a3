package tage.input.action;

import a3.myGame;
import net.java.games.input.Event;
/*============================================================================*/
	public class TurnAction extends AbstractInputAction
	{ 	private myGame game;
		private int direction = 1;
        private float movementSpeed = .04f;

		public TurnAction(myGame g)
		{ game = g;
		}
		//This constructor changes direction of rotation by making direction neg
		public TurnAction(myGame g, boolean isRight)
		{ 	game = g;
			if (isRight){direction *= (-1);}

		}
		@Override
		public void performAction(float time, Event e)
		{
			float keyValue = e.getValue();

			if (keyValue > -.2 && keyValue < .2) return; // deadzone
			float movement = movementSpeed* keyValue * direction;
			if(e.getComponent().toString().toLowerCase().contains("axis")) {
				movement *= (-1);
			}
			if(game.isMainViewPortControl()){
				game.getAvatar().yaw(movement);
			}else{
				game.getCam2().truck(movement);


			}
		}}