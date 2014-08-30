package eu.bibl.updaterimpl.rev170.analysers.item;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class ItemAnalyser extends Analyser {
	
	public ItemAnalyser() {
		super("Item");
		hooks = new FieldHook[] { new FieldHook("getPotionEffect", "Ljava/lang/String;", "Ljava/lang/String;"), new FieldHook("getName", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null)
			return c.getRefactoredName().equals("Item");
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "item/IItem"));
		
		ArrayList<FieldNode> stringFieldNodes = fields(cn, "Ljava/lang/String;");
		FieldNode potionEffectFieldNode = stringFieldNodes.get(0);
		FieldNode nameFieldNode = stringFieldNodes.get(1);
		addHook(hooks[0].buildObfFn(potionEffectFieldNode));
		addHook(hooks[1].buildObfFn(nameFieldNode));
	}
}