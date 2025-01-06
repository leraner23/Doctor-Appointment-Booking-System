package project.Appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.Appointment.DTOmapping.UserMapper;
import project.Appointment.dto.CreateUserDto;
import project.Appointment.dto.LoginDto;
import project.Appointment.dto.UserLoginDto;
import project.Appointment.entity.Users;
import project.Appointment.enums.Role;
import project.Appointment.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

   @Autowired
   private UserMapper userMappingDTO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;



    public Users createNewUser(CreateUserDto dto) throws Exception {
        //check if user with given email or username already exists
Users user = new Users();
user.setFullName(dto.getFullName());
user.setRole(dto.getRole());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setContact(dto.getContact());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setUsername(dto.getUsername());
        user.setAddress(dto.getAddress());


//        if(user == null) throw new Exception("NO User available to create");
   String fileName = fileUploadService.saveFile(dto.getImage());

        System.out.println(fileName);
        user.setProfile(fileName);
   return userRepository.save(user);
    }


    public Users findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Users findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public Users deleteUserByID(long userId) throws Exception {
       Users user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new Exception("User not found to delete by this Id");
        userRepository.deleteById(userId);
        return user;
    }

    public Users updateUserById(Users oldUser, Users user) {
        oldUser.setFullName(user.getFullName());
        oldUser.setAge(user.getAge());
        oldUser.setContact(user.getContact());
        oldUser.setEmail(user.getEmail());
        oldUser.setGender(user.getGender());
        oldUser.setRole(user.getRole());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        return userRepository.save(oldUser);
    }

    public Users authenticate(LoginDto loginRequest) {
        System.out.println(loginRequest.getEmail());
        System.out.println(loginRequest.getPassword());

        Users user = userRepository.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        if (user == null) {
            throw new RuntimeException("Invalid credentials or role");
        }
        return user;
    }


    public Boolean isRoleExists(Role role) {
        return userRepository.existsByRole(role);
    }
}
