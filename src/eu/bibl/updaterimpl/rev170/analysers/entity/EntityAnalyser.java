package eu.bibl.updaterimpl.rev170.analysers.entity;
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
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getEntityRidingOnThis", "L" + MinecraftAnalyser.INTERFACES + "entity/IEntity;"),
				new FieldMappingData("getEntityRiden", "L" + MinecraftAnalyser.INTERFACES + "entity/IEntity;"),
				new FieldMappingData("getX", "D", "D"),
				new FieldMappingData("getY", "D", "D"),
				new FieldMappingData("getZ", "D", "D"),
				new FieldMappingData("getRotationPitch", "F", "F"),
				new FieldMappingData("getRotationYaw", "F", "F"),
				new FieldMappingData("isInWeb", "Z", "Z"),
				new FieldMappingData("getMotionX", "D", "D"),
				new FieldMappingData("getMotionY", "D", "D"),
				new FieldMappingData("getMotionZ", "D", "D"),
				new FieldMappingData("getFallDistance", "F", "F"),
				new FieldMappingData("getFireTime", "I", "I"),
				new FieldMappingData("isOnGround", "Z", "Z"),
				new FieldMappingData("getDimension", "I", "I"),
				new FieldMappingData("isInvincible", "Z", "Z"),
				new FieldMappingData("getWidth", "F", "F"),
				new FieldMappingData("getHeight", "F", "F"),
				new FieldMappingData("isDead", "Z", "Z"),
				new FieldMappingData("isColliding", "Z", "Z"),
				new FieldMappingData("isCollidingVertically", "Z", "Z"),
				new FieldMappingData("isCollidingHorizontally", "Z", "Z"),
				new FieldMappingData("getWorld", "L" + MinecraftAnalyser.INTERFACES + "world/IWorld;"),
				new FieldMappingData("getStepHeight", "F", "F"),
				new FieldMappingData("getLifeInTicks", "I", "I"),
				new FieldMappingData("getBoundingBox", "L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new FieldMappingData("isInWater", "Z", "Z"),
				new FieldMappingData("getInFireTicks", "I", "I"),
				new FieldMappingData("getInFireResistanceTicks", "I", "I"),
				new FieldMappingData("getHurtResistanceTicks", "I", "I"),
				new FieldMappingData("isImmuneToFire", "Z", "Z"),
				new FieldMappingData("getDataWatcher", "L" + MinecraftAnalyser.INTERFACES + "entity/IDataWatcher;"),
				new FieldMappingData("isAddedToChunk", "Z", "Z"),
				new FieldMappingData("getChunkCoordX", "I", "I"),
				new FieldMappingData("getChunkCoordY", "I", "I"),
				new FieldMappingData("getChunkCoordZ", "I", "I"),
				new FieldMappingData("isSpawnForced", "Z", "Z") };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("onUpdate", "()V", "()V"),
				new CallbackMappingData("onEntityUpdate", "()V", "()V"),
				new CallbackMappingData("setPos", "(DDD)V", "(DDD)V")
		
		};
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("Entity"))
			return true;
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		InterfaceHook interfaceHook = new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntity");
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
		addFieldHook(fieldHooks[37]);
	}
	
	private void findSetPosMethod() {
		for(MethodNode m : methods(cn, "(DDD)V")) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, FSUB);
			if (is.match() && is.size() == 2) {
				addMethodHook(methodHooks[2].buildObfMn(m));
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
				addMethodHook(methodHooks[0].buildObfMn(m));
				addMethodHook(methodHooks[1].buildObfMn(m1));
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
						addFieldHook(fieldHooks[0].buildObfFin(fin));
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
			addFieldHook(fieldHooks[1].buildObfFn(firstEntityNode));
			addFieldHook(fieldHooks[2].buildObfFn(secondEntityNode));
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
								addFieldHook(fieldHooks[3].buildObfFin(fin1));
							} else if (vCount == 3) {
								addFieldHook(fieldHooks[4].buildObfFin(fin1));
							} else if (vCount == 4) {
								addFieldHook(fieldHooks[5].buildObfFin(fin1));
							} else if (vCount == 5) {
								addFieldHook(fieldHooks[6].buildObfFin(fin1));
							} else if (vCount == 6) {
								addFieldHook(fieldHooks[7].buildObfFin(fin1));
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
								addFieldHook(fieldHooks[8].buildObfFin(fin2));
							}
							addFieldHook(fieldHooks[9].buildObfFin(fin));
						} else if (i == 1) {
							addFieldHook(fieldHooks[10].buildObfFin(fin));
						} else if (i == 2) {
							addFieldHook(fieldHooks[11].buildObfFin(fin));
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
									addFieldHook(fieldHooks[12].buildObfFin(fin));
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
									addFieldHook(fieldHooks[14].buildObfFin(fin));
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
									addFieldHook(fieldHooks[15].buildObfFin(fin));
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
									addFieldHook(fieldHooks[16].buildObfFin(fin));
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
									addHook(new FieldMappingData(classHook, fin.name, "getPortalCoolDown", fin.desc, fin.desc, false));
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
									addFieldHook(fieldHooks[13].buildObfFin(fin));
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
						addFieldHook(fieldHooks[17].buildObfFin(firstFin));
						addFieldHook(fieldHooks[18].buildObfFin(secondFin));
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
										addFieldHook(fieldHooks[19].buildObfFin(deadFin));
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
							addFieldHook(fieldHooks[20].buildObfFin(isColldingFin));
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
								addFieldHook(fieldHooks[21].buildObfFin(isColldingVerticallyFin));
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
									addFieldHook(fieldHooks[22].buildObfFin(isColldingHorizontallyFin));
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
		addFieldHook(fieldHooks[23].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("World").getObfuscatedName() + ";").get(0)));
	}
	
	private void findStepHeightField() {
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, STEP_HEIGHT_REGEX);
			if (is.match()) {
				addFieldHook(fieldHooks[24].buildObfFin((FieldInsnNode) is.getMatches().get(0)[1]));
				break;
			}
		}
	}
	
	private void findLifeInTicksField() {
		ArrayList<FieldNode> fields = fields(cn);
		for(int i = 0; i < fields.size(); i++) {
			FieldNode f = fields.get(i);
			if (f.desc.equals("Ljava/util/Random;")) {
				addFieldHook(fieldHooks[25].buildObfFn(fields.get(i + 1)));
				break;
			}
		}
	}
	
	private void findBoundingBoxField() {
		addFieldHook(fieldHooks[26].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("AxisAlignedBB").getObfuscatedName() + ";").get(0)));
	}
	
	private void findInWaterFireTicksFireHurtResistance() {
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, IN_WATER_REGEX);
			if (is.match()) {
				FieldInsnNode inWater = (FieldInsnNode) is.getMatches().get(0)[5];
				FieldInsnNode fireTicks = (FieldInsnNode) is.getMatches().get(0)[8];
				addFieldHook(fieldHooks[27].buildObfFin(inWater));
				addFieldHook(fieldHooks[28].buildObfFin(fireTicks));
				
				ArrayList<FieldNode> fields = fields(cn, "I");
				for(int i = 0; i < fields.size(); i++) {
					FieldNode f = fields.get(i);
					if (f.name.equals(fireTicks.name)) {
						addFieldHook(fieldHooks[29].buildObfFn(fields.get(i - 1)));
						addFieldHook(fieldHooks[30].buildObfFn(fields.get(i + 1)));
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
				addFieldHook(fieldHooks[31].buildObfFin(fin));
				break;
			}
		}
	}
	
	private void findDataWatcherHook() {
		addFieldHook(fieldHooks[32].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("DataWatcher").getObfuscatedName() + ";").get(0)));
	}
	
	private void findChunkPositions() {
		ClassNode worldClient = analysisMap.requestNode(hookMap.getClassByRefactoredName("WorldClient").getObfuscatedName());
		for(MethodNode m : methods(worldClient)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, CHUNK_POS_REGEX);
			if (is.match()) {
				AbstractInsnNode[] ains = is.getMatches().get(1);
				FieldInsnNode chunkx = (FieldInsnNode) ains[1];
				FieldInsnNode chunkz = (FieldInsnNode) ains[4];
				FieldInsnNode added = (FieldInsnNode) ains[7];
				addFieldHook(fieldHooks[33].buildObfFin(added));
				addFieldHook(fieldHooks[34].buildObfFin(chunkx));
				addFieldHook(fieldHooks[36].buildObfFin(chunkz));
				
				ArrayList<FieldNode> fields = fields(cn, "I");
				for(int i = 0; i < fields.size(); i++) {
					FieldNode f = fields.get(i);
					if (f.name.equals(chunkx.name)) {
						addFieldHook(fieldHooks[35].buildObfFn(fields.get(i + 1)));
						break;
					}
				}
				break;
			}
		}
	}
}
