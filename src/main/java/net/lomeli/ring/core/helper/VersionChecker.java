package net.lomeli.ring.core.helper;

import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

import net.lomeli.ring.lib.ModLibs;

public class VersionChecker {
    private static boolean needsUpdate;
    private static boolean isDirect;
    private static String updateJson = "https://raw.githubusercontent.com/Lomeli12/MagicalRings/master/update.json";
    private static String version, downloadURL, changeLog;

    public static void checkForUpdates() {
        needsUpdate = false;
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new URL(updateJson).openStream()));

            if (reader != null) {
                int major = 0, minor = 0, revision = 0;
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("major"))
                        major = reader.nextInt();
                    else if (name.equals("minor"))
                        minor = reader.nextInt();
                    else if (name.equals("revision"))
                        revision = reader.nextInt();
                    else if (name.equals("downloadURL"))
                        downloadURL = reader.nextString();
                    else if (name.equals("direct"))
                        isDirect = reader.nextBoolean();
                    else if (name.equals("changeLog"))
                        changeLog = reader.nextString();
                }
                reader.endObject();
                if (major > ModLibs.MAJOR)
                    needsUpdate = true;
                else if (major == ModLibs.MAJOR) {
                    if (minor > ModLibs.MINOR)
                        needsUpdate = true;
                    else if (minor == ModLibs.MINOR) {
                        if (revision > ModLibs.REVISION)
                            needsUpdate = true;
                    }
                }

                if (needsUpdate)
                    version = major + "." + minor + "." + revision;
            }
        } catch (Exception e) {
            LogHelper.error("Could not connect to update server!");
        }
    }

    public static void sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("modDisplayName", ModLibs.MOD_NAME);
            tag.setString("oldVersion", ModLibs.VERSION);
            tag.setString("newVersion", version);
            tag.setString("updateUrl", downloadURL);
            tag.setBoolean("isDirectLink", isDirect);
            tag.setString("changeLog", changeLog);
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag);
        } else
            LogHelper.info("A new update for " + ModLibs.MOD_NAME + "has been found and can be downloaded at: " + downloadURL);
    }

    public static boolean needUpdate() {
        return needsUpdate;
    }
}
