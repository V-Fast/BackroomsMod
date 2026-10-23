package org.vfast.backrooms.client.render.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import org.vfast.backrooms.entity.stilllife.StillLifeAnatomy;
import org.vfast.backrooms.entity.stilllife.StillLifeStance;

/**
 * The player mesh with the stance and body imperfections of a Still Life, plus extra copies of body parts that are
 * shown when the anatomy duplicates them.
 */
public class StillLifeModel extends HumanoidModel<StillLifeRenderState> {
    private static final String EXTRA_HEAD = "extra_head";
    private static final String EXTRA_LEFT_ARM = "extra_left_arm";
    private static final String EXTRA_RIGHT_ARM = "extra_right_arm";
    private static final String EXTRA_LEFT_LEG = "extra_left_leg";
    private static final String EXTRA_RIGHT_LEG = "extra_right_leg";

    private final ModelPart extraHead;
    private final ModelPart extraLeftArm;
    private final ModelPart extraRightArm;
    private final ModelPart extraLeftLeg;
    private final ModelPart extraRightLeg;

    public StillLifeModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        this.extraHead = root.getChild(EXTRA_HEAD);
        this.extraLeftArm = root.getChild(EXTRA_LEFT_ARM);
        this.extraRightArm = root.getChild(EXTRA_RIGHT_ARM);
        this.extraLeftLeg = root.getChild(EXTRA_LEFT_LEG);
        this.extraRightLeg = root.getChild(EXTRA_RIGHT_LEG);
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        CubeDeformation none = CubeDeformation.NONE;
        CubeDeformation overlay = none.extend(0.25F);
        MeshDefinition mesh = PlayerModel.createMesh(none, slim);
        PartDefinition root = mesh.getRoot();

        // same cubes and UVs as the player parts they copy
        root.addOrReplaceChild(EXTRA_HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, none), PartPose.ZERO)
                .addOrReplaceChild("extra_hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, none.extend(0.5F)), PartPose.ZERO);

        float armWidth = slim ? 3.0F : 4.0F;
        root.addOrReplaceChild(EXTRA_LEFT_ARM, CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, armWidth, 12.0F, 4.0F, none), PartPose.ZERO)
                .addOrReplaceChild("extra_left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, armWidth, 12.0F, 4.0F, overlay), PartPose.ZERO);
        root.addOrReplaceChild(EXTRA_RIGHT_ARM, CubeListBuilder.create().texOffs(40, 16).addBox(1.0F - armWidth, -2.0F, -2.0F, armWidth, 12.0F, 4.0F, none), PartPose.ZERO)
                .addOrReplaceChild("extra_right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(1.0F - armWidth, -2.0F, -2.0F, armWidth, 12.0F, 4.0F, overlay), PartPose.ZERO);

        root.addOrReplaceChild(EXTRA_LEFT_LEG, CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, none), PartPose.ZERO)
                .addOrReplaceChild("extra_left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, overlay), PartPose.ZERO);
        root.addOrReplaceChild(EXTRA_RIGHT_LEG, CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, none), PartPose.ZERO)
                .addOrReplaceChild("extra_right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, overlay), PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(StillLifeRenderState state) {
        super.setupAnim(state);
        StillLifeAnatomy anatomy = state.anatomy;
        this.applyStance(state.stance, anatomy, state.isPassenger);
        this.applyAnatomy(anatomy);
        this.applyDuplicates(anatomy);
    }

    private void applyStance(StillLifeStance stance, StillLifeAnatomy anatomy, boolean riding) {
        switch (stance) {
            case SLOUCHED -> {
                this.body.xRot += 0.35F;
                this.head.y += 1.5F;
                this.head.z -= 2.5F;
                this.head.xRot += 0.3F;
                this.rightArm.z -= 1.5F;
                this.leftArm.z -= 1.5F;
                this.rightArm.xRot += 0.35F;
                this.leftArm.xRot += 0.35F;
            }
            case ARMS_FORWARD -> {
                this.rightArm.xRot = -Mth.HALF_PI + this.rightArm.xRot * 0.1F;
                this.leftArm.xRot = -Mth.HALF_PI + this.leftArm.xRot * 0.1F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
            }
            case ARMS_RAISED -> {
                this.rightArm.xRot = -Mth.PI;
                this.leftArm.xRot = -Mth.PI;
                this.rightArm.zRot = -0.1F;
                this.leftArm.zRot = 0.1F;
            }
            case T_POSE -> {
                this.rightArm.xRot = 0.0F;
                this.leftArm.xRot = 0.0F;
                this.rightArm.zRot = Mth.HALF_PI;
                this.leftArm.zRot = -Mth.HALF_PI;
            }
            case HEAD_DOWN -> this.head.xRot = 1.1F;
            case HEAD_BACK -> this.head.xRot = -1.2F;
            case BROKEN_NECK -> this.head.zRot = 1.4F * anatomy.duplicateSide();
            case STIFF -> {
                // limbs never swing, it glides around
                this.rightArm.xRot = 0.0F;
                this.leftArm.xRot = 0.0F;
                this.rightArm.zRot = 0.05F;
                this.leftArm.zRot = -0.05F;
                if (!riding) {
                    this.rightLeg.xRot = 0.0F;
                    this.leftLeg.xRot = 0.0F;
                }
            }
            case ARMS_BEHIND -> {
                this.rightArm.xRot = 0.55F;
                this.leftArm.xRot = 0.55F;
                this.rightArm.zRot = -0.25F;
                this.leftArm.zRot = 0.25F;
            }
            case DISLOCATED -> {
                this.rightArm.y += 2.5F;
                this.rightArm.xRot *= 0.2F;
                this.rightArm.zRot = -0.15F;
            }
            default -> {
            }
        }
    }

    private void applyAnatomy(StillLifeAnatomy anatomy) {
        this.head.xScale = this.head.yScale = this.head.zScale = anatomy.headScale();
        this.head.y -= anatomy.neckStretch();
        this.head.zRot += anatomy.headTilt();
        this.body.yRot += anatomy.torsoTwist();
        this.leftArm.yScale = anatomy.leftArmLength();
        this.rightArm.yScale = anatomy.rightArmLength();
        this.leftLeg.yScale = anatomy.leftLegLength();
        this.rightLeg.yScale = anatomy.rightLegLength();
    }

    private void applyDuplicates(StillLifeAnatomy anatomy) {
        float side = anatomy.duplicateSide();

        // a smaller head growing out of the shoulder
        this.extraHead.visible = anatomy.hasDuplicate(StillLifeAnatomy.DUPLICATE_HEAD);
        copyPose(this.extraHead, this.head);
        this.extraHead.x += side * 5.0F;
        this.extraHead.y += 1.5F;
        this.extraHead.zRot += side * 0.45F;
        this.extraHead.xScale = this.extraHead.yScale = this.extraHead.zScale = this.head.xScale * 0.8F;

        // arms sprouting a bit lower, swinging out of phase with the real ones
        this.extraLeftArm.visible = anatomy.hasDuplicate(StillLifeAnatomy.DUPLICATE_LEFT_ARM);
        copyPose(this.extraLeftArm, this.leftArm);
        this.extraLeftArm.x -= 0.5F;
        this.extraLeftArm.y += 3.5F;
        this.extraLeftArm.z += 1.0F;
        this.extraLeftArm.xRot = 0.2F - this.leftArm.xRot * 0.8F;
        this.extraLeftArm.zRot -= 0.35F;

        this.extraRightArm.visible = anatomy.hasDuplicate(StillLifeAnatomy.DUPLICATE_RIGHT_ARM);
        copyPose(this.extraRightArm, this.rightArm);
        this.extraRightArm.x += 0.5F;
        this.extraRightArm.y += 3.5F;
        this.extraRightArm.z += 1.0F;
        this.extraRightArm.xRot = 0.2F - this.rightArm.xRot * 0.8F;
        this.extraRightArm.zRot += 0.35F;

        // legs dangling behind, a little too short to reach the ground
        this.extraLeftLeg.visible = anatomy.hasDuplicate(StillLifeAnatomy.DUPLICATE_LEFT_LEG);
        copyPose(this.extraLeftLeg, this.leftLeg);
        this.extraLeftLeg.x += 1.2F;
        this.extraLeftLeg.z += 2.0F;
        this.extraLeftLeg.xRot = 0.25F - this.leftLeg.xRot * 0.6F;
        this.extraLeftLeg.yScale *= 0.85F;

        this.extraRightLeg.visible = anatomy.hasDuplicate(StillLifeAnatomy.DUPLICATE_RIGHT_LEG);
        copyPose(this.extraRightLeg, this.rightLeg);
        this.extraRightLeg.x -= 1.2F;
        this.extraRightLeg.z += 2.0F;
        this.extraRightLeg.xRot = 0.25F - this.rightLeg.xRot * 0.6F;
        this.extraRightLeg.yScale *= 0.85F;
    }

    private static void copyPose(ModelPart target, ModelPart source) {
        target.loadPose(source.storePose());
        target.xScale = source.xScale;
        target.yScale = source.yScale;
        target.zScale = source.zScale;
    }
}
