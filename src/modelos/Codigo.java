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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import modelos.compilador.Compilador;
import modelos.compilador.CompiladorConstants;
import modelos.compilador.ParseException;
import modelos.compilador.Token;
import vista.FormularioCodigo;

/**
 *
 * @author Manuel Angel Muñoz S
 */

public class Codigo implements Componente {
    private Compilador compilador;
    Conector abajo;
    Conector arriba;
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
    private Componente siguiente;
    /**
     * La direccion del componente anterior, es decir, el que esta conectado 
     * arriba de el y se ejecuto antes que este componente.
     */
    private Componente anterior;
    
    private String codigoInterior;
    ArrayList<String> mensajes;
    public Codigo(int x, int y){
        this.x=x;
        this.y=y;
        color= Color.GREEN;
        colorSeleccion=Color.BLUE;  
        alto=80;
        ancho=(int)(1.618*alto);
        arriba= new Conector(ancho/2, -30, 5, Color.BLACK);
        abajo= new Conector(ancho/2, alto+30,5, Color.BLACK);
    }
    @Override
    public void dibujar(Graphics g) {
        if(selected)
            g.setColor(colorSeleccion);
        else g.setColor(color);
        
        g.fillRect(x, y, ancho, alto);
        g.setColor(Color.BLACK);
        g.drawLine(x+arriba.x, y+arriba.y, x+ancho/2, y);
        g.drawLine(x+abajo.x, y+ abajo.y, x+ancho/2, y+alto);
        arriba.dibujar(g, this);
        abajo.dibujar(g, this);
        g.setColor(Color.WHITE);
        imprimirCodigo(g);
    }
    public void imprimirCodigo(Graphics g){
        if(codigoInterior==null)return;
        Font font = new Font("Courier new", Font.PLAIN, 12);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        Scanner s= new Scanner(codigoInterior);
        int linea=0;
        while(s.hasNext() && (linea*metrics.getHeight()+15<alto)){
            String aux=s.nextLine();
            int messageWidth = metrics.stringWidth(aux);
            if(messageWidth>ancho){
                aux=recortarCadena(metrics, aux);
            }
            g.drawString(aux, x, y+15+linea*metrics.getHeight());
            linea++;
        }
        int alt=metrics.getHeight()+2;
        if(mensajes!=null && mensajes.size()>0 && selected){
            for (int i = 0; i < mensajes.size(); i++) {
                g.setColor(Color.YELLOW);
                g.fillRect(x+50, y+alt*i+2, metrics.stringWidth(mensajes.get(i)), alt);
                g.setColor(Color.BLACK);
                g.drawString(mensajes.get(i), x+50, y+alt+alt*i);
            }
        }
    }
    public String recortarCadena(FontMetrics fm, String codigo){
        int i=10;
        StringBuilder aux;
        //System.out.println("Recorta");
        if(fm.stringWidth(codigo)>ancho){
            aux=new StringBuilder(codigo.substring(0, 10));
            while(fm.stringWidth(aux.toString())<(ancho-5) && i<codigo.length()){
                aux.append(codigo.charAt(i));
                i++;
            }
            if(i<codigo.length()){
                aux.append("...");
            }
            return aux.toString();
        }
        return codigo;
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
        StringBuilder codigo= new StringBuilder();
        if(codigoInterior!=null){
            FileWriter save;
            try {
                save = new FileWriter("codigo.temp");
                save.write(codigoInterior);
                save.close();
                compilador.ReInit(new InputStreamReader(new FileInputStream("codigo.temp")));
            } catch (IOException ex) {
                System.out.println("Error al abrir archivo " + ex);
                return "";
            }
        }else return "";
        try{
            compilador.codigo();
        }catch(ParseException ex){
            System.out.println("Error de sintaxis " + ex);
            compilador.mensajes.add("Error de sintaxis " + ex);
            mensajes= compilador.mensajes;
            selected=true;
            return "";
        }
        ArrayList<Token> tokens= compilador.token_source.tablaTok;
        //TreeMap<Token, Token> var=compilador.token_source.variables;//se necesitaria esto para hacer la aritmetica de tipos de datos y hacerles los cast automaticos necesarios, pero no tengo tiempo para eso
        mensajes= compilador.mensajes;
        if(mensajes!=null && mensajes.size()>0)selected=true;
        for (int i = 0; i < tokens.size(); i++) {
            switch(tokens.get(i).kind){
                case CompiladorConstants.caracter: codigo.append("char "); break;
                case CompiladorConstants.doble: codigo.append("double "); break;
                case CompiladorConstants.entero: codigo.append("int "); break;
                case CompiladorConstants.flotante: codigo.append("float "); break;
                case CompiladorConstants.largo: codigo.append("long long int "); break;
                case CompiladorConstants.FIN: codigo.append(";\n"); break;
                default:  
                    Token t=tokens.get(i);
                    codigo.append(t.image);
                    if(i>0 &&  (i+1)< tokens.size() && tokens.get(i+1).kind != CompiladorConstants.IGUAL
                            && t.kind!=tokens.get(i+1).kind)
                        codigo.append(' ');
            }
        }
        return codigo.toString();
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
    /**
     * @param compilador the compilador to set
     */
    public void setCompilador(Compilador compilador) {
        this.compilador = compilador;
    }
}
