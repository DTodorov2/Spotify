package bg.sofia.uni.fmi.mjt.server.repository;

import bg.sofia.uni.fmi.mjt.server.repository.stub.StringKeyBaseRepositoryImplStub;
import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.StringKeyBaseRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringKeyBaseRepositoryImplTest {

    private StringKeyBaseRepositoryImpl<IdentifiableModel> repository;
    private IdentifiableModel entity;
    private boolean result;
    private final String entityId = "entityId";

    @BeforeEach
    void setUp() {
        repository = new StringKeyBaseRepositoryImplStub();
        entity = mock(IdentifiableModel.class);
        when(entity.getId()).thenReturn(entityId);
    }

    @Test
    void testAddWithUniqueEntityId() {
        result = repository.add(entity);

        assertTrue(result, "True is expected when the entity id is unique!");
    }

    @Test
    void testAddWithExistingEntityId() {
        when(entity.isDuplicate(anyMap())).thenReturn(true);
        repository.add(entity);
        result = repository.add(entity);

        assertFalse(result, "False is expected when the entity id exist!");
    }

    @Test
    void testAddWithNullEntityId() {
        assertThrows(IllegalArgumentException.class, () -> repository.add(null),
                "IllegalArgumentException is expected to be thrown when trying to add null entity!");
    }

    @Test
    void testRemoveWithExistingEntity() {
        repository.add(entity);
        result = repository.remove(entityId);

        assertTrue(result, "True is expected when the entity id exists!");  // Елементът трябва да бъде премахнат
    }

    @Test
    void testRemoveWithNotExistingEntityId() {
        when(entity.isDuplicate(anyMap())).thenReturn(true);
        result = repository.remove(entityId);

        assertFalse(result, "False is expected when the entity id exist!");
    }

    @Test
    void testRemoveWithNullEntityId() {
        assertThrows(IllegalArgumentException.class, () -> repository.remove(null),
                "IllegalArgumentException is expected to be thrown when trying to add null entity!");
    }

}
