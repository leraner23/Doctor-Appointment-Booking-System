package project.Appointment.Cookie;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Appointment.entity.Users;
import project.Appointment.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserIdGenerator {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void setup() {
        System.out.println("Created object!");

        if(userRepository == null) {
            System.out.println("userRepository is null");
        }
    }

    public static String sampleSpace = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String generateId() {
        boolean isUnique = false;
        Random random = new Random();
        String id = "";
        while (!isUnique) {
            id = "";
            for (int i = 1; i <= 20; i++) {
                int index = random.nextInt(sampleSpace.length());
                char c = sampleSpace.charAt(index);
                id = id + c;
            }

            System.out.println("Before existsByCookie()");
            //check if id is unique in the database
            if (!userRepository.existsByCookieId(id)) {
                isUnique = true;
            }
        }
        return id;
    }


//    private static List<String> ids = new ArrayList<>();

//    public static String sampleSpace = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//
//    public static String generateId() {
//        boolean isUnique = false;
//        Random random = new Random();
//        String id = "";
//        while (!isUnique) {
//            id = "";
//            for (int i = 1; i <= 20; i++) {
//                int index = random.nextInt(sampleSpace.length());
//                char c = sampleSpace.charAt(index);
//                id = id + c;
//            }
//            if (!ids.contains(id)) {
//                isUnique = true;
//                ids.add(id);
//            }
//        }
//        return id;
//    }


    public String setYourCookie(HttpServletResponse response) {
        String generatedId = generateId();
        Cookie cookie = new Cookie("userId", generatedId);
        System.out.println(generatedId);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(24000);
        response.addCookie(cookie);
        System.out.println("Cookie 'userId' set with value: " + generatedId);
        return generatedId;
    }

    public Users getAuthenticatedUser(HttpServletRequest request) {
        String session = getYourCookie(request);
        return userRepository.findByCookieId(session);
    }

    public String getYourCookie(HttpServletRequest request) {
        String userId = null;
        Cookie[] cookies = request.getCookies();
        System.out.println("cookies" + cookies);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                    System.out.println("get" + userId);
                    break;
                }
            }
            return userId;
        } else {
            System.out.println("cookie not found");
            return userId;
        }

    }

}