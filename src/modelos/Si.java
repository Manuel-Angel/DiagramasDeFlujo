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
import java.util.Scanner;
import modelos.compilador.Compilador;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class Si extends ComponenteContenedor{
    int romboX[];
    int romboY[];
    public Si(int x, int y) {
        super(x, y);
        arriba= new Conector(ancho/2, -30, 5, Color.BLACK);
        abajo= new Conector(ancho/2, alto+60,5, Color.BLACK);//este va a ser variable
        conectoresInternos= new Conector[2];
        conectoresInternos[0]= new Conector(ancho/2, alto+30, 5, Color.BLACK);//El conector de si
        conectoresInternos[1]= new Conector( ancho+30,alto/2, 5, Color.BLACK); //El conector de no, tambien sera variable
        componentesInternos= new Componente[2];
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
        
        //if(codigoInterior!=null)
            //g.drawString(codigoInterior, romboX[3]+10, romboY[3]+5);
        //conector si
        g.drawLine(romboX[2], romboY[2], x+conectoresInternos[0].x, y + conectoresInternos[0].y); //linea
        conectoresInternos[0].dibujar(g, this); //conector si
        g.drawString("Si", x+conectoresInternos[0].x+10, y+conectoresInternos[0].y);
        Componente aux=null;
        int xIni=0, yIni=0;
        if(componentesInternos[0]!=null){
            aux= componentesInternos[0].getComponenteFinal();
            xIni=aux.getX() + aux.getAbajo().x;
            yIni=aux.getY() + aux.getAbajo().y;
        }else{
            xIni=x+conectoresInternos[0].x;
            yIni=y+conectoresInternos[0].y;
        }
        g.drawLine(xIni, yIni, x+abajo.x, y+abajo.y); //linea del ultimo componente de si al conector abajo
        
        //conector no
        g.drawLine(romboX[1], romboY[1], x+conectoresInternos[1].x, y+ conectoresInternos[1].y); //rombo a conector no
        conectoresInternos[1].dibujar(g, this); //conector si
        g.drawString("No", x+ conectoresInternos[1].x+10, y+conectoresInternos[1].y);
        if(componentesInternos[1]!=null){
            aux= componentesInternos[1].getComponenteFinal();
            xIni=aux.getX() + aux.getAbajo().x;
            yIni=aux.getY() + aux.getAbajo().y;
        }else{
            xIni=x+conectoresInternos[1].x;
            yIni=y+conectoresInternos[1].y;
        }
        g.drawLine(xIni, yIni, xIni, y+abajo.y); //linea del ultimo componente de no al conector abajo, correcta
        //g.drawLine(x+abajo.x, y, x, y);
        
        g.drawLine(x+abajo.x, y+abajo.y, xIni, y+abajo.y);
        
        g.drawLine(x+arriba.x, y+arriba.y,romboX[0], romboY[0]);
        arriba.dibujar(g, this);
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
    }
    public String recortarCadena(FontMetrics fm, String codigo, int tab){
        int i=10;
        StringBuilder aux;
        //if(fm.stringWidth(codigo)>ancho){ //esto no era necesario
            aux=new StringBuilder(codigo.substring(0, 10));
            while(fm.stringWidth(aux.toString())<(ancho-15-tab*2) && i<codigo.length()){
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
        StringBuilder codigo= new StringBuilder();
        codigo.append("if(").append((codigoInterior!=null)?codigoInterior.trim():"1").append("){\n");
        Componente aux= componentesInternos[0];
        String linea;
        while(aux!=null){
            linea=aux.generarCodigo();
            if(linea != null && linea.length()>0){
                linea=tabular(linea);
                codigo.append(linea);
            }
            aux=aux.getSiguiente();
        }
        aux= componentesInternos[1];
        
        StringBuilder sino=new StringBuilder();
        while(aux!=null){
            aux.setCompilador(compilador);
            linea=aux.generarCodigo();
            if(linea !=null &&linea.length()>0){
                linea=tabular(linea);
                sino.append(linea);
            }
            aux=aux.getSiguiente();
        }
        if(sino.length()>0){
            codigo.append("} else {\n");
            codigo.append(sino);
            codigo.append("}\n");
        }else codigo.append("}\n");
        
        return codigo.toString();
    }
    
    @Override
    public int getAlto() {
        int a= getAlturaComponentesInt();
        abajo.y=conectoresInternos[0].y + a +30;
        return  abajo.y- arriba.y; //arriba.y es negativo, asi que si lo restamos es como sumarlo
    }

    @Override
    public int getAncho() { //****ahora dudo que todo el proceso hecho aqui tenga efecto, tal vez solo con regresar los valores basado en los conectores **aqui**
        int anchoIzq=this.ancho/2; 
        //int anchoDer=anchoIzq + 30 + 10 + 20;//30 del largo minimo de la linea, 10 de lo que movimos la palabra "si" o "no" y 20 de lo que mide la palabra 
        int anchoDer=conectoresInternos[1].x  - anchoIzq + 10+20; //*-*
        int si=anchoMaximoComp(componentesInternos[0]);
        int no=anchoMaximoComp(componentesInternos[1]); //hay que sumarle la ventaja que tiene puesto que inicia desde el conector no, que esta mas a la derecha
        int aux=0;
        anchoIzq= Math.max(anchoIzq, si>>16);
        //aux= si & (0x0000ffff); //lado derecho de los componentes de si
        //aux+= (no & (0xffff0000))>>16; //lado izquierdo de los componentes de no
        //aux+= no & (0x0000ffff); //el lado derecho de los componentes de no
        aux+= (conectoresInternos[1].x -  anchoIzq) + (no & 0x0000ffff); //*-*
        anchoDer= Math.max(anchoDer, aux);
        
        //codigoInterior= anchoIzq + ","+anchoDer;//esto es solo para hacer pruebas
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
        abajo.y=conectoresInternos[0].y + altoT +30;
        
        int anchoDer=this.ancho + 30;//30 del largo minimo de la linea
        int si=anchoMaximoComp(componentesInternos[0]);
        int no=anchoMaximoComp(componentesInternos[1]);
        int aux;
        
        aux= (si & (0x0000ffff)) +  this.ancho/2; //lado derecho de los componentes de si, mas la mitad del ancho (ya que empieza desde la mitad)
        aux+= (no & (0xffff0000))>>16; //lado izquierdo de los componentes de no
        //aux+= no & (0x0000ffff); //el lado derecho de los componentes de no //esto solo si se va a medir el ancho, no cuando se actualiza
        anchoDer= Math.max(anchoDer, aux);
        
        conectoresInternos[1].x=anchoDer;
    }
    private int getAlturaComponentesInt(){
        int altoT=0, aux=conectoresInternos[1].y - conectoresInternos[0].y;//el de no menos el de si
        Componente comp=componentesInternos[0];
        while(comp!=null){
            altoT+=comp.getAlto();
            comp=comp.getSiguiente();
        }
        comp=componentesInternos[1];
        while(comp!=null){
            aux+=comp.getAlto();
            comp=comp.getSiguiente();
        }
        return Math.max(altoT, aux);
    }

    /*@Override
    public void mouseClick(MouseEvent evento) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}