package tw.gary.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class gamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE;
	static int DELAY = 90;
	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int appleEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running, isPause, isCreate;
	Timer timer;
	Random random;
	BufferedImage apple, snakeR, snakeL, snakeU, snakeD, body1, body2, body3, body4, body5, body6, tailR, tailL, tailU,
			tailD, background = null;
	public Thread t1;

	public gamePanel() {

		random = new Random();
		// 設定想要的尺寸

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		// 設定焦點
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		try {

			apple = ImageIO.read(new File("snake/apple.png"));
			snakeR = ImageIO.read(new File("snake/snakeR.png"));
			snakeL = ImageIO.read(new File("snake/snakeL.png"));
			snakeU = ImageIO.read(new File("snake/snakeU.png"));
			snakeD = ImageIO.read(new File("snake/snakeD.png"));
			tailR = ImageIO.read(new File("snake/tailR.png"));
			tailL = ImageIO.read(new File("snake/tailL.png"));
			tailU = ImageIO.read(new File("snake/tailU.png"));
			tailD = ImageIO.read(new File("snake/tailD.png"));
			body1 = ImageIO.read(new File("snake/body1.png"));
			body2 = ImageIO.read(new File("snake/body2.png"));
			body3 = ImageIO.read(new File("snake/body3.png"));
			body4 = ImageIO.read(new File("snake/body4.png"));
			body5 = ImageIO.read(new File("snake/body5.png"));
			body6 = ImageIO.read(new File("snake/body6.png"));
			background = ImageIO.read(new File("snake/background.jpg"));

		} catch (IOException e) {
			System.out.println(e.toString());
		}

		startGame();
	};

	public void startGame() {
		isPause = true;
		running = true;
		isCreate = false;
		timer = new Timer(DELAY, this);
		timer.stop();
	}

	public void pauseGame() {
		if (isPause) {
			timer.stop();
			isPause = false;
		} else {
			timer.start();
			isPause = true;
		}

	}

	public void start() {
		timer.start();
		newApple();
		for(int i = 0 ; i<bodyParts; i++) {
			if((appleX == x[i]) && (appleY == y[i])) {
				newApple();
			}
		}
		isPause = true;
		running = true;
		isCreate = true;
	}

	// 畫圖

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score:" + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + appleEaten)) / 2,
				g.getFont().getSize());

		if (running && isCreate) {
			// 劃出網格狀 調整 unitsize 可以調整網格大小 unitSIZE 表示 裡面的物件大小
//			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
//			}
			// 繪製蘋果
			g.setColor(Color.red);
			g.drawImage(apple, appleX, appleY, null);
			
			
//			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			
			// 繪製蛇的身體
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					if (direction == 'R') {
						g.drawImage(snakeR, x[i], y[i], null);
					} else if (direction == 'L') {
						g.drawImage(snakeL, x[i], y[i], null);
					} else if (direction == 'U') {
						g.drawImage(snakeU, x[i], y[i], null);
					} else {
						g.drawImage(snakeD, x[i], y[i], null);
					}
//					g.setColor(
//////							new Color((int) random.nextInt(255), (int) random.nextInt(255), (int) random.nextInt(255))
////									Color.BLUE);				        
////					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else if (i == (bodyParts - 1)) {
					if (x[i] < x[i - 1]) {
						g.drawImage(tailR, x[i], y[i], null);
					} else if (x[i] > x[i - 1]) {
						g.drawImage(tailL, x[i], y[i], null);
					} else if (y[i] > y[i - 1]) {
						g.drawImage(tailU, x[i], y[i], null);
					} else {
						g.drawImage(tailD, x[i], y[i], null);
					}
				} else {
					if (x[i] <= x[i - 1] && x[i] <= x[i + 1] && y[i] <= y[i - 1] && y[i] <= y[i + 1]) {
						g.drawImage(body3, x[i], y[i], null);
					} else if (x[i] <= x[i + 1] && x[i] <= x[i - 1] && y[i] >= y[i + 1] && y[i] >= y[i - 1]) {
						g.drawImage(body4, x[i], y[i], null);
					} else if (x[i] >= x[i + 1] && x[i] >= x[i - 1] && y[i] <= y[i + 1] && y[i] <= y[i - 1]) {
						g.drawImage(body5, x[i], y[i], null);
					} else if (x[i] >= x[i + 1] && x[i] >= x[i - 1] && y[i] >= y[i + 1] && y[i] >= y[i - 1]) {
						g.drawImage(body6, x[i], y[i], null);
					} else if (x[i] != x[i - 1] && x[i] != x[i + 1]) {
						g.drawImage(body1, x[i], y[i], null);
					} else if (y[i] != y[i - 1] && y[i] != y[i + 1]) {
						g.drawImage(body2, x[i], y[i], null);
					}
//					g.setColor(new Color(45, 180, 0));
//					g.setColor(
//							new Color((int) random.nextInt(255), (int) random.nextInt(255), (int) random.nextInt(255))
//							Color.GREEN);
//					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

				}
			}
		} else if (!running && isCreate) {
			gameOver(g);
		}

	}

	public void newApple() {
		// 隨機數字 (範圍 )
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		// 上下左右移動
		switch (direction) {
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
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			appleEaten++;
			newApple();
			for(int i = 0 ; i<bodyParts; i++) {
				if((appleX == x[i]) && (appleY == y[i])) {
					newApple();
				}
			}
		
			
			
			t1.start();
		}
		t1 = new Thread() {
			@Override
			public void run() {
				File file = new File("snake/score.wav");
				try {
					AudioInputStream audio = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);

					clip.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		};
	}

	public void checkcCollisions() {
		for (int i = bodyParts; i > 0; i--) {
			// 是否頭與身體相撞
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
				isCreate = true;
			}
		}
		// 檢查是否碰撞左邊邊界
		if (x[0] < 0) {
			running = false;
			isCreate = true;
		}
		// 檢查是否碰撞右邊邊界
		if (x[0] >= SCREEN_WIDTH) {
			running = false;
			isCreate = true;
		}
		// 檢查是否碰撞上面邊界
		if (y[0] < 0) {
			running = false;
			isCreate = true;
		}
		// 檢查是否碰撞下面邊界
		if (y[0] >= SCREEN_HEIGHT) {
			running = false;
			isCreate = true;
		}
		if (!running) {
			timer.stop();
		}
	}// 碰撞方法

	public void gameOver(Graphics g) {
		// gameover score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score:" + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score:" + appleEaten)) / 2,
				g.getFont().getSize());

		// gameover.text;
		g.setColor(Color.red);
		// 設定 字型
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

		new Thread() {
			@Override
			public void run() {
				File file = new File("snake/gameover.wav");
				try {
					AudioInputStream audio = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);

					clip.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		}.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkcCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				// 只能90度
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				// 只能90度
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				// 只能90度
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				// 只能90度
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SHIFT:
				// 加速
				timer.setDelay(40);
				break;

			case KeyEvent.VK_SPACE:
				if (running && !isCreate) {
					start();
				} else if (!isPause) {
					timer.start();
					isPause = true;
				} else if (isPause) {
					timer.stop();
					isPause = false;

				}
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				timer.setDelay(DELAY);
			}
		}
	}

}
