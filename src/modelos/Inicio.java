/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Scanner;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class Inicio implements Componente {
    Conector abajo;
    /**
     * Color normal del ovalo.
     */
    Color color;
    /**
     * Color que adquiere el ovalo cuando esta seleccionado.
     */
    Color colorSeleccion;
    private boolean selected;
    /**
     * La posicion en x del componente respecto al panel.
     */
    private int x=0;
    /**
     * La posicion en y del componente respecto al panel.
     */
    private int y=0;
    /**
     * El ancho del ovalo.
     */
    private int ancho;
    /**
     * El alto del ovalo.
     */
    private int alto;
    /**
     * Contiene la direccion del siguiente componente, es decir, el que esta 
     * conectado abajo de el y que se ejecutaria despues de este componente.
    */
    private Componente siguiente=null;
    private String codigoInterior;
    public Inicio(int x, int y){
        this.x=x;
        this.y=y;
        color= Color.CYAN;
        colorSeleccion=Color.BLUE;
        alto =80;
        ancho=(int)(alto*1.618);
        abajo= new Conector(ancho/2,alto+30,5,Color.BLACK);
    }
    @Override
    public void dibujar(Graphics g) {
        if(selected)
            g.setColor(colorSeleccion);
        else g.setColor(color);
        g.fillOval(x, y, ancho, alto);
        
        g.setColor(Color.BLACK);
        g.drawString("Inicio", x+ancho/3, y+alto/4);
        g.drawLine(x+ancho/2, y+alto, x+abajo.x, y+abajo.y);
        abajo.dibujar(g, this);
        g.setColor(Color.BLACK);
        imprimirCodigo(g);
    }
    public void imprimirCodigo(Graphics g){
        if(codigoInterior==null)return;
        Font font = new Font("Courier new", Font.PLAIN, 12);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        Scanner s= new Scanner(codigoInterior);
        int linea=0;
        while(s.hasNext() && (linea*metrics.getHeight()+15<alto) && linea<3){
            String aux=s.nextLine();
            int messageWidth = metrics.stringWidth(aux);
            if(messageWidth>(ancho-25)){
                aux=recortarCadena(metrics, aux);
            }
            g.drawString(aux, x+10, y+alto/4+10+linea*metrics.getHeight());
            linea++;
        }
    }
    public String recortarCadena(FontMetrics fm, String codigo){
        int i=10;
        StringBuilder aux;
        //if(fm.stringWidth(codigo)>ancho){
            aux=new StringBuilder(codigo.substring(0, 10));
            while(fm.stringWidth(aux.toString())<(ancho-25) && i<codigo.length()){
                aux.append(codigo.charAt(i));
                i++;
            }
            if(i<codigo.length()){
                aux.append("...");
            }
            return aux.toString();
        //}
        //return codigo;
    }
    /**
     * Genera todo el codigo de todo el programa.
     * @return el codigo completo
     */
    @Override
    public String generarCodigo() {
        StringBuilder codigoCompleto= new StringBuilder("#include<stdio.h>\n");
        codigoCompleto.append(codigoInterior);//contendra las declaraciones de variables globales
        codigoCompleto.append("\nint main(){\n");
        Componente aux=siguiente;
        String cod;
        while(aux!=null){
            cod=aux.generarCodigo();
            if(cod.length()>0){
                cod=tabular(cod);
                codigoCompleto.append(cod);
            }
            aux=aux.getSiguiente();
        }
        codigoCompleto.append('}');
        return codigoCompleto.toString();
    }
    protected String tabular(String cod){
        Scanner s= new Scanner(cod);
        StringBuilder codigoTab=new StringBuilder();
        while(s.hasNext()){
            codigoTab.append('\t');
            codigoTab.append(s.nextLine()).append('\n');
        }
        return codigoTab.toString();
    }
    @Override
    public Componente getComponenteFinal() {
        if(siguiente==null)return this;
        if(siguiente.isSelected()!=this.isSelected())return this;
        Componente aux=siguiente;
        while(aux.getSiguiente()!=null && aux.getSiguiente().isSelected()== this.isSelected()){  //buscara a los que esten en su mismo estado, si este componente esta seleccionado, buscara hasta encontrar uno no seleccionado 
            aux=aux.getSiguiente();
        }
        return aux;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x=x;
    }

    @Override
    public void setY(int y) {
        this.y=y;
    }

    @Override
    public Componente getSiguiente() {
        return siguiente;
    }

    @Override
    public Componente getAnterior() {
        return null;
    }

    @Override
    public void setSiguiente(Componente c) {
        siguiente=c;
    }

    @Override
    public void setAnterior(Componente c) {
        //no hay anterior al inicio
    }

    /**
     * @return the selected
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean estaEnArea(Point a, Point b) {
        int minX,minY, maxX, maxY;
        minX=Math.min(a.x, b.x);
        minY=Math.min(a.y, b.y);
        maxX=Math.max(a.x, b.x);
        maxY=Math.max(a.y, b.y);
        
        if(minX<=(x+ancho) && minY<=(y+alto)){ //checa esquina superior izquierda
            if(maxX >= x && maxY>=y){
                return true;
            }
        }
        return false;
    }

    @Override
    public void traslada(int dx, int dy) {
        x+=dx;
        y+=dy;
    }

    @Override
    public String getCodigoInterior() {
        return codigoInterior;
    }

    @Override
    public void setCodigoInterior(String codigo) {
        codigoInterior=codigo;
    }

    @Override
    public Componente getComponentePrincipio(boolean modo) {
        return this;
    }

    @Override
    public Conector getArriba() {
        return null;//el componente inicio no tiene conector de arriba
    }

    @Override
    public Conector getAbajo() {
        return abajo;
    }

    @Override
    public boolean intersectaConectorBajo(Componente c) {
        if(c.getArriba()==null)
            return false;
        int px, py;
        px=(abajo.x+x) - (c.getArriba().x+ c.getX());
        py=(abajo.y+y) - (c.getArriba().y+ c.getY());
        if(Math.sqrt(px*px + py*py) < abajo.radio*2)return true;
        //if(abajo.distance(c.getArriba()) < abajo.radio*2)return true;
        //System.out.println("distancia entre puntos: " + abajo.distance(c.getArriba()) +" diametro: " + abajo.radio*2);
        return false;
    }
    @Override
    public void alineaCon(Componente c) {
        //no se puede alinear con ninguno porque es el primero, los demas se alinean con el
    }

    @Override
    public int getAlto() {
        return abajo.y;
    }

    @Override
    public int getAncho() {
        int a=(ancho>>1)<<16;
        a|=ancho/2;
        return a;
    }

    /*@Override
    public void mouseClick(MouseEvent evento) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
