package eu.bibl.updaterimpl.rev170.analysers.item.container;

import java.util.ArrayList;
import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityPlayerAnalyser;

public class ContainerAnalyser extends Analyser {
	
	public ContainerAnalyser() {
		super("Container");
		hooks = new FieldHook[] {
				new FieldHook("getSlots", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getItemStacks", "Ljava/util/List;", "Ljava/util/List;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		for(MethodNode mNode : methods(cn)) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null) {
						if (ldc.cst.toString().equals("Listener already listening"))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "item/container/IContainer"));
		
		// TODO:
		// Finds player inventory container and open container in entityplayer
		findEntityPlayerInventoryFields();
		findItemStackAndSizeFields();
	}
	
	private void findEntityPlayerInventoryFields() {
		EntityPlayerAnalyser analyser = (EntityPlayerAnalyser) analysers.get("EntityPlayer");
		ClassHook entityPlayerHook = map.getClassByRefactoredName("EntityPlayer");
		if (entityPlayerHook != null) {
			ClassNode entityPlayerNode = analysisMap.requestNode(entityPlayerHook.getObfuscatedName());
			ArrayList<FieldNode> containerFieldNodes = fields(entityPlayerNode, "L" + cn.name + ";");
			if (containerFieldNodes.size() == 2) {
				FieldNode inventoryContainerFieldNode = containerFieldNodes.get(0);
				FieldNode openContainerFieldNode = containerFieldNodes.get(1);
				analyser.addHook(analyser.getHooks()[1].buildObfFn(inventoryContainerFieldNode));
				analyser.addHook(analyser.getHooks()[2].buildObfFn(openContainerFieldNode));
			}
		}
	}
	
	private void findItemStackAndSizeFields() {
		slotClassFor: for(MethodNode mNode : methods(cn)) {
			if (!mNode.name.equals("<init>")) {
				Type returnType = Type.getReturnType(mNode.desc);
				String slotClassName = returnType.getClassName();
				map.addClass(new ClassHook(slotClassName, "Slot"));
				
				ListIterator<?> it = mNode.instructions.iterator();
				int fieldInsns = 0;
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof FieldInsnNode) {
						fieldInsns++;
						if (fieldInsns == 3) {
							FieldInsnNode fin = (FieldInsnNode) ain;
							addHook(hooks[0].buildObfFin(fin));
						} else
							if (fieldInsns == 4) {
								FieldInsnNode fin = (FieldInsnNode) ain;
								addHook(hooks[1].buildObfFin(fin));
								break slotClassFor;
							}
					}
				}
				break;
			}
		}
	}
}