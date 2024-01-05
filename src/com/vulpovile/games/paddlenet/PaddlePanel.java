package com.vulpovile.games.paddlenet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vulpovile.games.listeners.SettingsListener;
import com.vulpovile.games.paddlenet.netcode.NetworkHandler;
import com.vulpovile.games.paddlenet.netcode.packet.Packet1Paddle1;
import com.vulpovile.games.paddlenet.netcode.packet.Packet2Paddle2;
import com.vulpovile.games.paddlenet.netcode.packet.Packet3Score1;
import com.vulpovile.games.paddlenet.netcode.packet.Packet4Score2;
import com.vulpovile.games.paddlenet.netcode.packet.Packet5BallVelocity;
import com.vulpovile.games.paddlenet.netcode.packet.Packet6BallPosition;
import com.vulpovile.games.paddlenet.netcode.packet.Packet7PaddleHit;
import com.vulpovile.games.paddlenet.netcode.packet.Packet8PaddleMiss;
import com.vulpovile.games.paddlenet.netcode.packet.Packet9BoardConfiguration;

/**
 * Yes I am aware the code for this is horrific
 * I made it in half an hour because I wondered how easy it would be to make
 * I did not care about any conventions, clean code, or anything
 * 
 * Good Luck
 * 
 * @author Vulpovile
 *
 */
public class PaddlePanel extends JPanel implements MouseMotionListener, MouseWheelListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int PADDLE = 0;
	public static final int TENNIS = 1;
	public static final int HOCKEY = 2;
	public static final int SQUASH = 3;

	private Random random = new Random();

	private JFrame containerWindow;

	private NetworkHandler networkHandler = null;

	private boolean isWaiting = false;

	private byte gameOverScore = 15;

	private short paddle1 = 240;
	private short paddle2 = 240;

	private short paddle1HeightHalf = 40;
	private short paddle2HeightHalf = 40;

	private short ballX = 640 / 2;
	private short ballY = 480 / 2;

	private byte ballXVelocity = 5;
	private byte ballYVelocity = 0;

	private byte ballHeightHalf = 8;
	private byte ballWidthHalf = 8;

	private short lastMouseTick = -1;

	private byte resetTicks = -1;

	private byte score1 = 0;
	private byte score2 = 0;

	private byte gameMode = PADDLE;

	private byte paddleDivider = 2;

	private boolean gameOver = false;

	private boolean colour = false;

	private boolean rightOwner = false;

	//For Ice Hockey
	private boolean paddle12hit = false;
	private boolean paddle22hit = false;
	private boolean canHit = true;
	
	private ArrayList<SettingsListener> settingsListeners = new ArrayList<SettingsListener>();

	//Functions

	public PaddlePanel(JFrame jFrame) {
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		this.requestFocusInWindow();
		this.containerWindow = jFrame;
		reset();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		super.paintComponent(g);
		g2d.scale(this.getWidth() / 640F, this.getHeight() / 480F);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 640, 480);
		if (colour)
			g2d.setColor(Color.YELLOW);
		else g2d.setColor(Color.WHITE);
		if (!isWaiting && gameMode != SQUASH)
		{
			g2d.fillRect(316, 2, 8, 474);
		}

		for (int i = 0; i < 160; i++)
		{
			g2d.fillRect(i * 10 + 3, 2, 4, 4);
			g2d.fillRect(i * 10 + 3, 472, 4, 4);
		}

		if (gameMode == HOCKEY)
		{
			for (int i = 0; i < 14; i++)
			{
				g2d.fillRect(3, i * 10 + 2, 4, 4);
				g2d.fillRect(633, i * 10 + 2, 4, 4);

				g2d.fillRect(3, 472 - (i * 10), 4, 4);
				g2d.fillRect(633, 472 - (i * 10), 4, 4);
			}
		}
		else if (gameMode == SQUASH)
		{
			for (int i = 0; i < 60; i++)
			{
				g2d.fillRect(3, i * 10 + 2, 4, 4);
			}
		}

		g2d.translate(150, 10);
		if (score1 >= 10)
			drawDigit(score1 / 10, g2d);
		g2d.translate(65, 0);
		drawDigit(score1 % 10, g2d);

		g2d.translate(155, 0);
		if (score2 >= 10)
			drawDigit(score2 / 10, g2d);
		g2d.translate(65, 0);
		drawDigit(score2 % 10, g2d);
		g2d.translate(-435, -10);

		if (isWaiting)
		{
			g2d.translate(198, 190);
			drawWait(g2d);
			g2d.translate(-198, -190);
		}

		if (colour)
			g2d.setColor(Color.BLUE);
		g2d.fillRect(ballX - ballWidthHalf, ballY - ballHeightHalf, ballWidthHalf + ballWidthHalf, ballHeightHalf + ballHeightHalf);

		if (gameMode == SQUASH)
		{
			if (!rightOwner && colour)
				g2d.setColor(Color.RED.darker());
			else if (!rightOwner)
				g2d.setColor(Color.GRAY);
			else if (colour)
				g2d.setColor(Color.RED);
		}
		else if (colour)
			g2d.setColor(Color.RED);
		else g2d.setColor(Color.WHITE);
		if (gameMode == SQUASH)
			g2d.fillRect(594, paddle1 - paddle1HeightHalf, 8, paddle1HeightHalf + paddle1HeightHalf);
		else g2d.fillRect(20, paddle1 - paddle1HeightHalf, 8, paddle1HeightHalf + paddle1HeightHalf);
		if (gameMode == TENNIS)
			g2d.fillRect(220, paddle1 - paddle1HeightHalf, 8, paddle1HeightHalf + paddle1HeightHalf);
		else if (gameMode == HOCKEY)
			g2d.fillRect(472, paddle1 - paddle1HeightHalf, 8, paddle1HeightHalf + paddle1HeightHalf);
		if (gameMode == SQUASH)
		{
			if (rightOwner && colour)
				g2d.setColor(Color.CYAN.darker());
			else if (rightOwner)
				g2d.setColor(Color.GRAY);
			else if (colour)
				g2d.setColor(Color.CYAN);
			else g2d.setColor(Color.WHITE);
		}
		else if (colour)
			g2d.setColor(Color.CYAN);
		else g2d.setColor(Color.WHITE);
		g2d.fillRect(612, paddle2 - paddle2HeightHalf, 8, paddle2HeightHalf + paddle2HeightHalf);
		if (gameMode == TENNIS)
			g2d.fillRect(412, paddle2 - paddle2HeightHalf, 8, paddle2HeightHalf + paddle2HeightHalf);
		else if (gameMode == HOCKEY)
			g2d.fillRect(160, paddle2 - paddle2HeightHalf, 8, paddle2HeightHalf + paddle2HeightHalf);
	}

	public void drawWait(Graphics2D g2d) {
		g2d.fillRect(0, 0, 15, 100);
		g2d.fillRect(35, 0, 15, 100);
		g2d.fillRect(70, 0, 15, 100);
		g2d.fillRect(0, 85, 75, 15);

		g2d.fillRect(100, 0, 50, 15);
		g2d.fillRect(100, 50, 50, 15);
		g2d.fillRect(100, 0, 15, 100);
		g2d.fillRect(135, 0, 15, 100);

		g2d.fillRect(165, 0, 15, 100);

		g2d.fillRect(195, 0, 49, 15);
		g2d.fillRect(213, 0, 15, 100);
	}

	//YUCK
	//There HAS to be a better way
	public void drawDigit(int digit, Graphics2D g2d) {
		switch (digit) {
			case 1:
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				break;
			case 2:
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
				//Bottom Left
				g2d.fillRect(0, 52, 15, 51);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				break;
			case 3:
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				break;
			case 4:
				//Top Left
				g2d.fillRect(0, 0, 15, 52);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				break;
			case 5:
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
				//Top Left
				g2d.fillRect(0, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				break;
			case 6:
				//Top Left
				g2d.fillRect(0, 0, 15, 52);
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				//Bottom Left
				g2d.fillRect(0, 52, 15, 51);
				break;
			case 7:
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				break;
			case 8:
				//Top Left
				g2d.fillRect(0, 0, 15, 52);
				//Bottom Left
				g2d.fillRect(0, 52, 15, 51);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				//Horizontal
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
				break;
			case 9:
				//Top Left
				g2d.fillRect(0, 0, 15, 52);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Middle
				g2d.fillRect(0, 42, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
				break;
			default:
				//Top Left
				g2d.fillRect(0, 0, 15, 52);
				//Bottom Left
				g2d.fillRect(0, 52, 15, 51);
				//Top Right
				g2d.fillRect(35, 0, 15, 52);
				//Bottom Right
				g2d.fillRect(35, 52, 15, 51);
				//Horizontal
				//Top
				g2d.fillRect(0, 0, 50, 15);
				//Bottom
				g2d.fillRect(0, 88, 50, 15);
		}

	}

	public void tick() {
		if (networkHandler == null || networkHandler.isServer())
		{
			short newPaddle2 = (short) ((lastMouseTick * 480) / getHeight());
			if (newPaddle2 != paddle2 && networkHandler != null)
			{
				networkHandler.enqueuePacket(new Packet2Paddle2(newPaddle2));
			}
			paddle2 = newPaddle2;
		}
		else
		{
			short newPaddle1 = (short) ((lastMouseTick * 480) / getHeight());
			if (newPaddle1 != paddle1 && networkHandler != null)
			{
				networkHandler.enqueuePacket(new Packet1Paddle1(newPaddle1));
			}
			paddle1 = newPaddle1;
		}
		if (resetTicks == -1)
		{
			if (ballY != Short.MIN_VALUE)
			{
				ballY += ballYVelocity;
				for (int i = 0; i < Math.abs(ballXVelocity); i++)
				{
					//Step
					ballX += Math.signum(ballXVelocity);

					//Scoring
					if (!isServer() && rightOwner && (ballX < -20 || (gameMode == SQUASH && ballX > 660)))
					{
						resetTicks = 60;
						ballX = -10;
						if (!gameOver)
						{
							score2++;
							if (networkHandler != null)
							{
								networkHandler.enqueuePacket(new Packet8PaddleMiss());
								networkHandler.enqueuePacket(new Packet4Score2(score2));
							}
							if (score2 >= gameOverScore)
							{
								gameOver = true;
								sendBoardConfiguration(true);
							}
						}
						Sound.toneAsync(2019, 50, 0.5);
						break;
					}
					else if (!rightOwner && ballX > 660 && (networkHandler == null || networkHandler.isServer()))
					{
						resetTicks = 60;
						ballX = -10;
						if (!gameOver)
						{
							score1++;
							if (networkHandler != null)
							{
								networkHandler.enqueuePacket(new Packet8PaddleMiss());
								networkHandler.enqueuePacket(new Packet3Score1(score1));
							}
							if (score1 >= gameOverScore)
							{
								gameOver = true;
								sendBoardConfiguration(true);
							}
						}
						Sound.toneAsync(2019, 50, 0.5);
						break;
					}

					//Single player paddle code 
					if (networkHandler == null && rightOwner)
						paddle1 -= (short) ((paddle1 - ballY) / (20 + random.nextInt(40)));

					if (ballY < ballHeightHalf + ballHeightHalf && ballYVelocity < 0 || ballY > 480 && ballYVelocity > 0)
					{
						ballYVelocity = (byte) -ballYVelocity;
						Sound.toneAsync(496, 50, 0.5);
						break;
					}

					if (gameMode == SQUASH && ballXVelocity < 0 && ballX < 4)
					{
						ballXVelocity = (byte) -ballXVelocity;
						canHit = true;
						Sound.toneAsync(496, 50, 0.5);
						break;
					}
					else if (gameMode == HOCKEY && (ballY < 142 || ballY > 338) && (ballXVelocity < 0 && ballX < 4 || ballXVelocity > 0 && ballX > 636))
					{
						ballXVelocity = (byte) -ballXVelocity;
						paddle22hit = false;
						paddle12hit = false;
						updateDirection();
						Sound.toneAsync(496, 50, 0.5);
						break;
					}

					//Collision
					if (!gameOver)
					{
						if (isMain() && !rightOwner && ballX > 612 && ballX < 620 && ballY < paddle2 + paddle2HeightHalf + ballHeightHalf && ballY > paddle2 - paddle2HeightHalf - ballHeightHalf)
						{
							canHit = false;
							this.paddle22hit = false;
							this.paddle12hit = false;
							this.rightOwner = !this.rightOwner;
							ballXVelocity = (byte) -ballXVelocity;
							int ballHitOffset = (ballY - paddle2) / (2 * (paddle2HeightHalf / 20)) / paddleDivider;
							ballYVelocity = (byte) (ballHitOffset * Math.abs(ballXVelocity / 5));
							Sound.toneAsync(1019, 50, 0.5);
							if (networkHandler != null)
							{
								networkHandler.enqueuePacket(new Packet7PaddleHit(ballX, ballY, ballXVelocity, ballYVelocity));
							}
							break;
						}
						else if (!isServer() && rightOwner && (gameMode == SQUASH && canHit && ballX > 594 && ballX < 602 || gameMode != SQUASH && ballX > 20 && ballX < 28) && ballY < paddle1 + paddle1HeightHalf + ballHeightHalf && ballY > paddle1 - paddle1HeightHalf - ballHeightHalf)
						{
							canHit = false;
							this.paddle22hit = false;
							this.paddle12hit = false;
							this.rightOwner = !this.rightOwner;
							ballXVelocity = (byte) -ballXVelocity;
							int ballHitOffset = (ballY - paddle1) / (2 * (paddle1HeightHalf / 20)) / paddleDivider;
							ballYVelocity = (byte) (ballHitOffset * Math.abs(ballXVelocity / 5));
							Sound.toneAsync(1019, 50, 0.5);
							if (networkHandler != null)
							{
								networkHandler.enqueuePacket(new Packet7PaddleHit(ballX, ballY, ballXVelocity, ballYVelocity));
							}
							break;
						}
						else if (gameMode == HOCKEY)
						{
							if (isMain() && !paddle22hit && ballX > 160 && ballX < 168 && ballY < paddle2 + paddle2HeightHalf + ballHeightHalf && ballY > paddle2 - paddle2HeightHalf - ballHeightHalf)
							{
								canHit = false;
								this.paddle22hit = true;
								if (!rightOwner)
								{
									this.paddle12hit = false;
									this.rightOwner = !this.rightOwner;
									ballXVelocity = (byte) -ballXVelocity;
								}
								int ballHitOffset = (ballY - paddle2) / (2 * (paddle2HeightHalf / 20)) / paddleDivider;
								ballYVelocity = (byte) (ballHitOffset * Math.abs(ballXVelocity / 5));
								Sound.toneAsync(1019, 50, 0.5);
								if (networkHandler != null)
								{
									networkHandler.enqueuePacket(new Packet7PaddleHit(ballX, ballY, ballXVelocity, ballYVelocity, rightOwner));
								}
								break;
							}
							else if (!isServer() && !paddle12hit && ballX > 472 && ballX < 480 && ballY < paddle1 + paddle1HeightHalf + ballHeightHalf && ballY > paddle1 - paddle1HeightHalf - ballHeightHalf)
							{
								canHit = false;
								this.paddle12hit = true;
								if (rightOwner)
								{
									this.paddle22hit = false;
									this.rightOwner = !this.rightOwner;
									ballXVelocity = (byte) -ballXVelocity;
								}
								int ballHitOffset = (ballY - paddle1) / (2 * (paddle1HeightHalf / 20)) / paddleDivider;
								ballYVelocity = (byte) (ballHitOffset * Math.abs(ballXVelocity / 5));
								Sound.toneAsync(1019, 50, 0.5);
								if (networkHandler != null)
								{
									networkHandler.enqueuePacket(new Packet7PaddleHit(ballX, ballY, ballXVelocity, ballYVelocity, !rightOwner));
								}
								break;
							}
						}
						else if (gameMode == TENNIS)
						{
							if (isMain() && !rightOwner && ballX > 412 && ballX < 420 && ballY < paddle2 + paddle2HeightHalf + ballHeightHalf && ballY > paddle2 - paddle2HeightHalf - ballHeightHalf)
							{
								canHit = false;
								this.paddle22hit = false;
								this.paddle12hit = false;
								this.rightOwner = !this.rightOwner;
								ballXVelocity = (byte) -ballXVelocity;
								int ballHitOffset = (ballY - paddle2) / (2 * (paddle2HeightHalf / 20)) / paddleDivider;
								ballYVelocity = (byte) (ballHitOffset * Math.abs(ballXVelocity / 5));
								Sound.toneAsync(1019, 50, 0.5);
								if (networkHandler != null)
								{
									networkHandler.enqueuePacket(new Packet7PaddleHit(ballX, ballY, ballXVelocity, ballYVelocity));
								}
								break;
							}
							else if (!isServer() && rightOwner && ballX > 220 && ballX < 228 && ballY < paddle1 + paddle1HeightHalf + ballHeightHalf && ballY > paddle1 - paddle1HeightHalf - ballHeightHalf)
							{
								this.canHit = false;
								this.paddle22hit = false;
								this.paddle12hit = false;
								this.rightOwner = !this.rightOwner;
								ballXVelocity = (byte) -ballXVelocity;
								int ballHitOffset = (ballY - paddle1) / (2 * (paddle1HeightHalf / 20)) / paddleDivider;
								ballYVelocity = (byte) (ballHitOffset * Math.abs(ballXVelocity / 5));
								Sound.toneAsync(1019, 50, 0.5);
								if (networkHandler != null)
								{
									networkHandler.enqueuePacket(new Packet7PaddleHit(ballX, ballY, ballXVelocity, ballYVelocity));
								}
								break;
							}
						}
					}
				}
			}
		}
		else if (resetTicks == 0 && isMain())
		{
			if (ballXVelocity > 0)
			{
				ballX = (short) -ballWidthHalf;
			}
			else
			{
				ballX = (short) (640 + ballWidthHalf);
			}
			updateDirection();
			ballY = 480 / 2;
			if (networkHandler != null)
			{
				networkHandler.enqueuePacket(new Packet6BallPosition(ballX, ballY));
				networkHandler.enqueuePacket(new Packet5BallVelocity(ballXVelocity, ballYVelocity));
			}
			resetTicks--;
		}
		else if (isMain())
		{
			resetTicks--;
		}

		repaint();
	}

	public void updateDirection() {
		if (gameMode != SQUASH)
		{
			this.rightOwner = ballXVelocity < 0;
		}
	}

	public void reset() {
		if (random.nextBoolean())
		{
			ballXVelocity = (byte) -ballXVelocity;
		}
		this.canHit = false;
		this.paddle22hit = false;
		this.paddle12hit = false;
		this.rightOwner = ballXVelocity < 0;
		if (gameMode == SQUASH)
		{
			ballXVelocity = (byte) Math.abs(ballXVelocity);
		}

		ballX = 320;
		ballY = Short.MIN_VALUE;
		ballYVelocity = 0;
		score1 = 0;
		score2 = 0;
		gameOver = false;
		resetTicks = 60;
		sendBoardConfiguration(true);
		String s = PaddleNet.PRODUCT_NAME;
		if (networkHandler != null)
		{
			if (networkHandler.isServer())
			{
				s += " - Connected as Server (RIGHT)";
			}
			else
			{
				s += " - Connected as Client (LEFT)";
			}
		}
		containerWindow.setTitle(s);
		settingsChanged();
	}

	public boolean isServer() {
		return this.networkHandler != null && networkHandler.isServer();
	}

	public boolean isMain() {
		return this.networkHandler == null || networkHandler.isServer();
	}

	public void sendBoardConfiguration(boolean complete) {
		if (isServer())
		{
			networkHandler.enqueuePacket(new Packet9BoardConfiguration(paddle1HeightHalf, paddle2HeightHalf, gameOverScore, ballHeightHalf, ballWidthHalf, paddleDivider, gameMode, gameOver, colour, rightOwner));
			if (complete)
			{
				networkHandler.enqueuePacket(new Packet3Score1(score1));
				networkHandler.enqueuePacket(new Packet4Score2(score2));
				networkHandler.enqueuePacket(new Packet5BallVelocity(ballXVelocity, ballYVelocity));
				networkHandler.enqueuePacket(new Packet6BallPosition(ballX, ballY));
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		lastMouseTick = (short) e.getY();
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (isMain())
		{
			this.ballY += e.getUnitsToScroll() * 10;
			if (networkHandler != null)
				networkHandler.enqueuePacket(new Packet6BallPosition(ballX, ballY));
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (networkHandler == null || networkHandler.isServer())
		{
			switch (e.getKeyCode()) {
				case KeyEvent.VK_R:
					reset();
					break;
				case KeyEvent.VK_Q:
					if (this.paddle1HeightHalf == 40)
						this.paddle1HeightHalf = 20;
					else this.paddle1HeightHalf = 40;
					sendBoardConfiguration(true);
					settingsChanged();
					break;
				case KeyEvent.VK_P:
					if (this.paddle2HeightHalf == 40)
						this.paddle2HeightHalf = 20;
					else this.paddle2HeightHalf = 40;
					sendBoardConfiguration(true);
					settingsChanged();
					break;
				case KeyEvent.VK_B:
					if (Math.abs(ballXVelocity) != 5)
						ballXVelocity = (byte) (Math.signum(ballXVelocity) * 5);
					else ballXVelocity = (byte) (Math.signum(ballXVelocity) * 10);
					sendBoardConfiguration(true);
					settingsChanged();
					break;
				case KeyEvent.VK_A:
					if (paddleDivider != 1)
						paddleDivider = (byte) 1;
					else paddleDivider = (byte) 2;
					sendBoardConfiguration(true);
					settingsChanged();
					break;
				case KeyEvent.VK_C:
					colour = !colour;
					sendBoardConfiguration(true);
					settingsChanged();
					break;
				case KeyEvent.VK_F12:
					if (Math.abs(ballYVelocity) == 100)
						ballYVelocity = 0;
					else ballYVelocity = 100;
					sendBoardConfiguration(true);
					break;
				case KeyEvent.VK_1:
				case KeyEvent.VK_2:
				case KeyEvent.VK_3:
				case KeyEvent.VK_4:
					this.gameMode = (byte) (e.getKeyCode() - KeyEvent.VK_1);
					updateDirection();
					sendBoardConfiguration(true);
					settingsChanged();
					break;
				default:
					break;
			}
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void setPaddle1(short pos) {
		this.paddle1 = pos;
	}

	public void setPaddle2(short pos) {
		this.paddle2 = pos;
	}

	public void setScore1(byte score) {
		this.score1 = score;
	}

	public void setScore2(byte score) {
		this.score2 = score;
	}

	public void setBallVelocity(byte x, byte y) {
		this.ballXVelocity = x;
		this.ballYVelocity = y;
		updateDirection();
		resetTicks = -1;
	}

	public void setBallPosition(short x, short y) {
		this.ballX = x;
		this.ballY = y;
		resetTicks = -1;
	}

	public void hit(short x, short y, byte xVelocity, byte yVelocity, boolean resetPaddleFlags) {
		canHit = false;
		if (resetPaddleFlags)
		{
			this.paddle12hit = false;
			this.paddle22hit = false;
		}
		setBallPosition(x, y);
		setBallVelocity(xVelocity, yVelocity);
		if (gameMode == SQUASH)
			this.rightOwner = !this.rightOwner;
		else updateDirection();
		Sound.toneAsync(1019, 50, 0.5);
	}

	public void update(short paddle1HeightHalf, short paddle2HeightHalf, byte gameOverScore, byte ballHeightHalf, byte ballWidthHalf, byte paddleDivider, byte gameMode, boolean gameOver, boolean colour, boolean rightOwner) {
		this.paddle1HeightHalf = paddle1HeightHalf;
		this.paddle2HeightHalf = paddle2HeightHalf;
		this.gameOverScore = gameOverScore;
		this.ballHeightHalf = ballHeightHalf;
		this.ballWidthHalf = ballWidthHalf;
		this.paddleDivider = paddleDivider;
		this.gameOver = gameOver;
		this.colour = colour;
		this.gameMode = gameMode;
		this.rightOwner = rightOwner;
		settingsChanged();
	}

	public void setWaiting(boolean waiting) {
		this.isWaiting = waiting;
	}

	public void setNetworkHandler(NetworkHandler handler) {
		this.networkHandler = handler;
	}

	public void miss() {
		this.paddle12hit = false;
		this.paddle22hit = false;
		Sound.toneAsync(2019, 50, 0.5);
		if (isServer())
		{
			if (score1 >= gameOverScore || score2 >= gameOverScore)
			{
				gameOver = true;
				sendBoardConfiguration(true);
			}
			resetTicks = 60;
		}
	}

	public byte getBallXSpeed() {
		return this.ballXVelocity;
	}

	public byte getPaddleDivider() {
		return this.paddleDivider;
	}

	public boolean getColour() {
		return this.colour;
	}

	public short getPaddle1Size() {
		return this.paddle1HeightHalf;
	}

	public short getPaddle2Size() {
		return this.paddle2HeightHalf;
	}

	public byte getGameMode() {
		return this.gameMode;
	}
	
	public void removeSettingsListeners()
	{
		this.settingsListeners.clear();
	}
	
	public void removeSettingsListener(SettingsListener listener)
	{
		this.settingsListeners.remove(listener);
	}
	
	public void addSettingsListener(SettingsListener listener)
	{
		this.settingsListeners.add(listener);
	}
	
	public void settingsChanged()
	{
		for(int i = 0; i < settingsListeners.size(); i++)
		{
			settingsListeners.get(i).settingsChanged(this);
		}
	}
}
