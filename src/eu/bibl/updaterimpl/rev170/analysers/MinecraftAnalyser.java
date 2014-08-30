package eu.bibl.updaterimpl.rev170.analysers;

import eu.bibl.banalysis.analyse.single.SingleAnalyser;
import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.filter.MethodFilter;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.ListIterator;

public class MinecraftAnalyser extends SingleAnalyser {

    private FieldMappingData[] hooks;
    private MappingData methodHooks;

	public MinecraftAnalyser() {
		super("Minecraft");
		hooks = new FieldMappingData[] {
				new FieldMappingData("getLocalPlayer", "L" + INTERFACES + "entity/IEntityClientPlayerMP;"),
				new FieldMappingData("getMemoryReserve", "[B", "[B"),
				new FieldMappingData("getWorldTypes", "[L" + INTERFACES + "world/IWorldType;"),
				new FieldMappingData("getDefaultWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldMappingData("getFlatWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldMappingData("getLargeBiomesWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldMappingData("getAmplifiedWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldMappingData("getDefault1_1WorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldMappingData("getUseEntityActions", "[L" + INTERFACES + "entity/IUseEntityAction;"),
				new FieldMappingData("getChatVisibilityTypes", "[L" + INTERFACES + "chat/IChatVisibility;"),
				new FieldMappingData("getDifficultyModes", "[L" + INTERFACES + "world/IDifficulty;"),
				new FieldMappingData("getClientStates", "[L" + INTERFACES + "client/IClientState;"),
				new FieldMappingData("getServerStatusInfoGson", "Lcom/google/gson/Gson;", "Lcom/google/gson/Gson;"),
				new FieldMappingData("inFireDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("onFireDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("lavaDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("inWallDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("drowningDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("starvingDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("cactusDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("fallingDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("outOfWorldDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("genericDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("magicDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("witherDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("anvilDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("fallingBlockDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldMappingData("getMCProfiler", "L" + INTERFACES + "client/profiler/IProfiler;") };
		methodHooks = new MethodHook[] {
				new MethodHook("runtick", "()V", "()V"),
				new MethodHook("getItemID", "(L" + INTERFACES + "item/IItem;)I"),
				new MethodHook("shutdown", "()V", "()V") };
	}

    @Override
    public boolean accept(ClassContainer classContainer, ClassNode classNode, HookMap hookMap) {
        List<MethodNode> staticInits = cn.methods(new MethodFilter() {
            @Override
            public boolean accept(MethodNode methodNode) {
                return methodNode.name.equals("<clinit>");
            }
        });
        if (staticInits.size() == 0)
            return false;
        MethodNode m = staticInits.get(0);
        ListIterator<?> it = m.instructions.iterator();
        while (it.hasNext()) {
            AbstractInsnNode ain = (AbstractInsnNode) it.next();
            if (ain instanceof LdcInsnNode) {
                LdcInsnNode lin = (LdcInsnNode) ain;
                Object o = lin.cst;
                if (o != null) {
                    if (o.toString().equals("10485760")) {
                        ClassMappingData hook = new ClassMappingData(cn.name, "Minecraft");
                        FieldInsnNode baseNode = (FieldInsnNode)ain.getNext().getNext();
                        hooks[1].set
                        addHook(hooks[1].buildObfFin(((FieldInsnNode) ain.getNext().getNext())));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void analyse(ClassContainer classContainer) {

    }

    @Override
	public boolean accept(ClassNode cn) {

	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "IMinecraft"));
		
		findRuntickMethod();
		findLocalPlayer();
	}
	
	private void findLocalPlayer() {
		ecpmpFinder: for(MethodNode mNode : cn.methods(new MethodFilter() {
            @Override
            public boolean accept(MethodNode methodNode) {
                return methodNode.desc.endsWith(")V");
            }
        })) {
			boolean found3553Int = false;
			boolean foundDisplayLdc = false;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (!foundDisplayLdc) {
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("display")) {
								foundDisplayLdc = true;
							}
						}
					}
				} else {
					if (found3553Int) {
						if (ain instanceof FieldInsnNode) {
							FieldInsnNode fin = (FieldInsnNode) ain;
							String pmpcName = fin.desc.substring(1, fin.desc.length()).replace(";", "");
							map.addClass(new ClassMappingData(pmpcName, "EntityClientPlayerMP", null));
							addHook(hooks[0].buildObfFin(fin)); // localPlayer
							break ecpmpFinder;
						}
					} else {
						if (ain instanceof IntInsnNode) {
							IntInsnNode iin = (IntInsnNode) ain;
							if (iin.operand == 3553) {
								found3553Int = true;
							}
						}
					}
				}
			}
		}
	}
	
	private void findRuntickMethod() {
		runtickLoop: for(MethodNode mNode : methods(cn)) {
			if (!mNode.desc.endsWith(")V"))
				continue;
			if (Access.isPublic(mNode.access)) {
				boolean guiCheck = false;
				boolean pickCheck = false;
				boolean gameModeCheck = false;
				boolean texturesCheck = false;
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (guiCheck && pickCheck && gameModeCheck && texturesCheck) {
						addHook(methodHooks[0].buildObfMn(mNode)); // runtick
						break runtickLoop;
					}
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst instanceof String) {
								if (ldc.cst.toString().equals("gui")) {
									guiCheck = true;
								} else if (ldc.cst.toString().equals("pick")) {
									pickCheck = true;
								} else if (ldc.cst.toString().equals("gameMode")) {
									gameModeCheck = true;
								} else if (ldc.cst.toString().equals("textures")) {
									texturesCheck = true;
								}
							}
						}
					}
				}
			}
		}
	}
}