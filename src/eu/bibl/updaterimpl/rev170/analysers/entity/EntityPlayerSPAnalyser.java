package eu.bibl.updaterimpl.rev170.analysers.entity;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.BytecodeMethodHook;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.client.MovementInputAnalyser;

public class EntityPlayerSPAnalyser extends Analyser {
	
	private static final int[] ENTITY_ACTION_STATE_REGEX = new int[] {
			ALOAD,
			ALOAD,
			GETFIELD,
			GETFIELD,
			PUTFIELD };
	
	public EntityPlayerSPAnalyser() {
		super("EntityPlayerSP");
		hooks = new FieldHook[] {
				new FieldHook("getMovementStafe", "F", "F"),
				new FieldHook("getMovementForward", "F", "F"),
				new FieldHook("isJumping", "Z", "Z"),
				new FieldHook("getRenderArmPitch", "F", "F"),
				new FieldHook("getRenderArmYaw", "F", "F"), };
		methodHooks = new MethodHook[] { new BytecodeMethodHook(null, "addChatMessage", "(Ljava/lang/String;)V") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityPlayerSP"))
			return true;
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IEntityPlayerSP", INTERFACES + "entity/IAbstractClientPlayer"));
		map.addClass(new ClassHook(cn.superName, "AbstractClientPlayer"));
		
		hookAddChatMessage(map.getClassByRefactoredName("GuiIngame"));
		
		findMovementInputFields();
	}
	
	private void findMovementInputFields() {
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, ENTITY_ACTION_STATE_REGEX);
			if (is.match() && is.size() == 3) {
				ClassHook movementInput = new ClassHook(Type.getType(((FieldInsnNode) is.getMatches().get(0)[2]).desc).getClassName(), "MovementInput");
				map.addClass(movementInput);
				MovementInputAnalyser analyser = (MovementInputAnalyser) analysers.get("MovementInput");
				for(int i = 0; i < is.size(); i++) {
					FieldInsnNode movementFin = (FieldInsnNode) is.getMatches().get(i)[3];
					map.addFieldHook(analyser.getHooks()[i].buildObfFin(movementFin).buildOwner(movementInput).identify());
					
					FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[4];
					addHook(hooks[i].buildObfFin(fin));
				}
				
				int found = 0;
				
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == D2F) {
						FieldInsnNode fin = (FieldInsnNode) ain.getNext();
						found++;
						if (found == 1) {
							addHook(hooks[3].buildObfFin(fin));
						} else
							if (found == 2) {
								addHook(hooks[4].buildObfFin(fin));
							} else {
								break;
							}
					}
				}
				
				break;
			}
		}
	}
	
	private void hookAddChatMessage(ClassHook guiIngameHook) {
		addChatMessageFor: for(MethodNode mNode : methods(cn)) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof FieldInsnNode) {
					FieldInsnNode fin = (FieldInsnNode) ain;
					if (fin.desc.equals("L" + guiIngameHook.getObfuscatedName() + ";")) {
						String paramClassName = map.getClassByRefactoredName("TranslatedChatComponent").getObfuscatedName();
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
						BytecodeMethodHook addChatMessageHook = (BytecodeMethodHook) methodHooks[0];
						addChatMessageHook.setInstructions(addChatList);
						addChatMessageHook.buildObfMn(mNode);
						addHook(addChatMessageHook);
						break addChatMessageFor;
					}
				}
			}
		}
	}
}