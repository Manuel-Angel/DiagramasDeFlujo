/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public interface Componente {
    /**
     * Contiene la direccion del siguiente componente, es decir, el que esta 
     * conectado abajo de el y que se ejecutaria despues de este componente.
    *
    public Componente siguiente=null;
    /**
     * Contiene la direccion del componente anterior, es decir, el que esta 
     * conectado arriba de este, y que se ejecuto justo antes que este.
     *
    public Componente anterior=null;*/
    /**
     * Contiene el codigo que el usuario escribio.
     * el codigo depende de el tipo de componente que sea
     * @return El codigo que el usuario escribio
     */
    public String getCodigoInterior();
    /**
     * Asigna un codigo escrito por el usuario.
     * @param codigo 
     */
    public void setCodigoInterior(String codigo);
    /**
     * Dibuja el componente en el entorno grafico.
     * @param g 
     */
    public void  dibujar(Graphics g);
    /**
     * Convierte el componente en su respectivo codigo en C. 
     * @return 
     */
    public String generarCodigo();
    /**
     * Retorna el componente que esta al final de la fila, osea el ultimo que 
     * se ejecuta despues de este.
     * @return 
     */
    public Componente getComponenteFinal();
    /**
     * Retorna el componente que esta hasta el principio de la cadena de 
     * ejecucion, osea el primero que se ejecutaria.
     * @return 
     */
    public Componente getComponentePrincipio();
    @Override
    public String toString();
    /**
     * La posicion en x del componente respecto al panel.
     * @return x
     */
    public int getX();
    /**
     * La posicion en y del componente respecto al panel.
     * @return y
     */
    public int getY();
    /**
     * La posicion en x del componente respecto al panel.
     * @param x 
     */
    public void setX(int x);
    /**
     * La posicion en y del componente respecto al panel.
     * @param y
     */
    public void setY(int y);
    public Componente getSiguiente();
    public Componente getAnterior();
    public void setSiguiente(Componente c);
    public void setAnterior(Componente c);
    public boolean isSelected();
    public void setSelected(boolean s);
    public boolean estaEnArea(Point a, Point b);
    public void traslada(int dx, int dy);
    public Conector getArriba();
    public Conector getAbajo();
    /**
     * Verifica si el componente que se le esta mandando esta intersectando el
     * conector de abajo de este componente, i.e. el otro componente solicita 
     * unirse a este por abajo.
     * @param c
     * @return 
     */
    public boolean intersectaConectorBajo(Componente c);
    /**
     * Alinea este componente y todos los siguientes con respecto al componente 
     * que se le indica, de tal manera que el conector de arriba de este 
     * componente queda exactamente arriba (o abajo) del conector de abajo del 
     * componente indicado en el parametro.
     * @param c El componente con el cual alinearse.
     */
    public void alineaCon(Componente c);
}