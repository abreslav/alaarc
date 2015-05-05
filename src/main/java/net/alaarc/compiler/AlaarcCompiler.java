package net.alaarc.compiler;

import net.alaarc.AlaarcOptions;
import net.alaarc.ast.nodes.AstProgram;
import net.alaarc.grammar.AlaarcLexer;
import net.alaarc.grammar.AlaarcParser;
import net.alaarc.vm.VmProgram;
import org.antlr.v4.runtime.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Top-level wrapper for compilation workflow.
 *
 * @author dnpetrov
 */
public class AlaarcCompiler {
    private final AlaarcOptions alaarcOptions;
    private ANTLRInputStream antlrInput = null;

    private AstProgram astProgram;
    private VmProgram vmProgram;

    private int numErrors;
    
    public AlaarcCompiler(AlaarcOptions alaarcOptions) {
        this.alaarcOptions = alaarcOptions;
    }

    AlaarcCompiler(String sourceFileName, ANTLRInputStream antlrInput) {
        this.alaarcOptions = new AlaarcOptions();
        this.alaarcOptions.setSourceFileName(sourceFileName);
        this.antlrInput = antlrInput;
    }

    public void run() {
        astProgram = null;
        vmProgram = null;
        numErrors = 0;

        if (!alaarcOptions.getSourceFileName().isPresent()) {
            System.out.println("Nothing to compile.");
            return;
        }

        String sourceFileName = alaarcOptions.getSourceFileName().get();

        // Testing hack: ANTLR input could be provided in constructor.
        if (antlrInput == null) {
            try {
                antlrInput = new ANTLRFileStream(sourceFileName);
            } catch (IOException e) {
                System.out.println("Couldn't open file: " + sourceFileName);
                e.printStackTrace();
                return;
            }
        }

        AlaarcLexer lexer = new AlaarcLexer(antlrInput);

        AlaarcParser parser = new AlaarcParser(new CommonTokenStream(lexer));
        parser.addErrorListener(new ParsingErrorListener(sourceFileName));
        AlaarcParser.InitContext parsed;
        try {
            parsed = parser.init();
            AstBuilder astBuilder = new AstBuilder(sourceFileName);
            astProgram = astBuilder.buildProgram(parsed);
        } catch (Exception e) {
            System.out.println("Parse failed: " + e.getMessage());
            return;
        }

        ProgramCodeGenerator codeGen = new ProgramCodeGenerator();
        codeGen.run(astProgram);
        vmProgram = codeGen.getVmProgram();

        if (alaarcOptions.getAsmFileName().isPresent() && vmProgram != null) {
            String asmFileName = alaarcOptions.getAsmFileName().get();
            try {
                OutputStream out = AlaarcOptions.resolveOutputStream(asmFileName);
                VmProgram.dump(out, vmProgram);
            } catch (FileNotFoundException e) {
                System.out.println("Could not emit generated code to " + asmFileName + ": " + e.getMessage());
            }
        }
    }

    public AstProgram getAstProgram() {
        return astProgram;
    }

    public VmProgram getVmProgram() {
        return vmProgram;
    }

    private class ParsingErrorListener extends BaseErrorListener {
        private final String sourceFileName;

        public ParsingErrorListener(String sourceFileName) {
            this.sourceFileName = sourceFileName;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            numErrors++;
        }
    }

    public int getNumErrors() {
        return numErrors;
    }

    public boolean hasErrors() {
        return numErrors > 0;
    }
}
