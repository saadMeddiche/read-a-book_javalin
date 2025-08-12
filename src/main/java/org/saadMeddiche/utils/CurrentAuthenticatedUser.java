package org.saadMeddiche.utils;

import org.saadMeddiche.entities.User;

public class CurrentAuthenticatedUser {

    // This should be changed when implementing real authentication
    public static User retrieve() {
        User user = new User();
        user.id = 1L;
        user.firstName = "Bob";
        user.lastName = "Callahan";
        user.email = "BobCallahan@gmail.com";
        user.username = "BobCallahan";
        user.password = "[PROTECTED]";
        return user;
    }

}
