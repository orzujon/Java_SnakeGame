import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.*;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 150;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	int moves = 0;
	JButton resetButton;
	boolean appleTrue = true;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(new Color(51,51,51));
		this.setFocusable(true);
		this.setLayout(null);
		this.addKeyListener(new MyKeyAdapter());
		
		resetButton = new JButton();
		resetButton.setText("Restart");
		resetButton.setBackground(null);
		resetButton.setForeground(Color.white);
		resetButton.setBorderPainted(false);
		resetButton.setFont(new Font("Ink Free",Font.PLAIN,20));
		resetButton.setSize(UNIT_SIZE*5,UNIT_SIZE);
		resetButton.setLocation(240, 400);
		resetButton.setVisible(false);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		
		this.add(resetButton);
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		drawBorders(g);
		resetButton();
		
		if(running){
			if(moves == 0) {
				welcome(g);
			}
			int count = 6;
			if (moves == 0) {
				for(int i = 0; i < bodyParts; i++) {
					x[i] = count*UNIT_SIZE;
					y[i] = UNIT_SIZE;
					count--;
					}
			}	
			/* this is just to show he grid lines
			 * 
			for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_HEIGHT, i*UNIT_SIZE);
			}*/
			g.setColor(Color.green);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < bodyParts; i++) {
				if(i==0) {
					g.setColor(Color.RED);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()+UNIT_SIZE);
		} else {
			gameOver(g);
		}
	}
	
	public void drawBorders(Graphics g) {
		g.setColor(new Color(153,102,0));
		g.fillRect(0, 0, SCREEN_WIDTH, UNIT_SIZE);
		g.fillRect(0, 0, UNIT_SIZE, SCREEN_HEIGHT);
		g.fillRect(SCREEN_WIDTH-UNIT_SIZE, 0, UNIT_SIZE, SCREEN_HEIGHT);
		g.fillRect(0, SCREEN_HEIGHT-UNIT_SIZE, SCREEN_WIDTH, UNIT_SIZE);
	}
	public void newApple() {
			appleX = random.nextInt(2,23)*UNIT_SIZE;
			appleY = random.nextInt(2,23)*UNIT_SIZE;
	}
	
	public void move() {
		
		for(int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		//Check if new Apple is on Snake-BodyParts 
		for(int i = 0; i <= bodyParts-1; i++) {
			if((appleX == x[i]) && (appleY == y[i])) {
				newApple();
				}
		}
		//Checks if head collides with body
		for(int i1 = bodyParts; i1 > 0; i1--) {
			if((x[0] == x[i1]) && (y[0] == y[i1])) {
				running = false;
			}
		}
		//Check if head touches left border
		if(x[0] < UNIT_SIZE) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH-2*UNIT_SIZE) {
			running = false;
		}
		if(y[0] < UNIT_SIZE) {
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT-2*UNIT_SIZE) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
			moves = 0;
		}
		
	}
	
	public void gameOver(Graphics g) {
		//Score 
		
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()+UNIT_SIZE);
		
		//Game Over text 
		
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
	}
	public void welcome(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD,25));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Welcome to Orzujon's Portfolio", (SCREEN_WIDTH - metrics3.stringWidth("Press any key"))/2, SCREEN_HEIGHT-30);
		
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD,25));
		FontMetrics metrics4 = getFontMetrics(g.getFont());
		g.drawString("Press any key", (SCREEN_WIDTH - metrics4.stringWidth("Press any key"))/2, SCREEN_HEIGHT/2);
	}
	
	public void resetButton() {
		if(running) {
			resetButton.setVisible(false);
		} else {
			resetButton.setVisible(true);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		if(e.getSource()==resetButton) {
			running = false;
			timer.stop();
			applesEaten = 0;
			moves = 0;
			bodyParts = 6;
			direction = 'R';
			startGame();
			
		}
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
					moves++;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
					moves++;
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
					moves++;
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
					moves++;

				}
				break;
			}
		}
	}
}
