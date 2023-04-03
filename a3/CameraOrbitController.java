package a3;
import a3.myGame;
import net.java.games.input.Event;
import tage.input.*;
import tage.input.action.*;
import tage.input.action.AbstractInputAction;
import tage.Camera;
import tage.GameObject;
import tage.Engine;
import org.joml.*;
import java.lang.Math;

/*============================================================================*/
	public class CameraOrbitController
	{ 	private Engine engine;
		private Camera camera; // the camera being controlled
		private GameObject avatar; // the target avatar the camera looks at
		private float cameraAzimuth; // rotation around target Y axis
		private float cameraElevation; // elevation of camera above target
		private float cameraRadius; // distance between camera and target
		/*----------------------------------------------------------------------------*/
		public CameraOrbitController(Camera cam, GameObject av,
									 Engine e)
		{ 	engine = e;
			camera = cam;
			avatar = av;
			cameraAzimuth = 0.0f; // start BEHIND and ABOVE the target
			cameraElevation = 20.0f; // elevation is in degrees
			cameraRadius = 2.0f; // distance from camera to avatar
			setupInputs("gpName");
			updateCameraPosition();
		}
		/*----------------------------------------------------------------------------*/
		private void setupInputs(String gp)
		{ 	InputManager im = engine.getInputManager();

			OrbitAzimuthAction azmAction = new OrbitAzimuthAction(false);
			OrbitAzimuthAction revAzmAction = new OrbitAzimuthAction(true);
			OrbitRadiusAction radAction = new OrbitRadiusAction(false);
			OrbitRadiusAction revRadAction = new OrbitRadiusAction(true);
			OrbitElevationAction elevationAction = new OrbitElevationAction(false);
			OrbitElevationAction revElevationAction = new OrbitElevationAction(true);
			/*GAMEPADS======================================================== */
			im.associateActionWithAllGamepads(
					net.java.games.input.Component.Identifier.Axis.RY, elevationAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllGamepads(
					net.java.games.input.Component.Identifier.Axis.RX, azmAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllGamepads(
					net.java.games.input.Component.Identifier.Axis.POV, radAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			/*KEYBOARDS======================================================== */
			im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.L, azmAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllKeyboards(
					net.java.games.input.Component.Identifier.Key.K, revAzmAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.P, radAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllKeyboards(
					net.java.games.input.Component.Identifier.Key.O, revRadAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllKeyboards(
					net.java.games.input.Component.Identifier.Key.PERIOD, elevationAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateActionWithAllKeyboards(
					net.java.games.input.Component.Identifier.Key.COMMA, revElevationAction,
					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		/*----------------------------------------------------------------------------*/
		// Compute the camera’s azimuth, elevation, and distance, relative to
		// the target in spherical coordinates, then convert to world Cartesian
		// coordinates and set the camera position from that.
		public void updateCameraPosition()
		{ 	Vector3f avatarRot = avatar.getWorldForwardVector();
			double avatarAngle = Math.toDegrees((double)
					avatarRot.angleSigned(new Vector3f(0,0,-1), new Vector3f(0,1,0)));
			float totalAz = cameraAzimuth - (float)avatarAngle;
			double theta = Math.toRadians(cameraAzimuth);
			double phi = Math.toRadians(cameraElevation);
			float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
			float y = cameraRadius * (float)(Math.sin(phi));
			float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
			camera.setLocation(new
					Vector3f(x,y,z).add(avatar.getWorldLocation()));
			camera.lookAt(avatar);
			
		}
		/*----------------------------------------------------------------------------*/
		private class OrbitAzimuthAction extends AbstractInputAction {
			private float rotAmount,  direction = 1;
			public OrbitAzimuthAction(boolean negativeRotation)
			{ if (negativeRotation){
				direction *= (-1);
			}
			}
			public void performAction(float time, Event event) {
				if (event.getValue() < -0.2) {
					rotAmount=-0.2f*direction; }
				else {
					if (event.getValue() > 0.2) {
						rotAmount=0.2f*direction; }
					else {
						rotAmount=0.0f; }}
				cameraAzimuth += rotAmount;
				cameraAzimuth = cameraAzimuth % 360;
				updateCameraPosition();}
		}
		/*----------------------------------------------------------------------------*/
		private class OrbitRadiusAction extends AbstractInputAction {
			private float radAmount, direction = 1;
			public OrbitRadiusAction(boolean negativeRotation)
			{ if (negativeRotation){
				direction *= (-1);
			}}
			public void performAction(float time, Event event) {
				float keyValue = event.getValue();
				if (keyValue > -.2 && keyValue < .2) return;
				direction = 1;
				if (event.getValue() > 0.74 && event.getValue() < 1) {
					direction *= (-1); }
				radAmount=0.2f*direction;
				cameraRadius += radAmount;
				updateCameraPosition();
			}
		}
		/*----------------------------------------------------------------------------*/
		private class OrbitElevationAction extends AbstractInputAction {
			private float elvAmount, direction = 1;
			public OrbitElevationAction(boolean negativeRotation)
			{ if (negativeRotation){
				direction *= (-1);
			}}
			public void performAction(float time, Event event) {
				if (event.getValue() < -0.2) {
					elvAmount=-0.2f*direction; }
				else {
					if (event.getValue() > 0.2) {
						elvAmount=0.2f*direction; }
					else {
						elvAmount=0.0f; }}
				cameraElevation += elvAmount;
				cameraElevation = cameraElevation % 360;
				updateCameraPosition();
			}
		}
	}