package com.joelallison.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class User {
    //note: there's other user metadata that I am storing in my database (and intend to include here at some point), but that would come at a much later date.
    String username;
    Creation[] creations;
    
}
