#!/bin/sh

#
# Copyright 2006-2007 Sun Microsystems, Inc.  All Rights Reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
# CA 95054 USA or visit www.sun.com if you need additional information or
# have any questions.
#


# @test
# @bug 6455258
# @summary Sanity test for com.sun.management.HotSpotDiagnosticMXBean.dumpHeap 
#          method 
#
# @build DumpHeap
# @run shell DumpHeap.sh

#Set appropriate jdk
                                                                                
if [ ! -z "${TESTJAVA}" ] ; then
     jdk="$TESTJAVA"
else
     echo "--Error: TESTJAVA must be defined as the pathname of a jdk to test."
     exit 1
fi

failed=0

# we use the pid of this shell process to name the heap dump output file.
DUMPFILE="java_pid$$.hprof"

${TESTJAVA}/bin/java ${TESTVMOPTS} -classpath $TESTCLASSES \
    DumpHeap ${DUMPFILE} || exit 2

# check that heap dump is parsable
${TESTJAVA}/bin/jhat -parseonly true ${DUMPFILE}
if [ $? != 0 ]; then failed=1; fi

# dump file is large so remove it
rm ${DUMPFILE}

exit $failed
