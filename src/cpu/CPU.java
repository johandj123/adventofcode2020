package cpu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class CPU {
    private final List<Instruction> program;

    private int pc = 0;

    private int accumulator = 0;

    public CPU(List<Instruction> program) {
        this.program = program;
    }

    public static List<Instruction> loadProgram(String programFile) throws IOException {
        return Files.readAllLines(new File(programFile).toPath())
                .stream()
                .map(Instruction::new)
                .collect(Collectors.toList());
    }

    public boolean isTerminated()
    {
        return pc == program.size();
    }

    public void executeStep() {
        program.get(pc).execute(this);
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void incrementPc() {
        this.pc++;
    }

    public int getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(int accumulator) {
        this.accumulator = accumulator;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "pc=" + pc +
                ", accumulator=" + accumulator +
                '}';
    }
}
