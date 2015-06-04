/* Compilador.java */
/* Generated By:JavaCC: Do not edit this line. Compilador.java */
package modelos.compilador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class Compilador implements CompiladorConstants {
        public ArrayList<String> mensajes= new ArrayList();
        public static void main (String args [] ) throws FileNotFoundException{
                String archivo="ejemplo.txt"; // cuando se requiera otro archivo, hay que pedir el nombre y se guarda aqui
                Reader r=new InputStreamReader(new FileInputStream(archivo));
                Compilador compilador = new Compilador(r); //en vez de System.in le pasamos un archivo
                try{
                        //compilador.analisisLexico();
                        compilador.declaracionesGlobales();
                }catch(ParseException ex){
                        System.out.println(ex.getMessage());
                }
                ArrayList<Token> tokens= compilador.token_source.tablaTok;
                TreeMap<Token,Token> v=compilador.token_source.variables;
                System.out.println("\u005cnTokens encontrados: ");
                for (Token token : tokens) {
                        System.out.printf("Tipo de token: %-12s token: %s\u005cn", CompiladorConstants.tokenImage[token.kind],token);
                }
                System.out.println("Variables: ");
                Set variables=v.keySet();
                for (Object variable : variables) {
                        System.out.printf("Nombre: %-12s tipo: %s\u005cn",variable,CompiladorConstants.tokenImage[v.get(variable).kind]);
                }
                System.out.println("Mensajes: ");
                for (int i = 0; i < compilador.mensajes.size(); i++)
                        System.out.println(compilador.mensajes.get(i));

        }

  final public void analisisLexico() throws ParseException {System.out.println("inicio");
        //ArrayList<String> comp = new ArrayList();

    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case entero:
      case flotante:
      case doble:
      case caracter:
      case largo:
      case OP_SUMA:
      case OP_RESTA:
      case NUMERO:
      case NUM_DEC:
      case MODULO:
      case IGUAL:
      case POR:
      case ENTRE:
      case FIN:
      case MENOR:
      case MAYOR:
      case ABRE:
      case CIERRA:
      case LETRA:
      case CARACTER_LITERAL:
      case VARIABLE:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case entero:{
        jj_consume_token(entero);

        break;
        }
      case flotante:{
        jj_consume_token(flotante);

        break;
        }
      case doble:{
        jj_consume_token(doble);

        break;
        }
      case caracter:{
        jj_consume_token(caracter);

        break;
        }
      case largo:{
        jj_consume_token(largo);

        break;
        }
      case OP_RESTA:{
        jj_consume_token(OP_RESTA);

        break;
        }
      case OP_SUMA:{
        jj_consume_token(OP_SUMA);

        break;
        }
      case NUMERO:{
        jj_consume_token(NUMERO);

        break;
        }
      case NUM_DEC:{
        jj_consume_token(NUM_DEC);

        break;
        }
      case MODULO:{
        jj_consume_token(MODULO);
        break;
        }
      case POR:{
        jj_consume_token(POR);

        break;
        }
      case ENTRE:{
        jj_consume_token(ENTRE);

        break;
        }
      case MENOR:{
        jj_consume_token(MENOR);
        break;
        }
      case MAYOR:{
        jj_consume_token(MAYOR);
        break;
        }
      case IGUAL:{
        jj_consume_token(IGUAL);

        break;
        }
      case LETRA:{
        jj_consume_token(LETRA);

        break;
        }
      case CARACTER_LITERAL:{
        jj_consume_token(CARACTER_LITERAL);
        break;
        }
      case VARIABLE:{
        jj_consume_token(VARIABLE);
        break;
        }
      case FIN:{
        jj_consume_token(FIN);

        break;
        }
      case ABRE:{
        jj_consume_token(ABRE);
        break;
        }
      case CIERRA:{
        jj_consume_token(CIERRA);
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(0);
  }

  final public Token tipoDeDato() throws ParseException {Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case entero:{
      t = jj_consume_token(entero);
{if ("" != null) return t;}
      break;
      }
    case flotante:{
      t = jj_consume_token(flotante);
{if ("" != null) return t;}
      break;
      }
    case doble:{
      t = jj_consume_token(doble);
{if ("" != null) return t;}
      break;
      }
    case caracter:{
      t = jj_consume_token(caracter);
{if ("" != null) return t;}
      break;
      }
    case largo:{
      t = jj_consume_token(largo);
{if ("" != null) return t;}
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void constantes() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NUMERO:{
      jj_consume_token(NUMERO);
      break;
      }
    case NUM_DEC:{
      jj_consume_token(NUM_DEC);
      break;
      }
    case CARACTER_LITERAL:{
      jj_consume_token(CARACTER_LITERAL);
      break;
      }
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void declaracionesGlobales() throws ParseException {Token t,tipo;
        //System.out.println("mensajes: "+mensajes.size() + " tabla: " + token_source.tablaTok.size() + " variables: "+token_source.variables.size());
        mensajes.clear();
        token_source.tablaTok.clear();
        token_source.variables.clear();
        int dimenciones=0;
        //System.out.println("mensajes: "+mensajes.size() + " tabla: " + token_source.tablaTok.size() + " variables: "+token_source.variables.size());

    try {
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case entero:
        case flotante:
        case doble:
        case caracter:
        case largo:{
          ;
          break;
          }
        default:
          jj_la1[4] = jj_gen;
          break label_2;
        }
        tipo = tipoDeDato();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case VARIABLE:{
          t = jj_consume_token(VARIABLE);
          break;
          }
        case LETRA:{
          t = jj_consume_token(LETRA);
          break;
          }
        default:
          jj_la1[5] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case CORCH_ABRE:{
          jj_consume_token(CORCH_ABRE);
          jj_consume_token(NUMERO);
          jj_consume_token(CORCH_CIER);
dimenciones++;
          break;
          }
        default:
          jj_la1[6] = jj_gen;
          ;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IGUAL:{
          jj_consume_token(IGUAL);
          constantes();
          break;
          }
        default:
          jj_la1[7] = jj_gen;
          ;
        }
if(token_source.variables.get(t)==null){
                                                t.dimenciones=dimenciones;
                                                dimenciones=0;
                                                token_source.variables.put(t,tipo);
                                }else mensajes.add("La variable " + t.image +" (linea:"+t.beginLine+" columna:" +t.beginColumn+")" +"ya ha sido declarada antes.");
        label_3:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case COMA:{
            ;
            break;
            }
          default:
            jj_la1[8] = jj_gen;
            break label_3;
          }
          jj_consume_token(COMA);
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case VARIABLE:{
            jj_consume_token(VARIABLE);
            break;
            }
          case LETRA:{
            t = jj_consume_token(LETRA);
            break;
            }
          default:
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case CORCH_ABRE:{
            jj_consume_token(CORCH_ABRE);
            jj_consume_token(NUMERO);
            jj_consume_token(CORCH_CIER);
dimenciones++;
            break;
            }
          default:
            jj_la1[10] = jj_gen;
            ;
          }
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case IGUAL:{
            jj_consume_token(IGUAL);
            constantes();
            break;
            }
          default:
            jj_la1[11] = jj_gen;
            ;
          }
if(token_source.variables.get(t)==null){
                                                t.dimenciones=dimenciones;
                                                dimenciones=0;
                                                token_source.variables.put(t,tipo);
                                        }else mensajes.add("La variable " + t.image +" (linea:"+t.beginLine+" columna:" +t.beginColumn+")" +"ya ha sido declarada antes.");
        }
        jj_consume_token(FIN);
      }
    } catch (ParseException ex) {
System.out.println("que pedo " + ex);
                mensajes.add("Error sintactico: " +ex);
    } catch (TokenMgrError te) {
//do{
                        //System.out.println("error lexico, caracter incorrecto: "+t.image+" en "+t.beginLine+" columna:"+t.beginColumn);
                //} while (t.kind!=EOL && t.kind !=EOF  );
           //throw new ParseException("error lexico, caracter incorrecto: "+t.image+" en "+t.beginLine+" columna:"+t.beginColumn+" ocurrio en element");
           mensajes.add("Error lexico: "+ te.getMessage());
    }
int ult=token_source.tablaTok.size()-1,i;
                for(i=ult;i>=0 && !token_source.tablaTok.get(i).image.equals(";");i--);

                if(i!=ult){
                        Token er=token_source.tablaTok.get(i+1);
                        mensajes.add("*Error sintactico en token: "+ er +" en linea "+er.beginLine +" columna: "+er.beginColumn);
                }
  }

  final public void lectura() throws ParseException {token_source.tablaTok.clear();
        //token_source.tablaTok=new ArrayList();
        mensajes=new ArrayList();
        Token t,tipo,aux;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case VARIABLE:{
      t = jj_consume_token(VARIABLE);
      break;
      }
    case LETRA:{
      t = jj_consume_token(LETRA);
      break;
      }
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
aux=token_source.variables.get(t);
                if(aux==null){
                        mensajes.add("La variable " + t.image +" (linea:"+t.beginLine+" columna:" +t.beginColumn+")" +" aun no ha sido declarada.");
                }
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case COMA:{
        ;
        break;
        }
      default:
        jj_la1[13] = jj_gen;
        break label_4;
      }
      jj_consume_token(COMA);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case VARIABLE:{
        t = jj_consume_token(VARIABLE);
        break;
        }
      case LETRA:{
        t = jj_consume_token(LETRA);
        break;
        }
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
aux=token_source.variables.get(t);
                        if(aux==null){
                                mensajes.add("La variable " + t.image +" (linea:"+t.beginLine+" columna:" +t.beginColumn+")" +" aun no ha sido declarada.");
                        }
    }
  }

  /** Generated Token Manager. */
  public CompiladorTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[15];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x387fffe0,0x387fffe0,0x3e0,0x10003000,0x3e0,0x28000000,0x2000000,0x8000,0x800000,0x28000000,0x2000000,0x8000,0x28000000,0x800000,0x28000000,};
   }

  /** Constructor with InputStream. */
  public Compilador(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Compilador(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new CompiladorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Compilador(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new CompiladorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Compilador(CompiladorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CompiladorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[30];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 15; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 30; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
