/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projectovi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ovidio
 */
import java.awt.Image;
import java.awt.Toolkit;

public class Malo extends Base{
    
    private static int conteo;
    private double velocidadX;
    private double velocidadY;
    
    /**
     * Metodo constructor que hereda los atributos de la clase <code>Base</code>.
     * @param posX es la <code>posiscion en x</code> del objeto malo.
     * @param posY es el <code>posiscion en y</code> del objeto malo.
     * @param image es la <code>imagen</code> del objeto malo.
     * @param conteo es el <code>contador</code> del objeto malo.
     */
    public Malo(int posX,int posY){
            super(posX,posY);
            conteo = 0;
            
            Image lakitu1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/lakitu1.gif"));
            Image lakitu2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/lakitu2.gif"));
            anim = new Animacion();
            anim.sumaCuadro(lakitu1, 100);
            anim.sumaCuadro(lakitu2, 100);
    }
    
    /**
     * Metodo modificador usado para cambiar el contador del objeto 
     * @param conteo es la <code>contador</code> del objeto.
     */
    public void setConteo(int conteo) {
            this.conteo = conteo;
    }

    /**
     * Metodo de acceso que regresa el contador del objeto 
     * @return conteo es la <code>contador</code> del objeto.
     */
    public int getConteo() {
            return conteo;
    }
    
    /**
     * Metodo modificador usado para cambiar la velocidad en x del objeto 
     * @param conteo es la <code>velocidadX</code> del objeto.
     */
    public void setVelocidadX(double velocidadX) {
            this.velocidadX = velocidadX;
    }

    /**
     * Metodo de acceso que regresa la velocidad en X del objeto 
     * @return conteo es la <code>velocidadX</code> del objeto.
     */
    public double getVelocidadX() {
            return velocidadX;
    }
    
    /**
     * Metodo modificador usado para cambiar la velocidad en x del objeto 
     * @param conteo es la <code>velocidadX</code> del objeto.
     */
    public void setVelocidadY(double velocidadY) {
            this.velocidadY = velocidadY;
    }

    /**
     * Metodo de acceso que regresa la velocidad en X del objeto 
     * @return conteo es la <code>velocidadX</code> del objeto.
     */
    public double getVelocidadY() {
            return velocidadY;
    }
}
