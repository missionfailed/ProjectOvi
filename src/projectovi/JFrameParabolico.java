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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class JFrameParabolico extends JFrame implements Runnable, KeyListener, MouseListener{
        private static final long serialVersionUID = 1L;
        //declaracion de variables
        private static final double GRAVEDAD = 9.81;
        private boolean pausa;  // Checa si el juego esta pausado
        private boolean colisiono;  // Checa si colisiono el bueno con algun malo
        private boolean clickPelota;    // Checa si se le dio click a la pelota
        private boolean llegaMaxAltura; // Checa si llego a la altura maxima de la parabola
        private boolean instrucciones;  // Checa si se oprimio el boton para ver las instrucciones
        private boolean off; // Checa si se quiere sonido o no
        private int colContador; // Tiempo de despliego de colision
        private int direccion;  // Direccion del Bueno
        private int velocidad;  // Velocidad del Bueno
        private int score;      // Score del juego
        private int perdida;    // Cantidad de bolas perdidas
        private int vidas;      // Cantidad de vidas
        private double velocidadInicial;    // velocidad inicial de la nube
        private double velocidadX;  // velocidad en X de la nube
        private double velocidadY;  // velocidad en Y de la nube
        private double angulo;      // angulo inicial de lanzamiento de la nube
        private double alturaMax;   // altura maxima del tiro parabolico
        private double alcanceMax;  // alcance maximo del tiro parabolico
        private int baseY;          // posicion inicial y
        private Image dbImage;	// Imagen a proyectar
        private Image ins;      // Imagen de instrucciones
        private Image creditos; // Imagen de creditos
        private Graphics dbg;	// Objeto grafico
        private SoundClip bomb;    //Objeto AudioClip 
        private SoundClip app;     //Objeto AudioClip
        private Bueno bueno;    // Objeto de la clase Bueno
        private Malo malo;   // Objeto dee la clase Malo
        private Base base;   // Objeto de base de la pelota
        private String []arr;   // Objeto de lo leeido del archivo
        //Variables control de tiempo de animacion
        private long tiempoActual;
	private long tiempoInicial;
        
         //Constructor
        public JFrameParabolico() {
            init();
            start();
        }
        
        public void init() {
            setSize(800, 500);
            pausa = false;
            colisiono = false;
            clickPelota = false;
            llegaMaxAltura = false;
            off = false;
            score = 0;
            direccion = 0;
            velocidad = 15;
            vidas = 5;
            perdida = 0;
            int posX = getWidth()/2;
            int posY = getHeight();
            bueno = new Bueno(posX,posY);
            bueno.setPosX(posX-bueno.getAncho()/2);
            bueno.setPosY(posY-bueno.getAlto()/2);
            //Se cargan los sonidos.
            bomb = new SoundClip ("/sounds/pokeball.wav");
            app = new SoundClip ("/sounds/Explosion.wav");
            ins = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/PantallaInstruccionesTRY1.png"));
            creditos = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/PantallaCreditosTRY1.png"));
            base = new Superficie(0, 0);
            base.setPosY(getHeight()-base.getAlto());
            malo = new Malo(0, 0);
            malo.setPosX(base.getAncho()/2);
            malo.setPosY(getHeight()-(base.getAlto()+malo.getAlto()));
            baseY = getHeight()-(base.getAlto()+malo.getAlto());
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
            
            if (!pausa && !instrucciones) {
                //dependiendo de la tecla que se este oprimiendo es hacia donde se mueve el personaje Bueno
                switch (direccion) {
                    case 1:
                        bueno.setPosX(bueno.getPosX() + velocidad + 2*vidas);
                        bueno.actualiza(tiempoActual);
                        break; 
                    case 2:
                        bueno.setPosX(bueno.getPosX() - velocidad - 2*vidas);
                        bueno.actualiza(tiempoActual);
                        break;
                    case 3:
                        bueno.setPosY(bueno.getPosY() - velocidad - 2*vidas);
                        bueno.actualiza(tiempoActual);
                        break;
                    case 4:
                        bueno.setPosY(bueno.getPosY() + velocidad + 2*vidas);
                        bueno.actualiza(tiempoActual);
                    case 0:
                        bueno.setPosX(bueno.getPosX());
                        break;
                }
                
                // movimiento parabolico de la nube
                if (clickPelota) {
                    malo.setPosX(malo.getPosX()+(int)velocidadX);
                    if (malo.getPosY() > alturaMax && !llegaMaxAltura) {
                        malo.setPosY(malo.getPosY()-(int)velocidadY);
                    } else {
                        llegaMaxAltura = true;
                        malo.setPosY(malo.getPosY()+(int)velocidadY);
                    }
                    
                    malo.actualiza(tiempoActual);
                }
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
            if (bueno.getPosX() < base.getAncho()) {
                bueno.setPosX(base.getAncho());
            }
            if (bueno.getPosY()+bueno.getAlto() > getHeight()) {
                bueno.setPosY(getHeight()-bueno.getAlto());
            }
            if (bueno.getPosY() < 0) {
                bueno.setPosY(0);
            }
            
            //checa colision del malo con la parte de abajo del applet
            if (malo.getPosY() + malo.getAlto() > getHeight()) {
                if (!off)
                    app.play();
                clickPelota = false;
                llegaMaxAltura = false;
                perdida++;
                if (perdida!=0 && perdida%3==0) {
                    vidas--;
                }
                malo.setPosX(base.getAncho()/2);
                malo.setPosY(getHeight()-(base.getAlto()+malo.getAlto()));
            }
            
            //colision entre objetos
            if (bueno.intersecta(malo)) {
                if (!off)
                    bomb.play();
                clickPelota = false;
                llegaMaxAltura = false;
                colisiono = true;
                colContador = 0;
                score+=2;
                malo.setPosX(base.getAncho()/2);
                malo.setPosY(getHeight()-(base.getAlto()+malo.getAlto()));
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
            //Presiono la letra P
            if (e.getKeyCode() == KeyEvent.VK_P) {
                pausa = !pausa;
            }
            //Presiono la letra G
            if (e.getKeyCode() == KeyEvent.VK_G) {
                if (!pausa && !instrucciones) {
                    try {
                        grabaArchivo();
                    } catch (IOException ex) {
                        System.out.println("Error en " + ex.toString());
                    }
                }
            }
            //Presiono la letra C
            if (e.getKeyCode() == KeyEvent.VK_C) {
                try {
                    leeArchivo();
                } catch (IOException ex) {
                    System.out.println("Error en " + ex.toString());
                }
            }
            //Presiono la letra I
            if (e.getKeyCode() == KeyEvent.VK_I) {
                instrucciones = !instrucciones;
            }
            //Presiono la letra S
            if (e.getKeyCode() == KeyEvent.VK_S) {
                off = !off;
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
            if (vidas > 0) {
                    if(bueno!=null && malo!=null && base!=null){
                        g.drawImage(bueno.getImagenI(), bueno.getPosX(), bueno.getPosY(), this);
                        g.drawImage(malo.getImagenI(), malo.getPosX(), malo.getPosY(), this);
                        g.drawImage(base.getImagenI(), base.getPosX(), base.getPosY(), this);
                        //Si el juego esta en pausa se despliega que esta pauso
                        if (pausa)
                            g.drawString(bueno.getPausado(), bueno.getPosX(), bueno.getPosY()+bueno.getAlto()/2);
                        if (instrucciones)
                            g.drawImage(ins, 0, 0, this);
                        //Pinta el Score y las vidas en la parte superior izquierda
                        g.drawString("Score:" + Integer.toString(score), 10, 70);
                        g.drawString("Vidas:" + Integer.toString(vidas), 10, 50);
                    }else{
                            //Da un mensaje mientras se carga el dibujo	
                            g.drawString("No se cargo la imagen..",20,20);
                    }
            } else {
                //imprime creditos
                g.drawImage(creditos, 0, 0, this);
            }
	}

    
        /**
	 * Metodo mouseClicked sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera al hacer click con el mouse
	 * sobre algun componente.
	 * e es el evento generado al hacer click con el mouse.
	 */
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            if (malo.mouse_contiene(x, y)) {
                if (!clickPelota) {
                    alturaMax = 0;
                    alcanceMax = getWidth();
                    
                    while (alturaMax <= 0 || alcanceMax >= getWidth()) {
                        velocidadInicial = Math.random()*74.0+1;
                        angulo = Math.random()+0.01;
                        alturaMax = baseY*1.0 - (pow(velocidadInicial, 2.0)*pow(sin(angulo), 2.0))/(2.0*GRAVEDAD);
                        alcanceMax = (pow(velocidadInicial, 2.0)*sin(2.0*angulo))/(GRAVEDAD);
                    }
                    velocidadX = velocidadInicial*cos(angulo);
                    velocidadY = velocidadInicial*sin(angulo)+GRAVEDAD;
                }
                clickPelota = true;
            }
        }

        /**
	 * Metodo mousePressed sobrescrito de la interface MouseListener.
	 * En este metodo maneja el evento que se genera al presionar un botÃ³n
	 * del mouse sobre algun componente.
	 * e es el evento generado al presionar un botÃ³n del mouse sobre algun componente.
	 */
        public void mousePressed(MouseEvent e) {
            //Se lanza la pelota

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
        /**
        * Metodo que lee a informacion de un archivo y lo agrega a un vector.
        *
        * @throws IOException
        */
        public void leeArchivo() throws IOException {
                                                          
                BufferedReader fileIn;
                try {
                        fileIn = new BufferedReader(new FileReader("gamedata.txt"));
                } catch (FileNotFoundException e){
                        File data = new File("/savedata/datos.txt");
                        PrintWriter fileOut = new PrintWriter(data);
                        fileOut.println(""+bueno.getPosX()+";"+""+bueno.getPosY()+";"+""+malo.getPosX()+";"+""+malo.getPosY()+";"+""+velocidadX+";"+""+velocidadY+";"+""+clickPelota+";"+""+llegaMaxAltura+";"+""+vidas+";"+""+perdida+";"+""+alturaMax+";"+""+off);
                        fileOut.close();
                        fileIn = new BufferedReader(new FileReader("gamedata.txt"));
                }
                String dato = fileIn.readLine();
                while(dato != null) {  
                                                        
                      arr = dato.split(";");
                      bueno.setPosX(Integer.parseInt(arr[0]));
                      bueno.setPosY(Integer.parseInt(arr[1]));
                      malo.setPosX(Integer.parseInt(arr[2]));
                      malo.setPosY(Integer.parseInt(arr[3]));
                      velocidadX = Double.parseDouble(arr[4]);
                      velocidadY = Double.parseDouble(arr[5]);
                      clickPelota = Boolean.parseBoolean(arr[6]);
                      llegaMaxAltura = Boolean.parseBoolean(arr[7]);
                      vidas = Integer.parseInt(arr[8]);
                      perdida = Integer.parseInt(arr[9]);
                      alturaMax = Double.parseDouble(arr[10]);
                      off = Boolean.parseBoolean(arr[11]);
                      dato = fileIn.readLine();
                }
                fileIn.close();
        }
        /**
        * Metodo que agrega la informacion del vector al archivo.
        *
        * @throws IOException
        */
        public void grabaArchivo() throws IOException {
                                                          
                PrintWriter fileOut = new PrintWriter(new FileWriter("gamedata.txt"));
                fileOut.println(""+bueno.getPosX()+";"+""+bueno.getPosY()+";"+""+malo.getPosX()+";"+""+malo.getPosY()+";"+""+velocidadX+";"+""+velocidadY+";"+""+clickPelota+";"+""+llegaMaxAltura+";"+""+vidas+";"+""+perdida+";"+""+alturaMax+";"+""+off);
                fileOut.close();
        }
}
