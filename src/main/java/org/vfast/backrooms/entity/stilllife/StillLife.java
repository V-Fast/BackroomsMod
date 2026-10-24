package org.vfast.backrooms.entity.stilllife;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.entity.stilllife.goal.ErraticMovementGoal;
import org.vfast.backrooms.entity.stilllife.goal.FleeStillLivesGoal;
import org.vfast.backrooms.entity.stilllife.goal.FollowPlayerBrieflyGoal;
import org.vfast.backrooms.entity.stilllife.goal.HideFromPlayersGoal;
import org.vfast.backrooms.entity.stilllife.goal.StillLifeAttackGoal;
import org.vfast.backrooms.entity.stilllife.goal.StrikeWhenCloseGoal;

import java.util.List;

/**
 * A failed copy of a person made by the Backrooms. Every trait (looks, pose, temperament, movement, fears) is rolled
 * independently when it spawns, so two Still Lives rarely behave the same way.
 */
public class StillLife extends PathfinderMob {
    private static final EntityDataAccessor<ResolvableProfile> DATA_PROFILE = SynchedEntityData.defineId(StillLife.class, EntityDataSerializers.RESOLVABLE_PROFILE);
    private static final EntityDataAccessor<Integer> DATA_SKIN = SynchedEntityData.defineId(StillLife.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_STANCE = SynchedEntityData.defineId(StillLife.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ANATOMY_SEED = SynchedEntityData.defineId(StillLife.class, EntityDataSerializers.INT);

    /** Amount of default skins, in the order of {@code DefaultPlayerSkin}. */
    public static final int DEFAULT_SKIN_COUNT = 18;
    public static final double FEAR_DISTANCE = 30.0;
    private static final double BASE_MOVEMENT_SPEED = 0.3;
    private static final double OBSERVATION_RANGE = 48.0;
    private static final float PROFILE_STEAL_CHANCE = 0.35F;
    private static final EntityDimensions CROUCHING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.5F).withEyeHeight(1.27F);

    private StillLifeTemperament temperament = StillLifeTemperament.PASSIVE;
    private StillLifeMovement movement = StillLifeMovement.WANDERER;
    private boolean fearful;
    private boolean shy;
    private boolean watcher;
    private boolean traitsRolled;
    private boolean petrified;

    private @Nullable StillLifeAnatomy anatomy;
    private int anatomySeed;

    public StillLife(EntityType<? extends StillLife> type, Level level) {
        super(type, level);
        this.lookControl = new StillLifeLookControl();
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGE_CAUTIOUS, 8.0F);
        this.getNavigation().setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.getNavigation().setMaxVisitedNodesMultiplier(4.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1.0);
    }

    public static boolean checkStillLifeSpawnRules(EntityType<StillLife> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        if (!Mob.checkMobSpawnRules(type, level, reason, pos, random)) return false;
        if (reason != EntitySpawnReason.NATURAL) return true;
        // keep them rare: a few per area, and most spawn attempts fail
        return random.nextInt(4) == 0 && level.getEntitiesOfClass(StillLife.class, new AABB(pos).inflate(64.0)).size() < 2;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PROFILE, ResolvableProfile.Static.EMPTY);
        builder.define(DATA_SKIN, 0);
        builder.define(DATA_STANCE, StillLifeStance.UPRIGHT.ordinal());
        builder.define(DATA_ANATOMY_SEED, 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (!this.traitsRolled) {
            RandomSource random = level.getRandom();
            Player original = this.copySomeone(level.getLevel(), random);
            this.rollTraits(random, original);
        }
        return data;
    }

    private @Nullable Player copySomeone(ServerLevel level, RandomSource random) {
        List<ServerPlayer> players = level.getPlayers(player -> player.isAlive() && !player.isSpectator());
        if (players.isEmpty()) return null;
        ServerPlayer original = players.get(random.nextInt(players.size()));
        this.setProfile(ResolvableProfile.createResolved(original.getGameProfile()));
        return original;
    }

    private void rollTraits(RandomSource random, @Nullable Player original) {
        // a copy of someone who was struggling tends to come out violent
        boolean troubled = original != null && (original.getHealth() < original.getMaxHealth() * 0.5F || original.getFoodData().getFoodLevel() < 6);
        this.temperament = StillLifeTemperament.roll(random, troubled);
        this.movement = StillLifeMovement.roll(random);
        this.fearful = random.nextFloat() < 0.25F;
        this.shy = random.nextFloat() < 0.25F;
        this.watcher = random.nextFloat() < 0.5F;
        this.setStance(StillLifeStance.roll(random));
        this.entityData.set(DATA_SKIN, random.nextInt(DEFAULT_SKIN_COUNT));
        this.entityData.set(DATA_ANATOMY_SEED, random.nextInt());
        this.setLeftHanded(random.nextBoolean());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_MOVEMENT_SPEED * Mth.nextDouble(random, 0.75, 1.3));
        this.getAttribute(Attributes.SCALE).setBaseValue(Mth.nextDouble(random, 0.9, 1.1));
        this.traitsRolled = true;
        this.rebuildGoals();
    }

    @Override
    protected void registerGoals() {
        // goals depend on the traits, which are only known after spawning or loading, see rebuildGoals
    }

    private void rebuildGoals() {
        this.goalSelector.removeAllGoals(_ -> true);
        this.targetSelector.removeAllGoals(_ -> true);

        boolean rooted = this.isRooted();
        this.goalSelector.addGoal(0, new FloatGoal(this));

        if (this.temperament.canAttack()) {
            this.goalSelector.addGoal(3, rooted ? new StrikeWhenCloseGoal(this) : new StillLifeAttackGoal(this, 1.3));
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
            if (this.temperament == StillLifeTemperament.HOSTILE) {
                this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
            }
        }

        if (!rooted) {
            if (this.fearful) this.goalSelector.addGoal(1, new FleeStillLivesGoal(this, FEAR_DISTANCE, 1.45));
            if (this.shy) this.goalSelector.addGoal(2, new HideFromPlayersGoal(this, 1.35));
            this.goalSelector.addGoal(4, new OpenDoorGoal(this, this.random.nextBoolean()));

            switch (this.movement) {
                case ERRATIC -> this.goalSelector.addGoal(5, new ErraticMovementGoal(this));
                case FOLLOWER -> {
                    this.goalSelector.addGoal(5, new FollowPlayerBrieflyGoal(this, 1.0, 40));
                    this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
                }
                case STATUE -> {
                    this.goalSelector.addGoal(5, new FollowPlayerBrieflyGoal(this, 1.1, 80));
                    this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
                }
                default -> {
                    this.goalSelector.addGoal(5, new FollowPlayerBrieflyGoal(this, 0.9, 600));
                    this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
                }
            }
        }

        if (this.watcher) {
            this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24.0F, 1.0F));
        } else if (!rooted) {
            this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        }
    }

    @Override
    public void tick() {
        // entities created without finalizeSpawn or save data (e.g. placed by structures) still need traits
        if (!this.level().isClientSide() && !this.traitsRolled) {
            this.rollTraits(this.random, null);
        }
        super.tick();
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        this.petrified = this.movement == StillLifeMovement.STATUE && this.findObserver(OBSERVATION_RANGE) != null;
        if (this.isRooted() || this.petrified) {
            this.getNavigation().stop();
            this.getMoveControl().setWait();
            this.setJumping(false);
            this.setZza(0.0F);
            this.setXxa(0.0F);
        }
        super.customServerAiStep(level);
    }

    /** Still Lives with the {@link StillLifeMovement#STILL} movement never move nor turn. */
    public boolean isRooted() {
        return this.movement == StillLifeMovement.STILL;
    }

    /** Whether a {@link StillLifeMovement#STATUE} Still Life is currently frozen by a player looking at it. */
    public boolean isPetrified() {
        return this.petrified;
    }

    public boolean isObservedBy(Player player) {
        if (player.isSpectator() || !player.isAlive() || this.distanceToSqr(player) > OBSERVATION_RANGE * OBSERVATION_RANGE) return false;
        return this.isLookingAtMe(player, 0.4, false, false, this.getEyeY(), this.getY() + this.getBbHeight() * 0.5);
    }

    public @Nullable Player findObserver(double range) {
        Player closest = null;
        double closestDistance = range * range;
        for (Player player : this.level().players()) {
            double distance = this.distanceToSqr(player);
            if (distance < closestDistance && this.isObservedBy(player)) {
                closest = player;
                closestDistance = distance;
            }
        }
        return closest;
    }

    /** Whether nothing of this Still Life could be seen by {@code player} if it stood at {@code feet}. */
    public boolean isHiddenFrom(Vec3 feet, Player player) {
        Vec3 eye = player.getEyePosition();
        return this.isSightBlocked(eye, feet.add(0.0, 0.3, 0.0), player)
                && this.isSightBlocked(eye, feet.add(0.0, this.getBbHeight() * 0.5, 0.0), player)
                && this.isSightBlocked(eye, feet.add(0.0, this.getBbHeight(), 0.0), player);
    }

    private boolean isSightBlocked(Vec3 from, Vec3 to, Player player) {
        return this.level().clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player)).getType() != HitResult.Type.MISS;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt) {
            this.spawnAtLocation(level, Items.DRIED_KELP);
        }
        return hurt;
    }

    @Override
    public void knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect) {}

    @Override
    public boolean isPushable() {
        return !this.isRooted() && super.isPushable();
    }

    @Override
    public void push(Entity entity) {
        if (!this.isRooted()) super.push(entity);
    }

    @Override
    public int getMaxFallDistance() {
        return 2;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
                if (!StillLife.this.isRooted() && !StillLife.this.isPetrified()) super.clientTick();
            }
        };
    }

    public StillLifeStance getStance() {
        return StillLifeStance.byId(this.entityData.get(DATA_STANCE));
    }

    private void setStance(StillLifeStance stance) {
        this.entityData.set(DATA_STANCE, stance.ordinal());
        super.setPose(stance.pose());
    }

    @Override
    public void setPose(Pose pose) {
        if (pose == Pose.DYING || pose == this.getStance().pose()) super.setPose(pose);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return pose == Pose.CROUCHING ? CROUCHING_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public StillLifeAnatomy getAnatomy() {
        int seed = this.entityData.get(DATA_ANATOMY_SEED);
        if (this.anatomy == null || this.anatomySeed != seed) {
            this.anatomy = StillLifeAnatomy.fromSeed(seed);
            this.anatomySeed = seed;
        }
        return this.anatomy;
    }

    public int getAnatomySeed() {
        return this.entityData.get(DATA_ANATOMY_SEED);
    }

    public int getDefaultSkin() {
        return Math.floorMod(this.entityData.get(DATA_SKIN), DEFAULT_SKIN_COUNT);
    }

    public ResolvableProfile getProfile() {
        return this.entityData.get(DATA_PROFILE);
    }

    public void setProfile(ResolvableProfile profile) {
        this.entityData.set(DATA_PROFILE, profile);
    }

    public boolean hasCustomProfile() {
        return !this.getProfile().equals(ResolvableProfile.Static.EMPTY);
    }

    @Override
    protected Component getTypeName() {
        return this.getProfile().name().<Component>map(Component::literal).orElseGet(super::getTypeName);
    }

    public StillLifeTemperament getTemperament() {
        return this.temperament;
    }

    public StillLifeMovement getMovement() {
        return this.movement;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.hasCustomProfile()) {
            output.store("profile", ResolvableProfile.CODEC, this.getProfile());
        }
        output.putInt("skin", this.getDefaultSkin());
        output.putInt("anatomy", this.getAnatomySeed());
        output.store("stance", StillLifeStance.CODEC, this.getStance());
        output.store("temperament", StillLifeTemperament.CODEC, this.temperament);
        output.store("movement", StillLifeMovement.CODEC, this.movement);
        output.putBoolean("fearful", this.fearful);
        output.putBoolean("shy", this.shy);
        output.putBoolean("watcher", this.watcher);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("profile", ResolvableProfile.CODEC).ifPresent(this::setProfile);
        // anything missing (e.g. a partial /summon) is rolled, so a Still Life is never left without a trait
        this.entityData.set(DATA_SKIN, input.getInt("skin").orElseGet(() -> this.random.nextInt(DEFAULT_SKIN_COUNT)));
        this.entityData.set(DATA_ANATOMY_SEED, input.getInt("anatomy").orElseGet(this.random::nextInt));
        this.setStance(input.read("stance", StillLifeStance.CODEC).orElseGet(() -> StillLifeStance.roll(this.random)));
        this.temperament = input.read("temperament", StillLifeTemperament.CODEC).orElseGet(() -> StillLifeTemperament.roll(this.random, false));
        this.movement = input.read("movement", StillLifeMovement.CODEC).orElseGet(() -> StillLifeMovement.roll(this.random));
        this.fearful = input.getBooleanOr("fearful", this.random.nextFloat() < 0.25F);
        this.shy = input.getBooleanOr("shy", this.random.nextFloat() < 0.25F);
        this.watcher = input.getBooleanOr("watcher", this.random.nextBoolean());
        this.traitsRolled = true;
        this.rebuildGoals();
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        return type == DataComponents.PROFILE ? castComponentValue(type, this.getProfile()) : super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponents.PROFILE);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.PROFILE) {
            this.setProfile(castComponentValue(DataComponents.PROFILE, value));
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    /** Freezes the head of rooted and petrified Still Lives. */
    private class StillLifeLookControl extends LookControl {
        StillLifeLookControl() {
            super(StillLife.this);
        }

        @Override
        public void tick() {
            if (StillLife.this.isPetrified()) return;
            if (StillLife.this.isRooted() && !StillLife.this.watcher) return;
            super.tick();
        }
    }
}
