package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class mod_SkinFix extends BaseMod {
	@MLProp(
		name = "Skin API",
		info = "You can change your skin API URL here if you need to."
	)
	public static String skinUrl = "https://os-mc.net/api/v1/skin?username=";
	@MLProp(
		name = "Use \".png\" in Skin URL",
		info = "This tells the mod whether or not it should append \'.png\' to the end of the API request URL. Some APIs require this, while others won\'t work with it."
	)
	public static boolean appendSkinPNG = false;
	@MLProp(
		name = "Cloak API",
		info = "You can change your cloak API URL here if you need to."
	)
	public static String capeUrl = "https://os-mc.net/api/v1/cloak?username=";
	@MLProp(
		name = "Global cloak URL",
		info = "If this is on, you can set an image URL inside the \'Cloak API\' setting and everyone will have that as a cloak."
	)
	public static boolean appendCapeUsername = true;
	@MLProp(
		name = "Use \".png\" in Cloak URL",
		info = "This tells the mod whether or not it should append \'.png\' to the end of the API request URL. Some APIs require this, while others won\'t work with it."
	)
	public static boolean appendCapePNG = false;
	private final Field field_20914_EField = this.getObfuscatedPrivateField(WorldClient.class, new String[]{"G", "field_20914_E"});

	public final String Name() {
		return "Hydra SkinFix";
	}

	public final String Description() {
		return "Our custom skin fix";
	}

	public final String Version() {
		return "v1.0";
	}

	public mod_SkinFix() {
		ModLoader.SetInGameHook(this, true, false);
	}

	public final boolean OnTickInGame(Minecraft var1) {
		this.updateSkinAndCape(var1.thePlayer);
		if(var1.theWorld instanceof WorldClient) {
			try {
				Iterator var2 = ((Set)this.field_20914_EField.get(var1.theWorld)).iterator();

				while(var2.hasNext()) {
					Entity var3 = (Entity)var2.next();
					if(var3 instanceof EntityOtherPlayerMP) {
						this.updateSkinAndCape((EntityPlayer)var3);
					}
				}
			} catch (IllegalArgumentException var4) {
				var4.printStackTrace();
			} catch (IllegalAccessException var5) {
				var5.printStackTrace();
			}
		}

		return true;
	}

	private final void updateSkinAndCape(EntityPlayer var1) {
		if(!Objects.equals(var1.skinUrl, skinUrl + var1.username) || !Objects.equals(var1.playerCloakUrl, capeUrl + var1.username)) {
			var1.skinUrl = skinUrl + var1.username;
			ModLoader.getMinecraftInstance().renderGlobal.obtainEntitySkin(var1);
			var1.playerCloakUrl = capeUrl + var1.username;
			var1.cloakUrl = var1.playerCloakUrl;
			ModLoader.getMinecraftInstance().renderEngine.obtainImageData(var1.cloakUrl, new ImageBufferDownload());
			System.out.println("Updated skin and cloak for: " + var1.username);
			System.out.println("Skin: " + var1.skinUrl);
			System.out.println("Cloak: " + var1.cloakUrl);
		}

	}

	private final Field getObfuscatedPrivateField(Class<?> var1, String[] var2) {
		Field[] var3 = var1.getDeclaredFields();
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			Field var6 = var3[var5];
			String[] var7 = var2;
			int var8 = var2.length;

			for(int var9 = 0; var9 < var8; ++var9) {
				String var10 = var7[var9];
				if(var6.getName().equals(var10)) {
					var6.setAccessible(true);
					return var6;
				}
			}
		}

		return null;
	}
}
