package a3;
import tage.*;
import tage.shapes.*;
import java.lang.Math;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;
import tage.input.*;
import tage.input.action.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;
import java.util.concurrent.CopyOnWriteArrayList;
import tage.RenderSystem;
import tage.nodeControllers.*;
public class myGame extends VariableFrameRateGame
{	private static Engine engine;
	private boolean paused=false;
	private boolean mainViewPortControl = true; //note
	private boolean speedBoostUsed = false;
	private boolean isRecentering;
	private boolean dolHasPrize = false;
	private boolean bobEffectStarted = false;
	private int counter=0, prizesCollected =0;
	private double lastFrameTime, currFrameTime, elapsTime;
	private float movementSpeed = .04f;
	private GameObject x, y, z, avatar, plane, prizeCaseO, curPrize;
	private ObjShape dolS, linxS, linyS, linzS, box, planeS, prizeCaseS;
	private TextureImage prizeTex, dolTex, waterTex, brickTex, docTex;
	private Light light1;
	private Camera cam, cam2;
	private CopyOnWriteArrayList<GameObject> prizeBoxes;
	private CameraOrbitController orbitController;
	private NodeController streachController, bobbingController, rotationController;
	private final int AMOUTOFPRIZES = 4;
	private final int MAPSIZE = 9;
	/*============================================================================*/
	/*============================================================================*/
	public myGame() { super(); }
	/*============================================================================*/
	public static void main(String[] args)
	{	myGame game = new myGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();}
	/*============================================================================*/
	@Override
	public void loadShapes()
	{	linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-3f));
		dolS = new ImportedModel("dolphinHighPoly.obj");
		box = new Cube();
		planeS = new Plane();
		prizeCaseS = new Cube();}
	/*============================================================================*/
	@Override
	public void loadTextures()
	{	prizeTex = new TextureImage("prizeBox.jpg");
		dolTex = new TextureImage("Dolphin_HighPolyUV.png");
		waterTex = new TextureImage("waterTexture.jpg");
		brickTex = new TextureImage("brick1.jpg");
		docTex = new TextureImage("wood.jpg");}
	/*============================================================================*/
	@Override
	public void buildObjects()
	{	Matrix4f initialTranslation, initialScale, initialRotation, curScale;
		avatar = new GameObject(GameObject.root(), dolS, dolTex);
		initialTranslation = (new Matrix4f()).translation(0,0.0f,1.5f);
		initialScale = (new Matrix4f()).scaling(1.5f);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY(
				(float)java.lang.Math.toRadians(135.0f));
		avatar.setLocalRotation(initialRotation);
		//----------------------AXIS LINES-------------------------------------
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));
		//--------------------PLANE--------------------------------------------
		plane = new GameObject(GameObject.root(), planeS, waterTex);
		initialScale = (new Matrix4f()).scaling(10f);
		plane.setLocalScale(initialScale);
		//----------------------DOCK-------------------------------------------
		prizeCaseO = new GameObject(GameObject.root(), prizeCaseS, docTex);
		curScale = prizeCaseO.getLocalScale();
		initialScale = (new Matrix4f()).scaling(1.5f);
		initialScale.scaling(curScale.m00(), curScale.m11() *0.2f, curScale.m22());
		prizeCaseO.setLocalScale(initialScale);
		initialTranslation = (new Matrix4f()).translation(0,0,0);
		prizeCaseO.setLocalTranslation(initialTranslation);
		//---------------------PRIZES-------------------------------------------
		setPrizes(AMOUTOFPRIZES);}
	/*============================================================================*/
	private int fluffyClouds, lakeIslands;
	public void loadSkyBoxes()
	{ 	fluffyClouds = (engine.getSceneGraph()).loadCubeMap("fluffyClouds");
		lakeIslands = (engine.getSceneGraph()).loadCubeMap("lakeIslands");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(fluffyClouds);
		(engine.getSceneGraph()).setSkyBoxEnabled(true);
	}
	/*============================================================================*/
	@Override
	public void initializeLights()
	{	Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);}
	/*============================================================================*/
	@Override
	public void createViewports(){
		//("NAME", Left, Bottom, Width, Height);
		(engine.getRenderSystem()).addViewport("MAIN", 0,0.26f,1,.75f);
		(engine.getRenderSystem()).addViewport("LOWER",.0f,.01f,.99f,.25f);
		(engine.getRenderSystem()).getViewport("LOWER").setHasBorder(true);}
	/*============================================================================*/
	@Override
	public void initializeGame()
	{	lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsTime = 0.0;
		(engine.getRenderSystem()).setWindowDimensions(818,430);
		// ------------- CONTROLLERS ------------------------------------------
		streachController = new StretchController(engine, 2.0f);
		streachController.addTarget(avatar);
		(engine.getSceneGraph()).addNodeController(streachController);
		//---------------------------------------------------------------------
		bobbingController = new BobbingController(engine, 3.0f);
		bobbingController.addTarget(prizeCaseO);
		(engine.getSceneGraph()).addNodeController(bobbingController);
		for(GameObject go : getPrizeBoxes()){
			bobbingController.addTarget(go);}
		//---------------------------------------------------------------------
		rotationController = new RotationController(engine, getAvatar().
													getWorldUpVector(),0.001f);
		rotationController.addTarget(getAvatar());
		(engine.getSceneGraph()).addNodeController(rotationController);
		//---------------------------------------------------------------------
		// ------------- positioning the cameras ------------------------------
		cam=engine.getRenderSystem().getViewport("MAIN").getCamera();
		cam2 = engine.getRenderSystem().getViewport("LOWER").getCamera();
		orbitController = new CameraOrbitController(
		 		cam, getAvatar(),  engine);
		cam2.pitch((float)Math.toRadians(90));	
		cam2.setLocation(new Vector3f(0,7,0).add(avatar.getWorldLocation()));
		// ----------------- INPUTS SECTION -----------------------------------
		setupInputs();}
	/*============================================================================*/
	public void update()
	{	lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		elapsTime += (currFrameTime - lastFrameTime) / 1000.0;
		startBobbing();
		//update objects and camera
		avatar.setLocalRotation(avatar.getLocalRotation());
		orbitController.updateCameraPosition();
		setHUDS();
		repositionOverheadView();
		checkIfPickOrPutPrize();
		engine.getInputManager().update(System.currentTimeMillis());}
	/*============================================================================*/
	private void startBobbing() {
		if(elapsTime>0 && !bobEffectStarted){
			bobbingController.toggle();
			bobEffectStarted = true;}}
	/*============================================================================*/
	private void setHUDS() {
		Vector3f h1Color;
		Vector3f h2Color = new Vector3f(0,1,0);
		String h1Text = "";
		float hud2X = engine.getRenderSystem().
						getViewport("MAIN").getActualLeft();
		float hud2Y = engine.getRenderSystem().
						getViewport("MAIN").getActualBottom();
		float hud1X = engine.getRenderSystem().
						getViewport("LOWER").getActualLeft();
		float hud1Y = engine.getRenderSystem().
						getViewport("LOWER").getActualBottom();
		hud2Y -= engine.getRenderSystem().getViewport("MAIN").getActualHeight();

		if(mainViewPortControl){
			h1Text = "Avatar Controls";
			h1Color = new Vector3f(1,0,0);
		}else{
			h1Text = "Camera Controls";
			h1Color = new Vector3f(0,0,1);}

		engine.getHUDmanager().setHUD1(h1Text, h1Color, (int)hud2X, (int)hud2Y);
		engine.getHUDmanager().setHUD2("Prizes Collected: " + prizesCollected +
								"/"+AMOUTOFPRIZES, h2Color, (int)hud1X, (int)hud1Y);
	}
	/*============================================================================*/
	private void repositionOverheadView() {
		if(mainViewPortControl){
			cam2.setLocation(new Vector3f(0,cam2.getLocation().y(),0).
				add(avatar.getWorldLocation()));}
	}
	/*============================================================================*/
	private void checkIfPickOrPutPrize() {
		Matrix4f newLocation;
		for (GameObject go :
				getPrizeBoxes()) {
			if (avatarNearPrize(go)) {
				newLocation = (new Matrix4f()).translation(0f, 0f, -1f);
				bobbingController.remTarget(go);
				go.setLocalTranslation(newLocation);
				go.setParent(getAvatar());
				go.propagateTranslation(true);
				go.propagateRotation(true);
				go.applyParentRotationToPosition(true);
				dolHasPrize = true;
				curPrize = go;}}
		if (avatarCanDepositPrize()) {
			prizesCollected++;
			curPrize.propagateScale(false);
			curPrize.propagateTranslation(false);
			newLocation = (new Matrix4f()).
					translation(0, (prizesCollected * .4f), 0.0f);
			curPrize.setLocalTranslation(newLocation);
			curPrize.setParent(prizeCaseO);
			curPrize.propagateTranslation(true);
			dolHasPrize = false;
			getPrizeBoxes().remove(curPrize);}}
	/*============================================================================*/
	private boolean avatarNearPrize(GameObject go) {
		return getAvatar().getWorldLocation().distance(go.getWorldLocation()) < 1
				&& !dolHasPrize;}
	/*============================================================================*/
	private boolean avatarCanDepositPrize() {
		return getAvatar().getWorldLocation().
				distance(prizeCaseO.getWorldLocation()) < 2 && dolHasPrize;}
	/*============================================================================*/
	/*============================ACTIONS=========================================*/
	@Override
	public void keyPressed(KeyEvent e)
	{	switch (e.getKeyCode())
		{ 	case KeyEvent.VK_0:
				rotationController.toggle();
			break;
			case KeyEvent.VK_1:
				bobbingController.toggle();
			break;
			case KeyEvent.VK_2:
				streachController.toggle();
			break;
		}
		super.keyPressed(e);}
	/*============================================================================*/
	private void setupInputs(){
		InputManager im = engine.getInputManager();
		ToFroAction fwdAction = new ToFroAction(this);
		ToFroAction bwdAction = new ToFroAction(this,true);
		TurnAction rTrnAction = new TurnAction(this,true);
		TurnAction lTrnAction = new TurnAction(this);
		PitchAction upPitch = new PitchAction(this);
		PitchAction dwnPitch = new PitchAction(this,true);
		TurnAction xAxisTurn = new TurnAction(this);
		PitchAction yAxisPitch = new PitchAction(this);
		ChangeViewport changeViewPort = new ChangeViewport(this);
		UpRightAction upRightAction = new UpRightAction(this);
		/*GAMEPADS======================================================== */
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Button._1,fwdAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Button._2,upRightAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Axis.X, xAxisTurn,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Axis.Y, fwdAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		/*KEYBOARDS======================================================== */
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.W, fwdAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);	
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.S, bwdAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);	
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.A, lTrnAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);	
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.D, rTrnAction,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.UP, upPitch,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.DOWN, dwnPitch,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.SPACE,changeViewPort,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.U, upRightAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);}
	/*============================================================================*/
	/*===========================GETTERS==========================================*/
	public GameObject getAvatar() { return avatar; }
	/*============================================================================*/
	public Camera getCam(){return cam;}
	/*============================================================================*/
	public Camera getCam2(){return cam2;}
	/*============================================================================*/
	public CopyOnWriteArrayList<GameObject> getPrizeBoxes(){return prizeBoxes;}
	/*============================================================================*/
	public NodeController getBobbingController() {return bobbingController;}
	/*============================================================================*/
	public boolean isMainViewPortControl() {return mainViewPortControl;}
	/*============================================================================*/
	public int getMapSize() {return MAPSIZE;}
	/*============================================================================*/
	/*===========================SETTERS==========================================*/
	public void setMainViewPortControl(boolean mainViewPortControl) {
		this.mainViewPortControl = mainViewPortControl;}
	/*============================================================================*/
	public void setPrizes(int amoutPrizes){
		float prizeScale = 0.2f, xCoord = 1, zCoord = 1;
		Matrix4f initialTranslation, initialScale;
		GameObject prizeBox;
		prizeBoxes = new CopyOnWriteArrayList<GameObject>();
		Random rand = new Random();
		for (int i = 0; i < amoutPrizes; i++) {
			int r1 = rand.nextInt(MAPSIZE-1);
			int r2 = rand.nextInt(MAPSIZE-1);
			prizeBox = new GameObject(GameObject.root(), new Cube(), prizeTex);
			prizeBox.setLocalTranslation((new Matrix4f()).
									translation(r1*xCoord,0.0f,r2*zCoord));
			prizeBox.setLocalScale((new Matrix4f()).scaling(prizeScale));
			prizeBoxes.add(prizeBox);
			//To place prizes in each quadrant
			if(i%2 == 0){
				xCoord*=(-1);
			}else{
				zCoord*=(-1);
			}
		}
	}
	/*============================================================================*/
}

