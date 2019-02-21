package org.wimdeblauwe.biob.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wimdeblauwe.biob.BinaryObject;
import org.wimdeblauwe.biob.BinaryObjectMetadata;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryBinaryObjectStorageTest {

    private InMemoryBinaryObjectStorage storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryBinaryObjectStorage();
    }

    @Test
    void testStoreAndRetrieve() {
        BinaryObjectMetadata metadata = new BinaryObjectMetadata(3, "test.jpg", "image/jpg");
        storage.store("images/1", metadata, new ByteArrayInputStream(new byte[]{1, 2, 3}));

        assertThat(storage.retrieve("images/1"))
                .hasValueSatisfying(binaryObject ->
                                    {
                                        assertThat(binaryObject.getInputStream())
                                                .hasSameContentAs(new ByteArrayInputStream(new byte[]{1, 2, 3}));
                                        assertThat(binaryObject.getMetadata())
                                                .isEqualTo(new BinaryObjectMetadata(3, "test.jpg", "image/jpg"));
                                    });
    }

    @Test
    void testRetrieveIfNotKnown() {
        Optional<BinaryObject> optional = storage.retrieve("unknown/path/1");
        assertThat(optional).isEmpty();
    }

    @Test
    void testHasBinaryObject() {
        storage.store("images/1", new BinaryObjectMetadata(3, "test.jpg", "image/jpg"),
                      new ByteArrayInputStream(new byte[]{1, 2, 3}));

        assertThat(storage.hasBinaryObject("images/1")).isTrue();
    }

    @Test
    void testHasBinaryObjectIfNotKnown() {
        assertThat(storage.hasBinaryObject("unknown/path/1")).isFalse();
    }
}