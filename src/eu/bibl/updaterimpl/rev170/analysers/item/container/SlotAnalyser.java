package eu.bibl.updaterimpl.rev170.analysers.item.container;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class SlotAnalyser extends Analyser {
	
	public SlotAnalyser() {
		super("Slot");
		hooks = new FieldHook[] {
				new FieldHook("getIndex", "I", "I"),
				new FieldHook("getNumber", "I", "I"),
				new FieldHook("getXDisplayPos", "I", "I"),
				new FieldHook("getYDisplayPos", "I", "I") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook owner = map.getClassByObfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("Slot");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "item/container/ISlot"));
		
		findAll();
	}
	
	private void findAll() {
		ArrayList<FieldNode> intFieldNodes = fields(cn, "I");
		if (intFieldNodes.size() == 4) {
			FieldNode indexFieldNode = intFieldNodes.get(0);
			FieldNode numberFieldNode = intFieldNodes.get(1);
			FieldNode xDisplayPosFieldNode = intFieldNodes.get(2);
			FieldNode yDisplayPosFieldNode = intFieldNodes.get(3);
			addHook(hooks[0].buildObfFn(indexFieldNode));
			addHook(hooks[1].buildObfFn(numberFieldNode));
			addHook(hooks[2].buildObfFn(xDisplayPosFieldNode));
			addHook(hooks[3].buildObfFn(yDisplayPosFieldNode));
		}
	}
}