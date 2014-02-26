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

import java.awt.Image;
import java.awt.Toolkit;

public class Superficie extends Base{
    
    /**
    * Metodo constructor que hereda los atributos de la clase <code>Base</code>.
    * @param posX es la <code>posiscion en x</code> del objeto Bueno.
    * @param posY es el <code>posiscion en y</code> del objeto Bueno.
    * @param image es la <code>imagen</code> del objeto Bueno.
    */
    public Superficie(int posX, int posY) {
        super(posX, posY);
        
        Image mushroom = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/giantmushroom.gif"));
        anim = new Animacion();
        anim.sumaCuadro(mushroom, 100);
    }
}
