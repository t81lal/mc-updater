package eu.bibl.updaterimpl.rev170.analysers;

import java.lang.reflect.Modifier;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;

public class MinecraftAnalyser extends Analyser {
	
	public static final String INTERFACES = "eu/bibl/mc/accessors/";
	
	public MinecraftAnalyser(ClassContainer container, HookMap hookMap) {
		super("Minecraft", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getLocalPlayer"), new MappingData("L" + INTERFACES + "entity/IEntityClientPlayerMP;"), false),
				/* 1 */new FieldMappingData(new MappingData("getMemoryReserve"), new MappingData("[B", "[B"), false),
				/* 2 */new FieldMappingData(new MappingData("getWorldTypes"), new MappingData("[L" + INTERFACES + "world/IWorldType;"), false),
				/* 3 */new FieldMappingData(new MappingData("getDefaultWorldType"), new MappingData("L" + INTERFACES + "world/IWorldType;"), false),
				/* 4 */new FieldMappingData(new MappingData("getFlatWorldType"), new MappingData("L" + INTERFACES + "world/IWorldType;"), false),
				/* 5 */new FieldMappingData(new MappingData("getLargeBiomesWorldType"), new MappingData("L" + INTERFACES + "world/IWorldType;"), false),
				/* 6 */new FieldMappingData(new MappingData("getAmplifiedWorldType"), new MappingData("L" + INTERFACES + "world/IWorldType;"), false),
				/* 7 */new FieldMappingData(new MappingData("getDefault1_1WorldType"), new MappingData("L" + INTERFACES + "world/IWorldType;"), false),
				/* 8 */new FieldMappingData(new MappingData("getUseEntityActions"), new MappingData("[L" + INTERFACES + "entity/IUseEntityAction;"), false),
				/* 9 */new FieldMappingData(new MappingData("getChatVisibilityTypes"), new MappingData("[L" + INTERFACES + "chat/IChatVisibility;"), false),
				/* 10 */new FieldMappingData(new MappingData("getDifficultyModes"), new MappingData("[L" + INTERFACES + "world/IDifficulty;"), false),
				/* 11 */new FieldMappingData(new MappingData("getClientStates"), new MappingData("[L" + INTERFACES + "client/IClientState;"), false),
				/* 12 */new FieldMappingData(new MappingData("getServerStatusInfoGson"), new MappingData("Lcom/google/gson/Gson;", "Lcom/google/gson/Gson;"), false),
				/* 13 */new FieldMappingData(new MappingData("inFireDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 14 */new FieldMappingData(new MappingData("onFireDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 15 */new FieldMappingData(new MappingData("lavaDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 16 */new FieldMappingData(new MappingData("inWallDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 17 */new FieldMappingData(new MappingData("drowningDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 18 */new FieldMappingData(new MappingData("starvingDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 19 */new FieldMappingData(new MappingData("cactusDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 20 */new FieldMappingData(new MappingData("fallingDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 21 */new FieldMappingData(new MappingData("outOfWorldDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 22 */new FieldMappingData(new MappingData("genericDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 23 */new FieldMappingData(new MappingData("magicDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 24 */new FieldMappingData(new MappingData("witherDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 25 */new FieldMappingData(new MappingData("anvilDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 26 */new FieldMappingData(new MappingData("fallingBlockDamageSource"), new MappingData("L" + INTERFACES + "entity/combat/IDamageSource;"), false),
				/* 27 */new FieldMappingData(new MappingData("getMCProfiler"), new MappingData("L" + INTERFACES + "client/profiler/IProfiler;"), false) };
		methodHooks = new CallbackMappingData[] {
				/* 0 */new CallbackMappingData(new MappingData("runtick"), new MappingData("()V", "()V"), null, null, false),
				/* 1 */new CallbackMappingData(new MappingData("getItemID"), new MappingData("(L" + INTERFACES + "item/IItem;)I"), null, null, false),
				/* 2 */new CallbackMappingData(new MappingData("shutdown"), new MappingData("()V", "()V"), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		LdcInsnNode ldc = InsnUtil.getLdc(cn, "10485760");
		if (ldc != null) {
			FieldInsnNode baseNode = (FieldInsnNode) ldc.getNext().getNext();
			fieldHooks[1].getName().setObfuscatedName(baseNode.name);
			addField(fieldHooks[1]);
		}
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		findRuntickMethod();
		findLocalPlayer();
		return new InterfaceMappingData(INTERFACES + "IMinecraft");
	}
	
	private void findLocalPlayer() {
		ecpmpFinder: for (MethodNode mNode : cn.methods()) {
			if (!mNode.desc.endsWith(")V"))
				continue;
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
							fieldHooks[0].getName().setObfuscatedName(fin.name);
							fieldHooks[0].getDesc().setObfuscatedName(fin.desc);
							addField(fieldHooks[0]);
							String pmpcName = fin.desc.substring(1, fin.desc.length()).replace(";", "");
							hookMap.addClass(new ClassMappingData(pmpcName, "EntityClientPlayerMP", null));
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
		runtickLoop: for (MethodNode mNode : cn.methods()) {
			if (!mNode.desc.endsWith(")V"))
				continue;
			if (Modifier.isPublic(mNode.access)) {
				boolean guiCheck = false;
				boolean pickCheck = false;
				boolean gameModeCheck = false;
				boolean texturesCheck = false;
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (guiCheck && pickCheck && gameModeCheck && texturesCheck) {
						methodHooks[0].getMethodName().setObfuscatedName(mNode.name);
						addMethod(methodHooks[0]);
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