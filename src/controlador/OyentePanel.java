/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import modelos.Componente;
import modelos.Diagrama;
import vista.PanelDiagrama;
import vista.FormularioCodigo;
import vista.Muestracodigo;
/**
 *
 * @author Manuel Angel Muñoz S
 */
public class OyentePanel extends MouseAdapter {
    Diagrama diagrama;
    PanelDiagrama panel;
    /**
     * El punto donde se presiono el boton del mouse.
     */
    Point p1;
    /**
     * El punto donde se solto el boton del mouse.
     */
    Point p2;
    /**
     * Indica si habia uno o mas componentes seleccionados en el primer clic,
     * si es asi, se moveran todos, si no, se seleccionaran mas.
     */
    private boolean selecciono;
    private int ux, uy;
    FormularioCodigo formulario;
    Muestracodigo codigo1;
    public OyentePanel(Diagrama d, PanelDiagrama pane){
        diagrama=d;
        panel=pane;
        panel.addMouseMotionListener(this);
        panel.addMouseListener(this);
        panel.addMouseWheelListener(this);
        formulario = new FormularioCodigo();
        codigo1 = new Muestracodigo();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e); 
        p1=e.getPoint();
        
        System.out.println("selecciono " + selecciono );
        
        if(selecciono){ //primero pregunta si ya hay algo seleccionado y si se esta dando clic a ese algo
            selecciono=diagrama.confirmaSeleccion(p1, p1);
        }
        if(!selecciono){ //si no habia nada seleccionado, empieza a buscar con las coordenadas actuales
            selecciono= diagrama.seleccionar(p1, p1);
        }
        
        if(!selecciono){
            panel.setSeleccionado(true);
            panel.setP1(p1);
            panel.setP2(p1);
            System.out.println("Se seleccionaran varios componentes");
        }else{ //si va a arrastrar un elemento
            ux=p1.x;
            uy=p1.y;
            System.out.println("Se van a arrastrar uno o varios componentes");
        }
    }
    @Override
      public void mouseClicked(MouseEvent e){
          super.mouseClicked(e);
          p1=e.getPoint();
          if(selecciono){
              selecciono=diagrama.confirmaSeleccion(p1, p1);
          }
          p2=e.getPoint();
          if(selecciono&&e.getClickCount()==2){
              selecciono=diagrama.confirmaSeleccion(p1, p2);
              Componente select=diagrama.getComponenteSeleccionado();
              if(select==null){
                  System.out.println("No hay componente seleccionado");
                  return ;
              }
              
              
              //a.comp=select;
              formulario.setComp(select);
              formulario.panel=panel;
              formulario.setVisible(selecciono);
              
//              select.setCodigoInterior(a.getCodigoEscrito());
          }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if(selecciono){ //si va a arrastrar un elemento
            //System.out.println("Arrastrando... ");
            Point act=e.getPoint();
            diagrama.trasladarSeleccionados(act.x - ux, act.y - uy);
            ux=act.x;
            uy=act.y;
        }else{ //si va a seleccionar varios elementos
            //System.out.println("Seleccionando");
            p2=e.getPoint();
            //System.out.println("p2: " + p2);
            diagrama.seleccionar(p1, p2); 
            //panel.setSeleccionado(true);
            //panel.setP1(p1);
            panel.setP2(p2);
        }
        panel.repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e); 
        p2=e.getPoint();
        
        if(panel.isSeleccionado()){
            selecciono=diagrama.seleccionar(p1, p2); //para verificar si se seleccionaron todos al final        
            panel.setSeleccionado(false);
        }else {//if(selecciono) //creo que este if es innecesario porque si no esta seleccionado el panel, hay algo seleccionado
            System.out.println("Enlaza y reacomoda");
            diagrama.enlazaComponentes();
            diagrama.reacomoda();
            diagrama.confirmaEnlazado();
            //Point act=e.getPoint(); //hay que corregir el hecho de que si se 
            //cambian de lugar algunos componentes al momento de reacomodarlos, 
            //y terminan tocando a otros como para conectarse, esos componentes 
            //no quedan debidamente enlazados, solo graficamente "conectados"
            //diagrama.trasladarSeleccionados(act.x - ux, act.y - uy);
            
        }
        System.out.println("Solto");
        panel.repaint();
    }
}
