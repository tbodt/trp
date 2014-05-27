grammar Puzzle;

puzzle: data+ ':' transformation* EOF;
data: STRING    # StringData
    | CATEGORY  # CategoryData
    ;
transformation
    : CATEGORY                              # CategoryTransformation
    | FUNC ( '(' value (',' value)* ')' )?  # FunctionTransformation
    ;
value returns [Object val]
    : INT {$val = $INT.int;}
    | STRING {$val = $STRING.text;}
    ;

STRING : '"' ~["]* '"' {setText(getText().substring(1, getText().length() - 1));} ;
CATEGORY : '[' [a-zA-Z/]* ']' {setText(getText().substring(1, getText().length() - 1));} ;
FUNC : [a-zA-Z0-9]+ ;
INT : '-'? [0-9]+ ;
WS : [ \t\r\n\f]+ -> skip;