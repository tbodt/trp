grammar Puzzle;

@header {
    import java.io.IOException;
}

puzzle: data+ ':' transformation* EOF;
data: STRING    # StringData
    | CATEGORY  # CategoryData
    ;
transformation
    : CATEGORY {
        try {
            if (Category.forName($CATEGORY.text) == null)
                notifyErrorListeners("nonexistent category " + $CATEGORY.text);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }                                                                   # CategoryTransformation
    | FUNC ( '(' value (',' value)* ')' )?  {
        if (Function.forName($FUNC.text) == null)
            notifyErrorListeners("nonexistent function " + $FUNC.text);
    }                                                                   # FunctionTransformation
    ;
value returns [Object val]
    : INT {$val = $INT.int;}
    | STRING {$val = $STRING.text;}
    ;

STRING : '"' ~["]* '"' {setText(getText().substring(1, getText().length() - 1));} ;
CATEGORY : '[' [a-zA-Z/]* ']' {setText(getText().substring(1, getText().length() - 1));} ;
INT : '-'? [0-9]+ ;
FUNC : [a-zA-Z]+ ;
WS : [ \t\r\n\f]+ -> skip;