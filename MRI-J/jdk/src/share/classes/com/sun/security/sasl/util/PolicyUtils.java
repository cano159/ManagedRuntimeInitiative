/*
 * Copyright 2000-2003 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.security.sasl.util;

import javax.security.sasl.Sasl;
import java.util.Map;

/**
 * Static class that contains utilities for dealing with Java SASL
 * security policy-related properties.
 *
 * @author Rosanna Lee
 */
final public class PolicyUtils {
    // Can't create one of these
    private PolicyUtils() {
    }

    public final static int NOPLAINTEXT = 0x0001;
    public final static int NOACTIVE = 0x0002;
    public final static int NODICTIONARY = 0x0004;
    public final static int FORWARD_SECRECY = 0x0008;
    public final static int NOANONYMOUS = 0x0010;
    public final static int PASS_CREDENTIALS = 0x0200;

    /**
     * Determines whether a mechanism's characteristics, as defined in flags,
     * fits the security policy properties found in props.
     * @param flags The mechanism's security characteristics
     * @param props The security policy properties to check
     * @return true if passes; false if fails
     */
    public static boolean checkPolicy(int flags, Map props) {
        if (props == null) {
            return true;
        }

        if ("true".equalsIgnoreCase((String)props.get(Sasl.POLICY_NOPLAINTEXT))
            && (flags&NOPLAINTEXT) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String)props.get(Sasl.POLICY_NOACTIVE))
            && (flags&NOACTIVE) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String)props.get(Sasl.POLICY_NODICTIONARY))
            && (flags&NODICTIONARY) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String)props.get(Sasl.POLICY_NOANONYMOUS))
            && (flags&NOANONYMOUS) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String)props.get(Sasl.POLICY_FORWARD_SECRECY))
            && (flags&FORWARD_SECRECY) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String)props.get(Sasl.POLICY_PASS_CREDENTIALS))
            && (flags&PASS_CREDENTIALS) == 0) {
            return false;
        }

        return true;
    }

    /**
     * Given a list of mechanisms and their characteristics, select the
     * subset that conforms to the policies defined in props.
     * Useful for SaslXXXFactory.getMechanismNames(props) implementations.
     *
     */
    public static String[] filterMechs(String[] mechs, int[] policies,
        Map props) {
        if (props == null) {
            return mechs.clone();
        }

        boolean[] passed = new boolean[mechs.length];
        int count = 0;
        for (int i = 0; i< mechs.length; i++) {
            if (passed[i] = checkPolicy(policies[i], props)) {
                ++count;
            }
        }
        String[] answer = new String[count];
        for (int i = 0, j=0; i< mechs.length; i++) {
            if (passed[i]) {
                answer[j++] = mechs[i];
            }
        }

        return answer;
    }
}
