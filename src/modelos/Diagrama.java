/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import modelos.compilador.Compilador;

/**
 *
 * @author Manuel Angel Muñoz S
 */
public class Diagrama {
    /**
     * Contiene todos los componentes que se encuentran en pantalla, si bien 
     * aqui aparecen en un orden, no se garantiza que esten en orden de 
     * ejecucion, si se requiere este orden, se debe iterar sobre los elementos
     * como una lista enlazada.
     */
    private ArrayList<Componente> componentes;
    private Componente bufferParesAEnlazar[][];
    private int paresAEnlazar;
    private Inicio compInicial;
    private Fin compFinal;
    private Componente bufferBusqueda;
    public boolean haySeleccionado;
    public Diagrama(){
        componentes= new ArrayList<>();
        bufferParesAEnlazar= new Componente[20][2];
    }
    /**
     * Devuelve el componente que esta seleccionado, si hay mas de uno o no 
     * hay ninguno seleccionado, devuelve null.
     * @return 
     */
    public Componente getComponenteSeleccionado(){
        Componente seleccionado=null;
        for (Componente componente : componentes) {
            if(componente.isSelected()){
                if(seleccionado==null){
                    seleccionado=componente;
                }else return null;
            }
        }
        return seleccionado;
    }
    /**
     * Establece como seleccionados a todos los componentes que esten en el area
     * formada por el rectangulo cuya esquina superior derecha es p1 y la 
     * esquina inferior izquierda es p2.
     * @param p1 El punto de la esquina superior izquierda del area a seleccionar
     * @param p2 El punto de la ezquina inferior derecha del area a seleccionar
     * @return true si selecciono alguno de los componentes, falso si no.
     */
    public boolean seleccionar(Point p1, Point p2){
        haySeleccionado=false;
        for (Componente componente : componentes) {
            if(componente.estaEnArea(p1, p2)){
                componente.setSelected(true);
                haySeleccionado=true;
            }else componente.setSelected(false);
        }
        return haySeleccionado;
    }
    /**
     * Confirma que se este dando clic a uno de los componentes que previamente 
     * han sido seleccionados sin alterar la seleccion actual.
     * @param p1
     * @param p2
     * @return 
     */
    public boolean confirmaSeleccion(Point p1, Point p2){
        for (Componente componente : componentes) {
            if(componente.isSelected() && componente.estaEnArea(p1, p2)){
                return true;
            }
        }
        return false;
    }
    /**
     * Agrega un componente al diagrama.
     * @param comp El componente a agregar al diagrama.
     * @return false si se agrega un componente Inicio y este ya existe, o si se agrega otro componente de fin, se devuelve true en los demas casos.
     */
    public boolean add(Componente comp){
        if(comp instanceof Inicio){ //si se le envia un Inicio y ya existe un inicio, no lo agrega y devuelve false
            if(compInicial==null){
                compInicial= (Inicio)comp;
            }else return false;
        }
        if(comp instanceof Fin){
            if(compFinal==null){
                compFinal=(Fin)comp;
            }//else return false; //para permitir mas de un return 0;
        }
        componentes.add(comp);
        return true;
    }
    public void dibuja(Graphics g){
        for (Componente componente : componentes) {
            componente.dibujar(g);
        }
    }
    public void trasladarSeleccionados(int dx, int dy){ //hay que hacer este mas optimo, es cuadratico
        for (Componente componente : componentes) {
            if(componente.isSelected()){
                componente.traslada(dx, dy);
            }
            if(componente.getAbajo()!=null)
                componente.getAbajo().color=Color.BLACK;
            if(componente.getArriba()!=null)
                componente.getArriba().color=Color.BLACK;
        }
        paresAEnlazar=0;
        Componente seleccionado, noSelec, seleccionadof;
        for(int i=0;i<componentes.size();i++){
            if(!componentes.get(i).isSelected()){
                continue;
            }
            seleccionado=componentes.get(i).getComponentePrincipio(true); //componentes seleccionados
            seleccionadof=componentes.get(i).getComponenteFinal();
            //System.out.println("Seleccionado: " + seleccionado.getClass());
            if(seleccionado.getArriba()!=null)
                seleccionado.getArriba().color=Color.BLACK;
            
            for (int j = 0; j < componentes.size(); j++) {
                if(componentes.get(j).isSelected()){
                    continue;
                }//para todos los no seleccionados...
                noSelec=componentes.get(j);
                if(noSelec.intersectaConectorBajo(seleccionado)){  //si un componente seleccionado se intenta unir a uno no seleccionado
                    noSelec.getAbajo().color= Color.GREEN;
                    seleccionado.getArriba().color= Color.GREEN;
                    
                    bufferParesAEnlazar[paresAEnlazar][0]=noSelec;
                    bufferParesAEnlazar[paresAEnlazar][1]=seleccionado;
                    paresAEnlazar++;
                    if(paresAEnlazar==20)break; // el maximo es 20 a la vez
                    System.out.println("se intersecto " + paresAEnlazar +" "+noSelec.getClass() + "  " + seleccionado.getClass());
                    break;
                }else {
                    //System.out.println("Componentes no relacionados: " + noSelec.getClass() + "  " + seleccionado.getClass());
                    if(noSelec.getSiguiente() == seleccionado){ //probablemente no necesite esto: || (seleccionado.getAnterior()!= null && seleccionado.getAnterior()==noSelec)){  //es por si va a despegar un componente de otro, si no funciona lo cambio por el buffer
                        System.out.println("Despego componentes " + noSelec.getClass() + "  " + seleccionado.getClass());
                        noSelec.setSiguiente(null);
                        seleccionado.setAnterior(null);
                    }else
                        if(noSelec instanceof ComponenteContenedor){
                            ((ComponenteContenedor)noSelec).borra(seleccionado);
                        }
                }
                //falla si cambio de lugar un componente que estaba arriba de otro y lo pongo atras de ese, espero que con esto ya no falle. ya no fallo
                //cambiar el seleccionado por componente.get(i).getComponenteFinal()
                if(seleccionadof.intersectaConectorBajo(noSelec)){ //si un componente no seleccionado se intenta unir al componente seleccionado
                    seleccionadof.getAbajo().color= Color.GREEN;
                    noSelec.getArriba().color= Color.GREEN;
                    
                    bufferParesAEnlazar[paresAEnlazar][0]=seleccionadof;
                    bufferParesAEnlazar[paresAEnlazar][1]=noSelec;
                    paresAEnlazar++;
                    if(paresAEnlazar==20)break; // el maximo es 20 a la vez
                    System.out.println("se intersecto por conector de abajo" + paresAEnlazar +" "+seleccionadof.getClass() + "  " + noSelec.getClass());
                    break;
                    
                }else if(seleccionadof.getSiguiente()== noSelec){
                    System.out.println("Despego componentes de abajo " + seleccionadof.getClass() +"  "+ noSelec.getClass());
                    //if(seleccionado no esta en un contenedor)se desconectan, si esta en un contenedor,
                    //y es el primero del contenedor, se remplaza inmediatamente( en el metodo borra de la clase ComponeteContenedor)
                    //, si pertenece a los demas, se agrega al buffer para que el metodo enlazar de esta clase los conecte, 
                    //porque ese no pregunta si en realidad estan conectados <-- esto no funcionara
                    int r=estaEnContenedor(seleccionado);  //este si tiene que ser el primero
                    if(r==2){ //no esta adentro del contenedor
                        seleccionadof.setSiguiente(null);
                        noSelec.setAnterior(null);
                    }else if(r==1){ //esta adentro del contenedor y no es el primero
                        seleccionadof.setSiguiente(null);//por el momento pondre esto, no se me ocurre otra cosa
                        noSelec.setAnterior(null);
                    }
                }
                
            }
        }
    }
    /**
     * Retorna 0 si el componente c esta en el contenedor y ademas es el primero,
     * retorna 1 si esta en el contenedor pero no es el primero y retorna
     * 2 si no esta en el contenedor.
     * @param c
     * @return 
     */
    public int estaEnContenedor(Componente c){
        int r;
        for (Componente componente : componentes) {
            if(c!=componente && componente instanceof ComponenteContenedor){
                r=((ComponenteContenedor)componente).contieneComponente(c);
                bufferBusqueda=componente;
                if(r==0)return 0;
                if(r==1)return 1;
            }
        }
        return 2;
    }
    public void enlazaComponentes() {
        for (int i = 0; i < paresAEnlazar; i++) {
            System.out.println("El siguiente del de arriba era " + bufferParesAEnlazar[i][0].getSiguiente());
            if(bufferParesAEnlazar[i][0].getSiguiente()== bufferParesAEnlazar[i][1]) continue; //si los componentes a enlazar ya estan enlazados entre ellos no hace nada, si no, los enlaza
            if(bufferParesAEnlazar[i][0] instanceof ComponenteContenedor){
                boolean cont=((ComponenteContenedor)bufferParesAEnlazar[i][0]).addComponente(bufferParesAEnlazar[i][1]);
                if(cont)
                    continue;
            }
            if(bufferParesAEnlazar[i][0].getSiguiente()==null){
                bufferParesAEnlazar[i][0].setSiguiente(bufferParesAEnlazar[i][1]);
                if(bufferParesAEnlazar[i][1].getAnterior()==null)
                    bufferParesAEnlazar[i][1].setAnterior(bufferParesAEnlazar[i][0]);
                else {
                    bufferParesAEnlazar[i][1].getAnterior().setSiguiente(null); //el que apuntaba antes al componente ya no debe apuntar a ninguno
                    //puede que esto no sea necesario porque si se movio se debio de haber quitado el enlaze desde el metodo trasladarSeleccionados
                    bufferParesAEnlazar[i][1].setAnterior(bufferParesAEnlazar[i][0]);
                }
            }else {
                System.out.println("El siguiente del de arriba era " + bufferParesAEnlazar[i][0].getSiguiente().getClass());
                
                Componente aux=bufferParesAEnlazar[i][0].getSiguiente();
                System.out.println("El componente a enviarse atras " + aux.getClass());
                bufferParesAEnlazar[i][0].setSiguiente(bufferParesAEnlazar[i][1]);
                
                if(bufferParesAEnlazar[i][1].getAnterior()==null){
                    bufferParesAEnlazar[i][1].setAnterior(bufferParesAEnlazar[i][0]);
                }else{
                    bufferParesAEnlazar[i][1].getAnterior().setSiguiente(null);
                    bufferParesAEnlazar[i][1].setAnterior(bufferParesAEnlazar[i][0]);
                }
                Componente ultimo= bufferParesAEnlazar[i][1].getComponenteFinal();
                System.out.println("Ultimo: " + ultimo.getClass());
                if(ultimo.getSiguiente()==null){
                    ultimo.setSiguiente(aux);
                    if(ultimo.getSiguiente()==null){//quiere decir que es un componente de fin, por lo que no se le puede poner un componente siguiente
                        aux.setAnterior(null);
                        aux.setX(aux.getX()+100);
                        System.out.println("Fue desplazado por un componente final");
                    }else
                        aux.setAnterior(ultimo);
                    
                }else{
                    ultimo.getSiguiente().setAnterior(null);
                    ultimo.setSiguiente(aux);
                    aux.setAnterior(ultimo);
                }
            }
        }
    }

    public void reacomoda() { //reacomodarlos se hara un desmadre si hay que acomodar contenedores adentro de contenedores, pues el primer contenedor tambien se tiene que actualizar D:
        for (int i = 0; i < paresAEnlazar; i++) {
            Componente p= bufferParesAEnlazar[i][0].getComponentePrincipio(false);
            if(p instanceof ComponenteContenedor){ //creo que este if no es necesario
                ((ComponenteContenedor)p).actualizaConectores();
                ((ComponenteContenedor)p).acomodaComponentesInt();
            }
            if(p.getSiguiente()!=null)p.getSiguiente().alineaCon(p);
            /*if(bufferParesAEnlazar[i][0] instanceof ComponenteContenedor){
                ComponenteContenedor aux= (ComponenteContenedor)bufferParesAEnlazar[i][0];
                aux.actualizaConectores();
                aux.acomodaComponentesInt();
            }else {
                int aux=estaEnContenedor(bufferParesAEnlazar[i][i]);
                if(aux== 0 || aux==1){
                    ((ComponenteContenedor)bufferBusqueda).actualizaConectores();
                    ((ComponenteContenedor)bufferBusqueda).acomodaComponentesInt();
                }else 
                    bufferParesAEnlazar[i][1].alineaCon(bufferParesAEnlazar[i][0]); //hay un problema con los contenedores
            }*/
        }
    }
    public void reacomodaTodos(){ //esto me anima a hacer un atributo de banderas para no repetir tanto esto
        for (Componente componente : componentes) {
            Componente p= componente.getComponentePrincipio(false); //esto hara que se repitan muchas cosas
            if(p instanceof ComponenteContenedor){
                ((ComponenteContenedor)p).actualizaConectores();
                ((ComponenteContenedor)p).acomodaComponentesInt();
            }
            if(p.getSiguiente()!=null)p.getSiguiente().alineaCon(p);
        }
    }
    /**
     * Si se cambian de lugar algunos componentes al momento de reacomodarlos
     * y terminan tocando a otros como para conectarse, esos componentes 
     * no quedan debidamente enlazados, solo graficamente "conectados", este
     * metodo corrige eso buscando los que no estan enlazados y verificados que
     * efectivamente no esten conectados, y si lo estan, los enlaza
     */
    public void confirmaEnlazado() {
        Componente noEnlazadosArriba[], noEnlazadosAbajo[];
        noEnlazadosArriba= new Componente[20]; //para diagramas grandes hay que hacer mas grande estos buffer
        noEnlazadosAbajo = new Componente[20];
        int arriba=0, abajo=0;
        for (Componente componente : componentes) {
            if(componente.getAnterior()==null){
                noEnlazadosArriba[arriba]=componente;
                arriba++;
            }
            if(componente.getSiguiente()==null){
                noEnlazadosAbajo[abajo]=componente;
                abajo++;
            }
        }
        for (int i = 0; i < abajo; i++) {
            for (int j = 0; j < arriba; j++) {
                if(noEnlazadosArriba[j]!=null && noEnlazadosAbajo[i]!= 
                        noEnlazadosArriba[j] && 
                        noEnlazadosAbajo[i].intersectaConectorBajo(noEnlazadosArriba[j])){
                    if(noEnlazadosAbajo[i] instanceof ComponenteContenedor){
                        if(bufferParesAEnlazar[i][0]!=null && bufferParesAEnlazar[i][1]!=null){
                            boolean cont=((ComponenteContenedor)bufferParesAEnlazar[i][0]).addComponente(bufferParesAEnlazar[i][1]);
                            if(cont)//si se agrego al contenedor ya no se enlaza por abajo
                                continue;
                        }
                    }
                    noEnlazadosAbajo[i].setSiguiente(noEnlazadosArriba[j]);
                    noEnlazadosArriba[j].setAnterior(noEnlazadosAbajo[i]);
                    noEnlazadosArriba[j]=null;
                    //break;//no creo que esto se necesite
                }
            }
        }
    }
    public void seleccionaTodos(){
        for (Componente componente : componentes) {
            componente.setSelected(true);
        }
    }

    /**
     * @return the compInicial
     */
    public Inicio getCompInicial() {
        return compInicial;
    }

    /**
     * @param compInicial the compInicial to set
     */
    public void setCompInicial(Inicio compInicial) {
        this.compInicial = compInicial;
    }

    /**
     * @return the compFinal
     */
    public Fin getCompFinal() {
        return compFinal;
    }

    /**
     * @param compFinal the compFinal to set
     */
    public void setCompFinal(Fin compFinal) {
        this.compFinal = compFinal;
    }
    /**
     * Devuelve una representacion de String de todo el diagrama.
     * @return 
     */
    public String guardarDiagrama(){
        StringBuilder datos= new StringBuilder();
        for (int i = 0; i < componentes.size(); i++) {
            Componente aux=componentes.get(i);
            if(aux instanceof Inicio){
                datos.append("INICIO");
            }else if(aux instanceof Codigo){
                datos.append("CODIGO");
            }else if(aux instanceof Lectura){
                datos.append("LECTURA");
            }else if(aux instanceof Imprimir){
                datos.append("IMPRIMIR");
            }else if(aux instanceof Si){
                datos.append("SI");
            }else if(aux instanceof Hacermientras){
                datos.append("HACERM");
            }else if(aux instanceof Fin){
                datos.append("FIN");
            }
            String delimitador="\\$";
            datos.append(delimitador); // \$ es el delimitador
            datos.append(aux.getX()).append(delimitador); //posicion en x
            datos.append(aux.getY()).append(delimitador); //posicion en y
            if(aux.getCodigoInterior()!=null)
                datos.append(aux.getCodigoInterior()).append(delimitador);//codigo interior
            else datos.append(delimitador);
            datos.append(getIndex(aux.getSiguiente())).append(delimitador);  //la posicion del componente siguiente en el arreglo
            datos.append(getIndex(aux.getAnterior()));  //la posicion del componente anterior en el arreglo
            
            System.out.print(aux.getCodigoInterior()+" anterior: " + aux.getAnterior()+" ");
            if(aux.getAnterior()!=null){
                System.out.println(aux.getAnterior().getCodigoInterior());
                if(estaEnContenedor(aux)==0){ //los que esten en un contenedor tendran un campo extra en el registro
                    System.out.println("Esta en contenedor, se agregara un registro extra ");
                    ComponenteContenedor a=(ComponenteContenedor)(aux.getAnterior());
                    datos.append(delimitador).append(a.enQueConectorEsta(aux)); //con que conector se conecto en el contenedor
                }
            }
            datos.append("\\%fin\n");
        }
        return datos.toString();
    }
    /**
     * Busca el componente c en el arreglo de componentes y devuelve su posicion
     * en el arreglo o su "index", si no se encuentra devuelve -1.
     * @param c el componente a buscar.
     * @return el indice en donde se encontro.
     */
    public int getIndex(Componente c){
        for (int i = 0; i < componentes.size(); i++) {
            if(componentes.get(i)==c)return i;
        }
        return -1;
    }
    public boolean crearDiagrama(String codigo){
        String registros[]= codigo.split("\\\\%fin\n"); //los delimitadores pueden dar error si el usuario los escribe
        int enlazes[][]=new int[registros.length][3];
        String items[];
        int x,y, sig,ant;
        ArrayList<Componente> nuevos= new ArrayList<>();
        try{
            for (int i = 0; i < registros.length; i++) {
                items= registros[i].split("\\\\\\$");
                x=Integer.parseInt(items[1]);
                y=Integer.parseInt(items[2]);
                sig=Integer.parseInt(items[4]);
                ant=Integer.parseInt(items[5]);
                
                Componente c=null;
                enlazes[i][0]=sig;
                enlazes[i][1]=ant;
                if(items.length>=7)
                    enlazes[i][2]=Integer.parseInt(items[6]);
                else enlazes[i][2]=-1;
                if(items[0].equals("INICIO")){
                    c= new Inicio(x,y);
                    c.setCodigoInterior(items[3]);
                    compInicial=(Inicio)c;
                }else if(items[0].equals("CODIGO")){
                    c= new Codigo(x,y);
                    c.setCodigoInterior(items[3]);
                }else if(items[0].equals("LECTURA")){
                    c= new Lectura(x,y);
                    c.setCodigoInterior(items[3]);
                }else if(items[0].equals("IMPRIMIR")){
                    c= new Imprimir(x,y);
                    c.setCodigoInterior(items[3]);
                }else if(items[0].equals("SI")){
                    c= new Si(x,y);
                    c.setCodigoInterior(items[3]);
                }else if(items[0].equals("HACERM")){
                    c= new Hacermientras(x,y);
                    c.setCodigoInterior(items[3]);
                }else if(items[0].equals("FIN")){
                    c= new Fin(x,y);
                    c.setCodigoInterior(items[3]);
                    compFinal=(Fin)c; //esto no creo que sirva cuando si se extiende a soportar funciones
                }
                nuevos.add(c);
            }
            for (int i = 0; i < registros.length; i++) {
                if(enlazes[i][0]!=-1)
                    nuevos.get(i).setSiguiente(nuevos.get(enlazes[i][0]));
                if(enlazes[i][2]!=-1){
                    ComponenteContenedor cc=(ComponenteContenedor)nuevos.get(enlazes[i][1]);
                    cc.addComponenteInterior(nuevos.get(i), enlazes[i][2]);
                }else if(enlazes[i][1]!=-1)
                    nuevos.get(i).setAnterior(nuevos.get(enlazes[i][1]));
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("El archivo esta corrompido, se salio del indice: " + ex.getMessage());
            return false;
        }catch(NumberFormatException e){
            System.out.println("El archivo esta corrompido, error de formato: " + e.getMessage());
            return false;
        }
        componentes.clear();
        componentes=nuevos;
        reacomodaTodos();
        return true;
    }
}