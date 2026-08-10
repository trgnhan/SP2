package com.nhan.sp2.service.impl;

import com.nhan.sp2.common.util.UserStatus;
import com.nhan.sp2.dto.request.UserPasswordRequest;
import com.nhan.sp2.dto.request.UserRequest;
import com.nhan.sp2.dto.response.PageResponse;
import com.nhan.sp2.dto.response.UserResponse;
import com.nhan.sp2.exception.ResourceNotFoundException;
import com.nhan.sp2.model.Address;
import com.nhan.sp2.model.User;
import com.nhan.sp2.repository.AddressRepository;
import com.nhan.sp2.repository.UserRepository;
import com.nhan.sp2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUser(Long userId) {
        log.info("Get user by id: {}", userId);

        User userById = getUserById(userId);

        return UserResponse.builder()
                .id(userById.getId())
                .firstName(userById.getFirstName())
                .lastName(userById.getLastName())
                .gender(userById.getGender())
                .birthday(userById.getBirthday())
                .username(userById.getUsername())
                .email(userById.getEmail())
                .phone(userById.getPhone())
                .build();
    }

    @Override
    public PageResponse<?> getListUser(String keyword, String sort, int pageNo, int pageSize) {

        // Sorting

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if(StringUtils.hasLength(sort)){
            // asc/ desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // asc/desc
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columName);
                }else {
                    order = new Sort.Order(Sort.Direction.DESC, columName);
                }
            }

        }
        // Pating
        Page<User> users ;

        // xu ly neu FE muon bat dau la 1
        int pageno = 0;
        if (pageNo>0){
            pageno = pageNo-1;
        }

        Pageable pageable = PageRequest.of(pageno, pageSize, Sort.by(order));

        // Searching
        if(StringUtils.hasLength(keyword)){
            // goi search method
            keyword = "%"+keyword.toLowerCase()+"%";
            users =  userRepository.searchByKeyword(keyword,pageable);
        }else {
            users = userRepository.findAll(pageable);
        }

        return getPageResponse(users, pageable);
    }

    private static PageResponse<?> getPageResponse(Page<User> users, Pageable pageable) {
        List<UserResponse> userList = users.stream().map(user -> UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPages(users.getTotalPages())
                .items(userList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addUser(UserRequest userRequest) {
        log.info("Adding user: {}", userRequest);
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setGender(userRequest.getGender());
        user.setBirthday(userRequest.getBirthday());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setUsername(userRequest.getUsername());
        user.setType(userRequest.getType());
        // khi moi tao thi none -> confirm thi none -> active
        user.setStatus(UserStatus.NONE);
        userRepository.save(user);

        if (user.getId()!= null){
            List<Address> addresses = new ArrayList<>();
            userRequest.getAddress().forEach(address -> {
                Address addressEntity = new Address();
                addressEntity.setApartmentNumber(address.getApartmentNumber());
                addressEntity.setFloor(address.getFloor());
                addressEntity.setBuilding(address.getBuilding());
                addressEntity.setStreetNumber(address.getStreetNumber());
                addressEntity.setStreet(address.getStreet());
                addressEntity.setCity(address.getCity());
                addressEntity.setCountry(address.getCountry());
                addressEntity.setAddressType(address.getAddressType());
                addressEntity.setUserId(user.getId());
                addresses.add(addressEntity);
            });
            addressRepository.saveAll(addresses);
            log.info("Address save: {}, from userId :{}", addresses, user.getId());
        }
        log.info("User added , userId: {}", user.getId());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserRequest userRequest) {
        log.info("Updating user: {}", userRequest);
        // get user by id
        User user = getUserById(userRequest.getId());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setGender(userRequest.getGender());
        user.setBirthday(userRequest.getBirthday());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setUsername(userRequest.getUsername());
        userRepository.save(user);
        log.info("User updated , user: {}", user);

        // save address
        List<Address> addresses = new ArrayList<>();
        userRequest.getAddress().forEach(address -> {
            Address addressEntity = addressRepository.findByUserIdAndAddressType(user.getId(), address.getAddressType());
            if (addressEntity == null) {
                addressEntity = new Address();

            }
            addressEntity.setApartmentNumber(address.getApartmentNumber());
            addressEntity.setFloor(address.getFloor());
            addressEntity.setBuilding(address.getBuilding());
            addressEntity.setStreetNumber(address.getStreetNumber());
            addressEntity.setStreet(address.getStreet());
            addressEntity.setCity(address.getCity());
            addressEntity.setCountry(address.getCountry());
            addressEntity.setAddressType(address.getAddressType());
            addressEntity.setUserId(user.getId());
            addresses.add(addressEntity);
        });
        addressRepository.saveAll(addresses);
        log.info("Save address : {}", addresses);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user: {}", userId);
        User user = getUserById(userId);
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
        log.info("User deleted , user: {}", user);

    }

    @Override
    public void changePasswordUser(UserPasswordRequest userPasswordRequest) {
        log.info("Changing password for user: {}", userPasswordRequest.getId());

        // Get user by id
        User  user = getUserById(userPasswordRequest.getId());

        if (userPasswordRequest.getPassword().equals(userPasswordRequest.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(userPasswordRequest.getPassword()));
        }

        userRepository.save(user);
        log.info("Password changed, user :{}", user);
    }


    /**
     * get user by id
     * @param userId
     * @return
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
    }

}
