package net.oldschoolminecraft.hydra.skin;

import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.oldschoolminecraft.hydra.misc.SimpleHTTP;

public class SkinFixThread extends Thread {
	private static final SkinFixThread instance = new SkinFixThread();
	private static final String STEVE = "https://minotar.net/skin/MHF_Steve";
	private final LinkedList<EntityPlayer> playerQueue = new LinkedList();

	public static SkinFixThread getInstance() {
		return instance;
	}

	public void queuePlayer(EntityPlayer var1) {
		this.playerQueue.add(var1);
	}

	public void run() {
		while(Minecraft.getMinecraft().running) {
			if(!this.playerQueue.isEmpty()) {
				EntityPlayer var1 = (EntityPlayer)this.playerQueue.remove();

				try {
					var1.skinUrl = this.getHydraSkin(var1.username);
					System.out.println("Found Hydra skin for: " + var1.username + ", url=" + var1.skinUrl + ", thread=" + Thread.currentThread().getName());
				} catch (SkinException var6) {
					try {
						var1.skinUrl = this.getMojangSkin(var1.username);
						System.out.println("Found Mojang skin for: " + var1.username + ", url=" + var1.skinUrl + ", thread=" + Thread.currentThread().getName());
					} catch (SkinException var5) {
						var1.skinUrl = "https://minotar.net/skin/MHF_Steve";
					}
				}

				try {
					String var2 = this.getHydraCloak(var1.username);
					var1.playerCloakUrl = var2;
					var1.cloakUrl = var2;
					System.out.println("Found Hydra cloak for: " + var1.username + ", url=" + var2 + ", thread=" + Thread.currentThread().getName());
				} catch (SkinException var4) {
				}

				Minecraft.getMinecraft().renderGlobal.obtainEntitySkin(var1);
			}
		}

	}

	private String getHydraSkin(String var1) throws SkinException {
		SimpleHTTP var2 = (new SimpleHTTP()).withURL("https://os-mc.net/cosmetics/skin?username=" + var1).exec();
		if(!var2.didExceptionOccur() && var2.getResponseCode() != 404) {
			return "https://os-mc.net/cosmetics/skin?username=" + var1;
		} else {
			throw new SkinException("No skin found");
		}
	}

	private String getHydraCloak(String var1) throws SkinException {
		SimpleHTTP var2 = (new SimpleHTTP()).withURL("https://os-mc.net/cosmetics/cloak?username=" + var1).exec();
		if(!var2.didExceptionOccur() && var2.getResponseCode() != 404) {
			return "https://os-mc.net/cosmetics/cloak?username=" + var1;
		} else {
			throw new SkinException("No cloak found");
		}
	}

	private String getMojangSkin(String var1) throws SkinException {
		SimpleHTTP var2 = (new SimpleHTTP()).withURL("https://minotar.net/skin/" + var1).exec();
		if(!var2.didExceptionOccur() && var2.getResponseCode() != 404) {
			return "https://minotar.net/skin/" + var1;
		} else {
			throw new SkinException("No skin found");
		}
	}
}
