package com.dodge.main.enemy;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dodge.main.Player;

@SuppressWarnings("serial")
public class Red extends JLabel {
	
	private JPanel board = null;
	private Player player = null;
	
	public Red(JPanel board, Player player) {
		this.setIcon(new ImageIcon("graphics/red.png"));
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
			} else {
				this.setLocation(280, 80);
			}
		} else {
			if(y < 180) {
				this.setLocation(80, 280);
			} else {
				this.setLocation(80, 80);
			}
		}
	}
	
	public void move() {
		int px = player.getX(); int py = player.getY();
		int tx = this.getX(); int ty = this.getY();
		int mx = 0; int my = 0;
		int m = 1;
		
		if(px < tx) {
			mx = -m;
		} else if(px > tx) {
			mx = m;
		}
		if(py < ty) {
			my = -m;
		} else if(py > ty) {
			my = m;
		}
		
		this.setLocation(tx+mx, ty+my);
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
