package com.example.sahtyapp1.ServiceImpl;

import com.example.sahtyapp1.Entity.Utilisateur;
import com.example.sahtyapp1.Repository.UtilisateurRepo;
import com.example.sahtyapp1.SeviceInterf.UtilisateurDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilisateurDetailServiceImpl implements UtilisateurDetailService, UserDetailsService {

    @Autowired
    UtilisateurRepo utilisateurRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UtilisateurDetailsImpl.build(utilisateur);

    }



    @Autowired
    private EmailService emailService;

    public void generateResetToken(String email) {
        Utilisateur user = utilisateurRepo.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            utilisateurRepo.save(user);
            emailService.sendResetToken(email, token);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        Utilisateur user = utilisateurRepo.findByResetToken(token);
        if (user != null) {
            user.setPassword(newPassword); // Vous devriez hacher le mot de passe ici
            user.setResetToken(null);
            utilisateurRepo.save(user);
            return true;
        }
        return false;
    }
}
