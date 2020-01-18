package com.dodge.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dodge.main.enemy.Blue;
import com.dodge.main.enemy.Green;
import com.dodge.main.enemy.Navy;
import com.dodge.main.enemy.Orange;
import com.dodge.main.enemy.Purple;
import com.dodge.main.enemy.Red;
import com.dodge.main.enemy.Yellow;

@SuppressWarnings("serial")
public class Main extends JFrame {
	
	private JPanel board = null; // 다양한 JLabel들을 붙일 JPanel
	private ImageIcon background = null;
	private boolean startAvailableFlag = true;
	private boolean enterAvailableFlag = true;
	private Player player = null;
	private List<Integer> enemyList = null;
	private int timer = 0;
	
	public Main() {
		setTitle("간단하게 만든 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); // 게임창을 화면 정중앙에 가깝게 위치시키기 위한 코드
        int mw = dim.width / 2 - 200;
        int mh = (int) (dim.height * 0.15f);
		setSize(400, 400);
        setLocation(mw, mh);
		setResizable(false);
		
		background = new ImageIcon("graphics/title.jpg");
		
		board = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(background.getImage(), -100, 0, null);
			}
		};
		board.setLayout(null);
		setContentPane(board);
		
		setVisible(true);
		
		requestFocus(true);
		addKeyListener(new EnterEvent());
	}
	
	public void gameStart() {
		player = new Player(board);
		player.addKeyListener(new Controller());
		player.requestFocus(true);
		enemyList = new ArrayList<Integer>();
		new Red(board, player);
		timer = 0;
		TimerThread timerThread = new TimerThread();
		timerThread.start();
	}
	
	public void gameOver() {
		board.removeAll();
		board.revalidate();
		board.repaint();
		
		requestFocus(true);
		
		JLabel gameOverText = new JLabel("Game Over");
		Font font = new Font("나눔 고딕", Font.PLAIN, 40);
		gameOverText.setFont(font);
		gameOverText.setSize(250, 50);
		gameOverText.setLocation(100, 80);
		board.add(gameOverText);
		
		String totalTime = timer/10+"."+timer%10+"초";
		JLabel totalTimeText = new JLabel(totalTime);
		totalTimeText.setFont(font);
		totalTimeText.setSize(250, 50);
		totalTimeText.setLocation(150, 160);
		board.add(totalTimeText);

		JLabel restartText = new JLabel("Enter키를 눌러 타이틀로 돌아가세요.");
		font = new Font("나눔 고딕", Font.PLAIN, 20);
		restartText.setFont(font);
		restartText.setSize(350, 50);
		restartText.setLocation(40, 240);
		board.add(restartText);
		
		board.repaint();
		
		enterAvailableFlag = true;
	}
	
	class EnterEvent extends KeyAdapter { // 타이틀에서, 게임오버시 엔터키 이벤트
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_ENTER) {
				if(enterAvailableFlag) {
					if(startAvailableFlag) { // 게임시작
						startAvailableFlag = false;
						enterAvailableFlag = false;
						gameStart();
						background = new ImageIcon("graphics/bg.jpg");
						board.repaint();
					} else { // 게임 재시작
						startAvailableFlag = true;
						board.removeAll();
						board.revalidate();
						background = new ImageIcon("graphics/title.jpg");
						board.repaint();
					}
				}
			}
		}
	}
	
	class Controller extends KeyAdapter { // 플레이어 기체 조종 이벤트
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_UP) {player.keyDown(1);}
			if(keyCode == KeyEvent.VK_DOWN) {player.keyDown(2);}
			if(keyCode == KeyEvent.VK_LEFT) {player.keyDown(3);}
			if(keyCode == KeyEvent.VK_RIGHT) {player.keyDown(4);}
		}
		public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if(keyCode == KeyEvent.VK_UP) {player.keyUp(1);}
			if(keyCode == KeyEvent.VK_DOWN) {player.keyUp(2);}
			if(keyCode == KeyEvent.VK_LEFT) {player.keyUp(3);}
			if(keyCode == KeyEvent.VK_RIGHT) {player.keyUp(4);}
        }
	}
	
	public void handleError(String msg) { // 오류 처리
		System.out.println(msg);
		System.exit(1);
	}
	
	public void moreEnemy() {
		double ran;
        int enemy;
        while(true) {
            ran = Math.random();
            enemy = (int) (ran * 6) + 1; // 1~6의 수 무작위로
            if(!enemyList.contains(enemy)) // 이전의 패턴과 달라야 가능
                break;
        }
        switch(enemy) {
        case 1:
        	new Orange(board, player);
        	enemyList.add(1);
        	break;
        case 2:
        	new Yellow(board, player);
        	enemyList.add(2);
        	break;
        case 3:
        	new Green(board, player);
        	enemyList.add(3);
        	break;
        case 4:
        	new Blue(board, player);
        	enemyList.add(4);
        	break;
        case 5:
        	new Navy(board, player);
        	enemyList.add(5);
        	break;
        case 6:
        	new Purple(board, player);
        	enemyList.add(6);
        	break;
        }
		
	}
	
	class TimerThread extends Thread {
        public void run() {
        	while(!player.isGameOver()) {
                try {
                    timer++;
                    if(timer%50==0 && enemyList.size()<6) {
                    	moreEnemy();
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    handleError(e.getMessage());
                }
            }
        	gameOver();
        }
    }
	
	public static void main(String[] args) {
		new Main();
	}
}
