package io.mycat.bigmem.sqlcache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  Mycat Buffer Page 抽象类
 *
 * @author zagnix
 * @version 1.0
 * @create 2016-12-27 18:49
 */


public abstract class MyCatBufferPage implements IBufferPage {
    private final static Logger logger =
                LoggerFactory.getLogger(MyCatBufferPage.class);

    protected volatile boolean dirty = false;
    protected volatile boolean recycled = false;
    protected ThreadLocalByteBuffer threadLocalByteBuffer;
    protected AtomicLong lastAccessedTimestamp;
    protected AtomicLong refCount = new AtomicLong(0);
    protected long cacheTTL = 0L;

    public MyCatBufferPage(ByteBuffer byteBuffer,long cacheTTL){
        this.lastAccessedTimestamp = new AtomicLong(System.currentTimeMillis());
        this.threadLocalByteBuffer = new ThreadLocalByteBuffer(byteBuffer);
        this.cacheTTL = cacheTTL;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isRecycled() {
        return recycled;
    }

    public void setRecycled(boolean recycled) {
        this.recycled = recycled;
    }

    public ThreadLocalByteBuffer getThreadLocalByteBuffer() {
        return threadLocalByteBuffer;
    }

    public void setThreadLocalByteBuffer(ThreadLocalByteBuffer threadLocalByteBuffer) {
        this.threadLocalByteBuffer = threadLocalByteBuffer;
    }

    public AtomicLong getLastAccessedTimestamp() {
        return lastAccessedTimestamp;
    }

    public void setLastAccessedTimestamp(AtomicLong lastAccessedTimestamp) {
        this.lastAccessedTimestamp = lastAccessedTimestamp;
    }

    public AtomicLong getRefCount() {
        return refCount;
    }

    public void setRefCount(AtomicLong refCount) {
        this.refCount = refCount;
    }

    public long getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(long cacheTTL) {
        this.cacheTTL = cacheTTL;
    }


    /**
     * Thread Local ByteBuffer
     */
    protected static class ThreadLocalByteBuffer extends ThreadLocal<ByteBuffer> {
        private ByteBuffer byteBuffer;

        public ThreadLocalByteBuffer(ByteBuffer src) {
            byteBuffer = src;
        }

        public ByteBuffer getByteBuffer() {
            return byteBuffer;
        }

        public void setByteBuffer(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
        }


        @Override
        protected synchronized ByteBuffer initialValue() {
            ByteBuffer bbuffer = byteBuffer.duplicate();
            return bbuffer;
        }
    }
}
