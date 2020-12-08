package cpu;

public class Instruction {
    private Opcode opcode;

    private int argument;

    public Instruction(String instruction)
    {
        String[] parts = instruction.split(" ");
        opcode = Opcode.valueOf(parts[0]);
        argument = Integer.parseInt(parts[1]);
    }

    public Instruction(Instruction instruction)
    {
        this.opcode = instruction.opcode;
        this.argument = instruction.argument;
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public void setOpcode(Opcode opcode) {
        this.opcode = opcode;
    }

    public int getArgument() {
        return argument;
    }

    public void setArgument(int argument) {
        this.argument = argument;
    }

    public void execute(CPU cpu)
    {
        opcode.execute(this, cpu);
    }
}
