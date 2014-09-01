package eu.bibl.updaterimpl.rev170.analysers.entity;

import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class EntityAnalyser extends Analyser {
	
	// server x,y,z regex
	// ALOAD 8
	// CHECKCAST net/minecraft/entity/Entity
	// ALOAD 1
	// INVOKEVIRTUAL
	// net/minecraft/network/play/server/S0EPacketSpawnObject.func_148997_d ()I
	// PUTFIELD net/minecraft/entity/Entity.serverPosX : I
	// ALOAD 8
	// CHECKCAST net/minecraft/entity/Entity
	// ALOAD 1
	// INVOKEVIRTUAL
	// net/minecraft/network/play/server/S0EPacketSpawnObject.func_148998_e ()I
	// PUTFIELD net/minecraft/entity/Entity.serverPosY : I
	// ALOAD 8
	// CHECKCAST net/minecraft/entity/Entity
	// ALOAD 1
	// INVOKEVIRTUAL
	// net/minecraft/network/play/server/S0EPacketSpawnObject.func_148994_f ()I
	// PUTFIELD net/minecraft/entity/Entity.serverPosZ : I
	
	private static final int[] WIDTHHEIGHT_REGEX = new int[] {
			ALOAD,
			FLOAD,
			PUTFIELD };
	private static final int[] DEAD_REGEX = new int[] {
			ALOAD,
			ICONST_1,
			PUTFIELD };
	private static final int[] KILL_REGEX = new int[] {
			ALOAD,
			INVOKEVIRTUAL };
	private static final int[] STEP_HEIGHT_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			F2D,
			DSTORE };
	private static final int[] IN_WATER_REGEX = new int[] {
			ALOAD,
			FCONST_0,
			PUTFIELD,
			ALOAD,
			ICONST_1,
			PUTFIELD,
			ALOAD,
			ICONST_0,
			PUTFIELD };
	private static final int[] CHUNK_POS_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			ISTORE,
			ALOAD,
			GETFIELD,
			ISTORE,
			ALOAD,
			GETFIELD };
	
	public EntityAnalyser(ClassContainer container, HookMap hookMap) {
		super("Entity", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getEntityID"), new MappingData("I", "I"), false),
				/* 1 */new FieldMappingData(new MappingData("getEntityRidingOnThis"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/IEntity;"), false),
				/* 2 */new FieldMappingData(new MappingData("getEntityRiden"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/IEntity;"), false),
				/* 3 */new FieldMappingData(new MappingData("getX"), new MappingData("D", "D"), false),
				/* 4 */new FieldMappingData(new MappingData("getY"), new MappingData("D", "D"), false),
				/* 5 */new FieldMappingData(new MappingData("getZ"), new MappingData("D", "D"), false),
				/* 6 */new FieldMappingData(new MappingData("getRotationPitch"), new MappingData("F", "F"), false),
				/* 7 */new FieldMappingData(new MappingData("getRotationYaw"), new MappingData("F", "F"), false),
				/* 8 */new FieldMappingData(new MappingData("isInWeb"), new MappingData("Z", "Z"), false),
				/* 9 */new FieldMappingData(new MappingData("getMotionX"), new MappingData("D", "D"), false),
				/* 10 */new FieldMappingData(new MappingData("getMotionY"), new MappingData("D", "D"), false),
				/* 11 */new FieldMappingData(new MappingData("getMotionZ"), new MappingData("D", "D"), false),
				/* 12 */new FieldMappingData(new MappingData("getFallDistance"), new MappingData("F", "F"), false),
				/* 13 */new FieldMappingData(new MappingData("getFireTime"), new MappingData("I", "I"), false),
				/* 14 */new FieldMappingData(new MappingData("isOnGround"), new MappingData("Z", "Z"), false),
				/* 15 */new FieldMappingData(new MappingData("getDimension"), new MappingData("I", "I"), false),
				/* 16 */new FieldMappingData(new MappingData("isInvincible"), new MappingData("Z", "Z"), false),
				/* 17 */new FieldMappingData(new MappingData("getWidth"), new MappingData("F", "F"), false),
				/* 18 */new FieldMappingData(new MappingData("getHeight"), new MappingData("F", "F"), false),
				/* 19 */new FieldMappingData(new MappingData("isDead"), new MappingData("Z", "Z"), false),
				/* 20 */new FieldMappingData(new MappingData("isColliding"), new MappingData("Z", "Z"), false),
				/* 21 */new FieldMappingData(new MappingData("isCollidingVertically"), new MappingData("Z", "Z"), false),
				/* 22 */new FieldMappingData(new MappingData("isCollidingHorizontally"), new MappingData("Z", "Z"), false),
				/* 23 */new FieldMappingData(new MappingData("getWorld"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "world/IWorld;"), false),
				/* 24 */new FieldMappingData(new MappingData("getStepHeight"), new MappingData("F", "F"), false),
				/* 25 */new FieldMappingData(new MappingData("getLifeInTicks"), new MappingData("I", "I"), false),
				/* 26 */new FieldMappingData(new MappingData("getBoundingBox"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), false),
				/* 27 */new FieldMappingData(new MappingData("isInWater"), new MappingData("Z", "Z"), false),
				/* 28 */new FieldMappingData(new MappingData("getInFireTicks"), new MappingData("I", "I"), false),
				/* 29 */new FieldMappingData(new MappingData("getInFireResistanceTicks"), new MappingData("I", "I"), false),
				/* 30 */new FieldMappingData(new MappingData("getHurtResistanceTicks"), new MappingData("I", "I"), false),
				/* 31 */new FieldMappingData(new MappingData("isImmuneToFire"), new MappingData("Z", "Z"), false),
				/* 32 */new FieldMappingData(new MappingData("getDataWatcher"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/IDataWatcher;"), false),
				/* 33 */new FieldMappingData(new MappingData("isAddedToChunk"), new MappingData("Z", "Z"), false),
				/* 34 */new FieldMappingData(new MappingData("getChunkCoordX"), new MappingData("I", "I"), false),
				/* 35 */new FieldMappingData(new MappingData("getChunkCoordY"), new MappingData("I", "I"), false),
				/* 36 */new FieldMappingData(new MappingData("getChunkCoordZ"), new MappingData("I", "I"), false),
				/* 37 */new FieldMappingData(new MappingData("isSpawnForced"), new MappingData("Z", "Z"), false), };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("onUpdate"), new MappingData("()V", "()V"), null, null, false),
				new CallbackMappingData(new MappingData("onEntityUpdate"), new MappingData("()V", "()V"), null, null, false),
				new CallbackMappingData(new MappingData("setPos"), new MappingData("(DDD)V", "(DDD)V"), null, null, false)
		
		};
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if ((c != null) && c.getRefactoredName().equals("Entity"))
			return true;
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		
		findEntityID();
		findRidingEntities();
		findXYZPitchYaw();
		findMotionXYZInWeb();
		findEntityNBTFields();
		findWidthHeight();
		findIsDead();
		findCollisionVars();
		findWorldField();
		findStepHeightField();
		findLifeInTicksField();
		findBoundingBoxField();
		findInWaterFireTicksFireHurtResistance();
		findFireImmunityHook();
		findDataWatcherHook();
		findChunkPositions();
		
		findOnUpdateMethod();
		findSetPosMethod();
		
		// found in worldanalyser
		addField(fieldHooks[37]);
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntity");
	}
	
	private void findSetPosMethod() {
		for (MethodNode m : InsnUtil.methods(cn, "(DDD)V")) {
			InstructionSearcher is = new InstructionSearcher(m.instructions, new int[] { FSUB });
			if (is.search() && (is.size() == 2)) {
				addMethod(methodHooks[2].buildObfMethod(m));
				break;
			}
		}
	}
	
	private void findOnUpdateMethod() {
		for (MethodNode m : InsnUtil.methods(cn, "()V")) {
			if ((m.access & ACC_STATIC) != 0)
				continue;
			if (m.instructions.size() != 3)
				continue;
			AbstractInsnNode ain = m.instructions.getFirst().getNext();
			if (ain.getOpcode() != INVOKEVIRTUAL)
				continue;
			MethodInsnNode min = (MethodInsnNode) ain;
			if (!min.owner.equals(cn.name))
				continue;
			MethodNode m1 = getMethod(min);
			if (InsnUtil.containsLdc(m1, "entityBaseTick")) {
				addMethod(methodHooks[0].buildObfMethod(m));
				addMethod(methodHooks[1].buildObfMethod(m1));
				break;
			}
		}
	}
	
	private MethodNode getMethod(MethodInsnNode min) {
		for (MethodNode m : cn.methods()) {
			if (m.name.equals(min.name) && m.desc.equals(min.desc))
				return m;
		}
		return null;
	}
	
	private boolean checkPositionFieldSetters(VarInsnNode vin) {
		if (vin.var == 0) {
			if (vin.getNext() instanceof VarInsnNode) {
				VarInsnNode vin1 = (VarInsnNode) vin.getNext();
				if (vin1.var == 0) {
					if (vin1.getNext().getOpcode() == GETFIELD) {
						if (vin1.getNext().getNext().getOpcode() == PUTFIELD) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void findEntityID() {
		for (MethodNode mNode : cn.methods()) {
			if (mNode.name.equals("<init>")) {
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == PUTFIELD) {
						FieldInsnNode fin = (FieldInsnNode) ain;
						addField(fieldHooks[0].buildObf(fin));
						break;
					}
				}
			}
		}
	}
	
	private void findRidingEntities() {
		List<FieldNode> entityFields = InsnUtil.fields(cn, "L" + cn.name + ";");
		if (entityFields.size() == 2) {
			FieldNode firstEntityNode = entityFields.get(0);
			FieldNode secondEntityNode = entityFields.get(1);
			addField(fieldHooks[1].buildObf(firstEntityNode));
			addField(fieldHooks[2].buildObf(secondEntityNode));
		}
	}
	
	private void findXYZPitchYaw() {
		posFor: for (MethodNode mNode : cn.methods()) {
			boolean foundLdc = false;
			int vCount = 0;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (!foundLdc) {
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("entityBaseTick")) {
								foundLdc = true;
							}
						}
					}
				} else {
					if (ain instanceof VarInsnNode) {
						VarInsnNode vin = (VarInsnNode) ain;
						if (checkPositionFieldSetters(vin)) {
							vCount++;
							FieldInsnNode fin1 = (FieldInsnNode) vin.getNext().getNext();
							if (vCount == 2) {
								addField(fieldHooks[3].buildObf(fin1));
							} else if (vCount == 3) {
								addField(fieldHooks[4].buildObf(fin1));
							} else if (vCount == 4) {
								addField(fieldHooks[5].buildObf(fin1));
							} else if (vCount == 5) {
								addField(fieldHooks[6].buildObf(fin1));
							} else if (vCount == 6) {
								addField(fieldHooks[7].buildObf(fin1));
							} else if (vCount > 6) {
								break posFor;
							}
						}
					}
				}
			}
		}
	}
	
	private void findMotionXYZInWeb() {
		motionPosFor: for (MethodNode mNode : cn.methods()) {
			boolean foundLdc = false;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (!foundLdc) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst != null) {
							if (((LdcInsnNode) ain).cst.toString().equals("move")) {
								foundLdc = true;
							}
						}
					}
				} else {
					int[] pat = new int[] {
							ALOAD,
							DCONST_0,
							PUTFIELD };
					InstructionSearcher is = new InstructionSearcher(mNode.instructions, pat);
					is.search();
					for (int i = 0; i < is.getMatches().size(); i++) {
						FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[2];
						if (i == 0) {
							AbstractInsnNode fin1 = fin.getPrevious();
							while (fin1.getOpcode() != PUTFIELD) {
								fin1 = fin1.getPrevious();
								if (fin1.getOpcode() == PUTFIELD)
									break;
							}
							if (fin1.getOpcode() == PUTFIELD) {
								FieldInsnNode fin2 = (FieldInsnNode) fin1;
								addField(fieldHooks[8].buildObf(fin2));
							}
							addField(fieldHooks[9].buildObf(fin));
						} else if (i == 1) {
							addField(fieldHooks[10].buildObf(fin));
						} else if (i == 2) {
							addField(fieldHooks[11].buildObf(fin));
						} else {
							break motionPosFor;
						}
					}
					break;
				}
			}
		}
	}
	
	private boolean findSavingMethod(MethodNode mNode) {
		ListIterator<?> it = mNode.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain instanceof LdcInsnNode) {
				LdcInsnNode ldc = (LdcInsnNode) ain;
				if (ldc.cst != null) {
					if (ldc.cst.toString().equals("Saving entity NBT")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void findEntityNBTFields() {
		for (MethodNode mNode : cn.methods()) {
			boolean foundSavingMethod = findSavingMethod(mNode);
			
			if (foundSavingMethod) {
				ListIterator<?> iter = mNode.instructions.iterator();
				while (iter.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) iter.next();
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("FallDistance")) {
								AbstractInsnNode nextInsn = ain;
								findingWhile: while (nextInsn.getOpcode() != GETFIELD) {
									if (nextInsn.getNext() == null)
										break findingWhile;
									nextInsn = nextInsn.getNext();
									if (nextInsn.getOpcode() == GETFIELD)
										break findingWhile;
								}
								if (nextInsn.getOpcode() == GETFIELD) {
									FieldInsnNode fin = (FieldInsnNode) nextInsn;
									addField(fieldHooks[12].buildObf(fin));
								}
							} else if (ldc.cst.toString().equals("OnGround")) {
								AbstractInsnNode nextInsn = ain;
								findingWhile: while (nextInsn.getOpcode() != GETFIELD) {
									if (nextInsn.getNext() == null)
										break findingWhile;
									nextInsn = nextInsn.getNext();
									if (nextInsn.getOpcode() == GETFIELD)
										break findingWhile;
								}
								if (nextInsn.getOpcode() == GETFIELD) {
									FieldInsnNode fin = (FieldInsnNode) nextInsn;
									addField(fieldHooks[14].buildObf(fin));
								}
							} else if (ldc.cst.toString().equals("Dimension")) {
								AbstractInsnNode nextInsn = ain;
								findingWhile: while (nextInsn.getOpcode() != GETFIELD) {
									if (nextInsn.getNext() == null)
										break findingWhile;
									nextInsn = nextInsn.getNext();
									if (nextInsn.getOpcode() == GETFIELD)
										break findingWhile;
								}
								if (nextInsn.getOpcode() == GETFIELD) {
									FieldInsnNode fin = (FieldInsnNode) nextInsn;
									addField(fieldHooks[15].buildObf(fin));
								}
							} else if (ldc.cst.toString().equals("Invulnerable")) {
								AbstractInsnNode nextInsn = ain;
								findingWhile: while (nextInsn.getOpcode() != GETFIELD) {
									if (nextInsn.getNext() == null)
										break findingWhile;
									nextInsn = nextInsn.getNext();
									if (nextInsn.getOpcode() == GETFIELD)
										break findingWhile;
								}
								if (nextInsn.getOpcode() == GETFIELD) {
									FieldInsnNode fin = (FieldInsnNode) nextInsn;
									addField(fieldHooks[16].buildObf(fin));
								}
							} else if (ldc.cst.toString().equals("PortalCoolDown")) {
								AbstractInsnNode nextInsn = ain;
								findingWhile: while (nextInsn.getOpcode() != GETFIELD) {
									if (nextInsn.getNext() == null)
										break findingWhile;
									nextInsn = nextInsn.getNext();
									if (nextInsn.getOpcode() == GETFIELD)
										break findingWhile;
								}
								if (nextInsn.getOpcode() == GETFIELD) {
									// FieldInsnNode fin = (FieldInsnNode) nextInsn;
									// addHook(new FieldMappingData(classHook, fin.name, "getPortalCoolDown", fin.desc, fin.desc, false));
								}
							} else if (ldc.cst.toString().equals("Fire")) {
								AbstractInsnNode nextInsn = ain;
								findingWhile: while (nextInsn.getOpcode() != GETFIELD) {
									if (nextInsn.getNext() == null)
										break findingWhile;
									nextInsn = nextInsn.getNext();
									if (nextInsn.getOpcode() == GETFIELD)
										break findingWhile;
								}
								if (nextInsn.getOpcode() == GETFIELD) {
									FieldInsnNode fin = (FieldInsnNode) nextInsn;
									addField(fieldHooks[13].buildObf(fin));
								}
							}
						}
					}
				}
				break;
			}
		}
	}
	
	private void findWidthHeight() {
		for (MethodNode mNode : cn.methods()) {
			InstructionSearcher is = new InstructionSearcher(mNode.instructions, WIDTHHEIGHT_REGEX);
			if (is.search()) {
				if (is.getMatches().size() == 2) {
					AbstractInsnNode[] firstPatAins = is.getMatches().get(0);
					AbstractInsnNode[] secondPatAins = is.getMatches().get(1);
					AbstractInsnNode firstAin = firstPatAins[0];
					AbstractInsnNode prevAin = firstAin.getPrevious();
					if (prevAin.getOpcode() == FSTORE) {
						FieldInsnNode firstFin = (FieldInsnNode) firstPatAins[2];
						FieldInsnNode secondFin = (FieldInsnNode) secondPatAins[2];
						addField(fieldHooks[17].buildObf(firstFin));
						addField(fieldHooks[18].buildObf(secondFin));
					}
				}
			}
		}
	}
	
	private void findIsDead() {
		deadFor: for (MethodNode mNode : InsnUtil.methods(cn, "()V")) {
			InstructionSearcher is = new InstructionSearcher(mNode.instructions, DEAD_REGEX);
			if (is.search()) {
				if (mNode.instructions.size() == (DEAD_REGEX.length + 1)) {
					for (MethodNode methodNode : InsnUtil.methods(cn, "()V")) {
						InstructionSearcher searcher2 = new InstructionSearcher(methodNode.instructions, KILL_REGEX);
						if (searcher2.search()) {
							if (searcher2.getMatches().size() == 1) {
								if (methodNode.instructions.size() == (KILL_REGEX.length + 1)) {
									MethodInsnNode methodCallInsn = (MethodInsnNode) searcher2.getMatches().get(0)[1];
									String methodCalled = methodCallInsn.name;
									if (methodCalled.equals(mNode.name)) {
										FieldInsnNode deadFin = (FieldInsnNode) is.getMatches().get(0)[2];
										addField(fieldHooks[19].buildObf(deadFin));
										break deadFor;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void findCollisionVars() {
		collisionFor: for (MethodNode mNode : cn.methods()) {
			boolean foundRestLdc = false;
			ListIterator<?> it = mNode.instructions.iterator();
			AbstractInsnNode ain = null;
			while (it.hasNext()) {
				ain = (AbstractInsnNode) it.next();
				if (!foundRestLdc) {
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("rest")) {
								foundRestLdc = true;
								break;
							}
						}
					}
				}
			}
			if (foundRestLdc) {
				if (ain != null) {
					AbstractInsnNode localAin = ain.getNext().getNext();
					while (localAin.getOpcode() != INVOKEVIRTUAL) {
						if (localAin.getNext() == null)
							continue collisionFor;
						localAin = localAin.getNext();
						if (localAin.getOpcode() == INVOKEVIRTUAL)
							break;
					}
					if (localAin.getOpcode() == INVOKEVIRTUAL) {
						localAin = localAin.getPrevious().getPrevious();
						while (localAin.getOpcode() != PUTFIELD) {
							if (localAin.getPrevious() == null)
								continue collisionFor;
							localAin = localAin.getPrevious();
							if (localAin.getOpcode() == PUTFIELD)
								break;
						}
						if (localAin.getOpcode() == PUTFIELD) {
							FieldInsnNode isColldingFin = (FieldInsnNode) localAin;
							addField(fieldHooks[20].buildObf(isColldingFin));
							localAin = localAin.getPrevious();
							while (localAin.getOpcode() != PUTFIELD) {
								if (localAin.getPrevious() == null)
									continue collisionFor;
								localAin = localAin.getPrevious();
								if (localAin.getOpcode() == PUTFIELD)
									break;
							}
							localAin = localAin.getPrevious();
							while (localAin.getOpcode() != PUTFIELD) {
								if (localAin.getPrevious() == null)
									continue collisionFor;
								localAin = localAin.getPrevious();
								if (localAin.getOpcode() == PUTFIELD)
									break;
							}
							if (localAin.getOpcode() == PUTFIELD) {
								FieldInsnNode isColldingVerticallyFin = (FieldInsnNode) localAin;
								addField(fieldHooks[21].buildObf(isColldingVerticallyFin));
								localAin = localAin.getPrevious();
								while (localAin.getOpcode() != PUTFIELD) {
									if (localAin.getPrevious() == null)
										continue collisionFor;
									localAin = localAin.getPrevious();
									if (localAin.getOpcode() == PUTFIELD)
										break;
								}
								if (localAin.getOpcode() == PUTFIELD) {
									FieldInsnNode isColldingHorizontallyFin = (FieldInsnNode) localAin;
									addField(fieldHooks[22].buildObf(isColldingHorizontallyFin));
									break collisionFor;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void findWorldField() {
		addField(fieldHooks[23].buildObf(InsnUtil.fields(cn, "L" + hookMap.getClassByRefactoredName("World").getObfuscatedName() + ";").get(0)));
	}
	
	private void findStepHeightField() {
		for (MethodNode m : cn.methods()) {
			InstructionSearcher is = new InstructionSearcher(m.instructions, STEP_HEIGHT_REGEX);
			if (is.search()) {
				addField(fieldHooks[24].buildObf((FieldInsnNode) is.getMatches().get(0)[1]));
				break;
			}
		}
	}
	
	private void findLifeInTicksField() {
		List<FieldNode> fields = cn.fields();
		for (int i = 0; i < fields.size(); i++) {
			FieldNode f = fields.get(i);
			if (f.desc.equals("Ljava/util/Random;")) {
				addField(fieldHooks[25].buildObf(fields.get(i + 1)));
				break;
			}
		}
	}
	
	private void findBoundingBoxField() {
		addField(fieldHooks[26].buildObf(InsnUtil.fields(cn, "L" + hookMap.getClassByRefactoredName("AxisAlignedBB").getObfuscatedName() + ";").get(0)));
	}
	
	private void findInWaterFireTicksFireHurtResistance() {
		for (MethodNode m : cn.methods()) {
			InstructionSearcher is = new InstructionSearcher(m.instructions, IN_WATER_REGEX);
			if (is.search()) {
				FieldInsnNode inWater = (FieldInsnNode) is.getMatches().get(0)[5];
				FieldInsnNode fireTicks = (FieldInsnNode) is.getMatches().get(0)[8];
				addField(fieldHooks[27].buildObf(inWater));
				addField(fieldHooks[28].buildObf(fireTicks));
				
				List<FieldNode> fields = InsnUtil.fields(cn, "I");
				for (int i = 0; i < fields.size(); i++) {
					FieldNode f = fields.get(i);
					if (f.name.equals(fireTicks.name)) {
						addField(fieldHooks[29].buildObf(fields.get(i - 1)));
						addField(fieldHooks[30].buildObf(fields.get(i + 1)));
						break;
					}
				}
				break;
			}
		}
	}
	
	private void findFireImmunityHook() {
		for (MethodNode m : InsnUtil.methods(cn, "()Z")) {
			if ((m.instructions.size() != 3) || !((m.access & ACC_FINAL) != 0))
				continue;
			InstructionSearcher is = new InstructionSearcher(m.instructions, new int[] {
					ALOAD,
					GETFIELD });
			if (is.search()) {
				FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[1];
				addField(fieldHooks[31].buildObf(fin));
				break;
			}
		}
	}
	
	private void findDataWatcherHook() {
		addField(fieldHooks[32].buildObf(InsnUtil.fields(cn, "L" + hookMap.getClassByRefactoredName("DataWatcher").getObfuscatedName() + ";").get(0)));
	}
	
	private void findChunkPositions() {
		ClassNode worldClient = getNode(hookMap.getClassByRefactoredName("WorldClient").getObfuscatedName());
		for (MethodNode m : worldClient.methods()) {
			InstructionSearcher is = new InstructionSearcher(m.instructions, CHUNK_POS_REGEX);
			if (is.search()) {
				AbstractInsnNode[] ains = is.getMatches().get(1);
				FieldInsnNode chunkx = (FieldInsnNode) ains[1];
				FieldInsnNode chunkz = (FieldInsnNode) ains[4];
				FieldInsnNode added = (FieldInsnNode) ains[7];
				addField(fieldHooks[33].buildObf(added));
				addField(fieldHooks[34].buildObf(chunkx));
				addField(fieldHooks[36].buildObf(chunkz));
				
				List<FieldNode> fields = InsnUtil.fields(cn, "I");
				for (int i = 0; i < fields.size(); i++) {
					FieldNode f = fields.get(i);
					if (f.name.equals(chunkx.name)) {
						addField(fieldHooks[35].buildObf(fields.get(i + 1)));
						break;
					}
				}
				break;
			}
		}
	}
}
