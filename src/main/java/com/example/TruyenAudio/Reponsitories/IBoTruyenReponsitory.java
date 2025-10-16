package com.example.TruyenAudio.Reponsitories;

import com.example.TruyenAudio.Entities.BoTruyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBoTruyenReponsitory extends JpaRepository<BoTruyen, Integer> {
    @org.springframework.data.jpa.repository.Query("""
  select distinct b
  from BoTruyen b
  left join fetch b.chapters
  left join fetch b.theLoais
  where b.BoTruyenId = :id
""")
    java.util.Optional<com.example.TruyenAudio.Entities.BoTruyen>
    findWithChaptersAndGenres(@org.springframework.data.repository.query.Param("id") Integer id);


}
