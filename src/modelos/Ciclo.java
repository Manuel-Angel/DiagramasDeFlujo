/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import modelos.compilador.Compilador;
import vista.FormularioCodigo;
/**
 *
 * @author Angel
 */
public class Ciclo extends ComponenteContenedor {
    int a=0;
    /**
     * Color normal del rectangulo.
     */
    Color color;
    /**
     * Color que adquiere el rectangulo cuando esta seleccionado.
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
     * El ancho del rectangulo.
     */
    private int ancho;
    /**
     * El alto del rectangulo.
     */
    private int alto;
    /**
     * Contiene la direccion del siguiente componente, es decir, el que esta 
     * conectado abajo de el y que se ejecutaria despues de este componente.
    */
    private Compilador compilador;
    public Ciclo(int x, int y) {
        super(x, y);
        this.x=x;
        this.y=y;
        color= Color.GREEN;
        colorSeleccion=Color.BLUE;  
        alto=100;
        ancho=(int)(1.818*alto);
        arriba= new Conector(ancho/2, -30, 5, Color.BLACK);
        abajo= new Conector(ancho/2, alto+30,5, Color.BLACK);
        conectoresInternos= new Conector[2];
        conectoresInternos[0]= new Conector(ancho/2, alto+30, 5, Color.BLACK);//El conector de si
        conectoresInternos[1]= new Conector( ancho+30,alto/2, 5, Color.BLACK); //El conector de no, tambien sera variable
        componentesInternos= new Componente[2];
       
    }
    
    @Override
    public void dibujar(Graphics g) {
        if(selected)
            g.setColor(colorSeleccion);
        else g.setColor(color);
        
        g.fillRect(x, y, ancho, alto);
        g.setColor(Color.BLACK);
        g.drawString((codigoInterior==null)?"": codigoInterior, x, y);
        g.drawLine(x+arriba.x, y+arriba.y, x+ancho/2, y);
        g.drawLine(x+abajo.x, y+ abajo.y, x+ancho/2, y+alto);
        arriba.dibujar(g, this);
        abajo.dibujar(g, this);
        
         g.drawLine(ancho, alto, x+conectoresInternos[0].x, y + conectoresInternos[0].y); //linea
         conectoresInternos[0].dibujar(g, this); //conector si
    }
    /*
    @Override
    public void mouseClick(MouseEvent evento){
        if(evento.getClickCount()==1){
            ejemplo a = new ejemplo();
            a.setVisible(true);
        }
    }*/

    @Override
    public String generarCodigo() {
        // aqui se va a hacer todo el parseo y ese pedo del JAVACC y asi
        //por mientras supondre que el usuario ya metio el codigo en lenguaje C
        return codigoInterior;
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
        return anterior;
    }

    @Override
    public void setSiguiente(Componente c) {
        siguiente=c;
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
        a++;
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
        int x,y;
        x=c.getX() + c.getAbajo().x;
        y=c.getY() + c.getAbajo().y;
        this.x=  x - arriba.x;
        this.y=  y - arriba.y;
        if(siguiente!=null){
            siguiente.alineaCon(this);
        }
    }

    @Override
    public int getAlto() {
        return abajo.y - arriba.y;
    }

    @Override
    public int getAncho() {
        int a=(ancho>>1)<<16;
        a|=ancho/2;
        return a;
    }

    @Override
    public void actualizaConectores() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param compilador the compilador to set
     */
    public void setCompilador(Compilador compilador) {
        this.compilador = compilador;
    }
}