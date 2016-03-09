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
	//构造函数
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

//定义我的画板
class MyPanel extends Panel implements KeyListener,Runnable{
	HeroTank ht=null;
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int tankNumbers=3;
	public MyPanel(int x,int y) {
		ht=new HeroTank(x, y);
		for (int i = 0; i < tankNumbers; i++) {
			//启动敌人坦克
			EnemyTank et=new EnemyTank((i+1)*50, 0);
			et.setType(1);
			et.setDirect(2);
			Thread tt=new Thread(et);
			tt.start();
			//加入子弹
			Shot shotes=new Shot(et.x, et.y, 2);
			Thread sThread=new Thread(shotes);
			sThread.start();
			ets.add(et);
		}
	}
	//重写paint方法
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 350, 350);
		if (ht.isActive) {
			this.paintTank(ht.getX(), ht.getY(), g, 0, ht.getDirect());
		}
		//画出子弹
		for (int i = 0; i < this.ht.ss.size(); i++) {
			Shot myShot=this.ht.ss.get(i);
			if (myShot!=null && myShot.isActive==true) 
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			if(myShot.isActive==false){
				this.ht.ss.remove(myShot);
			}
		}
		
		//画出敌人坦克
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank eTank=ets.get(i);
			if (eTank.isActive) {
				this.paintTank(eTank.getX(), eTank.getY(), g, eTank.getType(), eTank.getDirect());		
				//画出子弹
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
	//消灭敌人坦克
	public void hitTank(Shot shot,Tank et) {
		switch (et.direct) {
		case 0:
		case 2:
			if (shot.x>et.getX() && shot.x<et.getX()+20 && shot.y>et.getY() && shot.y<et.getY()+30) {
				//命中坦克
				shot.isActive=false;		//子弹消失
				et.isActive=false;			//坦克消失
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
	//画出坦克
	public void paintTank(int x,int y,Graphics g,int type,int direct) {
		//判断坦克类型
		switch (type) {
		case 0:
			g.setColor(Color.GRAY);
			break;
		case 1:
			g.setColor(Color.CYAN);
		default:
			break;
		}
		//判断方向
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
		//开火
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
			//判断是否集中敌人坦克
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
			//判断我是否被击中
			for (int i = 0; i < this.ets.size(); i++) {
				//取出坦克
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
	//坦克横坐标
	int x=0;
	//坦克纵坐标
	int y=0;
	//坦克方向    0表示上  1表示右  2表示下   3表示左
	int direct=0;
	//坦克速度
	int speed=1;
	//坦克类型	 0代表我的坦克   1代表敌人的坦克
	int type=0;
	//坦克是否还活着
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
	//构造函数
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
//我的坦克
class HeroTank extends Tank{
	//我的子弹
	Vector<Shot> ss=new Vector<Shot>();
	Shot shot=null;
	public HeroTank(int x,int y) {
		super(x, y);
	}
	//发射子弹
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
		//启动子弹线程
		Thread thread=new Thread(shot);
		thread.start();
	}
	//坦克向上移动
	public void moveUp() {
		this.y-=this.speed;
	}
	//坦克向下移动
	public void moveDown() {
		this.y+=this.speed;
	}
	//坦克向左移动
	public void moveLeft() {
		this.x-=this.speed;
	}
	//坦克向右移动
	public void moveRight() {
		this.x+=this.speed;
	}
}
//敌人的坦克
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
						//启动子弹线程
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
//我的子弹
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
			//子弹死亡
			if (x<0 || x>350 || y<0 || y>350) {
				isActive=false;
				break;
			}
		}
	}
}
