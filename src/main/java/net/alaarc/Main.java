package net.alaarc;

import net.alaarc.compiler.AlaarcCompiler;
import net.alaarc.interpreter.AlaarcInterpreter;
import net.alaarc.vm.VmProgram;

/**
 * @author dnpetrov
 */
public class Main {

    public static void main(String[] args) {
        AlaarcOptions options = parseCommandLine(args);

        AlaarcCompiler compiler = new AlaarcCompiler(options);
        try {
            compiler.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        VmProgram vmProgram = compiler.getVmProgram();
        AlaarcInterpreter interpreter = new AlaarcInterpreter(options, vmProgram);
        try {
            interpreter.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (interpreter.hadAnyProblems()) {
            System.exit(1);
        }
    }

    /**
     * Quick and dirty command-line parser.
     *
     * @param args
     * @return parsed options
     */
    private static AlaarcOptions parseCommandLine(String[] args) {
        if (args.length == 0) {
            printHelpAndDie(null);
        }

        AlaarcOptions options = new AlaarcOptions();

        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-log")) {
                if (++i >= args.length) {
                    printHelpAndDie("Option -log requires <FILE> argument");
                }
                options.setLogFileName(args[i]);
            } else if (arg.equals("-asm")) {
                if (++i >= args.length) {
                    printHelpAndDie("Option -asm requires <FILE> argument");
                }
                options.setAsmFileName(args[i]);
            } else if (arg.equals("-times")) {
                if (++i >= args.length) {
                    printHelpAndDie("Option -times requires <N> argument");
                }
                try {
                    int times = Integer.valueOf(args[i]);
                    options.setTimes(times);
                } catch (Exception e) {
                    printHelpAndDie(e.getMessage());
                }
            } else if (arg.equals("-help")) {
                printHelpAndDie(null);
            } else if (arg.startsWith("-")){
                printHelpAndDie("Unrecognized option: " + arg);
            } else {
                options.setSourceFileName(arg);
            }
        }
        return options;
    }

    private static void printHelpAndDie(String message) {
        if (message != null) {
            System.out.println(message);
        }
        System.out.println("Options: <SOURCE_FILE> ...");
        System.out.println("  <SOURCE_FILE>         source file name");
        System.out.println("  -asm <FILE>           emit generated code to FILE");
        System.out.println("  -log <FILE>           write logs to FILE");
        System.out.println("  -times <N>            run N times");
        System.out.println("  -help                 prints help");
        System.exit(1);
    }

}
