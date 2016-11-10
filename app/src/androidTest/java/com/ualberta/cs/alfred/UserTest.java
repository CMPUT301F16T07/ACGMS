package com.ualberta.cs.alfred;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {

        super(MainActivity.class);
    }

    public void testGetFirstName() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Firstname not the same", u1FirstName.equals(u1.getFirstName()));
    }

    public void testGetLastName() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Lastname not the same", u1LastName.equals(u1.getLastName()));
    }

    public void testGetUserName() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Username not the same", u1UserName.equals(u1.getUserName()));
    }

    public void testGetDateOfBirth() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Date of birth not the same", u1DateOfBirth.equals(u1.getDateOfBirth()));
    }

    public void testGetPhoneNumber() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Phone number not the same", u1PhoneNumber.equals(u1.getPhoneNumber()));
    }

    public void testGetEmail() {

        String u1FirstName = "Donald";
        String u1LastName = "Trump";
        String u1UserName = "dtrump";

        // Create date
        GregorianCalendar gc = new GregorianCalendar(2001, Calendar.JUNE, 13);
        Date u1DateOfBirth = gc.getTime();

        String u1PhoneNumber = "780-444-5555";
        String u1Email = "email@example.com";

        User u1 = new User(u1FirstName, u1LastName, u1UserName, u1DateOfBirth, u1PhoneNumber, u1Email);

        assertTrue("Email not the same", u1Email.equals(u1.getEmail()));
    }


}
