grammar InDy;
@header {
package io.github.minerofmillions.id_parser;
}

LPAREN : '(' ;
RPAREN : ')' ;
DEFINES : ':=' ;
LAMBDA : '\\';
COLON : ':' ;
NEWLINE : '\n' ;
SEMI : ';' ;

ID: [a-zA-Z_][a-zA-Z_0-9]* ;
WS: [ \t\n\r\f]+ -> skip ;

program : line* EOF ;

line
    : expr SEMI
    | asgn SEMI
    ;

asgn : ID ID* ':=' expr ;

expr
    : ID
    | expr '(' expr ')'
    | '\\' ID ':' expr
    ;
