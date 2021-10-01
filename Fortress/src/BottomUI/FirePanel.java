package BottomUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class FirePanel extends JPanel implements Runnable {
   private int misStartX;
   private int EmisStartX;
   private int misStartY;
   private int EmisStartY;
   private double gauge = Math.PI;
   private final double gaugeDIF = Math.PI / 90;
   private final double gaugeBarRadius = 30;
   private double missilePower = gauge * 1.8;
   private double cannonAngleDIF = Math.PI / 60;
   private double cannonAngle = Math.PI / 4;
   private double missileDx = missilePower * Math.cos(cannonAngle);
   private double missileDy = missilePower * Math.sin(cannonAngle);
   private double missileAngleX;
   private double missileAngleY;
   private final double GRAVITY = 0.098;
   private Vector<Integer> missileX = new Vector<Integer>();
   private Vector<Integer> missileY = new Vector<Integer>();
   private int cc = 0;
   private int gaugeCount = 0;
   private ImageIcon mis = new ImageIcon("../Fortress/src/img/missile.png");
   private ImageIcon bigMisRed = new ImageIcon("../Fortress/src/img/missileRed.png");
   private ImageIcon bigMisBlue = new ImageIcon("../Fortress/src/img/missileBlue.png");
   private Image ms;
   private boolean mine = false;
   private String team;
   private MainPanel mp;
   private Image boomBuffer;
   private Image[] boom;
   private Toolkit tool;
   private int num = 0;
   private boolean visible = false;
   private String missileSort = "Normal";
   private double rad = 0;

   FirePanel(String team, MainPanel mp) {
      tool = Toolkit.getDefaultToolkit();
      initImage();

      this.mp = mp;
      this.team = team;
      init();
      this.setSize(1280, 800);
      this.setOpaque(false);
   }

   // boomImg 초기화
   public void initImage() {
      boom = new Image[8];

      for (int i = 0; i < boom.length; i++) {
         boom[i] = tool.getImage(String.format("../Fortress/src/img/boom%d.png", i + 1));
      }
   }

   public void fillGaugeCount() {
      gaugeCount++;
   }

   public int getGauge() {
      return this.gaugeCount;
   }

   @Override
   public void paint(Graphics g) {

      Graphics2D gg = (Graphics2D) g;
      if (cc == missileX.size() - 1 && visible == true) {
         ms = null;
         g.drawImage(boom[num], missileX.elementAt(cc) - 100, missileY.elementAt(cc) - 170, this);
      } else {
         if (rad < 2.0) {
            if (team.equals("red") && mine == true) {
               gg.rotate(rad, missileX.elementAt(cc), missileY.elementAt(cc));
               rad = rad + 0.015;
            }
            else if(team.equals("red") && mine == false) {
            	gg.rotate(rad, missileX.elementAt(cc), missileY.elementAt(cc));
                rad = rad - 0.015;
            }
            else if (team.equals("blue") && mine == true) {
               gg.rotate(rad, missileX.elementAt(cc), missileY.elementAt(cc));
               rad = rad - 0.015;
            }
            else if (team.equals("blue") && mine == false) {
                gg.rotate(rad, missileX.elementAt(cc), missileY.elementAt(cc));
                rad = rad + 0.015;
             }
         } else {
            gg.rotate(rad, missileX.elementAt(cc), missileY.elementAt(cc));
         }
         gg.drawImage(ms, missileX.elementAt(cc) - 90, missileY.elementAt(cc) - 51, this);
      }

   }


   @Override
   public void update(Graphics g) {
      paint(g);
   }

   public void drawPower(int x, int y, String missileSort) { // Power 유지
      this.missileSort = missileSort;
      mine = true;
      misStartX = x;
      misStartY = y;
      missileX.removeAllElements();
      missileY.removeAllElements();
      for (int i = 0; i < gaugeCount; i++) {
         if (gauge < Math.PI * 2)
            gauge += gaugeDIF;
         else
            break;
      }
      missilePower = gauge * 1.8;
      missileDx = missilePower * Math.cos(cannonAngle);
      missileDy = missilePower * Math.sin(cannonAngle);
      this.missileAngleX = missileDx;
      this.missileAngleY = missileDy;
      missileX.add(misStartX);
      missileY.add(misStartY);
      makeMyArc();
      makeThread();
   }

   public void initWithoutAngle() {
      missileX.removeAllElements();
      missileY.removeAllElements();
      gauge = Math.PI;
      missilePower = gauge * 1.8;
      missileDx = missilePower * Math.cos(cannonAngle);
      missileDy = missilePower * Math.sin(cannonAngle);
      cc = 0;
      rad = 0;
      gaugeCount = 0;
      missileX.add(misStartX);
      missileY.add(misStartY);
      if (team.equals("red"))
         drawRedArc();
      else if ((team.equals("blue")))
         drawBlueArc();
   }

   public void init() {
      mine = false;
      missileX.removeAllElements();
      missileY.removeAllElements();
      gauge = Math.PI;
      missilePower = gauge * 1.8;
      cannonAngleDIF = Math.PI / 60;
      cannonAngle = Math.PI / 4;
      missileDx = missilePower * Math.cos(cannonAngle);
      missileDy = missilePower * Math.sin(cannonAngle);
      rad = 0;
      cc = 0;
      gaugeCount = 0;
      missileX.add(10000);
      missileY.add(10000);
   }

   public void drawEnemyPower(int gaugeCount, double missileAngleX, double missileAngleY, int x, int y,
         String missileSort) { // Power 유지
      this.missileSort = missileSort;
      mine = false;
      missileX.removeAllElements();
      missileY.removeAllElements();
      this.gaugeCount = gaugeCount;
      misStartX = x;
      misStartY = y;
      missileX.add(misStartX);
      missileY.add(misStartY);
      for (int i = 0; i < gaugeCount; i++) {
         if (gauge < Math.PI * 2)
            gauge += gaugeDIF;
         else
            break;
      }
      missilePower = gauge * 1.8;
      this.missileDx = missileAngleX;
      this.missileDy = missileAngleY;
      makeEnemyArc();
      checkHitted();
      makeThread();
   }

   public void checkHitted() {
      int xy[] = mp.getEnemyPosition();
      for (int i = 0; i < missileX.size(); i++) {
         if ((xy[0] + 80 > missileX.elementAt(i) && xy[0] < missileX.elementAt(i))
               && (xy[1] < missileY.elementAt(i) && xy[1] + 60 > missileY.elementAt(i))) {
            for (int j = missileX.size() - 1; j > i; j--) {
               missileX.remove(j);
               missileY.remove(j);
            }
            break;
         }
      }
   }

   public void makeMyArc() { // 내턴이 이미 true라는 가정하에
      if (team.equals("red")) {
         drawRedArc();
         if (mp.isHitted() == true)
            removeHitVec();
      } else if (team.equals("blue")) {
         drawBlueArc();
         if (mp.isHitted() == true)
            removeHitVec();
      }

   }

   public void makeEnemyArc() {
      if (team.equals("red")) {
         drawBlueArc();
      } else if (team.equals("blue")) {
         drawRedArc();
      }
   }

   public void removeHitVec() {
      for (int i = missileX.size() - 1; i > mp.hitIndex(); i--) {
         missileX.remove(i);
         missileY.remove(i);
      }
   }

   public void drawBlueArc() {
      for (int i = 0; i < 500; i++) {
         missileDy -= GRAVITY;
         if ((int) (missileY.elementAt(i) - missileDy) > 465)
            break;
         missileX.add((int) (missileX.elementAt(i) - missileDx));
         missileY.add((int) (missileY.elementAt(i) - missileDy));
      }
   }

   public void drawRedArc() {
      for (int i = 0; i < 500; i++) {
         missileDy -= GRAVITY;
         if ((int) (missileY.elementAt(i) - missileDy) > 465)
            break;
         missileX.add((int) (missileX.elementAt(i) + missileDx));
         missileY.add((int) (missileY.elementAt(i) - missileDy));
      }
   }

   public void makeThread() {
      new Thread(this).start();
   }

   @Override
   public void run() {
      if (missileSort.equals("Normal"))
         ms = mis.getImage();
      else if (missileSort.equals("Big") && team.equals("red") && mine == true)
         ms = bigMisRed.getImage();
      else if (missileSort.equals("Big") && team.equals("blue") && mine == false)
         ms = bigMisRed.getImage();
      else if (missileSort.equals("Big") && team.equals("blue") && mine == true)
          ms = bigMisBlue.getImage();
       else if (missileSort.equals("Big") && team.equals("red") && mine == false)
          ms = bigMisBlue.getImage();
      for (int i = 0; i < missileX.size(); i++) {
         try {
            Thread.sleep(15);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         cc = i;
         repaint();
      }
      visible = true;
      for (int i = 0; i < boom.length; i++) {
         try {
            Thread.sleep(75);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         num = i;
         repaint();
      }
      visible = false;
      if (mine == true) {
         mp.sendHitMessage();
      }
   }

   public void angleUp() {
      if (cannonAngle <= Math.PI / 2)
         cannonAngle += cannonAngleDIF;
   }

   public void angleDown() {
      if (cannonAngle >= 0)
         cannonAngle -= cannonAngleDIF;
   }

   public double getAngleX() {

      return this.missileAngleX;
   }

   public double getAngleY() {

      return this.missileAngleY;
   }

   public Vector<Integer> getVectorX() {
      return missileX;
   }

   public Vector<Integer> getVectorY() {
      return missileY;
   }
}