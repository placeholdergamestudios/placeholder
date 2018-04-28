package org.md2.common;

public enum Texture {
	STONE("Wall"), STONE_SIDE("WallSide"), STONE_CORNER("WallCorner"), STONE_INVCORNER("WallInvCorner"),
	APPLE("Apple"), HERB("Kraut"), GLUE("Glue"), BOOMERANG("Boomerang"), SHADOW("shadow"),
	DUMMY("Dummy"), SHORTSWORD("shortsword"), WOODENBOW("holzbogen"), WOODENBOW1("holzbogen1"), WOODENBOW2("holzbogen2"),
	PLAYERFRONT("PlayerFront"), PLAYERBACK("PlayerBack"), PLAYERLEFT("PlayerLeft"), PLAYERRIGHT("PlayerRight"),
	PLAYERFRONTW1("PlayerFrontW1"), PLAYERFRONTW2("PlayerFrontW2"),
	PLAYERBACKW1("PlayerBackW1"), PLAYERBACKW2("PlayerBackW2"),
	PLAYERRIGHTW1("PlayerRightW1"), PLAYERRIGHTW2("PlayerRightW2"), PLAYERRIGHTW3("PlayerRightW3"), PLAYERRIGHTW4("PlayerRightW4"),
	PLAYERLEFTW1("PlayerLeftW1"), PLAYERLEFTW2("PlayerLeftW2"), PLAYERLEFTW3("PlayerLeftW3"), PLAYERLEFTW4("PlayerLeftW4"),
	NORMALSOIL("NormalSoil"), HEALTHBAR("Healthbar"), LIVEBAR("LiveBar"), 
	ESCAPE_MENUE("ESC"), INVENTORY("InvBackground"), INVENTORY_SLOT("InvSlot"), INVENTORY_CURSOR("InvCursor"), CURSOR("Cursor"),
	ARROW("Pfeil"), BRONZECAP("BronzeCap"), CRYSTALORB("CrystalOrb"), BRONZEHANDLE("BronzeHandle"), FIREBALL("Fireball");
	
	private final String textureName;
	
	private Texture(String textureName)
	{
		this.textureName = textureName;
	}
	
	public String getTextureName()
	{
		return this.textureName;
	}
}
