package com.cd.crashdata;

import java.io.BufferedReader;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import javax.crypto.Cipher;
//import org.bouncycastle.openssl.PEMReader;
import android.util.Base64;
import android.util.Log;

public class rsaEncryption {

	private String publicKey;

	public rsaEncryption(String key) {
		
		this.publicKey = key;

	}

	/*
	 * Function to encrypt the data.
	 */
/*
	public String encrypt(String data, String pKey) throws Exception {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(
				"RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");

		byte[] keyBytes = Base64.decode(pKey, 0);

		PublicKey publickey = strToPublicKey(new String(keyBytes));
		cipher.init(Cipher.ENCRYPT_MODE, publickey);

		//Base 64 encode the encrypted data
		byte[] encryptedBytes = Base64.encode(cipher.doFinal(data.getBytes()),
				0);

		return new String(encryptedBytes);

	}*/
/*
	public static PublicKey strToPublicKey(String s) {

		PublicKey pbKey = null;
		try {

			BufferedReader br = new BufferedReader(new StringReader(s));
			PEMReader pr = new PEMReader(br);
			Object obj = pr.readObject();

			if (obj instanceof PublicKey) {
				pbKey = (PublicKey) pr.readObject();
			} else if (obj instanceof KeyPair) {
				KeyPair kp = (KeyPair) pr.readObject();
				pbKey = kp.getPublic();
			}
			pr.close();

		} catch (Exception e) {
			Log.d("CIPHER", e.getMessage());
		}

		return pbKey;
	}*/

	private String getPublicKey() {

		return publicKey;
		// write a code to fetch it
		// Base 64 encoded public key
		// return
		// "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0NCk1JR2ZNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0R05BRENCaVFLQmdRRE1PZGtaN28yMmJiSTAxZnY5QWtvMXFmTHd6MzRBYlA3ejgrcUMNCi9XZGNHdEFmdGk1QUFvZWUxOTV2NUd1QTZhak9JS3Y1cDkzdEdFNitWamdRdG03d0phS25kMno1UGpvZHIvc2R5bW9hNGdZT2dXcHQNCmVvamRybFZmeHZBbUxUN0JsWEExNGNuQUxDc3prYUtmTktSeEIrWDZlZGIyQkVaVnlKYThQcTBLYndJREFRQUINCi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLQ==";
	}

}
