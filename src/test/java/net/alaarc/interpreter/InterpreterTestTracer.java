package net.alaarc.interpreter;

import net.alaarc.NullAlaarcListener;
import net.alaarc.log.ILogMessageFormatter;
import net.alaarc.log.LogMessage;
import net.alaarc.log.Logger;
import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author dnpetrov
 */
public class InterpreterTestTracer extends NullAlaarcListener {
    private final StringWriter traceWriter;
    private final Logger traceLogger;

    private static final ILogMessageFormatter TEST_MESSAGE_FORMATTER = LogMessage::getMessage;

    public InterpreterTestTracer() {
        traceWriter = new StringWriter();
        traceLogger = new Logger(new PrintWriter(traceWriter, true), TEST_MESSAGE_FORMATTER);
    }

    private void log(String message) {
        try {
            traceLogger.log(LogMessage.create(message));
        } catch (InterruptedException e) {
            // swallow it
        }
    }


    @Override
    public void onVmException(VmInstruction instr, VmException e) {
        log(e.getMessage());
    }

    @Override
    public void onObjectDump(VmInstruction instr, String dump) {
        log(instr.toString() + ": " + dump);
    }

    @Override
    public void onPostMessage(VmInstruction instr, String message) {
        log(instr.toString() + ": " + message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr, long actualRc) {
        log("@" + instr.getDebugInfo() + ": assertion PASSED: "
                + actualRc + instr.getComparisonOperator() + instr.getNumber());
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
        log("@" + instr.getDebugInfo() + ": assertion FAILED: "
                + actualRc + instr.getComparisonOperator() + instr.getNumber());
    }


    public String getTrace() {
        return traceWriter.toString();
    }
}
