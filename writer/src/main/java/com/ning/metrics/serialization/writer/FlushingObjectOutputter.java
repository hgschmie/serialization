/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.serialization.writer;

import com.ning.metrics.serialization.event.Event;
import com.ning.metrics.serialization.event.EventSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class FlushingObjectOutputter extends DefaultObjectOutputter
{
    private final OutputStream out;
    private final int batchSize;
    private int objectsWritten = 0;

    public FlushingObjectOutputter(final FileOutputStream out, final EventSerializer serializer, final int batchSize) throws IOException
    {
        super(out, serializer);
        this.out = out;
        this.batchSize = batchSize;
    }

    @Override
    public void writeObject(final Event event) throws IOException
    {
        super.writeObject(event);
        objectsWritten++;

        if (objectsWritten >= batchSize) {
            out.flush();
            objectsWritten = 0;
        }
    }

    @Override
    public void close() throws IOException
    {
        out.flush();
        super.close();
    }
}
