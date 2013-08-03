package com.elezeta.cocoman;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class Cocoman {

    
    JFrame comecocosFrame;

    
    JPanel buttonPanel;

    
    JPanel barPanel;

    
    JLabel estadoLabel;

    
    JButton nuevoButton;

    
    JButton salirButton;

    
    GameScreen pantalla;

    
    public Cocoman() {
        //Creamos y configuramos la ventana.
        comecocosFrame = new JFrame("Cocoman");
        comecocosFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        comecocosFrame.setDefaultLookAndFeelDecorated(false); 
        comecocosFrame.setResizable(false);

        //Creamos y configuramos los paneles.
        buttonPanel = new JPanel(new GridLayout(1,2));
        barPanel = new JPanel(new GridLayout(2,1));

        //Creamos los componentes.
        nuevoButton = new JButton("New Game");
        salirButton = new JButton("Exit");
        estadoLabel = new JLabel("Welcome to Cocoman!", SwingConstants.LEFT);
        pantalla = new GameScreen(estadoLabel); //Pasamos la label a pantalla para que pueda cambiar su contenido.

        //Escuchamos los eventos de los botones.
        ActionListener actionListenerNUEVO = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.newGame();
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
            pantalla.setHeading('<',true);
          }
        };

        ActionListener actionListenerRIGHT = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setHeading('>',true);
          }
        };

        ActionListener actionListenerUP = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setHeading('^',true);
          }
        };

        ActionListener actionListenerDOWN = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setHeading('V',true);
          }
        };

        ActionListener actionListenerPAUSA = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            pantalla.setPause();
          }
        };

        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerLEFT, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerLEFT, KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerRIGHT, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerRIGHT, KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerUP, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerUP, KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerDOWN, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        comecocosFrame.getRootPane().registerKeyboardAction(actionListenerDOWN, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
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

    
    private static void createAndShowGUI() {
        Cocoman comecocos = new Cocoman();
    }

    
    public static void main(String[] args) {
        //Ponemos un trabajo para la hebra de procesamiento de eventos.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

