//Atilla Saadat 
//Hotline Miami
//June 12, 2015
//This game features 2-player 1v1 multiplayer hotline miami hosted either locally or on my Java Web Application 
// hosted at the following address: atillasaadat.me:8080/Miami
//Enter the port number you want (a number between 7000-9000 preferred) 
//then change the PORT number in the Database interface for all players
/*
 *Apart from the multiplayer, this game features a great collision system involving the intersection of the 
 *areas of polygons. It also features polygon rotations for enhanced hit detection. It is programmed in such a way to
 *allow for plug and play methods and supports sunctionality for 2> multiplayer, easy AI interation, and much more.
 */
 
 /*HOW TO PLAY ONLINE WITH WEB HOSTING!!!!
  *
  *1. open Database.java and change PORT to any integer between 7000-9000 preferrable
  *
  *2. visit atillasaadat.me:8080/Miami  , enter the port number you typed above, 
  *and click Start Hotline Miami Server ONLY ONCE!!!!!
  *
  *3. open 2 operations of HotlineMiami.java and enjoy
  *
  *There might be a chance it will not connect, if so, then follow the steps below to localhost
  *
  *This worked at my home but not at school. It worries me if it is the schools network or my code :P
  *
  *
  *Disclaimer: The online server honestly worked when i tried a LAN connection to the web server, 
  *as well as 2 seperate computers connect to the web server
  *
  *I have also included a Web Application Archive file i deployed to Apache Tomcat, hence the port 8080
  *
  *If the server online gives an message similar to "Server did not work", reload atillasaadat.me:8080/Miami
  *and pick a new port for Database.java and the Web server
  *
  *TIP: deleting all the classes sometimes help solve some problems connecting, compiler issues, and other issues 
  *
  */
 
 /*HOW TO SWITCH BETWEEN LOCALHOST AND WEB HOSTING!!!
  *
  *1. go to Database.java and un-comment the line with "localhost"
  *and comment the line with an actual IP address
  *
  *2. open and run Server.java
  *
  *3. open 2 operations of HotlineMiami.java and enjoy
  */


import java.util.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
import java.awt.geom.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.MouseInfo;
import javax.swing.Timer;
import java.applet.*;
import javax.sound.sampled.AudioSystem;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;



public class HotlineMiami extends JFrame implements ActionListener,Database{
	Timer myTimer;   
	GamePanel game;
	MainScreen ms;
	AudioClip back;

	//main contructor for creating panels for game, menu, and buttons
    public HotlineMiami() {
		super("Hotline Miami");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ms= new MainScreen(SCREENRESX,SCREENRESY);//menuscreen
		ms.setSize(SCREENRESX,SCREENRESY);
		ms.setVisible(true);
		this.setIconImage(new ImageIcon("icon.png").getImage());
		add(ms);

		myTimer = new Timer(10, this);	 // trigger every 10 ms
		

		game = new GamePanel(this);//gamescreen
		game.setSize(SCREENRESX,SCREENRESY);//set screen resolution
		game.setVisible(false);
		add(game);
		setVisible(true);
		setSize(SCREENRESX,SCREENRESY);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setResizable(false);
		
		back = Applet.newAudioClip(getClass().getResource("moon.wav"));//music
		back.loop();
		
    }
    //timer
	public void start(){
		myTimer.start();
	}

	//main menu, buttons values for changing menus
	public void actionPerformed(ActionEvent evt){
		if(ms.getName().equals("quit")){//quits if quit is chosen
			System.exit(0);
		}
		if (ms.getName().equals("main")){//sets mainscreen
			game.setVisible(false);
			ms.setVisible(true);
			ms.requestFocus();
			ms.repaint();
		}else{
			if(game.getLose()){//if game is over, change back to mainscreen
				game.reset();
				ms.goToMain();
				game.changeLose();
			}
			ms.setVisible(false);//set gamescreen
			game.setVisible(true);
			game.requestFocus();
			game.movement();
			game.repaint();
			
		}

	}
	//main calling the main hotline miami frame
    public static void main(String[] arguments) {
		HotlineMiami frame = new HotlineMiami();		
    }
}

class MainScreen extends JPanel implements MouseListener, MouseMotionListener,Database{
	//Menu Screen
	int mx,my;
	boolean click = false;
	private boolean []keys;
	
	Image mainMenu = new ImageIcon("mainmenu.jpg").getImage();
	String screen = "main";
	ButtonClass playButton,quitButton;
	
	//constructor with parameters of the screen resolution
	public MainScreen(int SCREENRESX,int SCREENRESY){
		super();
		keys = new boolean[KeyEvent.KEY_LAST+1];
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		SCREENRESX = SCREENRESX;
		SCREENRESY = SCREENRESY;
		playButton = new ButtonClass((int)(SCREENRESX*7/16),(int)(SCREENRESY*77/130),"play");
		quitButton = new ButtonClass ((int)(SCREENRESX*7/16),(int)(SCREENRESY*7/10),"quit");
		
	}
	//get string of what the current screen is
	public String getName(){//screen state
		return screen;
	}
	//go to main screen
	public void goToMain(){
		screen = "main";
	}
	//main menu screen
	public void paintComponent(Graphics g){
		if (screen.equals("main")){//prints screen + buttons
		//	g.drawImage(background,0,0,this);
			if(keys[KeyEvent.VK_ESCAPE]){
    			System.exit(0);
    		}
    		//draws main menu and buttons
			g.drawImage(mainMenu,0,0,this);
			g.drawImage(playButton.getImage(mx,my,click),playButton.getX(),playButton.getY(),this);
			g.drawImage(quitButton.getImage(mx,my,click),quitButton.getX(),quitButton.getY(),this);
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseReleased(MouseEvent e){click=false;}
	//
	public void mousePressed(MouseEvent e){
		click=true;
		//changes values when clicked
		if (playButton.getRect().contains(mx,my) && screen.equals("main")){
			screen ="play";
		}
		else if (quitButton.getRect().contains(mx,my)&& screen.equals("main")){
			screen = "quit";//changes value and quits
			System.exit(0);
		} 
	}
	public void mouseExited (MouseEvent e){}
	public void mouseClicked (MouseEvent e){}
	//mouse movement
	public void mouseDragged(MouseEvent e){
		mx = e.getX();
		my = e.getY();
	}
	//mouse movement
	public void mouseMoved(MouseEvent e){
		mx =e.getX();
		my= e.getY();
	}
	
}

class ButtonClass{//class for changing button images
	private Image pic1,pic2,pic3;
	private int x,y;
	//parameters x,y pos and name of picture
	public ButtonClass(int x, int y,String name){
		this.x=x;
		this.y=y;
		//3 button states
		pic1 = new ImageIcon(name+"Normal.png").getImage();
		pic2 = new ImageIcon(name+"Hover.png").getImage();
		pic3 = new ImageIcon(name+"Hover.png").getImage();
		
	}
	//get what kind of picture will be shown when hovered, not hovered, and clicked
	public Image getImage (int mx, int my, boolean click){//gets the images of the buttons in 3 different states
		Rectangle rect = new Rectangle(x,y,pic1.getWidth(null),pic1.getHeight(null));
		if (rect.contains(mx,my) && click){
			return pic3;
		}
		else if (rect.contains(mx,my)){
			return pic2;
		}
		else{
			return pic1;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	//get rectangle of picture
	public Rectangle getRect(){
		return new Rectangle(x,y,pic1.getWidth(null),pic1.getHeight(null));
	}
}

class Sprite implements Database{
	//class for handling sprite image iterations
	private ArrayList<BufferedImage> picList = new ArrayList<BufferedImage>();//list containig all the sprite images of one action
	private int imageCounter = 0;
	private int finalImage = 0;
	private int counterSpeed;
	int mx,my;
	//constructor, sprite name
	public Sprite(String picName){
		try{
			int count = 0;
			//aprite arraylist based on name of sprite
			while((new File(picName+"\\"+picName+count+".png")) != null){
				picList.add(ImageIO.read(new File(picName+"//"+picName+count+".png")));
				count++;
				//load all images in file before it exceeds what is available
			}
		}
		catch(NullPointerException e){}
		catch(IOException e){}
		
		counterSpeed = spriteSpeed;//speed of which the sprites run
		
	}
	//get the image at the frame number in the sprite cycle
	public BufferedImage frameAt(int x){
		return picList.get(x);
	}

	//rotate an image , passed an image, the position, and the mx,my which calculate the radians it is rotated
	public BufferedImage rotatedImage(BufferedImage image, int [] pos, int mx, int my){
		//rotate image to a certain degree to face towards the mouse
		BufferedImage newImage;
		AffineTransform at = new AffineTransform();
		// find the angle
		double angleInRad = Math.atan2((double)(my-pos[1]),(double)(mx-pos[0]));//angle between center of image and mouse
		//
				
		at.rotate(angleInRad, image.getWidth()/2, image.getHeight()/2);//rotate by x amount of radians among the middle of the image
    	AffineTransformOp op = new AffineTransformOp(at,
        	AffineTransformOp.TYPE_BILINEAR);
    	
    	return op.filter(image, null);
	}
	//original polygon from the attack pic dimensions
	private Polygon returnPolyFromImageAttack(BufferedImage temp,int [] pos){
		//return polygon of area where the attack will be effected, half of front of hitBox
		int [] xpoints = new int []{(int)(pos[0]+temp.getWidth()/2)
			,(int)(pos[0]+(temp.getWidth()))
			,(int)(pos[0]+(temp.getWidth()))
			,(int)(pos[0]+temp.getWidth()/2)};
		int [] ypoints = new int []{(int)(pos[1])
			,(int)(pos[1])
			,(int)(pos[1]+temp.getHeight())
			,(int)(pos[1]+temp.getHeight())};
		return new Polygon(xpoints,ypoints,4);
	}
	//original polygon from the hit detection pic dimensions
	private Polygon returnPolyFromImageHit(BufferedImage temp,int [] pos){
		//return polygon of area that can be vulnerble to attacks from enemy
		int [] xpoints = new int []{(int)(pos[0])
			,(int)(pos[0]+(temp.getWidth()))
			,(int)(pos[0]+(temp.getWidth()))
			,(int)(pos[0])};
		int [] ypoints = new int []{(int)(pos[1])
			,(int)(pos[1])
			,(int)(pos[1]+temp.getHeight())
			,(int)(pos[1]+temp.getHeight())};
		return new Polygon(xpoints,ypoints,4);
	}
	//polygon defining the collision area of player
	private Polygon returnCollision(BufferedImage temp,int [] pos){
		//returns polygon area which will be affected by wall collisions
		int [] xpoints = new int []{(int)(pos[0]+temp.getWidth()/4)
			,(int)(pos[0]+3*(temp.getWidth()/4))
			,(int)(pos[0]+3*(temp.getWidth()/4))
			,(int)(pos[0]+temp.getWidth()/4)};
		int [] ypoints = new int []{(int)(pos[1]+temp.getHeight()/4)
			,(int)(pos[1]+temp.getHeight()/4)
			,(int)(pos[1]+3*temp.getHeight()/4)
			,(int)(pos[1]+3*temp.getHeight()/4)};
		return new Polygon(xpoints,ypoints,4);
	}
	//rotates all points of a polygon along the center, returns bordering polygon
	private Polygon polyHandler(Polygon oldPoly,AffineTransform at){
		Polygon newPoly = new Polygon();
		for(int i = 0; i < oldPoly.npoints;i++){
			Point2D.Double p = new Point2D.Double(oldPoly.xpoints[i],oldPoly.ypoints[i]);
			Point2D p2 = at.transform(p,p);//rotate the points from the centre point based on given radian
			newPoly.addPoint((int)p2.getX(),(int)p2.getY());//add transformed point to new polygon
		}
		return newPoly;//return transformed polygon
	}
	//rotate the original attack polygon with the radians based on mouse position
	public Polygon attackPoly(BufferedImage image,int[]pos,int mx, int my){
		//rotates all points of a polygon along the center
		AffineTransform at = new AffineTransform();
		double angleInRad = Math.atan2((double)(my-pos[1]),(double)(mx-pos[0]));//angle between center of image and mouse
		at.rotate(angleInRad, pos[0] + image.getWidth()/2,pos[1] + image.getHeight()/2);//rotate along center
		return polyHandler(returnPolyFromImageAttack(image,pos),at);//return bordering polygon based on image
	}
	//rotate the original hitbox polygon with the radians based on mouse position
	public Polygon hitPoly(BufferedImage image,int[]pos, int mx, int my){
		//rotates all points of a polygon along the center
		AffineTransform at = new AffineTransform();
		double angleInRad = Math.atan2((double)(my-pos[1]),(double)(mx-pos[0]));//angle between center of image and mouse
		at.rotate(angleInRad, pos[0] + image.getWidth()/2,pos[1] + image.getHeight()/2);//rotate along center
		return polyHandler(returnPolyFromImageHit(image,pos),at);//return bordering polygon based on image
	}
	//rotate the original collision box polygon with the radians based on mouse position
	public Polygon collisionPoly(BufferedImage image,int[]pos, int mx, int my){
		AffineTransform at = new AffineTransform();
		double angleInRad = Math.atan2((double)(my-pos[1]),(double)(mx-pos[0]));//angle between center of image and mouse
		at.rotate(angleInRad, pos[0] + image.getWidth()/2,pos[1] + image.getHeight()/2);//rotate along center
		return polyHandler(returnCollision(image,pos),at);//return bordering polygon based on image
	}
	//draw the rorated image of the defined action (idle,attack,move)
	public void drawPlayer(Graphics g,GamePanel game,int [] pos,int mx, int my){		
		if(imageCounter%counterSpeed==0){//limits the speed at which sprites are cycled
			finalImage ++;
		}
		if(finalImage>picList.size()-1){//reset sprite index
			imageCounter = 0;
			finalImage = 0;
		}
		
		g.drawImage(rotatedImage(picList.get(finalImage),pos,mx,my),pos[0],pos[1],game);//draw sprite
		imageCounter++;
	}
	//sprite cycle specific for death, it only cycles through once
	public void drawDeath(Graphics g,GamePanel game,int [] pos,int mx, int my){
		//this sprite is different than the rest, because it should only go though one cycle
		if(imageCounter%counterSpeed==0){//limit speed of death cycle
			finalImage ++;
		}
		try{
			g.drawImage(rotatedImage(picList.get(finalImage),pos,mx,my),pos[0],pos[1],game);//if picture at index exists, cycle though once
		}catch(IndexOutOfBoundsException e){
			g.drawImage(rotatedImage(picList.get(lastDeadFrame),pos,mx,my),pos[0],pos[1],game);//the counter will exceed the array limit, and thus only ever
			//draw the last frame, resulting in one cycle
		}
		imageCounter++;
	}
}

class Player implements Database{
	//class to handle both players
	private int mx,my;
	private int [] pos;
	
	private int playerBox;
	
	private boolean deathState = false;
	
	public Sprite movePlayer;
	public Sprite swingPlayer;
	public Sprite idlePlayer;
	public Sprite deadPlayer;
	//player constructor, id tells whether it is player 1 (you) or player 2(the other)
	public Player(int playerId){
		pos = new int [2];
		pos[0] = initialPlayerPosX;//initial
		pos[1] = initialPlayerPosY;	
		if(playerId==1){//own player sprites
			movePlayer = new Sprite("batMove");
			swingPlayer = new Sprite("batSwing");
			idlePlayer = new Sprite("batIdle");
			deadPlayer = new Sprite("batDead");
		}else{//other player sprites
			movePlayer = new Sprite("barMove");
			swingPlayer = new Sprite("barSwing");
			idlePlayer = new Sprite("barIdle");
			deadPlayer = new Sprite("barDead");
		}
	}
	//called when the player dies
	public void hasDied(){
		deathState = true;
	}
	//checks if player is dead
	public Boolean isDead(){
		return deathState;
	}
	//get player xpos
	public int getPosX(){
		return pos[0];
	}
	//get playeer ypos
	public int getPosY(){
		return pos[1];
	}
	//set new xpos for player, used for different spawns
	public void setPosX(int newX){
		pos[0] = newX;
	}
	//set new ypos for player, used for different spawns
	public void setPosY(int newY){
		pos[1] = newY;
	}
	//true if user has been hit by the other player
	public boolean isUserHit(int[] player1Pos,int player1Mx,int player1My,int [] player2Pos,int player2Mx,int player2My){
		//check is user has been hit by an enemy attack
		Area userArea = new Area(hitBox(player1Pos,player1Mx,player1My));
		Area enemyArea = new Area(attackBox(player2Pos,player2Mx,player2My));
		userArea.intersect(enemyArea);
		return !userArea.isEmpty();
			
	}
	
	//returns coresspondng polygons
	public Polygon collisionBox(int [] pos, int mx, int my){
		return movePlayer.collisionPoly(movePlayer.frameAt(moveFrame),pos, mx,my);
	}
	//hitbox polygon, passes through spriteclass w/ rotations
	public Polygon hitBox(int [] pos, int mx, int my){
		return movePlayer.hitPoly(movePlayer.frameAt(moveFrame),pos, mx,my);
	}
	//attackbox polygon, passes through spriteclass w/ rotations
	public Polygon attackBox(int [] pos, int mx, int my){
		return swingPlayer.attackPoly(movePlayer.frameAt(attackFrame),pos,mx,my);
	}
	//draw sprite cycles, swingbat
	public void swingBat (Graphics g,GamePanel game,int [] pos,int mxTemp, int myTemp){
		swingPlayer.drawPlayer(g,game,pos,mxTemp,myTemp);
	}
	//draw sprite cycles, move
	public void move (Graphics g,GamePanel game,int [] pos,int mxTemp, int myTemp){
		movePlayer.drawPlayer(g,game,pos,mxTemp,myTemp);
	}
	//draw sprite cycles, idle
	public void idle (Graphics g,GamePanel game,int [] pos,int mxTemp, int myTemp){
		idlePlayer.drawPlayer(g,game,pos,mxTemp,myTemp);
	}
	//draw sprite cycles, death
	public void dead (Graphics g,GamePanel game,int [] pos,int mxTemp, int myTemp){
		deadPlayer.drawDeath(g,game,pos,mxTemp,myTemp);
	}
}


class GamePanel extends JPanel implements KeyListener,MouseListener,MouseMotionListener,Database{
	private int playerX,playerY, player2X, player2Y;
	private HotlineMiami mainFrame;
	private boolean []keys;
	private int mx,my;
	private int speedCounter = 0;
	private boolean lose;
	private BufferedImage tempBack, colourBack, completeBack;
	boolean click;
	private Font font;
	public Player player;
	
	public Player player2;
   	
   	private Client user;
   	private StringHandler stringHandler;
   	private int count;
   	String userName;
   	
   	private boolean changedSpawn;
   	
   	private int gameOverCounter;
	
	//gamepanel constructor, parameter main class
	public GamePanel(HotlineMiami m){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		//grab focus and start listeners when called
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		
		player = new Player(1);
		player2 = new Player(2);

		
		try{
			tempBack = ImageIO.read(new File("background.png"));
			colourBack = ImageIO.read(new File("colourBack.png"));
			completeBack = ImageIO.read(new File("completeBack.png"));
		}catch(IOException e){}
		mainFrame = m;
		
		playerX = player.getPosX();
		playerY = player.getPosY();
		
		player2X = player2.getPosX();
		player2Y = player2.getPosY();

		setSize(SCREENRESX,SCREENRESY);
        addKeyListener(this);
        
        try{
        	user = new Client();	
        }catch(Exception e){}
        
        stringHandler = new StringHandler();
        
        count = 0;
        click = false;
        try{
			InetAddress localAddress = InetAddress.getLocalHost();
			userName = localAddress.getHostName();//get computer host name as username for online
		}
		catch (IOException e){}
		changedSpawn = false;
		loadFont();
		lose = false;
		gameOverCounter = 0;
	}
	//reset game variables for when game is replayed
	void reset(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		player = new Player(1);
		player2 = new Player(2);

		
		try{
			tempBack = ImageIO.read(new File("background.png"));
			colourBack = ImageIO.read(new File("colourBack.png"));
			completeBack = ImageIO.read(new File("completeBack.png"));
		}catch(IOException e){}
		
		
		playerX = player.getPosX();
		playerY = player.getPosY();
		
		player2X = player2.getPosX();
		player2Y = player2.getPosY();

		setSize(SCREENRESX,SCREENRESY);
        addKeyListener(this);
        
        try{
        	user = new Client();	
        }catch(Exception e){}
        
        stringHandler = new StringHandler();
        
        count = 0;
        click = false;
        try{
			InetAddress localAddress = InetAddress.getLocalHost();
			userName = localAddress.getHostName();
		}
		catch (IOException e){}
		changedSpawn = false;
		loadFont();
		lose = false;
		gameOverCounter = 0;
	}
	//load font for text
	public void loadFont(){
    	try{
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(fontDirectory))).deriveFont(0,25);//import font
    	}
    	catch(IOException e){}
    	catch(FontFormatException e){}
    }
	
	
    public void addNotify() {//removes null crash
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    
	
	//movement , contains x and y pos, is spacebar is pressed, and if there is any movement at all
	public int [] movement(){
		int spacePress;
		int isMoving = 0;
		if(speedCounter%playerSpeed==0 ){//speeds limiter
			if(keys[KeyEvent.VK_W]){
				if(isInsideMap(new int []{playerX,playerY-1},mx,my)&&isInsideMap(new int []{playerX,playerY-2},mx,my)){
					//these if's check if the user will collide with a wall/obstacle before they actually move there, removing the need for position tracking
					playerY--;	
				}
				
			}
			if(keys[KeyEvent.VK_S]){
				if(isInsideMap(new int []{playerX,playerY+1},mx,my)&&isInsideMap(new int []{playerX,playerY+2},mx,my)){
					playerY++;
				}
			}
			if(keys[KeyEvent.VK_A]){
				if(isInsideMap(new int []{playerX-1,playerY},mx,my)&&isInsideMap(new int []{playerX-2,playerY},mx,my)){
					playerX--;
				}
			}
			if(keys[KeyEvent.VK_D]){
				if(isInsideMap(new int []{playerX+1,playerY},mx,my)&&isInsideMap(new int []{playerX+2,playerY},mx,my)){
					playerX++;
				}
			}
			
		}
		//is there any movement at all, used for drawing idle
		if(keys[KeyEvent.VK_W]||keys[KeyEvent.VK_S]||keys[KeyEvent.VK_A]||keys[KeyEvent.VK_D]){
			isMoving = 1;
		}
		speedCounter++;
		if(keys[KeyEvent.VK_SPACE]){
			spacePress = 1;
		}else{spacePress = 0;}
		
		int[] posArray = {playerX,playerY,spacePress,isMoving};
		return posArray;
	}
	
	public boolean getLose(){//get if the game is lost
		return lose;
	}
	//called when game is over, used to go back to main menu
	public void gameOver(){
		lose = true;
	}
	//changes if the current game is over or nor, used for reseting variables
	public void changeLose(){//change if the game is lost
		lose = false;
	}

    public void keyTyped(KeyEvent e) {}
	//gets keyboard input
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    public void mouseEntered(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseExited (MouseEvent e){}
	public void mouseClicked (MouseEvent e){}
	//gets mouse movement
	public void mouseDragged(MouseEvent e){
		mx = e.getX();
		my = e.getY();
	}
	public void mouseMoved(MouseEvent e){
		mx = e.getX();
		my = e.getY();
	}
	//gets center x and y of panel for any image
	public int [] centerPicture(BufferedImage image){
		int [] center = new int[2];
		center[0] = (SCREENRESX/2)-(image.getWidth()/2);
		center[1] = (SCREENRESY/2)-(image.getHeight()/2);
		return center;
	}
	//area of polygon defining the entirety of the walls of the map, used for collision
	public Area rooms(){
		int [] roomX,roomY;
		roomX = new int[]{
			347,560,560,570,570,747,747,756,756,858,858,756,756,934,934,896,896,934,934,756,756,747,747,
			756,756,934,934,909,909,756,756,747,747,570,570,560,560,339,339,560,560,570,570,560,560,347,
			347,378,378,347,347,336,336,944,944,748,748,570,570,347
				};
		roomY = new int[]{
			121,121,297,297,233,233,334,334,307,307,298,298,121,121,298,298,307,307,411,411,374,374,484,
			484,419,419,449,449,597,597,523,523,597,597,563,563,605,605,421,421,524,524,336,336,410,410,
			382,382,307,307,111,111,609,609,111,111,224,224,111,111
				};
		return new Area(new Polygon(roomX,roomY,roomX.length));
		
	}
	//arraylist of areas of polygons defining the entirety of the obstacles on the map, used for collision
	public ArrayList<Area> allObstacles(){//array of areas of the polygons defining all the non-wall linked obstacle
		int [] obsX,obsY;
		ArrayList<Area> all = new ArrayList<Area>();
		obsX = new int[]{404,435,435,404};
		obsY = new int[]{323,323,373,373};
		all.add(new Area(new Polygon(obsX,obsY,obsX.length)));
		obsX = new int[]{449,478,478,449};
		obsY = new int[]{305,305,381,381};
		all.add(new Area(new Polygon(obsX,obsY,obsX.length)));
		obsX = new int[]{824,843,843,848,848,870,870,848,848,840,840,822,822,816,816,795,795,816,816,824};
		obsY = new int[]{437,437,450,450,472,472,490,490,500,500,513,513,500,500,482,482,464,464,450,450};
		all.add(new Area(new Polygon(obsX,obsY,obsX.length)));
		obsX = new int[]{351,390,405,413,421,421,427,424,395,384,376,375,350};
		obsY = new int[]{488,473,512,513,531,549,562,573,585,578,561,551,501};
		all.add(new Area(new Polygon(obsX,obsY,obsX.length)));
		
		return all;
	}
	//takes the areas of the walls and obstacles and see if there is a collision or not
	public boolean isInsideMap(int [] pos, int mx, int my){
		//gets the special collsion box of the player w/ rotation
		Area userArea = new Area(player.collisionBox(pos,mx,my));
		Area room = rooms();
		ArrayList<Area> allObstacles = allObstacles();
		
		room.intersect(userArea);//intersects the area of the walls and the player		
		if(!room.isEmpty()){return false;}//it collides if there is no area left between the 2
		for(Area obs: allObstacles){//for all the obstacles, make the same intersection check
			obs.intersect(userArea);
			if(!obs.isEmpty()){
				return false;
			}
		}
		return true;
	}
	
	//main gamepanel loop for drawing graphics, graphics parameter
    public void paintComponent(Graphics g){ 
    	g.setFont(font); 
    	
    	if(!getLose()){//if the game is not lost
    		//quit program if esc key is pressed 
    		if(keys[KeyEvent.VK_ESCAPE]){
    			System.exit(0);
    		}
    		
			g.setColor(Color.BLACK);
			//g2d.rotate(3);
			//g.drawImage(colourBack,0,0,this);
			//g.drawImage(tempBack,centerPicture(tempBack)[0],centerPicture(tempBack)[1],this);
			//g.drawImage(op.filter(tempBack, null), 0, 0, null);
			g.drawImage(completeBack,0,0,this);
			g.setColor(Color.WHITE);
						
			try{
				//if player is dead
				if(player.isDead()){
					user.sendData(stringHandler.dataCompress(new int[]{playerX,playerY,0,0},mx,my,1,userName));
					//only send data about stationary position of dead player
				}else{
					user.sendData(stringHandler.dataCompress(movement(),mx,my,((player.isDead()) ? 1 : 0),userName));		
					//else send normal data, includeing movement
				}				
			}catch(NullPointerException e){
				//System.out.println(stringHandler.dataCompress(movement(),mx,my));
			}
			//if player 2 is alive
			if(!player2.isDead()){
				//if the player actually dies, draw his death
				if(player.isDead()){
					player.dead(g,this,new int[]{playerX,playerY},mx,my);
				}
				//else if he is attacking, draw swing
				else if(movement()[2]==1){//player swings bat
					player.swingBat(g,this,movement(),mx,my);	
				}else if(movement()[3]==0){//idle movement
					player.idle(g,this,movement(),mx,my);
				}else{						//movement
					player.move(g,this,movement(),mx,my);
				}
			}
			

			try{
				//int array for the data recieved from the server by the other player
				int [] receivedData = stringHandler.arrayedData(user.getData());//seperate data into its components
				int [] receivedPos = new int[]{receivedData[0],receivedData[1]}; 
				int receivedSwing = receivedData[2];
				int receivedIsMoving = receivedData[3];
				int receivedMx = receivedData[4];
				int receivedMy = receivedData[5];
				int receivedDeath = receivedData[6];
				String receivedUserName = stringHandler.getOtherUserName();
				int receivedId = receivedData[8];
				
				//alternates spawn points between both players(so they spawn at different positions)
				if(receivedId%2==0 && !changedSpawn){
					playerX = initialPlayer2PosX;
					playerY = initialPlayer2PosY;
					changedSpawn = true;
				}
				//if someone is dead
				if(player.isDead()||player2.isDead()){
					gameOverCounter++;
					//end game if any player dies
					if(gameOverCounter>deathCamTime){
						gameOver();
					}
				}
				//if current player kills the other
				if(player2.isDead()||receivedDeath==1){
					g.drawImage(completeBack,0,0,this);
					player2.dead(g,this,receivedPos,receivedMx,receivedMy);//drawdead other player
					player2.hasDied();
					if(movement()[2]==1){//including movement afterwards
						player.swingBat(g,this,movement(),mx,my);	
					}else if(movement()[3]==0){
						player.idle(g,this,movement(),mx,my);
					}else{
						player.move(g,this,movement(),mx,my);
					}
				}else if(receivedSwing == 1){//if other player tries to take a swing
					//draw swing
					player2.swingBat(g,this,receivedPos,receivedMx,receivedMy);
					//if player2 his our player, we die
					if(player.isUserHit(movement(),mx,my,receivedPos,receivedMx,receivedMy)){//if the current player is hit and dead
						g.drawImage(completeBack,0,0,this);
						player2.swingBat(g,this,receivedPos,receivedMx,receivedMy);//draw current dead player
						player.dead(g,this,new int[]{playerX,playerY},mx,my);
						player.hasDied();
						
					}
				}else if(receivedIsMoving==0){
					player2.idle(g,this,receivedPos,receivedMx,receivedMy);
				}else{
					player2.move(g,this,receivedPos,receivedMx,receivedMy);	
				}
				g.drawString(userName + " VS. "+receivedUserName,SCREENRESX/2-30,yFontOffset);//draw the name of the host computers versus each pther
			}catch(NullPointerException e){g.drawString(userName + " VS.",SCREENRESX/2-30,yFontOffset);}
			
			
    	}
    }
    
}
//handles data packeging from and to strings for sending through socket
class StringHandler{
	String data;
	String otherUserName = "";
	public StringHandler(){}
	//takes a string and returns int array to read all the received values
	public int [] arrayedData(String input){
		String[] temp = input.split(",");
		int[] data = new int[temp.length];
		for(int i= 0; i<temp.length;i++){
			try{
				data[i]=Integer.parseInt(temp[i]);	
			}
			catch(NumberFormatException e){
				otherUserName = temp[i];//this String will be saved for the host name, not counted in int []
			}
		}
		return data;
	}
	//get computer name of other player
	public String getOtherUserName(){
		return otherUserName;
	}
	//take data we want to send and copact it into one string
	public String dataCompress(int[] data, int mx, int my, int isDead, String userName){
		//converts the data i need to send into string to be able to be sent to the server
		String temp = "";
		for(int num: data){
			temp+=String.valueOf(num)+",";
		}
		temp+=String.valueOf(mx)+",";
		temp+=String.valueOf(my)+",";
		temp+=String.valueOf(isDead)+",";
		temp+=userName;
		return temp;
	}
}
//handles connection to server
class Client implements Database {
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	Input inputHandler;
	//client constructor
	public Client() throws Exception{
		//this is where you switch between local and hosted server,
		//socket = new Socket("localhost", PORT);//starts connection with ip @ port
		socket=new Socket(IPADDRESS,PORT);
		in = new DataInputStream(socket.getInputStream());//creates input/output strems
		out=new DataOutputStream(socket.getOutputStream());
		inputHandler = new Input(in);//used for handling inputed data
		Thread thread = new Thread(inputHandler);//start thread for communcation between streams
		thread.start();		
	}
	//send string through socket
	public void sendData(String data){
		try{
			out.writeUTF(data);	//send out string
		}catch(IOException e){}
		
	}
	//get inputed data from input class
	public String getData(){
		return inputHandler.getData();//receive string
	}	
}
//handles input from server
class Input implements Runnable{
	DataInputStream in;
	
	String dataInput;
	//get the data saved in this class from server
	public String getData(){
		return dataInput;
	}
	
	//constructor, used datainputstream	
	public Input(DataInputStream in){
		this.in=in;
	}
	//runs sidebyside with other methods
	public void run(){
		while (true){
			try{
				dataInput = in.readUTF();//receive data in the form of String
			}
			catch (IOException e){}
		}
	}
}