package tage.input.action;

import gameArcPlayground.myGame;
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
    {   
        Matrix4f oldRot =game.getAvatar().getLocalRotation();
        Matrix4f newRot = new Matrix4f().mul(oldRot).mul(0,1,1,1);
        

        // game.getAvatar().setLocalRotation((new Matrix4f()).
        //         rotation(0, 1, 1, 1));
        game.getAvatar().setLocalRotation(newRot.mul(oldRot));

    }
}