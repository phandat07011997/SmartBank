package develop.smartbank.service;

import develop.smartbank.database.repository.UserRepository;
import develop.smartbank.domain.dto.AuthResponse;
import develop.smartbank.domain.dto.LoginRequest;
import develop.smartbank.domain.dto.RegisterRequest;
import develop.smartbank.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email đã được sử dụng", null);
        }
        
        // Kiểm tra số điện thoại đã tồn tại
        if (userRepository.existsByPhone(request.getPhone())) {
            return new AuthResponse(false, "Số điện thoại đã được sử dụng", null);
        }
        
        // Tạo user mới
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        // Tạo response
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            savedUser.getUserId(),
            savedUser.getFullName(),
            savedUser.getEmail(),
            savedUser.getPhone()
        );
        
        return new AuthResponse(true, "Đăng ký thành công", userInfo);
    }
    
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            return new AuthResponse(false, "Email hoặc mật khẩu không đúng", null);
        }
        
        User user = userOptional.get();
        
        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return new AuthResponse(false, "Email hoặc mật khẩu không đúng", null);
        }
        
        // Tạo response
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            user.getUserId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone()
        );
        
        return new AuthResponse(true, "Đăng nhập thành công", userInfo);
    }
}

