/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelos.*;
import vista.PanelDiagrama;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class OyenteMenu implements ActionListener {

    Diagrama diagrama;
    PanelDiagrama panel;

    public OyenteMenu(Diagrama diagrama, PanelDiagrama panel) {
        this.diagrama = diagrama;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String accion = e.getActionCommand();

        if (accion.equals("Salir")) {
            System.exit(0);
        }
        if (accion.equals("Inicio")) {
            Inicio comp = new Inicio(150, 150);
            if (!diagrama.add(comp)) { // si no lo agrego correctamente porque ya habia un inicio...
                System.out.println("Error");
                JOptionPane.showMessageDialog(panel, "Ya hay un metodo inicio", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (accion.equals("Codigo")) {
            Codigo comp = new Codigo(150, 150);
            diagrama.add(comp);
        } else if (accion.equals("Lectura")) {
            Lectura comp = new Lectura(200, 200);
            diagrama.add(comp);
        } else if (accion.equals("Fin")) {
            Fin comp = new Fin(200, 250);
            if (!diagrama.add(comp)) { // si no lo agrego correctamente porque ya habia un inicio...
                System.out.println("Error");
                JOptionPane.showMessageDialog(panel, "Ya hay un metodo fin", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (accion.equals("Si")){
            Si comp = new Si(400, 250);
            diagrama.add(comp);
        }
        panel.repaint();
    }

}
