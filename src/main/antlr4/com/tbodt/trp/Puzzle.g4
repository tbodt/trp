/** 
 * A grammer that represents a puzzle for The Rapid Permuter to solve.
 */
grammar Puzzle;

/**
 * A rule describing the whole puzzle.
 */
puzzle: data+ ':' transformation* EOF;
/**
 * A rule describing data that will be transformed.
 */
data: STRING    # StringData
    | CATEGORY  # CategoryData
    ;
/**
 * A rule describing a transformation, either a category or a function.
 */
transformation
    : CATEGORY                              # CategoryTransformation
    | FUNC ( '(' value (',' value)* ')' )?  # FunctionTransformation
    ;
/**
 * A rule describing a value, i.e. a function argument.
 */
value
    : INT       # IntValue
    | STRING    # StringValue
    ;

STRING : '"' ~["]* '"' ;
CATEGORY : '[' [a-zA-Z/]* ']' ;
INT : '-'? [0-9]+ ;
FUNC : [a-zA-Z]+ ;
WS : [ \t\r\n\f]+ -> skip;