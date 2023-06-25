package am2.blocks;

import am2.AMCore;
import am2.buffs.BuffEffectFury;
import am2.buffs.BuffList;
import am2.particles.AMParticle;
import am2.particles.ParticleExpandingCollapsingRingAtPoint;
import am2.particles.ParticleFadeOut;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class BlockSanguineAmaryllis extends AMFlower{

	protected BlockSanguineAmaryllis(){
		super();
	}

	@Override
	public int tickRate(World par1World){
		return 100;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		if (!world.isRemote) {
			List<EntityPlayer> playersInRange = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - 4, y - 4, z - 4, x + 4, y + 4, z + 4));
			for (EntityPlayer p : playersInRange) {
				if (!p.isPotionActive(BuffList.fury)) p.addPotionEffect(new BuffEffectFury(220, 0));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random par5Random){

		if (par5Random.nextInt(10) != 0) return;

		int increment = AMCore.config.getGFXLevel() * 15;

		if (increment <= 0) return;

		for (int i = 0; i < 360; i += increment){
			int angle = i;
			double posX = x + 0.5 + Math.cos(angle) * 3;
			double posZ = z + 0.5 + Math.sin(angle) * 3;
			double posY = y + 0.6 + par5Random.nextFloat() * 0.2f;

			AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(world, "sparkle2", x + 0.5, posY, z + 0.5);
			if (effect != null){
				effect.setRGBColorF(1, 0.2f, 0);
				effect.setIgnoreMaxAge(true);
				effect.AddParticleController(new ParticleExpandingCollapsingRingAtPoint(effect, posX, posY, posZ, 0.3, 3, 0.2, 1, false).setExpanding());
				//effect.AddParticleController(new ParticleOrbitPoint(effect, x+0.5, y, z+0.5, 1, false).SetTargetDistance(0.35f + par5Random.nextFloat() * 0.2f).SetOrbitSpeed(0.5f).setIgnoreYCoordinate(true));
				//effect.AddParticleController(new ParticleFloatUpward(effect, 0, 0.035f, 1, false));
				effect.AddParticleController(new ParticleFadeOut(effect, 2, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
				effect.setParticleScale(0.05f);
			}
		}
	}
}
