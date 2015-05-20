/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import modelos.*;
import vista.PanelDiagrama;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class OyenteMenu implements ActionListener {

    Diagrama diagrama;
    PanelDiagrama panel;
    private JScrollPane scrollPane;
    public OyenteMenu(Diagrama diagrama, PanelDiagrama panel) {
        this.diagrama = diagrama;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String accion = e.getActionCommand();
        int x,y;
        JScrollBar barra=scrollPane.getHorizontalScrollBar();
        x=barra.getValue()+100;
        barra=scrollPane.getVerticalScrollBar();
        y=barra.getValue()+100;
        
        if (accion.equals("Salir")) {
            System.exit(0);
        }
        if (accion.equals("Inicio")) {
            Inicio comp = new Inicio(x, y);
            if (!diagrama.add(comp)) { // si no lo agrego correctamente porque ya habia un inicio...
                System.out.println("Error");
                JOptionPane.showMessageDialog(panel, "Ya hay un metodo inicio", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (accion.equals("Codigo")) {
            Codigo comp = new Codigo(x, y);
            diagrama.add(comp);
        } else if (accion.equals("Lectura")) {
            Lectura comp = new Lectura(x, y);
            diagrama.add(comp);
        } else if (accion.equals("Fin")) {
            Fin comp = new Fin(x, y);
            if (!diagrama.add(comp)) { // si no lo agrego correctamente porque ya habia un inicio...
                System.out.println("Error");
                JOptionPane.showMessageDialog(panel, "Ya hay un metodo fin", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (accion.equals("Si")){
            Si comp = new Si(x, y);
            diagrama.add(comp);
        }else if (accion.equals("Hacermientras")){
            Hacermientras comp = new Hacermientras(x, y);
            diagrama.add(comp);
        }else if (accion.equals("Mientras")){
            Mientras comp = new Mientras(x, y);
            diagrama.add(comp);
        }    else if (accion.equals("Ciclo")){
            Ciclo comp = new Ciclo(x, y);
            diagrama.add(comp);
        } else if(accion.equals("Reacomoda")){
            diagrama.reacomodaTodos();
        } else if(accion.equals("Selecciona todos")){
            diagrama.seleccionaTodos();
        }
        panel.repaint();
    }

    /**
     * @return the scrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * @param scrollPane the scrollPane to set
     */
    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

}