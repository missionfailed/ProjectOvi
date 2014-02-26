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

public class Bueno extends Base {
    
    private final static String DESAPARECE = "DESAPARECE";
    private final static String PAUSADO = "PAUSADO";

    /**
    * Metodo constructor que hereda los atributos de la clase <code>Base</code>.
    * @param posX es la <code>posiscion en x</code> del objeto Bueno.
    * @param posY es el <code>posiscion en y</code> del objeto Bueno.
    * @param image es la <code>imagen</code> del objeto Bueno.
    */
    public Bueno(int posX,int posY){
        super(posX,posY);
       
        Image luigi1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/luigi1.gif"));
        Image luigi2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/luigi2.gif"));
        Image luigi3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/luigi3.gif"));
        anim = new Animacion();
        anim.sumaCuadro(luigi1, 100);
        anim.sumaCuadro(luigi2, 100);
        anim.sumaCuadro(luigi3, 100);
    }
    
    public String getPausado() {
        return PAUSADO;
    }
    
    public String getDesaparece() {
        return DESAPARECE;
    }
    
    public boolean mouse_contiene(int x, int y) {
            return getPerimetro().contains(x, y);
    }
}
