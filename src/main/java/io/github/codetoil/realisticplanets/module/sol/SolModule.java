package io.github.codetoil.realisticplanets.module.sol;

import io.github.codetoil.realisticplanets.RPEventHandler;
import io.github.codetoil.realisticplanets.RPHelper;
import io.github.codetoil.realisticplanets.RealisticPlanets;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;
import java.util.List;

public class SolModule implements IPlanetsModule {
    public static Star starSol;
    public static final MaterialLiquid materialSunPlasma = new MaterialLiquid(MapColor.YELLOW);
    public static Fluid fluidSunPlasma;
    public static BlockFluidSunPlasma blockSunPlasma;
    public static int dimSolId = -5430;
    public static DimensionType dimSol;
    public static Biome biomeSolFlat;
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RealisticPlanets.logger.info("[Sol] preInit");
        MinecraftForge.EVENT_BUS.register(this);
        fluidSunPlasma = new Fluid("sun_plasma",
                new ResourceLocation("realisticplanets:blocks/fluids/sun_plasma_still"),
                new ResourceLocation("realisticplanets:blocks/fluids/sun_plasma_flow"),
                Color.YELLOW)
                .setDensity((int) Math.round(RPHelper.realDensityToForgeDensity(1410.0))) // 1410.0 kg/(m^3)
                .setLuminosity(15)
                .setViscosity((int) Math.round(RPHelper.realViscosityToForgeViscosity(0.000041))) // 4.1 x 10^-5 Pa-s
                .setTemperature(5778) //5778 K
                .setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA)
                .setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
                .setRarity(EnumRarity.RARE);
        biomeSolFlat = new BiomeSunGenBaseGC(new Biome.BiomeProperties("sol")
                .setBaseHeight(1.5F)
                .setHeightVariation(0.4F)
                .setRainfall(0.0F)
                .setRainDisabled()
                .setWaterColor(0xFFFF00)
                .setTemperature((float) RPHelper.realTemperatureToMinecraftTemperature(((double) fluidSunPlasma.getTemperature()) - 273.15)));

    }

    @Override
    public void init(FMLInitializationEvent event) {
        RealisticPlanets.logger.info("[Sol] init");
        RealisticPlanets.solarSystemMain = GalaxyRegistry.getRegisteredSolarSystems().get("sol");
        starSol = (Star) RealisticPlanets.solarSystemMain.getMainStar()
                .atmosphereComponent(EnumAtmosphericGas.HELIUM)
                .atmosphereComponent(EnumAtmosphericGas.HYDROGEN)
                .setTierRequired(3)
        ;
        MinecraftForge.EVENT_BUS.register(RPEventHandler.class);
        starSol.setBiomeInfo(biomeSolFlat);
        //Fluids
        FluidRegistry.registerFluid(fluidSunPlasma);
        blockSunPlasma = (BlockFluidSunPlasma) new BlockFluidSunPlasma(fluidSunPlasma, materialSunPlasma).initBlackList()
                .setUnlocalizedName("sun_plasma_still");
        fluidSunPlasma.setBlock(blockSunPlasma);
        blockSunPlasma.setRegistryName("realisticplanets:sun_plasma");
        FluidRegistry.addBucketForFluid(fluidSunPlasma);
        ForgeRegistries.BLOCKS.register(blockSunPlasma);
        //Biomes
        ForgeRegistries.BIOMES.register(biomeSolFlat);
        //Dimensions
        dimSol = DimensionType.register("star.sol", "_sol", dimSolId, WorldProviderSol.class, false);
        DimensionManager.registerDimension(dimSolId, dimSol);
        GalacticraftRegistry.registerRocketGui(WorldProviderSol.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
        GalacticraftRegistry.registerTeleportType(WorldProviderSol.class, new TeleportTypeSol());
        WorldUtil.dimNames.put(dimSolId, dimSol.getName());
        //Solar System
        starSol.setDimensionInfo(dimSolId, WorldProviderSol.class);

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        RealisticPlanets.logger.info("[Sol] postInit");

    }

    @Override
    public void serverInit(FMLServerStartedEvent event) {
        RealisticPlanets.logger.info("[Sol] serverInit");

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        RealisticPlanets.logger.info("[Sol] serverStarting");

    }

    @Override
    public void getGuiIDs(List<Integer> idList) {
        RealisticPlanets.logger.info("[Sol] getGuiIDs");

    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z) {
        RealisticPlanets.logger.info("[Sol] getGuiElement");
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        RealisticPlanets.logger.info("[Sol] getConfiguration");
        return null;
    }

    @Override
    public void syncConfig() {
        RealisticPlanets.logger.info("[Sol] syncConfig");

    }
}
