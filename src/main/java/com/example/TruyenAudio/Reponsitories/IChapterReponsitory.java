package com.example.TruyenAudio.Reponsitories;

import com.example.TruyenAudio.Entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChapterReponsitory extends JpaRepository<Chapter, Integer> {


}
