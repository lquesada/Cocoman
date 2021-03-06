package com.elezeta.cocoman;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class Cocoman {

    
    JFrame cocomanFrame;

    
    JPanel buttonPanel;

    
    JPanel barPanel;

    
    JLabel statusLabel;

    
    JButton newGameButton;

    
    JButton exitButton;

    
    GameScreen screen;

    
    public Cocoman() {
        //Creamos y configuramos la ventana.
        cocomanFrame = new JFrame("Cocoman");
        cocomanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cocomanFrame.setDefaultLookAndFeelDecorated(false); 
        cocomanFrame.setResizable(false);

        //Creamos y configuramos los paneles.
        buttonPanel = new JPanel(new GridLayout(1,2));
        barPanel = new JPanel(new GridLayout(2,1));

        //Creamos los componentes.
        newGameButton = new JButton("New Game");
        exitButton = new JButton("Exit");
        statusLabel = new JLabel("Welcome to Cocoman!", SwingConstants.LEFT);
        screen = new GameScreen(statusLabel); //Pasamos la label a pantalla para que pueda cambiar su contenido.

        //Escuchamos los eventos de los botones.
        ActionListener actionListenerNUEVO = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            screen.newGame();
          }
        };

        ActionListener actionListenerSALIR = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
          }
        };

        newGameButton.addActionListener(actionListenerNUEVO);
        exitButton.addActionListener(actionListenerSALIR);

        //Escuchamos los eventos del teclado.
        ActionListener actionListenerLEFT = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            screen.setHeading('<',true);
          }
        };

        ActionListener actionListenerRIGHT = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            screen.setHeading('>',true);
          }
        };

        ActionListener actionListenerUP = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            screen.setHeading('^',true);
          }
        };

        ActionListener actionListenerDOWN = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            screen.setHeading('V',true);
          }
        };

        ActionListener actionListenerPAUSA = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            screen.setPause();
          }
        };

        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerLEFT, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerLEFT, KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerRIGHT, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerRIGHT, KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerUP, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerUP, KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerDOWN, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerDOWN, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cocomanFrame.getRootPane().registerKeyboardAction(actionListenerPAUSA, KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        //A�adimos los componentes a los paneles.
        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);
        barPanel.add(buttonPanel);
        barPanel.add(statusLabel);
        cocomanFrame.getContentPane().add(screen,BorderLayout.CENTER);
        cocomanFrame.getContentPane().add(barPanel,BorderLayout.NORTH);

        //Mostramos la ventana.
        cocomanFrame.pack();
        screen.repaint();
        cocomanFrame.setVisible(true);

        //Lanzamos el Timer que se encarga de actualizar el juego.
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               screen.run();
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

