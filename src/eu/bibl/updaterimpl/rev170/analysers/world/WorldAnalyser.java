package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.profiler.ProfilerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityAnalyser;

public class WorldAnalyser extends Analyser {
	
	private static final int[] REMOVE_ENTITY_REGEX = new int[] {
		ALOAD,
		INVOKEVIRTUAL,
		ALOAD,
		INSTANCEOF,
		IFEQ };
	
	private static final int[] WEATHER_REGEX = new int[] {
		ALOAD,
		GETFIELD,
		ALOAD,
		INVOKEINTERFACE,
		POP,
		ICONST_1 };
	
	private static final int[] SPAWN_ENTITY_REGEX = new int[] {
		ALOAD,
		GETFIELD,
		LDC,
		DDIV,
		INVOKESTATIC,
		ISTORE,
		ALOAD,
		GETFIELD,
		LDC,
		DDIV,
		INVOKESTATIC,
		ISTORE,
		ALOAD,
		GETFIELD,
		ISTORE };
	
	public WorldAnalyser(ClassContainer container, HookMap hookMap) {
		super("World", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getLoadedEntities"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false),
				/* 1 */new FieldMappingData(new MappingData("getUnloadedEntites"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false),
				/* 2 */new FieldMappingData(new MappingData("getPlayerEntities"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false),
				/* 3 */new FieldMappingData(new MappingData("getWeatherEntities"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false), };
		methodHooks = new CallbackMappingData[] { new CallbackMappingData(new MappingData("addWeatherEffect"), new MappingData("(L" + MinecraftAnalyser.INTERFACES + "entity/IEntity;)Z"), null, null, false),
		
		};
	}
	
	@Override
	public boolean accept() {
		return hookMap.getClassByRefactoredName("World").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		all: for (MethodNode m : cn.methods()) {
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst.equals("remove")) {
						FieldInsnNode fin = (FieldInsnNode) InsnUtil.getNext(ain, GETFIELD);
						if (fin.desc.equals("Ljava/util/List;")) {
							addField(fieldHooks[0].buildObf(fin));
							FieldInsnNode fin2 = (FieldInsnNode) InsnUtil.getNext(fin.getNext(), GETFIELD);
							addField(fieldHooks[1].buildObf(fin2));
							MethodInsnNode profiler = (MethodInsnNode) InsnUtil.getNext(ain, INVOKEVIRTUAL);
							hookMap.addClass(new ClassMappingData(profiler.owner, "Profiler", null));
							ProfilerAnalyser profilerAnalyser = (ProfilerAnalyser) AnalyserCache.contextGet("Profiler");
							profilerAnalyser.findEndStart(profiler);
							break all;
						}
					}
				}
			}
		}
		
		for (MethodNode m : cn.methods()) {
		InstructionSearcher is = new InstructionSearcher(m.instructions, REMOVE_ENTITY_REGEX);
		if (is.search()) {
			AbstractInsnNode ain = is.getMatches().get(0)[4];
			AbstractInsnNode ain2 = ain.getNext().getNext();
			if (ain2.getOpcode() == GETFIELD) {
				FieldInsnNode fin = (FieldInsnNode) ain2;
				if (fin.desc.equals("Ljava/util/List;")) {
					addField(fieldHooks[2].buildObf(fin));
					break;
				}
			}
		}
	}
		
		for (MethodNode m : cn.methods()) {
		InstructionSearcher is = new InstructionSearcher(m.instructions, WEATHER_REGEX);
		if (is.search()) {
			FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[1];
			addField(fieldHooks[3].buildObf(fin));
			addMethod(methodHooks[0].buildObfMethod(m));
			break;
		}
	}
		
		for (MethodNode m : cn.methods()) {
		if (Type.getArgumentTypes(m.desc).length != 1)
			continue;
		InstructionSearcher is = new InstructionSearcher(m.instructions, SPAWN_ENTITY_REGEX);
		if (is.search()) {
			FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[13];
			EntityAnalyser analyser = (EntityAnalyser) AnalyserCache.contextGet("Entity");
			analyser.getFieldHooks()[37].buildObf(fin);
			break;
		}
	}
	return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorld");
	}
}
