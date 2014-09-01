package eu.bibl.updaterimpl.rev170.analysers.item.container;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityPlayerAnalyser;

public class PlayerInventoryAnalyser extends Analyser {
	
	public PlayerInventoryAnalyser(ClassContainer container, HookMap hookMap) {
		super("PlayerInventory", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getOwningPlayer"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/IEntityPlayer;"), false),
				/* 1 */new FieldMappingData(new MappingData("getInventoryItems"), new MappingData("[L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;"), false),
				/* 2 */new FieldMappingData(new MappingData("getArmourItems"), new MappingData("[L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;"), false),
				/* 3 */new FieldMappingData(new MappingData("getCurrentSlot"), new MappingData("I", "I"), false), };
	}
	
	@Override
	public boolean accept() {
		for (MethodNode mNode : cn.methods()) {
			if (mNode.desc.startsWith("(L") && mNode.desc.endsWith(";)Z")) {
				if (InsnUtil.containsLdc(mNode, "Item being added"))
					return true;
			}
		}
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		findItemItemStackAndGetItemIDAndDamage();
		findInventoryArmourItemsAndOwningPlayer();
		findCurrentSlot();
		findPlayerInventory();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/container/IPlayerInventory");
	}
	
	private void findInventoryArmourItemsAndOwningPlayer() {
		inventoryFor: for (MethodNode m : cn.methods()) {
			if (m.name.equals("<init>")) {
				InsnList list = m.instructions;
				AbstractInsnNode tempAin = list.getLast();
				while (tempAin.getOpcode() != PUTFIELD) {
					if (tempAin.getPrevious() == null)
						break;
					tempAin = tempAin.getPrevious();
					if (tempAin.getOpcode() == PUTFIELD)
						break;
				}
				if (tempAin.getOpcode() == PUTFIELD) {
					FieldInsnNode playerFin = (FieldInsnNode) tempAin;
					addField(fieldHooks[0].buildObf(playerFin));// getowningplayer
				}
				boolean foundMainInventory = false;
				boolean foundArmourInventory = false;
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == BIPUSH) {
						IntInsnNode iin = (IntInsnNode) ain;
						if (iin.operand == 36) {
							FieldInsnNode fin = (FieldInsnNode) ain.getNext().getNext();
							addField(fieldHooks[1].buildObf(fin));// getinventoryitems
							foundMainInventory = true;
						}
					} else if (ain.getOpcode() == ICONST_4) {
						FieldInsnNode fin = (FieldInsnNode) ain.getNext().getNext();
						addField(fieldHooks[2].buildObf(fin));// getarmouritems
					}
					if (foundMainInventory && foundArmourInventory)
						break inventoryFor;
				}
			}
		}
	}
	
	private void findItemItemStackAndGetItemIDAndDamage() {
		// ItemStackAnalyser analyser = (ItemStackAnalyser) AnalyserCache.contextGet("ItemStack");
		for (MethodNode mNode : cn.methods()) {
			if (mNode.desc.startsWith("(L") && mNode.desc.endsWith(";)Z")) {
				boolean foundItemStackClass = false;
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (!foundItemStackClass) {
						if (ain instanceof LdcInsnNode) {
							LdcInsnNode ldc = (LdcInsnNode) ain;
							if (ldc.cst != null) {
								if (ldc.cst.toString().equals("Item being added")) {
									Type[] parameters = Type.getArgumentTypes(mNode.desc);
									String itemStackClass = parameters[0].getInternalName().substring(0, parameters[0].getInternalName().length()).replace(";", "");
									hookMap.addClass(new ClassMappingData(itemStackClass, "ItemStack", null));
									foundItemStackClass = true;
								}
							}
						}
					} else {
						if (ain instanceof LdcInsnNode) {
							LdcInsnNode ldc = (LdcInsnNode) ain;
							if (ldc.cst != null) {
								String strung = ldc.cst.toString();
								if (strung.equals("Item ID")) {
									MethodInsnNode localMin = (MethodInsnNode) ain.getNext().getNext();
									// addMethodHook(methodHooks[0].buildObfMin(localMin));//getitem
									Type returnType = Type.getReturnType(localMin.desc);
									String itemClass = returnType.getInternalName().substring(0, returnType.getInternalName().length()).replace(";", "");
									hookMap.addClass(new ClassMappingData(itemClass, "Item", null));
									localMin = (MethodInsnNode) localMin.getNext();
									MinecraftAnalyser minecraftAnalyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
									minecraftAnalyser.getMethodHooks()[1].setMethodOwner((ClassMappingData) hookMap.getClassByRefactoredName("Item"));
									addMethod(minecraftAnalyser.getMethodHooks()[1].buildObfMethod(localMin));
								} else if (strung.equals("Item data")) {
									// MethodInsnNode localMin = (MethodInsnNode) ain.getNext().getNext();
									// addMethodHook(methodHooks[1].buildObfMin(localMin));//getdamage
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void findPlayerInventory() {
		EntityPlayerAnalyser analyser = (EntityPlayerAnalyser) AnalyserCache.contextGet("EntityPlayer");
		analyser.findPlayerInventory(classHook);
	}
	
	private void findCurrentSlot() {
		FieldNode currentSlotFieldNode = InsnUtil.fields(cn, "I").get(0);
		addField(fieldHooks[3].buildObf(currentSlotFieldNode));
	}
}
