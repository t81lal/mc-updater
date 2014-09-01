package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class UseEntityActionAnalyser extends Analyser {
	
	public UseEntityActionAnalyser(ClassContainer container, HookMap hookMap) {
		super("UseEntityAction", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData(new MappingData("getActionID"), new MappingData("I", "I"), false) };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "INTERACT");
	}
	
	@Override
	public InterfaceMappingData run() {
		MinecraftAnalyser analyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
		analyser.addField(analyser.getFieldHooks()[8].buildObf(InsnUtil.fields(cn, "[L" + cn.name + ";").get(0)));
		
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("<init>")) {
				addField(fieldHooks[0].buildObf((FieldInsnNode) InsnUtil.getNext(m.instructions.getFirst(), PUTFIELD)));
				break;
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IUseEntityAction");
	}
}
