package eu.bibl.updaterimpl.rev170.analysers.ui;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class GuiIngameAnalyser extends Analyser {
	
	public static final int[] GUI_NEW_CHAT_REGEX = new int[] { ALOAD, GETFIELD };
	
	public GuiIngameAnalyser() {
		super("GuiIngame");
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
						if (ldc.cst.toString().equals("Allocated memory: ")) {
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
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "ui/IGuiIngame"));
		
		for(MethodNode mNode : methods(cn)) {
			InsnSearcher searcher = new InsnSearcher(mNode.instructions, 0, GUI_NEW_CHAT_REGEX);
			if (searcher.match() && mNode.instructions.getLast().getOpcode() == ARETURN) {
				AbstractInsnNode[] ains = searcher.getMatches().get(0);
				FieldInsnNode fin = (FieldInsnNode) ains[1];
				if (fin.desc.startsWith("L") && fin.desc.endsWith(";")) {
					String guiNewChat = fin.desc.substring(1, fin.desc.length()).replace(";", "");
					ClassHook guinc = new ClassHook(guiNewChat, "GuiNewChat");
					map.addClass(guinc);
					break;
				}
			}
		}
	}
}