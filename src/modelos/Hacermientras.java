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
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import modelos.compilador.Compilador;
import modelos.compilador.ParseException;
/**
 *
 * @author Angel
 */
public class Hacermientras extends ComponenteContenedor{
    int romboX[];
    int romboY[];
    Conector conectorSi; //el conector de que si se repite el ciclo, no es un conector interno porque no servira como socket para conectarsele mas componentes
    public Hacermientras(int x, int y) {
        super(x, y);
        color= Color.ORANGE;
        arriba= new Conector(ancho/2, -60, 5, Color.BLACK);
        abajo= new Conector(ancho/2, alto+30,5, Color.BLACK);//la variable
        conectoresInternos= new Conector[1];
        conectoresInternos[0]= new Conector(ancho/2, -30, 5, Color.BLACK);
        conectorSi=new Conector( ancho+30,alto/2, 5, Color.BLACK);
        //conectoresInternos[1]= new Conector( ancho+30,alto/2, 5, Color.BLACK);
        // en el caso de los conectores, solo cambie las coordenadas de ubicacion puesto que ya tienen, la ejecucion que haran 
        componentesInternos= new Componente[1];
        romboX= new int[4];
        romboY= new int[4];
    }

    @Override
    public void dibujar(Graphics g) {
        if(selected)
            g.setColor(colorSeleccion);
        else g.setColor(color);
        romboX[0]=x + ancho/2;
        romboY[0]=y;
        romboX[1]=x + ancho;
        romboY[1]=y + alto /2;
        romboX[2]=x + ancho/2;
        romboY[2]=y + alto;
        romboX[3]=x;
        romboY[3]=y + alto /2;
        
        g.fillPolygon(romboX, romboY, 4);
        g.setColor(Color.BLACK);
        
        //conector si
        
        Componente aux=null;
        int xIni=0, yIni=0; 
        g.drawLine(x+arriba.x, y+arriba.y,x+ conectoresInternos[0].x,y+conectoresInternos[0].y);//linea de arriba a el conectorInterno
        if(componentesInternos[0]!=null){
            aux= componentesInternos[0].getComponenteFinal();
            xIni=aux.getX() + aux.getAbajo().x;
            yIni=aux.getY() + aux.getAbajo().y;
        }else{
            xIni=x+conectoresInternos[0].x;
            yIni=y+conectoresInternos[0].y;
        }
        g.drawLine(xIni, yIni, romboX[0], romboY[0]); //linea del ultimo componente del conector interno al rombo
        
        g.drawLine(romboX[1], romboY[1], x+conectorSi.x, y + conectorSi.y); //linea del rombo al conector de si
        conectorSi.dibujar(g, this); //conector si
        g.drawString("Si", x+conectorSi.x+10, y+conectorSi.y);
        
        
        g.drawLine(x+conectoresInternos[0].x, y+conectoresInternos[0].y, x+conectorSi.x, y+conectoresInternos[0].y); //linea del conector interno hacia la derecha
        g.drawLine(x+conectorSi.x, y+conectorSi.y,x+conectorSi.x,y+conectoresInternos[0].y); //linea del conector de si hacia arriba, para conectarse con el conector interno
        //imprime la flechita
        g.drawLine(x+conectoresInternos[0].x+5, y+conectoresInternos[0].y, x+conectoresInternos[0].x+14, y+conectoresInternos[0].y-5);
        g.drawLine(x+conectoresInternos[0].x+5, y+conectoresInternos[0].y, x+conectoresInternos[0].x+14, y+conectoresInternos[0].y+5);
        
        g.drawLine(x+abajo.x, y+abajo.y,romboX[2], romboY[2]); //imprime la linea del conector de abajo 
        conectoresInternos[0].dibujar(g, this);
        arriba.dibujar(g, this);
        abajo.dibujar(g, this);
        imprimirCodigo(g);
    }
    public void imprimirCodigo(Graphics g){
        if(codigoInterior==null)return;
        Font font = new Font("Courier new", Font.PLAIN, 12);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        Scanner s= new Scanner(codigoInterior);
        int linea=0;
        int tab=20;
        while(s.hasNext() && (linea*metrics.getHeight()+15<alto) &&linea<5){
            String aux=s.nextLine();
            int messageWidth = metrics.stringWidth(aux);
            if(messageWidth>=(ancho-15-Math.abs(tab)*2)){
                aux=recortarCadena(metrics, aux, Math.abs(tab));
            } //romboX[3]+10, romboY[3]+5
            g.drawString(aux, romboX[3]+10+Math.abs(tab), romboY[3]-22+linea*metrics.getHeight());
            tab-=10;
            linea++;
        }
        imprimirErrores(metrics, g);
    }
    public String recortarCadena(FontMetrics fm, String codigo, int tab){
        int i=10;
        StringBuilder aux;
        aux=new StringBuilder(codigo.substring(0, 10));
        while(fm.stringWidth(aux.toString())<(ancho-15-tab*2) && i<codigo.length()){
            aux.append(codigo.charAt(i));
            i++;
        }
        if(i<codigo.length()){
            aux.append("...");
        }
        return aux.toString();
    }
    @Override
    public String generarCodigo() {
        StringBuilder codigo= new StringBuilder();
        codigo.append("do{\n");
        Componente aux= componentesInternos[0];
        String linea;
        while(aux!=null){
            aux.setCompilador(compilador);
            linea=aux.generarCodigo();
            if(linea != null && linea.length()>0){
                linea=tabular(linea);
                codigo.append(linea);
            }
            aux=aux.getSiguiente();
        }
        codigo.append("}while(").append(generarCodigoCondicion()).append(");\n");
        return codigo.toString();
    }

    @Override
    public int getAlto() {
        int a= getAlturaComponentesInt();
        //abajo.y=conectoresInternos[0].y + a +30;
        //abajo.y=30;
        return  abajo.y- arriba.y; //arriba.y es negativo, asi que si lo restamos es como sumarlo
    }

    @Override
    public int getAncho() {
        int anchoIzq=this.ancho/2; 
        //int anchoDer=anchoIzq + 30 + 10 + 20;
        int anchoDer=conectorSi.x-anchoIzq+10;
//Configuracion de linea y texto 30 del largo minimo de la linea, 10 de lo que movimos la palabra "si" o "no" y 20 de lo que mide la palabra 
        
        return (anchoIzq<<16) | anchoDer;
    }
    /**
     * Calcula el ancho maximo de los componentes hacia la izquierda y hacia la
     * derecha, en los 16 bits mas significativos se guarda la medida maxima del
     * ancho hacia la izquierda, y en los menos significativos que guarda el 
     * ancho maximo de en medio hacia la derecha.
     * @return El componente del que se empieza a medir el ancho maximo.
     */
    private int anchoMaximoComp(Componente c){
        int anchoIzq=0, anchoDer=0;
        int aux, aux2;
        while(c!=null){
            if(c instanceof ComponenteContenedor){
                ((ComponenteContenedor)c).actualizaConectores(); //aqui la recursion 
            }
            aux=c.getAncho(); //para cuando le pregunta su ancho ya actualizo sus conectores
            aux2= aux & 0x0000ffff;//ancho derecha
            if(aux2>anchoDer){
                anchoDer=aux2;
            }
            aux2= aux & 0xffff0000;
            aux2= aux2>>16;
            if(aux2>anchoIzq){
                anchoIzq=aux2;
            }
            c=c.getSiguiente();
        }
        return (anchoIzq<<16) | anchoDer;
    }
    /**
     * Se actualizan todos los componentes en cascada, y se va midiendo el ancho
     * desde el mas interior o el mas anidado hasta el mas exterior.
     */
    @Override
    public void actualizaConectores() { //falta actualizar el otro
        int altoT=getAlturaComponentesInt();
        //abajo.y=conectoresInternos[0].y + altoT +30; //no creo que necesite este
        conectoresInternos[0].y=-30 - altoT;
        int cambio=arriba.y;
        arriba.y=conectoresInternos[0].y-30;
        cambio-=arriba.y;
        y+=cambio;//esto hace que se actualize el componente moviendose hacia abajo
        int anchoDer=this.ancho + 30;//30 del largo minimo de la linea
        int conectorInt=anchoMaximoComp(componentesInternos[0]);
        //int no=anchoMaximoComp(componentesInternos[1]);
        int aux;
        
        aux= (conectorInt & (0x0000ffff)) +  this.ancho/2; //lado derecho de los componentes de si, mas la mitad del ancho (ya que empieza desde la mitad)
        //aux+= (no & (0xffff0000))>>16; //lado izquierdo de los componentes de no
        //aux+= no & (0x0000ffff); //el lado derecho de los componentes de no //esto solo si se va a medir el ancho, no cuando se actualiza
        anchoDer= Math.max(anchoDer, aux);
        
        //conectoresInternos[1].x=anchoDer;
        conectorSi.x=anchoDer;
    }
    private int getAlturaComponentesInt(){
        int altoT=0;//, aux=conectoresInternos[1].y - conectoresInternos[0].y;
        Componente comp=componentesInternos[0];
        while(comp!=null){
            altoT+=comp.getAlto();
            comp=comp.getSiguiente();
        }
        /*
        comp=componentesInternos[1];
        while(comp!=null){
            aux+=comp.getAlto();
            comp=comp.getSiguiente();
        }*/
        return altoT;
    }
}