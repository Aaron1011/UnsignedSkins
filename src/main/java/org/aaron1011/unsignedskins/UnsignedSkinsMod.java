package org.aaron1011.unsignedskins;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class UnsignedSkinsMod extends DummyModContainer {

    public static final String modId = "unsignedskins";
    public static final String modName = "Unsigned Skins";
    public static final String version = "1.0.0";

    private static final String TEXTURE = "eyJ0ZXh0dXJlcyI6IHsiU0tJTiI6IHsidXJsIjogImh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9teXNraW4ucG5nIiwgIm1ldGFkYXRhIjogeyJtb2RlbCI6ICJzbGltIn19fSwgInRpbWVzdGFtcCI6IDE0MjUyMTg2NjIyNzQsICJwcm9maWxlTmFtZSI6ICJhamFqYSIsICJwcm9maWxlSWQiOiAiYTllNzYyNzdjNDUzNGZmMDk4YTA5NDYxZmU4MWU0NTgifQ==";
    private static final String TEXTURE_SIGNED = "eyJ0aW1lc3RhbXAiOjE0MjUyMjY4OTIyOTIsInByb2ZpbGVJZCI6ImE5ZTc2Mjc3YzQ1MzRmZjA5OGEwOTQ2MWZlODFlNDU4IiwicHJvZmlsZU5hbWUiOiJBYXJvbjEwMTEiLCJpc1B1YmxpYyI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJmMTkyZGY2MWY0OTI5NTBkMDU0MGNmOGY0NjM0NDEyZWZlM2FkYWU2MzJjMDY2NzE4OTc0ODc4NTI2ODRkNiIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19";
    private static final String TEXTURE_SIGNATURE = "jHV4TgnmmLF/8ttBMBU8ifnPn2Ga80HeENsB5dsFCEd+Mu2gGsDT/CyLUzVIQHJyRE81HdlHR6OYpRAhGpSuMXk9CiQavq2nD7yJv8CtOXZoftkY8d19FusWT9agGUHSYrSMnzGVtfh3WVteUmRjBgJkImbLqVOlltfS+mpebQ6VNTMUWHgKWEj37wUoWreIPSv2btBDqLULKziZatpiIcSmZo/wyYMTC6Snioc6KxS4evfbRa0lMNrCEM1sZwmYusvIoYDAXqaOu9DWMTAYQ+s8TQkyjkLfZpqSEze8Jg9VG4e2G4HEQ3hO+nXqwYxPcaPYd3UDSZYxOeSR7s57noP0286pEA9n5RGoTMKJlZpOVJyC9Xja8WgzDLsND/wqBnewtdXAm1z/gSb7g78y1WhfZrwRyWI3g0Uwj62dTkxMSWO4lf7KUoOfpgIsByLuD+JU5yCSY+RCQglyYB8q8oXYRjHRfoQrlEdU7glRWnsR35cTfye69yPbMZcZ1wyjHCzp6wOklySFlX2mLz/az2avrnrK2tIfumGJd2J3LxAjbAvKq1xJYQPTQaJNIDSfWJrlHuZHdEp8x2FY9/vYofj3SN8Ynf3J4/JTZSN8IzbdhZTwJjO7i7KBP2thdXO804vNwXtPqdFH8iJ0g9nhEXZARzjOdxpxrWSUaOWsd0w=";
    //private static final String TEXTURE = "eyJ0aW1lc3RhbXAiOjE0MjUyMTg2NjIyNzQsInByb2ZpbGVJZCI6ImE5ZTc2Mjc3YzQ1MzRmZjA5OGEwOTQ2MWZlODFlNDU4IiwicHJvZmlsZU5hbWUiOiJBYXJvbjEwMTEiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYxOTJkZjYxZjQ5Mjk1MGQwNTQwY2Y4ZjQ2MzQ0MTJlZmUzYWRhZTYzMmMwNjY3MTg5NzQ4Nzg1MjY4NGQ2IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=";

    private static final GameProfile profile;
    private static final GameProfile signedProfile;

    static {
        profile = new GameProfile(UUID.randomUUID(), "Bob");
        profile.getProperties().put("textures", new Property("textures", TEXTURE, null));

        signedProfile = new GameProfile(UUID.randomUUID(), "Steve");
        signedProfile.getProperties().put("textures", new Property("textures", TEXTURE_SIGNED, TEXTURE_SIGNATURE));
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
        FMLCommonHandler.instance().bus().register(this);
    }

    @Subscribe
    @SubscribeEvent
    @Mod.EventHandler
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Joined");

    }

    @SideOnly(Side.SERVER)
    @Subscribe
    @SubscribeEvent
    @Mod.EventHandler
    public void onPlayerShoot(ArrowLooseEvent event) {
        if (!(event.entityPlayer.worldObj instanceof WorldServer)) {
            return;
        }

        System.out.println("EVENT!!!!");

        event.entityPlayer.worldObj.setBlockState(event.entityPlayer.getPosition(), Blocks.skull.getDefaultState());

        TileEntitySkull skull = (TileEntitySkull) event.entityPlayer.worldObj.getTileEntity(event.entityPlayer.getPosition());
        skull.setType(3);
        skull.setPlayerProfile(profile);

        FakePlayer signedPlayer = FakePlayerFactory.get((WorldServer) event.entityPlayer.worldObj, signedProfile);
        FakePlayer unsignedPlayer = FakePlayerFactory.get((WorldServer) event.entityPlayer.worldObj, profile);

        ((EntityPlayerMP) event.entityPlayer).playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, signedPlayer));
        ((EntityPlayerMP) event.entityPlayer).playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, unsignedPlayer));
    }

}
