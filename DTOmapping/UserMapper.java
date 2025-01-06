package project.Appointment.DTOmapping;

import org.springframework.stereotype.Component;
import project.Appointment.dto.UserLoginDto;
import project.Appointment.entity.Users;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserLoginDto mapToDto(Users user) {
        UserLoginDto userDto = new UserLoginDto();
        userDto.setUserId(user.getUserId());
        userDto.setFullname(user.getFullName());
        userDto.setAge(user.getAge());
        userDto.setContact(user.getContact());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setRole(user.getRole());
        userDto.setUserName(user.getUsername());
        userDto.setAddress(user.getAddress());
        return userDto;
    }


    public Users mapToEntity(UserLoginDto userDto) {
        Users user = new Users();
        user.setUserId(userDto.getUserId());
        user.setFullName(userDto.getFullname());
        user.setAge(userDto.getAge());
        user.setContact(userDto.getContact());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setRole(userDto.getRole());
        user.setUsername(userDto.getUserName());
        user.setAddress(userDto.getAddress());
        return user;
    }


    public List<UserLoginDto> mapToUserDtos(List<Users> users){
        List<UserLoginDto> userDTOs= new ArrayList<>();

        for(Users user: users){
            UserLoginDto userDTO= mapToDto(user);
            userDTOs.add(userDTO);
        }

        return userDTOs;
    }


}
