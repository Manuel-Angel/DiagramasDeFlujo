/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Scanner;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public abstract class ComponenteContenedor implements Componente {
    protected Conector abajo;
    protected Conector arriba;
    protected Conector conectoresInternos[];
    /**
     * Contiene el o los primeros componentes que forman la cadena de 
     * componentes que contiene este contenedor.
     */
    protected Componente componentesInternos[];
    /**
     * Color normal del rombo.
     */
    Color color;
    /**
     * Color que adquiere el rombo cuando esta seleccionado.
     */
    Color colorSeleccion;
    protected boolean selected;
    /**
     * La posicion en x del componente respecto al panel.
     */
    protected int x=0;
    /**
     * La posicion en y del componente respecto al panel.
     */
    protected int y=0;
    /**
     * El ancho del rombo horizontalmente.
     */
    protected int ancho;
    /**
     * El alto del rombo verticalmente.
     */
    protected int alto;
    /**
     * Contiene la direccion del siguiente componente, es decir, el que esta 
     * conectado abajo de el y que se ejecutaria despues de este componente.
    */
    protected Componente siguiente;
    /**
     * La direccion del componente anterior, es decir, el que esta conectado 
     * arriba de el y se ejecuto antes que este componente.
     */
    protected Componente anterior;
    
    protected String codigoInterior;
    public ComponenteContenedor(int x, int y){
        this.x=x;
        this.y=y;
        color= Color.MAGENTA;
        colorSeleccion=Color.BLUE;
        alto=130;//(int)(1.618*80);
        ancho=alto;
        //arriba= new Conector(ancho/2, -30, 5, Color.BLACK);
        //abajo= new Conector(ancho/2, alto+30,5, Color.BLACK);
        //los conectores deben de ser declarados diferente en cada subClase
    }
    
    @Override
    public String getCodigoInterior() {
        return codigoInterior;
    }

    @Override
    public void setCodigoInterior(String codigo) {
        codigoInterior=codigo;
    }

    //@Override
    //public void dibujar(Graphics g) {   }

    //@Override
    //public String generarCodigo() {   }

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
        // tambien hay que mover a los componentes que contiene
        for (int i = 0; i < componentesInternos.length; i++) {
            if(componentesInternos[i]!=null && !componentesInternos[i].isSelected()){
                componentesInternos[i].traslada(dx, dy);
                Componente aux= componentesInternos[i].getSiguiente();
                while(aux!=null){
                    aux.traslada(dx, dy);
                    aux=aux.getSiguiente();
                }
            }
        }
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
        if(intersectaConector(c,abajo))return true;
        for (int i = 0; i < conectoresInternos.length; i++) {
            if(intersectaConector(c,conectoresInternos[i]))return true;    
        }
        return false;
    }
    protected boolean intersectaConector(Componente c, Conector conector){
        if(c.getArriba()==null)
            return false;
        int px, py;
        px=(conector.x+x) - (c.getArriba().x+ c.getX());
        py=(conector.y+y) - (c.getArriba().y+ c.getY());
        if(Math.sqrt(px*px + py*py) < abajo.radio*2)return true;
        
        return false;
    }
    public void alineaCon(Conector con, Componente c){
        int x,y;
        x=c.getX() + con.x;
        y=c.getY() + con.y;
        this.x=  x - arriba.x;
        this.y=  y - arriba.y;
        actualizaConectores();
        acomodaComponentesInt();
    }
    @Override
    public void alineaCon(Componente c) {
        /*int x,y;
        x=c.getX() + c.getAbajo().x;
        y=c.getY() + c.getAbajo().y;
        this.x=  x - arriba.x;
        this.y=  y - arriba.y;*/
        alineaCon(c.getAbajo(), c);
    }
    public void acomodaComponentesInt(){
        int x,y;
        for (int i = 0; i < componentesInternos.length; i++) {
            if(componentesInternos[i]!=null){
                x= this.x + conectoresInternos[i].x;
                componentesInternos[i].setX(x - componentesInternos[i].getArriba().x);
                y= this.y + conectoresInternos[i].y;
                componentesInternos[i].setY(y - componentesInternos[i].getArriba().y);
                Componente aux= componentesInternos[i];//.getSiguiente();
                if(aux instanceof ComponenteContenedor){ //creo que este if tampoco es necesario porque en el alineaCon se manda a llamar el actualizaConectores y el acomoda componentes
                    ((ComponenteContenedor)aux).actualizaConectores();
                    ((ComponenteContenedor)aux).acomodaComponentesInt();
                }
                aux=aux.getSiguiente();
                if(aux!=null)aux.alineaCon(componentesInternos[i]);
            }
        }
        if(siguiente!=null){
            siguiente.alineaCon(this);
        }
    }
    
    /**
     * Agrega un componente dentro de este contenedor, si el componente no 
     * pertenece adentro, si no que se conecta a el por abajo, entonces no lo 
     * agrega a dentro y retorna falso.
     * @param c el componente a agregar
     * @return true si se agrego, falso si no
     */
    public boolean addComponente(Componente c){
        if(intersectaConector(c, abajo)) // si el conector que intersecta es el de abajo
            return false; // quiere decir que no se agrega al contenedor, si no que se conecta a el por abajo, y de eso se encargara la clase diagrama
        for (int i = 0; i < conectoresInternos.length; i++) {
            if(intersectaConector(c,conectoresInternos[i])){
                if(componentesInternos[i]==c )return true; //si ya estan enlazados no hace nada
                addComponenteInterior(c,i);
                return true;
            }
        }
        return false;
    }
    /**
     * Agrega el componente c al conector i de este contenedor
     * @param c
     * @param i 
     */
    public void addComponenteInterior(Componente c, int i){
        if(c==null){
            if(i<componentesInternos.length ){
                if(componentesInternos[i]!=null){ //para borrarlo, puede dar problemas en otros casos... o no
                    componentesInternos[i].setAnterior(null);
                }
                componentesInternos[i]=null;
            }
            return ;
        }
        if(c.getAnterior()!=null){ // para el conector de arriba del elemento a agregar, se desconecta de su actual componente y se iguala a null
            c.getAnterior().setSiguiente(null);
            //c.setAnterior(null); 
        }
        c.setAnterior(this);//mejor que no sea nulo, que sea este elemento, ya lo pense y no creo que cause problemas... tantos
        Componente fin;
        //if(c.getSiguiente()==null){// para el conector de abajo, se conecta con el componente que estaba al principio de este contenedor
            //fin=c;
            //c.setSiguiente(componentesInternos[i]);
            //componentesInternos[i].setAnterior(c);
        //}else{
            fin= c.getComponenteFinal();
            if(fin instanceof Fin){
                fin.setX(fin.getX() + 100);
                if(fin==c)return ; //si se esta intentando agregar un Fin,  retorna true pues no debe de agregarse y debe de ignorarse
                fin=fin.getAnterior();
            }
            if(fin.getSiguiente()!=null){
                fin.getSiguiente().setAnterior(null);
            }
            //fin.setSiguiente(componentesInternos[i]);
            //componentesInternos[i].setAnterior(fin);
        //}
        if(componentesInternos[i]!=null){
            fin.setSiguiente(componentesInternos[i]);
            componentesInternos[i].setAnterior(fin);
        }else fin.setSiguiente(null);
        componentesInternos[i]=c;
    }
    /**
     * Remueve un componente del contenedor si se encuentraba conectado a uno
     * de los conectores de este contenedor.
     * @param c 
     */
    public void borra(Componente c){
        //if(intersectaConector(c, abajo)) // si el conector que intersecta es el de abajo, ya se encargo la clase diagrama
            //return ; 
        for (int i = 0; i < componentesInternos.length; i++) {
            if(c==componentesInternos[i]){
                if(c.getSiguiente()==null){
                    componentesInternos[i]=null;
                }else {
                    Componente fin= c.getComponenteFinal();
                    
                    if(fin!=null){ //este if no es necesario
                        Componente nuevoFin=fin.getSiguiente();
                        if(nuevoFin!=null)
                            nuevoFin.setAnterior(this);
                        componentesInternos[i]=nuevoFin;
                    }else{
                        componentesInternos[i]=null;
                    }
                }
                
                c.setAnterior(null);
                break;
            }
        }
    }
    public int enQueConectorEsta(Componente c){
        for (int ci = 0; ci < componentesInternos.length; ci++) {
            if(componentesInternos[ci]==c)return ci;
        }
        return -1;
    }
    /**
     * Retorna 0 si el componente c esta en el contenedor y ademas es el primero
     * retorna 1 si esta en el contenedor pero no es el primero y retorna
     * 2 si no esta en el contenedor.
     * @param c
     * @return 
     */
    public int contieneComponente(Componente c){
        for (int i = 0; i < componentesInternos.length; i++) {
            if(c==componentesInternos[i]){
                return 0; //es el primero del contenedor, se remplaza inmediatamente( en el metodo borra de la clase ComponeteContenedor)
            }else{
                Componente aux=componentesInternos[i];
                while(aux!=null){
                    if(aux==c){
                        return 1;
                    }
                    aux=aux.getSiguiente();
                }
            }
        }
        return 2;
    }
    /**
     * Calcula las dimenciones de los componentes que contiene y en base a esto
     * actualiza la posicion que deben tener los conectores, si alguno de los 
     * conectores variables son el de arriba o el de abajo, deberia actualizar
     * los componentes que estan conectados a el... probablemente no haya pedo 
     * porque Diagrama se encargara de eso.
     */
    public abstract void actualizaConectores();
    protected String tabular(String cod){
        Scanner s= new Scanner(cod.trim());
        StringBuilder codigoTab=new StringBuilder();
        while(s.hasNext()){
            codigoTab.append('\t');
            codigoTab.append(s.nextLine()).append('\n');
        }
        return codigoTab.toString();
    }
}
