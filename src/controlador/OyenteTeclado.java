
package controlador;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import modelos.Diagrama;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class OyenteTeclado implements KeyListener {
    Diagrama diagrama;
    JPanel panel;
    public OyenteTeclado(Diagrama dia, JPanel p){
        diagrama=dia;
        panel=p;
        p.addKeyListener(this);
        panel.setFocusable(true);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("tecla: " + e.getKeyChar());
        System.out.printf("ascci %d\n", (int)e.getKeyChar());
        if(e.getKeyChar()==127){
            diagrama.borraComponentesSeleccionados();
            diagrama.reacomodaTodos();
            diagrama.confirmaEnlazado();
            panel.repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
