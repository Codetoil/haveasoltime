package io.github.codetoil.realisticplanets;

import com.google.common.collect.Sets;
import io.github.codetoil.realisticplanets.proxy.RPProxy;
import io.github.codetoil.realisticplanets.module.sol.SolModule;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.Set;

@Mod(modid = RealisticPlanets.MODID, name = RealisticPlanets.NAME, version = RealisticPlanets.VERSION, useMetadata = true, dependencies = "required-after:galacticraftcore;")
public class RealisticPlanets {
    public static final String MODID = "realisticplanets";
    public static final String NAME = "RealisticPlanets";
    public static final String VERSION = "0.0.0.2";

    public static Logger logger;
    public static SolarSystem solarSystemMain;

    @SidedProxy(clientSide = "io.github.codetoil.realisticplanets.proxy.RPProxyClient", serverSide = "io.github.codetoil.realisticplanets.proxy.RPProxy")
    public static RPProxy proxy;

    public static final Set<IPlanetsModule> planetsModules = Sets.newHashSet();
    public static final Set<IPlanetsModuleClient> clientPlanetModules = Sets.newHashSet();

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        planetsModules.add(new SolModule());
        GalacticraftPlanets.commonModules.addAll(planetsModules);
        planetsModules.forEach(e -> e.preInit(event));
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.serverStarting(event);
    }

    @EventHandler
    public void serverInit(FMLServerStartedEvent event)
    {
        proxy.serverInit(event);
    }

    @SubscribeEvent
    public void registerVariants(ModelRegistryEvent event)
    {
        proxy.registerVariants();
    }

}
