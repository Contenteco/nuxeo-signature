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
 *    Wojciech Sulejman
 */
package org.nuxeo.ecm.platform.signature.api.user;

import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.nuxeo.ecm.platform.signature.api.exception.CertException;

/**
 * Carries user information encoded inside an x509Name.
 * <p>
 * This class is a DTO which exposes an X500 Principal view. It is used to pass user information between application
 * layers.
 * <p>
 * Verifies that all required tokens are present.
 * <p>
 * Required tokens:
 * <ul>
 * <li>user identifier (commonName field)
 * <li>user X500Principal: commonName CN, organizationalUnitName OU, organizationName O, countryName C
 * <li>user email (emailAddress)
 *
 * @author <a href="mailto:ws@nuxeo.com">Wojciech Sulejman</a>
 */
public class UserInfo {

    private Map<CNField, String> userFields;

    private X500Principal x500Principal;

    /**
     * The fields provided as a parameter to the constructor. Must be a full set of all the fields as present in the
     * CNField enum.
     *
     * @param userDNFields
     * @throws CertException
     */
    public UserInfo(Map<CNField, String> userDNFields) throws CertException {
        verify(userDNFields);
        this.userFields = userDNFields;
        try {
            x500Principal = new X500Principal(getDN(userDNFields));
        } catch (Exception e) {
            throw new CertException("User data might have an incorrect format" + e);
        }
    }

    /**
     * Verifies that all required X500 Principal field values have been set on this object
     *
     * @param userFields
     * @throws CertException
     */
    public void verify(Map<CNField, String> userFields) throws CertException {
        for (CNField key : CNField.values()) {
            if (null == userFields.get(key)) {
                throw new CertException("UserInfo X500 value missing for:" + key.name());
            }
        }
    }

    /**
     * Returns a formatted DN string
     * 
     * @param userFields
     * @return
     */
    public String getDN(Map<CNField, String> userFields) {
        String dN = "C=" + userFields.get(CNField.C) + ", O=" + userFields.get(CNField.O) + ", OU="
                + userFields.get(CNField.OU) + ", CN=" + userFields.get(CNField.CN);
        return dN;
    }

    public Map<CNField, String> getUserFields() {
        return userFields;
    }

    public X500Principal getX500Principal() {
        return x500Principal;
    }

    public String toString() {
        return this.getUserFields().get(CNField.UserID) + " " + this.getUserFields().get(CNField.CN);
    }

}