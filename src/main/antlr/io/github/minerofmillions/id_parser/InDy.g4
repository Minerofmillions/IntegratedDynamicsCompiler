grammar InDy;
@header {
package io.github.minerofmillions.id_parser;
}

LPAREN : '(' ;
RPAREN : ')' ;
DEFINES : ':=' ;
LAMBDA : '\\' ;
COLON : ':' ;
NEWLINE : '\n' ;
SEMI : ';' ;
LBRACKET : '[' ;
RBRACKET : ']' ;

WS: [ \t\n\r\f]+ -> skip ;
ID: ([a-zA-Z0-9_-]|LBRACKET|RBRACKET)+ ;

program : line* EOF ;

line
    : expr SEMI
    | asgn SEMI
    ;

asgn : ID+ ':=' expr ;

expr
    : ID
    | expr '(' expr ')'
    | '\\' ID ':' expr
    ;
