package io.github.codetoil.realisticplanets.module.sol;

import io.github.codetoil.realisticplanets.RealisticPlanets;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class SolModuleClient implements IPlanetsModuleClient {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RealisticPlanets.logger.info("[Sol Client] preInit");
        MinecraftForge.EVENT_BUS.register(this);

    }

    @Override
    public void registerVariants() {
        RealisticPlanets.logger.info("[Sol Client] registerVariants");

    }

    @Override
    public void init(FMLInitializationEvent event) {
        RealisticPlanets.logger.info("[Sol Client] init");
        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
        ModelLoader.setCustomStateMapper(SolModule.blockSunPlasma, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return new ModelResourceLocation( "realisticplanets:sun_plasma", "fluid");
            }
        });
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        RealisticPlanets.logger.info("[Sol Client] postInit");

    }

    @Override
    public void getGuiIDs(List<Integer> idList) {
        RealisticPlanets.logger.info("[Sol Client] getGuiIDs");

    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z) {
        RealisticPlanets.logger.info("[Sol Client] getGuiElement");
        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData) {
        RealisticPlanets.logger.info("[Sol Client] spawnParticle");
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            double dX = mc.getRenderViewEntity().posX - position.x;
            double dY = mc.getRenderViewEntity().posY - position.y;
            double dZ = mc.getRenderViewEntity().posZ - position.z;
            Particle particle = null;
            double viewDistance = 64.0D;

            /*if (particleID.equals("acidVapor"))
            {
                particle = new ParticleAcidVapor(mc.world, position.x, position.y, position.z, motion.x, motion.y, motion.z, 2.5F);
            }

            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
            {
                if (particleID.equals("acidExhaust"))
                {
                    particle = new ParticleAcidExhaust(mc.world, position.x, position.y, position.z, motion.x, motion.y, motion.z, 0.5F);
                }
            }*/

            if (particle != null)
            {
                mc.effectRenderer.addEffect(particle);
            }
        }
    }

    public static class TickHandlerClient
    {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event)
        {
            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final WorldClient world = minecraft.world;

            if (world != null)
            {
                if (world.provider instanceof WorldProviderSol)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        world.provider.setSkyRenderer(new SkyProviderSol((IGalacticraftWorldProvider) world.provider));
                    }

                    if (world.provider.getCloudRenderer() == null)
                    {
                        world.provider.setCloudRenderer(new CloudRenderer());
                    }
                }
            }
        }
    }
}
