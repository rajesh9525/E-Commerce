package E_commers.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.User;
import E_commers.model.UserActivity;
import E_commers.repo.ActivityRepository;
import E_commers.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    // ⚠️ Removed manual constructor — it was preventing @Autowired from injecting activityRepository

    public User findByEmail1(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public static User findByEmail(String email) {
        return null;
    }

    // ===== RECORD USER ACTIVITY =====
    public void recordActivity(String username, String action) {
        try {
            UserActivity activity = new UserActivity();
            activity.setUsername(username);
            activity.setAction(action);
            activity.setTime(LocalDate.now());
            activityRepository.save(activity);
        } catch (Exception e) {
            // Don't crash the login if activity recording fails
            System.err.println("Activity recording failed: " + e.getMessage());
        }
    }
}


