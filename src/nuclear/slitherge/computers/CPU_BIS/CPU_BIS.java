package nuclear.slitherge.computers.CPU_BIS;

import nuclear.slitherge.computers.CPU;
import nuclear.slitherge.computers.ComputerInterfaceHandler;

public class CPU_BIS extends CPU {

	short registers[];
	boolean FLAG_ZERO=false;
	boolean FLAG_CARRY=false;
	boolean FLAG_GREATER=false;
	boolean FLAG_LESS=false;
	/*
	 * REGISTERS
	 *  0 AX
	 *  1 BX
	 *  2 CX
	 *  3 DX
	 *  4 SI
	 *  5 DI
	 *  6 SP
	 *  7 PC
	 */
	public CPU_BIS(ComputerInterfaceHandler i, double x, double y, int ticks_per_update) {
		super(i, x, y, ticks_per_update);
		registers=new short[8];
		reset();
	}

	public CPU_BIS(ComputerInterfaceHandler i) {
		super(i);
		registers=new short[8];
		reset();
	}

	@Override
	public void reset() {
		for(byte i=0;i<8;i++)
			registers[i]=0;
	}

	@Override
	public void tick() {
		byte opcode=getProgramByte();
		if(opcode<(byte)0x80&&(opcode&(byte)15)<7)
			registers[opcode>>4]=registers[opcode&7];
		else if(opcode>>7==1){
			if(((opcode>>3)&1)==0)
				registers[0]=alu(registers[0],registers[opcode&7],(byte)((opcode>>4)&7));
			else {
				if(opcode>>4==8){
					registers[0]=alu(registers[0],getProgramByte(),(byte)(opcode&7));
				}else {
					registers[opcode&7]=opSingleArg(registers[opcode&7], (byte) ((opcode>>4)&7));
				}
			}
		}else if((opcode&0xF)==7){
			switch(opcode){
			case (byte)0x07:
				if(FLAG_ZERO)
					registers[7]=getProgramByte();
				break;
			case (byte)0x17:
				if(!FLAG_ZERO)
					registers[7]=getProgramByte();
				break;
			case (byte)0x27:
				if(FLAG_GREATER)
					registers[7]=getProgramByte();
				break;
			case (byte)0x37:
				if(FLAG_LESS||FLAG_ZERO)
					registers[7]=getProgramByte();
				break;
			case (byte)0x47:
				if(FLAG_LESS)
					registers[7]=getProgramByte();
				break;
			case (byte)0x57:
				if(FLAG_GREATER||FLAG_ZERO)
					registers[7]=getProgramByte();
				break;
			default:
				break;
			}
		}
	}
	
	private byte getProgramByte() {
		registers[7]++;
		return memRead(registers[7]-1);
	}

	private short alu(short a, short b, byte i) {
		int out;
		if(i==0){
			out=a+b;
		}else if(i==1){
			out=a-b;
		}else if(i==2){
			out=a*b;
		}else if(i==3){
			out=a/b;
		}else if(i==4){
			out=a|b;
		}else if(i==5){
			out=a&b;
		}else if(i==6){
			out=a^b;
		}else {
			out=-1;
		}
		check_flags(out);
		return (short)out;
	}
	private short opSingleArg(short a, byte op){
		int out=-1;
		if(op==1){
			out=a+1;
		}else if(op==2){
			out=a-1;
		}else if(op==3){
			out=~a;
		}else if(op==4){
			out=-a;
		}else if(op==5){
			push(a);
			out=a;
		}else if(op==6){
			out=pop();
		}
		check_flags(out);
		return (short)out;
		
	}

	private int pop() {
		registers[6]+=2;
		return readShort((short) (registers[6]-1));
	}

	private void push(short a) {
		registers[6]--;
		writeShort(a, registers[6]);
		registers[6]--;
	}
	private void writeShort(short data, short adr){
		memWrite((byte)(data&255), adr);
		memWrite((byte)(data>>8), adr+1);
	}

	private short readShort(short adr){
		short o=memRead(adr);
		return (short) (o+(memRead(adr+1)<<8));
	}
	
	private void check_flags(int a) {
		FLAG_ZERO=(short)a==0;
		FLAG_CARRY=a>(short)a;
	}

	@Override
	public String getName() {
		return "Nuclaer Tech BIS CPU";
	}

	@Override
	public void update() {
		
	}
}
