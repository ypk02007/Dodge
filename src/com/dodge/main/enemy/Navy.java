package com.dodge.main.enemy;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dodge.main.Player;

@SuppressWarnings("serial")
public class Navy extends JLabel {
	
	private JPanel board = null;
	private Player player = null;
	private int rx;
	private int ry;
	
	public Navy(JPanel board, Player player) {
		this.setIcon(new ImageIcon("graphics/navy.png"));
		this.setSize(40, 40);
		this.player = player;
		setStartPoint();
		this.board = board;
		board.add(this);
		setStartPoint();
    	MoveThread moveThread = new MoveThread();
    	moveThread.start();
	}
	
	public void setStartPoint() {
		int x = player.getX();
		int y = player.getY();
		if(x < 180) {
			if(y < 180) {
				this.setLocation(280, 280);
				rx = -3; ry = 3;
			} else {
				this.setLocation(280, 80);
				rx = -3; ry = -3;
			}
		} else {
			if(y < 180) {
				this.setLocation(80, 280);
				rx = 3; ry = 3;
			} else {
				this.setLocation(80, 80);
				rx = 3; ry = -3;
			}
		}
	}
	
	public void diffuseReflection(int news) {
		double ranX = Math.random();
		double ranY = Math.random();

		// 2~4 무작위
		rx = (int) (ranX * 3) + 2;
		ry = (int) (ranY * 3) + 2;
		switch(news) {
		case 1:
			rx = -rx; break;
		case 2:
			ry = -ry; break;
		case 3:
			break;
		case 4:
			rx = -rx; ry = -ry; break;
		}
	}
	
	public void move() {
		int x = this.getX();
		int y = this.getY();
		if(x<0) {
			diffuseReflection(3);
		} else if(x>355) {
			diffuseReflection(4);
		} else if(y<0) {
			diffuseReflection(1);
		} else if(y>330) {
			diffuseReflection(2);
		}
		this.setLocation(x+rx, y+ry);
	}
	
	public void touchCheck() {
		int px = player.getX(); int py = player.getY();
		int tx = this.getX(); int ty = this.getY();
		
		if((px>=tx&&px-tx<35||tx>=px&&tx-px<35)&&(py>=ty&&py-ty<35||ty>=py&&ty-py<35)) {
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
                    Thread.sleep(10);
                } catch (Exception e) {
                    handleError(e.getMessage());
                }
            }
        }
    }
}
