package eu.bibl.updaterimpl.rev170.analysers.ui;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class GuiIngameAnalyser extends Analyser {
	
	public static final int[] GUI_NEW_CHAT_REGEX = new int[] {
		ALOAD,
		GETFIELD };
	
	public GuiIngameAnalyser(ClassContainer container, HookMap hookMap) {
		super("GuiIngame", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		for (MethodNode mNode : cn.methods()) {
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
	public InterfaceMappingData run() {
		for (MethodNode mNode : cn.methods()) {
			InstructionSearcher searcher = new InstructionSearcher(mNode.instructions, GUI_NEW_CHAT_REGEX);
			if (searcher.search() && (mNode.instructions.getLast().getOpcode() == ARETURN)) {
				AbstractInsnNode[] ains = searcher.getMatches().get(0);
				FieldInsnNode fin = (FieldInsnNode) ains[1];
				if (fin.desc.startsWith("L") && fin.desc.endsWith(";")) {
					String guiNewChat = fin.desc.substring(1, fin.desc.length()).replace(";", "");
					ClassMappingData guinc = new ClassMappingData(guiNewChat, "GuiNewChat", null);
					hookMap.addClass(guinc);
					break;
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "ui/IGuiIngame");
	}
}
