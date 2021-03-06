/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Wojciech Sulejman
 */

package org.nuxeo.ecm.platform.signature.api.pki;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.nuxeo.ecm.platform.signature.api.exception.CertException;
import org.nuxeo.ecm.platform.signature.api.user.UserInfo;

/**
 * This service provides certificate generation and certificate related keystore operations.
 * <p>
 * The interfaces provided by this service are intended to abstract low-level generic certificate operations like PKI
 * key and certificate generation, CSR (Certificate Signing Request) signing with the root certificate, retrieving the
 * certificates from the keystore in a generic way, and also providing CRLs (Certificate Revocation Lists).
 * <p>
 * The bulk of this functionality is provided via the initializeUser(..) method used to generate a fully initialized
 * certificate enclosed in a secured keystore.
 * 
 * @author <a href="mailto:ws@nuxeo.com">Wojciech Sulejman</a>
 */
public interface CertService {

    /**
     * Retrieves the root certificate.
     * 
     * @return
     * @throws CertException
     */
    public X509Certificate getRootCertificate() throws CertException;

    /**
     * Sets up a root service to be used for CA-related services like certificate request signing and certificate
     * revocation.
     * 
     * @param keystore
     * @throws CertException
     */
    public void setRootService(RootService rootService) throws CertException;

    /**
     * Retrieves a KeyStore object from a supplied InputStream. Requires a keystore password.
     * 
     * @param userId
     * @return
     */
    public KeyStore getKeyStore(InputStream keystoreIS, String password) throws CertException;

    /**
     * Retrieves existing private and public key from a KeyStore.
     * 
     * @param userId
     * @return
     */
    public KeyPair getKeyPair(KeyStore ks, String keyAlias, String certificateAlias, String keyPassword)
            throws CertException;

    /**
     * Retrieves an existing certificate from a keystore using keystore's certificate alias.
     * 
     * @param userId
     * @return
     */
    public X509Certificate getCertificate(KeyStore keystore, String certificateAlias) throws CertException;

    /**
     * Generates a private key and a public certificate for a user whose X.509 field information was enclosed in a
     * UserInfo parameter. Stores those artifacts in a password protected keystore. This is the principal method for
     * activating a new certificate and signing it with a root certificate.
     * 
     * @param userId
     * @return KeyStore based on the provided userInfo
     */

    public KeyStore initializeUser(UserInfo userInfo, String keyPassword) throws CertException;

    /**
     * Wraps a certificate object into an OutputStream object secured by a keystore password
     * 
     * @param keystore
     * @param os
     * @param keystorePassword
     * @throws CertException
     */
    public void storeCertificate(KeyStore keystore, OutputStream os, String keystorePassword) throws CertException;

    /**
     * Extracts the email address from a certificate
     * 
     * @param certificate
     * @return
     * @throws CertException
     */
    public String getCertificateEmail(X509Certificate certificate) throws CertException;

}
