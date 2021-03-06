/*
 * Copyright 2001-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

/*
 * @test
 * @bug 4387882 4451038
 * @summary Need to revisit the javadocs for JSSE, especially the
 *      promoted classes, and HttpsURLConnection.getCipherSuite throws
 *      NullPointerException
 * @author Brad Wetmore
 */

import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import com.sun.net.ssl.HttpsURLConnection;

/*
 * Tests that the com null argument changes made it in ok.
 */

public class ComURLNulls {

    public static void main(String[] args) throws Exception {

        System.setProperty("java.protocol.handler.pkgs",
                                "com.sun.net.ssl.internal.www.protocol");
        /**
         * This test does not establish any connection to the specified
         * URL, hence a dummy URL is used.
         */
        URL foobar = new URL("https://example.com/");

        HttpsURLConnection urlc =
            (HttpsURLConnection) foobar.openConnection();

        try {
            urlc.getCipherSuite();
        } catch (IllegalStateException e) {
            System.out.print("Caught proper exception: ");
            System.out.println(e.getMessage());
        }

        try {
            urlc.getServerCertificateChain();
        } catch (IllegalStateException e) {
            System.out.print("Caught proper exception: ");
            System.out.println(e.getMessage());
        }

        try {
            urlc.setDefaultHostnameVerifier(null);
        } catch (IllegalArgumentException e) {
            System.out.print("Caught proper exception: ");
            System.out.println(e.getMessage());
        }

        try {
            urlc.setHostnameVerifier(null);
        } catch (IllegalArgumentException e) {
            System.out.print("Caught proper exception: ");
            System.out.println(e.getMessage());
        }

        try {
            urlc.setDefaultSSLSocketFactory(null);
        } catch (IllegalArgumentException e) {
            System.out.print("Caught proper exception: ");
            System.out.println(e.getMessage());
        }

        try {
            urlc.setSSLSocketFactory(null);
        } catch (IllegalArgumentException e) {
            System.out.print("Caught proper exception");
            System.out.println(e.getMessage());
        }
        System.out.println("TESTS PASSED");
    }
}
