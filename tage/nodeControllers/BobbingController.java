	package tage.nodeControllers;
	import tage.*;
	import org.joml.*;
	/**
	* A BobbingController is a node controller that, when enabled, causes any object
	* it is attached to to rotmove up and down a small amount on it's Y axis
	* @author Nicholas Burt
	*/
	public class BobbingController extends NodeController
	{ 	private float bobRate = .00007f;
		private float cycleTime = 2000.0f;
		private float totalTime = 0.0f;
		private float direction = 1.0f;
		private Vector3f newLocation;
		private Engine engine;
		public BobbingController(Engine e, float ctime)
		{ 	super();
			cycleTime = ctime;
			engine = e;
			newLocation = new Vector3f();
		}
		public void apply(GameObject go)
		{   float elapsedTime = super.getElapsedTime();
			totalTime += elapsedTime/1000.0f;
			if (totalTime > cycleTime)
			{ direction = -direction;
				totalTime = 0.0f;
			}
			float bobAmt = direction * bobRate * elapsedTime;
			newLocation = go.getWorldLocation().
					add(go.getWorldUpVector().mul(bobAmt));
			go.setLocalLocation(newLocation);} }
	/*============================================================================*/