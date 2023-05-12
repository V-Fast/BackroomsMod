package com.lumination.backrooms.client;

import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Environment(EnvType.CLIENT)
public class Discord {
    public static final long appId = 1036661108154585098L;

    // Taken from https://github.com/JnCrMx/discord-game-sdk4j/blob/master/examples/DownloadNativeLibrary.java
    public static File library() throws IOException {
        if (!BackroomsSettings.hasDiscordPresence()) return null;

        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if (osName.contains("windows")) {
            suffix = ".dll";
        } else if (osName.contains("linux")) {
            suffix = ".so";
        } else if (osName.contains("mac os")) {
            suffix = ".dylib";
        } else {
            throw new RuntimeException("cannot determine OS type: "+osName);
        }

		/*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */
        if(arch.equals("amd64")) arch = "x86_64";

        // Path of Discord's library inside the ZIP
        String zipPath = "lib/"+arch+"/"+name+suffix;

        // Open the URL as a ZipInputStream
        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
        connection.setRequestProperty("User-Agent", "discord-game-sdk4j (https://github.com/JnCrMx/discord-game-sdk4j)");
        ZipInputStream zin = new ZipInputStream(connection.getInputStream());

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while((entry = zin.getNextEntry())!=null)
        {
            if(entry.getName().equals(zipPath))
            {
                // Create a new temporary directory
                // We need to do this, because we may not change the filename on Windows
                File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-"+name+System.nanoTime());
                if(!tempDir.mkdir())
                    throw new IOException("Cannot create temporary directory");
                tempDir.deleteOnExit();

                // Create a temporary file inside our directory (with a "normal" name)
                File temp = new File(tempDir, name+suffix);
                temp.deleteOnExit();

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, temp.toPath());

                // We are done, so close the input stream
                zin.close();

                // Return our temporary file
                return temp;
            }
            // next entry
            zin.closeEntry();
        }
        zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }

    public static void setPresence(String details, String state, String image, int playerCount, int playerMax) throws IOException {
        if (!BackroomsSettings.hasDiscordPresence()) return;

        File discordLibrary = library();
        if (discordLibrary == null) {
            System.err.println("Cannot download DiscordSDK.");
            return;
        }

        Core.init(discordLibrary);

        try (CreateParams params = new CreateParams()) {
            params.setClientID(appId);
            params.setFlags(CreateParams.getDefaultFlags());
            try (Core core = new Core(params)) {
                try (Activity activity = new Activity()) {
                    activity.setDetails(details);
                    activity.setState(state);
                    activity.timestamps().setStart(Instant.ofEpochSecond(BackroomsModClient.start));
                    if (playerCount > 0) activity.party().size().setMaxSize(100);
                    if (playerMax > 0) activity.party().size().setCurrentSize(10);
                    activity.assets().setLargeImage(image);
                    activity.assets().setSmallImage("lumaa");
                    activity.assets().setSmallText("By Lumaa");

                    core.activityManager().updateActivity(activity);
                }

                while (true) {
                    core.runCallbacks();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setPresence(String details, String state, String image) throws IOException {
        setPresence(details, state, image, 0, 0);
    }
}