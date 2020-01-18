package com.dodge.main.enemy;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dodge.main.Player;

@SuppressWarnings("serial")
public class Green extends JLabel {
	
	private JPanel board = null;
	private Player player = null;
	private int point = 0;
	
	public Green(JPanel board, Player player) {
		this.setIcon(new ImageIcon("graphics/green.png"));
		this.setSize(40, 40);
		this.player = player;
		setStartPoint();
		this.board = board;
		board.add(this);
    	MoveThread moveThread = new MoveThread();
    	moveThread.start();
	}
	
	public void setStartPoint() {
		int x = player.getX();
		int y = player.getY();
		if(x < 180) {
			if(y < 180) {
				this.setLocation(280, 280);
				point = 3;
			} else {
				this.setLocation(280, 80);
				point = 2;
			}
		} else {
			if(y < 180) {
				this.setLocation(80, 280);
				point = 4;
			} else {
				this.setLocation(80, 80);
				point = 1;
			}
		}
	}
	
	public void move() {
		int m = 5;
		int x = this.getX();
		int y = this.getY();
		switch(point) {
		case 1:
			this.setLocation(x+m, y);
			if(x+m==280) { point = 2; }
			break;
		case 2:
			this.setLocation(x, y+m);
			if(y+m==280) { point = 3; }
			break;
		case 3:
			this.setLocation(x-m, y);
			if(x-m==80) { point = 4; }
			break;
		case 4:
			this.setLocation(x, y-m);
			if(y-m==80) { point = 1; }
			break;
		}
	}
	
	public void touchCheck() {
		int px = player.getX(); int py = player.getY();
		int tx = this.getX(); int ty = this.getY();
		
		if((px>=tx&&px-tx<40||tx>=px&&tx-px<40)&&(py>=ty&&py-ty<40||ty>=py&&ty-py<40)) {
			player.gameOver();
		}
	}
	
	public void handleError(String msg) { // 오류 처리
		System.out.println(msg);
		System.exit(1);
	}
    
    class MoveThread extends Thread { 
        public void run() {
            while(!player.isGameOver()) {
                try {
                	touchCheck();
                	move();
                    board.repaint();
                    Thread.sleep(15);
                } catch (Exception e) {
                    handleError(e.getMessage());
                }
            }
        }
    }
}
