package net.oldschoolminecraft.hydra.skin;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.oldschoolminecraft.hydra.misc.SimpleHTTP;
import net.oldschoolminecraft.hydra.pipes.StringPipe;

public class SkinFix {
	private static final Gson gson = new Gson();
	private static final String STEVE = "https://minotar.net/skin/MHF_Steve";
	private final EntityPlayer entityPlayer;

	public SkinFix(EntityPlayer var1) {
		this.entityPlayer = var1;
	}

	public void skinRoutine() {
		try {
			this.entityPlayer.skinUrl = this.getHydraSkin(this.entityPlayer.username);
			System.out.println("Found Hydra skin for: " + this.entityPlayer.username + ", url=" + this.entityPlayer.skinUrl + ", thread=" + Thread.currentThread().getName());
		} catch (SkinException var4) {
			try {
				this.entityPlayer.skinUrl = this.getMojangSkin(this.entityPlayer.username);
				System.out.println("Found Mojang skin for: " + this.entityPlayer.username + ", url=" + this.entityPlayer.skinUrl + ", thread=" + Thread.currentThread().getName());
			} catch (SkinException var3) {
				this.entityPlayer.skinUrl = "https://minotar.net/skin/MHF_Steve";
			}
		}

		Minecraft.getMinecraft().renderGlobal.obtainEntitySkin(this.entityPlayer);
	}

	public void cloakRoutine() {
		try {
			String var1 = this.getHydraCloak(this.entityPlayer.username);
			this.entityPlayer.playerCloakUrl = var1;
			this.entityPlayer.cloakUrl = var1;
			System.out.println("Found Hydra cloak for: " + this.entityPlayer.username + ", url=" + var1 + ", thread=" + Thread.currentThread().getName());
		} catch (SkinException var2) {
		}

		Minecraft.getMinecraft().renderGlobal.obtainEntitySkin(this.entityPlayer);
	}

	public void exec() {
		(new Thread(() -> {
			try {
				this.entityPlayer.skinUrl = this.getHydraSkin(this.entityPlayer.username);
			} catch (SkinException var5) {
				try {
					this.entityPlayer.skinUrl = this.getMojangSkin(this.entityPlayer.username);
				} catch (SkinException var4) {
					this.entityPlayer.skinUrl = "https://minotar.net/skin/MHF_Steve";
				}
			}

			try {
				String var1 = this.getHydraCloak(this.entityPlayer.username);
				this.entityPlayer.playerCloakUrl = var1;
				this.entityPlayer.cloakUrl = var1;
			} catch (SkinException var3) {
			}

		})).start();
	}

	public void tryGetSkin(String var1, StringPipe var2) {
		if(var1 != null) {
			String var3 = null;
			String var4 = null;

			try {
				var3 = this.getHydraSkin(var1);
			} catch (SkinException var7) {
				System.out.println("User does not have Hydra skin: " + var1 + ", ex=" + var7.getMessage());
			}

			try {
				var4 = this.getMojangSkin(var1);
			} catch (SkinException var6) {
				System.out.println("User does not have Mojang skin: " + var1 + ", ex=" + var6.getMessage());
			}

			if(var3 != null) {
				var2.flush(var3);
			} else if(var4 != null) {
				var2.flush(var4);
			} else {
				var2.flush("https://minotar.net/skin/MHF_Steve");
			}
		}
	}

	public void tryGetCloak(String var1, StringPipe var2) {
		String var3 = null;

		try {
			var3 = this.getHydraCloak(var1);
		} catch (SkinException var5) {
		}

		if(var3 != null) {
			var2.flush(var3);
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
