package com.dodge.main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Player extends JLabel {
	
	private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean gameover = false;
    
    private JPanel board = null;
    
    public Player(JPanel board) {
    	this.setIcon(new ImageIcon("graphics/player.png"));
    	this.setLocation(180, 180);
    	this.setSize(40, 40);
    	this.board = board;
    	board.add(this);
    	MoveThread moveThread = new MoveThread();
    	moveThread.start();
    }
    
    public void keyDown(int code) {
        switch(code) {
            case 1: up = true; break;
            case 2: down = true; break;
            case 3: left = true; break;
            case 4: right = true; break;
        }
    }

    public void keyUp(int code) {
        switch(code) {
            case 1: up = false; break;
            case 2: down = false; break;
            case 3: left = false; break;
            case 4: right = false; break;
        }
    }
    
    public void move() { // �����¿쿡 ���õ� flag�� true�� �Ǹ� �� �������� m ��ŭ �̵�
    	int x = this.getX();
    	int y = this.getY();
    	int m = 5; // �޼ҵ� ȣ�� �� �̵� �Ÿ�
    	
        if(y > 0 && up) {
        	y -= m;
            this.setLocation(x, y);
        }
        if(y < 330 && down) {
            y += m;
            this.setLocation(x, y);
        }
        if(x > 0 && left) {
            x -= m;
            this.setLocation(x, y);
        }
        if(x < 355 && right) {
            x += m;
            this.setLocation(x, y);
        }
    }
    
    public boolean isGameOver() {
    	return gameover;
    }
    
    public void gameOver() {
    	this.gameover = true;
    	this.setIcon(new ImageIcon("graphics/playerEnd.png"));
    }
    
    public void handleError(String msg) { // ���� ó��
		System.out.println(msg);
		System.exit(1);
	}
    
    class MoveThread extends Thread {
        public void run() {
            while(!gameover) {
                try {
                    move();
                    board.repaint();
                    Thread.sleep(10);
                } catch (Exception e) {
                    handleError(e.getMessage());
                }
            }
        }
    }
}
