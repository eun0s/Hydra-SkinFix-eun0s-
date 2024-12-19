package net.oldschoolminecraft.hydra.misc;

public class ModClassLoader extends ClassLoader {
	private ClassLoader parentClassLoader;
	private ClassLoader modClassLoader;

	public ModClassLoader(ClassLoader var1, ClassLoader var2) {
		this.parentClassLoader = var1;
		this.modClassLoader = var2;
	}

	protected Class<?> loadClass(String var1, boolean var2) throws ClassNotFoundException {
		try {
			Thread.currentThread().setContextClassLoader(this);
		} catch (Exception var8) {
		}

		Class var3;
		try {
			var3 = this.parentClassLoader.loadClass(var1);
			if(var3 != null) {
				return var3;
			}
		} catch (ClassNotFoundException var7) {
		}

		try {
			var3 = this.modClassLoader.loadClass(var1);
			if(var3 != null) {
				return var3;
			}
		} catch (ClassNotFoundException var6) {
		}

		try {
			var3 = ClassLoader.getSystemClassLoader().loadClass(var1);
			if(var3 != null) {
				return var3;
			}
		} catch (ClassNotFoundException var5) {
		}

		try {
			var3 = super.loadClass(var1, var2);
			if(var3 != null) {
				return var3;
			}
		} catch (ClassNotFoundException var4) {
		}

		throw new ClassNotFoundException(var1);
	}
}
