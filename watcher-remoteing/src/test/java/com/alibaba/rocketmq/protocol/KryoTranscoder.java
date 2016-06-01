package com.alibaba.rocketmq.protocol;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class KryoTranscoder {
    {
        initialize();
    }

    /**
     * Kryo serializer.
     */
    private static final Kryo kryo = new Kryo();

    /**
     * Logging instance.
     */
    private static final Logger logger = LoggerFactory.getLogger("");

    /**
     * Maximum size of single encoded object in bytes.
     */
    private static final int bufferSize = 512;

    /**
     * Map of class to serializer that handles it.
     */
    private Map<Class<?>, Serializer> serializerMap;


    /**
     * Creates a Kryo-based transcoder.
     *
     * @param initialBufferSize Initial size for buffer holding encoded object data.
     */
    public KryoTranscoder(final int initialBufferSize) {
//        bufferSize = initialBufferSize;
    }


    /**
     * Sets a map of additional types that should be regisetered with Kryo,
     * for example GoogleAccountsService and OpenIdService.
     *
     * @param map Map of class to the serializer instance that handles it.
     */
    public void setSerializerMap(final Map<Class<?>, Serializer> map) {
        this.serializerMap = map;
    }

    public void initialize() {
        // Register types we know about and do not require external configuration

//        kryo.register(BasicCredentialMetaData.class);
//        kryo.register(Class.class, new ClassSerializer(kryo));
//        kryo.register(Date.class, new DateSerializer());
//        kryo.register(HardTimeoutExpirationPolicy.class);
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(RemoteCommand.class);
        kryo.register(Person.class);
//        kryo.register(HandlerResult.class);
//        kryo.register(ImmutableAuthentication.class);
//        kryo.register(MultiTimeUseOrTimeoutExpirationPolicy.class);
//        kryo.register(NeverExpiresExpirationPolicy.class);
//        kryo.register(RememberMeDelegatingExpirationPolicy.class);
//        kryo.register(ServiceTicketImpl.class);
//        kryo.register(SimpleWebApplicationServiceImpl.class, new SimpleWebApplicationServiceSerializer(kryo));
//        kryo.register(ThrottledUseAndTimeoutExpirationPolicy.class);
//        kryo.register(TicketGrantingTicketExpirationPolicy.class);
//        kryo.register(TicketGrantingTicketImpl.class);
//        kryo.register(TimeoutExpirationPolicy.class);
//        kryo.register(URL.class, new URLSerializer(kryo));

        // Register other types
        if (serializerMap != null) {
            for (final Class<?> clazz : serializerMap.keySet()) {
                kryo.register(clazz, serializerMap.get(clazz));
            }
        }

        // Catchall for any classes not explicitly registered
        kryo.setReferences(false);
    }

    /**
     * Gets the kryo object that provides encoding and decoding services for this instance.
     *
     * @return Underlying Kryo instance.
     */
    public Kryo getKryo() {
        return kryo;
    }

    public static <T> T decode(final byte[] data, Class<T> classOfT) {
        Input input = new Input(data);
        return kryo.readObject(input, classOfT);
    }

    /**
     * Encodes the given object using registered Kryo serializers. Provides explicit buffer overflow protection, but
     * careful buffer sizing should be employed to reduce the need for this facility.
     *
     * @param o Object to encode.
     * @return Encoded bytes.
     */
    public static byte[] encodeToBytes(final Object o) {
        int factor = 1;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize * factor);
        Output output = new Output(new ByteBufferOutput(buffer));
//        kryo.writeObject(output, o);
//        output.flush();
//        output.close();
        output.getBuffer();

        byte[] result = null;

        while (result == null) {
            try {
                kryo.writeClassAndObject(output, o);
                result = new byte[buffer.flip().limit()];
                buffer.get(result);
            } catch (final SerializationException e) {
                Throwable rootCause = e;
                while (rootCause.getCause() != null) {
                    rootCause = rootCause.getCause();
                }
                if (rootCause instanceof BufferOverflowException) {
                    buffer = ByteBuffer.allocate(bufferSize * ++factor);
                    logger.warn("Buffer overflow while encoding {}", o);
                } else {
                    throw e;
                }
            }
        }

        System.out.println(result.length);
        return result;
    }
}
