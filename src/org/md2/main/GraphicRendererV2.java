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
import org.md2.common.ShaderProgram;
import org.md2.common.Texture;
import org.md2.common.TextureObject;
import org.md2.common.Tools;
import org.md2.common.VAOType;
import org.md2.common.VertexArrayObject;
import org.md2.gameobjects.GameObject;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.Item;
import org.md2.input.KeyboardInput;
import org.md2.worldmanagement.Inventory;


public class GraphicRendererV2 extends Thread
{
    
    private static int fps = 30; // Frames per second
    public static int tpf = 1000/fps; //Time per Frame in ms
    
    private static final int zNear = +10;
    private static final int zFar = -10;
    
    private static Vector2i resolution;
    private boolean wasResized;
    
    private GLFWErrorCallback errorCallback;
    
    private static Vector2f mousePosition;
    
    private long window;
    private ShaderProgram shaderProgram;
    
    private Vector2f cameraCenter;
    private int ppu;
    
    private HashMap<Character, TextureObject> fontChars;
    private HashMap<VAOType, VertexArrayObject> VAOs;
    private HashMap<Texture, TextureObject> TOs;
    private Vector2f renderDistance;
    
    public GraphicRendererV2()
    {
    	
    	resolution = new Vector2i(1200, 900);
    	ppu = (int)(resolution.x/10F);
    	renderDistance = new Vector2f(resolution.x/(float)ppu, resolution.y/(float)ppu);
    	mousePosition = new Vector2f();
    	System.out.println("Using LWJGL " + Version.getVersion() + "!");
    	setUpGLFW();
    	setUpShaders();
    	System.out.println("Using OpenGL version " + GL11.glGetString(GL11.GL_VERSION));
    	createVAOs();
    	createTOs();
    	createFontChars();
        //glfwSetCursor(window, GLFW.glfwCreateCursor(0, 0, 0));
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
        }
        
    }

	private void renderInGame()
    {
		
		shaderProgram.bind();
		GameObject focused = Game.getGame().getMechanicManager().getWorldManager().getPlayer();
		Vec2 cameraPosition = focused.getPosition();
		ArrayList<GameObject> GameObjects = Game.getGame().getMechanicManager().getWorldManager().getGameObjects(cameraPosition.x, cameraPosition.y, 20, 15);
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
		Item[][] container = inventory.getContainer();
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", new Matrix4f().ortho(-renderDistance.x, renderDistance.x, -renderDistance.y, renderDistance.y, zNear, zFar));
		Matrix4f matrix = getTransformationMatrix(new Vector2f(0, 0), 0, inventory.getInventoryRenderSize());
		TextureObject to = TOs.get(Texture.INVENTORY);
		VertexArrayObject vao = VAOs.get(VAOType.INVENTORY);
		renderRectObject(vao, to, matrix);
		for(int x = 0; x < container.length; x++){
			for(int y = 0; y < container[1].length; y++){
				if(!inventory.isValidPosition(x, y))
					continue;
				Item o = container[x][y];
				matrix = getTransformationMatrix(inventory.getInventorySlotTransformation(x, y));
				vao = VAOs.get(VAOType.INVENTORY_SLOT);
				to = TOs.get(Texture.INVENTORY_SLOT);
				renderRectObject(vao, to, matrix);
				if(o == null)
					continue;
				vao = VAOs.get(VAOType.INVENTORY_ITEM);
				Texture[] t = o.getTextures();
				renderIngameObject(vao, t, matrix);
				renderString(getItemStackCounterTrans(inventory.getInventorySlotTransformation(x, y)), o.getStackInformation());
			}
		}
		matrix = getTransformationMatrix(inventory.getCursorTransformation());
		vao = VAOs.get(VAOType.INVENTORY_CURSOR);
		to = TOs.get(Texture.INVENTORY_CURSOR);
		renderRectObject(vao, to, matrix);
		
		Item mouseItem = inventory.getMousePickUp();
		if(mouseItem != null){
			Vector4f itemTrans = new Vector4f(mousePosition, 0, 1.5F);
			matrix = getTransformationMatrix(itemTrans);
			vao = VAOs.get(VAOType.INVENTORY_ITEM);
			Texture[] t = mouseItem.getTextures();
			renderIngameObject(vao, t, matrix);
			renderString(getItemStackCounterTrans(itemTrans), mouseItem.getStackInformation());
		}
        shaderProgram.unbind();
	}
	
	private void renderString(Vector4f position, String text)
	{
		if(KeyboardInput.isPushed(KeyboardInput.ACTION_ENTER))
			System.out.println(position.x+" "+position.y+" "+position.z+" "+position.w+" ");
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
		//renderCursor();
		renderHotbar();
		
	}
	
	private void renderHotbar()
	{
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", new Matrix4f().ortho(-renderDistance.x, renderDistance.x, -renderDistance.y, renderDistance.y, zNear, zFar));
		
		
		Item[] hotbar = Game.getGame().getMechanicManager().getWorldManager().getPlayer().getInventory().getHotbar();
		for(int index = 0; index < hotbar.length; index++){
			WorldObject o = hotbar[index];
			if(o != null){
				Matrix4f matrix = getTransformationMatrix(new Vector2f(-renderDistance.x+1+index*2, -renderDistance.y+1), 0, 1f);
				TextureObject to = TOs.get(Texture.INVENTORY_SLOT);
				VertexArrayObject vao = VAOs.get(VAOType.INVENTORY_SLOT);
				renderRectObject(vao, to, matrix);
				vao = VAOs.get(VAOType.INVENTORY_ITEM);
				Texture[] t = o.getTextures();
				renderIngameObject(vao, t, matrix);
			}
		}
        shaderProgram.unbind();
	}
	
	private void renderCursor()
	{
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", new Matrix4f().ortho(-renderDistance.x, renderDistance.x, -renderDistance.y, renderDistance.y, zNear, zFar));
		Matrix4f matrix = getTransformationMatrix(mousePosition, 0, 1);
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
		if(focused instanceof LivingEntity){
			le = (LivingEntity) focused;
		}
		else return;
		float health = le.getPercentHealth();
		float missingHealth = 1 - health;
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", new Matrix4f().ortho(-1, 1, -1, 1, zNear, zFar));
		//renders the healthbar in the upper right corner of the window
		Matrix4f matrix = getTransformationMatrix(new Vector2f(-0.5f, 0.9f), 0, 1);
		TextureObject to = TOs.get(Texture.HEALTHBAR);
		VertexArrayObject vao = VAOs.get(VAOType.HEALTHBAR);
		renderRectObject(vao, to, matrix);
		//renders the health of the player in that healthbar
		matrix = getTransformationMatrix(new Vector2f(-0.5f-missingHealth/2+missingHealth/10, 0.9f), 0, health, 1);
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
    	TOs = new HashMap<Texture, TextureObject>();
    	for(Texture t: Texture.values()){
    		TextureObject to = new TextureObject(t.getTextureName());
    		TOs.put(t, to);
    	}
    }
    
    private void createFontChars()
    {
    	fontChars = new HashMap<Character, TextureObject>();
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
    
    public void renderIngameObject(VertexArrayObject vao, Texture[] textures, Matrix4f transMatrix)
    {
    	for(Texture t : textures)
    		if(t != null)
    			renderRectObject(vao, TOs.get(t), transMatrix);
    }
    
    public void createVAOs()
    {
    	VAOs  = new HashMap<VAOType, VertexArrayObject>();
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
    	
    	
    	window = GLFW.glfwCreateWindow(resolution.x, resolution.y, "_Amrius_OPENGL_UWotM8", MemoryUtil.NULL, MemoryUtil.NULL);
    	if (window == MemoryUtil.NULL) {
    		System.out.println("Failed to create the GLFW window");
    		GLFW.glfwTerminate();
    	    throw new RuntimeException("Failed to create the GLFW window");
    	}
    	
    	@SuppressWarnings("unused")
		GLFWWindowFocusCallback windowFocusCallback;
    	GLFW.glfwSetWindowFocusCallback(window, windowFocusCallback = new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focus) {
            	if(!focus){
            		Game.getGame().setMenue(Game.M_ESC);
            	}
            }           
    	});
    	
		@SuppressWarnings("unused")
		GLFWWindowSizeCallback windowSizeCallback;
    	GLFW.glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                resolution.x = width;
                resolution.y = height;
                wasResized = true;
            }           
    	});
    	
    	@SuppressWarnings("unused")
    	GLFWCursorPosCallback cursorPosCallback;
    	GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
        	public void invoke(long window, double xpos, double ypos) 
        	{
        		mousePosition.set(2*(float)xpos/ppu-renderDistance.x, -(2*(float)ypos/ppu-renderDistance.y));
        	}          
    	});
    	
    	@SuppressWarnings("unused")
    	GLFWMouseButtonCallback mouseButtonCallback;
    	GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {

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
        //GL11.glEnable(GL11.GL_DEPTH_TEST);
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
    	GLFW.glfwDestroyWindow(window);
    	Callbacks.glfwFreeCallbacks(window);
    	GLFW.glfwTerminate();
    	errorCallback.free();
    	Game.getGame().getKeyboardInput().free();
    	
    	System.exit(0);
    }
    
	
}
