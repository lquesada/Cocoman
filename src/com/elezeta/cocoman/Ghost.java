package com.elezeta.cocoman;



class Ghost {

  
  public int x;

  
  public int y;

  
  public char id;

  
  public boolean alive;

  
  public boolean out;

  
  public char heading;

  
  public boolean immune;

  
  Ghost(char i) {
    id = i;
    if (id == '1')
      x = 14;
    if (id == '2')
      x = 15;
    if (id == '3')
      x = 17;
    if (id == '4')
      x = 18;
    y = 15;
    alive = true;
    out = false;
    heading = 'q';
  }
}

