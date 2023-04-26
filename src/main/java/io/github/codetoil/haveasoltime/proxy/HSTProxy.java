package io.github.codetoil.haveasoltime.proxy;

import io.github.codetoil.haveasoltime.HaveASolTime;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;

public class HSTProxy {
    public void construction(FMLConstructionEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void preInit(FMLPreInitializationEvent event)
    {

    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData) {

    }
}
