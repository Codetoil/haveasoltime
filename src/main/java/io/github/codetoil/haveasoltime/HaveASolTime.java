package io.github.codetoil.haveasoltime;

import io.github.codetoil.haveasoltime.proxy.HSTProxy;
import io.github.codetoil.tothestars.asm.api.LandableStar;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;

@Mod(modid = HaveASolTime.MODID, dependencies = "required-after:galacticraftcore;required-after:galacticraftplanets;required-after:tothestars")
public class HaveASolTime {
    public static final String MODID = "haveasoltime";
    public static final String NAME = "HaveASolTime";
    public static final String VERSION = "0.0.1";

    public static Logger logger;

    @Mod.Instance(HaveASolTime.MODID)
    public static HaveASolTime instance;

    @SidedProxy(clientSide = "io.github.codetoil.haveasoltime.proxy.HSTProxyClient", serverSide = "io.github.codetoil.haveasoltime.proxy.HSTProxy")
    public static HSTProxy proxy;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public static Fluid solPlasma;
    public static Fluid solPlasmaHST;
    public static final MaterialLiquid materialSolPlasma = new MaterialLiquid(MapColor.YELLOW);
    public static LandableStar starSol;

    public static int dimSolId = -5430;
    public static DimensionType dimSol;
    public static Biome biomeSolFlat;

    @Mod.EventHandler
    public void contruction(FMLConstructionEvent event)
    {
        proxy.construction(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        HaveASolTime.starSol = new LandableStar("sol").setParentSolarSystem(GalacticraftCore.solarSystemSol);

        MinecraftForge.EVENT_BUS.register(new EventHandlerSol());

        if (!FluidRegistry.isFluidRegistered("sol_plasma"))
        {
            solPlasmaHST = new Fluid("sol_plasma",
                    new ResourceLocation("haveasoltime:blocks/fluids/sol_plasma_still"),
                    new ResourceLocation("haveasoltime:blocks/fluids/sol_plasma_flow"),
                    Color.YELLOW)
                    .setDensity((int) Math.round(HSTHelper.realDensityToForgeDensity(1410.0))) // 1410.0 kg/(m^3)
                    .setLuminosity(15)
                    .setViscosity((int) Math.round(HSTHelper.realViscosityToForgeViscosity(0.000041))) // 4.1 x 10^-5 Pa-s
                    .setTemperature(5778) //5778 K
                    .setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA)
                    .setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
                    .setRarity(EnumRarity.RARE);
            FluidRegistry.registerFluid(solPlasmaHST);
        } else
        {
            HaveASolTime.logger.info("HaveASolTime Sol Plasma Fluid is not default, issues may occur.");
        }

        solPlasma = FluidRegistry.getFluid("sol_plasma");

        if (solPlasma.getBlock() == null)
        {
            SolBlocks.blockSolPlasma = new BlockFluidSunPlasma(solPlasma, materialSolPlasma).initBlackList()
                    .setTranslationKey("sol_plasma");
            ((BlockFluidSunPlasma) SolBlocks.blockSolPlasma).setQuantaPerBlock(3);
            SolBlocks.registerBlock(SolBlocks.blockSolPlasma, ItemBlockDesc.class);
            solPlasma.setBlock(SolBlocks.blockSolPlasma);
        } else {
            HaveASolTime.logger.info("HaveASolTime Sol Plasma Block is not default, issues may occur.");
            SolBlocks.blockSolPlasma = solPlasma.getBlock();
        }

        if (SolBlocks.blockSolPlasma != null)
        {
            FluidRegistry.addBucketForFluid(solPlasma); // Create a Universal
            // Bucket AS WELL AS our
            // type, this is needed to
            // pull fluids out of other
            // mods tanks
            SolItems.bucketSunPlasma = new ItemBucketGC(SolBlocks.blockSolPlasma, solPlasma).setTranslationKey("bucket_sol_plasma");
            SolItems.registerItem(SolItems.bucketSunPlasma);
            EventHandlerGC.bucketList.put(SolBlocks.blockSolPlasma, SolItems.bucketSunPlasma);
        }

        SolBlocks.initBlocks();
        SolItems.initItems();

        biomeSolFlat = new BiomeSunGenBaseGC(new Biome.BiomeProperties("sol")
                .setBaseHeight(1.5F)
                .setHeightVariation(0.4F)
                .setRainfall(0.0F)
                .setRainDisabled()
                .setWaterColor(0xFFFF00)
                .setTemperature((float) HSTHelper.realTemperatureToMinecraftTemperature(((double) solPlasma.getTemperature()) - 273.15)));

        HaveASolTime.starSol.setBiomeInfo(biomeSolFlat);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SolBlocks.oreDictRegistration();

        HaveASolTime.starSol.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
        HaveASolTime.starSol.setDimensionInfo(dimSolId, WorldProviderSol.class).setTierRequired(3);
        HaveASolTime.starSol.atmosphereComponent(EnumAtmosphericGas.HELIUM)
                .atmosphereComponent(EnumAtmosphericGas.HYDROGEN);

        GalaxyRegistry.register(HaveASolTime.starSol);
        GalacticraftRegistry.registerRocketGui(WorldProviderSol.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
        GalacticraftRegistry.registerTeleportType(WorldProviderSol.class, new TeleportTypeSol());

        //Biomes
        ForgeRegistries.BIOMES.register(biomeSolFlat);

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        HaveASolTime.dimSol = WorldUtil.getDimensionTypeById(dimSolId);
        GalacticraftCore.solarSystemSol.setMainStar(HaveASolTime.starSol);

        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverInit(FMLServerStartedEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
