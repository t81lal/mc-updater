package eu.bibl.updaterimpl.rev170.analysers.nbt;

import java.util.HashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTBaseAnalyser extends NBTAnalyser {
	
	private static final HashMap<Integer, String> NBT_SUBS = new HashMap<Integer, String>();
	
	static {
		NBT_SUBS.put(0, "NBTTagEnd");
		NBT_SUBS.put(1, "NBTTagByte");
		NBT_SUBS.put(2, "NBTTagShort");
		NBT_SUBS.put(3, "NBTTagInt");
		NBT_SUBS.put(4, "NBTTagLong");
		NBT_SUBS.put(5, "NBTTagFloat");
		NBT_SUBS.put(6, "NBTTagDouble");
		NBT_SUBS.put(7, "NBTTagByteArray");
		NBT_SUBS.put(8, "NBTTagIntArray");
		NBT_SUBS.put(9, "NBTTagString");
		NBT_SUBS.put(10, "NBTTagList");
		NBT_SUBS.put(11, "NBTTagCompound");
		
	}
	
	public NBTBaseAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTBase", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("getID"), new MappingData("()I", "()I"), null, null, false),
				new CallbackMappingData(new MappingData("write"), new MappingData("(Ljava/io/DataOutput;)V", "(Ljava/io/DataOutput;)V"), null, null, false),
				new CallbackMappingData(new MappingData("read"), new MappingData(""), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "INT[]");
	}
	
	@Override
	public InterfaceMappingData run() {
		for (MethodNode m : InsnUtil.methods(cn, "(B)L" + cn.name + ";")) {
			if (!((m.access & ACC_ABSTRACT) != 0))
				continue;
			HashMap<Integer, TypeInsnNode> tins = getTypeInsns(m.instructions.getFirst());
			for (Integer num : tins.keySet()) {
				hookMap.addClass(new ClassMappingData(tins.get(num).desc, NBT_SUBS.get(num), null));
			}
		}
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("hashCode")) {
				MethodInsnNode min = (MethodInsnNode) m.instructions.getFirst().getNext();
				addMethod(methodHooks[0].buildObfMethod(min));
			} else if (m.desc.equals("(Ljava/io/DataOutput;)V")) {
				addMethod(methodHooks[1].buildObfMethod(m));
			} else {
				if (m.desc.startsWith("(Ljava/io/DataInput;I")) {
					methodHooks[2].getMethodDesc().setRefactoredName(m.desc);
					addMethod(methodHooks[2].buildObfMethod(m));
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/INBTBase");
	}
	
	private HashMap<Integer, TypeInsnNode> getTypeInsns(AbstractInsnNode ain) {
		HashMap<Integer, TypeInsnNode> tins = new HashMap<Integer, TypeInsnNode>();
		while (ain != null) {
			if (ain.getOpcode() == NEW) {
				tins.put(tins.size(), (TypeInsnNode) ain);
			}
			ain = ain.getNext();
		}
		return tins;
	}
}
