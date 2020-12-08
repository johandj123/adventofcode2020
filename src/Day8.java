import cpu.CPU;
import cpu.Instruction;
import cpu.Opcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<Instruction> program = CPU.loadProgram("input8.txt");
        firstPart(program);
        secondPart(program);
    }

    private static void firstPart(List<Instruction> program) {
        CPU cpu = new CPU(program);
        Set<Integer> seenInstructions = new HashSet<>();
        while (!seenInstructions.contains(cpu.getPc())) {
            seenInstructions.add(cpu.getPc());
            cpu.executeStep();
        }
        System.out.println("First part: " + cpu);
    }

    private static void secondPart(List<Instruction> program) {
        for (int i = 0; i < program.size(); i++) {
            List<Instruction> modifiedProgram = swapNopJmp(program, i);
            if (modifiedProgram != null) {
                tryRunProgram(modifiedProgram);
            }
        }
    }

    private static List<Instruction> swapNopJmp(List<Instruction> program, int index) {
        List<Instruction> result = null;
        if (Opcode.nop.equals(program.get(index).getOpcode())) {
            result = new ArrayList<>(program);
            Instruction instruction = new Instruction(result.get(index));
            result.set(index, instruction);
            instruction.setOpcode(Opcode.jmp);
        } else if (Opcode.jmp.equals(program.get(index).getOpcode())) {
            result = new ArrayList<>(program);
            Instruction instruction = new Instruction(result.get(index));
            result.set(index, instruction);
            instruction.setOpcode(Opcode.nop);
        }
        return result;
    }

    private static void tryRunProgram(List<Instruction> program) {
        CPU cpu = new CPU(program);
        Set<Integer> seenInstructions = new HashSet<>();
        while (!seenInstructions.contains(cpu.getPc()) && !cpu.isTerminated()) {
            seenInstructions.add(cpu.getPc());
            cpu.executeStep();
        }
        if (cpu.isTerminated()) {
            System.out.println("Second part: " + cpu);
        }
    }
}
