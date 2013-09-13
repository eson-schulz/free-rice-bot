import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Scanner;


public class FreeRiceBot {
	public static int count;
	public static int countMax = 50;
	public static int delayAmount = 200;
	public static int errorCount = 0;
	
	public static boolean adLoadEnabled = true;
	
	private BufferedImage screen;
	private int screenWidth, screenHeight;
	private int boxX, boxY;
	
	//Takes a screenShot of the screen
	private void setScreen(){
		Rectangle wholeBounds =  new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		try {
			BufferedImage bi = new Robot().createScreenCapture(wholeBounds);
			screenWidth = bi.getWidth();
			screenHeight = bi.getHeight();
			System.out.println("WIDTH: " + screenWidth + "  " + "HEIGHT: " + screenHeight);
			this.screen = bi;
		} catch (AWTException e) {
			throw new Error("FreeRiceBot Constructor - Error");
		}
	}
	
	private boolean findBoxStart(){
		for(int h = 0; h < screenHeight - 344; h++){
			for(int w = 0; w < screenWidth - 555; w++){
				if(isBox(w, h)){
					this.boxX = w;
					this.boxY = h;
					return true;
				}
			}
		}
		throw new Error("FreeRiceBot FindBox - Error 'Couldn't find box'");
	}
	
	//Gets the color of the pixel(red + blue + green)
	public static int getEasyRGB(int rgb){
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		return red + green + blue;
	}
	
	//Tells if this is the box
	private boolean isBox(int x, int y){
		if(getEasyRGB(screen.getRGB(x, y)) == 222){
			if(getEasyRGB(screen.getRGB(x + 549, y)) == 222){
				if(getEasyRGB(screen.getRGB(x, y + 343)) == 557){
					if(getEasyRGB(screen.getRGB(x, y + 344)) == 222){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Rectangle getBoxBounds(){
		return new Rectangle(boxX, boxY, 555, 334);
	}
	
	public boolean isAdUp(){
		Rectangle adBox = new Rectangle(boxX, boxY + 430, 1, 1);
		try {
			BufferedImage adImage = new Robot().createScreenCapture(adBox);
			if(getEasyRGB(adImage.getRGB(0, 0)) != 765){
				return true;
			}
			else{
				return false;
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
		throw new Error("Noep");
		
	}
	
	public void getUserInput(){
		System.out.println("Do you want to wait for ad? 1/0 (y/n)");
		Scanner sc = new Scanner(System.in);
		if(sc.nextInt() == 1){
			adLoadEnabled = true;
		}
		
		System.out.println("What rate do you want for the delay? 200/400/600");
		int speed = sc.nextInt();
		if(speed == 200 || speed == 400 || speed == 600){
			delayAmount = speed;
		}
		else{
			System.out.println("Not a valid choice, going to 500.");
			delayAmount = 500;
		}
		System.out.println("How many times do you want to go?");
		countMax = sc.nextInt();
		sc.close();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FreeRiceBot(){
		setScreen();
		findBoxStart();
	}
	
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		FreeRiceBot bot = new FreeRiceBot();
		Rectangle boxBounds = bot.getBoxBounds();
		int AdLoad = 0;
		NumberFinder finder;
		//bot.getUserInput();
		while(count < countMax){
			finder = new NumberFinder(boxBounds);
			if(! bot.isAdUp() && AdLoad <= 8 && adLoadEnabled){
				try {
					AdLoad += 1;
					System.out.println("Waiting: " + AdLoad);
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(finder.isLoading()){
				AdLoad = 0;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				AdLoad = 0;
				finder.setVariables();
				finder.clickAnswer();
				count += 1;
				System.out.println("Count: " + count);
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime - startTime) / 1000.0);
	}
}
