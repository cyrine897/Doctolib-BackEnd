package com.example.sahtyapp1.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class SignUpRequest {

    private String username;
    private String email;
    private String password;

    @NotBlank
    @NotNull
    private String prenom;

    @NotBlank
    @NotNull
    private String nom;

    @NotBlank
    @NotNull
    @NotEmpty
    private String adresse;
    private String rolee ;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date_naissance;

    @NotBlank
    @NotNull
    private Long numero;
@NotEmpty
@NotBlank
    @NotNull
    @JsonProperty("role")
    private Set<String> roles;

    public SignUpRequest(String username, String email, String password, String prenom, String nom, String adresse, LocalDate  date_naissance, Long numero, Set<String> roles , String rolee) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
        this.adresse = adresse;
        this.date_naissance = date_naissance;
        this.numero = numero;
        this.roles = roles;
        this.rolee=rolee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate  getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(LocalDate  date_naissance) {
        this.date_naissance = date_naissance;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Set<String> getRole() {
        return roles;
    }

    public void setRole(Set<String> roles) {
        this.roles = roles;
    }

    public String getRolee() {
        return rolee;
    }

    public void setRolee(String rolee) {
        this.rolee = rolee;
    }
}
