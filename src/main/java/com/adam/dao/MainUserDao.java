package com.adam.dao;

public class MainUserDao {

    public static void main(String[] args) {
        UserDao userDao = new UserDao(Environment.PRODUCTION);
        
        userDao.create(User.alice());
        userDao.create(User.john());
    }
    
}
