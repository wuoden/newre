package com.tank;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AllTank extends JFrame{
	MyPanel mp=null;
	public static void main(String[] args) {
		AllTank allTank=new AllTank();
	}
	//���캯��
	public AllTank() {
		mp=new MyPanel(170,310);
		this.add(mp);
		this.addKeyListener(mp);
		Thread thread=new Thread(mp);
		thread.start();
		this.setSize(500,450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}

//�����ҵĻ���
class MyPanel extends Panel implements KeyListener,Runnable{
	HeroTank ht=null;
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int tankNumbers=3;
	public MyPanel(int x,int y) {
		ht=new HeroTank(x, y);
		for (int i = 0; i < tankNumbers; i++) {
			//��������̹��
			EnemyTank et=new EnemyTank((i+1)*50, 0);
			et.setType(1);
			et.setDirect(2);
			Thread tt=new Thread(et);
			tt.start();
			//�����ӵ�
			Shot shotes=new Shot(et.x, et.y, 2);
			Thread sThread=new Thread(shotes);
			sThread.start();
			ets.add(et);
		}
	}
	//��дpaint����
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 350, 350);
		if (ht.isActive) {
			this.paintTank(ht.getX(), ht.getY(), g, 0, ht.getDirect());
		}
		//�����ӵ�
		for (int i = 0; i < this.ht.ss.size(); i++) {
			Shot myShot=this.ht.ss.get(i);
			if (myShot!=null && myShot.isActive==true) 
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			if(myShot.isActive==false){
				this.ht.ss.remove(myShot);
			}
		}
		
		//��������̹��
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank eTank=ets.get(i);
			if (eTank.isActive) {
				this.paintTank(eTank.getX(), eTank.getY(), g, eTank.getType(), eTank.getDirect());		
				//�����ӵ�
				for (int j = 0; j < eTank.sss.size(); j++) {
					Shot enemyShot=eTank.sss.get(j);
					if (enemyShot.isActive) {
						g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
					}else {
						eTank.sss.remove(enemyShot);
					}
				}
			}
		}
	}
	//�������̹��
	public void hitTank(Shot shot,Tank et) {
		switch (et.direct) {
		case 0:
		case 2:
			if (shot.x>et.getX() && shot.x<et.getX()+20 && shot.y>et.getY() && shot.y<et.getY()+30) {
				//����̹��
				shot.isActive=false;		//�ӵ���ʧ
				et.isActive=false;			//̹����ʧ
			}
			break;
		case 1:
		case 3:
			if (shot.x>et.getX() && shot.x<et.getX()+30 && shot.y>et.getY() && shot.y<et.getY()+20) {
				shot.isActive=false;
				et.isActive=false;
			}
			break;
		default:
			break;
		}
	}
	//����̹��
	public void paintTank(int x,int y,Graphics g,int type,int direct) {
		//�ж�̹������
		switch (type) {
		case 0:
			g.setColor(Color.GRAY);
			break;
		case 1:
			g.setColor(Color.CYAN);
		default:
			break;
		}
		//�жϷ���
		switch (direct) {
		case 0:
			g.fill3DRect(x, y-1, 5, 30, false);
			g.fill3DRect(x+15, y-1, 5, 30, false);
			g.fill3DRect(x+5, y+4, 10, 20, false);
			g.drawOval(x+5, y+9, 8, 8);
			g.drawLine(x+9, y+9, x+9, y-1);
			break;
		case 3:
			g.fill3DRect(x-1, y+5, 30, 5, false);
			g.fill3DRect(x-1, y+20, 30, 5, false);
			g.fill3DRect(x+4, y+10, 20, 10, false);
			g.drawOval(x+9, y+10, 8, 8);
			g.drawLine(x-1, y+14, x+9, y+14);
			break;
		case 2:
			g.fill3DRect(x, y+1, 5, 30, false);
			g.fill3DRect(x+15, y+1, 5, 30, false);
			g.fill3DRect(x+5, y+6, 10, 20, false);
			g.drawOval(x+5, y+11, 8, 8);
			g.drawLine(x+9, y+19, x+9, y+30);
			break;
		case 1:
			g.fill3DRect(x+1, y+5, 30, 5, false);
			g.fill3DRect(x+1, y+20, 30, 5, false);
			g.fill3DRect(x+6, y+10, 20, 10, false);
			g.drawOval(x+11, y+10, 8, 8);
			g.drawLine(x+19, y+14, x+29, y+14);
			break;
		default:
			break;
		}
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			this.ht.setDirect(0);
			if(this.ht.getY()>=0) this.ht.moveUp();
			break;
		case KeyEvent.VK_DOWN:
			this.ht.setDirect(2);
			if(this.ht.getY()<=318) this.ht.moveDown();
			break;
		case KeyEvent.VK_LEFT:
			this.ht.setDirect(3);
			if(this.ht.getX()>=0) this.ht.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			this.ht.setDirect(1);
			if(this.ht.getX()<=318) this.ht.moveRight();
			break;
		default:
			break;
		}
		//����
		if (e.getKeyCode()==KeyEvent.VK_SPACE) {
			if(this.ht.ss.size()<5){
				this.ht.shootEnmy();
			}
		}
		this.repaint();
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//�ж��Ƿ��е���̹��
			for (int i = 0; i < this.ht.ss.size(); i++) {
				Shot shots=this.ht.ss.get(i);
				if (shots.isActive) {
					for (int j = 0; j < ets.size(); j++) {
						EnemyTank ett=ets.get(j);
						if (ett.isActive) {
							this.hitTank(shots, ett);
						}
					}
				}
			}
			//�ж����Ƿ񱻻���
			for (int i = 0; i < this.ets.size(); i++) {
				//ȡ��̹��
				EnemyTank et=ets.get(i);
				for (int j = 0; j < et.sss.size(); j++) {
					Shot st=et.sss.get(j);
					this.hitTank(st, ht);
				}
			}
			this.repaint();
		}
	}
}

class Tank{
	//̹�˺�����
	int x=0;
	//̹��������
	int y=0;
	//̹�˷���    0��ʾ��  1��ʾ��  2��ʾ��   3��ʾ��
	int direct=0;
	//̹���ٶ�
	int speed=1;
	//̹������	 0�����ҵ�̹��   1������˵�̹��
	int type=0;
	//̹���Ƿ񻹻���
	boolean isActive=true;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	//���캯��
	public Tank(int x,int y) {
		this.x=x;
		this.y=y;
	}	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
}
//�ҵ�̹��
class HeroTank extends Tank{
	//�ҵ��ӵ�
	Vector<Shot> ss=new Vector<Shot>();
	Shot shot=null;
	public HeroTank(int x,int y) {
		super(x, y);
	}
	//�����ӵ�
	public void shootEnmy() {
		switch (direct) {
		case 3:
			shot=new Shot(x-1, y+13,3);
			ss.add(shot);
			break;
		case 0:
			shot=new Shot(x+8, y-1,0);
			ss.add(shot);
			break;
		case 1:
			shot=new Shot(x+29, y+13,1);
			ss.add(shot);
			break;
		case 2:
			shot=new Shot(x+8, y+29,2);
			ss.add(shot);
			break;
		default:
			break;
		}
		//�����ӵ��߳�
		Thread thread=new Thread(shot);
		thread.start();
	}
	//̹�������ƶ�
	public void moveUp() {
		this.y-=this.speed;
	}
	//̹�������ƶ�
	public void moveDown() {
		this.y+=this.speed;
	}
	//̹�������ƶ�
	public void moveLeft() {
		this.x-=this.speed;
	}
	//̹�������ƶ�
	public void moveRight() {
		this.x+=this.speed;
	}
}
//���˵�̹��
class EnemyTank extends Tank implements Runnable{
	Vector<Shot> sss=new Vector<Shot>();
	int times=0;
	public EnemyTank(int x,int y) {
		super(x,y);
	}
	public void run() {
		while (true) {
			switch (this.direct) {
			case 0:
				for (int i = 0; i < 20; i++) {
					if (this.y>0) 
						this.y-=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for (int i = 0; i < 20; i++) {
					if (this.x<318) 
						this.x+=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for (int i = 0; i < 20; i++) {
					if (this.y<318) 
						this.y+=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for (int i = 0; i < 20; i++) {
					if (this.x>0) 
						this.x-=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
			this.times++;
			if (this.times%2==0) {
				if (isActive) {
					if (sss.size()<4) {
						Shot shot=null;
						switch (direct) {
						case 3:
							shot=new Shot(x-1, y+13,3);
							sss.add(shot);
							break;
						case 0:
							shot=new Shot(x+8, y-1,0);
							sss.add(shot);
							break;
						case 1:
							shot=new Shot(x+29, y+13,1);
							sss.add(shot);
							break;
						case 2:
							shot=new Shot(x+8, y+29,2);
							sss.add(shot);
							break;
						default:
							break;
						}
						//�����ӵ��߳�
						Thread thread=new Thread(shot);
						thread.start();
					}
				}
			}
			this.direct=(int)(Math.random()*4);
			if (this.isActive==false) 
				break;
		}
	}		
}
//�ҵ��ӵ�
class Shot implements Runnable{
	int x=0;
	int y=0;
	int direct=0;
	int speed=2;
	boolean isActive=true;
	public Shot(int x,int y,int direct) {
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	public void run() {
		while(true){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (direct) {
			case 3:
				x-=speed;
				break;
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			default:
				break;
			}			
			//�ӵ�����
			if (x<0 || x>350 || y<0 || y>350) {
				isActive=false;
				break;
			}
		}
	}
}
