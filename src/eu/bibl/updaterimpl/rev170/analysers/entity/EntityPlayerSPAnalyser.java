package eu.bibl.updaterimpl.rev170.analysers.entity;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.MovementInputAnalyser;

public class EntityPlayerSPAnalyser extends Analyser {
	
	private static final int[] ENTITY_ACTION_STATE_REGEX = new int[] {
		ALOAD,
		ALOAD,
		GETFIELD,
		GETFIELD,
		PUTFIELD };
	
	public EntityPlayerSPAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityPlayerSP", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getMovementStafe"), new MappingData("F", "F"), false),
				/* 1 */new FieldMappingData(new MappingData("getMovementForward"), new MappingData("F", "F"), false),
				/* 2 */new FieldMappingData(new MappingData("isJumping"), new MappingData("Z", "Z"), false),
				/* 3 */new FieldMappingData(new MappingData("getRenderArmPitch"), new MappingData("F", "F"), false),
				/* 4 */new FieldMappingData(new MappingData("getRenderArmYaw"), new MappingData("F", "F"), false), };
		// methodHooks = new CallbackMappingData[] { new BytecodeCallbackMappingData(null, "addChatMessage", "(Ljava/lang/String;)V") };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if ((c != null) && c.getRefactoredName().equals("EntityPlayerSP"))
			return true;
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "AbstractClientPlayer", null));
		
		hookAddChatMessage((ClassMappingData) hookMap.getClassByRefactoredName("GuiIngame"));
		
		findMovementInputFields();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityPlayerSP");
	}
	
	private void findMovementInputFields() {
		for (MethodNode m : cn.methods()) {
			InstructionSearcher is = new InstructionSearcher(m.instructions, ENTITY_ACTION_STATE_REGEX);
			if (is.search() && (is.size() == 3)) {
				ClassMappingData movementInput = new ClassMappingData(Type.getType(((FieldInsnNode) is.getMatches().get(0)[2]).desc).getClassName(), "MovementInput", null);
				hookMap.addClass(movementInput);
				MovementInputAnalyser analyser = (MovementInputAnalyser) AnalyserCache.contextGet("MovementInput");
				for (int i = 0; i < is.size(); i++) {
					FieldInsnNode movementFin = (FieldInsnNode) is.getMatches().get(i)[3];
					hookMap.addField(analyser.getFieldHooks()[i].buildObf(movementFin).setFieldOwner(movementInput).identify());
					
					FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[4];
					addField(fieldHooks[i].buildObf(fin));
				}
				
				int found = 0;
				
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == D2F) {
						FieldInsnNode fin = (FieldInsnNode) ain.getNext();
						found++;
						if (found == 1) {
							addField(fieldHooks[3].buildObf(fin));
						} else if (found == 2) {
							addField(fieldHooks[4].buildObf(fin));
						} else {
							break;
						}
					}
				}
				
				break;
			}
		}
	}
	
	private void hookAddChatMessage(ClassMappingData guiIngameHook) {
		addChatMessageFor: for (MethodNode mNode : cn.methods()) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof FieldInsnNode) {
					FieldInsnNode fin = (FieldInsnNode) ain;
					if (fin.desc.equals("L" + guiIngameHook.getObfuscatedName() + ";")) {
						String paramClassName = hookMap.getClassByRefactoredName("TranslatedChatComponent").getObfuscatedName();
						InsnList addChatList = new InsnList();
						addChatList.add(new VarInsnNode(ALOAD, 0));
						addChatList.add(new TypeInsnNode(NEW, paramClassName));
						addChatList.add(new InsnNode(DUP));
						addChatList.add(new VarInsnNode(ALOAD, 1));
						addChatList.add(new InsnNode(ICONST_0));
						addChatList.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
						addChatList.add(new MethodInsnNode(INVOKESPECIAL, paramClassName, "<init>", "(Ljava/lang/String;[Ljava/lang/Object;)V"));
						addChatList.add(new MethodInsnNode(INVOKEVIRTUAL, cn.name, mNode.name, mNode.desc));
						addChatList.add(new InsnNode(RETURN));
						// BytecodeCallbackMappingData addChatMessageHook = (BytecodeCallbackMappingData) methodHooks[0];
						// addChatMessageHook.setInstructions(addChatList);
						// addChatMessageHook.buildObfMn(mNode);
						// addHook(addChatMessageHook);
						break addChatMessageFor;
					}
				}
			}
		}
	}
}
