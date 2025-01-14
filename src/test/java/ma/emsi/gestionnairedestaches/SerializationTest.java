package ma.emsi.gestionnairedestaches;

import ma.emsi.gestionnairedestaches.model.User;

import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {

    @Test
    void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        // Arrange
        User user = new User("hamza@gmail.com", "123", "Hamza.Aitahmed");

        // Act: Serialize the user object
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(user);

        // Act: Deserialize the user object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        User deserializedUser = (User) objectInputStream.readObject();

        // Assert
        assertNotNull(deserializedUser, "The deserialized user should not be null");
        assertEquals(user.getEmail(), deserializedUser.getEmail(), "Emails should match");
        assertEquals(user.getPassword(), deserializedUser.getPassword(), "Passwords should match");
        assertEquals(user.getUsername(), deserializedUser.getUsername(), "Usernames should match");
    }

    @Test
    void testSerializationThrowsExceptionForNonSerializableObject() {
        // Arrange
        class NonSerializable {
            String data = "test";
        }
        NonSerializable nonSerializableObject = new NonSerializable();

        // Act & Assert
        assertThrows(NotSerializableException.class, () -> {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(nonSerializableObject);
        }, "Should throw NotSerializableException for non-serializable objects");
    }
}