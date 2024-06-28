package com.example.sahtyapp1.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "utilisateur",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Utilisateur implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;
    @NotBlank
    private String nom;
    @NotBlank

    private String username;
    @NotBlank

    private String prenom;
    @NotBlank

    private String adresse;
    @NotBlank

    private String email ;

    private String rolee ;

    @NotBlank

    private String password;
    private String resetToken;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")

    private LocalDate  date_naissance ;
    @NotBlank

    private Long numero ;


    public Utilisateur(String username, String email, String encode, String nom, String prenom, String adresse, LocalDate dateNaissance, Long numero, Set<Role> roles) {}

    public Utilisateur(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @JsonProperty("role")
    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role> roles = new HashSet<>();




    @ManyToMany
    @JoinTable(
            name = "utilisateur_message",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id")
    )
    private Set<Message> messages = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "utilisateur_evaluation",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "evaluation_id")
    )
    private Set<Evaluation> evaluations = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToOne
    @JoinColumn(name = "dossierMedical_id")
    private DossierMedical dossierMedical;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RDV> rdvs = new HashSet<>();
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Set<Ordonnance> ordonnances = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "ordonnance_medicament",
            joinColumns = @JoinColumn(name = "ordonnance_id"),
            inverseJoinColumns = @JoinColumn(name = "medicament_id"))
    private Set<Medicament> medicaments = new HashSet<>();


    public Utilisateur() {

    }
}
