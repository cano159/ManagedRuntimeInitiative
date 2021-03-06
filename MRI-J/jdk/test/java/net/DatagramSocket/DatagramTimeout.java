/*
 * Copyright 1998-2001 Sun Microsystems, Inc.  All Rights Reserved.
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

/**
 * @test
 * @bug 4163126
 * @summary  test to see if timeout hangs
 * @run main/timeout=15 DatagramTimeout
 */
import java.net.*;
import java.io.*;

public class DatagramTimeout {

    public static ServerSocket sock;

    public static void main(String[] args) throws Exception {
        boolean success = false;
        try {
            DatagramSocket sock;
            DatagramPacket p;
            byte[] buffer = new byte[50];
            p = new DatagramPacket(buffer, buffer.length);
            sock = new DatagramSocket(2333);
            sock.setSoTimeout(2);
            sock.receive(p);
        } catch (SocketTimeoutException e) {
            success = true;
        }
        if (!success)
            throw new RuntimeException("Socket timeout failure.");
    }

}
