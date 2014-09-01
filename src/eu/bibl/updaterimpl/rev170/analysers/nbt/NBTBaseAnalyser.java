package eu.bibl.updaterimpl.rev170.analysers.nbt;
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
		methodHooks = new CallbackMappingData[] { new CallbackMappingData("getID", "()I", "()I"), new CallbackMappingData("write", "(Ljava/io/DataOutput;)V", "(Ljava/io/DataOutput;)V"), new CallbackMappingData("read", "") };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "INT[]");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
		for(MethodNode m : methods(cn, "(B)L" + cn.name + ";")) {
			if (Access.isAbstract(m.access))
				continue;
			HashMap<Integer, TypeInsnNode> tins = getTypeInsns(m.instructions.getFirst());
			for(Integer num : tins.keySet()) {
				hookMap.addClass(new ClassMappingData(tins.get(num).desc, NBT_SUBS.get(num)));
			}
		}
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("hashCode")) {
				MethodInsnNode min = (MethodInsnNode) m.instructions.getFirst().getNext();
				addMethodHook(methodHooks[0].buildObfMin(min));
			} else
				if (m.desc.equals("(Ljava/io/DataOutput;)V")) {
					addMethodHook(methodHooks[1].buildObfMn(m));
				} else {
					if (m.desc.startsWith("(Ljava/io/DataInput;I")) {
						addMethodHook(methodHooks[2].buildObfMn(m).buildRefacDesc(m.desc));
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
