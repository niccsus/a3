package tage.input.action;

import a3.myGame;
import net.java.games.input.Event;
    public class PitchAction extends AbstractInputAction
	{
		private int direction = -1;
		private myGame game;
        private float movementSpeed = .04f;
		public PitchAction(myGame g)
		{ game = g;
		}
		public PitchAction(myGame g, boolean isDwn)
		{ 	game = g;
			if (isDwn){direction *= (-1);}
		}
		@Override
		public void performAction(float time, Event e)
		{   if(game.isMainViewPortControl()){
			game.getAvatar().pitch(movementSpeed*direction);
		}else{
			//move camera
			game.getCam2().pitch(movementSpeed * direction);
		}
		}
	}
/*============================================================================*/