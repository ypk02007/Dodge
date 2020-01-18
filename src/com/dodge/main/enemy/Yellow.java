package com.dodge.main.enemy;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dodge.main.Player;

@SuppressWarnings("serial")
public class Yellow extends JLabel {
	private JPanel board = null;
	private Player player = null;
	private int targetX;
	private int targetY;
	private int mx;
	private int my;
	
	public Yellow(JPanel board, Player player) {
		this.setIcon(new ImageIcon("graphics/yellow.png"));
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
	
	public void lockOn() {
		targetX = player.getX();
		targetY = player.getY();
		int x = this.getX();
		int y = this.getY();
		int distX = targetX - x;
		int distY = targetY - y;
		
		mx = distX/4;
		my = distY/4;
	}
	
	public void vroom() {
		int x = this.getX();
		int y = this.getY();
		
		double ranX = Math.random();
		double ranY = Math.random();

		int rx = (int) (ranX * 2);
		int ry = (int) (ranY * 2);
		
		rx = (rx==1) ? -1 : 1;
		ry = (ry==1) ? -1 : 1;

		this.setLocation(x+rx, y+ry);
	}
	
	public void move() {
		int x = this.getX();
		int y = this.getY();
		this.setLocation(x+mx, y+my);
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
        	int coolTime = 0;
            while(!player.isGameOver()) {
                try {
                	touchCheck();
                	coolTime++;
                	if(coolTime==200) { // 2초간 대기 후 목표 설정
                		lockOn();
                	} else if(coolTime>200 && coolTime<240) { // 공격 준비 자세
                		vroom();
                	} else if(coolTime>=240 && coolTime<244) { // 공격
                		move();
                	} else if(coolTime>=244) { // 대기모드로 전환
                		coolTime = 0;
                	}
                    board.repaint();
                    Thread.sleep(10);
                } catch (Exception e) {
                    handleError(e.getMessage());
                }
            }
        }
    }
}
