/*
 * Copyright 1998-2004 Sun Microsystems, Inc.  All Rights Reserved.
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
 *
 * @bug 4087295
 * @build install/SerialDriver.java test/SerialDriver.java extension/ExtendedObjectInputStream.java
 * @summary Enable resolveClass() to accommodate package renaming.
 * This fix enables one to implement a resolveClass method that maps a
 * Serialiazable class within a serialization stream to the same class
 * in a different package within the JVM runtime. See run shell script
 * for instructions on how to run this test.
 */

package install;

import java.io.*;
import extension.ExtendedObjectInputStream;

public class SerialDriver implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    SerialDriver next;
    transient Object objarray[];

    public SerialDriver() {
        name = "<terminator>";
        next = null;
    }

    public SerialDriver(String name, SerialDriver next) {
        this.name = name;
        this.next = next;
    }

    static boolean serialize;
    static boolean deserialize;

    public static void main(String args[]) throws Exception  {
        SerialDriver obj = new SerialDriver("SerialDriver_2",
                                            new SerialDriver());
        SerialDriver[] array = new SerialDriver[5];
        for (int i = 0; i < array.length; i++)
            array[i] = new SerialDriver("SerialDriver_1_" + i, new SerialDriver());

        /*
         * see if we are serializing or deserializing.
         * The ability to deserialize or serialize allows
         * us to see the bidirectional readability and writeability
         */
        if (args.length == 1) {
            if (args[0].equals("-d")) {
                deserialize = true;
            } else if (args[0].equals("-s")) {
                serialize = true;
            } else {
                usage();
                throw new Exception("incorrect command line arguments");
            }
        } else {
            usage();
            throw new Exception("incorrect command line arguments");
        }

        File f = new File("stream.ser");
        if (serialize) {
            // Serialize the subclass
            try {
                FileOutputStream fo = new FileOutputStream(f);
                ObjectOutputStream so = new ObjectOutputStream(fo);
                so.writeObject(obj);
                /* Skip arrays since they do not work with rename yet.
                   The serialVersionUID changes due to the name change
                   and there is no way to set the serialVersionUID for an
                   array. */
                so.writeObject(array);
                so.flush();
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            }
        }
        if (deserialize) {
            // Deserialize the subclass
            try {
                FileInputStream fi = new FileInputStream(f);
                ExtendedObjectInputStream si =
                    new ExtendedObjectInputStream(fi);
                si.addRenamedClassName("test.SerialDriver", "install.SerialDriver");
                si.addRenamedClassName("[Ltest.SerialDriver;",
                                       "[Linstall.SerialDriver");
                obj = (SerialDriver) si.readObject();
                array = (SerialDriver[]) si.readObject();
                si.close();
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            }
            System.out.println();
            System.out.println("Printing deserialized class: ");
            System.out.println();
            System.out.println(obj.toString());
            System.out.println();
        }
    }


    public String toString() {
        String nextString = next != null ? next.toString() : "<null>";
        return "name =" + name + " next = <" + nextString + ">";
    }

    /**
     * Prints out the usage
     */
    static void usage() {
        System.out.println("Usage:");
        System.out.println("      -s (in order to serialize)");
        System.out.println("      -d (in order to deserialize)");
    }
}
