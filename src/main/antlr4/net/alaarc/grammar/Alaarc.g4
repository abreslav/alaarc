grammar Alaarc;

init: threadBody ;

// Statements

stmt
    : threadStmt
    | sleepStmt
    | sleepRandStmt
    | dumpStmt
    | assertRcStmt
    | logStmt
    | assignStmt
    ;

threadStmt: THREAD LEFT_CURLY threadBody RIGHT_CURLY;
threadBody: stmt*;

sleepStmt: SLEEP;

sleepRandStmt: SLEEP_RAND;

dumpStmt: DUMP lvExpr;

assertRcStmt: ASSERT_RC lvExpr comparisonOp INT;

logStmt: LOG STRING;

assignStmt: lvExpr assignOp rvExpr;

// Expressions

lvExpr: ID fieldDeref*;
fieldDeref: DOT ID;

rvExpr
    : lvExpr
    | newObjectExpr
    | nullExpr
    ;

newObjectExpr: OBJECT;
nullExpr: NULL;

// Operators

assignOp
    : ASSIGN
    | ASSIGN_WEAK
    ;

comparisonOp
    : EQ
    | NEQ
    | LE
    | LT
    | GE
    | GT
    ;

// Tokens

INT: '-'? [0-9]+;
STRING: '"' ~["\r\n]* '"';

THREAD: 'thread';
SLEEP: 'sleep';
SLEEP_RAND: 'sleepr';
DUMP: 'dump';
ASSERT_RC: 'assertrc';
LOG: 'log';

OBJECT: 'object';
NULL: 'null';

ID: ID_START_CHAR ID_NEXT_CHAR*;
fragment ID_START_CHAR: [a-zA-Z_];
fragment ID_NEXT_CHAR: [a-zA-Z_0-9];

LEFT_CURLY: '{';
RIGHT_CURLY: '}';

DOT: '.';
ASSIGN: '=';
ASSIGN_WEAK: '~=';
EQ: '==';
NEQ: '!=';
LE: '<=';
LT: '<';
GE: '>=';
GT: '>';

WS: [ \r\n\t] -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;

