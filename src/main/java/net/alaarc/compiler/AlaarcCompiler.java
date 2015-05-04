package net.alaarc.compiler;

import net.alaarc.AlaarcOptions;
import net.alaarc.ast.nodes.AstProgram;
import net.alaarc.grammar.AlaarcLexer;
import net.alaarc.grammar.AlaarcParser;
import net.alaarc.vm.VmProgram;
import org.antlr.v4.runtime.*;

import java.io.*;

/**
 * Top-level wrapper for compilation workflow.
 *
 * @author dnpetrov
 */
public class AlaarcCompiler {
    private final AlaarcOptions alaarcOptions;
    private ANTLRInputStream antlrInput = null;

    private VmProgram vmProgram;

    public AlaarcCompiler(AlaarcOptions alaarcOptions) {
        this.alaarcOptions = alaarcOptions;
    }

    public AlaarcCompiler(String sourceFileName, ANTLRInputStream antlrInput) {
        this.alaarcOptions = new AlaarcOptions();
        this.alaarcOptions.setSourceFileName(sourceFileName);
        this.antlrInput = antlrInput;
    }

    public void run() {
        vmProgram = null; // Minor. Single-threaded

        if (!alaarcOptions.getSourceFileName().isPresent()) { // Looks more like an error: file does not exist
            System.out.println("Nothing to compile.");
            return;
        }

        String sourceFileName = alaarcOptions.getSourceFileName().get();

        // Testing hack: ANTLR input could be provided in constructor.
        if (antlrInput == null) {
            try {
                antlrInput = new ANTLRInputStream(new InputStreamReader(new FileInputStream(sourceFileName))); // What about closing this file?
            } catch (IOException e) {
                // Exception handling is ad-hoc
                System.out.println("Couldn't open file: " + sourceFileName); // Also an error
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
        } catch (Exception e) {
            System.out.println("Parse failed."); // Also an error
            return;
        }

        AstBuilder astBuilder = new AstBuilder(sourceFileName);
        AstProgram astProgram = astBuilder.buildProgram(parsed);

        ProgramCodeGenerator codeGen = new ProgramCodeGenerator();
        codeGen.run(astProgram);
        vmProgram = codeGen.getVmProgram();

        // Questionable design: maybe separating logic of program generation from particular files etc?
        if (alaarcOptions.getAsmFileName().isPresent()) {
            String asmFileName = alaarcOptions.getAsmFileName().get();
            try {
                OutputStream out = AlaarcOptions.resolveOutputStream(asmFileName);
                VmProgram.dump(out, vmProgram);
            } catch (FileNotFoundException e) {
                System.out.println("Could not emit generated code to " + asmFileName + ": " + e.getMessage());
            }
        }
    }

    public VmProgram getVmProgram() {
        return vmProgram;
    }

    private static class ParsingErrorListener extends BaseErrorListener {
        private final String sourceFileName;

        public ParsingErrorListener(String sourceFileName) {
            this.sourceFileName = sourceFileName;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            final String message = sourceFileName + ":" + line + ": " + msg;
            System.err.println(message);
            throw new IllegalStateException(message);
        }
    }
}
