package org.aaron1011.unsignedskins;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

public class UnsignedSkinsMod extends DummyModContainer {

    public static final String modId = "unsignedskins";
    public static final String modName = "Unsigned Skins";
    public static final String version = "1.0.0";

    private static final String TEXTURE = "eyJ0aW1lc3RhbXAiOjE0MjUyMTg2NjIyNzQsInByb2ZpbGVJZCI6ImE5ZTc2Mjc3YzQ1MzRmZjA5OGEwOTQ2MWZlODFlNDU4IiwicHJvZmlsZU5hbWUiOiJBYXJvbjEwMTEiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYxOTJkZjYxZjQ5Mjk1MGQwNTQwY2Y4ZjQ2MzQ0MTJlZmUzYWRhZTYzMmMwNjY3MTg5NzQ4Nzg1MjY4NGQ2IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=";

    private static final GameProfile profile;

    static {
        profile = new GameProfile(UUID.randomUUID(), "Bob");
        profile.getProperties().put("textures", new Property("textures", TEXTURE, null));
    }

    public UnsignedSkinsMod() {
        super(new ModMetadata());
        ModMetadata md = super.getMetadata();
        md.modId = modId;
        md.name = modName;
        md.version = version;
        md.authorList = ImmutableList.of("Aaron1011");

        System.out.println("Constructor called!");

    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        System.out.println("Bus register");
        return true;
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        System.out.println("Init");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Subscribe
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Joined");
        event.player.worldObj.setBlockState(event.player.playerLocation, Blocks.skull.getDefaultState());

        TileEntitySkull skull = (TileEntitySkull) event.player.worldObj.getTileEntity(event.player.playerLocation);
        skull.setType(3);
        skull.setPlayerProfile(profile);
    }

}
