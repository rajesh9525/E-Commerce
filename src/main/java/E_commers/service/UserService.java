package E_commers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.User;
import E_commers.repo.UserRepository;


@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;

        public UserService(UserRepository userRepository) {
			this.userRepository = userRepository;
        }

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
	public User getUserById(Long id){
	    return userRepository.findById(id).orElse(null);
	}

	public static User findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
   
}
