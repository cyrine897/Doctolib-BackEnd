package com.example.sahtyapp1.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "DossierMedical")

public class DossierMedical implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idDossier;
    @Lob
    private byte[] fichier;
    String description;

    @OneToOne(mappedBy = "dossierMedical")
    private Utilisateur utilisateur;
}
