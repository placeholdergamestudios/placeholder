package org.md2.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.opengl.*;
import org.md2.rendering.ShaderProgram;
import org.md2.rendering.Texture;
import org.md2.rendering.TextureObject;
import org.md2.common.Tools;
import org.md2.rendering.VAOType;
import org.md2.rendering.VertexArrayObject;
import org.md2.gameobjects.GameObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.Item;
import org.md2.input.KeyboardInput;
import org.md2.worldmanagement.Inventory;
import org.md2.worldmanagement.InventorySlot;


public class GraphicRendererV2 extends Thread
{
    
    private static int fps = 30; // Frames per second
    private static int tpf = 1000/fps; //Time per Frame in ms
    
    private static final int zNear = +10;
    private static final int zFar = -10;

	private static  Matrix4f standardProjection;
    private static Vector2i resolution;
    private boolean wasResized;
    
    private GLFWErrorCallback errorCallback;
    
    private static Vector2f mousePosition;
    
    private long window;
    private ShaderProgram shaderProgram;
    
    public static Vector2f cameraCenter;
    private int ppu;
    private float hudSize;
    
    private HashMap<Character, TextureObject> fontChars;
    private HashMap<VAOType, VertexArrayObject> VAOs;
    private HashMap<Texture, TextureObject> TOs;
    public static Vector2f renderDistance;
    
    public GraphicRendererV2()
    {
    	
    	resolution = new Vector2i(1200, 900);
    	ppu = (int)(resolution.x/10F);
    	hudSize = 1.5F;
    	renderDistance = new Vector2f(resolution.x/(float)ppu, resolution.y/(float)ppu);
    	standardProjection = new Matrix4f().ortho(-renderDistance.x, renderDistance.x, -renderDistance.y, renderDistance.y, zNear, zFar);
    	cameraCenter = new Vector2f(0, 0);
    	mousePosition = new Vector2f();
    	System.out.println("Using LWJGL " + Version.getVersion() + "!");
    	setUpGLFW();
    	setUpShaders();
    	System.out.println("Using OpenGL version " + GL11.glGetString(GL11.GL_VERSION));
    	createVAOs();
    	createTOs();
    	createFontChars();
    }
    
    public void run()
    {
        while(!GLFW.glfwWindowShouldClose(window) && Game.getGame().isRunning()){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            
            render();
            
			GLFW.glfwSwapBuffers(window); // swap the color buffers			
			GLFW.glfwPollEvents();			
        }
        Game.getGame().setRunning(false);
        this.terminate();
         
    }
    
    private void render()
    {
    	if (wasResized) {
			GL11.glViewport(0, 0, resolution.x, resolution.y);
			ppu = (int)(resolution.x/10F);
			renderDistance = new Vector2f(resolution.x/(float)ppu, resolution.y/(float)ppu);
			standardProjection = new Matrix4f().ortho(-renderDistance.x, renderDistance.x, -renderDistance.y, renderDistance.y, zNear, zFar);
			wasResized = false;
		}
        if(Game.getGame().getMenue() == Game.M_INGAME){
            renderInGame();            
            renderHUD();
        }
        else if(Game.getGame().getMenue() == Game.M_ESC){
        	renderInGame();
        	renderHUD();
        	renderEscapeMenue();
        }
        else if(Game.getGame().getMenue() == Game.M_INVENTORY){
        	renderInGame();
        	renderInventory();
        	renderCursor();
        }
        
    }

	private void renderInGame()
    {
		
		shaderProgram.bind();
		GameObject focused = Game.getGame().getMechanicManager().getWorldManager().getPlayer();
		Vec2 cameraPosition = focused.getPosition();
		ArrayList<GameObject> GameObjects = Game.getGame().getMechanicManager().getWorldManager().getGameObjects(cameraPosition , new Vec2(20, 15));
		Game.requestLock();
		for(GameObject o : GameObjects)
			o.lockRenderPosition();
		Game.releaseLock();
		cameraCenter = new Vector2f(focused.getPosition().x, focused.getPosition().y);
		shaderProgram.setUniform("projectionMatrix", new Matrix4f().ortho(cameraCenter.x-renderDistance.x, cameraCenter.x+renderDistance.x, cameraCenter.y-renderDistance.y, cameraCenter.y+renderDistance.y, zNear, zFar));
		for(GameObject o : GameObjects){
			VertexArrayObject vao = VAOs.get(o.getVAOType());
			Matrix4f matrix;
			matrix = getTransformationMatrix(new Vector2f(o.getRenderPosition().x, o.getRenderPosition().y), o.getRenderAngle(), o.getRenderSize().x, o.getRenderSize().y);
			Texture[] t = o.getTextures();
			renderIngameObject(vao, t, matrix);
		}
        shaderProgram.unbind();
    }
	
	private void renderInventory()
	{
		Inventory inventory = Game.getGame().getMechanicManager().getWorldManager().getPlayer().getInventory();
		ArrayList<InventorySlot> slots = inventory.getAllSlots();

		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", standardProjection);

		Matrix4f matrix = getTransformationMatrix(new Vector2f(0, 0), 0, inventory.getInventoryRenderSize());
		TextureObject to = TOs.get(Texture.INVENTORY);
		VertexArrayObject vao = VAOs.get(VAOType.INVENTORY);
		renderRectObject(vao, to, matrix);

		vao = VAOs.get(VAOType.UNITSQUARE);

		for(InventorySlot invSlot: slots){
			matrix = getTransformationMatrix(new Vector2f(invSlot.getCoordinates().x, invSlot.getCoordinates().y), 0, invSlot.getSize().x*2);
			Texture[] t = invSlot.getTexture();
			renderIngameObject(vao, t, matrix);

			Item i = invSlot.getItem();
			if(i == null) continue;
			Vector4f itemTrans = new Vector4f(new Vector2f(invSlot.getCoordinates().x, invSlot.getCoordinates().y), 0, invSlot.getSize().x*2*0.75F);
			matrix = getTransformationMatrix(itemTrans);
			t = invSlot.getItem().getTextures();
			renderIngameObject(vao, t, matrix);
			renderString(getItemStackCounterTrans(itemTrans), i.getStackInformation());
		}

//		InventorySlot cursor = inventory.getCursor();
//		if(cursor != null) {
//			matrix = getTransformationMatrix(new Vector2f(cursor.getCoordinates().x, cursor.getCoordinates().y), 0, cursor.getSize().x * 2);
//			to = TOs.get(Texture.INVENTORY_CURSOR);
//			renderRectObject(vao, to, matrix);
//		}
		
		Item mouseItem = inventory.getHeldInMouse();
		if(mouseItem != null){
			Vector4f itemTrans = new Vector4f(mousePosition, 0, 1.5F);
			matrix = getTransformationMatrix(itemTrans);
			Texture[] t = mouseItem.getTextures();
			renderIngameObject(vao, t, matrix);
			renderString(getItemStackCounterTrans(itemTrans), mouseItem.getStackInformation());
		}
        shaderProgram.unbind();
	}
	
	private void renderString(Vector4f position, String text)
	{
		char[] cText = text.toCharArray();
		for(char c: cText){
			Matrix4f matrix = getTransformationMatrix(position);
			VertexArrayObject vao = VAOs.get(VAOType.CHAR);
			TextureObject to = fontChars.get(c);
			renderRectObject(vao, to, matrix);
			position.add(position.w*VAOType.CHAR.getSize().x, 0, 0, 0);
		}
	}
	
	private Vector4f getItemStackCounterTrans(Vector4f itemPos)
	{
		Vector4f slotTrans = new Vector4f(itemPos);
		float f = slotTrans.w/3F;
		slotTrans.add(-f, -f, 0, 0);
		slotTrans.w = slotTrans.w/3F;
		return slotTrans;
	}
	
	
	
	private void renderHUD() 
	{
		renderHealthbar();
		renderCursor();
		renderHotbar();
		
	}
	
    private void renderHotbar()
    {
    	shaderProgram.bind();
    	shaderProgram.setUniform("projectionMatrix", standardProjection);

		VertexArrayObject vao = VAOs.get(VAOType.UNITSQUARE);
		TextureObject to = TOs.get(Texture.INVENTORY_SLOT);
		ArrayList<InventorySlot> hotbar = Game.getGame().getMechanicManager().getWorldManager().getPlayer().getInventory().getHotbar();
		int index = 1;
		for(InventorySlot slot: hotbar){
			Item i = slot.getItem();
			if(i == null) continue;
			Matrix4f matrix = getTransformationMatrix(new Vector2f(-renderDistance.x+hudSize*index, -renderDistance.y+hudSize), 0, hudSize);
			renderRectObject(vao, to, matrix);

			Vector4f itemTrans = new Vector4f(new Vector2f(-renderDistance.x+hudSize*index, -renderDistance.y+hudSize), 0, hudSize*0.75F);
			Texture[] t = i.getTextures();
			renderIngameObject(vao, t, getTransformationMatrix(itemTrans));
			renderString(getItemStackCounterTrans(itemTrans), i.getStackInformation());
			index++;
		}
    	shaderProgram.unbind();
    }
	
	private void renderCursor()
	{
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", standardProjection);
		Matrix4f matrix = getTransformationMatrix(mousePosition, 0, 2*hudSize);
		TextureObject to = TOs.get(Texture.CURSOR);
		VertexArrayObject vao = VAOs.get(VAOType.CURSOR);
		renderRectObject(vao, to, matrix);
        shaderProgram.unbind();
	}
	
	private void renderHealthbar()
	{
		GameObject focused = Game.getGame().getMechanicManager().getWorldManager().getPlayer();
		LivingEntity le;
		//test whether there is health to be displayed
		if(focused instanceof LivingEntity) le = (LivingEntity) focused;
		else return;
		float health = le.getPercentHealth();
		float missingHealth = 1F - health;

		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", standardProjection);

		Matrix4f matrix = getTransformationMatrix(new Vector2f(-renderDistance.x+5*hudSize, renderDistance.y-hudSize), 0, hudSize);
		TextureObject to = TOs.get(Texture.HEALTHBAR);
		VertexArrayObject vao = VAOs.get(VAOType.HEALTHBAR);
		renderRectObject(vao, to, matrix);

		matrix = getTransformationMatrix(new Vector2f(-renderDistance.x+5*hudSize-missingHealth*4*hudSize, renderDistance.y-hudSize), 0, health*hudSize, hudSize);
		to = TOs.get(Texture.LIVEBAR);
		vao = VAOs.get(VAOType.LIVEBAR);
		renderRectObject(vao, to, matrix);
		shaderProgram.unbind();
	}
	
	private void renderEscapeMenue() 
	{
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", new Matrix4f().ortho(-1f, 1f, -1f, 1f, zNear, zFar));
		Matrix4f matrix = getTransformationMatrix(new Vector2f(0, 0), 0, 1f);
		VertexArrayObject vao = VAOs.get(VAOType.FULLSCREEN);
		TextureObject to = TOs.get(Texture.ESCAPE_MENUE);
		renderRectObject(vao, to, matrix);
        shaderProgram.unbind();
	}
    
    private Matrix4f getTransformationMatrix(Vector2f offset, float rotation, float scale)
    {
    	Matrix4f transformationMatrix = new Matrix4f();
    	transformationMatrix.identity().translate(new Vector3f(offset.x, offset.y, 0)).rotateZ(rotation).scale(scale, scale, 0);
        return transformationMatrix;
    }
    
    private Matrix4f getTransformationMatrix(Vector2f offset, float rotation, float scaleX, float scaleY)
    {
    	Matrix4f transformationMatrix = new Matrix4f();
    	transformationMatrix.identity().translate(new Vector3f(offset.x, offset.y, 0)).rotateZ(rotation).scale(scaleX, scaleY, 0);
        return transformationMatrix;
    }
    
    private Matrix4f getTransformationMatrix(Vector4f matrix)
    {
    	Matrix4f transformationMatrix = new Matrix4f();
    	transformationMatrix.identity().translate(new Vector3f(matrix.x, matrix.y, 0)).rotateZ(matrix.z).scale(matrix.w);
        return transformationMatrix;
    }
    
    private void setUpShaders()
    {
    	try{
    		shaderProgram = new ShaderProgram();
    		shaderProgram.createVertexShader(Tools.loadResource("/vertex.vs"));
    		shaderProgram.createFragmentShader(Tools.loadResource("/fragment.fs"));
    		shaderProgram.link();
    		shaderProgram.createUniform("projectionMatrix");
    		shaderProgram.createUniform("transformationMatrix");
    		shaderProgram.createUniform("texture_sampler");
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		System.out.println("Setting up the shaders failed");
    	}
	}
    
    private void createTOs()
    {
    	TOs = new HashMap<>();
    	for(Texture t: Texture.values()){
    		TextureObject to = new TextureObject(t.getTextureName());
    		TOs.put(t, to);
    	}
    }
    
    private void createFontChars()
    {
    	fontChars = new HashMap<>();
    	String charString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    	char[] chars = charString.toCharArray();
    	for(char c: chars){
    		TextureObject to = new TextureObject("Fonts/NormalFont/"+ c);
    		fontChars.put(c, to);
    	}
    	fontChars.put('/', new TextureObject("Fonts/NormalFont/slash"));
    	fontChars.put(' ', new TextureObject("Fonts/NormalFont/space"));
    }
    
    private void cleanupTOs()
    {
    	for(Texture t: Texture.values()){
    		TOs.get(t).cleanup();
    	}
    }
    
    private void cleanupVAOs()
    {
        for(VAOType vt: VAOType.values()){
        	VAOs.get(vt).cleanUp();
        	
        }  
    }
    
    private void renderRectObject(VertexArrayObject vao, TextureObject to, Matrix4f transMatrix)
    {
    	shaderProgram.setUniform("transformationMatrix", transMatrix);
    	shaderProgram.setUniform("texture_sampler", 0);
    	GL30.glBindVertexArray(vao.getVaoID());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, to.getTextureID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
    
    private void renderIngameObject(VertexArrayObject vao, Texture[] textures, Matrix4f transMatrix)
    {
    	for(Texture t : textures)
    		if(t != null)
    			renderRectObject(vao, TOs.get(t), transMatrix);
    }
    
    private void createVAOs()
    {
    	VAOs  = new HashMap<>();
    	for(VAOType vt: VAOType.values()){
    		VertexArrayObject vao = new VertexArrayObject(
    				vt.getCoords(),
        			Tools.createRectTextureVBO(),
        			Tools.createRectIndicesVBO());
    		VAOs.put(vt, vao);
    	}
    }
    

	public void setInput(KeyboardInput ki) 
	{
		GLFW.glfwSetKeyCallback(window, ki);
	}
	
	private void setUpGLFW()
    {
    	//setting up GLFW
    	System.out.println("Setting up GLFW...");
    	errorCallback = GLFWErrorCallback.createPrint(System.err);
    	GLFW.glfwSetErrorCallback(errorCallback);
    	if (!GLFW.glfwInit()) {
    		System.out.println("Unable to initialize GLFW");
    	    throw new IllegalStateException("Unable to initialize GLFW");
    	}
    	GLFW.glfwDefaultWindowHints();
    	GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
    	GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
    	GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
    	
    	
    	window = GLFW.glfwCreateWindow(resolution.x, resolution.y, "Placeholder Game Studios - Placeholder", MemoryUtil.NULL, MemoryUtil.NULL);
    	if (window == MemoryUtil.NULL) {
    		System.out.println("Failed to create the GLFW window");
    		GLFW.glfwTerminate();
    	    throw new RuntimeException("Failed to create the GLFW window");
    	}
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);

    	GLFW.glfwSetWindowFocusCallback(window, new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focus) {
            	if(!focus){
            		Game.getGame().setMenue(Game.M_ESC);
            	}
            }           
    	});

    	GLFW.glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                resolution.x = width;
                resolution.y = height;
                wasResized = true;
            }           
    	});

    	GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
        	public void invoke(long window, double xpos, double ypos) 
        	{
        		mousePosition.set(2*(float)xpos/ppu-renderDistance.x, -(2*(float)ypos/ppu-renderDistance.y));
        	}          
    	});

    	GLFW.glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				KeyboardInput.invokeMouse(button, action, mods);
			} 
    	});
    	
    	GLFW.glfwMakeContextCurrent(window);
    	GLFW.glfwSwapInterval(1);
    	GLFW.glfwShowWindow(window);
    	System.out.println("Done");
    	GL.createCapabilities();
    	GL11.glViewport(0, 0, resolution.x, resolution.y);
    	
    	//setting up openGL
        GL11.glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthRange(zNear, zFar);
    }
	
	public static Vec2 getMousePos()
	{
		return new Vec2(mousePosition.x, mousePosition.y);
	}
	
    
    private void terminate()
    {
    	cleanupVAOs();
    	cleanupTOs();
    	shaderProgram.cleanup();
    	GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    	GLFW.glfwDestroyWindow(window);
    	Callbacks.glfwFreeCallbacks(window);
    	GLFW.glfwTerminate();
    	errorCallback.free();
    	Game.getGame().getKeyboardInput().free();
    	
    	System.exit(0);
    }
    
	
}
