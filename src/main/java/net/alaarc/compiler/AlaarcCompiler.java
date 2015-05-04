package net.alaarc.compiler;

import net.alaarc.AlaarcOptions;
import net.alaarc.ast.nodes.AstProgram;
import net.alaarc.grammar.AlaarcLexer;
import net.alaarc.grammar.AlaarcParser;
import net.alaarc.vm.VmProgram;
import org.antlr.v4.runtime.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Top-level wrapper for compilation workflow.
 *
 * <p>
 *     TODO refactor into AlaarcCompiler & AlaarcCompilationResult?
 * </p>
 *
 * @author dnpetrov
 */
public class AlaarcCompiler {
    private final AlaarcOptions alaarcOptions;
    private final ANTLRInputStream antlrInput;
    private final String sourceFileName;

    private AstProgram astProgram;
    private VmProgram vmProgram;

    private int numErrors;
    
    public AlaarcCompiler(AlaarcOptions alaarcOptions) {
        this.alaarcOptions = alaarcOptions;
        this.sourceFileName = alaarcOptions.getSourceFileName().get();
        this.antlrInput = createAntlrInputStream(sourceFileName);
    }

    private ANTLRInputStream createAntlrInputStream(String sourceFileName) {
        try {
            return new ANTLRFileStream(sourceFileName);
        } catch (IOException e) {
            System.out.println("Couldn't open file: " + sourceFileName + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    AlaarcCompiler(AlaarcOptions alaarcOptions, InputStream input) throws IOException {
        this.alaarcOptions = alaarcOptions;
        this.sourceFileName = alaarcOptions.getSourceFileName().get();
        this.antlrInput = new ANTLRInputStream(input);
    }

    public void run() {
        astProgram = null;
        vmProgram = null;
        numErrors = 0;

        if (!alaarcOptions.getSourceFileName().isPresent()) { // Looks more like an error: file does not exist
            System.out.println("Nothing to compile.");
            return;
        }

        AlaarcLexer lexer = new AlaarcLexer(antlrInput);

        AlaarcParser parser = new AlaarcParser(new CommonTokenStream(lexer));
        parser.addErrorListener(new ParsingErrorListener());
        AlaarcParser.InitContext parsed;
        try {
            parsed = parser.init();
            AstBuilder astBuilder = new AstBuilder(sourceFileName);
            astProgram = astBuilder.buildProgram(parsed);
        } catch (Exception e) {
            // system.err?
            // ad hoc exception handling
            System.out.println("Parse failed: " + e.getMessage());
            return;
        }

        ProgramCodeGenerator codeGen = new ProgramCodeGenerator();
        codeGen.run(astProgram);
        vmProgram = codeGen.getVmProgram();

        // Questionable design: maybe separating logic of program generation from particular files etc?
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
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            // TODO error reporting (should replace ANTLR default)
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
