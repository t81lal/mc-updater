package eu.bibl.updaterimpl.rev170.analysers.entity;

import java.util.ArrayList;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.Access;
import eu.bibl.updater.analysis.Analyser;

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
	
	public EntityAnalyser() {
		super("Entity");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getEntityRidingOnThis", "L" + INTERFACES + "entity/IEntity;"),
				new FieldHook("getEntityRiden", "L" + INTERFACES + "entity/IEntity;"),
				new FieldHook("getX", "D", "D"),
				new FieldHook("getY", "D", "D"),
				new FieldHook("getZ", "D", "D"),
				new FieldHook("getRotationPitch", "F", "F"),
				new FieldHook("getRotationYaw", "F", "F"),
				new FieldHook("isInWeb", "Z", "Z"),
				new FieldHook("getMotionX", "D", "D"),
				new FieldHook("getMotionY", "D", "D"),
				new FieldHook("getMotionZ", "D", "D"),
				new FieldHook("getFallDistance", "F", "F"),
				new FieldHook("getFireTime", "I", "I"),
				new FieldHook("isOnGround", "Z", "Z"),
				new FieldHook("getDimension", "I", "I"),
				new FieldHook("isInvincible", "Z", "Z"),
				new FieldHook("getWidth", "F", "F"),
				new FieldHook("getHeight", "F", "F"),
				new FieldHook("isDead", "Z", "Z"),
				new FieldHook("isColliding", "Z", "Z"),
				new FieldHook("isCollidingVertically", "Z", "Z"),
				new FieldHook("isCollidingHorizontally", "Z", "Z"),
				new FieldHook("getWorld", "L" + INTERFACES + "world/IWorld;"),
				new FieldHook("getStepHeight", "F", "F"),
				new FieldHook("getLifeInTicks", "I", "I"),
				new FieldHook("getBoundingBox", "L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new FieldHook("isInWater", "Z", "Z"),
				new FieldHook("getInFireTicks", "I", "I"),
				new FieldHook("getInFireResistanceTicks", "I", "I"),
				new FieldHook("getHurtResistanceTicks", "I", "I"),
				new FieldHook("isImmuneToFire", "Z", "Z"),
				new FieldHook("getDataWatcher", "L" + INTERFACES + "entity/IDataWatcher;"),
				new FieldHook("isAddedToChunk", "Z", "Z"),
				new FieldHook("getChunkCoordX", "I", "I"),
				new FieldHook("getChunkCoordY", "I", "I"),
				new FieldHook("getChunkCoordZ", "I", "I"),
				new FieldHook("isSpawnForced", "Z", "Z") };
		methodHooks = new MethodHook[] {
				new MethodHook("onUpdate", "()V", "()V"),
				new MethodHook("onEntityUpdate", "()V", "()V"),
				new MethodHook("setPos", "(DDD)V", "(DDD)V")
		
		};
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("Entity"))
			return true;
		return false;
	}
	
	@Override
	public void run() {
		InterfaceHook interfaceHook = new InterfaceHook(classHook, INTERFACES + "entity/IEntity");
		classHook.setInterfaceHook(interfaceHook);
		
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
		addHook(hooks[37]);
	}
	
	private void findSetPosMethod() {
		for(MethodNode m : methods(cn, "(DDD)V")) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, FSUB);
			if (is.match() && is.size() == 2) {
				addHook(methodHooks[2].buildObfMn(m));
				break;
			}
		}
	}
	
	private void findOnUpdateMethod() {
		for(MethodNode m : methods(cn, "()V")) {
			if (Access.isStatic(m.access))
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
			if (containsLdc(m1, "entityBaseTick")) {
				addHook(methodHooks[0].buildObfMn(m));
				addHook(methodHooks[1].buildObfMn(m1));
				break;
			}
		}
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
		for(MethodNode mNode : methods(cn)) {
			if (mNode.name.equals("<init>")) {
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == PUTFIELD) {
						FieldInsnNode fin = (FieldInsnNode) ain;
						addHook(hooks[0].buildObfFin(fin));
						break;
					}
				}
			}
		}
	}
	
	private void findRidingEntities() {
		ArrayList<FieldNode> entityFields = fields(cn, "L" + cn.name + ";");
		if (entityFields.size() == 2) {
			FieldNode firstEntityNode = entityFields.get(0);
			FieldNode secondEntityNode = entityFields.get(1);
			addHook(hooks[1].buildObfFn(firstEntityNode));
			addHook(hooks[2].buildObfFn(secondEntityNode));
		}
	}
	
	private void findXYZPitchYaw() {
		posFor: for(MethodNode mNode : methods(cn)) {
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
								addHook(hooks[3].buildObfFin(fin1));
							} else if (vCount == 3) {
								addHook(hooks[4].buildObfFin(fin1));
							} else if (vCount == 4) {
								addHook(hooks[5].buildObfFin(fin1));
							} else if (vCount == 5) {
								addHook(hooks[6].buildObfFin(fin1));
							} else if (vCount == 6) {
								addHook(hooks[7].buildObfFin(fin1));
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
		motionPosFor: for(MethodNode mNode : methods(cn)) {
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
					InsnSearcher is = new InsnSearcher(mNode.instructions, 0, pat);
					is.match();
					for(int i = 0; i < is.getMatches().size(); i++) {
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
								addHook(hooks[8].buildObfFin(fin2));
							}
							addHook(hooks[9].buildObfFin(fin));
						} else if (i == 1) {
							addHook(hooks[10].buildObfFin(fin));
						} else if (i == 2) {
							addHook(hooks[11].buildObfFin(fin));
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
		for(MethodNode mNode : methods(cn)) {
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
									addHook(hooks[12].buildObfFin(fin));
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
									addHook(hooks[14].buildObfFin(fin));
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
									addHook(hooks[15].buildObfFin(fin));
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
									addHook(hooks[16].buildObfFin(fin));
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
									FieldInsnNode fin = (FieldInsnNode) nextInsn;
									addHook(new FieldHook(classHook, fin.name, "getPortalCoolDown", fin.desc, fin.desc, false));
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
									addHook(hooks[13].buildObfFin(fin));
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
		for(MethodNode mNode : methods(cn)) {
			InsnSearcher is = new InsnSearcher(mNode.instructions, 0, WIDTHHEIGHT_REGEX);
			if (is.match()) {
				if (is.getMatches().size() == 2) {
					AbstractInsnNode[] firstPatAins = is.getMatches().get(0);
					AbstractInsnNode[] secondPatAins = is.getMatches().get(1);
					AbstractInsnNode firstAin = firstPatAins[0];
					AbstractInsnNode prevAin = firstAin.getPrevious();
					if (prevAin.getOpcode() == FSTORE) {
						FieldInsnNode firstFin = (FieldInsnNode) firstPatAins[2];
						FieldInsnNode secondFin = (FieldInsnNode) secondPatAins[2];
						addHook(hooks[17].buildObfFin(firstFin));
						addHook(hooks[18].buildObfFin(secondFin));
					}
				}
			}
		}
	}
	
	private void findIsDead() {
		deadFor: for(MethodNode mNode : methods(cn, "()V")) {
			InsnSearcher is = new InsnSearcher(mNode.instructions, 0, DEAD_REGEX);
			if (is.match()) {
				if (mNode.instructions.size() == DEAD_REGEX.length + 1) {
					for(MethodNode methodNode : methods(cn, "()V")) {
						InsnSearcher searcher2 = new InsnSearcher(methodNode.instructions, 0, KILL_REGEX);
						if (searcher2.match()) {
							if (searcher2.getMatches().size() == 1) {
								if (methodNode.instructions.size() == KILL_REGEX.length + 1) {
									MethodInsnNode methodCallInsn = (MethodInsnNode) searcher2.getMatches().get(0)[1];
									String methodCalled = methodCallInsn.name;
									if (methodCalled.equals(mNode.name)) {
										FieldInsnNode deadFin = (FieldInsnNode) is.getMatches().get(0)[2];
										addHook(hooks[19].buildObfFin(deadFin));
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
		collisionFor: for(MethodNode mNode : methods(cn)) {
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
							addHook(hooks[20].buildObfFin(isColldingFin));
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
								addHook(hooks[21].buildObfFin(isColldingVerticallyFin));
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
									addHook(hooks[22].buildObfFin(isColldingHorizontallyFin));
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
		addHook(hooks[23].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("World").getObfuscatedName() + ";").get(0)));
	}
	
	private void findStepHeightField() {
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, STEP_HEIGHT_REGEX);
			if (is.match()) {
				addHook(hooks[24].buildObfFin((FieldInsnNode) is.getMatches().get(0)[1]));
				break;
			}
		}
	}
	
	private void findLifeInTicksField() {
		ArrayList<FieldNode> fields = fields(cn);
		for(int i = 0; i < fields.size(); i++) {
			FieldNode f = fields.get(i);
			if (f.desc.equals("Ljava/util/Random;")) {
				addHook(hooks[25].buildObfFn(fields.get(i + 1)));
				break;
			}
		}
	}
	
	private void findBoundingBoxField() {
		addHook(hooks[26].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("AxisAlignedBB").getObfuscatedName() + ";").get(0)));
	}
	
	private void findInWaterFireTicksFireHurtResistance() {
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, IN_WATER_REGEX);
			if (is.match()) {
				FieldInsnNode inWater = (FieldInsnNode) is.getMatches().get(0)[5];
				FieldInsnNode fireTicks = (FieldInsnNode) is.getMatches().get(0)[8];
				addHook(hooks[27].buildObfFin(inWater));
				addHook(hooks[28].buildObfFin(fireTicks));
				
				ArrayList<FieldNode> fields = fields(cn, "I");
				for(int i = 0; i < fields.size(); i++) {
					FieldNode f = fields.get(i);
					if (f.name.equals(fireTicks.name)) {
						addHook(hooks[29].buildObfFn(fields.get(i - 1)));
						addHook(hooks[30].buildObfFn(fields.get(i + 1)));
						break;
					}
				}
				break;
			}
		}
	}
	
	private void findFireImmunityHook() {
		for(MethodNode m : methods(cn, "()Z")) {
			if (m.instructions.size() != 3 || !Access.isFinal(m.access))
				continue;
			InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] {
					ALOAD,
					GETFIELD });
			if (is.match()) {
				FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[1];
				addHook(hooks[31].buildObfFin(fin));
				break;
			}
		}
	}
	
	private void findDataWatcherHook() {
		addHook(hooks[32].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("DataWatcher").getObfuscatedName() + ";").get(0)));
	}
	
	private void findChunkPositions() {
		ClassNode worldClient = analysisMap.requestNode(map.getClassByRefactoredName("WorldClient").getObfuscatedName());
		for(MethodNode m : methods(worldClient)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, CHUNK_POS_REGEX);
			if (is.match()) {
				AbstractInsnNode[] ains = is.getMatches().get(1);
				FieldInsnNode chunkx = (FieldInsnNode) ains[1];
				FieldInsnNode chunkz = (FieldInsnNode) ains[4];
				FieldInsnNode added = (FieldInsnNode) ains[7];
				addHook(hooks[33].buildObfFin(added));
				addHook(hooks[34].buildObfFin(chunkx));
				addHook(hooks[36].buildObfFin(chunkz));
				
				ArrayList<FieldNode> fields = fields(cn, "I");
				for(int i = 0; i < fields.size(); i++) {
					FieldNode f = fields.get(i);
					if (f.name.equals(chunkx.name)) {
						addHook(hooks[35].buildObfFn(fields.get(i + 1)));
						break;
					}
				}
				break;
			}
		}
	}
}