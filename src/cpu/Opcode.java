package cpu;

public enum Opcode {
    nop {
        @Override
        public void execute(Instruction instruction, CPU cpu) {
            cpu.incrementPc();
        }
    },
    acc {
        @Override
        public void execute(Instruction instruction, CPU cpu) {
            cpu.setAccumulator(cpu.getAccumulator() + instruction.getArgument());
            cpu.incrementPc();
        }
    },
    jmp {
        @Override
        public void execute(Instruction instruction, CPU cpu) {
            cpu.setPc(cpu.getPc() + instruction.getArgument());
        }
    };

    public abstract void execute(Instruction instruction, CPU cpu);
}
