package jflex;

import java_cup.runtime.*;
import cup.*;

%%
%cup
%line
%column
%unicode
%public
%class Scanner

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

%}


/* Pattern */
leftparen       = \(
rightparen      = \)
whitespace      = [ \t]
comma           = ,
apo             = \"
source          = SRC
destination     = DST
protocol        = PROTO
port            = PRT
flags           = FLAGS
found           = FOUND
listed          = LISTED
as              = AS
sum             = SUM
count           = COUNT
min             = MIN
max             = MAX
dcard           = DCARD
scard           = SCARD
pcard           = PCARD
matches         = MATCHES
greater         = >
less            = <
equal           = =
and             = AND|and
or              = OR|or
plus            = \+
minus           = -
multiply        = \*
division        = \/
network         = {IPv4network}|{IPv4address}
IPv4network     = {IPv4address}\/{mask}
IPv4address     = {d8}\.{d8}\.{d8}\.{d8}
d8              = {digit}|{digit_1_9}{digit}|1{digit}{digit}|2{digit_0_4}{digit}|25{digit_0_5}
mask            = {digit}|1{digit}|2{digit}|3{digit_0_2}
flags_string    = [U.][A.][P.][R.][S.][F.]
sscope          = SSCOPE
dscope          = DSCOPE
integer         = {digit}+
digit           = [0-9]
digit_1_9       = [1-9]
digit_0_2       = [0-2]
digit_0_4       = [0-4]
digit_0_5       = [0-5]
identifier      = {letter}({alphanumeric}|{other_char})*
letter          = [A-Za-z]
alphanumeric    = {letter}|{digit}
other_char      = [_\-]
real            = {integer}+\.{integer}+


%%
/* Lexicals */
<YYINITIAL> {
{comma}         { return symbol(sym.COMMA); }
{apo}           { return symbol(sym.APO); }
{leftparen}     { return symbol(sym.LEFTPAREN); }
{rightparen}    { return symbol(sym.RIGHTPAREN); }
{source}        { return symbol(sym.SRC); }
{destination}   { return symbol(sym.DST); }
{protocol}      { return symbol(sym.PROTO); }
{port}          { return symbol(sym.PRT); }
{flags}         { return symbol(sym.FLAGS); }
{sum}           { return symbol(sym.SUM); }
{count}         { return symbol(sym.COUNT); }
{min}           { return symbol(sym.MIN); }
{max}           { return symbol(sym.MAX); }
{dcard}         { return symbol(sym.DCARD); }
{scard}         { return symbol(sym.SCARD); }
{pcard}         { return symbol(sym.PCARD); }
{sscope}        { return symbol(sym.SSCOPE); }
{dscope}        { return symbol(sym.DSCOPE); }
{matches}       { return symbol(sym.MATCHES); }
{greater}       { return symbol(sym.GT); }
{less}          { return symbol(sym.LT); }
{equal}         { return symbol(sym.EQ); }
{and}           { return symbol(sym.AND); }
{or}            { return symbol(sym.OR); }
{plus}          { return symbol(sym.PLUS); }
{minus}         { return symbol(sym.MINUS); }
{multiply}      { return symbol(sym.MULTIPLY); }
{division}      { return symbol(sym.DIVISION); }
{found}         { return symbol(sym.FOUND); }
{listed}        { return symbol(sym.LISTED); }
{as}            { return symbol(sym.AS); }
{whitespace}    { /* do nothing */ }
{real}          { return symbol(sym.FLOAT_flex, new Float(yytext())); }
{integer}       { return symbol(sym.INT_flex, new Integer(yytext())); }
{identifier}    { if (yytext().matches("^now(-[0-9]+[mdw])?")) return symbol(sym.TIME, yytext()); else return symbol(sym.IDENTIFIER, yytext()); }
{network}       { return symbol(sym.NETWORK, yytext()); }
{flags_string}  { return symbol(sym.FLAGS_string, yytext()); }
}

[^]             { throw new Error("Illegal character >"+yytext()+"< at position "+yycolumn); }
