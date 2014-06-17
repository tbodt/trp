grammar Puzzle;

puzzle: data+ ':' transformation* EOF;
data: STRING    # StringData
    | CATEGORY  # CategoryData
    ;
transformation
    : CATEGORY                              # CategoryTransformation
    | FUNC ( '(' value (',' value)* ')' )?  # FunctionTransformation
    ;
value
    : INT       # IntValue
    | STRING    # StringValue
    ;

STRING : '"' ~["]* '"' ;
CATEGORY : '[' [a-zA-Z/]* ']' ;
INT : '-'? [0-9]+ ;
FUNC : [a-zA-Z]+ ;
WS : [ \t\r\n\f]+ -> skip;