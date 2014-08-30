package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class EntityPlayerAnalyser extends Analyser {
	
	public EntityPlayerAnalyser() {
		super("EntityPlayer");
		hooks = new FieldHook[] {
				new FieldHook("getInventory", "L" + INTERFACES + "item/container/IPlayerInventory;"),
				new FieldHook("getInventoryContainer", "L" + INTERFACES + "item/container/IContainer;"),
				new FieldHook("getOpenContainer", "L" + INTERFACES + "item/container/IContainer;"), };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityPlayer"))
			return true;
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IEntityPlayer", INTERFACES + "entity/IEntityLivingBase"));
		map.addClass(new ClassHook(cn.superName, "EntityLivingBase"));
	}
	
	// Called from PlayerInventoryAnalyser
	public void findPlayerInventory(ClassHook playerInventoryHook) {
		for(FieldNode fNode : fields(cn)) {
			if (fNode.desc.equals("L" + playerInventoryHook.getObfuscatedName() + ";")) {
				addHook(hooks[0].buildObfFn(fNode)); // getinventory
				break;
			}
		}
	}
}