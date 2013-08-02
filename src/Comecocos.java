/**
* @file Comecocos.java
* @brief Fichero implementación del programa Comecocos.
*
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
* @brief Clase lanzadora de la interfaz.
*
* Esta clase contiene el main y se encarga de lanzar la interfaz gráfica.
* Dentro de la interfaz gráfica se encuentran los botones y los manejadores,
* y luego la PantallaComecocos, que contiene la pantalla del juego en sí.
*/
public class Comecocos {

    /**
    * @brief Frame principal.
    */
    JFrame comecocosFrame;

    /**
    * @brief Panel de botones.
    */
    JPanel buttonPanel;

    /**
    * @brief Panel de barra (botones+label).
    */
    JPanel barPanel;

    /**
    * @brief Label de estado.
    */
    JLabel estadoLabel;

    /**
    * @brief Botón de juego nuevo.
    */
    JButton nuevoButton;

    /**
    * @brief Botón de salir.
    */
    JButton salirButton;

    /**
    * @brief Pantalla del juego en sí.
    */
    PantallaComecocos pantalla;

    /**
    * @brief Constructor.
    *
    * Lanza la interfaz gráfica, los listeners, y el timer que actualiza el juego.
    */
    public Comecocos() {
        //Creamos y configuramos la ventana.
        comecocosFrame = new JFrame("Comecocos");
        comecocosFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        comecocosFrame.setDefaultLookAndFeelDecorated(false); 
        comecocosFrame.setResizable(false);

        //Creamos y configuramos los paneles.
        buttonPanel = new JPanel(new GridLayout(1,2));
        barPanel = new JPanel(new GridLayout(2,1));

        //Creamos los componentes.
        nuevoButton = new JButton("Nuevo");
        salirButton = new JButton("Salir");
        estadoLabel = new JLabel("Bienvenido!", SwingConstants.LEFT);
        pantalla = new PantallaComecocos(estadoLabel); //Pasamos la label a pantalla para que pueda cambiar su contenido.

        //Escuchamos los eventos de los botones.
        ActionListener actionListenerNUEVO = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.nuevojuego();
          }
        };

        ActionListener actionListenerSALIR = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
          }
        };

        nuevoButton.addActionListener(actionListenerNUEVO);
        salirButton.addActionListener(actionListenerSALIR);

        //Escuchamos los eventos del teclado.
        ActionListener actionListenerLEFT = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setDireccion('<',true);
          }
        };

        ActionListener actionListenerRIGHT = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setDireccion('>',true);
          }
        };

        ActionListener actionListenerUP = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setDireccion('^',true);
          }
        };

        ActionListener actionListenerDOWN = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setDireccion('V',true);
          }
        };

        ActionListener actionListenerPAUSA = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setPausa();
          }
        };

        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerLEFT, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerRIGHT, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerUP, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerDOWN, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerPAUSA, KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Añadimos los componentes a los paneles.
        buttonPanel.add(nuevoButton);
        buttonPanel.add(salirButton);
        barPanel.add(buttonPanel);
        barPanel.add(estadoLabel);
        comecocosFrame.getContentPane().add(pantalla,BorderLayout.CENTER);
        comecocosFrame.getContentPane().add(barPanel,BorderLayout.NORTH);

        //Mostramos la ventana.
        comecocosFrame.pack();
        pantalla.repaint();
        comecocosFrame.setVisible(true);

        //Lanzamos el Timer que se encarga de actualizar el juego.
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               pantalla.run();
            }
        };
        new Timer(50, taskPerformer).start();
    }

    /**
    * @brief Lanzador usado para crear la GUI.
    *
    * Esto se hace así para mantener la seguridad entre hebras.
    */
    private static void createAndShowGUI() {
        Comecocos comecocos = new Comecocos();
    }

    /**
    * @brief Método main.
    * @param args Parámetros.
    *
    * Esto lanza un trabajo para la hebra de procesamiento de eventos.
    */
    public static void main(String[] args) {
        //Ponemos un trabajo para la hebra de procesamiento de eventos.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

/**
* @brief Clase que contiene la pantalla del juego.
*
* Contiene una partida del juego y es un JPanel con la pantalla del juego en sí.
*/
class PantallaComecocos extends JPanel {
  /**
  * @brief Juego actual, clase con la lógica del juego.
  */
  Juego game;

  /**
  * @brief "Puntero" a la etiqueta para poner mensajes.
  */
  JLabel label;

  /**
  * @brief Indicador de salto para controlar el movimiento suave.
  */
  int muststep = 0;

  /**
  * @brief Próxima dirección a tomar.
  */ 
  char nextdir = 'q';

  /**
  * @brief Última dirección tomada (para poner el comecocos apuntando a un sitio u otro).
  */
  char lastdireccion = '>';

  /**
  * @brief Cambiar la dirección.
  * @param newdir Nueva direccio de entre <>^V (izquierda, derecha, arriba, abajo).
  * @param quitarpausa Forzar quitar pausa?
  */
  public void setDireccion(char newdir,boolean quitarpausa) {
    nextdir = newdir;
    if ((game.getPausa()) && (quitarpausa))
      game.setPausa();
    if (muststep != 0)
      return;
    if ((game.getMuerto()) || (game.getGameover()) || (game.getGanas()))
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

  /**
  * @brief Manejador del botón de juego nuevo.
  */
  public void nuevojuego() {
    game = new Juego();
  }

  /**
  * @brief Poner pausa.
  */
  public void setPausa() {
    if ((game.getMuerto()) || (game.getGameover()) || (game.getGanas()) || ((game.getPausa())))
      return;
    game.setPausa();
  }

  /**
  * @brief Constructor.
  *
  * Inicializa la clase con la lógica del juego y define el tamaño preferido.
  */
  public PantallaComecocos(JLabel labelasociada) {
    label = labelasociada;
    game = new Juego();
    this.setPreferredSize(new Dimension(496, 496));
  }

  /**
  * @brief Método run. Se ejecuta una vez cada 50 milisegundos.
  *
  * Da una serie de pasos, pero da un paso en la lógica del juego sólo cada 4 pasos.
  * De esta forma podemos hacer un movimiento suave del comecocos.
  */
  public void run() {
    int i;
    if ((game.getMuerto()) || (game.getGameover()) || (game.getGanas())) {
      muststep = 0;
    }
    String text = new String();
    if (game.getMuerto())
      text = "MUERTO";
    else if (game.getGameover())
      text = "GAME OVER";
    else if (game.getGanas())
      text = "HAS GANADO";
    else if (game.getPausa())
      text = "PAUSA";
    else
      muststep = (muststep+1)%4;
    label.setText("Puntos: "+game.getPuntos()+" Vidas: "+game.getVidas()+"  "+text);
    repaint();
    if (muststep == 0) {
      game.step();
      if (nextdir != 'q') {
        setDireccion(nextdir,false);
      }
    }
  }

  /**
  * @brief Método paint.
  * @param g Graphics en el cual pintar.
  *
  * Este método es el estandar usado para pintar la pantalla.
  */
  public void paint(Graphics g) {
    super.paint(g);
    int x,y,i;

    for (y = 1;y < 32;y++) {
      for (x = 1;x < 32;x++) {
        switch (game.getContent(x,y)) {
          case '#':
          dibujaitem(g,x,y,'#');
          break;
          case '_':
          dibujaitem(g,x,y,'_');
          break;
          case '.':
          dibujaitem(g,x,y,'.');
          break;
          case ',':
          dibujaitem(g,x,y,',');
          break;
          default:
          dibujaitem(g,x,y,' ');
        }
      }
    }
    dibujaitem(g,game.getComecocosx(),game.getComecocosy(),'C');
    for (i = 0;i < 4;i++) {
      if (!game.getFantasmav(i)) {
        switch (game.getFantasmaid(i)) {
          case '1':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'a');
          break;
          case '2':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'b');
          break;
          case '3':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'c');
          break;
          case '4':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'d');
          break;
        }
      }
      else if ((game.getSupercome()) && (!game.getFantasmainmune(i))) {
        switch (game.getFantasmaid(i)) {
          case '1':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'5');
          break;
          case '2':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'6');
          break;
          case '3':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'7');
          break;
          case '4':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'8');
          break;
        }
      }
      else {
        switch (game.getFantasmaid(i)) {
          case '1':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'1');
          break;
          case '2':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'2');
          break;
          case '3':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'3');
          break;
          case '4':
            dibujaitem(g,game.getFantasmax(i),game.getFantasmay(i),'4');
          break;
        }
      }
    }
  }

  /**
  * @brief Dibujar un item determinado.
  * @param g Graphics en el que dibujar.
  * @param x Coordenada x de la celda en la que dibujar.
  * @param y Coordenada y de la celda en la que dibujar.
  * @param item Identificador del objeto a dibujar.
  */
  public void dibujaitem(Graphics g,int x,int y,char item) {
    int startx = (x-1)*16;
    int starty = (y-1)*16;
    boolean movil = false;
    char movildir = ' ';
    switch (item) {
      case 'C':
        movil = true;
        movildir = game.getDireccion();
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
    char aux = game.getDireccion();
    if (aux != 'q')
      lastdireccion = aux;
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
        switch (lastdireccion) {
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


/**
* @brief Clase que representa a un fantasma.
*/
class Fantasma {

  /**
  * @brief Coordenada x del fantasma.
  */
  public int x;

  /**
  * @brief Coordenada y del fantasma.
  */
  public int y;

  /**
  * @brief ID del fantasma.
  */
  public char id;

  /**
  * @brief Si el fantasma está vivo o no.
  */
  public boolean v;

  /**
  * @brief Si el fantasma está fuera de la casa o no.
  */
  public boolean f;

  /**
  * @brief Dirección del fantasma.
  */
  public char d;

  /**
  * @brief Si el fantasma es inmune al comecocos o no.
  */
  public boolean inmune;

  /**
  * @brief Constructor.
  * @param i ID del fantasma.
  */
  Fantasma(char i) {
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
    v = true;
    f = false;
    d = 'q';
  }
}

/**
* @brief Clase que contiene la lógica del juego.
*/
class Juego {

  /**
  * @brief Rejilla de juego.
  */
  char mapa[][];

  /**
  * @brief Coordenada x de la posición del comecocos.
  */
  int comecocosx = 15;

  /**
  * @brief Coordenada y de la posición del comecocos.
  */
  int comecocosy = 18;

  /**
  * @brief Dirección del comecocos.
  */
  char direccion = 'q';

  /**
  * @brief Número de paso del juego.
  */
  int stepnum = 0;

  /**
  * @brief Tiempo hasta que podemos comer fantasmas.
  */
  int supercomehasta = 0;

  /**
  * @brief Puntos que tenemos.
  */
  int puntos = 0;

  /**
  * @brief Número de vidas que tenemos.
  */
  int vidas = 3;

  /**
  * @brief Retraso para volver a jugar tras haber muerto.
  */
  int muertohasta = 0;

  /**
  * @brief Si un fantasma puede salir o no de casa.
  */
  int fantasmasfuera = 0;

  /**
  * @brief Si estamos pausados o no.
  */
  boolean pausa = true;

  /**
  * @brief Número de puntitos que quedan por comer para ganar.
  */
  int puntitos = 0;

  /**
  * @brief Si has ganado o no.
  */
  boolean ganas = false;

  /**
  * @brief Si has perdido o no.
  */
  boolean gameover = false;

  /**
  * @brief Número de fantasmas que te has comido.
  */
  int fantasmascomidos = 0;

  /**
  * @brief Vector de fantasmas.
  */
  Fantasma fantasmas[];

  /**
  * @brief Generador de números aleatorios.
  */
  Random rand;

  /**
  * @brief Constructor. Inicializa el mapa y el juego en sí.
  */
  Juego() {
    int x,y;
    fantasmas = new Fantasma[4];
    fantasmas[0] = new Fantasma('1');
    fantasmas[1] = new Fantasma('2');
    fantasmas[2] = new Fantasma('3');
    fantasmas[3] = new Fantasma('4');
    rand = new Random();
    mapa = new char[33][33];
    mapa[0] =  new String("#################################").toCharArray();
    mapa[1] =  new String("#################################").toCharArray();
    mapa[2] =  new String("##.............................##").toCharArray();
    mapa[3] =  new String("##.####.#####.##.##.#####.####.##").toCharArray();
    mapa[4] =  new String("##,####.#####.##.##.#####.####,##").toCharArray();
    mapa[5] =  new String("##.####.#####.##.##.#####.####.##").toCharArray();
    mapa[6] =  new String("##.............................##").toCharArray();
    mapa[7] =  new String("##.####.###.#########.###.####.##").toCharArray();
    mapa[8] =  new String("##.####.###.#########.###.####.##").toCharArray();
    mapa[9] =  new String("##......###....###....###......##").toCharArray();
    mapa[10] = new String("#######.###### ### ######.#######").toCharArray();
    mapa[11] = new String("      #.###### ### ######.#      ").toCharArray();
    mapa[12] = new String("      #.###           ###.#      ").toCharArray();
    mapa[13] = new String("      #.### ##_____## ###.#      ").toCharArray();
    mapa[14] = new String("#######.### #$^^^^^$# ###.#######").toCharArray();
    mapa[15] = new String("      $.    #$^^^^^$#    .$      ").toCharArray();
    mapa[16] = new String("#######.### #$^^^^^$# ###.#######").toCharArray();
    mapa[17] = new String("      #.### ######### ###.#      ").toCharArray();
    mapa[18] = new String("      #.###           ###.#      ").toCharArray();
    mapa[19] = new String("      #.### ######### ###.#      ").toCharArray();
    mapa[20] = new String("#######.### ######### ###.#######").toCharArray();
    mapa[21] = new String("##.............###.............##").toCharArray();
    mapa[22] = new String("##.#####.#####.###.#####.#####.##").toCharArray();
    mapa[23] = new String("##.#####.#####.###.#####.#####.##").toCharArray();
    mapa[24] = new String("##,..###.................###..,##").toCharArray();
    mapa[25] = new String("####.###.##.#########.##.###.####").toCharArray();
    mapa[26] = new String("####.###.##.#########.##.###.####").toCharArray();
    mapa[27] = new String("##.......##....###....##.......##").toCharArray();
    mapa[28] = new String("##.###########.###.###########.##").toCharArray();
    mapa[29] = new String("##.###########.###.###########.##").toCharArray();
    mapa[30] = new String("##.............................##").toCharArray();
    mapa[31] = new String("#################################").toCharArray();
    mapa[32] = new String("#################################").toCharArray();
    for (y = 1;y < 32;y++) {
      for (x = 1;x < 32;x++) {
        if ((mapa[y][x] == '.') || (mapa[y][x] == ','))
          puntitos++;
      }
    }
  }

  /**
  * @brief Mirar número de vidas.
  * @return Número de vidas.
  */
  public int getVidas() {
    return vidas;
  }

  /**
  * @brief Mirar número de puntos.
  * @return Número de puntos.
  */
  public int getPuntos() {
    return puntos;
  }

  /**
  * @brief Mirar si hemos perdido o no.
  * @return Si hemos perdido true, false si no.
  */
  public boolean getGameover() {
    return gameover;
  }

  /**
  * @brief Mirar si hemos ganado o no.
  * @return Si hemos ganado true, false si no.
  */
  public boolean getGanas() {
    return ganas;
  }

  /**
  * @brief Poner pausa.
  */
  public void setPausa() {
    if (!pausa)
      pausa=true;
    else
      pausa=false;
  }

  /**
  * @brief Mirar si estamos pausados.
  * @return Si estamos pausados true, si no false.
  */
  public boolean getPausa() {
    return pausa;
  }

  /**
  * @brief Mirar si estamos muertos temporalmente.
  * @return Si estamos muertos, true, si no, false.
  */
  public boolean getMuerto() {
    if (muertohasta>stepnum)
      return true;
    else
      return false;
  }

  /**
  * @brief Si los fantasmas están débiles o no.
  * @return true si sí, false si no.
  */
  public boolean getSupercome() {
    if (supercomehasta>stepnum) {
      if (supercomehasta-8<stepnum) {
        if (stepnum%2 == 1)
          return true;
        else
          return false;
      }
      return true;
    }
    else {
      fantasmascomidos = 0;
      return false;
    }
  }

  /**
  * @brief Obtener dirección del comecocos.
  * @return dirección, entre V^<> (abajo, arriba, izquierda, derecha).
  */
  public char getDireccion() {
    return direccion;
  }

  /**
  * @brief Intentar cambiar la dirección a arriba.
  */
  public void setDireccionArriba() {
    if ((mapa[comecocosy-1][comecocosx] == '#') || (mapa[comecocosy-1][comecocosx] == '_')) {
    }
    else {
      direccion = '^';
    }
  }

  /**
  * @brief Intentar cambiar la dirección a abajo.
  */
  public void setDireccionAbajo() {
    if ((mapa[comecocosy+1][comecocosx] == '#') || (mapa[comecocosy+1][comecocosx] == '_')) {
    }
    else {
      direccion = 'V';
    }
  }

  /**
  * @brief Intentar cambiar la dirección a izquierda.
  */
  public void setDireccionIzquierda() {
    if ((mapa[comecocosy][comecocosx-1] == '#') || (mapa[comecocosy][comecocosx-1] == '_')) {
    }
    else {
      direccion = '<';
    }
  }

  /**
  * @brief Intentar cambiar la dirección a derecha.
  */
  public void setDireccionDerecha() {
    if ((mapa[comecocosy][comecocosx+1] == '#') || (mapa[comecocosy][comecocosx+1] == '_')) {
    }
    else {
      direccion = '>';
    }
  }


  /**
  * @brief Obtener contenido de la rejilla.
  * @param x Coordenada x.
  * @param y Coordenada y.
  * @return Caracter identificativo del contenido.
  */
  public char getContent(int x,int y) {
    if (mapa[y][x] == '#')
      return '#';
    else if (mapa[y][x] == '_')
      return '_';
    else if (mapa[y][x] == '.')
      return '.';
    else if (mapa[y][x] == ',')
      return ',';
    else
      return ' ';
  }

  /**
  * @brief Obtener coordenada x del comecocos.
  * @return Coordenada x.
  */
  public int getComecocosx() {
    return comecocosx;
  }


  /**
  * @brief Obtener coordenada y del comecocos.
  * @return Coordenada y.
  */
  public int getComecocosy() {
    return comecocosy;
  }


  /**
  * @brief Obtener coordenada x del fantasma indicado.
  * @param i Número del fantasma.
  * @return Coordenada x.
  */
  public int getFantasmax(int i) {
    return fantasmas[i].x;
  } 


  /**
  * @brief Obtener coordenada y del fantasma indicado.
  * @param i Número del fantasma.
  * @return Coordenada y.
  */
  public int getFantasmay(int i) {
    return fantasmas[i].y;
  } 

  /**
  * @brief Obtener si el fantasma indicado está vivo o no.
  * @param i Número del fantasma.
  * @return true si está vivo, false si no.
  */
  public boolean getFantasmav(int i) {
    return fantasmas[i].v;
  } 

  /**
  * @brief Obtener si el fantasma indicado está fuera de la casa o no.
  * @param i Número del fantasma.
  * @return true si está fuera, false si no.
  */
  public char getFantasmaid(int i) {
    return fantasmas[i].id;
  } 

  /**
  * @brief Obtener si el fantasma indicado es inmune al comecocos o no.
  * @param i Número del fantasma.
  * @return true si es inmune, false si no.
  */
  public boolean getFantasmainmune(int i) {
    return fantasmas[i].inmune;
  } 

  /**
  * @brief Obtener dirección del fantasma indicado.
  * @param i Número del fantasma.
  * @return dirección del fantasma.
  */
  public char getFantasmad(int i) {
    return fantasmas[i].d;
  } 
      
  /**
  * @brief Hacer un paso en el juego.
  *
  * Esta es la función que maneja toda la lógica.
  * En ella se desplaza el comecocos, se comen los puntitos, se desplazan los fantasmas,
  * y en caso de colisión entre ellos o con paredes, se les cambia la dirección.
  *
  * También se comprueba con checkcome si un fantasma se ha comido al comecocos o viceversa.
  *
  * También se comprueba si hemos ganado o no, o si hemos perdido.
  */
  public void step() {
    stepnum++;
    if ((getMuerto()) || (getGameover()) || (getPausa()) || (getGanas()))
      return;
    int i;
    if (stepnum == 3)
      fantasmasfuera = 1;
    if (stepnum%10 == 0)
      fantasmasfuera = 1;
    if (direccion == '<') {
      if ((mapa[comecocosy][comecocosx-1] != '#') && (mapa[comecocosy][comecocosx-1] != '_')) {
        comecocosx -= 1;
        if (comecocosx == 0)
          comecocosx = 30;
        if ((mapa[comecocosy][comecocosx-1] != '#') && (mapa[comecocosy][comecocosx-1] != '_')) {
        }
        else
          direccion = 'q';
      }
    }
    else if (direccion == '>') {
      if ((mapa[comecocosy][comecocosx+1] != '#') && (mapa[comecocosy][comecocosx+1] != '_')) {
        comecocosx += 1;
        if (comecocosx == 31)
          comecocosx = 1;
        if ((mapa[comecocosy][comecocosx+1] != '#') && (mapa[comecocosy][comecocosx+1] != '_')) {
        }
        else
          direccion = 'q';
      }
    }
    else if (direccion == '^') {
      if ((mapa[comecocosy-1][comecocosx] != '#') && (mapa[comecocosy-1][comecocosx] != '_')) {
        comecocosy -= 1;
        if (comecocosy == 0)
          comecocosy = 31;
        if ((mapa[comecocosy-1][comecocosx] != '#') && (mapa[comecocosy-1][comecocosx] != '_')) {
        }
        else
          direccion = 'q';
      }
    }
    else if (direccion == 'V') {
      if ((mapa[comecocosy+1][comecocosx] != '#') && (mapa[comecocosy+1][comecocosx] != '_')) {
        comecocosy += 1;
        if (comecocosy == 32)
          comecocosy = 1;
        if ((mapa[comecocosy+1][comecocosx] != '#') && (mapa[comecocosy+1][comecocosx] != '_')) {
        }
        else
          direccion = 'q';
      }
    }
    if (mapa[comecocosy][comecocosx] == '.') {
      mapa[comecocosy][comecocosx] = ' ';
      puntos = puntos+10;
      puntitos--;
    }
    if (mapa[comecocosy][comecocosx] == ',') {
      mapa[comecocosy][comecocosx] = ' ';
      puntos = puntos+100;
      supercomehasta = stepnum+40;
      puntitos--;
      for (i = 0;i < 4;i++) {
        fantasmas[i].inmune = false;
      }
    }
   
    checkcome();


    if (fantasmasfuera == 1) {
      i = 0;
      while ((i < 4) && (fantasmasfuera == 1)) {
        if (!fantasmas[i].f) {
          fantasmas[i].f = true;
          fantasmasfuera = 0;
        }
        i++;
      }
    }

    for (i = 0;i < 4;i++) {
      boolean nuevadir = false;
      boolean posibleizq = false;
      boolean posibleder = false;
      boolean posiblearr = false;
      boolean posibleaba = false;
      boolean movido = false;
      if (mapa[fantasmas[i].y][fantasmas[i].x] == '^') {
        fantasmas[i].v = true;
        fantasmas[i].inmune = true;
        if (fantasmas[i].f)
          fantasmas[i].d = '^';
      }
      if ((!fantasmas[i].v) && (mapa[fantasmas[i].y+2][fantasmas[i].x] == '^')) {
        fantasmas[i].y++;
        movido = true;
      }
      else if ((fantasmas[i].f) || (!fantasmas[i].v)) {
        if ((mapa[fantasmas[i].y][fantasmas[i].x] == '^') || (mapa[fantasmas[i].y][fantasmas[i].x] == '_')) {
          fantasmas[i].y--;
          fantasmas[i].d = 'V';
          movido = true;
        }
        else {
          int z;
          for (z = i+1;z < 4;z++) {
            if ((fantasmas[i].x == fantasmas[z].x) && (fantasmas[i].y == fantasmas[z].y)) {
              if (fantasmas[i].d == '^')
                fantasmas[i].d = 'V';
              else if (fantasmas[i].d == 'V')
                fantasmas[i].d = '^';
              else if (fantasmas[i].d == '<')
                fantasmas[i].d = '>';
              else if (fantasmas[i].d == '>')
                fantasmas[i].d = '<';
            }
          }
          if ((mapa[fantasmas[i].y][fantasmas[i].x-1] != '#') &&
              (mapa[fantasmas[i].y][fantasmas[i].x-1] != '_') &&
              (mapa[fantasmas[i].y][fantasmas[i].x-1] != '$')
              && (fantasmas[i].d != '>')
              )
            posibleizq = true;
          if ((mapa[fantasmas[i].y][fantasmas[i].x+1] != '#') &&
              (mapa[fantasmas[i].y][fantasmas[i].x+1] != '_') &&
              (mapa[fantasmas[i].y][fantasmas[i].x+1] != '$')
              && (fantasmas[i].d != '<')
              )
            posibleder = true;
          if ((mapa[fantasmas[i].y-1][fantasmas[i].x] != '#') &&
              (mapa[fantasmas[i].y-1][fantasmas[i].x] != '_') &&
              (mapa[fantasmas[i].y-1][fantasmas[i].x] != '$')
              && (fantasmas[i].d != 'V')
              )
            posiblearr = true;
          if ((mapa[fantasmas[i].y+1][fantasmas[i].x] != '#') &&
              (mapa[fantasmas[i].y+1][fantasmas[i].x] != '_') &&
              (mapa[fantasmas[i].y+1][fantasmas[i].x] != '$')
              && (fantasmas[i].d != '^')
              )
            posibleaba = true;

          if ((fantasmas[i].d == '<') && (posibleizq) && (!posibleaba) && (!posiblearr)) {
            fantasmas[i].x -= 1;
            movido = true;
          }
          if ((fantasmas[i].d == '>') && (posibleder) && (!posibleaba) && (!posiblearr)) {
            fantasmas[i].x += 1;
            movido = true;
          }
          if ((fantasmas[i].d == '^') && (posiblearr) && (!posibleizq) && (!posibleder)) {
            fantasmas[i].y -= 1;
            movido = true;
          }
          if ((fantasmas[i].d == 'V') && (posibleaba) && (!posibleizq) && (!posibleder)) {
            fantasmas[i].y += 1;
            movido = true;
          }
          if (!movido) {
            if ((!posibleizq) && (!posibleder) && (!posiblearr) && (!posibleaba)) {
              fantasmas[i].d = 'q';
              movido = true;
            }
            else if ((posibleizq) && (!posibleder) && (!posiblearr) && (!posibleaba)) {
              fantasmas[i].d = '<';
              fantasmas[i].x -= 1;
              movido = true;
            }
            else if ((!posibleizq) && (posibleder) && (!posiblearr) && (!posibleaba)) {
              fantasmas[i].d = '>';
              fantasmas[i].x += 1;
              movido = true;
            }
            else if ((!posibleizq) && (!posibleder) && (posiblearr) && (!posibleaba)) {
              fantasmas[i].d = '^';
              fantasmas[i].y -= 1;
              movido = true;
            }
            else if ((!posibleizq) && (!posibleder) && (!posiblearr) && (posibleaba)) {
              fantasmas[i].d = 'V';
              fantasmas[i].y += 1;
              movido = true;
            }
          }
          if (!movido) {
            int objetivox,objetivoy,desvx,desvy,desvxabs,desvyabs;
            if (!fantasmas[i].v) {
              objetivox = 16;
              objetivoy = 14;
            }
            else if ((supercomehasta>stepnum) && (!fantasmas[i].inmune)) {
              objetivox = 2*fantasmas[i].x-comecocosx;
              objetivoy = 2*fantasmas[i].y-comecocosx;
            }
            else {
              objetivox = comecocosx;
              objetivoy = comecocosy;
            }
            desvx = objetivox - fantasmas[i].x;
            desvy = objetivoy - fantasmas[i].y;
            desvxabs = desvx*desvx;
            desvyabs = desvy*desvy;
            if (desvxabs > desvyabs) {
              if ((desvx < 0) && (posibleizq)) {
                fantasmas[i].d = '<';
                fantasmas[i].x -= 1;
                movido = true;
              }
              else if ((desvx > 0) && (posibleder)) {
                fantasmas[i].d = '>';
                fantasmas[i].x += 1;
                movido = true;
              }
              else if ((desvy < 0) && (posiblearr)) {
                fantasmas[i].d = '^';
                fantasmas[i].y -= 1;
                movido = true;
              }
              else if ((desvy > 0) && (posibleaba)) {
                fantasmas[i].d = 'V';
                fantasmas[i].y += 1;
                movido = true;
              }
            }
            else {
              if ((desvy < 0) && (posiblearr)) {
                fantasmas[i].d = '^';
                fantasmas[i].y -= 1;
                movido = true;
              }
              else if ((desvy > 0) && (posibleaba)) {
                fantasmas[i].d = 'V';
                fantasmas[i].y += 1;
                movido = true;
              }
              else if ((desvx < 0) && (posibleizq)) {
                fantasmas[i].d = '<';
                fantasmas[i].x -= 1;
                movido = true;
              }
              else if ((desvx > 0) && (posibleder)) {
                fantasmas[i].d = '>';
                fantasmas[i].x += 1;
                movido = true;
              }
            }
          }
          if (!movido) {
            while (!movido) {
              int azar = rand.nextInt()%4;
              if ((azar == 0) && (posibleizq)) {
                fantasmas[i].d = '<';
                fantasmas[i].x -= 1;
                movido = true;
              }
              else if ((azar == 1) && (posibleder)) {
                fantasmas[i].d = '>';
                fantasmas[i].x += 1;
                movido = true;
              }
              else if ((azar == 2) && (posiblearr)) {
                fantasmas[i].d = '^';
                fantasmas[i].y -= 1;
                movido = true;
              }
              else if ((azar == 3) && (posibleaba)) {
                fantasmas[i].d = 'V';
                fantasmas[i].y += 1;
                movido = true;
              }
            }
          }
        } 
      }
    }
    checkcome();

    if (puntitos == 0) {
      pausa = true;
      ganas = true;
    }

  }

  /**
  * @brief Función que comprueba si alguien se ha comido a algo.
  */
  private void checkcome() {
    int i;
    for (i = 0;i < 4;i++) {
      if ((fantasmas[i].x == comecocosx) && (fantasmas[i].y == comecocosy)) {
        if ((supercomehasta>stepnum) && (!fantasmas[i].inmune) && (fantasmas[i].v)) {
          fantasmas[i].v = false;
          fantasmascomidos++;
          fantasmas[i].f = false;
          puntos = puntos+(100<<fantasmascomidos);
        }
        else if (fantasmas[i].v) {
          vidas--;
          if (vidas == 0) {
            gameover = true;
            return;
          }
          comecocosx = 15;
          comecocosy = 18;
          fantasmas[0] = new Fantasma('1');
          fantasmas[1] = new Fantasma('2');
          fantasmas[2] = new Fantasma('3');
          fantasmas[3] = new Fantasma('4');
          muertohasta = stepnum+5;
          supercomehasta = 0;
          pausa = true;
        }
      }
    }
  }
}
