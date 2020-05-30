package io.github.codetoil.realisticplanets.proxy;

import io.github.codetoil.realisticplanets.RealisticPlanets;
import io.github.codetoil.realisticplanets.module.sol.SolModuleClient;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static io.github.codetoil.realisticplanets.RealisticPlanets.clientPlanetModules;

public class RPProxyClient extends RPProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(this);
        RealisticPlanets.logger.info("[Client] preInit");
        clientPlanetModules.add(new SolModuleClient());
        GalacticraftPlanets.clientModules.addAll(clientPlanetModules);
        clientPlanetModules.forEach(e -> e.preInit(event));
    }
}
