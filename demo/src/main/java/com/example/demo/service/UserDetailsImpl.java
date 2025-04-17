package com.example.demo.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;

@Service
public class UserDetailsImpl implements UserDetailsService {

  private UserRepo userRepo;

  private RoleRepo roleRepo;

  private BCryptPasswordEncoder encoder;

  @Autowired
  public UserDetailsImpl(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.encoder = encoder;

  }
  @Override
  public User findByUsername(String username) {
    return userRepo.findByUsername(username).get();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User tempUser = findByUsername(username);

    if (tempUser == null) {
      throw new UsernameNotFoundException("USERNAME İS NOT FOUND...");
    }
    return new org.springframework.security.core.userdetails.User(tempUser.getUsername(), tempUser.getPassword(),
        mapToAuthorities(tempUser.getRoles()));

  }

  @Override
  public User saveUser(User user) {
    // Şifreyi hashle
    String hashedPassword = encoder.encode(user.getPassword());
    user.setPassword(hashedPassword);
    user.setEnabled(true);

    // Roller var mı kontrol et
    if (user.getRoles() != null) {
      Set<Role> updatedRoles = new HashSet<>();

      // Rolleri kontrol et ve güncelle
      user.getRoles().forEach(role -> {
        if (role.getId() != null) {
          // Var olan bir rolü al
          Optional<Role> existingRole = roleRepo.findByName(role.getName());
          if (existingRole.isPresent()) {
              updatedRoles.add(existingRole.get()); // Var olan rolü ekle
          }
        } else {
          // Yeni bir rolse, oluştur
          Optional<Role> existingRole = roleRepo.findByName(role.getName());
          if (existingRole.isPresent()) {
            updatedRoles.add(existingRole.get()); // Var olan rolü ekle
          } else {
            // Eğer yeni rolse, kaydet
            Role newRole = roleRepo.save(role );
            updatedRoles.add(newRole);
          }
        }
      });

      // Kullanıcıya güncellenmiş roller setini ata
      user.setRoles(updatedRoles);
    }

    // Kullanıcıyı kaydet
    return userRepo.save(user);
  }

  @Override
  public User saveDefaultUser(User user) {

    // Varsayılan olarak USER rolünü atıyoruz
    Role userRole = roleRepo.findByName("ROLE_USER").get();
    if (userRole == null) {
      // Eğer ROLE_USER veritabanında yoksa, oluştur ve kaydet
      userRole = new Role();
      userRole.setName("ROLE_USER");
      roleRepo.save(userRole);
    }

    // Kullanıcıya sadece USER rolünü ata
    user.setRoles(Set.of(userRole));

    // Parolayı hashleyerek kaydet
    String hashedPassword = encoder.encode(user.getPassword());
    user.setPassword(hashedPassword);
    user.setEnabled(true);
    return userRepo.save(user);
  }

  private Collection<? extends GrantedAuthority> mapToAuthorities(Collection<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
  }

}
