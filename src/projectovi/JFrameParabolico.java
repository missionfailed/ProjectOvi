/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projectovi;

/**
 *
 * @author Ovidio
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

public class JFrameParabolico extends JFrame implements Runnable, KeyListener, MouseListener{
        private static final long serialVersionUID = 1L;
        //declaracion de variables
        private boolean pausa;  // Checa si el juego esta pausado
        private boolean colisiono;  // Checa si colisiono el bueno con algun malo
        private int colContador; // Tiempo de despliego de colision
        private int direccion;  // Direccion del Bueno
        private int antDireccion;   // Direccion anterior del objeto bueno
        private int velocidad;  // Velocidad del Bueno
        private int caida;      // Velocidad del Malo
        private int score;      // Score del juego
        private int mCantidad;  // Cantidad de malos
        private Image dbImage;	// Imagen a proyectar
        private Graphics dbg;	// Objeto grafico
        private SoundClip bomb;    //Objeto AudioClip 
        private SoundClip app;     //Objeto AudioClip
        private Bueno bueno;    // Objeto de la clase Bueno
        private Malo malo;   // Objeto dee la clase Malo
        //Variables control de tiempo de animacion
        private long tiempoActual;
	private long tiempoInicial;
        
         //Constructor
        public JFrameParabolico() {
            init();
            start();
        }
        
        public void init() {
            setSize(600, 600);
            pausa = false;
            colisiono = false;
            score = 0;
            direccion = 0;
            velocidad = 10;
            caida = 1;
            int posX = getWidth()/2;
            int posY = getHeight();
            bueno = new Bueno(posX,posY);
            bueno.setPosX(posX-bueno.getAncho()/2);
            bueno.setPosY(posY-bueno.getAlto()/2);
            //Se cargan los sonidos.
            bomb = new SoundClip ("/sounds/pokeball.wav");
            app = new SoundClip ("/sounds/Explosion.wav");
            posX = getWidth()/2;
            posY = getHeight()/2;
            malo = new Malo(posX, posY, 3);
            setBackground(Color.green);
            addKeyListener(this);
            addMouseListener(this);
        }
        
        /** 
	 * Metodo <I>start</I> sobrescrito de la clase <code>Applet</code>.<P>
        * En este metodo se crea e inicializa el hilo
        * para la animacion este metodo es llamado despues del init o 
        * cuando el usuario visita otra pagina y luego regresa a la pagina
        * en donde esta este <code>Applet</code>
        * 
        */
	public void start () {
		// Declaras un hilo
		Thread th = new Thread (this);
		// Empieza el hilo
		th.start ();
	}
        
        /**
	 * Metodo <I>stop</I> sobrescrito de la clase <code>Applet</code>.<P>
	 * En este metodo se pueden tomar acciones para cuando se termina
	 * de usar el <code>Applet</code>. Usualmente cuando el usuario sale de la pagina
	 * en donde esta este <code>Applet</code>.
	 */
	public void stop() {
       
	}
        
        /**
	 * Metodo <I>destroy</I> sobrescrito de la clase <code>Applet</code>.<P>
	 * En este metodo se toman las acciones necesarias para cuando
	 * el <code>Applet</code> ya no va a ser usado. Usualmente cuando el usuario
	 * cierra el navegador.
	 */
	public void destroy() {
	    
	}
        
        /** 
	 * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
        * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
        * la posicion en x o y dependiendo de la direccion, finalmente 
        * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
        * 
        */
	public void run () {
            //Guarda el tiempo actual del sistema
            tiempoActual = System.currentTimeMillis();
                while (true) {
                    actualiza();
                    checaColision();
                    repaint();    // Se actualiza el <code>Applet</code> repintando el contenido.
                    try	{
                            // El thread se duerme.
                            Thread.sleep (200);
                    }
                    catch (InterruptedException ex)	{
                            System.out.println("Error en " + ex.toString());
                    }
                }
	}
        
        /**
         * Metodo <I>actualiza</I>
         * Este metodo actualiza a los personajes en el applet en sus movimientos
        */
        public void actualiza() {
            
            //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
            
            //Guarda el tiempo actual
            tiempoActual += tiempoTranscurrido;
            
            if (!pausa) {
                //dependiendo de la tecla que se este oprimiendo es hacia donde se mueve el personaje Bueno
                switch (direccion) {
                    case 1:
                        bueno.setPosX(bueno.getPosX() + velocidad);
                        bueno.actualiza(tiempoActual);
                        break; 
                    case 2:
                        bueno.setPosX(bueno.getPosX() - velocidad);
                        bueno.actualiza(tiempoActual);
                        break;
                    case 3:
                        bueno.setPosY(bueno.getPosY() - velocidad);
                        bueno.actualiza(tiempoActual);
                        break;
                    case 4:
                        bueno.setPosY(bueno.getPosY() + velocidad);
                        bueno.actualiza(tiempoActual);
                    case 0:
                        bueno.setPosX(bueno.getPosX());
                        break;
                }
                
                malo.setPosY(malo.getPosY()+malo.getVelocidad());
                malo.actualiza(tiempoActual);
            }
        }
        
        /**
         * Metodo <I>checaColision</I>
         * Este metodo checa la colision entre los personajes,
         * la colision de los malos con la parte inferior del applet y
         * la  colision del bueno con los extremos del applet
        */
        public void checaColision() {
            //checa la colision del personaje con las paredes de los extremos
            if (bueno.getPosX()+bueno.getAncho() > getWidth()) {
                bueno.setPosX(getWidth()-bueno.getAncho());
            }
            if (bueno.getPosX() < 0) {
                bueno.setPosX(0);
            }
            if (bueno.getPosY()+bueno.getAlto() > getHeight()) {
                bueno.setPosY(getHeight()-bueno.getAlto());
            }
            if (bueno.getPosY() < 0) {
                bueno.setPosY(0);
            }
            
            //colision entre objetos
            if (bueno.intersecta(malo)) {
                bomb.play();
                colisiono = true;
                colContador = 0;
                score+=2;
                malo.setPosX(getWidth()/2);
                malo.setPosY(getHeight()/2);
            }
        }
        
        /**
	 * Metodo <I>keyTyped</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar una tecla que no es de accion.
	 * @param e es el <code>evento</code> que se genera en al presionar las teclas.
	 */
        public void keyTyped(KeyEvent e) {
            
        }
        
        /**
	 * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
	 * @param e es el <code>evento</code> generado al presionar las teclas.
	 */
        public void keyPressed(KeyEvent e) {
            //presiono flecha izquierda
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                direccion = 2;
            //Presiono flecha derecha
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                direccion = 1;
            }
            
            //Presiono la letra p
            if (e.getKeyCode() == KeyEvent.VK_P) {
                pausa = !pausa;
            }
        }
        
        /**
	 * Metodo <I>keyReleased</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al soltar la tecla presionada.
	 * @param e es el <code>evento</code> que se genera en al soltar las teclas.
	 */
        public void keyReleased(KeyEvent e) {
            //libero flecha izquierda o libero flecha derecha
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                direccion = 0;
            }
        }
        
        /**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo se dibuja la imagen con la posicion actualizada,
	 * ademas que cuando la imagen es cargada te despliega una advertencia.
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
        
        public void paint(Graphics g) {
                //Inicializa el DoubleBuffer
                if (dbImage == null){
                    dbImage = createImage(this.getSize().width, this.getSize().height);
                    dbg = dbImage.getGraphics();
                }
                //Actualiza la imagen de fondo
                dbg.setColor(getBackground());
                dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
                //Actualiza el Foreground
                dbg.setColor(getForeground());
                paint1(dbg);
                //Dibuja la imagen actualizada
                g.drawImage(dbImage, 0, 0, this);
		  }
        
        public void paint1 (Graphics g){
            //Se pinta siempre y cuando tengas vidas
                    if(bueno!=null && malo!=null){
                        g.drawImage(bueno.getImagenI(), bueno.getPosX(), bueno.getPosY(), this);
                        g.drawImage(malo.getImagenI(), malo.getPosX(), malo.getPosY(), this);
                        //Si el juego esta en pausa se despliega que esta pauso
                        if (pausa)
                            g.drawString(bueno.getPausado(), bueno.getPosX(), bueno.getPosY()+bueno.getAlto()/2);
                        //Despliega si colisiono con alguno de los malos
                        if (colisiono && colContador < 8) {
                            g.drawString(bueno.getDesaparece(), bueno.getPosX(), bueno.getPosY()+bueno.getAlto()/2);
                            colContador++;
                        } else {
                            colisiono = false;
                            colContador = 0;
                        }
                        //Pinta el Score y las vidas en la parte superior izquierda
                        g.drawString("Score:" + Integer.toString(score), 10, 50);
                    }else{
                            //Da un mensaje mientras se carga el dibujo	
                            g.drawString("No se cargo la imagen..",20,20);
                    }
	}

    
        /**
	 * Metodo mouseClicked sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera al hacer click con el mouse
	 * sobre algun componente.
	 * e es el evento generado al hacer click con el mouse.
	 */
        public void mouseClicked(MouseEvent e) {
            
        }

        /**
	 * Metodo mousePressed sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera al presionar un botÃ³n
	 * del mouse sobre algun componente.
	 * e es el evento generado al presionar un botÃ³n del mouse sobre algun componente.
	 */
        public void mousePressed(MouseEvent e) {

        }

        /**
	 * Metodo mouseReleased sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera al soltar un botÃ³n
	 * del mouse sobre algun componente.
	 * e es el evento generado al soltar un botÃ³n del mouse sobre algun componente.
	 */
        public void mouseReleased(MouseEvent e) {

        }

        /**
	 * Metodo mouseEntered sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera cuando el mouse
	 * entra en algun componente.
	 * e es el evento generado cuando el mouse entra en algun componente.
	 */
        public void mouseEntered(MouseEvent e) {

        }

        /**
	 * Metodo mouseExited sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera cuando el mouse
	 * sale de algun componente.
	 * e es el evento generado cuando el mouse sale de algun componente.
	 */
        public void mouseExited(MouseEvent e) {

        }
}
