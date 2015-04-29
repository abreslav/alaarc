package net.alaarc.grammar.tests;

import net.alaarc.ast.AstBuilder;
import net.alaarc.ast.AstDumper;
import net.alaarc.ast.AstNode;
import net.alaarc.ast.nodes.AstProgram;
import net.alaarc.grammar.AlaarcLexer;
import net.alaarc.grammar.AlaarcParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class GrammarSmokeTests {

    @Test
    public void testGrammar0() throws Exception {
        parseProgram("/test1.alaarc");
    }

    @Test
    public void testGrammarError() throws Exception {
        try {
            parseProgram("/test-error.alaarc");
        } catch (Exception e) {
            // That's ok, we were expecting it
        }
    }

    private void parseProgram(final String path) throws IOException {
        AlaarcLexer lexer = new AlaarcLexer(getResourceAsInput(path));
        AlaarcParser parser = new AlaarcParser(new CommonTokenStream(lexer));
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                final String message = path + ":" + line + ": " + msg;
                System.err.println(message);
                throw new IllegalStateException(message, e);
            }
        });
        AlaarcParser.InitContext parsed = parser.init();
        System.out.println("Parse tree: " + parsed.toStringTree(parser));
        System.out.println();

        AstBuilder astBuilder = new AstBuilder(path);
        AstProgram program = astBuilder.buildProgram(parsed);
        System.out.println("AstProgram:");
        dumpAstNode(program, 2);
    }

    private void dumpAstNode(AstNode node, int indent) {
        AstDumper.dump(System.out, node, indent);
    }

    private ANTLRInputStream getResourceAsInput(String path) throws IOException {
        return new ANTLRInputStream(getClass().getResourceAsStream(path));
    }
}
