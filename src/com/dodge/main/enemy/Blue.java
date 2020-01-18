package com.dodge.main.enemy;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dodge.main.Player;

@SuppressWarnings("serial")
public class Blue extends JLabel {
	
	private JPanel board = null;
	private Player player = null;
	private boolean stealthFlag = false;
	
	public Blue(JPanel board, Player player) {
		this.setIcon(new ImageIcon("graphics/blue.png"));
		this.setSize(40, 40);
		this.player = player;
		setStartPoint();
		this.board = board;
		board.add(this);
    	MoveThread moveThread = new MoveThread();
    	moveThread.start();
    	SizeThread sizeThread = new SizeThread();
    	sizeThread.start();
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
		int dis = this.getHeight();
		
		if((px>=tx&&px-tx<40||tx>=px&&tx-px<dis)&&(py>=ty&&py-ty<40||ty>=py&&ty-py<dis)) {
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
    
    void stealth() {
    	if(stealthFlag) {
    		this.setIcon(new ImageIcon("graphics/blue.png"));
    		stealthFlag = false;
    	} else {
    		this.setIcon(new ImageIcon("graphics/blueStealth.png"));
    		stealthFlag = true;
    	}
		board.repaint();
    }
    
    class SizeThread extends Thread { 
        public void run() {
            while(!player.isGameOver()) {
                try {
                	stealth();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    handleError(e.getMessage());
                }
            }
        }
    }
}
