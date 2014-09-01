package eu.bibl.updaterimpl.rev170.analysers.client.profiler;

import java.util.Arrays;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.InstructionVector;
import eu.bibl.banalysis.filter.InstructionFilter;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ProfilerAnalyser extends Analyser {
	
	private MethodInsnNode startEndMin;
	
	public ProfilerAnalyser(ClassContainer container, HookMap hookMap) {
		super("Profiler", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				/* 0 */new CallbackMappingData(new MappingData("endStartSection"), new MappingData("(Ljava/lang/String;)V", "(Ljava/lang/String;)V"), null, null, false),
				/* 1 */new CallbackMappingData(new MappingData("endSection"), new MappingData("()V", "()V"), null, null, false),
				/* 2 */new CallbackMappingData(new MappingData("startSection"), new MappingData("(Ljava/lang/String;)V", "(Ljava/lang/String;)V"), null, null, false),
				/* 3 */new CallbackMappingData(new MappingData("clear"), new MappingData("()V", "()V"), null, null, false) };
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("isProfilingEnabled"), new MappingData("Z", "Z"), false),
				/* 1 */new FieldMappingData(new MappingData("getTimestampList"), new MappingData("Ljava/uti/List;", "Ljava/util/List;"), false),
				/* 2 */new FieldMappingData(new MappingData("getSectionList"), new MappingData("Ljava/uti/List;", "Ljava/util/List;"), false),
				/* 3 */new FieldMappingData(new MappingData("getProfilingMap"), new MappingData("Ljava/util/Map;", "Ljava/util/Map;"), false),
				/* 4 */new FieldMappingData(new MappingData("getSectionName"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false) };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData hook = (ClassMappingData) hookMap.getClassByRefactoredName("Profiler");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		addMethod(methodHooks[0].buildObfMethod(startEndMin));
		for (MethodNode m : InsnUtil.methods(cn, startEndMin.desc)) {
			if (m.name.equals(startEndMin.name)) {
				InstructionVector vector = new InstructionVector(Arrays.asList(m.instructions.toArray()), false, new InstructionFilter() {
					@Override
					public boolean accept(AbstractInsnNode t) {
						return t instanceof MethodInsnNode;
					}
				});
				MethodInsnNode end = (MethodInsnNode) vector.getAt(0);
				MethodInsnNode start = (MethodInsnNode) vector.getAt(1);
				addMethod(methodHooks[1].buildObfMethod(end));
				addMethod(methodHooks[2].buildObfMethod(start));
				findFields(end);
			}
		}
		
		all: for (MethodNode m : InsnUtil.methods(cn, "()V")) {
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof MethodInsnNode) {
					MethodInsnNode min = (MethodInsnNode) ain;
					if (min.name.equals("clear")) {
						addMethod(methodHooks[3].buildObfMethod(m));
						break all;
					}
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/profiler/IProfiler");
	}
	
	private void findFields(MethodInsnNode min) {
		for (MethodNode m : cn.methods()) {
			if (m.name.equals(min.name) && m.desc.equals(min.desc)) {
				InstructionVector vector = new InstructionVector(Arrays.asList(m.instructions.toArray()), false, new InstructionFilter() {
					@Override
					public boolean accept(AbstractInsnNode t) {
						return t instanceof FieldInsnNode;
					}
				});
				FieldInsnNode enabled = (FieldInsnNode) vector.getAt(0);
				FieldInsnNode ts = (FieldInsnNode) vector.getAt(1);
				FieldInsnNode sl = (FieldInsnNode) vector.getAt(3);
				FieldInsnNode pm = (FieldInsnNode) vector.getAt(5);
				FieldInsnNode sec = (FieldInsnNode) vector.getAt(6);
				
				addField(fieldHooks[0].buildObf(enabled));
				addField(fieldHooks[1].buildObf(ts));
				addField(fieldHooks[2].buildObf(sl));
				addField(fieldHooks[3].buildObf(pm));
				addField(fieldHooks[4].buildObf(sec));
			}
		}
		
		for (FieldNode f : getNode(hookMap.getClassByRefactoredName("Minecraft").getObfuscatedName()).fields()) {
			if (f.desc.equals("L" + cn.name + ";")) {
				MinecraftAnalyser mcAnalyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
				mcAnalyser.addField(mcAnalyser.getFieldHooks()[27].buildObf(f));
				break;
			}
		}
	}
	
	public void findEndStart(MethodInsnNode min) {
		startEndMin = min;
		classHook.setObfuscatedName(min.owner);
	}
}
