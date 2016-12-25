package com.amanapp.application.core.logics;

/**
 * Created by Abdullah ALT on 11/20/2016.
 */
public class CurrentUser {
    private static User currentUser = new User();

    private CurrentUser() {
    }

    public static void set(String email) {
        currentUser.setCurrentUser(email.toLowerCase());
    }

    public static String get() {
        return currentUser.getCurrentUser();
    }

    private static class User {
        private String email;

        public String getCurrentUser() {
            return email;
        }

        public void setCurrentUser(String email) {
            this.email = email;
        }

    }
}
