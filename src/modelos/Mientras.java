/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import modelos.compilador.Compilador;

/**
 *
 * @author Angel
 */
public class Mientras extends ComponenteContenedor{
    int romboX[];
    int romboY[];
    private Compilador compilador;
    public Mientras(int x, int y) {
        super(x, y);
        arriba= new Conector(ancho/2, -30, 5, Color.BLACK);
        abajo= new Conector(ancho/2, alto+60,5, Color.BLACK);//la variable
        conectoresInternos= new Conector[3];
        conectoresInternos[0]= new Conector(ancho/2, alto+30, 5, Color.BLACK);//El conector de si
        conectoresInternos[1]= new Conector( ancho+30,alto/2, 5, Color.BLACK); //El conector de no
        //conectoresInternos[2]= new Conector( ancho-30,alto/2, 5, Color.BLACK); //El conector de no
        componentesInternos= new Componente[3];
        romboX= new int[5];
        romboY= new int[5];
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
        romboX[4]=x + ancho;
        romboY[4]=y + alto/2;
        
        g.fillPolygon(romboX, romboY, 4);
        g.setColor(Color.BLACK);
        
        if(codigoInterior!=null)
            g.drawString(codigoInterior, romboX[3]+15, romboY[3]+5);
           
        //conector si
        
        g.drawLine(romboX[2], romboY[2], x+conectoresInternos[0].x, y + conectoresInternos[0].y); //linea
        conectoresInternos[0].dibujar(g, this); //conector no
        g.drawString("Si", x+conectoresInternos[0].x+10, y+conectoresInternos[0].y);
        Componente aux=null;
        int xIni=0, yIni=0;
       // g.drawLine(xIni, yIni, x+abajo.x, y+arriba.y); //linea del primer componente de si al conector 
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
        g.drawLine(romboX[1], romboY[1], x+conectoresInternos[1].x, y+ conectoresInternos[1].y); //rombo a conector
        //conectoresInternos[1].dibujar(g, this); //conector si
        //g.drawString("Si", x+ conectoresInternos[1].x+5, y+conectoresInternos[1].y);
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
    }

    @Override
    public String generarCodigo() {
        StringBuilder codigo= new StringBuilder();
       
        
        return codigo.toString();
    }

    @Override
    public int getAlto() {
        int a= getAlturaComponentesInt();
        abajo.y=conectoresInternos[0].y + a +30;
        return  abajo.y- arriba.y; //arriba.y es negativo, asi que si lo restamos es como sumarlo
    }

    @Override
    public int getAncho() {
        int anchoIzq=this.ancho/2; 
        //int anchoDer=anchoIzq + 30 + 10 + 20;//30 del largo minimo de la linea, 10 de lo que movimos la palabra "si" o "no" y 20 de lo que mide la palabra 
        int anchoDer=conectoresInternos[1].x  - anchoIzq + 10+20; //*-*
       
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
        int altoT=0, aux=conectoresInternos[1].y - conectoresInternos[0].y;
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
