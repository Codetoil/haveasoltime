package io.github.codetoil.realisticplanets.proxy;

import io.github.codetoil.realisticplanets.RealisticPlanets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class RPProxy implements IGuiHandler {

    public void preInit(FMLPreInitializationEvent event)
    {
        RealisticPlanets.logger.info("preInit");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void registerVariants()
    {
        RealisticPlanets.logger.info("registerVariants");

    }

    public void init(FMLInitializationEvent event)
    {
        RealisticPlanets.logger.info("init");

    }

    public void postInit(FMLPostInitializationEvent event)
    {
        RealisticPlanets.logger.info("postInit");

    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        RealisticPlanets.logger.info("serverStarting");

    }

    public void serverInit(FMLServerStartedEvent event)
    {
        RealisticPlanets.logger.info("serverInit");

    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    public void postRegisterItem(Item item)
    {
        RealisticPlanets.logger.info("postRegisterItem");
    }
}
