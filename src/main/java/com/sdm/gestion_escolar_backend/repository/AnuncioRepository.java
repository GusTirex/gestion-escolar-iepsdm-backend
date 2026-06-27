package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Anuncio;

public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {
}
