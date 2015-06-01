/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelos.*;
import vista.Muestracodigo;
import vista.PanelDiagrama;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class OyenteMenu implements ActionListener {

    Diagrama diagrama;
    PanelDiagrama panel;
    private JScrollPane scrollPane;
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Texto", "txt");
    private JFileChooser jfc;
    String archivoAct;
    public OyenteMenu(Diagrama diagrama, PanelDiagrama panel) {
        this.diagrama = diagrama;
        this.panel = panel;
        jfc = new JFileChooser();
        jfc.setFileFilter(filter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Comando: "+ e.getActionCommand());
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
        } else if(accion.equals("Compilar")){
            compilar();
        } else if(accion.equals("Imprimir")){
            System.out.println("Imprimir");
            Imprimir comp= new Imprimir(x,y);
            diagrama.add(comp);
        } else if(accion.equals("Abrir")){
            abrir();
        }else if(accion.equals("Guardar")){
            System.out.println("Guardar");
            guardarArchivo();
        }else if(accion.equals("Guardar como")){
            
        }else if(accion.equals("Convertidor")){
            Muestracodigo ventana=new Muestracodigo();
            ventana.getConvertidor().setText(diagrama.getCompInicial().generarCodigo());
            ventana.setVisible(true); 
        }
        panel.repaint();
    }
    
    public void compilar(){
        if(diagrama.getCompInicial()==null){
            System.out.println("No hay componente de inicio");
            return ;
        }
        String codigo=diagrama.getCompInicial().generarCodigo();
        System.out.println("Codigo:\n");
        System.out.println(codigo);
    }
    private void guardarArchivo() {
        try {
            String guarda=null;
            if(archivoAct==null){
                String nombre = "";
                JFileChooser file = new JFileChooser();
                file.showSaveDialog(null);
                File ar=file.getSelectedFile();
                guarda = (ar!=null)? ar.toString():null;
            } else guarda=archivoAct;

            if (guarda != null) {
                /*guardamos el archivo y le damos el formato directamente,
                 * si queremos que se guarde en formato doc lo definimos como .doc*/
                String dir= guarda;//
                if(!dir.contains(".txt")){
                    dir+=".txt";
                }
                System.out.println("direccion: " + dir);
                archivoAct=dir;
                FileWriter save = new FileWriter(dir);
                save.write(diagrama.guardarDiagrama());
                save.close();
                
                /*JOptionPane.showMessageDialog(null,
                        "El archivo se a guardado Exitosamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);*/
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Su archivo no se ha guardado",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
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

    private void abrir() {
        try {
            jfc = new JFileChooser();
            jfc.setFileFilter(filter);
            int opcion = jfc.showOpenDialog(jfc);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                String ruta = jfc.getSelectedFile().getPath();
                FileReader reader;
                reader = new FileReader(ruta);
                StringBuilder sb= new StringBuilder();
                while(reader.ready()){
                    sb.append((char)reader.read());
                }
                boolean correcto=diagrama.crearDiagrama(sb.toString());
                if(correcto){
                    panel.repaint();
                    archivoAct=ruta;
                }
            } else {
                System.out.println("salio del jfc");
            }

        } catch (IOException q) {
            System.out.println("Error" + q);
        }
    }

}