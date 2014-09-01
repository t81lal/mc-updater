package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
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

public class WorldTypeAnalyser extends Analyser {
	
	public WorldTypeAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldType", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				// mc hooks
				// new FieldMappingData("getWorldTypes", "[L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
				// new FieldMappingData("getDefaultWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
				// new FieldMappingData("getFlatWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
				// new FieldMappingData("getLargeBiomesWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
				// new FieldMappingData("getAmplifiedWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
				// new FieldMappingData("getDefault1_1WorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
				// instance
				/* 0 */new FieldMappingData(new MappingData("getWorldTypeID"), new MappingData("I", "I"), false),
				/* 1 */new FieldMappingData(new MappingData("getWorldType"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false),
				/* 2 */new FieldMappingData(new MappingData("getGeneratorVersion"), new MappingData("I", "I"), false),
				/* 3 */new FieldMappingData(new MappingData("canBeGenerated"), new MappingData("Z", "Z"), false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "default_1_1");
	}
	
	@Override
	public InterfaceMappingData run() {
		MinecraftAnalyser analyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
		analyser.addField(analyser.getFieldHooks()[2].buildObf(InsnUtil.fields(cn, "[L" + cn.name + ";").get(0)));
		
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						FieldInsnNode fin = (FieldInsnNode) InsnUtil.getNext(ldc, PUTSTATIC);
						String cst = (String) ldc.cst;
						if (cst.equals("default")) {
							analyser.addField(analyser.getFieldHooks()[3].buildObf(fin));
						} else if (cst.equals("flat")) {
							analyser.addField(analyser.getFieldHooks()[4].buildObf(fin));
						} else if (cst.equals("largeBiomes")) {
							analyser.addField(analyser.getFieldHooks()[5].buildObf(fin));
						} else if (cst.equals("amplified")) {
							analyser.addField(analyser.getFieldHooks()[6].buildObf(fin));
						} else if (cst.equals("default_1_1")) {
							analyser.addField(analyser.getFieldHooks()[7].buildObf(fin));
						}
					}
				}
			} else if (m.name.equals("<init>")) {
				FieldInsnNode worldType = (FieldInsnNode) InsnUtil.getNext(m.instructions.getFirst(), PUTFIELD);
				if (worldType != null) {
					addField(fieldHooks[0].buildObf(worldType));
					FieldInsnNode generatorversion = (FieldInsnNode) InsnUtil.getNext(worldType.getNext(), PUTFIELD);
					addField(fieldHooks[1].buildObf(generatorversion));
					FieldInsnNode canbegenerated = (FieldInsnNode) InsnUtil.getNext(generatorversion.getNext(), PUTFIELD);
					addField(fieldHooks[2].buildObf(canbegenerated));
					FieldInsnNode worldtypeid = (FieldInsnNode) InsnUtil.getNext(canbegenerated.getNext(), PUTFIELD);
					addField(fieldHooks[3].buildObf(worldtypeid));
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorldType");
	}
}