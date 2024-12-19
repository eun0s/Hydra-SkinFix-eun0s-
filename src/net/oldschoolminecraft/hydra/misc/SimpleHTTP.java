package net.oldschoolminecraft.hydra.misc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class SimpleHTTP {
	private String rawResponse;
	private int responseCode;
	private String targetURL;
	private Exception ex;

	public SimpleHTTP withURL(String var1) {
		this.targetURL = var1;
		return this;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public String getRawResponse() {
		return this.rawResponse;
	}

	public boolean didExceptionOccur() {
		return this.ex != null;
	}

	public Exception getException() {
		return this.ex;
	}

	public SimpleHTTP exec() {
		if(this.targetURL == null) {
			throw new RuntimeException("No URL specified");
		} else {
			try {
				HttpsURLConnection var1 = (HttpsURLConnection)(new URL(this.targetURL)).openConnection();
				var1.setInstanceFollowRedirects(true);
				var1.setConnectTimeout(1000);
				var1.setReadTimeout(1000);
				InputStream var2 = var1.getInputStream();
				String var3 = var1.getContentEncoding();
				var3 = var3 == null ? "UTF-8" : var3;
				short var4 = 1024;
				char[] var5 = new char[var4];
				StringBuilder var6 = new StringBuilder();
				InputStreamReader var7 = new InputStreamReader(var2, var3);

				while(true) {
					int var8 = var7.read(var5, 0, var5.length);
					if(var8 <= 0) {
						this.rawResponse = var6.toString();
						break;
					}

					var6.append(var5, 0, var8);
				}
			} catch (Exception var9) {
				this.ex = var9;
			}

			return this;
		}
	}
}
