package eu.bibl.updaterimpl.rev170.analysers.item.container;

import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityPlayerAnalyser;

public class ContainerAnalyser extends Analyser {
	
	public ContainerAnalyser(ClassContainer container, HookMap hookMap) {
		super("Container", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getSlots"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false),
				/* 1 */new FieldMappingData(new MappingData("getItemStacks"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "Listener already listening");
	}
	
	@Override
	public InterfaceMappingData run() {
		// TODO:
		// Finds player inventory container and open container in entityplayer
		findEntityPlayerInventoryFields();
		findItemStackAndSizeFields();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/container/IContainer");
	}
	
	private void findEntityPlayerInventoryFields() {
		EntityPlayerAnalyser analyser = (EntityPlayerAnalyser) AnalyserCache.contextGet("EntityPlayer");
		ClassMappingData entityPlayerHook = (ClassMappingData) hookMap.getClassByRefactoredName("EntityPlayer");
		if (entityPlayerHook != null) {
			ClassNode entityPlayerNode = getNode(entityPlayerHook.getObfuscatedName());
			List<FieldNode> containerFieldNodes = InsnUtil.fields(entityPlayerNode, "L" + cn.name + ";");
			if (containerFieldNodes.size() == 2) {
				FieldNode inventoryContainerFieldNode = containerFieldNodes.get(0);
				FieldNode openContainerFieldNode = containerFieldNodes.get(1);
				analyser.addField(analyser.getFieldHooks()[1].buildObf(inventoryContainerFieldNode));
				analyser.addField(analyser.getFieldHooks()[2].buildObf(openContainerFieldNode));
			}
		}
	}
	
	private void findItemStackAndSizeFields() {
		slotClassFor: for (MethodNode mNode : cn.methods()) {
			if (!mNode.name.equals("<init>")) {
				Type returnType = Type.getReturnType(mNode.desc);
				String slotClassName = returnType.getClassName();
				hookMap.addClass(new ClassMappingData(slotClassName, "Slot", null));
				
				ListIterator<?> it = mNode.instructions.iterator();
				int fieldInsns = 0;
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof FieldInsnNode) {
						fieldInsns++;
						if (fieldInsns == 3) {
							FieldInsnNode fin = (FieldInsnNode) ain;
							addField(fieldHooks[0].buildObf(fin));
						} else if (fieldInsns == 4) {
							FieldInsnNode fin = (FieldInsnNode) ain;
							addField(fieldHooks[1].buildObf(fin));
							break slotClassFor;
						}
					}
				}
				break;
			}
		}
	}
}
