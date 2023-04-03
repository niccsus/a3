package tage.input.action;

import a3.myGame;
import net.java.games.input.Event;
/*============================================================================*/
public class ChangeViewport extends AbstractInputAction
{
    private myGame game;
    public ChangeViewport(myGame g)
    { game = g;
    }

    @Override
    public void performAction(float time, Event e)
    { 	if(game.isMainViewPortControl()){
        game.setMainViewPortControl(false);

    }else{
        game.setMainViewPortControl(true);
    }
    }
}