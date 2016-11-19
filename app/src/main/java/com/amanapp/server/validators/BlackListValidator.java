package com.amanapp.server.validators;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
class BlackListValidator implements Validator {

    //TODO: Make initBlackList in another class or any where else
    //TODO: Add more elements to the list and make it adaptable (easy to change and expandable)
    private List<String> initBlackList() {
        List<String> blackList = new LinkedList<>();
        blackList.add("Password123");
        return blackList;
    }

    @Override
    public boolean validate(String message) {
        List<String> blackList = initBlackList();
        return !blackList.contains(message);
    }

    @Override
    public String getErrorMessage() {
        return "the password is easy to guess";
    }


}
