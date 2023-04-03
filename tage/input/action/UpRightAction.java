package tage.input.action;

import a3.myGame;
import net.java.games.input.Event;
import org.joml.*;
/*============================================================================*/
public class UpRightAction extends AbstractInputAction
{ 	private myGame game;
    public UpRightAction(myGame g)
    { game = g;
    }
    @Override
    public void performAction(float time, Event e)
    {   game.getAvatar().setLocalRotation((new Matrix4f()).
                rotation(0, 1, 1, 1));
    }
}