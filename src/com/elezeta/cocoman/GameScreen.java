package com.elezeta.cocoman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;



class GameScreen extends JPanel {
  
  Game game;

  
  JLabel label;

  
  int muststep = 0;

   
  char nextheading = 'q';

  
  char lastheading = '>';

  
  public void setHeading(char newdir,boolean removePause) {
    nextheading = newdir;
    if ((game.pause) && (removePause))
      game.pause = !game.pause;
    if (muststep != 0)
      return;
    if ((game.deaduntil>game.stepnum) || (game.gameover) || (game.youwin))
      return;
    switch (newdir) {
      case '^':
      game.setDireccionArriba();
      break;
      case 'V':
      game.setDireccionAbajo();
      break;
      case '<':
      game.setDireccionIzquierda();
      break;
      case '>':
      game.setDireccionDerecha();
      break;
    }
  }

  
  public void newGame() {
    game = new Game();
  }

  
  public void setPause() {
    if ((game.deaduntil>game.stepnum) || (game.gameover) || (game.youwin))
      return;
    game.pause = !game.pause;
  }

  
  public GameScreen(JLabel labelasociada) {
    label = labelasociada;
    game = new Game();
    this.setPreferredSize(new Dimension(496, 496));
  }

  
  public void run() {
    int i;
    if ((game.deaduntil>game.stepnum) || (game.gameover) || (game.youwin)) {
      muststep = 0;
    }
    String text = new String();
    if (game.deaduntil>game.stepnum)
      text = "YOU'RE DEAD";
    else if (game.gameover)
      text = "GAME OVER";
    else if (game.youwin)
      text = "YOU WON";
    else if (game.pause)
      text = "PAUSE";
    else
      muststep = (muststep+1)%4;
    label.setText("Points: "+game.points+" Cocomen: "+game.lives+"  "+text);
    repaint();
    if (muststep == 0) {
      game.step();
      if (nextheading != 'q') {
        setHeading(nextheading,false);
      }
    }
  }

  
  public void paint(Graphics g) {
    super.paint(g);
    int x,y,i;

    for (y = 1;y < 32;y++) {
      for (x = 1;x < 32;x++) {
        switch (game.getContent(x,y)) {
          case '#':
          drawItem(g,x,y,'#');
          break;
          case '_':
          drawItem(g,x,y,'_');
          break;
          case '.':
          drawItem(g,x,y,'.');
          break;
          case ',':
          drawItem(g,x,y,',');
          break;
          default:
          drawItem(g,x,y,' ');
        }
      }
    }
    drawItem(g,game.man.x,game.man.y,'C');
    for (i = 0;i < 4;i++) {
      if (!game.ghosts[i].alive) {
        switch (game.ghosts[i].id) {
          case '1':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'a');
          break;
          case '2':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'b');
          break;
          case '3':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'c');
          break;
          case '4':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'d');
          break;
        }
      }
      else if ((game.getSupercome()) && (!game.ghosts[i].immune)) {
        switch (game.ghosts[i].id) {
          case '1':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'5');
          break;
          case '2':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'6');
          break;
          case '3':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'7');
          break;
          case '4':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'8');
          break;
        }
      }
      else {
        switch (game.ghosts[i].id) {
          case '1':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'1');
          break;
          case '2':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'2');
          break;
          case '3':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'3');
          break;
          case '4':
            drawItem(g,game.ghosts[i].x,game.ghosts[i].y,'4');
          break;
        }
      }
    }
  }

  
  public void drawItem(Graphics g,int x,int y,char item) {
    int startx = (x-1)*16;
    int starty = (y-1)*16;
    boolean movil = false;
    char movildir = ' ';
    switch (item) {
      case 'C':
        movil = true;
        movildir = game.man.heading;
      break;
    }
    if (movil) {
      switch (movildir) {
        case '<':
          startx -= muststep*4;
        break;
        case '>':
          startx += muststep*4;
        break;
        case '^':
          starty -= muststep*4;
        break;
        case 'V':
          starty += muststep*4;
        break;
      }
    }
    char aux = game.man.heading;
    if (aux != 'q')
      lastheading = aux;
    switch (item) {
      case '1':
        g.setColor(new Color(0,255,0));
        g.fillRect(startx+3,starty+3,10,10);
        g.setColor(new Color(0,0,0));
        g.fillRect(startx+4,starty+4,2,4);
        g.fillRect(startx+9,starty+4,2,4);
        g.setColor(new Color(255,255,255));
        g.drawRect(startx+4,starty+4,2,4);
        g.drawRect(startx+9,starty+4,2,4);
      break;
      case '2':
        g.setColor(new Color(0,0,255));
        g.fillRect(startx+3,starty+3,10,10);
        g.setColor(new Color(0,0,0));
        g.fillRect(startx+4,starty+4,2,4);
        g.fillRect(startx+9,starty+4,2,4);
        g.setColor(new Color(255,255,255));
        g.drawRect(startx+4,starty+4,2,4);
        g.drawRect(startx+9,starty+4,2,4);
      break;
      case '3':
        g.setColor(new Color(255,0,0));
        g.fillRect(startx+3,starty+3,10,10);
        g.setColor(new Color(0,0,0));
        g.fillRect(startx+4,starty+4,2,4);
        g.fillRect(startx+9,starty+4,2,4);
        g.setColor(new Color(255,255,255));
        g.drawRect(startx+4,starty+4,2,4);
        g.drawRect(startx+9,starty+4,2,4);
      break;
      case '4':
        g.setColor(new Color(255,255,0));
        g.fillRect(startx+3,starty+3,10,10);
        g.setColor(new Color(0,0,0));
        g.fillRect(startx+4,starty+4,2,4);
        g.fillRect(startx+9,starty+4,2,4);
        g.setColor(new Color(255,255,255));
        g.drawRect(startx+4,starty+4,2,4);
        g.drawRect(startx+9,starty+4,2,4);
      break;
      case '5':
      case '6':
      case '7':
      case '8':
        g.setColor(new Color(255,255,255));
        g.fillRect(startx+3,starty+3,10,10);
        g.setColor(new Color(0,0,0));
        g.fillRect(startx+4,starty+4,2,4);
        g.fillRect(startx+9,starty+4,2,4);
        g.setColor(new Color(0,0,255));
        g.drawRect(startx+4,starty+4,2,4);
        g.drawRect(startx+9,starty+4,2,4);
      break;
      case ' ':
        g.setColor(new Color(0,0,0));
        g.fillRect(startx,starty,16,16);
      break;
      case 'a':
      case 'b':
      case 'c':
      case 'd':
        g.setColor(new Color(0,0,0));
        g.fillRect(startx+4,starty+4,2,4);
        g.fillRect(startx+9,starty+4,2,4);
        g.setColor(new Color(255,255,255));
        g.drawRect(startx+4,starty+4,2,4);
        g.drawRect(startx+9,starty+4,2,4);
      break;
      case 'C':
        g.setColor(new Color(0,255,0));
        g.fillRect(startx+3,starty+3,10,10);
        g.setColor(new Color(0,0,0));
        switch (lastheading) {
          case '<':
          if (muststep < 2)
            g.fillRect(startx+3,starty+6,4,4);
          g.fillRect(startx+3,starty+7,4,2);
          break;
          case 'V':
          if (muststep < 2)
            g.fillRect(startx+6,starty+10,4,4);
          g.fillRect(startx+7,starty+9,2,4);
          break;
          case '^':
          if (muststep < 2)
            g.fillRect(startx+6,starty+2,4,4);
          g.fillRect(startx+7,starty+3,2,4);
          break;
          default:
          if (muststep < 2)
            g.fillRect(startx+10,starty+6,4,4);
          g.fillRect(startx+9,starty+7,4,2);
          break;
        }
      break;
      case '#':
        g.setColor(new Color(255,255,0));
        g.fillRect(startx,starty,16,16);
      break;
      case '_':
        g.setColor(new Color(0,0,0));
        g.fillRect(startx,starty,16,8);
        g.setColor(new Color(255,0,0));
        g.fillRect(startx,starty+8,16,8);
      break;
      case '.':
        g.setColor(new Color(0,0,0));
        g.fillRect(startx,starty,16,16);
        g.setColor(new Color(255,255,255));
        g.fillRect(startx+7,starty+7,2,2);
      break;
      case ',':
        g.setColor(new Color(0,0,0));
        g.fillRect(startx,starty,16,16);
        g.setColor(new Color(255,255,255));
        g.fillRect(startx+5,starty+5,6,6);
      break;
    }
  }
}
