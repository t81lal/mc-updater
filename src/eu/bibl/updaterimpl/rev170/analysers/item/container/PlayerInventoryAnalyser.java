package eu.bibl.updaterimpl.rev170.analysers.item.container;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityPlayerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.item.ItemStackAnalyser;

public class PlayerInventoryAnalyser extends Analyser {
	
	public PlayerInventoryAnalyser() {
		super("PlayerInventory");
		hooks = new FieldHook[] {
				new FieldHook("getOwningPlayer", "L" + INTERFACES + "entity/IEntityPlayer;"),
				new FieldHook("getInventoryItems", "[L" + INTERFACES + "item/IItemStack;"),
				new FieldHook("getArmourItems", "[L" + INTERFACES + "item/IItemStack;"),
				new FieldHook("getCurrentSlot", "I", "I") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		for(MethodNode mNode : methods(cn)) {
			if (mNode.desc.startsWith("(L") && mNode.desc.endsWith(";)Z")) {
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("Item being added"))
								return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "item/container/IPlayerInventory"));
		
		findItemItemStackAndGetItemIDAndDamage();
		findInventoryArmourItemsAndOwningPlayer();
		findCurrentSlot();
		findPlayerInventory();
	}
	
	private void findInventoryArmourItemsAndOwningPlayer() {
		inventoryFor: for(MethodNode m : methods(cn)) {
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
					addHook(hooks[0].buildObfFin(playerFin));// getowningplayer
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
							addHook(hooks[1].buildObfFin(fin));// getinventoryitems
							foundMainInventory = true;
						}
					} else
						if (ain.getOpcode() == ICONST_4) {
							FieldInsnNode fin = (FieldInsnNode) ain.getNext().getNext();
							addHook(hooks[2].buildObfFin(fin));// getarmouritems
						}
					if (foundMainInventory && foundArmourInventory)
						break inventoryFor;
				}
			}
		}
	}
	
	private void findItemItemStackAndGetItemIDAndDamage() {
		ItemStackAnalyser analyser = (ItemStackAnalyser) analysers.get("ItemStack");
		for(MethodNode mNode : methods(cn)) {
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
									map.addClass(new ClassHook(itemStackClass, "ItemStack"));
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
									// addHook(methodHooks[0].buildObfMin(localMin));//getitem
									Type returnType = Type.getReturnType(localMin.desc);
									String itemClass = returnType.getInternalName().substring(0, returnType.getInternalName().length()).replace(";", "");
									map.addClass(new ClassHook(itemClass, "Item"));
									localMin = (MethodInsnNode) localMin.getNext();
									MinecraftAnalyser minecraftAnalyser = (MinecraftAnalyser) analysers.get("Minecraft");
									addMinecraftHook(minecraftAnalyser.getMethodHooks()[1].buildObfMin(localMin), map.getClassByRefactoredName("Item").getObfuscatedName());
								} else
									if (strung.equals("Item data")) {
										MethodInsnNode localMin = (MethodInsnNode) ain.getNext().getNext();
										// addHook(methodHooks[1].buildObfMin(localMin));//getdamage
									}
							}
						}
					}
				}
			}
		}
	}
	
	private void findPlayerInventory() {
		EntityPlayerAnalyser analyser = (EntityPlayerAnalyser) analysers.get("EntityPlayer");
		analyser.findPlayerInventory(classHook);
	}
	
	private void findCurrentSlot() {
		FieldNode currentSlotFieldNode = fields(cn, "I").get(0);
		addHook(hooks[3].buildObfFn(currentSlotFieldNode));
	}
}