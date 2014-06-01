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
            if (Category.forName($CATEGORY.getText()) == null)
                notifyErrorListeners("nonexistent category " + $CATEGORY.getText());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }                                       # CategoryTransformation
    | FUNC ( '(' value (',' value)* ')' )?  # FunctionTransformation
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