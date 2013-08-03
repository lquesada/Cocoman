package com.elezeta.cocoman;

import java.util.Random;



public class Game {

  
  char map[][];

  
  Man man;
  

  
  int stepnum = 0;

  
  int supercocomanuntil = 0;

  
  int points = 0;

  
  int lives = 3;

  
  int deaduntil = 0;

  
  int ghostsinthewild = 0;

  
  boolean pause = true;

  
  int pellets = 0;

  
  boolean youwin = false;

  
  boolean gameover = false;

  
  int eatenghosts = 0;

  
  Ghost ghosts[];

  
  Random rand;

  
  Game() {
    int x,y;
    ghosts = new Ghost[4];
    ghosts[0] = new Ghost('1');
    ghosts[1] = new Ghost('2');
    ghosts[2] = new Ghost('3');
    ghosts[3] = new Ghost('4');
    man = new Man();
    man.x = 15;
    man.y = 18;
    rand = new Random();
    map = new char[33][33];
    map[0] =  new String("#################################").toCharArray();
    map[1] =  new String("#################################").toCharArray();
    map[2] =  new String("##.............................##").toCharArray();
    map[3] =  new String("##.####.#####.##.##.#####.####.##").toCharArray();
    map[4] =  new String("##,####.#####.##.##.#####.####,##").toCharArray();
    map[5] =  new String("##.####.#####.##.##.#####.####.##").toCharArray();
    map[6] =  new String("##.............................##").toCharArray();
    map[7] =  new String("##.####.###.#########.###.####.##").toCharArray();
    map[8] =  new String("##.####.###.#########.###.####.##").toCharArray();
    map[9] =  new String("##......###....###....###......##").toCharArray();
    map[10] = new String("#######.###### ### ######.#######").toCharArray();
    map[11] = new String("      #.###### ### ######.#      ").toCharArray();
    map[12] = new String("      #.###           ###.#      ").toCharArray();
    map[13] = new String("      #.### ##_____## ###.#      ").toCharArray();
    map[14] = new String("#######.### #$^^^^^$# ###.#######").toCharArray();
    map[15] = new String("      $.    #$^^^^^$#    .$      ").toCharArray();
    map[16] = new String("#######.### #$^^^^^$# ###.#######").toCharArray();
    map[17] = new String("      #.### ######### ###.#      ").toCharArray();
    map[18] = new String("      #.###           ###.#      ").toCharArray();
    map[19] = new String("      #.### ######### ###.#      ").toCharArray();
    map[20] = new String("#######.### ######### ###.#######").toCharArray();
    map[21] = new String("##.............###.............##").toCharArray();
    map[22] = new String("##.#####.#####.###.#####.#####.##").toCharArray();
    map[23] = new String("##.#####.#####.###.#####.#####.##").toCharArray();
    map[24] = new String("##,..###.................###..,##").toCharArray();
    map[25] = new String("####.###.##.#########.##.###.####").toCharArray();
    map[26] = new String("####.###.##.#########.##.###.####").toCharArray();
    map[27] = new String("##.......##....###....##.......##").toCharArray();
    map[28] = new String("##.###########.###.###########.##").toCharArray();
    map[29] = new String("##.###########.###.###########.##").toCharArray();
    map[30] = new String("##.............................##").toCharArray();
    map[31] = new String("#################################").toCharArray();
    map[32] = new String("#################################").toCharArray();
    for (y = 1;y < 32;y++) {
      for (x = 1;x < 32;x++) {
        if ((map[y][x] == '.') || (map[y][x] == ','))
          pellets++;
      }
    }
  }

  
  public boolean getSupercome() {
    if (supercocomanuntil>stepnum) {
      if (supercocomanuntil-8<stepnum) {
        if (stepnum%2 == 1)
          return true;
        else
          return false;
      }
      return true;
    }
    else {
      eatenghosts = 0;
      return false;
    }
  }
  
  public void setDireccionArriba() {
    if ((map[man.y-1][man.x] == '#') || (map[man.y-1][man.x] == '_')) {
    }
    else {
      man.heading = '^';
    }
  }

  
  public void setDireccionAbajo() {
    if ((map[man.y+1][man.x] == '#') || (map[man.y+1][man.x] == '_')) {
    }
    else {
    	man.heading = 'V';
    }
  }

  
  public void setDireccionIzquierda() {
    if ((map[man.y][man.x-1] == '#') || (map[man.y][man.x-1] == '_')) {
    }
    else {
    	man.heading = '<';
    }
  }

  
  public void setDireccionDerecha() {
    if ((map[man.y][man.x+1] == '#') || (map[man.y][man.x+1] == '_')) {
    }
    else {
      man.heading = '>';
    }
  }


  
  public char getContent(int x,int y) {
    if (map[y][x] == '#')
      return '#';
    else if (map[y][x] == '_')
      return '_';
    else if (map[y][x] == '.')
      return '.';
    else if (map[y][x] == ',')
      return ',';
    else
      return ' ';
  }
 
  
  public void step() {
    stepnum++;
    if ((deaduntil>stepnum) || (gameover) || (pause) || (youwin))
      return;
    int i;
    if (stepnum == 3)
      ghostsinthewild = 1;
    if (stepnum%10 == 0)
      ghostsinthewild = 1;
    if (man.heading == '<') {
      if ((map[man.y][man.x-1] != '#') && (map[man.y][man.x-1] != '_')) {
        man.x -= 1;
        if (man.x == 0)
          man.x = 30;
        if ((map[man.y][man.x-1] != '#') && (map[man.y][man.x-1] != '_')) {
        }
        else
          man.heading = 'q';
      }
    }
    else if (man.heading == '>') {
      if ((map[man.y][man.x+1] != '#') && (map[man.y][man.x+1] != '_')) {
        man.x += 1;
        if (man.x == 31)
          man.x = 1;
        if ((map[man.y][man.x+1] != '#') && (map[man.y][man.x+1] != '_')) {
        }
        else
          man.heading = 'q';
      }
    }
    else if (man.heading == '^') {
      if ((map[man.y-1][man.x] != '#') && (map[man.y-1][man.x] != '_')) {
        man.y -= 1;
        if (man.y == 0)
          man.y = 31;
        if ((map[man.y-1][man.x] != '#') && (map[man.y-1][man.x] != '_')) {
        }
        else
          man.heading = 'q';
      }
    }
    else if (man.heading == 'V') {
      if ((map[man.y+1][man.x] != '#') && (map[man.y+1][man.x] != '_')) {
        man.y += 1;
        if (man.y == 32)
          man.y = 1;
        if ((map[man.y+1][man.x] != '#') && (map[man.y+1][man.x] != '_')) {
        }
        else
          man.heading = 'q';
      }
    }
    if (map[man.y][man.x] == '.') {
      map[man.y][man.x] = ' ';
      points = points+10;
      pellets--;
    }
    if (map[man.y][man.x] == ',') {
      map[man.y][man.x] = ' ';
      points = points+100;
      supercocomanuntil = stepnum+40;
      pellets--;
      for (i = 0;i < 4;i++) {
        ghosts[i].immune = false;
      }
    }
   
    checkcome();


    if (ghostsinthewild == 1) {
      i = 0;
      while ((i < 4) && (ghostsinthewild == 1)) {
        if (!ghosts[i].out) {
          ghosts[i].out = true;
          ghostsinthewild = 0;
        }
        i++;
      }
    }

    for (i = 0;i < 4;i++) {
      boolean newheading = false;
      boolean cangoleft = false;
      boolean cangoright = false;
      boolean cangoup = false;
      boolean cangodown = false;
      boolean moved = false;
      if (map[ghosts[i].y][ghosts[i].x] == '^') {
        ghosts[i].alive = true;
        ghosts[i].immune = true;
        if (ghosts[i].out)
          ghosts[i].heading = '^';
      }
      if ((!ghosts[i].alive) && (map[ghosts[i].y+2][ghosts[i].x] == '^')) {
        ghosts[i].y++;
        moved = true;
      }
      else if ((ghosts[i].out) || (!ghosts[i].alive)) {
        if ((map[ghosts[i].y][ghosts[i].x] == '^') || (map[ghosts[i].y][ghosts[i].x] == '_')) {
          ghosts[i].y--;
          ghosts[i].heading = 'V';
          moved = true;
        }
        else {
          int z;
          for (z = i+1;z < 4;z++) {
            if ((ghosts[i].x == ghosts[z].x) && (ghosts[i].y == ghosts[z].y)) {
              if (ghosts[i].heading == '^')
                ghosts[i].heading = 'V';
              else if (ghosts[i].heading == 'V')
                ghosts[i].heading = '^';
              else if (ghosts[i].heading == '<')
                ghosts[i].heading = '>';
              else if (ghosts[i].heading == '>')
                ghosts[i].heading = '<';
            }
          }
          if ((map[ghosts[i].y][ghosts[i].x-1] != '#') &&
              (map[ghosts[i].y][ghosts[i].x-1] != '_') &&
              (map[ghosts[i].y][ghosts[i].x-1] != '$')
              && (ghosts[i].heading != '>')
              )
            cangoleft = true;
          if ((map[ghosts[i].y][ghosts[i].x+1] != '#') &&
              (map[ghosts[i].y][ghosts[i].x+1] != '_') &&
              (map[ghosts[i].y][ghosts[i].x+1] != '$')
              && (ghosts[i].heading != '<')
              )
            cangoright = true;
          if ((map[ghosts[i].y-1][ghosts[i].x] != '#') &&
              (map[ghosts[i].y-1][ghosts[i].x] != '_') &&
              (map[ghosts[i].y-1][ghosts[i].x] != '$')
              && (ghosts[i].heading != 'V')
              )
            cangoup = true;
          if ((map[ghosts[i].y+1][ghosts[i].x] != '#') &&
              (map[ghosts[i].y+1][ghosts[i].x] != '_') &&
              (map[ghosts[i].y+1][ghosts[i].x] != '$')
              && (ghosts[i].heading != '^')
              )
            cangodown = true;

          if ((ghosts[i].heading == '<') && (cangoleft) && (!cangodown) && (!cangoup)) {
            ghosts[i].x -= 1;
            moved = true;
          }
          if ((ghosts[i].heading == '>') && (cangoright) && (!cangodown) && (!cangoup)) {
            ghosts[i].x += 1;
            moved = true;
          }
          if ((ghosts[i].heading == '^') && (cangoup) && (!cangoleft) && (!cangoright)) {
            ghosts[i].y -= 1;
            moved = true;
          }
          if ((ghosts[i].heading == 'V') && (cangodown) && (!cangoleft) && (!cangoright)) {
            ghosts[i].y += 1;
            moved = true;
          }
          if (!moved) {
            if ((!cangoleft) && (!cangoright) && (!cangoup) && (!cangodown)) {
              ghosts[i].heading = 'q';
              moved = true;
            }
            else if ((cangoleft) && (!cangoright) && (!cangoup) && (!cangodown)) {
              ghosts[i].heading = '<';
              ghosts[i].x -= 1;
              moved = true;
            }
            else if ((!cangoleft) && (cangoright) && (!cangoup) && (!cangodown)) {
              ghosts[i].heading = '>';
              ghosts[i].x += 1;
              moved = true;
            }
            else if ((!cangoleft) && (!cangoright) && (cangoup) && (!cangodown)) {
              ghosts[i].heading = '^';
              ghosts[i].y -= 1;
              moved = true;
            }
            else if ((!cangoleft) && (!cangoright) && (!cangoup) && (cangodown)) {
              ghosts[i].heading = 'V';
              ghosts[i].y += 1;
              moved = true;
            }
          }
          if (!moved) {
            int targetx,targety,offsetx,offsety,offsetxabs,offsetyabs;
            if (!ghosts[i].alive) {
              targetx = 16;
              targety = 14;
            }
            else if ((supercocomanuntil>stepnum) && (!ghosts[i].immune)) {
              targetx = 2*ghosts[i].x-man.x;
              targety = 2*ghosts[i].y-man.x;
            }
            else {
              targetx = man.x;
              targety = man.y;
            }
            offsetx = targetx - ghosts[i].x;
            offsety = targety - ghosts[i].y;
            offsetxabs = offsetx*offsetx;
            offsetyabs = offsety*offsety;
            if (offsetxabs > offsetyabs) {
              if ((offsetx < 0) && (cangoleft)) {
                ghosts[i].heading = '<';
                ghosts[i].x -= 1;
                moved = true;
              }
              else if ((offsetx > 0) && (cangoright)) {
                ghosts[i].heading = '>';
                ghosts[i].x += 1;
                moved = true;
              }
              else if ((offsety < 0) && (cangoup)) {
                ghosts[i].heading = '^';
                ghosts[i].y -= 1;
                moved = true;
              }
              else if ((offsety > 0) && (cangodown)) {
                ghosts[i].heading = 'V';
                ghosts[i].y += 1;
                moved = true;
              }
            }
            else {
              if ((offsety < 0) && (cangoup)) {
                ghosts[i].heading = '^';
                ghosts[i].y -= 1;
                moved = true;
              }
              else if ((offsety > 0) && (cangodown)) {
                ghosts[i].heading = 'V';
                ghosts[i].y += 1;
                moved = true;
              }
              else if ((offsetx < 0) && (cangoleft)) {
                ghosts[i].heading = '<';
                ghosts[i].x -= 1;
                moved = true;
              }
              else if ((offsetx > 0) && (cangoright)) {
                ghosts[i].heading = '>';
                ghosts[i].x += 1;
                moved = true;
              }
            }
          }
          if (!moved) {
            while (!moved) {
              int randValue = rand.nextInt()%4;
              if ((randValue == 0) && (cangoleft)) {
                ghosts[i].heading = '<';
                ghosts[i].x -= 1;
                moved = true;
              }
              else if ((randValue == 1) && (cangoright)) {
                ghosts[i].heading = '>';
                ghosts[i].x += 1;
                moved = true;
              }
              else if ((randValue == 2) && (cangoup)) {
                ghosts[i].heading = '^';
                ghosts[i].y -= 1;
                moved = true;
              }
              else if ((randValue == 3) && (cangodown)) {
                ghosts[i].heading = 'V';
                ghosts[i].y += 1;
                moved = true;
              }
            }
          }
        } 
      }
    }
    checkcome();

    if (pellets == 0) {
      pause = true;
      youwin = true;
    }

  }

  
  private void checkcome() {
    int i;
    for (i = 0;i < 4;i++) {
      if ((ghosts[i].x == man.x) && (ghosts[i].y == man.y)) {
        if ((supercocomanuntil>stepnum) && (!ghosts[i].immune) && (ghosts[i].alive)) {
          ghosts[i].alive = false;
          eatenghosts++;
          ghosts[i].out = false;
          points = points+(100<<eatenghosts);
        }
        else if (ghosts[i].alive) {
          lives--;
          if (lives == 0) {
            gameover = true;
            return;
          }
          man.x = 15;
          man.y = 18;
          ghosts[0] = new Ghost('1');
          ghosts[1] = new Ghost('2');
          ghosts[2] = new Ghost('3');
          ghosts[3] = new Ghost('4');
          deaduntil = stepnum+5;
          supercocomanuntil = 0;
          pause = true;
        }
      }
    }
  }
}