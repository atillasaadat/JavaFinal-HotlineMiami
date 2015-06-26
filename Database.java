public interface Database{
	public final int SCREENRESX = 1280;
	public final int SCREENRESY = 720;
	
	public final int PORT = 9012;
	//change IPADRESS = "localhost" to change it to local hosting
	//public final String IPADDRESS = "localhost";
	public final String IPADDRESS = "104.131.81.144";
	
	public int spriteSpeed = 5;//speed of cycles, more is slower
	public int playerSpeed = 4;//speed of player, more is slower
	//spawn point1
	public int initialPlayerPosX = 495;
	public int initialPlayerPosY = 495;
	//spawn point2
	public int initialPlayer2PosX = 848;
	public int initialPlayer2PosY = 206;
	//frame at which to deal attack damage
	public int attackFrame = 5;
	//when to end death sprite cycle
	public int lastDeadFrame = 7;
	//used for idle picture
	public int moveFrame = 0;
	public String fontDirectory = "friday13.ttf";
	public int yFontOffset = 30;
	//time before game is reset after death
	public int deathCamTime = 300;
	public String songName = "sound.wav";
}