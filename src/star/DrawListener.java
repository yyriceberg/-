package star;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

public class DrawListener implements MouseListener,MouseMotionListener{
	public Graphics2D g;  
    public int x1,y1,x2,y2,ox,oy,x3,y3;  
    public ButtonGroup bg;  
    public String command;  
    public Color color;  
    public DrawBorder db;  
    public boolean flag=true;  
      
    public static final  Stroke s1 = new BasicStroke(1);  
    public static final  Stroke s2 = new BasicStroke(10);  
    public static final  Stroke s3 = new BasicStroke(15);  
      
    public Random r =new Random();  
    //构造函数1  
    public DrawListener(Graphics g1){  
    	
        g=(Graphics2D)g1;  
    }  
      
    //构造函数2  
    public DrawListener(Graphics g2, ButtonGroup bg2) {  
        g=(Graphics2D)g2;  
        bg=bg2;  
    }  
  
    //构造函数3  
    public DrawListener(Graphics g2, ButtonGroup bg2, DrawBorder db1) {  
        g=(Graphics2D)g2;  
        bg=bg2;  
        db=db1;  
    }  
  
    //鼠标按下事件监听  
    public void mousePressed(MouseEvent e) {  
        //获取鼠标按下点的坐标  
        x1=e.getX();  
        y1=e.getY();  
          
      
          
        //判断选择的是左面板中的那个按钮被选中（前面已经设置每个按钮的名称了）    
        ButtonModel bm=bg.getSelection();//拿到按钮组中被选中的按钮    
        command=bm.getActionCommand();//拿到选中按钮的名字    
         
    }  
  
    public void mouseReleased(MouseEvent e) {    
        //获取鼠标释放的坐标    
        x2=e.getX();    
        y2=e.getY();    
          
          
        //如果选中的是绘制直线的按钮，那么根据鼠标按下点的坐标和释放点的左边绘制直线（两点确定一条直线）    
        if("pic10".equals(command))    
        {    
            g.drawLine(x1, y1, x2, y2);    
        }//同理选中的是矩形按钮，那么绘制矩形（这里有绘制矩形的纠正，不纠正的话从右下角往左上角方向绘制矩形会出现问题，参看后面难点解析）    
        else if("pic12".equals(command)){    
            g.drawRect(Math.min(x2, x1),Math.min(y2, y1), Math.abs(x2-x1),Math.abs(y1-y2));    
        }//绘制椭圆    
        else if("pic14".equals(command)){    
            g.drawOval(Math.min(x2, x1),Math.min(y2, y1), Math.abs(x2-x1),Math.abs(y1-y2));    
        } else if("pic15".equals(command)){  
            g.drawRoundRect(Math.min(x2, x1),Math.min(y2, y1), Math.abs(x2-x1),Math.abs(y1-y2),40,40);    
        }//绘制曲线  
        else if("pic13".equals(command)){  
              
            //第一次画直线，设置标志  
            if(flag){  
                g.drawLine(x1, y1, x2, y2);   
                flag=false;  
              //记录这次鼠标释放的坐标，作为下次绘制直线的起点  
                x3=x2;  
                y3=y2;  
                //记录第一点击的坐标，绘制封闭的曲线  
                ox=x1;  
                oy=y1;  
            }  
            else{  
                 g.drawLine(x3, y3, x2, y2);  
                  //记录上次鼠标释放的坐标  
                 x3=x2;  
                 y3=y2;   
            }  
        }  
        //取色功能  
        else if("pic4".equals(command)){  
              
            //拿到相对面板的那个坐标  
            int x=e.getXOnScreen();  
            int y=e.getYOnScreen();  
              
            try {  
                  
                Robot robot = new Robot();//Robot类的使用  
                  
                //拿到坐标点的那个矩形  
                Rectangle rect = new Rectangle(x,y,1,1);  
                //生成该矩形的缓冲图片  
                BufferedImage bi =robot.createScreenCapture(rect);  
                //得到图片的背景颜色  
                int  c =bi.getRGB(0, 0);  
                //将该颜色进行封装  
                Color color = new Color(c);  
                //将取色笔取来的图片设置成画笔的颜色  
                db.c=color;  
            } catch (AWTException e1) {  
                e1.printStackTrace();  
            }  
        }  
            
    }    
  
    public void mouseClicked(MouseEvent e) {  
        //多边形图形双击封闭  
        int count =e.getClickCount();  
        if(count==2 && "pic13".equals(command)){  
            g.drawLine(ox, oy, x2, y2);  
            flag=true;  
        }  
    }  
  
    public void mouseEntered(MouseEvent e) {  
            color=db.c;//设置画笔颜色    
            g.setColor(color);  
            g.setStroke(s1);  
    }  
  
    public void mouseExited(MouseEvent e) {  
          
    }  
  
    public void mouseDragged(MouseEvent e) {  
          
        int x=e.getX();  
        int y=e.getY();  
          
        //画笔功能  
        if("pic6".equals(command)){  
            g.drawLine(x1, y1, x, y);  
            x1=x;  
            y1=y;  
        }  
        //橡皮擦功能  
        else if("pic2".equals(command)){  
            db.c=Color.white;  
            g.setColor(db.c);  
            g.setStroke(s3);  
            g.drawLine(x1, y1, x, y);  
            x1=x;  
            y1=y;  
        }  
        //刷子功能  
        else if("pic7".equals(command)){  
            g.setStroke(s2);//设置画笔 粗细  
            g.drawLine(x1, y1, x, y);  
            x1=x;  
            
            y1=y;  
        }  
        //喷桶功能  
        else if("pic8".equals(command)){  
            //随机产生30个-15到15之间的整数  
            for (int i = 0; i < 30; i++) {  
                int xp=r.nextInt(31)-15;  
                int yp=r.nextInt(31)-15;  
                //在x，y附件绘制原点  
                g.drawLine(x+xp, y+yp, x+xp, y+yp);  
            }  
              
        }  
          
          
    }  
  
    public void mouseMoved(MouseEvent e) {  
    	}
    }