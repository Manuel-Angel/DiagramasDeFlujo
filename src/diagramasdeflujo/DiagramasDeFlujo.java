/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagramasdeflujo;

import controlador.OyenteMenu;
import controlador.OyentePanel;
import controlador.OyenteTeclado;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import modelos.*;
import vista.PanelDiagrama;
import vista.VentanaDiagramas;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class DiagramasDeFlujo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Diagrama diagrama= new Diagrama();
        PanelDiagrama panel= new PanelDiagrama(diagrama);
        //Inicio prueba= new Inicio(50,50);
        //diagrama.add(prueba);
        //Codigo c= new Codigo(0,0);
        
        OyentePanel oyente= new OyentePanel(diagrama, panel);
        OyenteMenu oyenteM= new OyenteMenu(diagrama, panel);
        
        VentanaDiagramas ventana= new VentanaDiagramas("Diagramas de flujo");
        ventana.addEventos(oyenteM);
        
        OyenteTeclado oyenteTe= new OyenteTeclado(diagrama,panel);
        panel.setPreferredSize(new Dimension(2000,3000));
        JScrollPane scroll= new JScrollPane(panel);
        
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //JScrollBar barraH=scroll.getHorizontalScrollBar();
        oyenteM.setScrollPane(scroll);
        //System.out.println("maximo: "+ barraH.getMaximum() + " minimo: " + barraH.getMinimum() + " " + barraH.getValue());
        //barraH.setValue(100);
        ///ventana.add(scroll);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800,700);
        ventana.setLocation(10, 10);
        ventana.setContentPane(scroll);
        ventana.setVisible(true);
        
        
    }
    
}
