
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20160615 (GIT 4ac7450)
//----------------------------------------------------

package cup;

import java_cup.runtime.*;
import java_cup.runtime.XMLElement;

/** CUP v0.11b 20160615 (GIT 4ac7450) generated parser.
  */
@SuppressWarnings({"rawtypes"})
public class parser extends java_cup.runtime.lr_parser {

 public final Class getSymbolContainer() {
    return sym.class;
}

  /** Default constructor. */
  @Deprecated
  public parser() {super();}

  /** Constructor which sets the default scanner. */
  @Deprecated
  public parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\056\000\002\002\004\000\002\002\005\000\002\002" +
    "\005\000\002\002\005\000\002\002\003\000\002\002\003" +
    "\000\002\002\003\000\002\002\003\000\002\002\003\000" +
    "\002\002\003\000\002\003\005\000\002\003\012\000\002" +
    "\004\003\000\002\004\003\000\002\004\003\000\002\004" +
    "\003\000\002\004\003\000\002\005\003\000\002\005\003" +
    "\000\002\005\003\000\002\005\003\000\002\005\003\000" +
    "\002\005\003\000\002\005\003\000\002\005\003\000\002" +
    "\005\003\000\002\005\003\000\002\006\010\000\002\006" +
    "\012\000\002\007\003\000\002\007\003\000\002\007\003" +
    "\000\002\007\003\000\002\007\003\000\002\007\003\000" +
    "\002\007\003\000\002\010\004\000\002\010\004\000\002" +
    "\011\003\000\002\011\003\000\002\012\003\000\002\012" +
    "\003\000\002\013\005\000\002\014\005\000\002\015\005" +
    "\000\002\016\005" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\121\000\042\005\015\006\027\010\014\011\005\012" +
    "\010\013\013\014\024\015\011\016\030\017\022\020\023" +
    "\021\012\022\017\023\021\050\033\051\016\001\002\000" +
    "\032\002\ufffd\007\ufffd\026\ufffd\027\ufffd\030\ufffd\031\ufffd" +
    "\032\ufffd\033\ufffd\034\ufffd\035\ufffd\036\ufffd\037\ufffd\001" +
    "\002\000\034\002\ufff4\007\ufff4\026\ufff4\027\ufff4\030\ufff4" +
    "\031\ufff4\032\ufff4\033\ufff4\034\ufff4\035\ufff4\036\ufff4\037" +
    "\ufff4\040\ufff4\001\002\000\032\002\ufff9\007\ufff9\026\ufff9" +
    "\027\ufff9\030\ufff9\031\ufff9\032\ufff9\033\ufff9\034\ufff9\035" +
    "\ufff9\036\ufff9\037\ufff9\001\002\000\032\002\ufff8\007\ufff8" +
    "\026\ufff8\027\ufff8\030\ufff8\031\ufff8\032\ufff8\033\ufff8\034" +
    "\ufff8\035\ufff8\036\ufff8\037\ufff8\001\002\000\034\002\ufff3" +
    "\007\ufff3\026\ufff3\027\ufff3\030\ufff3\031\ufff3\032\ufff3\033" +
    "\ufff3\034\ufff3\035\ufff3\036\ufff3\037\ufff3\040\ufff3\001\002" +
    "\000\010\005\uffe4\024\uffe4\025\uffe4\001\002\000\010\005" +
    "\uffe0\024\uffe0\025\uffe0\001\002\000\034\002\ufff2\007\ufff2" +
    "\026\ufff2\027\ufff2\030\ufff2\031\ufff2\032\ufff2\033\ufff2\034" +
    "\ufff2\035\ufff2\036\ufff2\037\ufff2\040\ufff2\001\002\000\034" +
    "\002\ufff5\007\ufff5\026\ufff5\027\ufff5\030\ufff5\031\ufff5\032" +
    "\ufff5\033\ufff5\034\ufff5\035\ufff5\036\ufff5\037\ufff5\040\ufff5" +
    "\001\002\000\010\044\053\046\105\047\122\001\002\000" +
    "\032\002\uffd8\007\uffd8\026\uffd8\027\uffd8\030\uffd8\031\uffd8" +
    "\032\uffd8\033\uffd8\034\uffd8\035\uffd8\036\uffd8\037\uffd8\001" +
    "\002\000\010\005\uffdf\024\uffdf\025\uffdf\001\002\000\030" +
    "\002\121\026\071\027\066\030\072\031\064\032\060\033" +
    "\073\034\063\035\061\036\062\037\067\001\002\000\010" +
    "\005\uffde\024\uffde\025\uffde\001\002\000\010\005\uffe2\024" +
    "\uffe2\025\uffe2\001\002\000\010\005\uffe1\024\uffe1\025\uffe1" +
    "\001\002\000\034\002\ufff1\007\ufff1\026\ufff1\027\ufff1\030" +
    "\ufff1\031\ufff1\032\ufff1\033\ufff1\034\ufff1\035\ufff1\036\ufff1" +
    "\037\ufff1\040\ufff1\001\002\000\010\005\043\024\075\025" +
    "\077\001\002\000\032\002\ufffa\007\ufffa\026\ufffa\027\ufffa" +
    "\030\ufffa\031\ufffa\032\ufffa\033\ufffa\034\ufffa\035\ufffa\036" +
    "\ufffa\037\ufffa\001\002\000\042\005\015\006\027\010\014" +
    "\011\005\012\010\013\013\014\024\015\011\016\030\017" +
    "\022\020\023\021\012\022\017\023\021\050\033\051\016" +
    "\001\002\000\010\005\uffe3\024\uffe3\025\uffe3\001\002\000" +
    "\032\002\ufffb\007\ufffb\026\ufffb\027\ufffb\030\ufffb\031\ufffb" +
    "\032\ufffb\033\ufffb\034\ufffb\035\ufffb\036\ufffb\037\ufffb\001" +
    "\002\000\034\002\ufffc\007\ufffc\026\ufffc\027\ufffc\030\ufffc" +
    "\031\ufffc\032\ufffc\033\ufffc\034\ufffc\035\ufffc\036\ufffc\037" +
    "\ufffc\040\034\001\002\000\032\002\uffd9\007\uffd9\026\uffd9" +
    "\027\uffd9\030\uffd9\031\uffd9\032\uffd9\033\uffd9\034\uffd9\035" +
    "\uffd9\036\uffd9\037\uffd9\001\002\000\006\006\037\041\035" +
    "\001\002\000\004\005\043\001\002\000\032\002\ufffe\007" +
    "\ufffe\026\ufffe\027\ufffe\030\ufffe\031\ufffe\032\ufffe\033\ufffe" +
    "\034\ufffe\035\ufffe\036\ufffe\037\ufffe\001\002\000\006\006" +
    "\037\041\035\001\002\000\004\007\041\001\002\000\034" +
    "\002\ufff7\004\ufff7\007\ufff7\026\ufff7\027\ufff7\030\ufff7\031" +
    "\ufff7\032\ufff7\033\ufff7\034\ufff7\035\ufff7\036\ufff7\037\ufff7" +
    "\001\002\000\004\004\046\001\002\000\004\045\044\001" +
    "\002\000\004\005\045\001\002\000\004\004\uffd7\001\002" +
    "\000\004\005\043\001\002\000\004\004\050\001\002\000" +
    "\004\005\052\001\002\000\004\042\055\001\002\000\004" +
    "\044\053\001\002\000\004\005\054\001\002\000\036\002" +
    "\uffd4\004\uffd4\007\uffd4\026\uffd4\027\uffd4\030\uffd4\031\uffd4" +
    "\032\uffd4\033\uffd4\034\uffd4\035\uffd4\036\uffd4\037\uffd4\042" +
    "\uffd4\001\002\000\004\005\052\001\002\000\034\002\ufff6" +
    "\004\ufff6\007\ufff6\026\ufff6\027\ufff6\030\ufff6\031\ufff6\032" +
    "\ufff6\033\ufff6\034\ufff6\035\ufff6\036\ufff6\037\ufff6\001\002" +
    "\000\030\007\070\026\071\027\066\030\072\031\064\032" +
    "\060\033\073\034\063\035\061\036\062\037\067\001\002" +
    "\000\042\005\uffec\006\uffec\010\uffec\011\uffec\012\uffec\013" +
    "\uffec\014\uffec\015\uffec\016\uffec\017\uffec\020\uffec\021\uffec" +
    "\022\uffec\023\uffec\050\uffec\051\uffec\001\002\000\042\005" +
    "\uffe9\006\uffe9\010\uffe9\011\uffe9\012\uffe9\013\uffe9\014\uffe9" +
    "\015\uffe9\016\uffe9\017\uffe9\020\uffe9\021\uffe9\022\uffe9\023" +
    "\uffe9\050\uffe9\051\uffe9\001\002\000\042\005\uffe8\006\uffe8" +
    "\010\uffe8\011\uffe8\012\uffe8\013\uffe8\014\uffe8\015\uffe8\016" +
    "\uffe8\017\uffe8\020\uffe8\021\uffe8\022\uffe8\023\uffe8\050\uffe8" +
    "\051\uffe8\001\002\000\042\005\uffea\006\uffea\010\uffea\011" +
    "\uffea\012\uffea\013\uffea\014\uffea\015\uffea\016\uffea\017\uffea" +
    "\020\uffea\021\uffea\022\uffea\023\uffea\050\uffea\051\uffea\001" +
    "\002\000\042\005\uffed\006\uffed\010\uffed\011\uffed\012\uffed" +
    "\013\uffed\014\uffed\015\uffed\016\uffed\017\uffed\020\uffed\021" +
    "\uffed\022\uffed\023\uffed\050\uffed\051\uffed\001\002\000\042" +
    "\005\015\006\027\010\014\011\005\012\010\013\013\014" +
    "\024\015\011\016\030\017\022\020\023\021\012\022\017" +
    "\023\021\050\033\051\016\001\002\000\042\005\uffef\006" +
    "\uffef\010\uffef\011\uffef\012\uffef\013\uffef\014\uffef\015\uffef" +
    "\016\uffef\017\uffef\020\uffef\021\uffef\022\uffef\023\uffef\050" +
    "\uffef\051\uffef\001\002\000\042\005\uffe7\006\uffe7\010\uffe7" +
    "\011\uffe7\012\uffe7\013\uffe7\014\uffe7\015\uffe7\016\uffe7\017" +
    "\uffe7\020\uffe7\021\uffe7\022\uffe7\023\uffe7\050\uffe7\051\uffe7" +
    "\001\002\000\032\002\000\007\000\026\000\027\000\030" +
    "\000\031\000\032\000\033\000\034\000\035\000\036\000" +
    "\037\000\001\002\000\042\005\ufff0\006\ufff0\010\ufff0\011" +
    "\ufff0\012\ufff0\013\ufff0\014\ufff0\015\ufff0\016\ufff0\017\ufff0" +
    "\020\ufff0\021\ufff0\022\ufff0\023\ufff0\050\ufff0\051\ufff0\001" +
    "\002\000\042\005\uffee\006\uffee\010\uffee\011\uffee\012\uffee" +
    "\013\uffee\014\uffee\015\uffee\016\uffee\017\uffee\020\uffee\021" +
    "\uffee\022\uffee\023\uffee\050\uffee\051\uffee\001\002\000\042" +
    "\005\uffeb\006\uffeb\010\uffeb\011\uffeb\012\uffeb\013\uffeb\014" +
    "\uffeb\015\uffeb\016\uffeb\017\uffeb\020\uffeb\021\uffeb\022\uffeb" +
    "\023\uffeb\050\uffeb\051\uffeb\001\002\000\032\002\uffff\007" +
    "\uffff\026\071\027\066\030\072\031\064\032\060\033\073" +
    "\034\063\035\061\036\062\037\067\001\002\000\010\005" +
    "\uffdb\006\uffdb\041\uffdb\001\002\000\004\004\115\001\002" +
    "\000\010\005\uffda\006\uffda\041\uffda\001\002\000\004\004" +
    "\107\001\002\000\010\005\103\006\037\041\035\001\002" +
    "\000\004\004\uffdd\001\002\000\004\046\105\001\002\000" +
    "\004\004\uffdc\001\002\000\004\005\106\001\002\000\034" +
    "\002\uffd6\004\uffd6\007\uffd6\026\uffd6\027\uffd6\030\uffd6\031" +
    "\uffd6\032\uffd6\033\uffd6\034\uffd6\035\uffd6\036\uffd6\037\uffd6" +
    "\001\002\000\004\005\043\001\002\000\004\004\111\001" +
    "\002\000\004\005\043\001\002\000\004\004\113\001\002" +
    "\000\042\005\015\006\027\010\014\011\005\012\010\013" +
    "\013\014\024\015\011\016\030\017\022\020\023\021\012" +
    "\022\017\023\021\050\033\051\016\001\002\000\032\002" +
    "\uffe5\007\uffe5\026\071\027\066\030\072\031\064\032\060" +
    "\033\073\034\063\035\061\036\062\037\067\001\002\000" +
    "\004\005\043\001\002\000\004\004\117\001\002\000\042" +
    "\005\015\006\027\010\014\011\005\012\010\013\013\014" +
    "\024\015\011\016\030\017\022\020\023\021\012\022\017" +
    "\023\021\050\033\051\016\001\002\000\032\002\uffe6\007" +
    "\uffe6\026\071\027\066\030\072\031\064\032\060\033\073" +
    "\034\063\035\061\036\062\037\067\001\002\000\004\002" +
    "\001\001\002\000\004\005\123\001\002\000\032\002\uffd5" +
    "\007\uffd5\026\uffd5\027\uffd5\030\uffd5\031\uffd5\032\uffd5\033" +
    "\uffd5\034\uffd5\035\uffd5\036\uffd5\037\uffd5\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\121\000\022\002\017\004\031\006\003\007\024\012" +
    "\006\014\030\015\005\016\025\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\005\064\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\010\010\077\011\100\013\075\001\001\000\002" +
    "\001\001\000\022\002\056\004\031\006\003\007\024\012" +
    "\006\014\030\015\005\016\025\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\003\035\001\001\000\004\013\041\001\001\000\002" +
    "\001\001\000\004\003\037\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\004\013\046\001\001\000" +
    "\002\001\001\000\004\016\050\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\016\055\001\001\000\002\001\001\000\004\005\064" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\022\002\073" +
    "\004\031\006\003\007\024\012\006\014\030\015\005\016" +
    "\025\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\004\005\064\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\006\003" +
    "\103\014\101\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\013\107\001\001\000\002\001\001\000\004\013\111" +
    "\001\001\000\002\001\001\000\022\002\113\004\031\006" +
    "\003\007\024\012\006\014\030\015\005\016\025\001\001" +
    "\000\004\005\064\001\001\000\004\013\115\001\001\000" +
    "\002\001\001\000\022\002\117\004\031\006\003\007\024" +
    "\012\006\014\030\015\005\016\025\001\001\000\004\005" +
    "\064\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


/** Cup generated class to encapsulate user supplied action code.*/
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class CUP$parser$actions {
  private final parser parser;

  /** Constructor */
  CUP$parser$actions(parser parser) {
    this.parser = parser;
  }

  /** Method 0 with the actual generated action code for actions 0 to 300. */
  public final java_cup.runtime.Symbol CUP$parser$do_action_part00000000(
    int                        CUP$parser$act_num,
    java_cup.runtime.lr_parser CUP$parser$parser,
    java.util.Stack            CUP$parser$stack,
    int                        CUP$parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$parser$result;

      /* select the action based on the action number */
      switch (CUP$parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= term EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		RESULT = start_val;
              CUP$parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$parser$parser.done_parsing();
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // term ::= LEFTPAREN term RIGHTPAREN 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // term ::= term binop term 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // term ::= key FOUND lookup 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // term ::= eval 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // term ::= key 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // term ::= aponetwork 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // term ::= apoidentifier 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // term ::= apoflags 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // term ::= const 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("term",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // lookup ::= LEFTPAREN lookup RIGHTPAREN 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("lookup",1, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // lookup ::= LISTED apotime COMMA apotime COMMA apoidentifier AS apoidentifier 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("lookup",1, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // key ::= SRC 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("key",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // key ::= DST 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("key",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // key ::= PROTO 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("key",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // key ::= PRT 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("key",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // key ::= FLAGS 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("key",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // binop ::= MATCHES 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // binop ::= GT 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // binop ::= LT 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // binop ::= EQ 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // binop ::= AND 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // binop ::= OR 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // binop ::= PLUS 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 24: // binop ::= MINUS 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 25: // binop ::= MULTIPLY 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 26: // binop ::= DIVISION 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("binop",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 27: // eval ::= eval_op apotime COMMA apotime COMMA term 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval",4, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 28: // eval ::= eval_op scope COMMA apotime COMMA apotime COMMA term 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval",4, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 29: // eval_op ::= SUM 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 30: // eval_op ::= COUNT 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 31: // eval_op ::= MIN 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 32: // eval_op ::= MAX 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 33: // eval_op ::= DCARD 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 34: // eval_op ::= SCARD 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 35: // eval_op ::= PCARD 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("eval_op",5, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 36: // scope ::= scope_op aponetwork 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("scope",6, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 37: // scope ::= scope_op lookup 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("scope",6, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 38: // scope_op ::= SSCOPE 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("scope_op",7, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 39: // scope_op ::= DSCOPE 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("scope_op",7, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 40: // const ::= INT_flex 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("const",8, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 41: // const ::= FLOAT_flex 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("const",8, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 42: // apotime ::= APO TIME APO 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("apotime",9, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 43: // aponetwork ::= APO NETWORK APO 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("aponetwork",10, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 44: // apoflags ::= APO FLAGS_string APO 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("apoflags",11, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 45: // apoidentifier ::= APO IDENTIFIER APO 
            {
              Object RESULT =null;

              CUP$parser$result = parser.getSymbolFactory().newSymbol("apoidentifier",12, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number "+CUP$parser$act_num+"found in internal parse table");

        }
    } /* end of method */

  /** Method splitting the generated action code into several parts. */
  public final java_cup.runtime.Symbol CUP$parser$do_action(
    int                        CUP$parser$act_num,
    java_cup.runtime.lr_parser CUP$parser$parser,
    java.util.Stack            CUP$parser$stack,
    int                        CUP$parser$top)
    throws java.lang.Exception
    {
              return CUP$parser$do_action_part00000000(
                               CUP$parser$act_num,
                               CUP$parser$parser,
                               CUP$parser$stack,
                               CUP$parser$top);
    }
}

}