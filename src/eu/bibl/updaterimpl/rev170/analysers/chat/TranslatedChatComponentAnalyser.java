package eu.bibl.updaterimpl.rev170.analysers.chat;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class TranslatedChatComponentAnalyser extends Analyser {
	
	private ClassMappingData tccOwner;
	
	public TranslatedChatComponentAnalyser(ClassContainer container, HookMap hookMap) {
		super("TranslatedChatComponent", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		for (MethodNode mNode : cn.methods()) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if ((ldc.cst != null) && ldc.cst.toString().equals("tile.bed.occupied")) {
						AbstractInsnNode prevAin = ain.getPrevious();
						TypeInsnNode newInsn = (TypeInsnNode) prevAin.getPrevious();
						tccOwner = new ClassMappingData(newInsn.desc, "TranslatedChatComponent", null);
						hookMap.addClass(tccOwner);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		// NOTE, DO NOT USE cn.name AS THIS IS BLOCK_BED CLASS, NOT TCC!
		classHook = tccOwner;
		
		cn = getNode(classHook.getObfuscatedName());
		ClassMappingData chatStyle = new ClassMappingData(cn.superName, "ChatComponentStyle", null);
		hookMap.addClass(chatStyle);
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "chat/ITranslatableChatComponent");
	}
}
