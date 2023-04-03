package tage.input.action;
//
import a3.myGame;
import net.java.games.input.Event;
    /*============================================================================*/
	public class ToFroAction extends AbstractInputAction
	{
		private myGame game;
		private int direction = 1, mapSize, outOfBoundCorrector = 1;
		private float movementSpeed = .04f;
		private double lastTime, currentTime;
		public ToFroAction(myGame g)
		{ game = g;
		}
		//Constructor sets direction of transale based on 'isLeft' (1 or -1)
		public ToFroAction(myGame g, boolean isBwd)
		{ 	game = g;
			lastTime=0.0;
			if (isBwd){direction *=(-1);}
		}
		@Override
		public void performAction(float time, Event e)
		{
			double elapsedTime = time-lastTime;
			float keyValue = e.getValue();
			// currentTime = game.getElapsTime();
			// System.out.println(elapsedTime);
			checkBounds();
			if (keyValue > -.2 && keyValue < .2) return;
			float movement = movementSpeed * direction * keyValue;
			if(e.getComponent().toString().toLowerCase().contains("axis")) {
				movement *= (-1);
			}
			if(game.isMainViewPortControl()){
				game.getAvatar().moveToFro(movement * outOfBoundCorrector );
			}else{
			//move camera
				game.getCam2().moveToFro(movement);
		}

		}
		/*This method is to check if the object is moving out of bounds on the map
		 *if so the direction of movement is reversed untill inbounds */
		public void checkBounds(){
			float x = game.getAvatar().getWorldLocation().x();
			float y = game.getAvatar().getWorldLocation().y();
			float z = game.getAvatar().getWorldLocation().z();
			mapSize = game.getMapSize();
			if(Math.abs(z) > mapSize || (Math.abs(x) > mapSize || y < -0.3)){
				outOfBoundCorrector *=(-1);}
			else{
				outOfBoundCorrector = 1;
			}
		}
	}