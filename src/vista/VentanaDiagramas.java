/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import controlador.OyenteMenu;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class VentanaDiagramas extends javax.swing.JFrame {

    /**
     * Creates new form VentanaDiagramas
     */
    public VentanaDiagramas(String nombre){
        super(nombre);
        initComponents();
    }
    public VentanaDiagramas() {
        initComponents();
    }
    public void addEventos(OyenteMenu o){
        salir.addActionListener(o);
        
        inicio.addActionListener(o);
        codigo.addActionListener(o);
        lectura.addActionListener(o);
        si.addActionListener(o);
        fin.addActionListener(o);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        archivo = new javax.swing.JMenu();
        salir = new javax.swing.JMenuItem();
        insertar = new javax.swing.JMenu();
        inicio = new javax.swing.JMenuItem();
        codigo = new javax.swing.JMenuItem();
        lectura = new javax.swing.JMenuItem();
        si = new javax.swing.JMenuItem();
        fin = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        archivo.setText("Archivo");

        salir.setText("Salir");
        archivo.add(salir);

        jMenuBar1.add(archivo);

        insertar.setText("Insertar");

        inicio.setText("Inicio");
        insertar.add(inicio);

        codigo.setText("Codigo");
        insertar.add(codigo);

        lectura.setText("Lectura");
        insertar.add(lectura);

        si.setText("Si");
        insertar.add(si);

        fin.setText("Fin");
        insertar.add(fin);

        jMenuBar1.add(insertar);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaDiagramas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaDiagramas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaDiagramas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaDiagramas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaDiagramas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu archivo;
    private javax.swing.JMenuItem codigo;
    private javax.swing.JMenuItem fin;
    private javax.swing.JMenuItem inicio;
    private javax.swing.JMenu insertar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem lectura;
    private javax.swing.JMenuItem salir;
    private javax.swing.JMenuItem si;
    // End of variables declaration//GEN-END:variables
}
