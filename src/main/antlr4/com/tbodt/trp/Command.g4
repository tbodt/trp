/** 
 * A grammer that represents a command for The Rapid Permuter.
 */
grammar Command;

/**
 * A rule describing a whole command.
 */
command: data EOF;

/**
 * A rule describing data that will be transformed.
 */
data: STRING                    # StringData
    | CATEGORY                  # CategoryData
    | data ':' transformation+  # TransformedData
    ;

/**
 * A rule describing a transformation, either a category or a function.
 */
transformation
    : FUNC ( '(' value (',' value)* ')' )?  # FunctionTransformation
    ;

/**
 * A rule describing a value, i.e. a function argument.
 */
value
    : INT               # IntValue
    | STRING            # StringValue
    | transformation    # TransformationValue
    | data              # DataValue
    ;

STRING : '"' ~["]* '"' ;
CATEGORY : '[' ~[\]]* ']' ;
INT : '-'? [0-9]+ ;
FUNC : [a-zA-Z]+ ;
WS : [ \t\r\n\f]+ -> skip;