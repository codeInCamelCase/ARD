package in.ac.bits_pilani.goa.ard.models;

import org.junit.Test;

import in.ac.bits_pilani.goa.ard.utils.AHC;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for model User to ensure it's usable to use with firebase db.
 * @author Rushikesh Jogdand
 */

public class UserTest {
    @Test
    public void fieldNamesMatchFDR() throws NoSuchFieldException {
        assertNotNull(User.class.getDeclaredField(AHC.FDR_USERS_NAME));
        assertNotNull(User.class.getDeclaredField(AHC.FDR_USERS_EMAIL));
        assertNotNull(User.class.getDeclaredField(AHC.FDR_USERS_PHOTO_URL));
    }

    @Test
    public void emptyConstructorExists() throws NoSuchMethodException {
        assertNotNull(User.class.getDeclaredConstructor());
    }

    @Test
    public void settersExist() throws NoSuchMethodException {
        assertNotNull(User.class.getDeclaredMethod("setName", String.class));
        assertNotNull(User.class.getDeclaredMethod("setEmail", String.class));
        assertNotNull(User.class.getDeclaredMethod("setPhotoUrl", String.class));
    }
}
