package eu.bibl.updaterimpl.rev170.analysers.nbt;

import java.util.HashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.Access;

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
	
	public NBTBaseAnalyser() {
		super("NBTBase");
		methodHooks = new MethodHook[] { new MethodHook("getID", "()I", "()I"), new MethodHook("write", "(Ljava/io/DataOutput;)V", "(Ljava/io/DataOutput;)V"), new MethodHook("read", "") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "INT[]");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/INBTBase"));
		for(MethodNode m : methods(cn, "(B)L" + cn.name + ";")) {
			if (Access.isAbstract(m.access))
				continue;
			HashMap<Integer, TypeInsnNode> tins = getTypeInsns(m.instructions.getFirst());
			for(Integer num : tins.keySet()) {
				map.addClass(new ClassHook(tins.get(num).desc, NBT_SUBS.get(num)));
			}
		}
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("hashCode")) {
				MethodInsnNode min = (MethodInsnNode) m.instructions.getFirst().getNext();
				addHook(methodHooks[0].buildObfMin(min));
			} else
				if (m.desc.equals("(Ljava/io/DataOutput;)V")) {
					addHook(methodHooks[1].buildObfMn(m));
				} else {
					if (m.desc.startsWith("(Ljava/io/DataInput;I")) {
						addHook(methodHooks[2].buildObfMn(m).buildRefacDesc(m.desc));
					}
				}
		}
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