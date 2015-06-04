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
import modelos.compilador.Compilador;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class Fin implements Componente{
    Conector arriba;
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
    
    private Componente anterior=null;
    private String codigoInterior;
    private Compilador compilador;
    public Fin(int x, int y){
        this.x=x;
        this.y=y;
        color=Color.CYAN;
        colorSeleccion=Color.BLUE;
        alto =80;
        ancho=(int)(alto*1.618);
        arriba= new Conector(ancho/2,-30,5,Color.BLACK);
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
    public void dibujar(Graphics g) {
        if(selected)
            g.setColor(colorSeleccion);
        else g.setColor(color);
        g.fillOval(x, y, ancho, alto);
        g.setColor(Color.BLACK);
        g.drawLine(x+ancho/2, y, x+arriba.x, y+arriba.y);
        g.drawString("Fin", x+ancho/3+10, y+14);
        arriba.dibujar(g, this);
        imprimirCodigo(g);
    }
    public void imprimirCodigo(Graphics g){
        if(codigoInterior==null)return;
        Font font = new Font("Courier new", Font.PLAIN, 12);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        Scanner s= new Scanner(codigoInterior);
        int linea=0;
        while(s.hasNext() && (linea*metrics.getHeight()+15<alto)&&linea <3){
            String aux=s.nextLine();
            int messageWidth = metrics.stringWidth(aux);
            //System.out.println("linea:"+messageWidth+" ancho:"+(ancho-28));
            if(messageWidth>=(ancho-25)){
                aux=recortarCadena(metrics, aux);
            }
            g.drawString(aux, x+10, y+alto/4+10+linea*metrics.getHeight());
            linea++;
        }
    }
    public String recortarCadena(FontMetrics fm, String codigo){
        int i=10;
        StringBuilder aux;
        //if(fm.stringWidth(codigo)>ancho){//esto no era necesario
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

    @Override
    public String generarCodigo() {
        return "return 0\n";
                //+ "}";   
    }

    @Override
    public Componente getComponenteFinal() {
        return this;
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
        return null;
    }

    @Override
    public Componente getAnterior() {
        return anterior;
    }

    @Override
    public void setSiguiente(Componente c) {
        //no hay siguiente despues del fin
    }

    @Override
    public void setAnterior(Componente c) {
        anterior=c;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean s) {
        selected=s;
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
    public Componente getComponentePrincipio(boolean modo) { 
        if(anterior==null)return this;
        Componente aux=anterior;
        if(modo){
            if(anterior.isSelected()!= this.isSelected())return this;
            while(aux.getAnterior()!=null && aux.getAnterior().isSelected()==this.isSelected()){ //buscara a los que esten en su mismo estado, si este componente esta seleccionado, buscara hasta encontrar uno no seleccionado 
                aux=aux.getAnterior();
            }
            return aux;
        }
        while(aux.getAnterior()!=null){ 
                aux=aux.getAnterior();
        }
        return aux;
    }

    @Override
    public Conector getArriba() {
        return arriba;
    }

    @Override
    public Conector getAbajo() {
        return null; //no hay conector de abajo
    }

    @Override
    public boolean intersectaConectorBajo(Componente c) {
        return false;
    }
    @Override
    public void alineaCon(Componente c) {
        int x,y;
        x=c.getX() + c.getAbajo().x;
        y=c.getY() + c.getAbajo().y;
        this.x=  x - arriba.x;
        this.y=  y - arriba.y;
    }

    @Override
    public int getAlto() {
        return alto - arriba.y;
    }

    @Override
    public int getAncho() {
        int a=(ancho>>1)<<16;
        a|=ancho/2;
        return a;
    }
    /**
    * @param compilador the compilador to set
    */
    public void setCompilador(Compilador compilador) {
        this.compilador = compilador;
    }
    /*@Override
    public void mouseClick(MouseEvent evento) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
