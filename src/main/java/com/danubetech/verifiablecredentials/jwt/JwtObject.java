package com.danubetech.verifiablecredentials.jwt;

import com.danubetech.keyformats.crypto.ByteSigner;
import com.danubetech.keyformats.crypto.ByteVerifier;
import com.danubetech.keyformats.crypto.impl.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import info.weboftrust.ldsignatures.adapter.JWSSignerAdapter;
import info.weboftrust.ldsignatures.adapter.JWSVerifierAdapter;
import org.bitcoinj.core.ECKey;
import org.erdtman.jcs.JsonCanonicalizer;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class JwtObject {

	private final JWTClaimsSet payload;
	private JWSObject jwsObject;
	private String compactSerialization;

	public JwtObject(JWTClaimsSet payload, JWSObject jwsObject, String compactSerialization) {

		if (payload == null) throw new NullPointerException();

		this.payload = payload;
		this.jwsObject = jwsObject;
		this.compactSerialization = compactSerialization;
	}

	/*
	 * Sign
	 */

	private String sign(JWSSigner jwsSigner, JWSAlgorithm alg, String kid, boolean canonicalize) throws JOSEException {

		JWSHeader.Builder jwsHeaderBuilder = new JWSHeader.Builder(alg);
		jwsHeaderBuilder.type(JOSEObjectType.JWT);
		if (kid != null) jwsHeaderBuilder.keyID(kid);

		JWSHeader jwsHeader = jwsHeaderBuilder.build();

		JWSObject jwsObject = new EscapedSlashWorkaroundJWSObject(jwsHeader, this.getPayload(), canonicalize);

		jwsObject.sign(jwsSigner);

		this.jwsObject = jwsObject;
		this.compactSerialization = jwsObject.serialize();

		return this.compactSerialization;
	}

	public String sign_RSA_PS256(ByteSigner signer, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new JWSSignerAdapter(signer, JWSAlgorithm.PS256), JWSAlgorithm.PS256, kid, canonicalize);
	}

	public String sign_RSA_PS256(ByteSigner signer) throws JOSEException {
		return this.sign_RSA_PS256(signer, null, false);
	}

	public String sign_RSA_PS256(RSAPrivateKey privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign_RSA_PS256(new RSA_PS256_PrivateKeySigner(privateKey), kid, canonicalize);
	}

	public String sign_RSA_PS256(RSAPrivateKey privateKey) throws JOSEException {
		return this.sign_RSA_PS256(privateKey, null, false);
	}

	public String sign_RSA_PS256(com.nimbusds.jose.jwk.RSAKey privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new com.nimbusds.jose.crypto.RSASSASigner(privateKey), JWSAlgorithm.PS256, kid, canonicalize);
	}

	public String sign_RSA_PS256(com.nimbusds.jose.jwk.RSAKey privateKey) throws JOSEException {
		return this.sign_RSA_PS256(privateKey, null, false);
	}

	public String sign_RSA_RS256(ByteSigner signer, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new JWSSignerAdapter(signer, JWSAlgorithm.RS256), JWSAlgorithm.RS256, kid, canonicalize);
	}

	public String sign_RSA_RS256(ByteSigner signer) throws JOSEException {
		return this.sign_RSA_RS256(signer, null, false);
	}

	public String sign_RSA_RS256(RSAPrivateKey privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign_RSA_RS256(new RSA_RS256_PrivateKeySigner(privateKey), kid, canonicalize);
	}

	public String sign_RSA_RS256(RSAPrivateKey privateKey) throws JOSEException {
		return this.sign_RSA_RS256(privateKey, null, false);
	}

	public String sign_RSA_RS256(com.nimbusds.jose.jwk.RSAKey privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new com.nimbusds.jose.crypto.RSASSASigner(privateKey), JWSAlgorithm.RS256, kid, canonicalize);
	}

	public String sign_RSA_RS256(com.nimbusds.jose.jwk.RSAKey privateKey) throws JOSEException {
		return this.sign_RSA_RS256(privateKey, null, false);
	}

	public String sign_Ed25519_EdDSA(ByteSigner signer, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new JWSSignerAdapter(signer, JWSAlgorithm.EdDSA), JWSAlgorithm.EdDSA, kid, canonicalize);
	}

	public String sign_Ed25519_EdDSA(ByteSigner signer) throws JOSEException {
		return this.sign_Ed25519_EdDSA(signer, null, false);
	}

	public String sign_Ed25519_EdDSA(byte[] privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign_Ed25519_EdDSA(new Ed25519_EdDSA_PrivateKeySigner(privateKey), kid, canonicalize);
	}

	public String sign_Ed25519_EdDSA(byte[] privateKey) throws JOSEException {
		return this.sign_Ed25519_EdDSA(privateKey, null, false);
	}

	public String sign_Ed25519_EdDSA(com.nimbusds.jose.jwk.OctetKeyPair privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new com.nimbusds.jose.crypto.Ed25519Signer(privateKey), JWSAlgorithm.EdDSA, kid, canonicalize);
	}

	public String sign_Ed25519_EdDSA(com.nimbusds.jose.jwk.OctetKeyPair privateKey) throws JOSEException {
		return this.sign_Ed25519_EdDSA(privateKey, null, false);
	}

	public String sign_secp256k1_ES256K(ByteSigner signer, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new JWSSignerAdapter(signer, JWSAlgorithm.ES256K), JWSAlgorithm.ES256K, kid, canonicalize);
	}

	public String sign_secp256k1_ES256K(ByteSigner signer) throws JOSEException {
		return this.sign_secp256k1_ES256K(signer, null, false);
	}

	public String sign_secp256k1_ES256K(ECKey privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign_secp256k1_ES256K(new secp256k1_ES256K_PrivateKeySigner(privateKey));
	}

	public String sign_secp256k1_ES256K(ECKey privateKey) throws JOSEException {
		return this.sign_secp256k1_ES256K(privateKey, null, false);
	}

	public String sign_secp256k1_ES256K(com.nimbusds.jose.jwk.ECKey privateKey, String kid, boolean canonicalize) throws JOSEException {
		return this.sign(new com.nimbusds.jose.crypto.ECDSASigner(privateKey), JWSAlgorithm.ES256K, kid, canonicalize);
	}

	public String sign_secp256k1_ES256K(com.nimbusds.jose.jwk.ECKey privateKey) throws JOSEException {
		return this.sign_secp256k1_ES256K(privateKey, null, false);
	}

	/*
	 * Verify
	 */

	private boolean verify(JWSVerifier jwsVerifier) throws JOSEException {
		return this.jwsObject.verify(jwsVerifier);
	}

	public boolean verify_RSA_PS256(ByteVerifier verifier) throws JOSEException {
		return this.verify(new JWSVerifierAdapter(verifier, JWSAlgorithm.RS256));
	}

	public boolean verify_RSA_PS256(RSAPublicKey publicKey) throws JOSEException {
		return this.verify_RSA_PS256(new RSA_PS256_PublicKeyVerifier(publicKey));
	}

	public boolean verify_RSA_PS256(com.nimbusds.jose.jwk.RSAKey publicKey) throws JOSEException {
		return this.verify(new com.nimbusds.jose.crypto.RSASSAVerifier(publicKey));
	}

	public boolean verify_RSA_RS256(ByteVerifier verifier) throws JOSEException {
		return this.verify(new JWSVerifierAdapter(verifier, JWSAlgorithm.RS256));
	}

	public boolean verify_RSA_RS256(RSAPublicKey publicKey) throws JOSEException {
		return this.verify_RSA_RS256(new RSA_RS256_PublicKeyVerifier(publicKey));
	}

	public boolean verify_RSA_RS256(com.nimbusds.jose.jwk.RSAKey publicKey) throws JOSEException {
		return this.verify(new com.nimbusds.jose.crypto.RSASSAVerifier(publicKey));
	}

	public boolean verify_Ed25519_EdDSA(ByteVerifier verifier) throws JOSEException {
		return this.verify(new JWSVerifierAdapter(verifier, JWSAlgorithm.EdDSA));
	}

	public boolean verify_Ed25519_EdDSA(byte[] publicKey) throws JOSEException {
		return this.verify_Ed25519_EdDSA(new Ed25519_EdDSA_PublicKeyVerifier(publicKey));
	}

	public boolean verify_Ed25519_EdDSA(com.nimbusds.jose.jwk.OctetKeyPair publicKey) throws JOSEException {
		return this.verify(new com.nimbusds.jose.crypto.Ed25519Verifier(publicKey));
	}

	public boolean verify_secp256k1_ES256K(ByteVerifier verifier) throws JOSEException {
		return this.verify(new JWSVerifierAdapter(verifier, JWSAlgorithm.ES256K));
	}

	public boolean verify_secp256k1_ES256K(ECKey publicKey) throws JOSEException {
		return this.verify_secp256k1_ES256K(new secp256k1_ES256K_PublicKeyVerifier(publicKey));
	}

	public boolean verify_secp256k1_ES256K(com.nimbusds.jose.jwk.ECKey publicKey) throws JOSEException {
		return this.verify(new com.nimbusds.jose.crypto.ECDSAVerifier(publicKey));
	}

	/*
	 * Helper class
	 */

	private static class EscapedSlashWorkaroundJWSObject extends JWSObject {

		private static final long serialVersionUID = -587898962717783109L;

		public EscapedSlashWorkaroundJWSObject(final JWSHeader jwsHeader, final JWTClaimsSet jwtClaimsSet, boolean canonicalize) {
			super(jwsHeader, makePayload(jwtClaimsSet, canonicalize));
		}

		private static Payload makePayload(JWTClaimsSet jwtClaimsSet, boolean canonicalize) {
			Map<String, Object> jsonObject = jwtClaimsSet.toJSONObject();
			String payloadString = JSONObjectUtils.toJSONString(jsonObject);
			if (canonicalize) {
				try {
					payloadString = new JsonCanonicalizer(payloadString).getEncodedString();
				} catch (IOException ex) {
					throw new IllegalArgumentException(ex.getMessage(), ex);
				}
			}
			payloadString = payloadString.replace("\\/", "/");
			return new Payload(payloadString);
		}
	}

	/*
	 * Getters
	 */

	public JWTClaimsSet getPayload() {
		return this.payload;
	}

	public JWSObject getJwsObject() {
		return this.jwsObject;
	}

	public String getCompactSerialization() {
		return this.compactSerialization;
	}
}
